package com.lampro.mymusic.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.lampro.mymusic.databinding.ItemMadeForYouBinding;


public class BaseRecyclerViewAdapter<T, VBD extends ViewDataBinding>
        extends RecyclerView.Adapter<BaseRecyclerViewAdapter.BaseViewHolder<VBD>> {

    @NonNull
    @Override
    public BaseViewHolder<VBD> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<VBD> holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    // BaseViewHolder class
    public static abstract class BaseViewHolder<VBD extends ViewDataBinding> extends RecyclerView.ViewHolder {
        public final VBD binding;

        public BaseViewHolder(VBD binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
