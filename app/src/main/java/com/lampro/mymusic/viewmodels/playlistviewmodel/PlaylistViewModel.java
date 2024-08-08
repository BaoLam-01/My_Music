package com.lampro.mymusic.viewmodels.playlistviewmodel;


import android.Manifest;
import android.app.Application;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.lampro.mymusic.R;
import com.lampro.mymusic.model.Playlist;
import com.lampro.mymusic.model.Song;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PlaylistViewModel extends AndroidViewModel {

    public MutableLiveData<List<String>> liveDataListFilter = new MutableLiveData<>();
    public MutableLiveData<List<Playlist>> liveDataListPlaylist = new MutableLiveData<>();

    public PlaylistViewModel(@NonNull Application application) {
        super(application);
    }

    public void setDataListFilter() {
        liveDataListFilter.setValue(getLiveDataListFilter());
    }

    public void setDataListPlaylist() {
        liveDataListPlaylist.setValue(getLiveDataListPlaylist());
    }


    public List<String> getLiveDataListFilter() {
        List<String> data;
        List<String> list = Arrays.asList(getApplication().getResources().getStringArray(R.array.filter_playlist));
        data = list;
        return data;
    }

    public List<Playlist> getLiveDataListPlaylist() {
        List<Playlist> data = new ArrayList<>();
        data.add(new Playlist(R.drawable.heart, "Liked songs", 0));
        data.add(new Playlist(R.drawable.downloaded, "Downloaded", 0));
        data.add(new Playlist(R.drawable.folder, "This device", getSongCountThisDevice()));
        data.add(new Playlist(R.drawable.default_music, "Playlist 1", 0));
        data.add(new Playlist(R.drawable.default_music, "Playlist 2", 0));
        return data;
    }

    private int getSongCountThisDevice() {

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        String[] projection = {MediaStore.Audio.Media._ID,
//                MediaStore.Audio.Media.TITLE,
//                MediaStore.Audio.Media.ARTIST,
//                MediaStore.Audio.Media.ALBUM,
//                MediaStore.Audio.Media.DURATION,
//                MediaStore.Audio.Media.DATA,
//                MediaStore.Audio.Media.ALBUM_ID};
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        ContentResolver contentResolver = getApplication().getContentResolver();
        Cursor cursor;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                cursor = contentResolver.query(uri, null, selection, null, sortOrder);
            } else return 0;
        } else {
            if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                cursor = contentResolver.query(uri, null, selection, null, sortOrder);
            } else return 0;
        }


        return cursor.getCount();
    }
}
