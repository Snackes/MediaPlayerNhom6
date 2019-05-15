package com.nhom6.mediaplayer.Manager;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.nhom6.mediaplayer.model.Song;

import java.util.ArrayList;

public class SongManager extends Activity {

    Context context=this;
    public ArrayList<Song> _songs = new ArrayList<Song>(); // list tất cả các songs


    //cóntructor
    public SongManager() {
    }


    //TODO: hàm load tất cả bài hát có trong máy , trả về một ArrayList<Song>
    public ArrayList<Song> loadSong(Context context) {

        //điều kiện chọn
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        // cái này kiểu như lưới hứng
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION, // thời gian của bài hát *miliseconds
                MediaStore.Audio.Media.DATA,    // filepath of the audio file
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ARTIST_ID
        };

        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                MediaStore.Audio.Media.TITLE);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    int duration = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                    String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    int songid = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                    int albumid = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
                    int artistid = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID)));
                    //
                    //get AlbumArt
                    ImageOfAlbum imageAlbum = new ImageOfAlbum();
                    String img = imageAlbum.getAlbumArt(context,albumid);


                    ///
                    Song s = new Song(title,artist,album,duration,url,songid,artistid,albumid,img);
                    _songs.add(s);

                } while (cursor.moveToNext());
            }

            cursor.close();


        }
        return _songs;
    }

    //TODO: hàm load Songs theo id
    public Song loadSongWithID(Context context,int songID)
    {
       // tạo song với mục đích trả về
       Song _newSong =  null;

        //
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                MediaStore.Audio.Media._ID + "=?",
                new String[] {String.valueOf(songID)},
                null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {

                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                int duration = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                int songid = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                int albumid = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
                int artistid = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID)));
                //
                //get AlbumArt
                ImageOfAlbum imageAlbum = new ImageOfAlbum();
                String img = imageAlbum.getAlbumArt(context,albumid);

                //khởi tạo với những giá trị đã lấy đc
                _newSong = new Song(title,artist,album,duration,url,songid,artistid,albumid,img);

            }
            cursor.close();

        }

        return _newSong;
    }







}