package com.nhom6.mediaplayer.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
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
import com.nhom6.mediaplayer.Manager.PlayListManager;
import com.nhom6.mediaplayer.Manager.SongManager;
import com.nhom6.mediaplayer.R;
import com.nhom6.mediaplayer.adapter.ListSongAdapter;
import com.nhom6.mediaplayer.adapter.PlaylistAdapterView;
import com.nhom6.mediaplayer.model.PlayList;
import com.nhom6.mediaplayer.model.Song;

import java.util.ArrayList;

public class ShowAllSong extends AppCompatActivity {
    final Context context = this;
    private Button buttonCreatePlaylist;
    //khai báo ListView cho adapter
    private SwipeMenuListView listView;
    private ListView listViewAdd;
    PlayListManager playlistsManager = new PlayListManager();
    public ArrayList<PlayList> _playlists = new ArrayList<PlayList>();
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
        setContentView(R.layout.activity_all_song);



        //find id ListView
        listView = (SwipeMenuListView) findViewById(R.id.listViewSong);
        MyDatabaseHelper db=new MyDatabaseHelper(this);
        //Kiểm tra xem trong csdl bảng song đã có dữ liệu chưa?
        if(db.CheckTableSong()==0){
            //tiến hành lấy toàn bộ song trong máy
            _songs = songsManager.loadSong(this);
            //đưa songs lấy được vào csdl
            db.addSong(_songs);
        }
        else {
            _songs=db.GetListSong();
        }

        //đưa vào adapter để hiển thị
        ListSongAdapter listSongAdapter = new ListSongAdapter(this,R.layout.row_item_song,_songs);
        listView.setAdapter(listSongAdapter);
        setSwipeListView();



    }
    private void setSwipeListView() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem plusItem = new SwipeMenuItem(context);
                // set item background
                plusItem.setBackground(R.color.greenic);

                // set item width
                plusItem.setWidth(100);
                // set a icon
                plusItem.setIcon(R.drawable.ic_add);
                // add to menu
                menu.addMenuItem(plusItem);
                SwipeMenuItem loveItem = new SwipeMenuItem(context);
                // set item background
                loveItem.setBackground(R.color.pinkwhite);
                // set item width
                loveItem.setWidth(100);
                // set a icon
                loveItem.setIcon(R.drawable.ic_love);
                // add to menu
                menu.addMenuItem(loveItem);

            }
        };

        // set creator
        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, final int index) {
                switch (index) {
                    case 0:
                        final Dialog dialogAdd = new Dialog(context);
                        dialogAdd.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        //load playlist len dialog
                        LayoutInflater inflaterDia = getLayoutInflater();
                        View mView = inflaterDia.inflate(R.layout.dialog_addplaylist, null);
                        listViewAdd = mView.findViewById(R.id.listDialogPL);
                        //tiến hành lấy toàn bộ playlist trong máy
                        _playlists = playlistsManager.loadPlayList(context);
                        //đưa vào adapter để hiển thị
                        PlaylistAdapterView listPlayListVAdapter = new PlaylistAdapterView(context, R.layout.row_item_playlist_view, _playlists);
                        listViewAdd.setAdapter(listPlayListVAdapter);
                        listViewAdd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            //lưu bài hát vào khi chọn playlist đã có
                            public void onItemClick(AdapterView<?> parent, View view, int position1, long id) {
                                MyDatabaseHelper db= new MyDatabaseHelper(context);
                                db.addSongForPlayList(_playlists.get(position1).getIDPlayList(),_songs.get(position).getSongid());
                                Toast.makeText(getApplicationContext(),
                                        "Thêm được rồi vô playlist coi đi e iu...", Toast.LENGTH_LONG).show();
                                dialogAdd.cancel();
                            }
                        });
                        dialogAdd.setContentView(mView);
                        dialogAdd.setCancelable(true);
                        Window window=dialogAdd.getWindow();
                        window.setGravity(Gravity.CENTER);
                        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        window.setBackgroundDrawableResource(R.drawable.borderradius);
                        dialogAdd.show();


                        buttonCreatePlaylist = mView.findViewById(R.id.btnCreatePlayList);
                        buttonCreatePlaylist.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                dialogAdd.hide();
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                        context);
                                // set title
                                alertDialogBuilder.setTitle("Create PlayList");
                                // Get the layout inflater
                                // Inflate and set the layout for the dialog
                                // Pass null as the parent view because its going in the dialog layout
                                final LayoutInflater inflatercreate = getLayoutInflater();
                                final View moduleView = inflatercreate.inflate(R.layout.create_playlist,null);
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
                        break;
                    case 1:
                        MyDatabaseHelper db=new MyDatabaseHelper(context);
                        db.AddSongFavorite(_songs.get(position).getSongid());
                        Toast.makeText(getApplicationContext(),
                                "Thêm được rồi em iu s2...", Toast.LENGTH_LONG).show();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;


            }
        });


    }
}
