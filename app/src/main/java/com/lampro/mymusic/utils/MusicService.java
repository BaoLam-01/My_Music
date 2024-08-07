package com.lampro.mymusic.utils;

import static com.lampro.mymusic.MyApplication.CHANNEL_ID;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.media3.common.util.UnstableApi;

import com.lampro.mymusic.R;
import com.lampro.mymusic.model.Song;

import java.util.List;

public class MusicService extends Service {

    public static final String START = "START";
    public static final String PLAY = "PLAY";
    public static final String PAUSE = "PAUSE";
    public static final String CLEAR = "CLEAR";
    public static final String NEXT = "NEXT";
    public static final String PREVIOUS = "PREVIOUS";
    public static final String SEND_ACTION_TO_ACTIVITY = "SEND_ACTION_TO_ACTIVITY";

    private MediaPlayer mediaPlayer;
    private List<Song> listSong;
    private int position;
    private Song song;


    private MediaSessionCompat mediaSession;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            position = bundle.getInt("position");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                listSong = bundle.getParcelableArrayList("listSong", Song.class);
            } else {
                listSong = bundle.getParcelableArrayList("listSong");
            }


            assert listSong != null;
            song = listSong.get(position);
            String actionMusic = intent.getAction();
            handleActionMusic(actionMusic);

        }


        return START_STICKY;
    }

    private void handleActionMusic(String action) {
        switch (action) {
            case START:
                startMusic(song.getUriSong());
                sendNotificationMedia(song);
                break;
            case PLAY:
                playMusic();
                break;
            case PAUSE:
                pauseMusic();
                break;
            case PREVIOUS:
                previousMusic();
                break;
            case NEXT:
                nextMusic();
                break;
            case CLEAR:
                stopSelf();
                sendActionToActivity(CLEAR);
                break;
        }
    }


    private void startMusic(Uri uri) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.setLooping(false);
        mediaPlayer.setVolume(1f, 1f);
        mediaPlayer.start();
        mediaPlayer.isPlaying();
        sendActionToActivity(START);
    }

    private void playMusic() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            sendActionToActivity(PLAY);
        }
    }

    private void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            sendActionToActivity(PAUSE);
        }
    }

    private void previousMusic() {
        if (position == 0) {
            position = listSong.size();
        }
        position -= 1;
        song = listSong.get(position);
        startMusic(song.getUriSong());
        sendActionToActivity(PREVIOUS);
    }

    private void nextMusic() {
        if (position == listSong.size() - 1) {
            position = -1;
        }
        position += 1;
        song = listSong.get(position);
        startMusic(song.getUriSong());
        sendActionToActivity(NEXT);
    }

    @SuppressLint("ForegroundServiceType")
    @OptIn(markerClass = UnstableApi.class)
    private void sendNotificationMedia(Song song) {
//        InputStream inputStream;
//        try {
//            inputStream = getContentResolver().openInputStream(song.getImg());
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.diem_xua);

        Intent intentPlayPause = new Intent(this, MusicReceiver.class);
        int icon;
        if (mediaPlayer.isPlaying()) {
            icon = R.drawable.ic_pause;
            intentPlayPause.setAction(PAUSE);
        } else {
            icon = R.drawable.ic_play;
            intentPlayPause.setAction(PLAY);
        }


        PendingIntent playPausePendingIntent = PendingIntent.getBroadcast(this, 0, intentPlayPause, PendingIntent.FLAG_IMMUTABLE);

        Intent previousIntent = new Intent(this, MusicReceiver.class).setAction(PREVIOUS);
        PendingIntent previousPendingIntent = PendingIntent.getBroadcast(this, 0, previousIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent nextIntent = new Intent(this, MusicReceiver.class).setAction(NEXT);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(this, 0, nextIntent, PendingIntent.FLAG_IMMUTABLE);

        mediaSession = new MediaSessionCompat(this, "MusicService");
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_ic_mymusic_app)
                .setSubText("My Music")
                .setContentTitle(song.getName())
                .setContentText(song.getArtist())
                .setLargeIcon(song.getImg())
                .addAction(R.drawable.ic_previous, "Previous", previousPendingIntent)
                .addAction(icon, "Play,Pause", playPausePendingIntent)
                .addAction(R.drawable.ic_next, "Next", nextPendingIntent)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(1)
                        .setMediaSession(mediaSession.getSessionToken())
                )
                .build();
        initMediaSession();

        startForeground(1, notification);
    }

    private void initMediaSession() {


        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                Intent intentPlayPause = new Intent(getBaseContext(), MusicService.class);
                intentPlayPause.setAction(PLAY);
                PendingIntent playPausePendingIntent = PendingIntent.getService(getBaseContext(), 0, intentPlayPause, PendingIntent.FLAG_IMMUTABLE);
            }

            @Override
            public void onPause() {
                Intent intentPlayPause = new Intent(getBaseContext(), MusicService.class);
                intentPlayPause.setAction(PAUSE);
                PendingIntent playPausePendingIntent = PendingIntent.getService(getBaseContext(), 0, intentPlayPause, PendingIntent.FLAG_IMMUTABLE);
            }

            @Override
            public void onSkipToNext() {
                // Handle next action
            }

            @Override
            public void onSkipToPrevious() {
                // Handle previous action
            }
        });

        mediaSession.setActive(true);
    }

    private void sendActionToActivity(String action) {
        Intent intent = new Intent("SEND_ACTION_TO_ACTIVITY");
        intent.setAction(action);

        Bundle bundle = new Bundle();
//        bundle.putParcelable("mediaPlayer", (Parcelable) mediaPlayer);
        bundle.putParcelable("song", song);

        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}
