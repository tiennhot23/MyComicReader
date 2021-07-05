package com.e.mycomicreader.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.e.mycomicreader.Common.IRecylerClickListener;
import com.e.mycomicreader.R;
import com.e.mycomicreader.models.Chapter;
import com.e.mycomicreader.models.DetailComic;
import com.e.mycomicreader.views.ChapterActivity;
import com.e.mycomicreader.views.OfflineChapterActivity;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class DetailComicAdapter extends RecyclerView.Adapter<DetailComicAdapter.ViewHolder>{
    Context context;
    List<Chapter> chapter_list;
    DetailComic detailComic;
    IRecylerClickListener recylerClickListener;
    List<File> files;
    Boolean offline = false;

    public DetailComicAdapter() {
    }

    public DetailComicAdapter(Context context, List<Chapter> chapter_list, DetailComic detailComic, IRecylerClickListener recylerClickListener) {
        this.context = context;
        this.chapter_list = chapter_list;
        this.detailComic = detailComic;
        this.recylerClickListener = recylerClickListener;
        offline = false;
    }

    public DetailComicAdapter(Context context, List<File> files, IRecylerClickListener recylerClickListener) {
        this.context = context;
        this.files = files;
        this.recylerClickListener = recylerClickListener;
        offline = true;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.chapter_item, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        if(offline){
            holder.chapter_uploaded.setText("");
            holder.chapter_title.setText(files.get(position).getName());
        }else{
            holder.chapter_title.setText(chapter_list.get(position).chapter_title);
            holder.chapter_uploaded.setText(chapter_list.get(position).chapter_uploaded);
        }
        holder.btn_check.setImageResource(R.drawable.ic_uncheck);
        holder.btn_check.setTag(R.drawable.ic_uncheck);
    }

    @Override
    public int getItemCount() {
        if(offline) return files.size();
        else return chapter_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView chapter_title, chapter_uploaded;
        ImageView btn_check;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            chapter_title = itemView.findViewById(R.id.chapter_title);
            chapter_uploaded = itemView.findViewById(R.id.chapter_uploaded);
            btn_check = itemView.findViewById(R.id.btn_check);
            btn_check.setTag(R.drawable.ic_uncheck);

            int uncheck_id = (int) btn_check.getTag();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(offline){
                        Intent intent = new Intent(context, OfflineChapterActivity.class);
                        intent.putExtra("position", getBindingAdapterPosition());
                        intent.putExtra("list_file", (Serializable) files);
                        context.startActivity(intent);
                    }else{
                        Intent intent = new Intent(context, ChapterActivity.class);
                        intent.putExtra("position", getBindingAdapterPosition());
                        intent.putExtra("chapter_list", (Serializable) chapter_list);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });

            btn_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(((int) btn_check.getTag()) == uncheck_id){
                        btn_check.setImageResource(R.drawable.ic_check);
                        btn_check.setTag(R.drawable.ic_check);
                        recylerClickListener.onCheckBoxClick(getBindingAdapterPosition(), "check");
                    }else{
                        btn_check.setImageResource(R.drawable.ic_uncheck);
                        btn_check.setTag(R.drawable.ic_uncheck);
                        recylerClickListener.onCheckBoxClick(getBindingAdapterPosition(), "uncheck");
                    }

                }
            });
        }
    }
}
