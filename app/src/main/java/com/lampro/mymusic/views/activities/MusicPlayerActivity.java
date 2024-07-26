package com.lampro.mymusic.views.activities;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.lampro.mymusic.R;
import com.lampro.mymusic.base.BaseActivity;
import com.lampro.mymusic.databinding.ActivityMusicPlayerBinding;

import eightbitlab.com.blurview.RenderEffectBlur;
import eightbitlab.com.blurview.RenderScriptBlur;

public class MusicPlayerActivity extends BaseActivity<ActivityMusicPlayerBinding> {

    @Override
    protected ActivityMusicPlayerBinding inflateBinding() {
        return ActivityMusicPlayerBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        float radius = 10f;

        View decorView = getWindow().getDecorView();
        // ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);

        // Optional:
        // Set drawable to draw in the beginning of each blurred frame.
        // Can be used in case your layout has a lot of transparent space and your content
        // gets a too low alpha value after blur is applied.
        Drawable windowBackground = decorView.getBackground();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            binding.blurView.setupWith(rootView, new RenderEffectBlur()) // or RenderEffectBlur
                    .setFrameClearDrawable(windowBackground) // Optional
                    .setBlurRadius(radius);
        }
    }
}