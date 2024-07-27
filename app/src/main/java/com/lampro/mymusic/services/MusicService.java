package com.lampro.mymusic.services;

import static com.lampro.mymusic.MyApplication.CHANNEL_ID;

import android.Manifest;
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

import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
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
    private final Binder binder = new MusicBinder();

    public class MusicBinder extends Binder{
        public MusicService getService(){
            return MusicService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Song song = intent.getParcelableExtra("song");
        if (intent.getAction() == null) {
            startMusic(song.getUriSong());
        } else if (intent.getAction() == "PLAY_PAUSE") {

        }
        sendNotificationMedia(song);

        return START_STICKY;
    }

    private void startMusic(Uri uri) {
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

    @OptIn(markerClass = UnstableApi.class)
    private void sendNotificationMedia(Song song) {
        InputStream inputStream;
        try {
            inputStream = getContentResolver().openInputStream(song.getImg());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);


        int icon;
        Intent intent = new Intent(this, MainActivity.class);

        if (mediaPlayer.isPlaying()) {
            icon = R.drawable.pause;
            intent.setAction("PAUSE_MUSIC");
        } else {
            icon = R.drawable.play;
            intent.setAction("PLAY_MUSIC");
        }


        PendingIntent pausePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_ic_mymusic_app)
                .setSubText("My Music")
                .setContentTitle(song.getName())
                .setContentText(song.getArtist())
                .setLargeIcon(bitmap)
                .addAction(R.drawable.ic_previous, "Previous", null)
                .addAction(icon, "Play,Pause", pausePendingIntent)
                .addAction(R.drawable.ic_next, "Next", null)
                .setStyle(new MediaStyleNotificationHelper.MediaStyle(getSystemService(MediaSession.class))
                        .setShowActionsInCompactView(0 /* #1: pause button */)
                        .setShowActionsInCompactView(1 /* #1: pause button */)
                        .setShowActionsInCompactView(2 /* #1: pause button */)
                ).build();

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {

            managerCompat.notify(1, notification);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
