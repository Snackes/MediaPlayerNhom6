package com.nhom6.mediaplayer.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.nhom6.mediaplayer.Manager.ArtistManager;
import com.nhom6.mediaplayer.Manager.LoveSongManager;
import com.nhom6.mediaplayer.R;
import com.nhom6.mediaplayer.adapter.LoveSongAdapter;
import com.nhom6.mediaplayer.adapter.SingerAdapter;
import com.nhom6.mediaplayer.model.Artist;
import com.nhom6.mediaplayer.model.LoveSong;

import java.util.ArrayList;

public class LoveActivity extends AppCompatActivity {
    //khai báo ListView cho adapter
    private ListView listLoveSong;

    //khai báo artistManager để loadSong
    LoveSongManager lovesongsManager = new LoveSongManager();
    public ArrayList<LoveSong> _lovesongs = new ArrayList<LoveSong>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_love);

        //find id ListView
        listLoveSong = (ListView) findViewById(R.id.listLoveSong);

        //tiến hành lấy toàn bộ song trong máy
        _lovesongs = lovesongsManager.loadSong(this);
        //đưa vào adapter để hiển thị
        LoveSongAdapter listArtistAdapter = new LoveSongAdapter(this,R.layout.row_item_lovesong,_lovesongs);
        listLoveSong.setAdapter(listArtistAdapter);
    }
}
