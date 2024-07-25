package com.lampro.mymusic.views.fragments;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lampro.mymusic.adapters.RecentlySongAdapter;
import com.lampro.mymusic.base.BaseFragment;
import com.lampro.mymusic.databinding.FragmentThisDeviceBinding;
import com.lampro.mymusic.model.Song;
import com.lampro.mymusic.views.activities.MainActivity;

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

    private RecentlySongAdapter mRecentlySongAdapter;
    private MainActivity mainActivity;

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

        mRecentlySongAdapter = new RecentlySongAdapter();
        binding.rvSongs.setAdapter(mRecentlySongAdapter);
        binding.rvSongs.setLayoutManager(new LinearLayoutManager(this.requireContext(),LinearLayoutManager.VERTICAL,false));

        mainActivity = (MainActivity) getActivity();
        mainActivity.setCallback(this);
        mainActivity.checkSelfPermission();


    }


    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                onRequestSuccess();

            }
        } else {
            // Các quyền khác cho Android dưới 11
            if (ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.READ_MEDIA_AUDIO)
                    == PackageManager.PERMISSION_GRANTED) {
                onRequestSuccess();
            }
        }
    }

    private List<Song> getAllSongs(ContentResolver contentResolver) {
        List<Song> songs = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA};
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        Cursor cursor = contentResolver.query(uri, projection, selection, null, null);
        cursor.moveToFirst();

        int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
        int titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
        int artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
        int albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
        int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
        int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);


        do {
            String id = cursor.getString(idColumn);
            String title = cursor.getString(titleColumn);
            String artist = cursor.getString(artistColumn);
            String album = cursor.getString(albumColumn);
            String duration = cursor.getString(durationColumn);
            String data = cursor.getString(dataColumn);
            songs.add(new Song(id, title, artist, album, duration, data));

        } while (cursor.moveToNext());
        return songs;
    }

    @Override
    public void onRequestSuccess() {
        List<Song> songs = getAllSongs(getContext().getContentResolver());
        mRecentlySongAdapter.updateData(songs);
    }
}