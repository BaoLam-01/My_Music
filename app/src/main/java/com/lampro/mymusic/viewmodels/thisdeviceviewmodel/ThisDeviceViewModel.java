package com.lampro.mymusic.viewmodels.thisdeviceviewmodel;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.lampro.mymusic.model.Song;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID};
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        Cursor cursor = contentResolver.query(uri, projection, selection, null, sortOrder);
        assert cursor != null;
        cursor.moveToFirst();

        int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
        int titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
        int artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
        int albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
        int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
        int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        int idAlbumColumn= cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);


        do {

            Long id = cursor.getLong(idColumn);

            String title = cursor.getString(titleColumn);
            String artist = cursor.getString(artistColumn);
            String album = cursor.getString(albumColumn);
            String duration = cursor.getString(durationColumn);
            String data = cursor.getString(dataColumn);
            Long idAlbum = cursor.getLong(idAlbumColumn);


            String getUri = getSongUri(getApplication(), id);
//            Bitmap img = getAlbumArt(contentResolver,getUri);


            songs.add(new Song(null, id, title, artist, album, duration, data, getUri));

        } while (cursor.moveToNext());
        cursor.close();
        liveDataListSong.setValue(songs);


    }


    private String getSongUri(Context context, Long songId) {
        Uri contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        return ContentUris.withAppendedId(contentUri, songId).toString();
    }


    public Bitmap getAlbumArt(ContentResolver contentResolver,Uri filePath)  {
        try (MediaMetadataRetriever retriever = new MediaMetadataRetriever()) {
            try {
                retriever.setDataSource(Objects.requireNonNull(contentResolver.openAssetFileDescriptor(filePath, "r")).getFileDescriptor());
                byte[] art = retriever.getEmbeddedPicture();
                if (art != null) {
                    return BitmapFactory.decodeByteArray(art, 0, art.length);
                } else {
                    // Trường hợp không có ảnh bìa
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                try {
                    retriever.release();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    

}
