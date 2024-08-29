package com.lampro.mymusic.views.fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lampro.mymusic.adapters.RecentlySongAdapter;
import com.lampro.mymusic.adapters.ThisDeviceSongAdapter;
import com.lampro.mymusic.base.BaseFragment;
import com.lampro.mymusic.databinding.FragmentThisDeviceBinding;
import com.lampro.mymusic.interfaces.IOnClickItemSong;
import com.lampro.mymusic.model.Song;
import com.lampro.mymusic.utils.CustomItemDecoration;
import com.lampro.mymusic.utils.CustomItemSongDecoration;
import com.lampro.mymusic.viewmodels.thisdeviceviewmodel.ThisDeviceViewModel;
import com.lampro.mymusic.viewmodels.thisdeviceviewmodel.ThisDeviceViewModelFactory;
import com.lampro.mymusic.views.activities.MainActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThisDeviceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThisDeviceFragment extends BaseFragment<FragmentThisDeviceBinding> implements MainActivity.OnRequestPermission {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private final String TAG = "BAOLAM";

    private ThisDeviceSongAdapter mThisDeviceSong;
    private MainActivity mainActivity;

    private ThisDeviceViewModel mThisDeviceViewModel;



    public ThisDeviceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ThisDeviceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ThisDeviceFragment newInstance(String param1, String param2) {
        ThisDeviceFragment fragment = new ThisDeviceFragment();
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
    protected FragmentThisDeviceBinding inflateBinding() {
        return FragmentThisDeviceBinding.inflate(getLayoutInflater());
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        mThisDeviceSong = new ThisDeviceSongAdapter(mainActivity);

        initview();

        initViewModel();

        mThisDeviceViewModel.liveDataListSong.observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                mThisDeviceSong.updateData(songs);
            }
        });


        mainActivity.setCallback(this);
        mainActivity.checkSelfPermission();

    }


    private void initview() {

        mainActivity.setHideAppbarLayout();

        int top = getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._10sdp);
        int bottom = getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._60sdp);
        CustomItemSongDecoration customItemDecoration = new CustomItemSongDecoration(top, bottom);

        binding.rvSongs.setAdapter(mThisDeviceSong);
        binding.rvSongs.setLayoutManager(new LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false));
        binding.rvSongs.addItemDecoration(customItemDecoration);
    }

    private void initViewModel() {
        Application application = getActivity().getApplication();
        ThisDeviceViewModelFactory thisDeviceViewModelFactory = new ThisDeviceViewModelFactory(application);
        mThisDeviceViewModel = new ViewModelProvider(this, thisDeviceViewModelFactory).get(ThisDeviceViewModel.class);
    }


    @Override
    public void onRequestSuccess() {
        mThisDeviceViewModel.getAllSongs(requireContext().getContentResolver());
    }


}