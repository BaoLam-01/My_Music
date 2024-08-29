package com.lampro.mymusic;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class MyApplication extends Application {
    public static Context context;
    public static final String CHANNEL_ID = "CHANNEL_MUSIC_APP";

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        createNotificationChannel();
    }
    public static Context getContext() {
        return context;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Channel music", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setSound(null,null);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}
