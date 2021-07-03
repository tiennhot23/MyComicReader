package com.e.mycomicreader.adapters;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.e.mycomicreader.R;
import com.e.mycomicreader.models.Genre;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {
    private Context context;
    private List<Genre> genres ,selected_genres;
    SparseBooleanArray itemStateArray = new SparseBooleanArray();
    public GenreAdapter() {
    }

    public GenreAdapter(Context context, List<Genre> genres) {
        this.context = context;
        this.genres = genres;
        selected_genres = new ArrayList<>();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.check_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.check_options.setText(genres.get(position).genre_name);
        holder.check_options.setChecked(itemStateArray.get(position));
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    public String getFilterArray(){
        List<String> id_selected = new ArrayList<>();
        for(Genre genre: selected_genres){
            id_selected.add(genre.genre_name);
        }
        return  new Gson().toJson(id_selected);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox check_options;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            check_options = itemView.findViewById(R.id.check_options);
            check_options.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int adapterPosition = getBindingAdapterPosition();
                    itemStateArray.put(adapterPosition, isChecked);
                    if(isChecked){
                        selected_genres.add(genres.get(adapterPosition));
                    }else{
                        selected_genres.remove(genres.get(adapterPosition));
                    }
                }
            });
        }
    }
}
