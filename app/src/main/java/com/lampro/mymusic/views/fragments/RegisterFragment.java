package com.lampro.mymusic.views.fragments;

import static android.widget.Toast.LENGTH_SHORT;

import static androidx.fragment.app.FragmentManager.TAG;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lampro.mymusic.R;
import com.lampro.mymusic.base.BaseFragment;
import com.lampro.mymusic.databinding.FragmentRegisterBinding;
import com.lampro.mymusic.views.activities.MainActivity;

import java.util.concurrent.Executor;

import eightbitlab.com.blurview.RenderEffectBlur;

public class RegisterFragment extends BaseFragment<FragmentRegisterBinding> implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static RegisterFragment instance;

    private String mParam1;
    private String mParam2;



    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment getInstance() {
        if (instance == null) {
            instance = new RegisterFragment();
        }
        return instance;
    }

    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    protected FragmentRegisterBinding inflateBinding() {
        return FragmentRegisterBinding.inflate(getLayoutInflater());
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUi();
        listener();
    }

    private void initUi() {
        setBackgroundBlur();
    }

    private void setBackgroundBlur() {
        float radius = 3f;

        View decorView = requireActivity().getWindow().getDecorView();
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

    private void listener() {
        binding.btnSI.setOnClickListener(this);
        binding.btnSU.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {


        if (v.getId() == binding.btnSI.getId()) {

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fr_container, new LoginFragment())
                    .commit();
        }
        if (v.getId() == binding.btnSU.getId()) {

            String email = binding.edtEmailSU.getText().toString().trim();
            String password = binding.edtPwSU.getText().toString().trim();
            String cfPassword = binding.edtCfPwSU.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || cfPassword.isEmpty()) {
                Toast.makeText(getContext(), "Cannot be left blank!", LENGTH_SHORT).show();
            } else {
                if (email.length() < 11 || !email.endsWith("@gmail.com")) {

                    Toast.makeText(getContext(), "Email is not valid!", LENGTH_SHORT).show();

//                    } else if (checkEmail(edtEmailSU.getText().toString().trim())) {
//
//                        Toast.makeText(getContext(), "Email already exists, please enter another email!", LENGTH_SHORT).show();

                } else if (password.length() < 6 || password.contains(" ")) {
                    Toast.makeText(getContext(), "Password must be more than 6 characters and " +
                            "cannot contain spaces!", LENGTH_SHORT).show();
                } else if (!cfPassword.equals(password)) {
                    Toast.makeText(getContext(), "Passwords do not match!", LENGTH_SHORT).show();
                } else {
                    register(email,password);
                }
            }
        }
    }

    private void register(String email, String password) {
        showLoadingDialog();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getContext(), "Register success", LENGTH_SHORT).show();
                            goToLognin(email,password);
                            hideLoadingDialog();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getContext(), "Register fail" + task.toString(), LENGTH_SHORT).show();
                            goToLognin(email,password);
                            hideLoadingDialog();
                        }
                    }
                });

    }

    private void goToLognin(String email, String password) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fr_container, LoginFragment.newInstance(email,password))
                .commit();

    }
}