package com.lampro.mymusic.base;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewbinding.ViewBinding;

import com.lampro.mymusic.R;

public abstract class BaseActivity<VB extends ViewBinding> extends AppCompatActivity {
    private VB _binding = null;
    protected VB binding;

    abstract protected VB inflateBinding();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        _binding = inflateBinding();
        if (_binding != null){
            binding = _binding;
            setContentView(binding.getRoot());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        _binding = null;
    }
}
