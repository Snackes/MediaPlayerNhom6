package com.nhom6.mediaplayer.Manager;

import android.app.Activity;
import android.content.Context;
import com.nhom6.mediaplayer.Database.MyDatabaseHelper;
import com.nhom6.mediaplayer.model.PlayList;
import com.nhom6.mediaplayer.model.Song;


import java.util.ArrayList;

public class PlayListManager extends Activity {

    public ArrayList<PlayList> _playlists = new ArrayList<PlayList>(); // list tất cả các songs
    //cóntructor
    public PlayListManager() {
    }
    //Load playlist o day
    public ArrayList<PlayList> loadPlayList(Context context) {
        MyDatabaseHelper db =new MyDatabaseHelper(context);
        db.createDefaultPlayListsIfNeed();
        return  _playlists=  db.getAllPlayLists();
    }
    public void CreatePlayList(String title,Context context){
        MyDatabaseHelper db =new MyDatabaseHelper(context);
        db.addPlayList(title);
    }
    public void CreatePlayListAndAddSong(String title, Context context, int idsong){
        MyDatabaseHelper db =new MyDatabaseHelper(context);
        db.addPlayListAndAddSong(title,idsong);
    }

}