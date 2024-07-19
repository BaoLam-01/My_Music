package com.lampro.mymusic.adapters;

import androidx.annotation.NonNull;

import com.lampro.mymusic.R;
import com.lampro.mymusic.base.BaseRecyclerViewAdapter;
import com.lampro.mymusic.databinding.ItemPopularSongBinding;
import com.lampro.mymusic.model.Song;

public class PopularSongAdapter extends BaseRecyclerViewAdapter<Song, ItemPopularSongBinding> {
    @Override
    public int getLayout() {
        return R.layout.item_popular_song;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<ItemPopularSongBinding> holder, int position) {
        holder.binding.setItemPopularSong(mlistAdapter.get(position));
    }
}
