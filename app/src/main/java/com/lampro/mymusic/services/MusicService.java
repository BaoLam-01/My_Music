package com.lampro.mymusic.services;

import static com.lampro.mymusic.MyApplication.CHANNEL_ID;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ForegroundServiceStartNotAllowedException;
import android.app.ForegroundServiceTypeException;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.text.Editable;
import android.util.Xml;

import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.annotation.XmlRes;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.session.MediaSession;
import androidx.media3.session.MediaStyleNotificationHelper;

import com.lampro.mymusic.R;
import com.lampro.mymusic.model.Song;
import com.lampro.mymusic.views.activities.MainActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MusicService extends Service {

    private MediaPlayer mediaPlayer;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Song song = intent.getParcelableExtra("song");
        if (intent.getAction() == null) {
            assert song != null;
            startMusic(song.getUriSong());
        } else if (intent.getAction() == "PLAY_PAUSE") {

        }
        sendNotificationMedia(song);

        return START_STICKY;
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
    }

    private void playPauseMusic() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }
        }
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
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.diem_xua);

        int icon = R.drawable.play;
        Intent intent = new Intent(this, MusicService.class);

        if (mediaPlayer.isPlaying()) {
            icon = R.drawable.pause;
            intent.setAction("PAUSE_MUSIC");
        } else {
            icon = R.drawable.play;
            intent.setAction("PLAY_MUSIC");
        }


        PendingIntent pausePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_ic_mymusic_app)
                .setSubText("My Music")
                .setContentTitle(song.getName())
                .setContentText(song.getArtist())
                .setLargeIcon(bitmap)
                .setSound(null)
                .addAction(R.drawable.ic_previous, "Previous", null)
                .addAction(R.drawable.play, "Play,Pause", null)
                .addAction(R.drawable.ic_next, "Next", null)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(1)
                )
                .build();

        startForeground(1,notification);

//
//        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
//
//            managerCompat.notify(1,notification);
//        }

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
