package com.lampro.mymusic.viewmodels.thisdeviceviewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ThisDeviceViewModelFactory implements ViewModelProvider.Factory {
    private Application application;
    public ThisDeviceViewModelFactory(Application application) {
        this.application = application;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ThisDeviceViewModel(application);
    }
}
