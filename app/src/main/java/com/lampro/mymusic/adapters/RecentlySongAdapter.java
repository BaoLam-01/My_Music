package com.lampro.mymusic.adapters;

import androidx.annotation.NonNull;

import com.lampro.mymusic.R;
import com.lampro.mymusic.base.BaseRecyclerViewAdapter;
import com.lampro.mymusic.databinding.ItemRecentlySongBinding;
import com.lampro.mymusic.model.Song;

public class RecentlySongAdapter extends BaseRecyclerViewAdapter<Song, ItemRecentlySongBinding> {
    @Override
    public int getLayout() {
        return R.layout.item_recently_song;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<ItemRecentlySongBinding> holder, int position) {
        holder.binding.setItemRecentlySong(mlistAdapter.get(position));
    }
}
