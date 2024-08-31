package com.lampro.mymusic.adapters;

import static com.lampro.mymusic.MyApplication.context;
import static com.lampro.mymusic.MyApplication.getContext;
import static com.lampro.mymusic.utils.MusicService.START;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lampro.mymusic.R;
import com.lampro.mymusic.base.BaseRecyclerViewAdapter;
import com.lampro.mymusic.databinding.ItemSongBinding;
import com.lampro.mymusic.interfaces.IGetBitmapImage;
import com.lampro.mymusic.interfaces.IOnClickItemSong;
import com.lampro.mymusic.model.Song;
import com.lampro.mymusic.model.database.RetroMusicDatabase;
import com.lampro.mymusic.utils.MusicService;
import com.lampro.mymusic.utils.PrefManager;
import com.lampro.mymusic.views.activities.MainActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThisDeviceSongAdapter extends BaseRecyclerViewAdapter<Song, ItemSongBinding> {

    private IOnClickItemSong iOnClickItemSong;
    private IGetBitmapImage iGetBitmapImage;
    Gson gson = new Gson();
    Type listType = new TypeToken<List<Song>>(){}.getType();
    String listSong;

    private final ExecutorService executorService = Executors.newFixedThreadPool(4);
    private final Handler handler = new Handler(Looper.getMainLooper());

    public ThisDeviceSongAdapter(IOnClickItemSong iOnClickItemSong, IGetBitmapImage iGetBitmapImage) {
        this.iOnClickItemSong = iOnClickItemSong;
        this.iGetBitmapImage = iGetBitmapImage;
    }

    @Override
    public void updateData(List<Song> a) {
        super.updateData(a);
        listSong = gson.toJson(a,listType);
    }

    @Override
    public int getLayout() {
        return R.layout.item_song;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<ItemSongBinding> holder, int position) {
        executorService.execute(() -> {
            Song song = mlistAdapter.get(position);
            Uri uri = song.getUriSong();
            if (iGetBitmapImage != null) {
                Bitmap albumArt = iGetBitmapImage.getBitmapImage(uri);
                if (albumArt != null) {
                    handler.post(() -> {
                        mlistAdapter.get(position).setImg(albumArt);
                        holder.binding.setItemRecentlySong(mlistAdapter.get(position));
                    });
                }
            }

        });

        holder.binding.setItemRecentlySong(mlistAdapter.get(position));
        holder.binding.itemBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iOnClickItemSong != null) {
                    PrefManager.setCurrentListPlaying(listSong);
                    Log.e("lamhaha", "onClick: " + listSong);
                    iOnClickItemSong.playSong(listSong,holder.getAdapterPosition());
                }
            }
        });
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
