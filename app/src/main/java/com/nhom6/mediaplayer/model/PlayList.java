package com.nhom6.mediaplayer.model;
import android.graphics.Bitmap;

public class PlayList {
    private String Playlistname;

    private int Playlistid;
    private Bitmap PlayListArt;

    //constructor
    public PlayList(){}

    public PlayList(String Playlistname, int Playlistid, Bitmap PlayListArt) {
        this.Playlistname=Playlistname;
        this.Playlistid=Playlistid;
        this.PlayListArt=PlayListArt;

    }

    public Bitmap getPlayListArt() {
        return PlayListArt;
    }

    public void setPlayLisArt(Bitmap PlayListArt) {
        this.PlayListArt = PlayListArt;
    }



    public int getPlaylistid() {
        return Playlistid;
    }

    public void setPlaylistid(int Playlistid) {
        this.Playlistid = Playlistid;
    }



    public String getPlaylistname() {
        return Playlistname;
    }

    public void setPlaylistname(String Playlistname) {
        this.Playlistname = Playlistname;
    }
}