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
import com.lampro.mymusic.databinding.ItemMadeForYouBinding;

import java.util.List;

public class MadeForYouAdapter extends RecyclerView.Adapter<MadeForYouAdapter.MadeForYouViewHolder>{
    private List<String> mlist;

    public MadeForYouAdapter() {
    }

    public MadeForYouAdapter(List<String> mlist) {
        this.mlist = mlist;
    }

    public void updateData(List<String> a) {
        mlist = a;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MadeForYouViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_made_for_you,parent,false);
        ItemMadeForYouBinding binding = ItemMadeForYouBinding.bind(view);
//        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_made_for_you, parent, false);
        return new MadeForYouViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MadeForYouViewHolder holder, int position) {
        holder.binding.imgMfyItem.setImageResource(R.drawable.artist_1);

    }

    @Override
    public int getItemCount() {
        if (mlist != null) {
            return mlist.size();
        }
        return 0;
    }

    class MadeForYouViewHolder extends RecyclerView.ViewHolder{
        ItemMadeForYouBinding binding;

        public MadeForYouViewHolder(ItemMadeForYouBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }


}
