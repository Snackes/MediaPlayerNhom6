package com.nhom6.mediaplayer;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;

import com.nhom6.mediaplayer.model.Song;

import java.util.ArrayList;

public class Manager extends Activity {
    public ArrayList<Song> _songs = new ArrayList<Song>();

    //cóntructor
    public Manager() {
    }

    public ArrayList<Song> loadSong(Context context) {
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,    // filepath of the audio file
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ARTIST_ID


                // context id/ uri id of the file
        };


        //Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
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
                    Bitmap bm = getAlbumArt(context,albumid);

                    ///
                    Song s = new Song(title,artist,album,duration,url,songid,artistid,albumid,bm);
                    _songs.add(s);

                } while (cursor.moveToNext());
            }

            cursor.close();
            //Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();

        }
        return _songs;
    }
    public Bitmap getAlbumArt(Context context , int albumID)
    {
        Bitmap bm =null ;


        //Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,null,null,null,null);
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID+ "=?",
                new String[] {String.valueOf(albumID)},
                null);
        if (cursor.moveToFirst()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));

            bm = BitmapFactory.decodeFile(path);

        }
        return bm;

    }
}