package com.lampro.mymusic.views.fragments;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.View;
import android.widget.Toast;

import com.lampro.mymusic.R;
import com.lampro.mymusic.adapters.FilterAdapter;
import com.lampro.mymusic.adapters.PlaylistAdapter;
import com.lampro.mymusic.base.BaseFragment;
import com.lampro.mymusic.databinding.FragmentPlaylistBinding;
import com.lampro.mymusic.model.Playlist;
import com.lampro.mymusic.utils.CustomItemDecoration;
import com.lampro.mymusic.viewmodels.playlistviewmodel.PlaylistViewModel;
import com.lampro.mymusic.viewmodels.playlistviewmodel.PlaylistViewModelFactory;
import com.lampro.mymusic.views.activities.MainActivity;

import java.util.List;


public class PlaylistFragment extends BaseFragment<FragmentPlaylistBinding> implements PlaylistAdapter.IOnPlaylistItemClickListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private PlaylistViewModel mPlaylistViewModel;
    private FilterAdapter mFilterAdapter;
    private PlaylistAdapter mPlaylistAdapter;


    public PlaylistFragment() {
    }


    public static PlaylistFragment newInstance(String param1, String param2) {
        PlaylistFragment fragment = new PlaylistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    protected FragmentPlaylistBinding inflateBinding() {
        return FragmentPlaylistBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViewHolder();
        initView();

        mPlaylistViewModel.liveDataListFilter.observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                mFilterAdapter.updateData(strings);
            }
        });

        mPlaylistViewModel.liveDataListPlaylist.observe(getViewLifecycleOwner(), new Observer<List<Playlist>>() {
            @Override
            public void onChanged(List<Playlist> playlists) {
                mPlaylistAdapter.updateData(playlists);
            }
        });

    }

    private void initViewHolder() {
        if (getActivity() != null) {
            Application application = getActivity().getApplication();
            PlaylistViewModelFactory playlistViewModelFactory = new PlaylistViewModelFactory(application);
            mPlaylistViewModel = new ViewModelProvider(this, playlistViewModelFactory).get(PlaylistViewModel.class);
        }
    }

    private void initView() {
        int marginInPx = getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._12sdp);
        CustomItemDecoration customItemDecoration = new CustomItemDecoration(marginInPx);

        binding.rvFilter.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvPlaylist.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));

        mFilterAdapter = new FilterAdapter();
        mPlaylistViewModel.setDataListFilter();
        binding.rvFilter.setAdapter(mFilterAdapter);
        binding.rvFilter.addItemDecoration(customItemDecoration);

        mPlaylistAdapter = new PlaylistAdapter(this);
        mPlaylistViewModel.setDataListPlaylist();
        binding.rvPlaylist.setAdapter(mPlaylistAdapter);


    }

    @Override
    public void onItemPlaylistClickListener(Playlist playlist) {
        if (playlist.getTitle() == "This device") {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new ThisDeviceFragment(), null);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.setVisibility();
        } else {
            return;
        }


    }
}