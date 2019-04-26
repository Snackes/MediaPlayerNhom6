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

        //load playlist len dialog
        listView = (ListView) findViewById(R.id.listDialogPL);

        //tiến hành lấy toàn bộ song trong máy
        _playlists = playlistsManager.loadPlayList(this);
        //đưa vào adapter để hiển thị
        PlaylistAdapter listPlayListAdapter = new PlaylistAdapter(this,R.layout.row_item_playlist,_playlists);
        listView.setAdapter(listPlayListAdapter);

        button = findViewById(R.id.btnThemPlayList);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set title
                alertDialogBuilder.setTitle("Your Title");
                    // Get the layout inflater
                LayoutInflater inflater = getLayoutInflater();

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                alertDialogBuilder.setView(inflater.inflate(R.layout.dialog_addplaylist, null))
                // Add action buttons
                .setPositiveButton(R.string.signin, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });




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
