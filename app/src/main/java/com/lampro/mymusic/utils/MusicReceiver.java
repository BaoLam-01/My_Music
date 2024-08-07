package com.lampro.mymusic.utils;

import static com.lampro.mymusic.utils.MusicService.PAUSE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Objects;

public class MusicReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), PAUSE)) {
            Intent serviceIntent = new Intent(context, MusicService.class);
            serviceIntent.setAction(PAUSE);
            context.startForegroundService(serviceIntent);
        }
    }
}
