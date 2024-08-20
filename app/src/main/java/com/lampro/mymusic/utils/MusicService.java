package com.lampro.mymusic.utils;

import static com.lampro.mymusic.MyApplication.CHANNEL_ID;
import static com.lampro.mymusic.MyApplication.context;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.media3.common.util.UnstableApi;

import com.lampro.mymusic.R;
import com.lampro.mymusic.model.Song;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MusicService extends Service {

    public static final String START = "START";
    public static final String PLAY = "PLAY";
    public static final String PAUSE = "PAUSE";
    public static final String CLEAR = "CLEAR";
    public static final String NEXT = "NEXT";
    public static final String PREVIOUS = "PREVIOUS";
    public static final String MUTED = "MUTED";
    public static final String UNMUTED = "UNMUTED";
    public static final String SEND_ACTION_TO_ACTIVITY = "com.lampro.mymusic.utils.SEND_ACTION_TO_ACTIVITY";

    private MediaPlayer mediaPlayer;
    private List<Song> listSong;
    private int position;
    private Song song;
    private boolean isMuted = false;


    private MediaSessionCompat mediaSession;

    private MusicBinder musicBinder = new MusicBinder();

    public class MusicBinder extends Binder {
        public MusicService getMusicService() {
            return MusicService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBinder;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(@NonNull Intent intent, int flags, int startId) {

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

        }

        String actionMusic = intent.getAction();
        assert actionMusic != null;
        handleActionMusic(actionMusic);


        return START_STICKY;
    }


    private void handleActionMusic(String action) {
        switch (action) {
            case START:
                startMusic(song.getUriSong());
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
            case MUTED:
                mutedMusic();
                break;
            case UNMUTED:
                unMutedMusic();
                break;
            case CLEAR:
//                stopSelf();
                clearMusic();
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
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                nextMusic();
            }
        });
        isMuted = false;
        mediaPlayer.start();
        sendActionToActivity(START);
        sendNotificationMedia(song);

    }

    public void playMusic() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            sendActionToActivity(PLAY);
            sendNotificationMedia(song);
        }

    }

    public void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            sendActionToActivity(PAUSE);
            sendNotificationMedia(song);
        }
    }

    public void previousMusic() {
        if (position == 0) {
            position = listSong.size();
        }
        position -= 1;
        song = listSong.get(position);
        startMusic(song.getUriSong());
    }

    public void nextMusic() {
        if (position == listSong.size() - 1) {
            position = -1;
        }
        position += 1;
        song = listSong.get(position);
        startMusic(song.getUriSong());
    }

    public void mutedMusic() {
        mediaPlayer.setVolume(0f, 0f);
        isMuted = true;
        sendActionToActivity(MUTED);
    }

    public void unMutedMusic() {
        mediaPlayer.setVolume(1f, 1f);
        isMuted = false;
        sendActionToActivity(UNMUTED);
    }

    public void clearMusic() {
        sendActionToActivity(CLEAR);
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        stopSelf();
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


        Intent intentPlayPause = new Intent(this, MusicService.class);
        int icon;
        if (mediaPlayer.isPlaying()) {
            icon = R.drawable.ic_pause;
            intentPlayPause.setAction(PAUSE);
        } else {
            icon = R.drawable.ic_play;
            intentPlayPause.setAction(PLAY);
        }



        PendingIntent playPausePendingIntent = PendingIntent.getService(context.getApplicationContext(),
                0, intentPlayPause, PendingIntent.FLAG_IMMUTABLE);

        Intent previousIntent = new Intent(this, MusicService.class).setAction(PREVIOUS);
        PendingIntent previousPendingIntent = PendingIntent.getService(this, 1,
                previousIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent nextIntent = new Intent(this, MusicService.class).setAction(NEXT);
        PendingIntent nextPendingIntent = PendingIntent.getService(this, 2,
                nextIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent clearIntent = new Intent(this, MusicService.class).setAction(CLEAR);
        PendingIntent clearPendingIntent = PendingIntent.getService(this, 3,
                clearIntent, PendingIntent.FLAG_MUTABLE);

        mediaSession = new MediaSessionCompat(this, "MusicService");
        mediaSession.setActive(true);
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
                .addAction(R.drawable.ic_clear,"Clear", clearPendingIntent)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(1)
                        .setMediaSession(mediaSession.getSessionToken())
                )
                .build();
//        initMediaSession();

        startForeground(1, notification);
    }


    private void initMediaSession() {


        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
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

            @Override
            public void onStop() {
                super.onStop();
            }
        });

        mediaSession.setActive(true);
    }

    public void sendActionToActivity(String action) {
        Intent intent = new Intent(SEND_ACTION_TO_ACTIVITY);

        Bundle bundle = new Bundle();
        bundle.putString("action", action);
        bundle.putBoolean("isMuted", isMuted);
        bundle.putBoolean("isPlaying", mediaPlayer.isPlaying());
        bundle.putParcelable("songPlaying", song);

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
        mediaSession.release();
    }

}
