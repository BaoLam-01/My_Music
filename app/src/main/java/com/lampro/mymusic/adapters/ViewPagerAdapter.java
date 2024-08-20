package com.lampro.mymusic.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.lampro.mymusic.views.fragments.DiscoverFragment;
import com.lampro.mymusic.views.fragments.SearchFragment;
import com.lampro.mymusic.views.fragments.PlaylistFragment;
import com.lampro.mymusic.views.fragments.SettingFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position) {
            case 0: fragment = new DiscoverFragment();
            break;
            case 1: fragment = new SearchFragment();
            break;
            case 2: fragment = new PlaylistFragment();
            break;
            case 3: fragment = new SettingFragment();
            break;
            default: fragment = new DiscoverFragment();
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
