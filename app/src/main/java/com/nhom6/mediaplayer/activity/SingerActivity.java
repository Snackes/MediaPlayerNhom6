package com.nhom6.mediaplayer.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.nhom6.mediaplayer.Manager.ArtistManager;
import com.nhom6.mediaplayer.Manager.PlayListManager;
import com.nhom6.mediaplayer.R;
import com.nhom6.mediaplayer.adapter.PlaylistAdapter;
import com.nhom6.mediaplayer.adapter.SingerAdapter;
import com.nhom6.mediaplayer.model.Artist;
import com.nhom6.mediaplayer.model.PlayList;

import java.util.ArrayList;

public class SingerActivity extends AppCompatActivity {
    //khai báo ListView cho adapter
    private ListView listSinger;

    //khai báo artistManager để loadSong
    ArtistManager artistsManager = new ArtistManager();
    public ArrayList<Artist> _artists = new ArrayList<Artist>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_singer);


        //find id ListView
        listSinger = (ListView) findViewById(R.id.listViewSinger);

        //tiến hành lấy toàn bộ song trong máy
        _artists = artistsManager.loadArtist(this);
        //đưa vào adapter để hiển thị
        SingerAdapter listArtistAdapter = new SingerAdapter(this,R.layout.row_item_singer,_artists);
        listSinger.setAdapter(listArtistAdapter);
    }
}
