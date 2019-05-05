package com.nhom6.mediaplayer.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
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
    SwipeMenuListView LvSongInPlayList;
    ArrayList<Song> _songs = new ArrayList<Song>();
    final Context context = this;
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
        _songs= db.GetListSongInPlayList(this.playlist.getIDPlayList());
        if(_songs.size()==0){
            Toast.makeText(getApplicationContext(), "Méo có gì trong này", Toast.LENGTH_LONG).show();
            // Trở lại MainActivity.
            this.onBackPressed();
            return;
        }
        //đưa vào adapter để hiển thị
        ListSongAdapter listSongAdapter = new ListSongAdapter(this,R.layout.row_item_song,_songs);
        LvSongInPlayList.setAdapter(listSongAdapter);
        setSwipeListView();
    }
    private void setSwipeListView() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(context);
                // set item background
                deleteItem.setBackground(R.color.pinkic);
                // set item width
                deleteItem.setWidth(100);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        LvSongInPlayList.setMenuCreator(creator);
        LvSongInPlayList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        final Dialog dialog = new Dialog(context);
                        dialog.getWindow();
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.setContentView(R.layout.dialog_data);
                        TextView dialog_title = (TextView) dialog.findViewById(R.id.dialog_title);
                        dialog_title.setText(String.valueOf("Xóa khỏi playlist...!"));

                        TextView dialog_description = (TextView) dialog.findViewById(R.id.dialog_description);
                        dialog_description.setText(String.valueOf("Bạn có muốn xóa bài hát?"));

                        Button buttonCancel = (Button) dialog.findViewById(R.id.buttonCancel);
                        buttonCancel.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });

                        Button buttonOK = (Button) dialog.findViewById(R.id.buttonOK);
                        buttonOK.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                MyDatabaseHelper db=new MyDatabaseHelper(context);
                                db.deleteSongInPlayList(_songs.get(position).getSongid(),playlist.getIDPlayList());
                                //return;
                                dialog.cancel();
                            }
                        });
                        dialog.show();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;


            }
        });


    }
}
