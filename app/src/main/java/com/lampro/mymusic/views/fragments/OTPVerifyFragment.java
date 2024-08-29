package com.lampro.mymusic.views.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lampro.mymusic.R;
import com.lampro.mymusic.base.BaseFragment;
import com.lampro.mymusic.databinding.FragmentOTPVerifyBinding;


public class OTPVerifyFragment extends BaseFragment<FragmentOTPVerifyBinding> {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public OTPVerifyFragment() {
        // Required empty public constructor
    }

    public static OTPVerifyFragment newInstance(String param1, String param2) {
        OTPVerifyFragment fragment = new OTPVerifyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    protected FragmentOTPVerifyBinding inflateBinding() {
        return FragmentOTPVerifyBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}