package com.lampro.mymusic.views.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.window.OnBackInvokedDispatcher;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.lampro.mymusic.R;
import com.lampro.mymusic.base.BaseActivity;
import com.lampro.mymusic.databinding.ActivityLoginAndRegisterBinding;
import com.lampro.mymusic.views.fragments.LoginFragment;

public class LoginAndRegisterActivity extends BaseActivity<ActivityLoginAndRegisterBinding> {

    @Override
    protected ActivityLoginAndRegisterBinding inflateBinding() {
        return ActivityLoginAndRegisterBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().beginTransaction()
                .replace(binding.frContainer.getId(), new LoginFragment())
                .commit();



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getOnBackInvokedDispatcher().registerOnBackInvokedCallback(
                    OnBackInvokedDispatcher.PRIORITY_DEFAULT,
                    () -> {
                        // Xử lý sự kiện quay lại
                        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {

                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            // Khởi động Activity mới
                            startActivity(intent);
                        } else {
                            getSupportFragmentManager().popBackStack();
                        }
                    }
            );
        }
    }

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Khởi động Activity mới
            startActivity(intent);
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
}