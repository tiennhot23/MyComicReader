package com.e.mycomicreader.views;

import android.content.Intent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.*;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.e.mycomicreader.R;
import com.e.mycomicreader.adapters.MainViewPagerAdapter;
import com.e.mycomicreader.fragments.FollowedFragment;
import com.e.mycomicreader.fragments.HomeFragment;
import com.e.mycomicreader.fragments.LibraryFragment;
import com.e.mycomicreader.fragments.SearchFragment;
import org.jetbrains.annotations.NotNull;

public class MainActivity extends FragmentActivity  {
    private static final int NUM_PAGES = 4;

    private ViewPager2 viewPager;
    private MainViewPagerAdapter mainViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        startSplashScreen();

        viewPager = this.findViewById(R.id.view_pager);
        mainViewPagerAdapter = new MainViewPagerAdapter(this);
        mainViewPagerAdapter.addFragment(new HomeFragment(), "Home");
        mainViewPagerAdapter.addFragment(new SearchFragment(), "Search");
        mainViewPagerAdapter.addFragment(new LibraryFragment(), "Library");
        mainViewPagerAdapter.addFragment(new FollowedFragment(), "Followed");
        viewPager.setAdapter(mainViewPagerAdapter);
        viewPager.setPageTransformer(new ZoomOutPageTransformer());


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

    private void startSplashScreen() {
        Intent intent = new Intent(this, SplashScreen.class);
        startActivity(intent);
    }
}