package com.lampro.mymusic.views.activities;

import static com.lampro.mymusic.MyApplication.CHANNEL_ID;

import android.Manifest;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.provider.Settings;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.session.MediaSession;
import androidx.media3.session.MediaStyleNotificationHelper;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lampro.mymusic.DataRepository;
import com.lampro.mymusic.R;
import com.lampro.mymusic.adapters.ViewPagerAdapter;
import com.lampro.mymusic.base.BaseActivity;
import com.lampro.mymusic.databinding.ActivityMainBinding;
import com.lampro.mymusic.interfaces.IOnClickItemSong;
import com.lampro.mymusic.model.Song;
import com.lampro.mymusic.services.MusicService;

import java.util.List;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements View.OnClickListener, IOnClickItemSong {

    public static final int MY_REQUEST_CODE = 10;

    private BottomNavigationView navigationView;
    private ViewPager2 vpContent;

    private OnRequestPermission onRequestPermission;
    private ActivityResultLauncher<Intent> getResult;
//    private MediaPlayer mediaPlayer;

    private Handler handler = new Handler();
    private Runnable updateSeekBar;

    private boolean isMuted = false;


    private MusicService musicService;
    private boolean isBound = false;


    @Override
    public void sendOrderedBroadcast(Intent intent, @Nullable String receiverPermission) {
        super.sendOrderedBroadcast(intent, receiverPermission);
        if (intent.getAction() != null && intent.getAction().equals("NEXT_MUSIC")) {
            Intent nextIntent = new Intent(this, MusicService.class);
            nextIntent.setAction("NEXT");
            this.startService(nextIntent);
            Toast.makeText(this, "Playing next song", Toast.LENGTH_SHORT).show();
        }

        if (intent.getAction() != null && intent.getAction().equals("PAUSE_MUSIC")) {
            Intent nextIntent = new Intent(this, MusicService.class);
            Toast.makeText(this, "Pause", Toast.LENGTH_SHORT).show();
            binding.ibtnPause.setImageResource(R.drawable.pause);
        }

        if (intent.getAction() != null && intent.getAction().equals("PLAY_MUSIC")) {
            Intent nextIntent = new Intent(this, MusicService.class);
            Toast.makeText(this, "Play", Toast.LENGTH_SHORT).show();
            binding.ibtnPause.setImageResource(R.drawable.play);
        }
    }


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };
    @Override
    protected ActivityMainBinding inflateBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initView();
        listener();


        getResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
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
                }
        );

    }

    private void initView() {


        vpContent = binding.vpContent;
        vpContent.setAdapter(new ViewPagerAdapter(this));
        vpContent.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        vpContent.setUserInputEnabled(false);

        navigationView = binding.navBottom;

//        if (mediaPlayer == null) {
//            binding.llFramePlaying.setVisibility(View.GONE);
//        } else {
//            binding.llFramePlaying.setVisibility(View.VISIBLE);
//        }
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
        binding.ibtnPause.setOnClickListener(this);
        binding.ibtnVolume.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.cv_img_playing || v.getId() == R.id.ll_title_playing) {
            Intent intent = new Intent(MainActivity.this, MusicPlayerActivity.class);
            startActivity(intent);
        }

//        if (v.getId() == R.id.ibtn_volume) {
//            if (mediaPlayer != null) {
//                if (!isMuted) {
//                    mediaPlayer.setVolume(0f, 0f);
//                    binding.ibtnVolume.setImageResource(R.drawable.mute);
//                } else {
//                    mediaPlayer.setVolume(1f, 1f);
//                    binding.ibtnVolume.setImageResource(R.drawable.volume_high);
//                }
//                isMuted = !isMuted;
//            }
//        }
//        if (v.getId() == R.id.ibtn_pause) {
//            if (mediaPlayer != null) {
//                if (mediaPlayer.isPlaying()) {
//                    mediaPlayer.pause();
//                    binding.ibtnPause.setImageResource(R.drawable.play);
//                } else {
//                    mediaPlayer.start();
//                    binding.ibtnPause.setImageResource(R.drawable.pause);
//                }
//            }
//
//        }
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_MEDIA_AUDIO};
                }
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


    }

    @Override
    public void playSong(List<Song> listSong, int position) {

        Intent intent = new Intent(this, MusicService.class);
        Song s = listSong.get(position);
        intent.setAction(null);
        intent.putExtra("song", s);
        startService(intent);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);

//        if (mediaPlayer != null) {
//            mediaPlayer.release();
//        }
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
//            Uri uriSong = listSong.get(position).getUriSong();
//
//            mediaPlayer = MediaPlayer.create(MainActivity.this, uriSong);
//            mediaPlayer.setVolume(1f, 1f);
//            mediaPlayer.setLooping(false);
//            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    int vt = (position + 1) % listSong.size();
//                    playSong(listSong, vt);
//                }
//            });
//            binding.seekbar.setMax(mediaPlayer.getDuration());
//            mediaPlayer.start();
//            handler.post(updateSeekBar);
//            binding.setSongPlaying(listSong.get(position));
//            binding.llFramePlaying.setVisibility(View.VISIBLE);
//        }
    }


    public interface OnRequestPermission {
        void onRequestSuccess();
    }

    public void setCallback(OnRequestPermission onRequestPermission) {
        this.onRequestPermission = onRequestPermission;
    }


    @OptIn(markerClass = UnstableApi.class)
    private void sendNotificationMedia() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.son_tung);


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_small_music)
                .setSubText("LamPro")
                .setContentTitle("Title of song")
                .setContentText("Singe of song")
                .setLargeIcon(bitmap)
                .addAction(R.drawable.ic_previous, "Previous", null)
                .addAction(R.drawable.ic_pause, "Play,Pause", null)
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
    protected void onDestroy() {
        super.onDestroy();
//        if (mediaPlayer != null) {
//            mediaPlayer.release();
//        }
        handler.removeCallbacks(updateSeekBar);
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }
}