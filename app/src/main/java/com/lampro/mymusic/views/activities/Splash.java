package com.lampro.mymusic.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lampro.mymusic.R;
import com.lampro.mymusic.base.BaseActivity;
import com.lampro.mymusic.databinding.ActivitySplashBinding;

public class Splash extends BaseActivity<ActivitySplashBinding> {

    @Override
    protected ActivitySplashBinding inflateBinding() {
        return ActivitySplashBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);


        new Handler().postDelayed(this::nextActivity, 300);
    }

    private void nextActivity() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
//            chua log in

            Intent intent = new Intent(Splash.this, LoginAndRegisterActivity.class);
            finish();
            startActivity(intent);

        } else {
            Intent intent = new Intent(Splash.this, MainActivity.class);
            finish();
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}