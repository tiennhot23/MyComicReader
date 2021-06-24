package com.e.mycomicreader.views;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.e.mycomicreader.R;
import com.e.mycomicreader.Retrofit.IComicAPI;
import com.e.mycomicreader.adapters.ComicAdapter;
import com.e.mycomicreader.adapters.MainViewPagerAdapter;
import com.e.mycomicreader.fragments.FollowedFragment;
import com.e.mycomicreader.fragments.HomeFragment;
import com.e.mycomicreader.fragments.LibraryFragment;
import com.e.mycomicreader.fragments.SearchFragment;
import com.e.mycomicreader.models.Comic;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.util.List;

public class MainActivity extends FragmentActivity  {
    private ViewPager2 viewPager;
    private MainViewPagerAdapter mainViewPagerAdapter;
    private MeowBottomNavigation bottomNavigation;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(R.color.background_color);
        }

//        startSplashScreen();
        UI();




    }




    public void UI(){
        bottomNavigation = this.findViewById(R.id.bottom_navigation);
        bottomNavigation.show(1, true);
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_search));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_library));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_followed));

        viewPager = this.findViewById(R.id.view_pager);
        mainViewPagerAdapter = new MainViewPagerAdapter(this, bottomNavigation);
        mainViewPagerAdapter.addFragment(new HomeFragment(), "Home");
        mainViewPagerAdapter.addFragment(new SearchFragment(), "Search");
        mainViewPagerAdapter.addFragment(new LibraryFragment(), "Library");
        mainViewPagerAdapter.addFragment(new FollowedFragment(), "Followed");
        viewPager.setAdapter(mainViewPagerAdapter);
        viewPager.setPageTransformer(new ZoomOutPageTransformer());
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                bottomNavigation.show(position + 1, true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });




        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {

            }
        });
        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                switch (item.getId()){
                    case 1:{
                        viewPager.setCurrentItem(0);
                        break;
                    }
                    case 2:{
                        viewPager.setCurrentItem(1);
                        break;
                    }
                    case 3:{
                        viewPager.setCurrentItem(2);
                        break;
                    }
                    case 4:{
                        viewPager.setCurrentItem(3);
                        break;
                    }
                }
            }
        });
        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {

            }
        });
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