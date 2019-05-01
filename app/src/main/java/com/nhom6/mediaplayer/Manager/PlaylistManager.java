package com.nhom6.mediaplayer.Manager;

import android.app.Activity;

import com.nhom6.mediaplayer.Database.MyDatabaseHelper;
import com.nhom6.mediaplayer.model.Playlist;

public class PlaylistManager extends Activity {

    // Người dùng đồng ý xóa một playlist.
    public void deletePlaylist (Playlist playlist){
        MyDatabaseHelper db = new MyDatabaseHelper(this);
        db.deletePlaylist(playlist);
        //this.ListPlaylist.remove(playlist);
        // Refresh ListView.
       // this.listViewAdapter.notifyDataSetChanged();
    }
}
