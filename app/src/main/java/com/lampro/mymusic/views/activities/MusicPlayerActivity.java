package com.lampro.mymusic.views.activities;

import static com.lampro.mymusic.utils.MusicService.CLEAR;
import static com.lampro.mymusic.utils.MusicService.MUTED;
import static com.lampro.mymusic.utils.MusicService.PAUSE;
import static com.lampro.mymusic.utils.MusicService.PLAY;
import static com.lampro.mymusic.utils.MusicService.SEND_ACTION_TO_ACTIVITY;
import static com.lampro.mymusic.utils.MusicService.START;
import static com.lampro.mymusic.utils.MusicService.UNMUTED;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.lampro.mymusic.R;
import com.lampro.mymusic.base.BaseActivity;
import com.lampro.mymusic.databinding.ActivityMusicPlayerBinding;
import com.lampro.mymusic.model.Song;
import com.lampro.mymusic.utils.MusicService;

import eightbitlab.com.blurview.RenderEffectBlur;
import eightbitlab.com.blurview.RenderScriptBlur;

public class MusicPlayerActivity extends BaseActivity<ActivityMusicPlayerBinding> implements View.OnClickListener{


    private boolean isMuted;
    private boolean isPlaying;
    private Song songPlaying;

    private MusicService musicService;
    private boolean isConnected = false;

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder musicBinder = (MusicService.MusicBinder) service;
            musicService = musicBinder.getMusicService();
            isConnected = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
            isConnected = false;
        }

    };

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                isMuted = bundle.getBoolean("isMuted");
                isPlaying = bundle.getBoolean("isPlaying");
                songPlaying = bundle.getParcelable("songPlaying");
                String actionMusic = bundle.getString("action");
                assert actionMusic != null;
                handlerActionMusic(actionMusic);

            }

        }
    };



    private void handlerActionMusic(String actionMusic) {
        switch (actionMusic) {
            case START:
                binding.setSongPlaying(songPlaying);
                setStatusButtonPlayOrPause();
                break;
            case PLAY:
            case PAUSE:
                setStatusButtonPlayOrPause();
                break;
            case MUTED:
            case UNMUTED:
                setStatusButtonMute();
                break;
            case CLEAR:
                finish();
                break;
        }
    }

    private void setStatusButtonPlayOrPause() {
        if (isPlaying) {
            binding.imgIcPlayOrPause.setImageResource(R.drawable.pause);
        } else {
            binding.imgIcPlayOrPause.setImageResource(R.drawable.play);
        }
    }

    private void setStatusButtonMute() {
        if (isMuted) {
            binding.imgVlHight.setImageResource(R.drawable.mute);
        } else {
            binding.imgVlHight.setImageResource(R.drawable.volume_high);
        }
    }

    @Override
    protected ActivityMusicPlayerBinding inflateBinding() {
        return ActivityMusicPlayerBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        songPlaying = getIntent().getParcelableExtra("songPlaying");
        if (songPlaying != null) {
            binding.setSongPlaying(songPlaying);
        }

        bindMusicService();

        listener();

        float radius = 10f;

        View decorView = getWindow().getDecorView();
        // ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);

        // Optional:
        // Set drawable to draw in the beginning of each blurred frame.
        // Can be used in case your layout has a lot of transparent space and your content
        // gets a too low alpha value after blur is applied.
        Drawable windowBackground = decorView.getBackground();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            binding.blurView.setupWith(rootView, new RenderEffectBlur()) // or RenderEffectBlur
                    .setFrameClearDrawable(windowBackground) // Optional
                    .setBlurRadius(radius);
        }


    }

    private void listener() {
        binding.imgIcPrevious.setOnClickListener(this);
        binding.imgIcNext.setOnClickListener(this);
        binding.imgIcPlayOrPause.setOnClickListener(this);
        binding.imgVlHight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_ic_previous) {
            musicService.previousMusic();
        }
        if (v.getId() == R.id.img_ic_next) {
            musicService.nextMusic();
        }
        if (v.getId() == R.id.img_ic_play_or_pause) {
            if (isPlaying) {
                musicService.pauseMusic();
            } else {
                musicService.playMusic();
            }
        }
        if (v.getId() == R.id.img_vl_hight) {
            if (isMuted) {
                musicService.unMutedMusic();
            } else {
                musicService.mutedMusic();
            }
        }
    }

    private void bindMusicService() {
        Intent musicIntent = new Intent(this,MusicService.class);
        bindService(musicIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(SEND_ACTION_TO_ACTIVITY));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isConnected) {
            isConnected = false;
            unbindService(serviceConnection);
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }


}