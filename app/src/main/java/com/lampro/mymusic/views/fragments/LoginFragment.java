package com.lampro.mymusic.views.fragments;

import static android.widget.Toast.LENGTH_SHORT;

import static com.lampro.mymusic.constant.Constant.USER_NAME_KEY;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lampro.mymusic.R;
import com.lampro.mymusic.base.BaseFragment;
import com.lampro.mymusic.databinding.FragmentLoginBinding;
import com.lampro.mymusic.utils.PrefManager;
import com.lampro.mymusic.views.activities.MainActivity;

import java.util.concurrent.Executor;

import eightbitlab.com.blurview.RenderEffectBlur;


public class LoginFragment extends BaseFragment<FragmentLoginBinding> implements View.OnClickListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static LoginFragment instance;

    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment getInstance() {
        if (instance == null) {
            instance = new LoginFragment();
        }
        return instance;
    }


    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
    protected FragmentLoginBinding inflateBinding() {
        return FragmentLoginBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI();

        listener();
    }

    private void initUI() {
        setBackgroundBlur();


        String email = PrefManager.getEmail(USER_NAME_KEY);
        if (email != null) {
            binding.edtEmailSI.setText(email);
        }

        if (mParam1 != null && mParam2 != null){
            binding.edtEmailSI.setText(mParam1);
            binding.edtPwSI.setText(mParam2);
        }
    }

    private void listener() {
        binding.btnSU.setOnClickListener(this);
        binding.btnSI.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == binding.btnSU.getId()) {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fr_container, new RegisterFragment())
                    .addToBackStack(null)
                    .commit();
        }
        if (v.getId() == binding.btnSI.getId()) {
            onClickSignIn();
        }
    }

    private void onClickSignIn() {
        String email = binding.edtEmailSI.getText().toString().trim();
        String password = binding.edtPwSI.getText().toString().trim();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Username or password cannot be left blank!", LENGTH_SHORT).show();
        } else {

            if (email.length() < 11 || !email.endsWith("@gmail.com")) {

                Toast.makeText(getContext(), "Email is not valid!", LENGTH_SHORT).show();


            } else if (password.length() < 6 || password.contains(" ")) {
                Toast.makeText(getContext(), "Password must be more than 6 characters and " +
                        "cannot contain spaces!", LENGTH_SHORT).show();
            } else {
                login(email, password);
            }
        }
    }

    private void login(String email, String password) {
        showLoadingDialog();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            PrefManager.saveEmail(USER_NAME_KEY, email);

                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                            requireActivity().finishAffinity();
                            hideLoadingDialog();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getContext(), "Authentication failed." + task.toString(),
                                    Toast.LENGTH_SHORT).show();
                            hideLoadingDialog();
                        }
                    }
                });
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


}