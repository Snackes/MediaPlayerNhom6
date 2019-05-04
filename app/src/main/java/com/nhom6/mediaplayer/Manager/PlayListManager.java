package com.nhom6.mediaplayer.Manager;

import android.app.Activity;
import android.content.Context;
import com.nhom6.mediaplayer.Database.MyDatabaseHelper;
import com.nhom6.mediaplayer.model.PlayList;


import java.util.ArrayList;

public class PlayListManager extends Activity {

    public ArrayList<PlayList> _playlists = new ArrayList<PlayList>(); // list tất cả các songs
    //cóntructor
    public PlayListManager() {
    }
    //Load playlist o day
    public ArrayList<PlayList> loadPlayList(Context context) {
/*        String playlistName = "Vu's Song";
        int playlistid = 0001;
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(),R.drawable.adele);
        PlayList pl = new PlayList(playlistName,playlistid,bm);
        _playlists.add(pl);
        return _playlists;*/

        MyDatabaseHelper db =new MyDatabaseHelper(context);
        db.createDefaultPlayListsIfNeed();
        return  _playlists=  db.getAllPlayLists();
    }
    public void CreatePlayList(String title,Context context){
        MyDatabaseHelper db =new MyDatabaseHelper(context);
        db.addPlayList(title);
    }

}