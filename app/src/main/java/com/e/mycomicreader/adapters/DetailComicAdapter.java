package com.e.mycomicreader.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.e.mycomicreader.R;
import com.e.mycomicreader.models.Chapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DetailComicAdapter extends RecyclerView.Adapter<DetailComicAdapter.ViewHolder>{
    Context context;
    List<Chapter> chapter_list;

    public DetailComicAdapter() {
    }

    public DetailComicAdapter(Context context, List<Chapter> chapter_list) {
        this.context = context;
        this.chapter_list = chapter_list;
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
        holder.chapter_title.setText(chapter_list.get(position).chapter_title);
        holder.chapter_uploaded.setText(chapter_list.get(position).chapter_uploaded);
    }

    @Override
    public int getItemCount() {
        return chapter_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView chapter_title, chapter_uploaded;
        ImageView btn_download;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            chapter_title = itemView.findViewById(R.id.chapter_title);
            chapter_uploaded = itemView.findViewById(R.id.chapter_uploaded);
            btn_download = itemView.findViewById(R.id.btn_download);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, chapter_list.get(getBindingAdapterPosition()).chapter_title, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
