package com.nhom6.mediaplayer.model;
import java.io.Serializable;

public class Album implements Serializable {
    //
    private int albumID; // id của album
    private String albumTitle; // tên album
    private String artistName;// tên artist
    private int songCount;// số bài hát trong album này
    private String albumArt;//
    //


    public int getAlbumID() {
        return albumID;
    }

    public void setAlbumID(int albumID) {
        this.albumID = albumID;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }



    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getSongCount() {
        return songCount;
    }

    public void setSongCount(int songCount) {
        this.songCount = songCount;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    //constructor
    public Album(){}

    //
    public  Album(int albumID, String albumTitle, String artistName,int songCount, String albumArt)
    {
        this.albumID = albumID;
        this.albumTitle = albumTitle;
        this.artistName = artistName;
        this.songCount = songCount;
        this.albumArt = albumArt;
    }






}
