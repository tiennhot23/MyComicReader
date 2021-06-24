package com.e.mycomicreader.adapters;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.e.mycomicreader.views.MainActivity;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainViewPagerAdapter extends FragmentStateAdapter {

    private ArrayList<Fragment> arrayFragment = new ArrayList<>();
    private ArrayList<String> arrayTitle = new ArrayList<>();

    MeowBottomNavigation bottomNavigation;

    public MainViewPagerAdapter(@NonNull @NotNull FragmentActivity fragmentActivity, MeowBottomNavigation bn) {
        super(fragmentActivity);
        this.bottomNavigation = bn;
    }

    public MainViewPagerAdapter(@NonNull @NotNull Fragment fragment) {
        super(fragment);
    }

    public MainViewPagerAdapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public void addFragment(Fragment fragment, String title){
        arrayFragment.add(fragment);
        arrayTitle.add(title);
    }

    @NotNull
    @Override
    public Fragment createFragment(int position) {
        return arrayFragment.get(position);
    }

    @Override
    public int getItemCount() {
        return arrayFragment.size();
    }
}
