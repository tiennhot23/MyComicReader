package com.e.mycomicreader.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.e.mycomicreader.R;
import com.e.mycomicreader.adapters.OfflineChapterAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wajahatkarim3.easyflipviewpager.BookFlipPageTransformer;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OfflineChapterActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private int position;
    private FloatingActionButton more, next, prev;
    private Boolean isMoreClicked = true;
    private List<File> files, list_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_chapter);

        position = getIntent().getIntExtra("position", 0);
        files = (List<File>) getIntent().getSerializableExtra("list_file");
        viewPager = findViewById(R.id.view_pager);
        more = findViewById(R.id.more);
        next = findViewById(R.id.next);
        prev = findViewById(R.id.previous);
//        recycler.setLayoutManager(new LinearLayoutManager(this));
        list_image = new ArrayList<>();
        list_image = Arrays.asList(files.get(position).listFiles());
        Collections.sort(list_image);

        fetchOfflineChapter();

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibleButtons();
                setAnimateButtons();
                isMoreClicked = !isMoreClicked;
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position + 1 == files.size()){
                    return;
                }
                Intent intent = new Intent(OfflineChapterActivity.this, OfflineChapterActivity.class);
                intent.putExtra("position", position + 1);
                intent.putExtra("list_file", (Serializable) files);
                startActivity(intent);
                finish();
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position - 1 < 0){
                    return;
                }
                Intent intent = new Intent(OfflineChapterActivity.this, OfflineChapterActivity.class);
                intent.putExtra("position", position - 1);
                intent.putExtra("list_file", (Serializable) files);
                startActivity(intent);
                finish();
            }
        });
    }

    private void fetchOfflineChapter(){
        BookFlipPageTransformer bookFlipPageTransformer = new BookFlipPageTransformer();
        bookFlipPageTransformer.setScaleAmountPercent(10f);
        viewPager.setAdapter(new OfflineChapterAdapter(this, list_image));
        viewPager.setPageTransformer(new ZoomOutPageTransformer());
    }

    private void setVisibleButtons(){
        if(isMoreClicked){
            next.setVisibility(View.VISIBLE);
            prev.setVisibility(View.VISIBLE);
        }else{
            next.setVisibility(View.INVISIBLE);
            prev.setVisibility(View.INVISIBLE);
        }
    }

    private void setAnimateButtons(){
        if(isMoreClicked){
            Animation anim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.anim_scale_out);
            next.startAnimation(anim);
            prev.startAnimation(anim);
        }else{
            Animation anim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.anim_scale_in);
            next.startAnimation(anim);
            prev.startAnimation(anim);
        }
    }

    public class ZoomOutPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {

            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }
}