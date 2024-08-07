package com.lampro.mymusic.views.activities;

import static com.lampro.mymusic.utils.MusicService.CLEAR;
import static com.lampro.mymusic.utils.MusicService.MUTED;
import static com.lampro.mymusic.utils.MusicService.PAUSE;
import static com.lampro.mymusic.utils.MusicService.PLAY;
import static com.lampro.mymusic.utils.MusicService.SEND_ACTION_TO_ACTIVITY;
import static com.lampro.mymusic.utils.MusicService.START;
import static com.lampro.mymusic.utils.MusicService.UNMUTED;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.Settings;
import android.view.View;
import android.widget.SeekBar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lampro.mymusic.R;
import com.lampro.mymusic.adapters.ViewPagerAdapter;
import com.lampro.mymusic.base.BaseActivity;
import com.lampro.mymusic.databinding.ActivityMainBinding;
import com.lampro.mymusic.interfaces.IOnClickItemSong;
import com.lampro.mymusic.model.Song;
import com.lampro.mymusic.utils.MusicService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements View.OnClickListener, IOnClickItemSong {

    public static final int MY_REQUEST_CODE = 10;
    public static final int REQUEST_FOREGROUND_SERVICE_MEDIA_PLAYBACK = 1;

    private BottomNavigationView navigationView;
    private ViewPager2 vpContent;

    private OnRequestPermission onRequestPermission;
    private ActivityResultLauncher<Intent> getResult;

    private Handler handler = new Handler();
    private Runnable updateSeekBar;

    private boolean isMuted = false;


    private List<Song> playlistPlaying;
    private int positionPlaying;
    private Song songPlaying;
    private boolean isPlaying;


    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            assert bundle != null;
            songPlaying = bundle.getParcelable("songPlaying");
            isPlaying = bundle.getBoolean("isPlaying");
            isMuted = bundle.getBoolean("isMuted");

            String actionMusic = intent.getAction();
            assert actionMusic != null;
            handlerActionMusic(actionMusic);

        }
    };


    @Override
    protected ActivityMainBinding inflateBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(SEND_ACTION_TO_ACTIVITY));

        initView();
        listener();


        getResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_CANCELED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            if (Environment.isExternalStorageManager()) {
                                onRequestPermission.onRequestSuccess();

                            }
                        } else {
                            // Các quyền khác cho Android dưới 11
                            if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_MEDIA_AUDIO)
                                    == PackageManager.PERMISSION_GRANTED) {
                                onRequestPermission.onRequestSuccess();
                            }
                        }
                    }
