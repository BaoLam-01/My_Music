package com.lampro.mymusic.views.activities;

import static com.lampro.mymusic.utils.MusicService.CLEAR;
import static com.lampro.mymusic.utils.MusicService.MUTED;
import static com.lampro.mymusic.utils.MusicService.PAUSE;
import static com.lampro.mymusic.utils.MusicService.PLAY;
import static com.lampro.mymusic.utils.MusicService.SEND_ACTION_TO_ACTIVITY;
import static com.lampro.mymusic.utils.MusicService.START;
import static com.lampro.mymusic.utils.MusicService.UNMUTED;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SeekBar;

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

public class MusicPlayerActivity extends BaseActivity<ActivityMusicPlayerBinding> implements View.OnClickListener {


    private boolean isMuted;
    private boolean isPlaying;
    private Song songPlaying;

    private MusicService musicService;
    private boolean isConnected = false;

    private Runnable updateSeekBar;
    private final Handler handler = new Handler();

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder musicBinder = (MusicService.MusicBinder) service;
            musicService = musicBinder.getMusicService();
            if (musicService.getMediaPlayer() != null) {
                musicService.sendActionToActivity(START);
            }
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
                initView();
                if (musicService.getMediaPlayer() != null) {
                    binding.seekbar.setMax(musicService.getMediaPlayer().getDuration());
                }
                updateSeekBar.run();
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
                handler.removeCallbacks(updateSeekBar);
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


        initView();

        bindMusicService();

        listener();

        setBackgroundBlur();


    }

    private void setBackgroundBlur() {
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

    private void initView() {
        if (songPlaying != null) {

            binding.setSongPlaying(songPlaying);

        }
    }

    private void listener() {
        updateSeekBar();

        binding.imgIcPrevious.setOnClickListener(this);
        binding.imgIcNext.setOnClickListener(this);
        binding.imgIcPlayOrPause.setOnClickListener(this);
        binding.imgVlHight.setOnClickListener(this);
        binding.ibtnHide.setOnClickListener(this);


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
        if ((v.getId() == R.id.ibtn_hide)) {
            finish();
            overridePendingTransition(R.anim.slide_down,R.anim.fade_out);
        }
    }

    private void bindMusicService() {
        Intent musicIntent = new Intent(this, MusicService.class);
        bindService(musicIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(SEND_ACTION_TO_ACTIVITY));
    }

    private void updateSeekBar() {
        binding.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && musicService != null) {
                    musicService.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

//      Cap nhat seekbar theo tien trinh bai nhac
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (musicService != null) {
                    binding.seekbar.setProgress(musicService.getCurrentPosMediaPlayer());
                }
                handler.postDelayed(this, 1000);
            }
        };
    }

    @Override
    public void finish() {
        super.finish();
//        overridePendingTransition(R.anim.fade_out, R.anim.slide_down);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateSeekBar);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        if (isConnected) {
            isConnected = false;
            unbindService(serviceConnection);
        }

    }


}