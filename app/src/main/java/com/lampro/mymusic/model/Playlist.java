package com.lampro.mymusic.model;

public class Playlist {
    private int img;
    private String title, Singers;

    public Playlist(int img, String title, String Singers) {
        this.img = img;
        this.title = title;
        this.Singers = Singers;
    }


    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSingers() {
        return Singers;
    }

    public void setSingers(String singers) {
        this.Singers = singers;
    }
}
