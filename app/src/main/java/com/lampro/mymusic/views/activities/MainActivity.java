package com.lampro.mymusic.views.activities;

import static com.lampro.mymusic.utils.BindingUtils.loadUserCircleImg;
import static com.lampro.mymusic.utils.MusicService.CLEAR;
import static com.lampro.mymusic.utils.MusicService.MUTED;
import static com.lampro.mymusic.utils.MusicService.PAUSE;
import static com.lampro.mymusic.utils.MusicService.PLAY;
import static com.lampro.mymusic.utils.MusicService.SEND_ACTION_TO_ACTIVITY;
import static com.lampro.mymusic.utils.MusicService.START;
import static com.lampro.mymusic.utils.MusicService.UNMUTED;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.window.OnBackInvokedDispatcher;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.ViewDataBinding;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewbinding.ViewBinding;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lampro.mymusic.R;
import com.lampro.mymusic.adapters.ViewPagerAdapter;
import com.lampro.mymusic.base.BaseActivity;
import com.lampro.mymusic.databinding.ActivityMainBinding;
import com.lampro.mymusic.databinding.LayoutHeaderNavBinding;
import com.lampro.mymusic.interfaces.IOnClickItemSong;
import com.lampro.mymusic.model.Song;
import com.lampro.mymusic.utils.MusicService;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements View.OnClickListener,
        IOnClickItemSong, NavigationView.OnNavigationItemSelectedListener {

    public static final int MY_REQUEST_CODE = 10;
    public static final int REQUEST_FOREGROUND_SERVICE_MEDIA_PLAYBACK = 1;

    private Toolbar toolbar;
    private NavigationView navigationView;

    private BottomNavigationView bottomNav;
    private ViewPager2 vpContent;

    private OnRequestPermission onRequestPermission;
    private ActivityResultLauncher<Intent> getResult;

    private Handler handler = new Handler();
    private Runnable updateSeekBar;
    private boolean isMuted = false;


    private String listSongPlaying;
    private int positionPlaying;
    private Song songPlaying;
    private boolean isPlaying;

    private MusicService musicService;
    private Boolean isServiceConnected;


    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            binding.llFramePlaying.setVisibility(View.VISIBLE);
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

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isServiceConnected = true;
            MusicService.MusicBinder musicBinder = (MusicService.MusicBinder) service;
            musicService = musicBinder.getMusicService();
            if (musicService.getMediaPlayer() != null) {
                musicService.sendActionToActivity(START);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceConnected = false;
            musicService = null;
        }
    };

    @Override
    protected ActivityMainBinding inflateBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bindMusicService();

        initView();
        listener();
        bottomNav.setSelectedItemId(R.id.discover);


        getResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_CANCELED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            if (Environment.isExternalStorageManager()) {
                                onRequestPermission.onRequestSuccess();

                            }
                        }
                    }
//
                }
        );

    }

    private void bindMusicService() {
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(SEND_ACTION_TO_ACTIVITY));
    }

    private void initView() {
        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        navigationView = binding.navigationView;


        vpContent = binding.vpContent;
        vpContent.setAdapter(new ViewPagerAdapter(this));
        vpContent.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        vpContent.setUserInputEnabled(false);

        bottomNav = binding.navBottom;

        showUserInfor();

    }

    private void showUserInfor() {

        TextView tvName = navigationView.getHeaderView(0).findViewById(R.id.tv_user_namne);
        TextView tvEmail = navigationView.getHeaderView(0).findViewById(R.id.tv_email);
        ImageView imgAvata = navigationView.getHeaderView(0).findViewById(R.id.img_avatar);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        String userName = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();

        tvName.setText(userName);
        tvEmail.setText(email);
        Glide.with(this).load(photoUrl).error(R.drawable.user_default).into(imgAvata);
    }

    private void listener() {
        binding.imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.drawerLayout.openDrawer(GravityCompat.END);
            }
        });

