package com.lampro.mymusic.adapters;

import androidx.annotation.NonNull;

import com.lampro.mymusic.R;
import com.lampro.mymusic.base.BaseRecyclerViewAdapter;
import com.lampro.mymusic.databinding.ItemMadeForYouBinding;
import com.lampro.mymusic.model.Playlist;

public class MadeForYouAdapter extends BaseRecyclerViewAdapter<Playlist,ItemMadeForYouBinding> {


    @Override
    public int getLayout() {
        return R.layout.item_made_for_you;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<ItemMadeForYouBinding> holder, int position) {
        holder.binding.setItemMadeForYou(mlistAdapter.get(position));
    }
}
