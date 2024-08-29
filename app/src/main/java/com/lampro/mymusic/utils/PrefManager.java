package com.lampro.mymusic.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.lampro.mymusic.MyApplication;

public class PrefManager {
    public static final String PREF_NAME = "User_Info";

    public static void saveEmail(String key, String value) {
        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key,value).apply();
    }
    public static String getEmail(String key) {
        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,null);
    }
}
