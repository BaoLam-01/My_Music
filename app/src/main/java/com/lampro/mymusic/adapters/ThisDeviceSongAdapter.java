package com.lampro.mymusic.adapters;

import android.net.Uri;
import android.view.View;

import androidx.annotation.NonNull;

import com.lampro.mymusic.R;
import com.lampro.mymusic.base.BaseRecyclerViewAdapter;
import com.lampro.mymusic.databinding.ItemSongBinding;
import com.lampro.mymusic.interfaces.IOnClickItemSong;
import com.lampro.mymusic.model.Song;

public class ThisDeviceSongAdapter  extends BaseRecyclerViewAdapter<Song, ItemSongBinding> {

    private IOnClickItemSong iOnClickItemSong;

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
                iOnClickItemSong.playSong(mlistAdapter,holder.getAdapterPosition());
            }
        });
    }

}
