package com.e.mycomicreader.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.e.mycomicreader.views.DetailComicActivity;
import com.e.mycomicreader.views.MainActivity;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ComicAdapter3 extends RecyclerView.Adapter<ComicAdapter3.ViewHolder>{
    Context context;
    List<Comic> comics = new ArrayList<>();
    HashMap<String, Boolean> isFollowed = new HashMap<>();

    public ComicAdapter3() {
    }

    public ComicAdapter3(Context context, List<Comic> comics, HashMap<String, Boolean> isFollowed) {
        this.context = context;
        this.comics = comics;
        this.isFollowed = isFollowed;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.comic_item_3, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        Picasso.get().load(comics.get(position).thumb).into(holder.thumb);
        if(isFollowed.containsKey(comics.get(position).endpoint))
            holder.btn_follow.setImageResource(R.drawable.ic_marked);
        else
            holder.btn_follow.setImageResource(R.drawable.ic_mark);
        holder.btn_follow.setTag(position);
        holder.title_comic.setText(comics.get(position).title);
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<comics.get(position).genre_list.size(); i++){
            if(i==0) sb.append(comics.get(position).genre_list.get(i).genre_name);
            else sb.append(", ").append(comics.get(position).genre_list.get(i).genre_name);
        }
        holder.genres.setText(sb.toString());
        holder.rating.setText(comics.get(position).rating);
        holder.chapter.setText(comics.get(position).chapter);
    }

    @Override
    public int getItemCount() {
        return comics.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView thumb;
        TextView title_comic, chapter, rating, genres;
        ImageView btn_follow;
        Toast toast = null;
        public ViewHolder(@NotNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.thumb);
            title_comic = itemView.findViewById(R.id.title_comic);
            chapter = itemView.findViewById(R.id.chapter);
            genres = itemView.findViewById(R.id.genres);
            rating = itemView.findViewById(R.id.rating);
            btn_follow = itemView.findViewById(R.id.btn_follow);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailComicActivity.class);
                    intent.putExtra("endpoint", comics.get(getBindingAdapterPosition()).endpoint);
                    context.startActivity(intent);
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
                        MainActivity.followedComicViewModel.insert(new FollowedComic(comics.get(getBindingAdapterPosition()).endpoint));
                        if(!isFollowed.containsKey(comics.get((Integer) btn_follow.getTag()).endpoint)){
                            isFollowed.put(comics.get((Integer) btn_follow.getTag()).endpoint, true);
//                            FollowedFragment.listFollowedComic.add(comics.get((Integer) btn_follow.getTag()));
                        }
                    }else{
                        btn_follow.setImageResource(R.drawable.ic_mark);
                        toast = Toast.makeText(context, "Unollowed", Toast.LENGTH_SHORT);
                        toast.show();
                        MainActivity.followedComicViewModel.delete(new FollowedComic(comics.get(getBindingAdapterPosition()).endpoint));
                        if(isFollowed.containsKey(comics.get((Integer) btn_follow.getTag()).endpoint)){
                            isFollowed.remove(comics.get((Integer) btn_follow.getTag()).endpoint);
//                            FollowedFragment.listFollowedComic.remove(comics.get((Integer) btn_follow.getTag()));
                        }
                    }
                }
            });
        }
    }
}
