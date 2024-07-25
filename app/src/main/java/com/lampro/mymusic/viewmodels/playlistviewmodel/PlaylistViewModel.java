package com.lampro.mymusic.viewmodels.playlistviewmodel;

import android.app.Application;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.lampro.mymusic.R;
import com.lampro.mymusic.model.Playlist;

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
        List<String> data = new ArrayList<>();
        List<String> list = Arrays.asList(getApplication().getResources().getStringArray(R.array.filter_playlist));
        data = list;
        return data;
    }

    public List<Playlist> getLiveDataListPlaylist() {
        List<Playlist> data = new ArrayList<>();
        data.add(new Playlist(R.drawable.heart, "Liked songs", 32));
        data.add(new Playlist(R.drawable.downloaded, "Downloaded", 0));
        data.add(new Playlist(R.drawable.folder, "This device", 66));
        data.add(new Playlist(R.drawable.default_music, "Playlist 1", 0));
        data.add(new Playlist(R.drawable.default_music, "Playlist 2", 2));
        return data;
    }
}
