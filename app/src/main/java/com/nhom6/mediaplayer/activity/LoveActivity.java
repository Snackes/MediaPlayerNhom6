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
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.nhom6.mediaplayer.Database.MyDatabaseHelper;
import com.nhom6.mediaplayer.MainActivity;
import com.nhom6.mediaplayer.R;
import com.nhom6.mediaplayer.adapter.ListSongAdapter;
import com.nhom6.mediaplayer.model.Song;

import java.util.ArrayList;

public class LoveActivity extends AppCompatActivity {
    //khai báo ListView cho adapter
    Activity activity=this;
    private SwipeMenuListView listLoveSong;
    public Context context = this;
    public ArrayList<Song> _lovesongs = new ArrayList<Song>();
    public ListSongAdapter listsongtAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_love);

        //find id ListView
        listLoveSong = (SwipeMenuListView) findViewById(R.id.listLoveSong);

        MyDatabaseHelper db=new MyDatabaseHelper(context);
        _lovesongs=db.GetListSongFavorite();
        //đưa vào adapter để hiển thị
/*        listsongtAdapter = new ListSongAdapter(this,R.layout.row_item_lovesong,_lovesongs);
        listLoveSong.setAdapter(listsongtAdapter);*/
        activity=this;
        listsongtAdapter = new ListSongAdapter(this,_lovesongs);
        listLoveSong.setAdapter(listsongtAdapter);
        setSwipeListView();
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

    //Xóa 1 bài hát được chọn khỏi danh sách bài hát yêu thích
    public void DeleteSongInFavorite(){
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
                                                MyDatabaseHelper db=new MyDatabaseHelper(context);
                                                db.deleteSongInFavorite(_lovesongs.get(position).getSongid());
                                                _lovesongs=db.GetListSongFavorite();
                                                listsongtAdapter = new ListSongAdapter(activity,_lovesongs);
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
}
