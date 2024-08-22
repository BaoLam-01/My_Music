package com.lampro.mymusic.model;

import java.util.Date;

public class Track {
    private int trackId;
    private String tittle;
    private int artistId;
    private int albumId;
    private long duration;
    private String releaseDate;

    public Track(int trackId, String tittle, int artistId, int albumId, long duration, String releaseDate) {
        this.trackId = trackId;
        this.tittle = tittle;
        this.artistId = artistId;
        this.albumId = albumId;
        this.duration = duration;
        this.releaseDate = releaseDate;
    }

    public int getTrackId() {
        return trackId;
    }

    public void setTrackId(int trackId) {
        this.trackId = trackId;
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

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
