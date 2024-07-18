package com.lampro.mymusic.viewmodels.discoverviewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lampro.mymusic.R;
import com.lampro.mymusic.adapters.MadeForYouAdapter;
import com.lampro.mymusic.model.MadeForYou;

import java.util.ArrayList;
import java.util.List;

public class DiscoverViewModel extends AndroidViewModel {


    public MutableLiveData<List<MadeForYou>> liveDataMadeForYou = new MutableLiveData<>();

    public DiscoverViewModel(@NonNull Application application) {
        super(application);
    }

    public void getDataMadeForYou() {
        liveDataMadeForYou.setValue(getData());
    }
    private List<MadeForYou> getData() {
        List<MadeForYou> data = new ArrayList<>();
        data.add(new MadeForYou(R.drawable.mfy_1,"Indonesian pops","Nadine amizah, Ghea Indrawari, Yura Yunita"));
        data.add(new MadeForYou(R.drawable.mfy_1,"Indonesian pops","Nadine amizah, Ghea Indrawari, Yura Yunita"));
        data.add(new MadeForYou(R.drawable.mfy_1,"Indonesian pops","Nadine amizah, Ghea Indrawari, Yura Yunita"));
        return data;
    }


}
