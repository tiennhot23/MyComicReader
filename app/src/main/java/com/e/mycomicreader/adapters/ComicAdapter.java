package com.e.mycomicreader.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.e.mycomicreader.R;
import com.e.mycomicreader.entity.FollowedComic;
import com.e.mycomicreader.models.Comic;
import com.e.mycomicreader.views.MainActivity;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ViewHolder>{
    Context context;
    List<Comic> comics = new ArrayList<>();
    public ComicAdapter() {
    }

    public ComicAdapter(Context context, List<Comic> comics) {
        this.context = context;
        this.comics = comics;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.comic_item, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        Picasso.get().load(comics.get(position).thumb).into(holder.thumb);
        holder.btn_follow.setTag(position);
    }

    @Override
    public int getItemCount() {
        return comics.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView thumb;
        TextView title_comic, title_chapter, rating;
        ImageView btn_follow;
        Toast toast = null;
        public ViewHolder(@NotNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.thumb);
            title_comic = itemView.findViewById(R.id.title_comic);
            title_chapter = itemView.findViewById(R.id.title_chapter);
            rating = itemView.findViewById(R.id.rating);
            btn_follow = itemView.findViewById(R.id.btn_follow);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            } );

            btn_follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(toast != null) toast.cancel();
                    if(btn_follow.getDrawable().getConstantState().equals(context.getResources().getDrawable(R.drawable.ic_mark).getConstantState())){
                        btn_follow.setImageResource(R.drawable.ic_marked);
                        toast = Toast.makeText(context, "Followed", Toast.LENGTH_SHORT);
                        toast.show();
                        MainActivity.followedComicViewModel.insert(new FollowedComic(comics.get((Integer) btn_follow.getTag()).endpoint));
                    }else{
                        btn_follow.setImageResource(R.drawable.ic_mark);
                        toast = Toast.makeText(context, "Unollowed", Toast.LENGTH_SHORT);
                        toast.show();
                        MainActivity.followedComicViewModel.delete(new FollowedComic(comics.get((Integer) btn_follow.getTag()).endpoint));
                    }
                }
            });
        }
    }
}
