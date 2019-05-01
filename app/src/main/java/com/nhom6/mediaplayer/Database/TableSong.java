package com.nhom6.mediaplayer.Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import com.nhom6.mediaplayer.model.Playlist;
import com.nhom6.mediaplayer.model.Song;

import java.util.ArrayList;
import java.util.List;


public class TableSong extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";


    // Phiên bản
    private static final int DATABASE_VERSION = 1;

    // Tên cơ sở dữ liệu.
    private static final String DATABASE_NAME = "Playlist_Manager";
    // Tên bảng: Playlist.
    private static final String TABLE_SONG = "Song";

    private static final String COLUMN_SONG_ID ="SongId";
    private static final String COLUMN_SONG_NAME ="SongName";
    private static final String COLUMN_ARTIST_NAME = "Artistname";
    private static final String COLUMN_ARTISTNAME_ID ="ArtistnameId";
    private static final String COLUMN_ALBUM ="Album";
    private static final String COLUMN_ALBUM_ID = "AlbumId";
    private static final String COLUMN_DURATION ="Duration";
    private static final String COLUMN_SONGURL ="SongUrl";
    private static final String COLUMN_ALBUMART = "AlbumArt";

    public TableSong(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Tạo các bảng.
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "MyDatabaseHelper.onCreate ... ");
        String script1 ="CREATE TABLE IF NOT EXISTS " + TABLE_SONG +
                " (" + COLUMN_SONG_ID + " INTEGER  PRIMARY KEY," +
                COLUMN_SONG_NAME + " TEXT," +
                COLUMN_ARTIST_NAME + " TEXT," +
                COLUMN_ARTISTNAME_ID + " INTEGER," +
                COLUMN_ALBUM + " TEXT," +
                COLUMN_ALBUM_ID + " INTEGER," +
                COLUMN_DURATION + " TEXT," +
                COLUMN_SONGURL + " TEXT," +
                COLUMN_ALBUMART + " TEXT)";
        // Chạy lệnh tạo bảng.
        db.execSQL(script1);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "MyDatabaseHelper.onUpgrade ... ");
        // Hủy (drop) bảng cũ nếu nó đã tồn tại.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONG);
        // Và tạo lại.
        onCreate(db);
    }

    //Lấy tất cả các bài hát có trong có trong cơ sở dữ liệu
    public List<Song> getAllSong() {
        Log.i(TAG, "MyDatabaseHelper.getAllPlaylists ... " );

        List<Song> ListSong = new ArrayList<Song>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_SONG;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                Song song = new Song();
                song.setSongid(Integer.parseInt(cursor.getString(0)));
                song.setSongname(cursor.getString(1));
                song.setArtistname(cursor.getString(2));
                song.setArtistnameId(Integer.parseInt(cursor.getString(3)));
                song.setAlbum(cursor.getString(4));
                song.setAlbumId(Integer.parseInt(cursor.getString(5)));
                song.setDuration(Integer.parseInt(cursor.getString(6)));
                song.setSongUrl(cursor.getString(7));
                song.setAlbumArt(cursor.getString(8));
                // Thêm vào danh sách.
                ListSong.add(song);
            } while (cursor.moveToNext());
        }

        return ListSong;
    }
}
