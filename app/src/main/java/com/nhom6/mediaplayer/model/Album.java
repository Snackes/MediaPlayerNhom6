package com.nhom6.mediaplayer.model;

public class Album {
    //
    public int albumID; // id của album
    public String albumTitle; // tên album
    public String artistName;// tên artist
    public int songCount;// số bài hát trong album này
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

    //constructor
    public Album(){}

    //
    public  Album(int albumID, String albumTitle, String artistName,int songCount)
    {
        this.albumID = albumID;
        this.albumTitle = albumTitle;
        this.artistName = artistName;
        this.songCount = songCount;
    }






}
