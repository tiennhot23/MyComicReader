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
import com.e.mycomicreader.models.Comic;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ComicAdapter2 extends RecyclerView.Adapter<ComicAdapter2.ViewHolder>{

    Context context;
    List<Comic> comics = new ArrayList<>();

    public ComicAdapter2() {
    }

    public ComicAdapter2(Context context, List<Comic> comics) {
        this.context = context;
        this.comics = comics;
    }

    @NotNull
    @Override
    public ComicAdapter2.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.comic_item_2, parent, false);
        return new ComicAdapter2.ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NotNull ComicAdapter2.ViewHolder holder, int position) {
        if((position + 1)/2 == 0){
            Picasso.get().load(comics.get(position).thumb).into(holder.thumb);
        }else{
            Picasso.get().load(comics.get(position).thumb).into(holder.thumb2);
        }

        Picasso.get().load(comics.get(position).thumb).into(holder.background);
    }

    @Override
    public int getItemCount() {
        return comics.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView thumb, thumb2, background;
        TextView title_comic, genres, desc;
        public ViewHolder(@NotNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.thumb);
            title_comic = itemView.findViewById(R.id.title_comic);
            thumb2 = itemView.findViewById(R.id.thumb2);
            genres = itemView.findViewById(R.id.genres);
            desc = itemView.findViewById(R.id.desc);
            background = itemView.findViewById(R.id.background);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            } );

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(context, "Press", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }
    }
}
