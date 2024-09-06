package com.lampro.mymusic.model;

public class Track {
    private int trackId;
    private String tittle;
    private int artistId;
    private int albumId;
    private long duration;
    private String releaseDate;
    private String uriSong;
    private String img;

    public Track() {
    }

    public Track(int trackId, String tittle, int artistId, int albumId, long duration, String releaseDate, String uriSong, String img) {
        this.trackId = trackId;
        this.tittle = tittle;
        this.artistId = artistId;
        this.albumId = albumId;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.uriSong = uriSong;
        this.img = img;
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

    public String getUriSong() {
        return uriSong;
    }

    public void setUriSong(String uriSong) {
        this.uriSong = uriSong;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
