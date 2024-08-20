package com.lampro.mymusic.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Song implements Parcelable {
    private Bitmap img;
    private Long id;
    private String name, artist,album,duration,data;
    private Uri uriSong;

    public Song(Bitmap img, String name, String artist) {
        this.img = img;
        this.name = name;
        this.artist = artist;
    }

    public Song(Long id, String name, String artist, String album, String duration, String data) {
        this.img = img;
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.data = data;
    }

    public Song(Bitmap img, Long id, String name, String artist, String album, String duration, String data) {
        this.img = img;
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.data = data;
    }

    public Song(Bitmap img, Long id, String name, String artist, String album, String duration, String data, Uri uriSong) {
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
        img = in.readParcelable(Uri.class.getClassLoader());
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        name = in.readString();
        artist = in.readString();
        album = in.readString();
        duration = in.readString();
        data = in.readString();
        uriSong = in.readParcelable(Uri.class.getClassLoader());
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        int dura = Integer.parseInt(duration);
        int minute = dura / 60;
        int second = dura % 60;
        String duration = String.format("%02d:%02d", minute, second);
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
        return uriSong;
    }

    public void setUriSong(Uri uriSong) {
        this.uriSong = uriSong;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeParcelable(img, flags);
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(name);
        dest.writeString(artist);
        dest.writeString(album);
        dest.writeString(duration);
        dest.writeString(data);
        dest.writeParcelable(uriSong, flags);
    }
}
