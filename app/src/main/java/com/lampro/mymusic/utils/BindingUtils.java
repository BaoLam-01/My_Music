package com.lampro.mymusic.utils;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.lampro.mymusic.R;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation;

public class BindingUtils {
    @BindingAdapter("imageUrl")
    public static void loadImg(@NonNull ImageView imageView, int img) {
        Glide.with(imageView.getContext()).load(img).into(imageView);

    }

    @BindingAdapter("circleImageUrl")
    public static void loadCircleImg(@NonNull ImageView imageView, int img) {
        Glide.with(imageView.getContext()).load(img).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(imageView);

    }
}
