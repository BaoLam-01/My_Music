package com.lampro.mymusic.views.activities;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.lampro.mymusic.R;
import com.lampro.mymusic.adapters.ViewPagerAdapter;
import com.lampro.mymusic.base.BaseActivity;
import com.lampro.mymusic.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends BaseActivity<ActivityMainBinding> {


    @Override
    protected ActivityMainBinding inflateBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ViewPager2 vpContent = binding.vpContent;
        vpContent.setAdapter(new ViewPagerAdapter(this));
        vpContent.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        BottomNavigationView navigationView = binding.navBottom;
        navigationView.setOnItemSelectedListener(v -> {


            if (v.getItemId() == R.id.discover) {
                vpContent.setCurrentItem(0);
            } else if (v.getItemId() == R.id.liked) {
                vpContent.setCurrentItem(1);
            } else if (v.getItemId() == R.id.playlist) {
                vpContent.setCurrentItem(2);
            } else {
                vpContent.setCurrentItem(3);
            }

            return true;
        });

        vpContent.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        navigationView.setSelectedItemId(R.id.discover);
                        break;
                    case 1:
                        navigationView.setSelectedItemId(R.id.liked);
                        break;
                    case 2:
                        navigationView.setSelectedItemId(R.id.playlist);
                        break;
                    case 3:
                        navigationView.setSelectedItemId(R.id.setting);
                        break;
                    default:
                        navigationView.setSelectedItemId(R.id.discover);
                        break;
                }
            }
        });
    }
}