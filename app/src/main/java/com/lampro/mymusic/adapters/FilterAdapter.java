package com.lampro.mymusic.adapters;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.lampro.mymusic.R;
import com.lampro.mymusic.base.BaseRecyclerViewAdapter;
import com.lampro.mymusic.databinding.ItemFilterPlaylistBinding;

public class FilterAdapter extends BaseRecyclerViewAdapter<String, ItemFilterPlaylistBinding> {
    @Override
    public int getLayout() {
        return R.layout.item_filter_playlist;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<ItemFilterPlaylistBinding> holder, int position) {
        holder.binding.setItemFilter(mlistAdapter.get(position));
        holder.binding.itemBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }
}
