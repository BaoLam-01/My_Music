package com.lampro.mymusic.adapters;



import androidx.annotation.NonNull;

import com.lampro.mymusic.R;
import com.lampro.mymusic.base.BaseRecyclerViewAdapter;
import com.lampro.mymusic.databinding.ItemPopularSingerBinding;
import com.lampro.mymusic.model.Singer;

public class PopularSingerAdapter extends BaseRecyclerViewAdapter<Singer, ItemPopularSingerBinding> {
    @Override
    public int getLayout() {
        return R.layout.item_popular_singer;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<ItemPopularSingerBinding> holder, int position) {
        holder.binding.setItemPopularSinger(mlistAdapter.get(position));

    }
}
