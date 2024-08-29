package com.lampro.mymusic.base;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.lampro.mymusic.R;

import java.util.ArrayList;

public abstract class BaseFragment<VB extends ViewBinding> extends Fragment {

    private VB _binding = null;
    protected VB binding;
    abstract protected VB inflateBinding();

    private AlertDialog alertDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        _binding = inflateBinding();
        if (_binding != null) {
            binding = _binding;
        }

        View dialogView = inflater.inflate(R.layout.loading, null);

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView)
                .setCancelable(false);
        alertDialog = builder.create();
        Window window = alertDialog.getWindow();
        assert window != null;
        window.setBackgroundDrawable(new ColorDrawable(0));
        return binding.getRoot();
    }

    protected void showLoadingDialog() {
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
    }
    protected void hideLoadingDialog() {
        if (alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        _binding = null;
    }
}
