package com.lampro.mymusic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.lampro.mymusic.model.Song;

import java.util.List;

public class DataRepository {
    public MutableLiveData<List<Song>> listSongLiveData = new MutableLiveData<List<Song>>();

    private DataRepository(){};
    private static DataRepository instance;


    public static DataRepository getInstance() {
        if (instance == null) {
            instance = new DataRepository();
        }
        return instance;
    }

}
