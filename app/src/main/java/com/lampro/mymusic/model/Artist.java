package com.lampro.mymusic.model;

public class Artist {
    private String artistId;
    private String artistName;
    private String genre;

    public Artist(String artistId, String artistName, String genre) {
        this.artistId = artistId;
        this.artistName = artistName;
        this.genre = genre;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
