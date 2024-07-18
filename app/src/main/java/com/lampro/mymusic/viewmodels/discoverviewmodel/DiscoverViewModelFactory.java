package com.lampro.mymusic.viewmodels.discoverviewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class DiscoverViewModelFactory implements ViewModelProvider.Factory {
    public Application application;

    public DiscoverViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DiscoverViewModel(application);
    }
}
