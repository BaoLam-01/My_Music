package com.lampro.mymusic.model;

public class Track_Playlist {
    private int playlistId;
    private int trackId;

    public Track_Playlist(int playlistId, int trackId) {
        this.playlistId = playlistId;
        this.trackId = trackId;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }

    public int getTrackId() {
        return trackId;
    }

    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }

}
