package com.nhom6.mediaplayer.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;

import com.nhom6.mediaplayer.MainActivity;
import com.nhom6.mediaplayer.Manager.PlayListManager;
import com.nhom6.mediaplayer.R;
import com.nhom6.mediaplayer.adapter.PlaylistAdapter;
import com.nhom6.mediaplayer.adapter.PlaylistAdapterView;
import com.nhom6.mediaplayer.model.PlayList;

import java.util.ArrayList;


public class PlayActivity extends AppCompatActivity {
    final Context context = this;
    private Button button;
    //khai báo ListView cho adapter
    private ListView listView;
    //khai báo SongManager để loadSong
    PlayListManager playlistsManager = new PlayListManager();
    public ArrayList<PlayList> _playlists = new ArrayList<PlayList>();

    public static final int MY_REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_play);

    }

    public void showMainSreen(View view) {
        try {

            Intent intent = new Intent(this, MainActivity.class); //Call GreetingActivity
            this.startActivityForResult(intent, MY_REQUEST_CODE);
        } catch (Exception e) {
            System.out.println(e);
        }
    }





}
