package com.lampro.mymusic.utils;

import static com.lampro.mymusic.MyApplication.context;
import static com.lampro.mymusic.constant.Constant.CURRENT_LIST_PLAYING_KEY;
import static com.lampro.mymusic.constant.Constant.EMAIL_KEY;
import static com.lampro.mymusic.constant.Constant.PASSWORD_KEY;
import static com.lampro.mymusic.constant.Constant.REMEMBER_STATUS_KEY;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.media3.common.C;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lampro.mymusic.MyApplication;
import com.lampro.mymusic.model.Song;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class PrefManager {
    public static final String PREF_NAME = "User_Info";
    public static final String CURRENT_LIST_PLAYING = "Current_List_Playing";

    public static void saveEmail(String value) {
        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(EMAIL_KEY, value).apply();
    }

    public static String getEmail() {
        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(EMAIL_KEY, "");

    }

    public static void savePassword(String password) {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    PREF_NAME,
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(PASSWORD_KEY, password);
            editor.apply();

        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }

//        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
//        sharedPreferences.edit().putString(PASSWORD_KEY,password).apply();
    }

    public static String getPassword() {
//        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        String masterKeyAlias = null;
        try {
            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    PREF_NAME,
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            return sharedPreferences.getString(PASSWORD_KEY, "");
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void setRememberStatus(Boolean rememberStatus) {
        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(REMEMBER_STATUS_KEY, rememberStatus).apply();
    }

    public static Boolean getRememberStatus() {
        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(REMEMBER_STATUS_KEY, false);
    }


//    CURRENT LIST PLAYING

    public static void setCurrentListPlaying(String listSong) {
        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences(CURRENT_LIST_PLAYING, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(CURRENT_LIST_PLAYING_KEY, listSong).apply();
    }

    public static String getCurrentListPlaying() {

        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences(CURRENT_LIST_PLAYING, Context.MODE_PRIVATE);

        return sharedPreferences.getString(CURRENT_LIST_PLAYING_KEY, "");
    }

}