//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                binding.drawerLayout.openDrawer(GravityCompat.END);
//            }
//        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getOnBackInvokedDispatcher().registerOnBackInvokedCallback(
                    OnBackInvokedDispatcher.PRIORITY_DEFAULT,
                    () -> {
                        // Xử lý sự kiện quay lại
                        if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
                            binding.drawerLayout.closeDrawer(GravityCompat.END);
                        } else {
                            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {

                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                // Khởi động Activity mới
                                startActivity(intent);
                            } else {
                                getSupportFragmentManager().popBackStack();
                            }
                        }
                    }
            );
        }

        navigationView.setNavigationItemSelectedListener(this);

        bottomNav.setOnItemSelectedListener(v -> {


            if (v.getItemId() == R.id.discover) {
                vpContent.setCurrentItem(0);
                setViewToolBar(R.id.discover);
                setViewGone();
            } else if (v.getItemId() == R.id.liked) {
                vpContent.setCurrentItem(1);
                setViewToolBar(R.id.liked);
                setViewGone();
            } else if (v.getItemId() == R.id.playlist) {
                vpContent.setCurrentItem(2);
                setViewToolBar(R.id.playlist);
                setViewGone();
            } else {
                vpContent.setCurrentItem(3);
                setViewToolBar(R.id.setting);
                setViewGone();
            }

            return true;
        });


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
                if (musicService.getMediaPlayer() != null) {
                    binding.seekbar.setProgress(musicService.getCurrentPosMediaPlayer());
                }
                handler.postDelayed(this, 1000);
            }
        };

        binding.tvSongName.setSelected(true);
        binding.cvImgPlaying.setOnClickListener(this);
        binding.llTitlePlaying.setOnClickListener(this);
        binding.ibtnPlayOrPause.setOnClickListener(this);
        binding.ibtnVolume.setOnClickListener(this);

    }

    private void setViewToolBar(int id) {
        if (id == R.id.discover) {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            if (hour < 12) {

                binding.tvToolbar.setText(R.string.good_morning);
            } else if (hour < 18) {

                binding.tvToolbar.setText(R.string.good_afternoon);
            } else {
                binding.tvToolbar.setText(R.string.good_evening);
            }

            setVisibleAppbarLayout();
        } else if (id == R.id.liked) {

            binding.tvToolbar.setText(R.string.liked);
            setVisibleAppbarLayout();

        } else if (id == R.id.playlist) {
            binding.tvToolbar.setText(R.string.playlists);
            setVisibleAppbarLayout();

        } else {
            binding.tvToolbar.setText(R.string.settings);
            setVisibleAppbarLayout();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_account) {

        } else if (id == R.id.news) {

        } else if (id == R.id.history) {

        } else {
            FirebaseAuth.getInstance().signOut();
            goToLogin();
        }
        binding.drawerLayout.closeDrawer(GravityCompat.END);
        return true;
    }

    private void goToLogin() {
        Intent intent = new Intent(this, LoginAndRegisterActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.cv_img_playing || v.getId() == R.id.ll_title_playing) {
            Intent intent = new Intent(MainActivity.this, MusicPlayerActivity.class);
            startActivity(intent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                overridePendingTransition(R.anim.slide_up | R.anim.fade_in, 0);
            }

        }


        if (v.getId() == R.id.ibtn_volume) {
            if (isMuted) {
                musicService.unMutedMusic();
            } else {
                musicService.mutedMusic();
            }
        }
        if (v.getId() == R.id.ibtn_play_or_pause) {
            if (isPlaying) {
                musicService.pauseMusic();
            } else {
                musicService.playMusic();
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

    public void setHideAppbarLayout() {
        binding.appbar.setVisibility(View.GONE);
    }

    public void setVisibleAppbarLayout() {
        binding.appbar.setVisibility(View.VISIBLE);
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

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onRequestPermission.onRequestSuccess();
            }
        }

        if (requestCode == REQUEST_FOREGROUND_SERVICE_MEDIA_PLAYBACK) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startSong();
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
                startSong();
            }
        } else {
            // Đối với Android 10 đến 12
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_FOREGROUND_SERVICE_MEDIA_PLAYBACK);
            } else {
                startSong();
            }
        }
    }

    @Override
    public void playSong(String listSong, int position) {

        listSongPlaying = listSong;
        positionPlaying = position;
        checkSelfPermissionPlayMusic();

    }


    private void startSong() {
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction(START);
        Bundle bundle = new Bundle();
        bundle.putInt("position", positionPlaying);
        bundle.putString("listSong", listSongPlaying);
        intent.putExtras(bundle);
        startService(intent);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);

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
                if (musicService.getMediaPlayer() != null) {
                    binding.seekbar.setMax(musicService.getMediaPlayer().getDuration());
                }
                updateSeekBar.run();
                binding.llFramePlaying.setVisibility(View.VISIBLE);
                setStatusButtonPlayOrPause();
                setStatusButtonMute();
                break;
            case PLAY:
                setStatusButtonPlayOrPause();
                break;
            case PAUSE:
                setStatusButtonPlayOrPause();
                break;
            case MUTED:
                setStatusButtonMute();
                break;
            case UNMUTED:
                setStatusButtonMute();
                break;
            case CLEAR:
                binding.llFramePlaying.setVisibility(View.GONE);
                handler.removeCallbacks(updateSeekBar);
                if (isServiceConnected) {
                    unbindService(serviceConnection);
                    isServiceConnected = false;
                }
                break;
        }
    }

    private void setStatusButtonPlayOrPause() {
        if (isPlaying) {
            binding.ibtnPlayOrPause.setImageResource(R.drawable.pause);
        } else {
            binding.ibtnPlayOrPause.setImageResource(R.drawable.play);
        }
    }

    private void setStatusButtonMute() {
        if (isMuted) {
            binding.ibtnVolume.setImageResource(R.drawable.mute);
        } else {
            binding.ibtnVolume.setImageResource(R.drawable.volume_high);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateSeekBar);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        if (isServiceConnected) {
            unbindService(serviceConnection);
            isServiceConnected = false;
        }
    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
            binding.drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                // Khởi động Activity mới
                startActivity(intent);
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }
    }
}