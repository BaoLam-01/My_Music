package com.lampro.mymusic.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

    @BindingAdapter("circleUserImageUrl")
    public static void loadUserCircleImg(@NonNull ImageView imageView, Uri img) {
        Glide.with(imageView.getContext()).load(img).placeholder(R.drawable.user_default).error(R.drawable.user_default).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(imageView);

    }


    @BindingAdapter("imageUri")
    public static void loadUriImg(@NonNull ImageView imageView, Uri uri) {
        Glide.with(imageView.getContext()).load(uri).centerCrop().placeholder(R.drawable.ic_small_music).error(R.drawable.ic_small_music).into(imageView);

    }


    @BindingAdapter("imageBitmap")
    public static void loadBitmapImg(@NonNull ImageView imageView, Bitmap bitmap) {
        Glide.with(imageView.getContext()).load(bitmap).centerCrop().error(R.drawable.ic_small_music).placeholder(R.drawable.ic_small_music).into(imageView);

    }

    @BindingAdapter("imageBitmap2")
    public static void loadBitmapImg2(@NonNull ImageView imageView, Bitmap bitmap) {
        Glide.with(imageView.getContext()).load(bitmap).centerCrop().error(R.drawable.img_bg_music_playing).placeholder(R.drawable.img_bg_music_playing).into(imageView);

    }



}
