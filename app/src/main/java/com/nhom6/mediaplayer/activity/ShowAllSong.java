package com.nhom6.mediaplayer.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.nhom6.mediaplayer.Manager.SongManager;
import com.nhom6.mediaplayer.R;
import com.nhom6.mediaplayer.adapter.ListSongAdapter;
import com.nhom6.mediaplayer.model.Song;

import java.util.ArrayList;

public class ShowAllSong extends AppCompatActivity {

    //khai báo ListView cho adapter
    private ListView listView;

    //khai báo SongManager để loadSong
    SongManager songsManager = new SongManager();
    public ArrayList<Song> _songs = new ArrayList<Song>();
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_song);
        //find id ListView
        listView = (ListView) findViewById(R.id.listViewSong);

        //tiến hành lấy toàn bộ song trong máy
        _songs = songsManager.loadSong(this);
        //đưa vào adapter để hiển thị
        ListSongAdapter listSongAdapter = new ListSongAdapter(this,R.layout.row_item_song,_songs);
        listView.setAdapter(listSongAdapter);


    }
}
