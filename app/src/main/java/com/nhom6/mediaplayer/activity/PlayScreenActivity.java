package com.nhom6.mediaplayer.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;


import com.nhom6.mediaplayer.Manager.PlayListManager;
import com.nhom6.mediaplayer.Manager.SongManager;
import com.nhom6.mediaplayer.R;
import com.nhom6.mediaplayer.adapter.CustomPagerAdapter;
import com.nhom6.mediaplayer.adapter.ListSameSongAdapter;
import com.nhom6.mediaplayer.adapter.ListSongAdapter;
import com.nhom6.mediaplayer.adapter.PlaylistAdapterView;
import com.nhom6.mediaplayer.model.PlayList;
import com.nhom6.mediaplayer.model.Song;

import java.util.ArrayList;


public class PlayScreenActivity extends AppCompatActivity {
    final Context context = this;
    private Button button;
    //khai báo ListView cho adapter
    private ListView listView;
    //khai báo SongManager để loadSong
    PlayListManager playlistsManager = new PlayListManager();
    public ArrayList<PlayList> _playlists = new ArrayList<PlayList>();
    //khai báo ListView cho adapter
    private ListView listSameSong;

    //khai báo SongManager để loadSong
    SongManager songsManager = new SongManager();
    public ArrayList<Song> _songs = new ArrayList<Song>();
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_play_screen);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(this));

        //load list nhac tuong tu len dialog
        LayoutInflater inflaterDia = getLayoutInflater();
        View mViewSame = inflaterDia.inflate(R.layout.activity_same_song, null);
        //find id ListView
        listSameSong = (ListView) mViewSame.findViewById(R.id.listSameSong);

        //tiến hành lấy song tuong tu trong máy
        _songs = songsManager.loadSong(this);
        //đưa vào adapter để hiển thị
        ListSameSongAdapter listSameSongAdapter = new ListSameSongAdapter(this,R.layout.row_item_samesong,_songs);
        listSameSong.setAdapter(listSameSongAdapter);




    }
    public void showPlayListDialog(View view)
    {

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //load playlist len dialog
        LayoutInflater inflaterDia = getLayoutInflater();
        View mView = inflaterDia.inflate(R.layout.dialog_addplaylist, null);
        listView = mView.findViewById(R.id.listDialogPL);

        //tiến hành lấy toàn bộ song trong máy
        _playlists = playlistsManager.loadPlayList(this);
        //đưa vào adapter để hiển thị
        PlaylistAdapterView listPlayListVAdapter = new PlaylistAdapterView(this, R.layout.row_item_playlist_view, _playlists);
        listView.setAdapter(listPlayListVAdapter);
        dialog.setContentView(mView);
        dialog.setCancelable(true);
        Window window=dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(R.drawable.borderradius);
        dialog.show();
    }
    public void clickLove(View view)
    {
        Snackbar.make(view,"Đã đưa vào mục yêu thích",Snackbar.LENGTH_LONG)
                .setAction("No action", null).show();
    }


}