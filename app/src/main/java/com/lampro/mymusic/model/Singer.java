package com.lampro.mymusic.model;

public class Singer {
    private int avt;
    private String Name;

    public Singer(int avt, String name) {
        this.avt = avt;
        Name = name;
    }


    public int getAvt() {
        return avt;
    }

    public void setAvt(int avt) {
        this.avt = avt;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
