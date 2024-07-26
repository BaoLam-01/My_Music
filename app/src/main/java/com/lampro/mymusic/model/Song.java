package com.lampro.mymusic.model;

import android.net.Uri;

public class Song {
    private Uri img;
    private Long id;
    private String name, artist,album,duration,data;
    private Uri uriSong;

    public Song(Uri img, String name, String artist) {
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

    public Song(Uri img, Long id, String name, String artist, String album, String duration, String data) {
        this.img = img;
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.data = data;
    }

    public Song(Uri img, Long id, String name, String artist, String album, String duration, String data, Uri uriSong) {
        this.img = img;
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.data = data;
        this.uriSong = uriSong;
    }


    public Uri getImg() {
        return img;
    }

    public void setImg(Uri img) {
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
}
