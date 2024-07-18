package com.lampro.mymusic.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.lampro.mymusic.R;
import com.lampro.mymusic.databinding.ItemMadeForYouBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import kotlin.Unit;


public abstract class BaseRecyclerViewAdapter<T, VBD extends ViewDataBinding>
        extends RecyclerView.Adapter<BaseRecyclerViewAdapter.BaseViewHolder<VBD>> {

    public List<T> mlistAdapter;

//    public BaseRecyclerViewAdapter(List<T> list) {
//        this.mlistAdapter = list;
//    }

    public abstract int getLayout();
    @NonNull
    @Override
    public BaseViewHolder<VBD> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        VBD binding = DataBindingUtil.inflate(inflater, getLayout(), parent, false);
        return new BaseViewHolder<VBD>(binding);
    }

    @Override
    public abstract void onBindViewHolder(@NonNull BaseViewHolder<VBD> holder, int position);

    @Override
    public int getItemCount() {
        if (mlistAdapter != null) {
            return mlistAdapter.size();
        }
        return 0;
    }

    // BaseViewHolder class
    public static class BaseViewHolder<VBD extends ViewDataBinding> extends RecyclerView.ViewHolder {
        public final VBD binding;

        public BaseViewHolder(VBD binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void updateData(List<T> a) {
        mlistAdapter = a;
        notifyDataSetChanged();
    }

}
