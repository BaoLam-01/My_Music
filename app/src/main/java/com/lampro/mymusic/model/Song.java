package com.lampro.mymusic.model;

public class Song {
    private int img;
    private String id,name, artist,album,duration,data;

    public Song(int img, String name, String artist) {
        this.img = img;
        this.name = name;
        this.artist = artist;
    }

    public Song(String id, String name, String artist, String album, String duration, String data) {
        this.img = img;
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.data = data;
    }

    public Song(int img, String id, String name, String artist, String album, String duration, String data) {
        this.img = img;
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.data = data;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
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
}
