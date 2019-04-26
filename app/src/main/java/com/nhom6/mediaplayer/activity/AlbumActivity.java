package com.nhom6.mediaplayer.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ListView;

import com.nhom6.mediaplayer.Manager.AlbumManager;
import com.nhom6.mediaplayer.Manager.SongManager;
import com.nhom6.mediaplayer.R;
import com.nhom6.mediaplayer.adapter.AlbumAdapter;
import com.nhom6.mediaplayer.adapter.ListSongAdapter;
import com.nhom6.mediaplayer.model.Album;
import com.nhom6.mediaplayer.model.Song;

import java.util.ArrayList;

public class AlbumActivity extends AppCompatActivity {
    GridView gridView;
    //khai báo SongManager để loadSong
    AlbumManager albumsManager = new AlbumManager();
    public ArrayList<Album> _albums = new ArrayList<Album>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_album);

        //find id GirdView
        gridView =  findViewById(R.id.gridViewAB);

        //tiến hành lấy toàn bộ song trong máy
        _albums = albumsManager.loadAlbum(this);
        //đưa vào adapter để hiển thị
        AlbumAdapter listAlbumAdapter = new AlbumAdapter(this,R.layout.girdview_album,_albums);
        gridView.setAdapter(listAlbumAdapter);
    }
}
