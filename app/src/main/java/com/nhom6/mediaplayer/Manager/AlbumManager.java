package com.nhom6.mediaplayer.Manager;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;

import com.nhom6.mediaplayer.model.Album;
import com.nhom6.mediaplayer.model.Song;

import java.util.ArrayList;

public class AlbumManager extends Activity {
    public ArrayList<Album> _album = new ArrayList<Album>(); // list tất cả các albums

    //constructor
    public AlbumManager(){}



    //TODO: hàm load tất cả album trong máy
    public ArrayList<Album> loadAlbum(Context context)
    {

        String[] projection = {
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums._COUNT
        };

        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
                ,projection
                ,null
                ,null
                ,null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int albumID = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums._ID)));
                    String albumTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
                    String artistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST));
                    int countSong = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums._COUNT)));


                    Album album = new Album(albumID,albumTitle,artistName,countSong);
                    _album.add(album);
                } while (cursor.moveToNext());
            }

            cursor.close();


        }
        return _album;
    }
}
