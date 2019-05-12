package com.nhom6.mediaplayer.Database;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nhom6.mediaplayer.model.Album;
import com.nhom6.mediaplayer.model.Artist;
import com.nhom6.mediaplayer.model.PlayList;
import com.nhom6.mediaplayer.model.Song;

import java.sql.PreparedStatement;
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


    // Tên bảng: Song
    private static final String TABLE_SONG = "Song";
    private static final String COLUMN_SONG_NAME ="SongName";
    private static final String COLUMN_ARTIST_NAME = "Artistname";
    private static final String COLUMN_ARTISTNAME_ID ="ArtistnameId";
    private static final String COLUMN_ALBUM ="Album";
    private static final String COLUMN_ALBUM_ID = "AlbumId";
    private static final String COLUMN_DURATION ="Duration";
    private static final String COLUMN_SONGURL ="SongUrl";
    private static final String COLUMN_ALBUMART = "AlbumArt";
    private static final String COLUMN_FAVORITE = "Favorite";
    private static final String CREATE_TABLE_SONG ="CREATE TABLE IF NOT EXISTS " + TABLE_SONG +
            " (" + COLUMN_SONG_ID + " INTEGER  PRIMARY KEY," +
            COLUMN_SONG_NAME + " TEXT," +
            COLUMN_ARTIST_NAME + " TEXT," +
            COLUMN_ARTISTNAME_ID + " INTEGER," +
            COLUMN_ALBUM + " TEXT," +
            COLUMN_ALBUM_ID + " INTEGER," +
            COLUMN_DURATION + " TEXT," +
            COLUMN_SONGURL + " TEXT," +
            COLUMN_ALBUMART + " TEXT," +
            COLUMN_FAVORITE + " INTEGER)";


    // Tên bảng: Album.
    private static final String TABLE_ALBUM = "Album";
    private static final String COLUMN_SONG_COUNT ="SongCount";
    private static final String CREATE_TABLE_ALBUM = "CREATE TABLE IF NOT EXISTS " + TABLE_ALBUM +
            " (" + COLUMN_ALBUM_ID + " INTEGER PRIMARY KEY ," +
            COLUMN_ALBUM + " TEXT," +
            COLUMN_ARTIST_NAME + " TEXT," +
            COLUMN_SONG_COUNT + " INTEGER," +
            COLUMN_ALBUMART + " TEXT)";


    // Tên bảng: Singer.
    private static final String TABLE_SINGER = "Singer";
    private static final String CREATE_TABLE_SINGER = "CREATE TABLE IF NOT EXISTS " + TABLE_SINGER +
            " (" + COLUMN_ARTISTNAME_ID + " INTEGER PRIMARY KEY ," +
            COLUMN_ARTIST_NAME + " TEXT," +
            COLUMN_SONG_COUNT + " INTEGER)";


    public MyDatabaseHelper(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Tạo các bảng.

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SONG);
        db.execSQL(CREATE_TABLE_PLAYLIST);
        db.execSQL(CREATE_TABLE_ALBUM);
        db.execSQL(CREATE_TABLE_SONG_PLAYLIST);
        db.execSQL(CREATE_TABLE_SINGER);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Hủy (drop) bảng cũ nếu nó đã tồn tại.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALBUM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SINGER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONG_PLAYLIST);
        // Và tạo lại.
        onCreate(db);
    }

    // Nếu trong bảng PlayList chưa có dữ liệu,
    public void createDefaultPlayListsIfNeed()  {
        int count = this.getPlayListCount();
        if(count ==0 ) {
            this.addPlayList("Chất");
        }
    }

    //Thêm 1 PlayList cùng bài hát muốn thêm vào playlist đó vào csdl
    public void addPlayListAndAddSong(String NamePlaylist, int idsong) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        int maxIDPlayList=getMaxIDPlayList();
        values.put(COLUMN_PLAYLIST_ID, maxIDPlayList+1);
        values.put(COLUMN_PLAYLIST_TITLE, NamePlaylist);
        // Trèn một dòng dữ liệu vào bảng.
        db.insert(TABLE_PLAYLIST, null, values);

        ContentValues values1 = new ContentValues();
        values1.put(COLUMN_PLAYLIST_ID, maxIDPlayList+1);
        values1.put(COLUMN_SONG_ID, idsong);
        // Trèn một dòng dữ liệu vào bảng.
        db.insert(TABLE_SONG_PLAYLIST, null, values1);
        // Đóng kết nối database.
        db.close();
    }

    //Thêm 1 PlayList vào csdl
    public void addPlayList(String schuoi) {
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

    //lấy danh sách bài hát trong một PlayList theo id của PlayList đó trong csdl
    public ArrayList<Song> GetListSongInPlayList(int idPlayList) {
//        String selectQuery = "select * from "+TABLE_SONG_PLAYLIST+" where PlayList_Id="+idPlayList;
        String selectQuery = "SELECT  * FROM " + TABLE_SONG + " ts, "
                + TABLE_SONG_PLAYLIST + " sp WHERE sp."
                + COLUMN_PLAYLIST_ID + " = '" + idPlayList
                + "'" + " AND ts." + COLUMN_SONG_ID + " = "
                + "sp." + COLUMN_SONG_ID;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery( selectQuery, null);
        ArrayList<Song> listSong = new ArrayList<Song>();
        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                Song song=new Song();
                song.setSongid(Integer.parseInt(cursor.getString(0)));
                song.setSongname(cursor.getString(1));
                song.setArtistname(cursor.getString(2));
                song.setArtistnameId(Integer.parseInt(cursor.getString(3)));
                song.setAlbum(cursor.getString(4));
                song.setAlbumId(Integer.parseInt(cursor.getString(5)));
                song.setDuration(Integer.parseInt(cursor.getString(6)));
                song.setSongUrl(cursor.getString(7));
                song.setAlbumArt(cursor.getString(8));
                song.setFavorite(Integer.parseInt(cursor.getString(9)));
                listSong.add(song);
            } while (cursor.moveToNext());
        }
        return listSong;
    }

    //lấy danh sách bài hát trong một Album theo id của Album đó trong csdl
    public ArrayList<Song> GetListSongInAlbum(int idAlbum) {
        //        String selectQuery = "select * from "+TABLE_SONG_PLAYLIST+" where PlayList_Id="+idPlayList;
        String selectQuery = "SELECT  * FROM " + TABLE_SONG+
                " WHERE "
                + COLUMN_ALBUM_ID + " = " + idAlbum;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery( selectQuery, null);
        ArrayList<Song> listSong = new ArrayList<Song>();
        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                Song song=new Song();
                song.setSongid(Integer.parseInt(cursor.getString(0)));
                song.setSongname(cursor.getString(1));
                song.setArtistname(cursor.getString(2));
                song.setArtistnameId(Integer.parseInt(cursor.getString(3)));
                song.setAlbum(cursor.getString(4));
                song.setAlbumId(Integer.parseInt(cursor.getString(5)));
                song.setDuration(Integer.parseInt(cursor.getString(6)));
                song.setSongUrl(cursor.getString(7));
                song.setAlbumArt(cursor.getString(8));
                song.setFavorite(Integer.parseInt(cursor.getString(9)));
                listSong.add(song);
            } while (cursor.moveToNext());
        }
        return listSong;
    }

    //lấy danh sách bài hát trong một ca sĩ theo id của ca sĩ đó trong csdl
    public ArrayList<Song> GetListSongOfArtist(int idArtist) {
        //        String selectQuery = "select * from "+TABLE_SONG_PLAYLIST+" where PlayList_Id="+idPlayList;
        String selectQuery = "SELECT  * FROM " + TABLE_SONG+
                " WHERE "
                + COLUMN_ARTISTNAME_ID + " = " + idArtist;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery( selectQuery, null);
        ArrayList<Song> listSong = new ArrayList<Song>();
        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                Song song=new Song();
                song.setSongid(Integer.parseInt(cursor.getString(0)));
                song.setSongname(cursor.getString(1));
                song.setArtistname(cursor.getString(2));
                song.setArtistnameId(Integer.parseInt(cursor.getString(3)));
                song.setAlbum(cursor.getString(4));
                song.setAlbumId(Integer.parseInt(cursor.getString(5)));
                song.setDuration(Integer.parseInt(cursor.getString(6)));
                song.setSongUrl(cursor.getString(7));
                song.setAlbumArt(cursor.getString(8));
                song.setFavorite(Integer.parseInt(cursor.getString(9)));
                listSong.add(song);
            } while (cursor.moveToNext());
        }
        return listSong;
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

    //Lấy tất cả bài hát có trong csdl
    public ArrayList<Song> GetListSong() {
        String selectQuery = "select distinct * from "+TABLE_SONG;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery( selectQuery, null);

        ArrayList<Song> ListSong = new ArrayList<Song>();
        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                Song song=new Song();
                song.setSongid(Integer.parseInt(cursor.getString(0)));
                song.setSongname(cursor.getString(1));
                song.setArtistname(cursor.getString(2));
                song.setArtistnameId(Integer.parseInt(cursor.getString(3)));
                song.setAlbum(cursor.getString(4));
                song.setAlbumId(Integer.parseInt(cursor.getString(5)));
                song.setDuration(Integer.parseInt(cursor.getString(6)));
                song.setSongUrl(cursor.getString(7));
                song.setAlbumArt(cursor.getString(8));
                song.setFavorite(Integer.parseInt(cursor.getString(9)));
                ListSong.add(song);
            } while (cursor.moveToNext());
        }
        return ListSong;
    }

    //Lấy tất cả album có trong csdl
    public ArrayList<Album> GetListAlbums() {
        String selectQuery = "select * from "+TABLE_ALBUM;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery( selectQuery, null);

        ArrayList<Album> ListAlbum = new ArrayList<Album>();
        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                Album album=new Album();
                album.setAlbumID(Integer.parseInt(cursor.getString(0)));
                album.setAlbumTitle(cursor.getString(1));
                album.setArtistName(cursor.getString(2));
                album.setSongCount(Integer.parseInt(cursor.getString(3)));
                album.setAlbumArt(cursor.getString(4));
                ListAlbum.add(album);
            } while (cursor.moveToNext());
        }
        return ListAlbum;
    }

    //Lấy ra danh sách bài hát yêu thích(Tức có thuộc tính Favorite=1)
    public ArrayList<Song> GetListSongFavorite(){
        String selectQuery = "select * from "+TABLE_SONG+" WHERE "+COLUMN_FAVORITE+"=1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery( selectQuery, null);

        ArrayList<Song> ListSong = new ArrayList<Song>();
        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                Song song=new Song();
                song.setSongid(Integer.parseInt(cursor.getString(0)));
                song.setSongname(cursor.getString(1));
                song.setArtistname(cursor.getString(2));
                song.setArtistnameId(Integer.parseInt(cursor.getString(3)));
                song.setAlbum(cursor.getString(4));
                song.setAlbumId(Integer.parseInt(cursor.getString(5)));
                song.setDuration(Integer.parseInt(cursor.getString(6)));
                song.setSongUrl(cursor.getString(7));
                song.setAlbumArt(cursor.getString(8));
                song.setFavorite(Integer.parseInt(cursor.getString(9)));
                ListSong.add(song);
            } while (cursor.moveToNext());
        }
        return ListSong;
    }
    //Lấy thong tin bai hat theo id
    public Song GetInfoSong(int idsong){
        String selectQuery = "select * from "+TABLE_SONG+" WHERE "+COLUMN_SONG_ID+"="+idsong+"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery( selectQuery, null);
        // Duyệt trên con trỏ, và thêm vào danh sách.
        Song song=new Song();
        if (cursor.moveToFirst()) {
            do {
                song.setSongid(Integer.parseInt(cursor.getString(0)));
                song.setSongname(cursor.getString(1));
                song.setArtistname(cursor.getString(2));
                song.setArtistnameId(Integer.parseInt(cursor.getString(3)));
                song.setAlbum(cursor.getString(4));
                song.setAlbumId(Integer.parseInt(cursor.getString(5)));
                song.setDuration(Integer.parseInt(cursor.getString(6)));
                song.setSongUrl(cursor.getString(7));
                song.setAlbumArt(cursor.getString(8));
                song.setFavorite(Integer.parseInt(cursor.getString(9)));
            } while (cursor.moveToNext());
        }
        return song;
    }

    //Lấy tất cả ca sĩ có trong csdl
    public ArrayList<Artist> GetListSinger() {
        String selectQuery = "select * from "+TABLE_SINGER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery( selectQuery, null);

        ArrayList<Artist> ListArtist = new ArrayList<Artist>();
        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                Artist artist=new Artist();
                artist.setArtistID(Integer.parseInt(cursor.getString(0)));
                artist.setArtistName(cursor.getString(1));
                artist.setSongsCount(Integer.parseInt(cursor.getString(2)));
                ListArtist.add(artist);
            } while (cursor.moveToNext());
        }
        return ListArtist;
    }

    //Thêm 1 bài hát vào PlayList vào csdl

    public void addSongForPlayList(int idplaylist, int idsong) {
        //Log.i(TAG, "MyDatabaseHelper.addPlayList ... " + PlayList.getTitle());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYLIST_ID, idplaylist);
        values.put(COLUMN_SONG_ID, idsong);

        // Trèn một dòng dữ liệu vào bảng.
        db.insert(TABLE_SONG_PLAYLIST, null, values);
        // Đóng kết nối database.
        db.close();
    }

    //Đếm số lượng của PlayList hiện có
    public int getPlayListCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PLAYLIST;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
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

    //xóa 1 bài hát khỏi PlayList
    public void deleteSongInPlayList(int idsong,int idPlayList) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SONG_PLAYLIST, COLUMN_PLAYLIST_ID + " = ? AND "+ COLUMN_SONG_ID +" = ?",
                new String[] {String.valueOf(idPlayList),String.valueOf(idsong)});
        db.close();
    }

    //xóa 1 PlayList khỏi cơ sở dữ liệu
    public void deletePlayList(int idplaylist) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SONG_PLAYLIST, COLUMN_PLAYLIST_ID + " = ?",
                new String[] { String.valueOf(idplaylist) });
        db.delete(TABLE_PLAYLIST, COLUMN_PLAYLIST_ID + " = ?",
                new String[] { String.valueOf(idplaylist) });
        db.close();
    }

    //xóa 1 bài hát khỏi DANH SÁCH YÊU THÍCH
    public void deleteSongInFavorite(int idsong) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "UPDATE "+TABLE_SONG+" SET "+COLUMN_FAVORITE+"=0 WHERE "+COLUMN_SONG_ID+"="+idsong+"";
        db.execSQL(selectQuery);
    }

    //Thêm tất cả bài hát đã load được từ máy vào csdl
    public void addSong(ArrayList<Song>_song) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for(int i=0;i<_song.size();i++){
            values.put(COLUMN_SONG_ID, _song.get(i).getSongid());
            values.put(COLUMN_SONG_NAME, _song.get(i).getSongname());
            values.put(COLUMN_ARTIST_NAME, _song.get(i).getArtistname());
            values.put(COLUMN_ARTISTNAME_ID, _song.get(i).getArtistnameId());
            values.put(COLUMN_ALBUM, _song.get(i).getAlbum());
            values.put(COLUMN_ALBUM_ID, _song.get(i).getAlbumId());
            values.put(COLUMN_DURATION, _song.get(i).getDuration());
            values.put(COLUMN_SONGURL, _song.get(i).getSongUrl());
            values.put(COLUMN_ALBUMART, _song.get(i).getAlbumArt());
            values.put(COLUMN_FAVORITE, _song.get(i).getFavorite());
            db.insert(TABLE_SONG, null, values);
        }
        // Đóng kết nối database.
        db.close();
    }

    //Thêm tất cả ca sĩ đã load được từ máy vào csdl
    public void addSinger(ArrayList<Artist>_Aritst) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for(int i=0;i<_Aritst.size();i++){
            values.put(COLUMN_ARTISTNAME_ID, _Aritst.get(i).getArtistID());
            values.put(COLUMN_ARTIST_NAME, _Aritst.get(i).getArtistName());
            values.put(COLUMN_SONG_COUNT, _Aritst.get(i).getSongsCount());
            db.insert(TABLE_SINGER, null, values);
        }
        // Đóng kết nối database.
        db.close();
    }

    //Thêm tất cả bài hát đã load được từ máy vào csdl
    public void addAlbum(ArrayList<Album>_album) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        for(int i=0;i<_album.size();i++){
            values.put(COLUMN_ALBUM_ID, _album.get(i).getAlbumID());
            values.put(COLUMN_ALBUM, _album.get(i).getAlbumTitle());
            values.put(COLUMN_ARTIST_NAME, _album.get(i).getArtistName());
            values.put(COLUMN_SONG_COUNT, _album.get(i).getSongCount());
            values.put(COLUMN_ALBUMART, _album.get(i).getAlbumArt());
            db.insert(TABLE_ALBUM, null, values);
        }
        // Đóng kết nối database.
        db.close();
    }

    //Kiểm tra xem trong bảng Song có dữ liệu chưa
    public int CheckTableSong(){
        String countQuery = "SELECT  * FROM " + TABLE_SONG;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }

    //Kiểm tra xem trong bảng Singer có dữ liệu chưa
    public int CheckTableSinger(){
        String countQuery = "SELECT  * FROM " + TABLE_SINGER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }

    //Kiểm tra xem trong bảng Song có dữ liệu chưa
    public int CheckTableAlbum(){
        String countQuery = "SELECT  * FROM " + TABLE_ALBUM;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }

    //Lấy ra danh sách bài hát yêu thích(Tức có thuộc tính Favorite=1)
    public int CheckSongFavorite(int idsong){
        String selectQuery = "select * from "+TABLE_SONG+" WHERE "+COLUMN_FAVORITE+"=1"+
                " AND "+ COLUMN_SONG_ID +"="+idsong+"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }

    //Thêm bài hát vào yêu thích (Tức bật thuộc tính Favorite = 1)
    public void AddSongFavorite (int idsong){
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "UPDATE "+TABLE_SONG+" SET "+COLUMN_FAVORITE+"=1 WHERE "+COLUMN_SONG_ID+"="+idsong+"";
        db.execSQL(selectQuery);
    }

    //xóa 1 PlayList khỏi cơ sở dữ liệu
    public int UpdateNamePlaylist(int idplaylist,String NewName) {
/*        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "UPDATE "+TABLE_PLAYLIST+" SET "+COLUMN_PLAYLIST_TITLE+"="+NewName+" WHERE "+COLUMN_PLAYLIST_ID+"="+idplaylist+"";
        db.execSQL(selectQuery);*/

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYLIST_TITLE, NewName);
        // updating row
        return db.update(TABLE_PLAYLIST, values, COLUMN_PLAYLIST_ID + " = ?",
                new String[] { String.valueOf(idplaylist) });
    }


    //Tìm kiếm danh sách bài hát theo chuỗi nhập vào
    public ArrayList<Song> SearchSong(String Chuoi,int idObject, int test){
        String selectQuery="";
        if(test==0){
            selectQuery = "SELECT  * FROM " + TABLE_SONG
                    + " WHERE "
                    + COLUMN_SONG_NAME +
                    " LIKE  '"+Chuoi+"%'";
        }
        if(test==1){
            selectQuery = "SELECT  * FROM " + TABLE_SONG + " ts, "
                    + TABLE_SONG_PLAYLIST + " sp WHERE sp."
                    + COLUMN_PLAYLIST_ID + " = '" + idObject
                    + "'" + " AND "
                    + COLUMN_SONG_NAME + " LIKE  '"+Chuoi+"%'"+"AND"+
                    " ts." + COLUMN_SONG_ID + " = "+ "sp." + COLUMN_SONG_ID;
        }
        if(test==2){
            selectQuery = "SELECT  * FROM " + TABLE_SONG
                    + " WHERE "
                    + COLUMN_SONG_NAME +
                    " LIKE  '"+Chuoi+"%'"+"AND "+COLUMN_ALBUM_ID +"="+idObject;
        }
        if(test==3){
            selectQuery = "SELECT  * FROM " + TABLE_SONG
                    + " WHERE "
                    + COLUMN_SONG_NAME +
                    " LIKE  '"+Chuoi+"%'"+"AND "+COLUMN_ARTISTNAME_ID +"="+idObject;
        }
        if(test==4){
            selectQuery = "SELECT  * FROM " + TABLE_SONG
                    + " WHERE "
                    + COLUMN_SONG_NAME +
                    " LIKE  '"+Chuoi+"%'"+"AND "+COLUMN_FAVORITE +"="+idObject;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery( selectQuery, null);
        ArrayList<Song> listSong = new ArrayList<Song>();
        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                Song song=new Song();
                song.setSongid(Integer.parseInt(cursor.getString(0)));
                song.setSongname(cursor.getString(1));
                song.setArtistname(cursor.getString(2));
                song.setArtistnameId(Integer.parseInt(cursor.getString(3)));
                song.setAlbum(cursor.getString(4));
                song.setAlbumId(Integer.parseInt(cursor.getString(5)));
                song.setDuration(Integer.parseInt(cursor.getString(6)));
                song.setSongUrl(cursor.getString(7));
                song.setAlbumArt(cursor.getString(8));
                song.setFavorite(Integer.parseInt(cursor.getString(9)));
                listSong.add(song);
            } while (cursor.moveToNext());
        }
        return listSong;
    }

    //Tìm kiếm danh sách Playlist theo chuỗi nhập vào
    public ArrayList<PlayList> SearchPlayList(String Chuoi){
        String selectQuery = "SELECT  * FROM " + TABLE_PLAYLIST
                + " WHERE "
                + COLUMN_PLAYLIST_TITLE +
                " LIKE  '"+Chuoi+"%'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery( selectQuery, null);
        ArrayList<PlayList> ListPlayList = new ArrayList<PlayList>();
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

    //Tìm kiếm danh sách Album theo chuỗi nhập vào
    public ArrayList<Album> SearchAlbum(String Chuoi){
        String selectQuery = "SELECT  * FROM " + TABLE_ALBUM
                + " WHERE "
                + COLUMN_ALBUM +
                " LIKE  '"+Chuoi+"%'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery( selectQuery, null);
        ArrayList<Album> ListAlbum = new ArrayList<Album>();
        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                Album album=new Album();
                album.setAlbumID(Integer.parseInt(cursor.getString(0)));
                album.setAlbumTitle(cursor.getString(1));
                album.setArtistName(cursor.getString(2));
                album.setSongCount(Integer.parseInt(cursor.getString(3)));
                album.setAlbumArt(cursor.getString(4));
                ListAlbum.add(album);
            } while (cursor.moveToNext());
        }
        return ListAlbum;
    }

    //Tìm kiếm danh sách Artist theo chuỗi nhập vào
    public ArrayList<Artist> SearchSinger(String Chuoi){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " +
                TABLE_SINGER + " where " + COLUMN_ARTIST_NAME + " like ?", new String[] { "%" + Chuoi + "%" });
        ArrayList<Artist> ListArtist = new ArrayList<Artist>();
        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                Artist artist=new Artist();
                artist.setArtistID(Integer.parseInt(cursor.getString(0)));
                artist.setArtistName(cursor.getString(1));
                artist.setSongsCount(Integer.parseInt(cursor.getString(2)));
                ListArtist.add(artist);
            } while (cursor.moveToNext());
        }
        return ListArtist;
    }

}
