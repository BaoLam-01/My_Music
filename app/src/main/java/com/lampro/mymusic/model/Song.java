package com.lampro.mymusic.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;


public class Song implements Parcelable {
    private long id;
    private Bitmap img;
    private String name, artist, album, duration, data;
    private String uriSong;

    public Song(Bitmap img, String name, String artist) {
        this.img = img;
        this.name = name;
        this.artist = artist;
    }

    public Song(long id, String name, String artist, String album, String duration, String data) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.data = data;
    }

    public Song(Bitmap img, long id, String name, String artist, String album, String duration, String data) {
        this.img = img;
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.data = data;
    }

    public Song(Bitmap img, long id, String name, String artist, String album, String duration, String data, String uriSong) {
        this.img = img;
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.data = data;
        this.uriSong = uriSong;
    }


    protected Song(Parcel in) {
        id = in.readLong();
        img = in.readParcelable(Bitmap.class.getClassLoader());
        name = in.readString();
        artist = in.readString();
        album = in.readString();
        duration = in.readString();
        data = in.readString();
        uriSong = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeParcelable(img, flags);
        dest.writeString(name);
        dest.writeString(artist);
        dest.writeString(album);
        dest.writeString(duration);
        dest.writeString(data);
        dest.writeString(uriSong);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Uri getUriSong() {

        return Uri.parse(uriSong);
    }

    public void setUriSong(String uriSong) {
        this.uriSong = uriSong;
    }



    public String getTypeDuration() {
        int dura = Integer.parseInt(duration);
        int minute = (dura / 1000) / 60;
        int second = (dura / 1000) % 60;
        return String.format("%02d:%02d", minute, second);
    }

}
