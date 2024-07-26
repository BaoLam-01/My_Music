package com.lampro.mymusic.viewmodels.thisdeviceviewmodel;

import android.app.Application;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.lampro.mymusic.model.Song;

import java.util.ArrayList;
import java.util.List;

public class ThisDeviceViewModel extends AndroidViewModel {

    public MutableLiveData<List<Song>> liveDataListSong = new MutableLiveData<>();

    public ThisDeviceViewModel(@NonNull Application application) {
        super(application);
    }

    public void getAllSongs(ContentResolver contentResolver) {
        List<Song> songs = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA};
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        Cursor cursor = contentResolver.query(uri, projection, selection, null, sortOrder);
        cursor.moveToFirst();

        int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
        int titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
        int artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
        int albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
        int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
        int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);


        do {
            int img = cursor.getInt(dataColumn);
            String id = cursor.getString(idColumn);
            String title = cursor.getString(titleColumn);
            String artist = cursor.getString(artistColumn);
            String album = cursor.getString(albumColumn);
            String duration = cursor.getString(durationColumn);
            String data = cursor.getString(dataColumn);
            songs.add(new Song(img, id, title, artist, album, duration, data));

        } while (cursor.moveToNext());
        liveDataListSong.setValue(songs);
    }



}
