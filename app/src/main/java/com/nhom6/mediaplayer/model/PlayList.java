package com.nhom6.mediaplayer.model;
import java.io.Serializable;

public class PlayList implements Serializable {

    public int IDPlayList;
    private String Title;
    private int IDsong;

    public PlayList()  {
    }

    public PlayList(String title, int idsong) {
        this.Title= title;
        this.IDsong= idsong;
    }
    public PlayList(int idPlayList, String title) {
        this.Title= title;
        this.IDPlayList= idPlayList;
    }
    public PlayList(String title) {
        this.Title= title;
    }

    public PlayList(int idPlayList, String title, int idsong) {
        this.IDPlayList= idPlayList;
        this.Title= title;
        this.IDsong= idsong;
    }

    public int getIDPlayList() {
        return IDPlayList;
    }

    public void setIDPlayList(int idPlayList) {
        this.IDPlayList = idPlayList;
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


    @Override
    public String toString()  {
        return this.Title;
    }

}