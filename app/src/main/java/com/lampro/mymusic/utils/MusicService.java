package com.lampro.mymusic.utils;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static com.lampro.mymusic.MyApplication.CHANNEL_ID;
import static com.lampro.mymusic.MyApplication.context;
import static com.lampro.mymusic.MyApplication.getContext;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcelable;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.media3.common.util.UnstableApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;
import com.lampro.mymusic.R;
import com.lampro.mymusic.model.Song;
import com.lampro.mymusic.model.database.RetroMusicDatabase;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    private MediaPlayer mediaPlayer = null;
    private List<Song> listSong = null;
    private int position = 0;
    private Song song = null;
    private boolean isMuted = false;
    private Gson gson;
    Type listType;


    private MediaSessionCompat mediaSession;

    private MusicBinder musicBinder = new MusicBinder();
    private final Handler handler = new Handler(Looper.getMainLooper());

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
        gson = new GsonBuilder()
                .registerTypeAdapter(Uri.class, new UriInstanceCreator())
                .create();
        listType = new TypeToken<List<Song>>() {
        }.getType();
        mediaSession = new MediaSessionCompat(this, "MusicService");

    }

    @Override
    public int onStartCommand(@NonNull Intent intent, int flags, int startId) {

        Bundle bundle = null;
        String actionMusic = "";
        if (intent != null) {
            bundle = intent.getExtras();
            actionMusic = intent.getAction();
        }

        if (bundle != null) {
            position = bundle.getInt("position");

            String listSongJson = bundle.getString("listSong");
            if (listSongJson != null && !listSongJson.isEmpty()) {
                listSong = gson.fromJson(listSongJson, listType);
                if (listSong != null && !listSong.isEmpty()) {

                    song = listSong.get(position);
                }

            }

        }
        assert actionMusic != null;
        handleActionMusic(actionMusic);


        return START_STICKY;
    }

    public void setSongImage() {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        Handler handler = new Handler(Looper.getMainLooper());
        executorService.execute(() -> {
            Uri uri = song.getUriSong();
            Bitmap albumArt = getAlbumArt(context.getContentResolver(),uri);
            if (albumArt != null) {
                handler.post(() -> {
                    song.setImg(albumArt);
                    sendNotificationMedia(song);
                    sendActionToActivity(START);
                });
            }

        });
    }

    private void handleActionMusic(String action) {
        switch (action) {
            case START:
                if (song != null) {
                    if (song.getUriSong() != null && !song.getUriSong().toString().isEmpty()) {
                        startMusic(song.getUriSong());
                    }
                }
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
        if (mediaPlayer != null) {

            setSongImage();

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


        Bitmap bitmap;
        if (song.getImg() != null) {
            bitmap = song.getImg();
        } else
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.img_music_notification);

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
                clearIntent, PendingIntent.FLAG_IMMUTABLE);

        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_mymusic_app)
                .setSubText("My Music")
                .setContentTitle(song.getName())
                .setContentText(song.getArtist())
                .setLargeIcon(bitmap)
                .addAction(R.drawable.ic_previous, "Previous", previousPendingIntent)
                .addAction(icon, "Play,Pause", playPausePendingIntent)
                .addAction(R.drawable.ic_next, "Next", nextPendingIntent)
                .addAction(R.drawable.ic_clear, "Clear", clearPendingIntent)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(1)
                        .setMediaSession(mediaSession.getSessionToken())
                )
                .build();

        startForeground(1, notification);
    }


    private void initMediaSession() {


        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onSeekTo(long pos) {
                super.onSeekTo(pos);
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

    public void setProgress(int progress) {
        mediaPlayer.seekTo(progress);
    }

    public int getCurrentPosMediaPlayer() {
        return mediaPlayer.getCurrentPosition();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (mediaSession != null) {
            mediaSession.release();
        }
    }

    public static class UriInstanceCreator implements InstanceCreator<Uri> {
        @Override
        public Uri createInstance(Type type) {
            return Uri.EMPTY; // Hoặc một Uri mặc định nào đó
        }
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
