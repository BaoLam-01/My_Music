package com.lampro.mymusic.adapters;

import static com.lampro.mymusic.utils.MusicService.START;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.lampro.mymusic.R;
import com.lampro.mymusic.base.BaseRecyclerViewAdapter;
import com.lampro.mymusic.databinding.ItemSongBinding;
import com.lampro.mymusic.interfaces.IOnClickItemSong;
import com.lampro.mymusic.model.Song;
import com.lampro.mymusic.utils.MusicService;
import com.lampro.mymusic.views.activities.MainActivity;

import java.util.ArrayList;

public class ThisDeviceSongAdapter extends BaseRecyclerViewAdapter<Song, ItemSongBinding> {

    private IOnClickItemSong iOnClickItemSong;
    private MainActivity mainActivity;

    public ThisDeviceSongAdapter(IOnClickItemSong iOnClickItemSong) {
        this.iOnClickItemSong = iOnClickItemSong;
    }

    @Override
    public int getLayout() {
        return R.layout.item_song;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<ItemSongBinding> holder, int position) {
        holder.binding.setItemRecentlySong(mlistAdapter.get(position));
        holder.binding.itemBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iOnClickItemSong.playSong(mlistAdapter, holder.getAdapterPosition());
            }
        });
    }


}
