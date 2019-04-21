package com.nhom6.mediaplayer.model;

public class Song {
    private String Songname;
    private String Artistname;
    //private int ArtistnameId;



    private String Album;
    //private int AlbumId;
    private int Duration;
    private String SongUrl;
    private int Songid;

    //constructor
    public Song(){}

    public Song(String songname, String artistname, String Album, int Duration, String songUrl, int Songid) {
        this.Songname = songname;
        this.Artistname = artistname;
        this.SongUrl = songUrl;
        this.Album = Album;
        this.Duration = Duration;
        this.Songid = Songid;
       // this.AlbumId = AlbumID;
        //this.ArtistnameId = ArtistID;

    }
//    public int getArtistnameId() {
//        return ArtistnameId;
//    }
//
//    public void setArtistnameId(int artistnameId) {
//        ArtistnameId = artistnameId;
//    }
//
//    public int getAlbumId() {
//        return AlbumId;
//    }

//    public void setAlbumId(int albumId) {
//        AlbumId = albumId;
//    }
    public String getArtistname() {
        return Artistname;
    }

    public void setArtistname(String artistname) {
        Artistname = artistname;
    }

    public String getAlbum() {
        return Album;
    }

    public void setAlbum(String album) {
        Album = album;
    }

    public int getDuration() {
        return Duration;
    }

    public void setDuration(int duration) {
        Duration = duration;
    }

    public String getSongUrl() {
        return SongUrl;
    }

    public void setSongUrl(String songUrl) {
        SongUrl = songUrl;
    }

    public int getSongid() {
        return Songid;
    }

    public void setSongid(int songid) {
        Songid = songid;
    }



    public String getSongname() {
        return Songname;
    }

    public void setSongname(String songname) {
        Songname = songname;
    }
}
