package com.lampro.mymusic.adapters;

import android.view.View;

import androidx.annotation.NonNull;

import com.lampro.mymusic.R;
import com.lampro.mymusic.base.BaseRecyclerViewAdapter;
import com.lampro.mymusic.databinding.ItemPlaylistForListBinding;
import com.lampro.mymusic.model.Playlist;

public class PlaylistAdapter extends BaseRecyclerViewAdapter<Playlist, ItemPlaylistForListBinding> {
    private IOnPlaylistItemClickListener iOnPlaylistItemClickListener;

    public PlaylistAdapter(IOnPlaylistItemClickListener iOnPlaylistItemClickListener) {
        this.iOnPlaylistItemClickListener = iOnPlaylistItemClickListener;
    }

    @Override
    public int getLayout() {
        return R.layout.item_playlist_for_list;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<ItemPlaylistForListBinding> holder, int position) {
        holder.binding.setItemPlaylist(mlistAdapter.get(position));
        holder.binding.itemBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Playlist p = mlistAdapter.get(position);
                iOnPlaylistItemClickListener.onItemPlaylistClickListener(p);
            }
        });
    }
    public interface IOnPlaylistItemClickListener {
        void onItemPlaylistClickListener(Playlist playlist);
    }

}

