package com.lampro.mymusic.viewmodels.playlistviewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class PlaylistViewModelFactory implements ViewModelProvider.Factory {

    private Application application;


    public PlaylistViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new PlaylistViewModel(application);
    }
}
