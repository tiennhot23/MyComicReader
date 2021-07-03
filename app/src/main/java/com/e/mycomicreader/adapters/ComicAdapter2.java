package com.e.mycomicreader.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.e.mycomicreader.R;
import com.e.mycomicreader.fragments.MyBottomSheetFragement;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class ComicAdapter2 extends RecyclerView.Adapter<ComicAdapter2.ViewHolder>{

    Context context;
    File[] files;
    MyBottomSheetFragement bottomSheetFragement;
    public ComicAdapter2() {
    }

    public ComicAdapter2(Context context, File[] files) {
        this.context = context;
        this.files = files;
    }

    @NotNull
    @Override
    public ComicAdapter2.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.comic_item_2, parent, false);
        return new ComicAdapter2.ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NotNull ComicAdapter2.ViewHolder holder, int position) {
        File thumbFile = new File(files[position].getAbsolutePath() + "/info/thumb.jpg");
        File themeFile = new File(files[position].getAbsolutePath() + "/info/theme.jpg");
        if((position + 1)/2 == 0){
            Picasso.get().load(thumbFile).into(holder.thumb);
        }else{
            Picasso.get().load(thumbFile).into(holder.thumb2);
        }
        Picasso.get().load(themeFile).into(holder.background);

        File genreFile = new File(files[position].getAbsolutePath() + "/info/genre.txt");
        File descFile = new File(files[position].getAbsolutePath() + "/info/desc.txt");
        File titleFile = new File(files[position].getAbsolutePath() + "/info/title.txt");

        try {
            String genre = null;
            BufferedReader br = new BufferedReader(new FileReader(genreFile));
            genre = br.readLine();
            holder.genres.setText(genre);
            String desc = null;
            br = new BufferedReader(new FileReader(descFile));
            desc = br.readLine();
            holder.desc.setText(desc);
            String title = null;
            br = new BufferedReader(new FileReader(titleFile));
            title = br.readLine();
            holder.title_comic.setText(title);
        } catch (IOException e) {
            Toast.makeText(context, "error while reading file", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        if(files == null) return 0;
        return files.length;
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
                    bottomSheetFragement = new MyBottomSheetFragement(context, files[getBindingAdapterPosition()].listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            return !name.equals("info");
                        }
                    }));
                    bottomSheetFragement.show(((FragmentActivity)context).getSupportFragmentManager(), bottomSheetFragement.getTag());
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