//
                }
        );

    }

    private void initView() {


        vpContent = binding.vpContent;
        vpContent.setAdapter(new ViewPagerAdapter(this));
        vpContent.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        vpContent.setUserInputEnabled(false);

        navigationView = binding.navBottom;

    }

    private void listener() {
        navigationView.setOnItemSelectedListener(v -> {


            if (v.getItemId() == R.id.discover) {
                vpContent.setCurrentItem(0);
                setViewGone();
            } else if (v.getItemId() == R.id.liked) {
                vpContent.setCurrentItem(1);
                setViewGone();
            } else if (v.getItemId() == R.id.playlist) {
                vpContent.setCurrentItem(2);
                setViewGone();
            } else {
                vpContent.setCurrentItem(3);
                setViewGone();
            }

            return true;
        });


        binding.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                if (fromUser && mediaPlayer != null) {
//                    mediaPlayer.seekTo(progress);
//                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

//      Cap nhat seekbar theo tien trinh bai nhac
//        updateSeekBar = new Runnable() {
//            @Override
//            public void run() {
//                if (mediaPlayer != null) {
//                    binding.seekbar.setProgress(mediaPlayer.getCurrentPosition());
//                }
//                handler.postDelayed(this, 1000);
//            }
//        };

        binding.tvSongName.setSelected(true);
        binding.cvImgPlaying.setOnClickListener(this);
        binding.llTitlePlaying.setOnClickListener(this);
        binding.ibtnPlayOrPause.setOnClickListener(this);
        binding.ibtnVolume.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.cv_img_playing || v.getId() == R.id.ll_title_playing) {
            Intent intent = new Intent(MainActivity.this, MusicPlayerActivity.class);
            startActivity(intent);
        }

        if (v.getId() == R.id.ibtn_volume) {
            if (isMuted) {
                sendActionToService(UNMUTED);
            } else {
                sendActionToService(MUTED);
            }
        }
        if (v.getId() == R.id.ibtn_play_or_pause) {
            if (isPlaying) {
                sendActionToService(PAUSE);
            } else {
                sendActionToService(PLAY);
            }

        }
    }


    public void setVisibility() {
        binding.fragmentContainer.setVisibility(View.VISIBLE);
    }

    public void setViewGone() {
        binding.fragmentContainer.setVisibility(View.GONE);
        getSupportFragmentManager()
                .popBackStack();
    }

    public void checkSelfPermission() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                getResult.launch(intent);
            } else {
                onRequestPermission.onRequestSuccess();
            }
        } else {
            // Các quyền khác cho Android dưới 11
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO)
                    == PackageManager.PERMISSION_GRANTED) {
                onRequestPermission.onRequestSuccess();

            } else {
                String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(this, permissions, MY_REQUEST_CODE);
            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    onRequestPermission.onRequestSuccess();
                }

            } else {

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onRequestPermission.onRequestSuccess();
                }
            }

        }

        if (requestCode == REQUEST_FOREGROUND_SERVICE_MEDIA_PLAYBACK) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startSong(playlistPlaying, positionPlaying);
            }
        }


    }


    private void checkSelfPermissionPlayMusic() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Đối với Android 13 trở lên
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.READ_MEDIA_AUDIO,
                                Manifest.permission.POST_NOTIFICATIONS},
                        REQUEST_FOREGROUND_SERVICE_MEDIA_PLAYBACK);
            } else {
                startSong(playlistPlaying, positionPlaying);
            }
        } else {
            // Đối với Android 10 đến 12
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_FOREGROUND_SERVICE_MEDIA_PLAYBACK);
            } else {
                startSong(playlistPlaying, positionPlaying);
            }
        }
    }

    @Override
    public void playSong(List<Song> listSong, int position) {

        playlistPlaying = listSong;
        positionPlaying = position;
        checkSelfPermissionPlayMusic();
    }

    private void startSong(List<Song> listSong, int position) {
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction(START);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("listSong", (ArrayList<? extends Parcelable>) listSong);
        bundle.putInt("position", position);
        intent.putExtras(bundle);
        startForegroundService(intent);

    }


    public interface OnRequestPermission {
        void onRequestSuccess();
    }

    public void setCallback(OnRequestPermission onRequestPermission) {
        this.onRequestPermission = onRequestPermission;
    }


    public void handlerActionMusic(String action) {
        switch (action) {
            case START:
                binding.setSongPlaying(songPlaying);
                binding.llFramePlaying.setVisibility(View.VISIBLE);
                binding.llFramePlaying.setVisibility(View.GONE);
                setStatustButtonPlayOrPause();
                break;
            case PLAY:
                setStatustButtonPlayOrPause();
                break;
            case PAUSE:
                setStatustButtonPlayOrPause();
                break;
            case MUTED:
                setStatustButtonMute();
                break;
            case UNMUTED:
                setStatustButtonMute();
                break;
            case CLEAR:
                binding.llFramePlaying.setVisibility(View.GONE);
                break;
        }
    }

    private void setStatustButtonPlayOrPause() {
        if (isPlaying) {
            binding.ibtnPlayOrPause.setImageResource(R.drawable.pause);
        } else {
            binding.ibtnPlayOrPause.setImageResource(R.drawable.play);
        }
    }

    private void setStatustButtonMute() {
        if (isMuted) {
            binding.ibtnPlayOrPause.setImageResource(R.drawable.mute);
        } else {
            binding.ibtnPlayOrPause.setImageResource(R.drawable.volume_high);
        }
    }

    private void sendActionToService(String action) {
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction(action);
        startForegroundService(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        handler.removeCallbacks(updateSeekBar);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }
}