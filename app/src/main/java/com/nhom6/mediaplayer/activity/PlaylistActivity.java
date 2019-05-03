package com.nhom6.mediaplayer.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.nhom6.mediaplayer.Manager.PlayListManager;
import com.nhom6.mediaplayer.R;
import com.nhom6.mediaplayer.adapter.PlaylistAdapter;
import com.nhom6.mediaplayer.model.PlayList;

import java.util.ArrayList;


public class PlaylistActivity extends AppCompatActivity {

    final Context context = this;
    private Button buttonCreatePlaylist;
    private static final int MY_REQUEST_CODE = 1000;
    //khai báo ListView cho adapter
    private ListView listView;
    //khai báo SongManager để loadSong
    PlayListManager playlistsManager = new PlayListManager();
    public ArrayList<PlayList> _playlists = new ArrayList<PlayList>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_playlist);

        buttonCreatePlaylist = findViewById(R.id.btnCreatePlayList);
        buttonCreatePlaylist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);
                // set title
                alertDialogBuilder.setTitle("Create PlayList");
                // Get the layout inflater
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                final LayoutInflater inflater = getLayoutInflater();
                final View moduleView = inflater.inflate(R.layout.create_playlist,null);
                alertDialogBuilder.setView(moduleView);
                final EditText edtPlayListName=moduleView.findViewById(R.id.namePlayList);
                        // Add action buttons
                        alertDialogBuilder.setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {


                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                String title= edtPlayListName.getText().toString();
                                if(title.equals("")) {
                                    Toast.makeText(getApplicationContext(),
                                            "Đm chưa nhập tạo cái qq...", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                playlistsManager.CreatePlayList(title,context);
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
        //find id ListView
        listView = (ListView) findViewById(R.id.listPlayList);
        //tiến hành lấy toàn bộ playlist trong máy
        _playlists = playlistsManager.loadPlayList(this);
        //đưa vào adapter để hiển thị
        PlaylistAdapter listPlayListAdapter = new PlaylistAdapter(this,R.layout.row_item_playlist,_playlists);
        listView.setAdapter(listPlayListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlayList playList=_playlists.get(position);
                Intent intent = new Intent(context, SongOfPlaylistActivity.class);
                intent.putExtra("playlist", playList);
                startActivity(intent);
                startActivityForResult(intent, MY_REQUEST_CODE);
            }
        });
    }


}
