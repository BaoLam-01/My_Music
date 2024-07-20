package com.lampro.mymusic.viewmodels.discoverviewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.lampro.mymusic.R;
import com.lampro.mymusic.model.Playlist;
import com.lampro.mymusic.model.Singer;
import com.lampro.mymusic.model.Song;

import java.util.ArrayList;
import java.util.List;

public class DiscoverViewModel extends AndroidViewModel {


    public MutableLiveData<List<Playlist>> liveDataMadeForYou = new MutableLiveData<>();
    public MutableLiveData<List<Singer>> liveDataPopularSinger = new MutableLiveData<>();
    public MutableLiveData<List<Song>> liveDataPopularSong = new MutableLiveData<>();
    public MutableLiveData<List<Song>> liveDataRecentlySong = new MutableLiveData<>();

    public DiscoverViewModel(@NonNull Application application) {
        super(application);
    }

    public void setDataMadeForYou() {
        liveDataMadeForYou.setValue(getDataMfy());
    }

    public void setDataPopularSinger() {
        liveDataPopularSinger.setValue(getDataPopularSinger());
    }


    public void setDataPopularSong() {
        liveDataPopularSong.setValue(getDataPopularSong());
    }

    public void setDataRecentlySong() {
        liveDataRecentlySong.setValue(getDataRecentlySong());
    }




    private List<Playlist> getDataMfy() {
        List<Playlist> data = new ArrayList<>();
        data.add(new Playlist(R.drawable.mfy_indonesian_pops,"Indonesian pops","Nadine amizah, Ghea Indrawari, Yura Yunita"));
        data.add(new Playlist(R.drawable.mfy_hiphop_mix,"90s hiphop mix","Nadine amizah, Ghea Indrawari, Yura Yunita"));
        data.add(new Playlist(R.drawable.saigon,"Sai Gon mix","Trinh Cong Son, Pham Duy, Ngo Thuy Mien, Le Uyen PHuong"));
        data.add(new Playlist(R.drawable.top_100_rap_viet,"Top 100 ban nhac rap viet hay nhat","BinZ, GDucky, Rymastic, Bray, De Choat"));
        return data;
    }


    private List<Singer> getDataPopularSinger() {
        List<Singer> data = new ArrayList<>();
        data.add(new Singer(R.drawable.nugroho_alis,"Nugroho alis"));
        data.add(new Singer(R.drawable.maria_srinirani,"Maria Srinirani"));
        data.add(new Singer(R.drawable.son_tung,"Son Tung"));
        data.add(new Singer(R.drawable.my_tam,"My Tam"));
        data.add(new Singer(R.drawable.trinh_cong_son,"Trinh Cong Son"));
        data.add(new Singer(R.drawable.thanh_lan,"Thanh Lan"));
        return data;
    }


    private List<Song> getDataPopularSong() {
        List<Song> data = new ArrayList<>();
        data.add(new Song(R.drawable.hidupku_indah,"Hidupku indah", "James adam"));
        data.add(new Song(R.drawable.begitu_adanya,"Begitu adanya", "Nugroho alis"));
        data.add(new Song(R.drawable.ya_begitulah,"Ya begitulah", "Maria Srinirani"));
        data.add(new Song(R.drawable.em_cua_ngay_hom_qua,"Em cua ngay hom qua", "Son Tung MTP"));
        data.add(new Song(R.drawable.thang_tu_la_loi_noi_doi_cua_em,"Thang tu la loi noi doi cua em", "Ha Anh Tuan"));
        data.add(new Song(R.drawable.la_lung,"La lung", "Vu"));
        return data;
    }

    private List<Song> getDataRecentlySong() {
        List<Song> data = new ArrayList<>();
        data.add(new Song(R.drawable.em_cua_ngay_hom_qua,"Em cua ngay hom qua", "Son Tung MTP"));
        data.add(new Song(R.drawable.la_lung,"La lung", "Vu"));
        data.add(new Song(R.drawable.diem_xua,"Diem xua", "Trinh Cong Son"));
        data.add(new Song(R.drawable.dap_vo_cay_dan,"Dap vo cay dan", "Quang Le"));
        data.add(new Song(R.drawable.co_chang_trai_viet_len_cay,"Co chang trai viet len cay", "Phan Manh Quynh"));
        data.add(new Song(R.drawable.nguoi_khac,"Nguoi khac", "Phan Manh Quynh"));
        data.add(new Song(R.drawable.nguoi_khac,"Nguoi khac", "Phan Manh Quynh"));
        return data;
    }

}
