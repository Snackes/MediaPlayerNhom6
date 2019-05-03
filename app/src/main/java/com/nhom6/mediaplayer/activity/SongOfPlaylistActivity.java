package com.nhom6.mediaplayer.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhom6.mediaplayer.Database.MyDatabaseHelper;
import com.nhom6.mediaplayer.Manager.SongManager;
import com.nhom6.mediaplayer.R;
import com.nhom6.mediaplayer.adapter.ListSongAdapter;
import com.nhom6.mediaplayer.model.PlayList;
import com.nhom6.mediaplayer.model.Song;

import java.util.ArrayList;

public class SongOfPlaylistActivity extends AppCompatActivity {

    PlayList playlist;
    TextView PlayListName;
    ListView LvSongInPlayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_song_of_playlist);

        this.PlayListName=this.findViewById(R.id.txtPlaylistname);
        this.LvSongInPlayList=this.findViewById(R.id.listSongofActivity);

        Intent intent = this.getIntent();
        this.playlist = (PlayList) intent.getSerializableExtra("playlist");

        this.PlayListName.setText(playlist.getTitle());
        MyDatabaseHelper db = new MyDatabaseHelper(this);
        SongManager sm=new SongManager();
        ArrayList<String> listIDsong= db.GetListSongInPlayList(this.playlist.getIDPlayList());
        if(listIDsong.size()==0){
            Toast.makeText(getApplicationContext(), "Méo có gì trong này", Toast.LENGTH_LONG).show();
            // Trở lại MainActivity.
            this.onBackPressed();
            return;
        }
        ArrayList<Song> _songs = new ArrayList<Song>();
        for(int i=0; i<listIDsong.size();i++){
            Song song = sm.loadSongWithID(this,Integer.parseInt(listIDsong.get(i)));
            _songs.add(song);
        }

        //đưa vào adapter để hiển thị
        //đưa vào adapter để hiển thị
        ListSongAdapter listSongAdapter = new ListSongAdapter(this,R.layout.row_item_song,_songs);
        LvSongInPlayList.setAdapter(listSongAdapter);
    }
}
