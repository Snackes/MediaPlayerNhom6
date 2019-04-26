package com.nhom6.mediaplayer.Manager;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;

import com.nhom6.mediaplayer.R;
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
        String playlistName = "Vu's Song";
        int playlistid = 0001;
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(),R.drawable.adele);
        PlayList pl = new PlayList(playlistName,playlistid,bm);
        _playlists.add(pl);
        return _playlists;
    }

}