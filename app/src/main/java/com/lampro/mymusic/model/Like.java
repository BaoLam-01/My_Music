package com.lampro.mymusic.model;

public class Like {
    private int likeId;
    private int userId;
    private int trackId;

    public Like(int likeId, int userId, int trackId) {
        this.likeId = likeId;
        this.userId = userId;
        this.trackId = trackId;
    }

    public int getLikeId() {
        return likeId;
    }

    public void setLikeId(int likeId) {
        this.likeId = likeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTrackId() {
        return trackId;
    }

    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }
}
