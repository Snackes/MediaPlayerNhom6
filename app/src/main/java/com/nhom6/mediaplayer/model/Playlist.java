package com.nhom6.mediaplayer.model;

import java.io.Serializable;

public class Playlist implements Serializable {

    public int IDPlaylist;
    private String Title;
    private int IDsong;

    public Playlist()  {
    }

    public Playlist(String title, int idsong) {
        this.Title= title;
        this.IDsong= idsong;
    }
    public Playlist(int idPlaylist, String title) {
        this.Title= title;
        this.IDPlaylist= idPlaylist;
    }
    public Playlist(String title) {
        this.Title= title;
    }

    public Playlist(int idplaylist, String title, int idsong) {
        this.IDPlaylist= idplaylist;
        this.Title= title;
        this.IDsong= idsong;
    }

    public int getIDPlaylist() {
        return IDPlaylist;
    }

    public void setIDPlaylist(int idPlaylist) {
        this.IDPlaylist = idPlaylist;
    }
    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }


    public int getIDsong() {
        return IDsong;
    }

    public void setIDsong(int idsong) {
        this.IDsong = idsong;
    }


    @Override
    public String toString()  {
        return this.Title;
    }

}