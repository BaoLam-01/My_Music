package com.lampro.mymusic.model;

public class Album {
    private int albumId;
    private String tittle;
    private int artistId;
    private String genre;
    private String releaseDate;


    public Album(int albumId, String tittle, int artistId, String genre, String releaseDate) {
        this.albumId = albumId;
        this.tittle = tittle;
        this.artistId = artistId;
        this.genre = genre;
        this.releaseDate = releaseDate;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
