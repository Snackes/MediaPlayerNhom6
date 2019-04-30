package com.nhom6.mediaplayer.Manager;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;

import com.nhom6.mediaplayer.model.Album;
import com.nhom6.mediaplayer.model.Song;

public class ImageOfAlbum extends Activity {

    //constructor
    public ImageOfAlbum(){}

    //TODO: lấy hình của album với đầu vào là albumID
    public String getAlbumArt(Context context , int albumID)
    {
        String path =null ;


        //Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,null,null,null,null);
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID+ "=?",
                new String[] {String.valueOf(albumID)},
                null);
        if (cursor.moveToFirst()) {
             path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));

            //bm = BitmapFactory.decodeFile(path);

        }
        return path;

    }



}
