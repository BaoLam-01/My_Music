package com.lampro.mymusic.views.fragments;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lampro.mymusic.R;
import com.lampro.mymusic.adapters.MadeForYouAdapter;
import com.lampro.mymusic.base.BaseFragment;
import com.lampro.mymusic.databinding.FragmentDiscoverBinding;
import com.lampro.mymusic.model.MadeForYou;
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


        mDiscoverViewModel.liveDataMadeForYou.observe(getViewLifecycleOwner(), new Observer<List<MadeForYou>>() {

            @Override
            public void onChanged(List<MadeForYou> madeForYous) {
                madeForYouAdapter.updateData(madeForYous);
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
        mDiscoverViewModel.getDataMadeForYou();
        madeForYouAdapter = new MadeForYouAdapter();
        binding.rvMadeForYou.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvMadeForYou.setAdapter(madeForYouAdapter);

    }
}