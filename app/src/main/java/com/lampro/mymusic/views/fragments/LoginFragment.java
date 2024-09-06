package com.lampro.mymusic.views.fragments;

import static android.widget.Toast.LENGTH_SHORT;

import android.app.Application;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lampro.mymusic.R;
import com.lampro.mymusic.base.BaseFragment;
import com.lampro.mymusic.databinding.FragmentLoginBinding;
import com.lampro.mymusic.model.User;
import com.lampro.mymusic.utils.PrefManager;
import com.lampro.mymusic.viewmodels.loginviewmodel.LoginViewModel;
import com.lampro.mymusic.viewmodels.loginviewmodel.LoginViewModelFactory;
import com.lampro.mymusic.views.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import eightbitlab.com.blurview.RenderEffectBlur;


public class LoginFragment extends BaseFragment<FragmentLoginBinding> implements View.OnClickListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static LoginFragment instance;

    private String mParam1;
    private String mParam2;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference userRef = firebaseDatabase.getReference("List User");
    private List<User> listUser = new ArrayList<>();

    private LoginViewModel mloginViewModel;

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


    private void initViewModel() {
        Application application = requireActivity().getApplication();
        LoginViewModelFactory loginViewModelFactory = new LoginViewModelFactory(application);
        mloginViewModel = new ViewModelProvider(this, loginViewModelFactory).get(LoginViewModel.class);
    }

    @Override
    protected FragmentLoginBinding inflateBinding() {
        return FragmentLoginBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
        mloginViewModel.getListUser();

        mloginViewModel.listUserLiveData.observe(getViewLifecycleOwner(), users -> {
            listUser = users;
        });


        initUI();

        listener();
    }

    private void initUI() {
        setBackgroundBlur();


        String email = PrefManager.getEmail();
        if (email != null && !email.isEmpty()) {
            binding.edtEmailSI.setText(email);
        }
        if (PrefManager.getRememberStatus()) {
            String password = PrefManager.getPassword();
            if (password != null && !password.isEmpty()) {
                binding.edtPwSI.setText(password);
            }
        }


        if (mParam1 != null && mParam2 != null) {
            binding.edtEmailSI.setText(mParam1);
            binding.edtPwSI.setText(mParam2);
        }

        Boolean rememberStatus = PrefManager.getRememberStatus();
        if (rememberStatus) {
            binding.cbRememberMe.setChecked(true);
        } else {
            binding.cbRememberMe.setChecked(false);
        }
    }

    private void listener() {
        binding.btnSU.setOnClickListener(this);
        binding.btnSI.setOnClickListener(this);
        binding.btnFgPw.setOnClickListener(this);
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
        if (v.getId() == binding.btnFgPw.getId()) {
            onClickForgotPassword();
        }
    }

    private void onClickForgotPassword() {

        String emailAddress = binding.edtEmailSI.getText().toString().trim();

        if (emailAddress.isEmpty()) {
            Toast.makeText(getContext(), "Username or password cannot be left blank!", LENGTH_SHORT).show();
        } else {

            if (emailAddress.length() < 11 || !emailAddress.endsWith("@gmail.com")) {
                Toast.makeText(getContext(), "Email is not valid!", LENGTH_SHORT).show();
            } else if (!checkEmailExist(emailAddress)) {
                Toast.makeText(getContext(), "Account not registered please try again", LENGTH_SHORT).show();
            } else {

                showLoadingDialog();
                mloginViewModel.resetPassword(emailAddress);
                mloginViewModel.taskResetPasswordResult.observe(getViewLifecycleOwner(), voidTask -> {
                    if (voidTask.isSuccessful()) {
                        Toast.makeText(getContext(), "Email sent", LENGTH_SHORT).show();
                        hideLoadingDialog();
                    }else {
                        Toast.makeText(getInstance().getContext(), "Email sent failed", LENGTH_SHORT).show();
                        hideLoadingDialog();
                    }
                });

            }
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

         mloginViewModel.login(email, password);
         mloginViewModel.taskLoginResult.observe(getViewLifecycleOwner(), authResultTask -> {
             if (authResultTask.isSuccessful()) {
                 PrefManager.saveEmail(email);
                 if (binding.cbRememberMe.isChecked()) {
                     PrefManager.setRememberStatus(true);
                     PrefManager.savePassword(password);
                 } else {
                     PrefManager.setRememberStatus(false);
                 }

                 Intent intent = new Intent(getActivity(), MainActivity.class);
                 startActivity(intent);
                 requireActivity().finishAffinity();
                 hideLoadingDialog();
             }else {
                 // If sign in fails, display a message to the user.
                 Toast.makeText(getContext(), "Wrong username or password please try again.",
                         Toast.LENGTH_SHORT).show();
                 hideLoadingDialog();
             }
         });

    }

    private Boolean checkEmailExist(String email) {
        Boolean isExist = false;
        if (listUser == null || listUser.isEmpty() || email.isEmpty()) {
            return isExist;
        }
        for (User user : listUser) {
            if (Objects.equals(user.getEmail(), email)) {
                isExist = true;
                break;
            } else {
                isExist = false;
            }
        }
        return isExist;
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