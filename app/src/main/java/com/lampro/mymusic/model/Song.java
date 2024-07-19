package com.lampro.mymusic.model;

public class Song {
    private int img;
    private String name, artist;

    public Song(int img, String name, String artist) {
        this.img = img;
        this.name = name;
        this.artist = artist;
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
