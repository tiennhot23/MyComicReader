package com.e.mycomicreader.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.e.mycomicreader.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//public class OfflineChapterAdapter extends PagerAdapter {
//
//    Context context;
//    List<File> list_image;
//
//    public OfflineChapterAdapter() {
//    }
//
//    public OfflineChapterAdapter(Context context, List<File> list_image) {
//        this.context = context;
//        this.list_image = list_image;
//    }
//
//    @Override
//    public int getCount() {
//        return list_image.size();
//    }
//
//    @Override
//    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
//        return view.equals(0);
//    }
//
//    @Override
//    public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
//        container.removeView((View) object);
//    }
//
//    @NonNull
//    @NotNull
//    @Override
//    public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
//        View view = LayoutInflater.from(context).inflate(R.layout.chapter_image_item, container, false);
//        PhotoView chapter_image = view.findViewById(R.id.chapter_image);
//        Picasso.get().load(list_image.get(position)).into(chapter_image);
//        container.addView(view);
//        System.out.println(list_image.get(position).getAbsolutePath());
//        return view;
//    }
//}


public class OfflineChapterAdapter extends RecyclerView.Adapter<OfflineChapterAdapter.ViewHolder>{

    Context context;
    List<File> list_image;

    public OfflineChapterAdapter() {
    }

    public OfflineChapterAdapter(Context context, List<File> list_image) {
        this.context = context;
        this.list_image = list_image;
        Collections.sort(this.list_image, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o1.getName().substring(0,o1.getName().length()-5).compareTo(o2.getName().substring(0,o2.getName().length()-5));
            }
        });
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.chapter_image_item, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        Picasso.get().load(list_image.get(position)).into(holder.chapter_image);
    }

    @Override
    public int getItemCount() {
        return list_image.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        PhotoView chapter_image;
        public ViewHolder(@NotNull View itemView) {
            super(itemView);
            chapter_image = itemView.findViewById(R.id.chapter_image);
        }
    }
}