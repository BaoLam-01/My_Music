package com.lampro.mymusic.views.fragments;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.View;

import com.lampro.mymusic.adapters.MadeForYouAdapter;
import com.lampro.mymusic.adapters.PopularSingerAdapter;
import com.lampro.mymusic.adapters.PopularSongAdapter;
import com.lampro.mymusic.adapters.RecentlySongAdapter;
import com.lampro.mymusic.base.BaseFragment;
import com.lampro.mymusic.databinding.FragmentDiscoverBinding;
import com.lampro.mymusic.model.Playlist;
import com.lampro.mymusic.model.Singer;
import com.lampro.mymusic.model.Song;
import com.lampro.mymusic.utils.CustomItemDecoration;
import com.lampro.mymusic.viewmodels.discoverviewmodel.DiscoverViewModel;
import com.lampro.mymusic.viewmodels.discoverviewmodel.DiscoverViewModelFactory;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscoverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoverFragment extends BaseFragment<FragmentDiscoverBinding> {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private DiscoverViewModel mDiscoverViewModel;
    private MadeForYouAdapter madeForYouAdapter;
    private PopularSingerAdapter popularSingerAdapter;
    private PopularSongAdapter popularSongAdapter;
    private RecentlySongAdapter recentlySongAdapter;



    public DiscoverFragment() {
        // Required empty public constructor
    }

    public static DiscoverFragment newInstance(String param1, String param2) {
        DiscoverFragment fragment = new DiscoverFragment();
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
    protected FragmentDiscoverBinding inflateBinding() {
        return FragmentDiscoverBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initViewModel();
        initView();


        mDiscoverViewModel.liveDataMadeForYou.observe(getViewLifecycleOwner(), new Observer<List<Playlist>>() {

            @Override
            public void onChanged(List<Playlist> madeForYous) {
                madeForYouAdapter.updateData(madeForYous);
            }
        });

        mDiscoverViewModel.liveDataPopularSinger.observe(getViewLifecycleOwner(), new Observer<List<Singer>>() {
            @Override
            public void onChanged(List<Singer> popularSingers) {
               popularSingerAdapter.updateData(popularSingers);
            }
        });

        mDiscoverViewModel.liveDataPopularSong.observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> popularSongs) {
               popularSongAdapter.updateData(popularSongs);
            }
        });

        mDiscoverViewModel.liveDataRecentlySong.observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> recentlySongs) {
               recentlySongAdapter.updateData(recentlySongs);
            }
        });

    }

    public void initViewModel() {
        if (getActivity() != null) {
            Application application = getActivity().getApplication();
            DiscoverViewModelFactory discoverViewModelFactory = new DiscoverViewModelFactory(application);
            mDiscoverViewModel = new ViewModelProvider(this, discoverViewModelFactory).get(DiscoverViewModel.class);
        }
    }
    public void initView() {
        int marginInPx = getResources().getDimensionPixelSize(com.intuit.sdp.R.dimen._12sdp);
        CustomItemDecoration customItemDecoration = new CustomItemDecoration(marginInPx);


        madeForYouAdapter = new MadeForYouAdapter();
        mDiscoverViewModel.setDataMadeForYou();
        binding.rvMadeForYou.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvMadeForYou.addItemDecoration(customItemDecoration);
        binding.rvMadeForYou.setAdapter(madeForYouAdapter);

        popularSingerAdapter = new PopularSingerAdapter();
        mDiscoverViewModel.setDataPopularSinger();
        binding.rvPopularSinger.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvPopularSinger.addItemDecoration(customItemDecoration);
        binding.rvPopularSinger.setAdapter(popularSingerAdapter);

        popularSongAdapter = new PopularSongAdapter();
        mDiscoverViewModel.setDataPopularSong();
        binding.rvPopularSong.setLayoutManager(new LinearLayoutManager(this.getContext(),LinearLayoutManager.HORIZONTAL,false));
        binding.rvPopularSong.addItemDecoration(customItemDecoration);
        binding.rvPopularSong.setAdapter(popularSongAdapter);

        recentlySongAdapter = new RecentlySongAdapter();
        mDiscoverViewModel.setDataRecentlySong();
        binding.rvRecently.setLayoutManager(new LinearLayoutManager(this.getContext(),LinearLayoutManager.VERTICAL,false));
//        binding.rvRecently.addItemDecoration(customItemDecoration);
        binding.rvRecently.setAdapter(recentlySongAdapter);
    }
}