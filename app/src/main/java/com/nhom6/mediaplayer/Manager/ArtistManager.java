package com.nhom6.mediaplayer.Manager;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.nhom6.mediaplayer.model.Album;
import com.nhom6.mediaplayer.model.Artist;

import java.util.ArrayList;

public class ArtistManager {

    public ArrayList<Artist> _artist = new ArrayList<Artist>(); // list chứa tất cả các artist

    //constructor
    public ArtistManager(){}

    //

    //TODO: hàm load tất cả artist trong máy

    public ArrayList<Artist> loadArtist(Context context)
    {
        Uri uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;

//        String[] projection = {
//                MediaStore.Audio.Albums.ALBUM,
//                MediaStore.Audio.Albums._ID,
//                MediaStore.Audio.Albums.ARTIST,
//                MediaStore.Audio.Albums._COUNT
//        };
        //String sortOrder = MediaStore.Audio.Media.ALBUM + " ASC";

        Cursor cursor = context.getContentResolver().query(uri,null,null,null,null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int artistID = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists._ID)));
                    String artistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
                    int countSong =Integer.parseInt(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS)));

                    Artist artist = new Artist(artistID,artistName,countSong);
                    _artist.add(artist);
                } while (cursor.moveToNext());
            }

            cursor.close();


        }
        return _artist;
    }


}
