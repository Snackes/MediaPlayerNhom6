package com.nhom6.mediaplayer.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.nhom6.mediaplayer.Database.MyDatabaseHelper;
import com.nhom6.mediaplayer.R;
import com.nhom6.mediaplayer.adapter.ListSongAdapter;
import com.nhom6.mediaplayer.model.Song;

import java.util.ArrayList;

public class LoveActivity extends AppCompatActivity {
    //khai báo ListView cho adapter
    private SwipeMenuListView listLoveSong;
    public Context context = this;
    public ArrayList<Song> _lovesongs = new ArrayList<Song>();
    private ListSongAdapter listsongtAdapter;

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

        //tiến hành lấy toàn bộ song trong máy
        MyDatabaseHelper db=new MyDatabaseHelper(context);
        _lovesongs=db.GetListSongFavorite();
        //đưa vào adapter để hiển thị
        listsongtAdapter = new ListSongAdapter(this,R.layout.row_item_lovesong,_lovesongs);
        listLoveSong.setAdapter(listsongtAdapter);
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
        listLoveSong.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        break;
                    case 1:
                        final Dialog dialog = new Dialog(context);
                        dialog.getWindow();
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.setContentView(R.layout.dialog_data);
                        TextView dialog_title = (TextView) dialog.findViewById(R.id.dialog_title);
                        dialog_title.setText(String.valueOf("Xóa khỏi danh sách"));

                        TextView dialog_description = (TextView) dialog.findViewById(R.id.dialog_description);
                        dialog_description.setText(String.valueOf("Bạn muốn xóa bài hát này chứ ?"));

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
                                db.deleteSongInFavorite(_lovesongs.get(position).getSongid());
                                _lovesongs=db.GetListSongFavorite();
                                listLoveSong.invalidateViews();
                                listsongtAdapter.notifyDataSetChanged();
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
