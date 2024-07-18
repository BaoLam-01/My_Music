package com.lampro.mymusic.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.lampro.mymusic.R;
import com.lampro.mymusic.base.BaseRecyclerViewAdapter;
import com.lampro.mymusic.databinding.ItemMadeForYouBinding;
import com.lampro.mymusic.model.MadeForYou;

import java.util.List;

public class MadeForYouAdapter extends BaseRecyclerViewAdapter<MadeForYou,ItemMadeForYouBinding> {


    @Override
    public int getLayout() {
        return R.layout.item_made_for_you;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<ItemMadeForYouBinding> holder, int position) {
        holder.binding.setItemMadeForYou(mlistAdapter.get(position));
    }
}
