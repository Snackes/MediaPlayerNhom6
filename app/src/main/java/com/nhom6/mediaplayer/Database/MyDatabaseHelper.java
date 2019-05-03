package com.nhom6.mediaplayer.Database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nhom6.mediaplayer.model.PlayList;

import java.util.ArrayList;


public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "SQLite";
    // Phiên bản
    private static final int DATABASE_VERSION = 1;
    // Tên cơ sở dữ liệu.
    private static final String DATABASE_NAME = "PlayList_Manager";
    // Tên bảng: PlayList.
    private static final String TABLE_PLAYLIST = "PlayList";
    private static final String COLUMN_PLAYLIST_ID ="PlayList_Id";
    private static final String COLUMN_PLAYLIST_TITLE ="PlayList_Title";
    private static final String COLUMN_SONG_ID = "Song_Id";
    private static final String CREATE_TABLE_PLAYLIST = "CREATE TABLE IF NOT EXISTS " + TABLE_PLAYLIST +
            " (" + COLUMN_PLAYLIST_ID + " INTEGER PRIMARY KEY ," +
            COLUMN_PLAYLIST_TITLE + " TEXT)";

    // Tên bảng: Song_PlayList.
    private static final String TABLE_SONG_PLAYLIST = "Song_PlayList";
    private static final String CREATE_TABLE_SONG_PLAYLIST = "CREATE TABLE IF NOT EXISTS " + TABLE_SONG_PLAYLIST +
            " (" + COLUMN_PLAYLIST_ID + " INTEGER ," +
            COLUMN_SONG_ID + " INTEGER," +
            " PRIMARY KEY(" + COLUMN_PLAYLIST_ID + "," + COLUMN_SONG_ID + "))";

    public MyDatabaseHelper(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // Tạo các bảng.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PLAYLIST);
        db.execSQL(CREATE_TABLE_SONG_PLAYLIST);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "MyDatabaseHelper.onUpgrade ... ");
        // Hủy (drop) bảng cũ nếu nó đã tồn tại.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLIST);
        // Và tạo lại.
        onCreate(db);
    }

    // Nếu trong bảng PlayList chưa có dữ liệu,
    // Trèn vào mặc định 2 bản ghi.
    public void createDefaultPlayListsIfNeed()  {
        int count = this.getPlayListCount();
        if(count ==0 ) {
            this.addPlayList("Chất");
        }
    }
    //Thêm 1 PlayList vào csdl
    public void addPlayList(String schuoi) {
        //Log.i(TAG, "MyDatabaseHelper.addPlayList ... " + PlayList.getTitle());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        int maxIDPlayList=getMaxIDPlayList();
        values.put(COLUMN_PLAYLIST_ID, maxIDPlayList+1);
        values.put(COLUMN_PLAYLIST_TITLE, schuoi);

        // Trèn một dòng dữ liệu vào bảng.
        db.insert(TABLE_PLAYLIST, null, values);
        // Đóng kết nối database.
        db.close();
    }
    //Lấy danh sách PlayList có trong cơ sở dữ liệu
    public ArrayList<PlayList> getAllPlayLists() {
        Log.i(TAG, "MyDatabaseHelper.getAllPlayLists ... " );

        ArrayList<PlayList> ListPlayList = new ArrayList<PlayList>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_PLAYLIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                PlayList playlist = new PlayList();
                playlist.setIDPlayList(Integer.parseInt(cursor.getString(0)));
                playlist.setTitle(cursor.getString(1));
                // Thêm vào danh sách.
                ListPlayList.add(playlist);
            } while (cursor.moveToNext());
        }
        return ListPlayList;
    }
    //lấy danh sách id bài hát trong một PlayList theo id của PlayList đó trong csdl
    public ArrayList<String> GetListSongInPlayList(int idPlayList) {
        String selectQuery = "select "+COLUMN_SONG_ID+" from "+TABLE_SONG_PLAYLIST+" where PlayList_Id="+idPlayList;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery( selectQuery, null);

        ArrayList<String> listIDsong = new ArrayList<String>();
        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                listIDsong.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return listIDsong;
    }
    //Thêm 1 bài hát vào PlayList vào csdl
    public void addSongForPlayList(PlayList playlist) {
        //Log.i(TAG, "MyDatabaseHelper.addPlayList ... " + PlayList.getTitle());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYLIST_ID, playlist.getIDPlayList());
        values.put(COLUMN_SONG_ID, playlist.getIDsong());

        // Trèn một dòng dữ liệu vào bảng.
        db.insert(TABLE_SONG_PLAYLIST, null, values);
        // Đóng kết nối database.
        db.close();
    }
    //Đếm số lượng của PlayList hiện có
    public int getPlayListCount() {
        Log.i(TAG, "MyDatabaseHelper.getPlayListsCount ... " );

        String countQuery = "SELECT  * FROM " + TABLE_PLAYLIST;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }
    //Lấy id lớn nhất của PlayList trong csdl
    public int getMaxIDPlayList(){
        int mx=-1;
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor=db.rawQuery("SELECT max(PlayList_Id) from PlayList ",null);
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
    public int updatePlayList(PlayList playlist) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYLIST_TITLE, playlist.getTitle());
        values.put(COLUMN_SONG_ID, playlist.getIDsong());

        // updating row
        return db.update(TABLE_PLAYLIST, values, COLUMN_PLAYLIST_ID + " = ?",
                new String[]{String.valueOf(playlist.getIDPlayList())});
    }
    //xóa 1 bài hát khỏi PlayList khỏi csdl
    public void deleteSongInPlayList(int idsong,int idPlayList) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLAYLIST, COLUMN_PLAYLIST_ID + " = ?" + COLUMN_SONG_ID +" = ?",
                new String[] {String.valueOf(idPlayList),String.valueOf(idsong)});
        db.close();
    }
    //xóa 1 PlayList khỏi cơ sở dữ liệu
    public void deletePlayList(PlayList playlist) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SONG_PLAYLIST, COLUMN_PLAYLIST_ID + " = ?",
                new String[] { String.valueOf(playlist.getIDPlayList()) });
        db.delete(TABLE_PLAYLIST, COLUMN_PLAYLIST_ID + " = ?",
                new String[] { String.valueOf(playlist.getIDPlayList()) });
        db.close();
    }
}
