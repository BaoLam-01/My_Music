package com.lampro.mymusic.model.database;

import static com.lampro.mymusic.constant.Constant.RETRO_MUSIC_DATABASE;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

public abstract class RetroMusicDatabase extends RoomDatabase {
    public static RetroMusicDatabase instance;

    public static synchronized RetroMusicDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),RetroMusicDatabase.class, RETRO_MUSIC_DATABASE)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract SongDao songDao();

}
