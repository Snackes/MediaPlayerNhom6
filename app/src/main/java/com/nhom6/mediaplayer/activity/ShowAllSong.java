package com.nhom6.mediaplayer.activity;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
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
    //khai báo song mình sẽ lấy ra để
    //
    Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_all_song);

        //find id ListView
        listView = (ListView) findViewById(R.id.listViewSong);

        //tiến hành lấy toàn bộ song trong máy
        _songs = songsManager.loadSong(this);
        //đưa vào adapter để hiển thị
        ListSongAdapter listSongAdapter = new ListSongAdapter(this,R.layout.row_item_song,_songs);
        listView.setAdapter(listSongAdapter);

        context = getApplicationContext();



//
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Lấy item tại vị trí click
                Song newsong = (Song) parent.getItemAtPosition(position);

                //tạo bundle

                Bundle Song = new Bundle();
                // nhét thông tin vào bundle
                Song.putString("Songname", newsong.getSongname());
                Song.putString("Artistname", newsong.getArtistname());
                Song.putString("Album", newsong.getAlbum());
                Song.putString("SongUrl", newsong.getSongUrl());
                Song.putString("AlbumArt", newsong.getAlbumArt());
                Song.putInt("ArtistnameId", newsong.getArtistnameId());
                Song.putInt("AlbumId", newsong.getAlbumId());
                Song.putInt("Duration", newsong.getDuration());
                Song.putInt("Songid", newsong.getSongid());
                //
                //
                Intent i = new Intent(context, PlayMedia.class);
                i.putExtras(Song);

                startActivity(i);
            }
        });
    }
}
