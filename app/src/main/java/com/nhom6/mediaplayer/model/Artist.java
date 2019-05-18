package com.nhom6.mediaplayer.model;

import java.io.Serializable;

public class Artist implements Serializable {
    //
    private int artistID; // ID nghệ sĩ
    private String artistName; // tên nghệ sĩ
    private int songsCount;// số bài hát của nghệ sĩ đó
    //



    //constructor
    public Artist(){}

    public Artist( int artistID, String artistName, int songsCount)
    {
        this.artistID = artistID;
        this.artistName = artistName;
        this.songsCount = songsCount;
    }

    public int getArtistID() {
        return artistID;
    }

    public void setArtistID(int artistID) {
        this.artistID = artistID;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getSongsCount() {
        return songsCount;
    }

    public void setSongsCount(int songsCount) {
        this.songsCount = songsCount;
    }
}
