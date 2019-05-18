package com.nhom6.mediaplayer.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.nhom6.mediaplayer.Database.MyDatabaseHelper;
import com.nhom6.mediaplayer.MainActivity;
import com.nhom6.mediaplayer.R;
import com.nhom6.mediaplayer.adapter.ListSongAdapter;
import com.nhom6.mediaplayer.model.Song;
import com.nhom6.mediaplayer.service.MetaDataCompat;

import java.util.ArrayList;

public class LoveActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    //khai báo ListView cho adapter
    Activity activity = this;
    Boolean Refresh = true;
    Boolean CheckClickitem = true;
    private SearchView searchView;
    private SwipeMenuListView listLoveSong;
    public Context context = this;
    public ArrayList<Song> _lovesongs = new ArrayList<Song>();
    public ListSongAdapter listsongtAdapter;
    private final MetaDataCompat metaDataCompat = new MetaDataCompat();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_love);

        Refresh = false;
        //find id ListView
        listLoveSong = (SwipeMenuListView) findViewById(R.id.listLoveSong);

        MyDatabaseHelper db = new MyDatabaseHelper(context);
        _lovesongs = db.GetListSongFavorite();

        activity = this;
        listsongtAdapter = new ListSongAdapter(this, _lovesongs);
        listLoveSong.setAdapter(listsongtAdapter);
        setSwipeListView();
        ClickItem();

        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
    }

    //Xét icon xóa cho từng bài hát
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
        listLoveSong.setMenuCreator(creator);
        DeleteSongInFavorite();
    }

    @Override
    protected void onResume() {
        if (Refresh == true) {
            RefreshLoveActivity();
        }
        Refresh = true;
        CheckClickitem = true;
        super.onResume();
    }

    //Xóa 1 bài hát được chọn khỏi danh sách bài hát yêu thích
    public void DeleteSongInFavorite() {
        listLoveSong.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        new AlertDialog.Builder(context)
                                .setTitle("Xóa khỏi yêu thích")
                                .setMessage("Bạn muốn xóa bài hát này..?")
                                .setIcon(R.drawable.adele)
                                .setPositiveButton("Đồng ý",
                                        new DialogInterface.OnClickListener() {
                                            @TargetApi(11)
                                            public void onClick(DialogInterface dialog, int id) {
                                                MyDatabaseHelper db = new MyDatabaseHelper(context);
                                                db.deleteSongInFavorite(_lovesongs.get(position).getSongid());
                                                _lovesongs = db.GetListSongFavorite();
                                                listsongtAdapter = new ListSongAdapter(activity, _lovesongs);
                                                listLoveSong.setAdapter(listsongtAdapter);
                                                setSwipeListView();
                                                dialog.cancel();
                                            }
                                        })
                                .setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                                    @TargetApi(11)
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                }).show();
                }
                // false : close the menu; true : not close the menu
                return false;


            }
        });
    }

    public void ClickItem() {

        listLoveSong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(CheckClickitem==false){
                    return;
                }
                CheckClickitem=false;
                //TODO: khi mình intent 1 item sang PlayActivity mình sẽ gửi 2 thứ: position của item và listID ( toàn bộ )
                //Lấy item tại vị trí click
                Song newsong = (Song) parent.getItemAtPosition(position);

                //lấy listID
                ArrayList<String> lstUrlSong = GetListUrlSong();
                ArrayList<Integer> lstIDSong = GetListIDSong();

                //tạo bundle

                Bundle Package = new Bundle();
                // nhét thông tin vào bundle
                Package.putInt("position", position);
                Package.putInt("test", 4);
                Package.putInt("IDObject", 1);
                //Package.putStringArrayList("lstUrlSong", lstUrlSong);
                Package.putIntegerArrayList("lstIDSong", lstIDSong);

                //
                Intent i = new Intent(context, PlayActivity.class);


                metaDataCompat.music.clear();
                MyDatabaseHelper db = new MyDatabaseHelper(context);
                for (Integer item : lstIDSong) {
                    Song song = db.GetInfoSong(item);
                    //nạp vào metadata
                    metaDataCompat.createMediaMetadataCompat(song);
                }
//                Intent i1 = new Intent(context, PlayActivity.class);
//                i1.putExtras(Package);
                i.putExtras(Package);
                startActivity(i);
            }
        });

    }

    //TODO: input : ArrayList<Song> , output : ArrayList<String> là các Url của song
    private ArrayList<String> GetListUrlSong() {
        ArrayList<String> lstUrlSong = new ArrayList<String>();

        for (Song item : _lovesongs) {
            lstUrlSong.add(item.getSongUrl());
        }
        return lstUrlSong;
    }

    //TODO: input : ArrayList<Song> , output : ArrayList<Integer> là các ID của song
    private ArrayList<Integer> GetListIDSong() {
        ArrayList<Integer> lstIDSong = new ArrayList<Integer>();

        for (Song item : _lovesongs) {
            lstIDSong.add(item.getSongid());
        }
        return lstIDSong;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        MyDatabaseHelper db = new MyDatabaseHelper(this);
        _lovesongs = db.SearchSong(text, 1, 4);
        listsongtAdapter = new ListSongAdapter(this, _lovesongs);
        listLoveSong.setAdapter(listsongtAdapter);
        setSwipeListView();
        return false;
    }

    public void RefreshLoveActivity() {
        MyDatabaseHelper db = new MyDatabaseHelper(context);
        _lovesongs = db.GetListSongFavorite();
        listsongtAdapter = new ListSongAdapter(this, _lovesongs);
        listLoveSong.setAdapter(listsongtAdapter);
    }


}
