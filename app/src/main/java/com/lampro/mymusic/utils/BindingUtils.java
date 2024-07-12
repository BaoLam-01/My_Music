package com.lampro.mymusic.utils;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.lampro.mymusic.R;

public class BindingUtils {
    @BindingAdapter("imageUrl")
    public static void loadSvgImg(ImageView imageView,String img) {
        Glide.with(imageView.getContext()).load(R.drawable.liked).into(imageView);

    }

}
