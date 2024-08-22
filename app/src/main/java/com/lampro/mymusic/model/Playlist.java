package com.lampro.mymusic.model;

public class Playlist {
    private int playlistId;
    private int userId;
    private String tittle;
    private String creationDate;

    private int img, songCount;
    private String title, Singers;

    public Playlist(int img, String title, String Singers) {
        this.img = img;
        this.title = title;
        this.Singers = Singers;
    }

    public Playlist(int img, String title, int songCount) {
        this.img = img;
        this.title = title;
        this.songCount = songCount;
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

    public int getSongCount() {
        return songCount;
    }

    public void setSongCount(int songCount) {
        this.songCount = songCount;
    }
}
