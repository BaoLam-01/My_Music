package com.lampro.mymusic.interfaces;

import android.net.Uri;

import com.lampro.mymusic.model.Song;

import java.util.List;

public interface IOnClickItemSong {
    void playSong(List<Song> listSong, int position);
}
