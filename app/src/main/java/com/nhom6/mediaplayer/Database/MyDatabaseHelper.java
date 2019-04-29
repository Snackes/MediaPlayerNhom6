package com.nhom6.mediaplayer.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nhom6.mediaplayer.model.Playlist;

import java.util.ArrayList;
import java.util.List;


public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";


    // Phiên bản
    private static final int DATABASE_VERSION = 1;


    // Tên cơ sở dữ liệu.
    private static final String DATABASE_NAME = "Playlist_Manager";


    // Tên bảng: Playlist.
    private static final String TABLE_PLAYLIST = "Playlist";

    private static final String COLUMN_PLAYLIST_ID ="Playlist_Id";
    private static final String COLUMN_PLAYLIST_TITLE ="Playlist_Title";
    private static final String COLUMN_SONG_ID = "Song_Id";

    public MyDatabaseHelper(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Tạo các bảng.
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "MyDatabaseHelper.onCreate ... ");
        String script1 ="CREATE TABLE IF NOT EXISTS " + TABLE_PLAYLIST +
                " (" + COLUMN_PLAYLIST_ID + " INTEGER ," +
                COLUMN_PLAYLIST_TITLE + " TEXT," +
                COLUMN_SONG_ID + " INTEGER, PRIMARY KEY(" + COLUMN_PLAYLIST_ID + "," + COLUMN_SONG_ID + "))";
        // Chạy lệnh tạo bảng.
        db.execSQL(script1);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "MyDatabaseHelper.onUpgrade ... ");
        // Hủy (drop) bảng cũ nếu nó đã tồn tại.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLIST);
        // Và tạo lại.
        onCreate(db);
    }

    // Nếu trong bảng Playlist chưa có dữ liệu,
    // Trèn vào mặc định 2 bản ghi.
    public void createDefaultPlaylistsIfNeed()  {
        int count = this.getPlaylistCount();
        if(count ==0 ) {
            Playlist playlist1=  new Playlist(1,"ok",33032);
            Playlist playlist2=  new Playlist(1,"ok",33029);
            Playlist playlist3=  new Playlist(1,"ok",33030);
            Playlist playlist4=  new Playlist(1,"ok",33031);
            Playlist playlist5=  new Playlist(2,"chó",3);
            Playlist playlist6=  new Playlist(4,"chuột",11);
            Playlist playlist7=  new Playlist(5,"rắn",12);
            Playlist playlist8=  new Playlist(5,"chuột",18);
            this.addPlaylist(playlist1);
            this.addPlaylist(playlist2);
            this.addPlaylist(playlist3);
            this.addPlaylist(playlist4);
            this.addPlaylist(playlist5);
            this.addPlaylist(playlist6);
            this.addPlaylist(playlist7);
            this.addPlaylist(playlist8);

        }
    }

    public void addPlaylist(Playlist playlist) {
        //Log.i(TAG, "MyDatabaseHelper.addPlaylist ... " + playlist.getTitle());

        SQLiteDatabase db = this.getWritableDatabase();

        int maxid=getMaxIDPlaylist();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYLIST_ID, maxid+1);
        values.put(COLUMN_PLAYLIST_TITLE, playlist.getTitle());
        values.put(COLUMN_SONG_ID, playlist .getIDsong());

        // Trèn một dòng dữ liệu vào bảng.
        db.insert(TABLE_PLAYLIST, null, values);
        // Đóng kết nối database.
        db.close();
    }


    //Lấy 1 playlist khi trỏ vào theo id của nó trong csdl
    public Playlist getPlaylist(int id) {
        Log.i(TAG, "MyDatabaseHelper.getPlaylist ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PLAYLIST, new String[] { COLUMN_PLAYLIST_ID,
                        COLUMN_PLAYLIST_TITLE, COLUMN_SONG_ID }, COLUMN_PLAYLIST_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Playlist playlist = new Playlist(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), Integer.parseInt(cursor.getString(2)));
        return playlist;
    }


    //Lấy danh sách playlist có trong cơ sở dữ liệu
    public List<Playlist> getAllPlaylists() {
        Log.i(TAG, "MyDatabaseHelper.getAllPlaylists ... " );

        List<Playlist> ListPlaylist = new ArrayList<Playlist>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_PLAYLIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                Playlist playlist = new Playlist();
                playlist.setIDPlaylist(Integer.parseInt(cursor.getString(0)));
                playlist.setTitle(cursor.getString(1));
                playlist.setIDsong(Integer.parseInt(cursor.getString(2)));

                // Thêm vào danh sách.
                ListPlaylist.add(playlist);
            } while (cursor.moveToNext());
        }

        return ListPlaylist;
    }

    //lấy danh sách bài hát trong một playlist theo id của playlist đó trong csdl
    public ArrayList<String> GetListSongInPlaylist(int idplaylist) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery( "select Song_Id from Playlist where Playlist_Id="+idplaylist+"", null);

        ArrayList<String> test = new ArrayList<String>();
        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                test.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return test;
    }
    //Đếm số lượng của playlist hiện có
    public int getPlaylistCount() {
        Log.i(TAG, "MyDatabaseHelper.getPlaylistsCount ... " );

        String countQuery = "SELECT  * FROM " + TABLE_PLAYLIST;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }

    //Lấy id lớn nhất của playlist trong csdl
    public int getMaxIDPlaylist(){
        int mx=-1;
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor=db.rawQuery("SELECT max(Playlist_Id) from Playlist ",null);
            if (cursor != null)
                if(cursor.moveToFirst())
                {
                    mx= cursor.getInt(0);
                }
            //  cursor.close();
            return mx;
        }
        catch(Exception e){

            return -1;
        }
    }
    public int updatePlaylist(Playlist playlist) {
        Log.i(TAG, "MyDatabaseHelper.updatePlaylist ... "  + playlist.getTitle());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYLIST_TITLE, playlist.getTitle());
        values.put(COLUMN_SONG_ID, playlist.getIDsong());

        // updating row
        return db.update(TABLE_PLAYLIST, values, COLUMN_PLAYLIST_ID + " = ?",
                new String[]{String.valueOf(playlist.getIDPlaylist())});
    }

    //xóa 1 playlist khỏi cơ sở dữ liệu
    public void deleteSongInPlaylist(int idsong,int idplaylist) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLAYLIST, COLUMN_PLAYLIST_ID + " = ?" + COLUMN_SONG_ID +" = ?",
                new String[] {String.valueOf(idplaylist),String.valueOf(idsong)});
        db.close();
    }
    //xóa 1 playlist khỏi cơ sở dữ liệu
    public void deletePlaylist(Playlist playlist) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLAYLIST, COLUMN_PLAYLIST_ID + " = ?",
                new String[] { String.valueOf(playlist.getIDPlaylist()) });
        db.close();
    }
}
