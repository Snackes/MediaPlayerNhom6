package com.nhom6.mediaplayer.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Dialog dialogAdd = new Dialog(context);
                        dialogAdd.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        //load playlist len dialog
                        LayoutInflater inflaterDia = getLayoutInflater();
                        View mView = inflaterDia.inflate(R.layout.dialog_addplaylist, null);
                        listViewAdd = mView.findViewById(R.id.listDialogPL);

                        //tiến hành lấy toàn bộ song trong máy
                        _playlists = playlistsManager.loadPlayList(context);
                        //đưa vào adapter để hiển thị
                        PlaylistAdapterView listPlayListVAdapter = new PlaylistAdapterView(context, R.layout.row_item_playlist_view, _playlists);
                        listViewAdd.setAdapter(listPlayListVAdapter);
                        dialogAdd.setContentView(mView);
                        dialogAdd.setCancelable(true);
                        Window window=dialogAdd.getWindow();
                        window.setGravity(Gravity.CENTER);
                        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        window.setBackgroundDrawableResource(R.drawable.borderradius);
                        dialogAdd.show();
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
