package com.nhom6.mediaplayer.activity;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.nhom6.mediaplayer.Database.MyDatabaseHelper;
import com.nhom6.mediaplayer.MainActivity;
import com.nhom6.mediaplayer.Manager.PlayListManager;
import com.nhom6.mediaplayer.R;
import com.nhom6.mediaplayer.adapter.ListSongAdapter;
import com.nhom6.mediaplayer.adapter.PlaylistAdapter;
import com.nhom6.mediaplayer.model.PlayList;
import com.nhom6.mediaplayer.model.Song;

import java.io.Serializable;
import java.util.ArrayList;


public class PlaylistActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private SearchView searchView;
    Activity activity=this;
    final Context context = this;
    private Button buttonCreatePlaylist;
    //khai báo ListView cho adapter
    private SwipeMenuListView listView;
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

        listView = (SwipeMenuListView) findViewById(R.id.listPlayList);
        buttonCreatePlaylist = findViewById(R.id.btnCreatePlayList);
        //tạo mới 1 playlist
        CreatePLaylist();
        //tiến hành lấy toàn bộ playlist trong máy
        _playlists = playlistsManager.loadPlayList(this);
        //đưa vào adapter để hiển thị
        PlaylistAdapter listPlayListAdapter = new PlaylistAdapter(this,_playlists);
        listView.setAdapter(listPlayListAdapter);
        //gọi hàm xử lí khi click vào 1 playlist
        clickItemPlaylist();
        setSwipeListView();

        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
    }

    //xử lí khi click vào 1 playlist
    public void clickItemPlaylist(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //Xem danh sách bài hát khi chọn vào 1 playlist
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlayList playList=_playlists.get(position);
                Intent intent = new Intent(context, SongOfPlaylistActivity.class);
                intent.putExtra("playlist",(Serializable)playList);
                startActivity(intent);
            }
        });
    }

    //Tạo mới 1 playlist
    public void CreatePLaylist(){
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
                                    "Bạn chưa nhập tên cho PlayList..!", Toast.LENGTH_LONG).show();
                            return;
                        }
                        playlistsManager.CreatePlayList(title,context);
                        //tiến hành lấy toàn bộ playlist trong máy
                        _playlists = playlistsManager.loadPlayList(activity);
                        //đưa vào adapter để hiển thị
                        PlaylistAdapter listPlayListAdapter = new PlaylistAdapter(activity,_playlists);
                        listView.setAdapter(listPlayListAdapter);
                        setSwipeListView();
                        dialog.cancel();
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

    //xử lí chức năng xóa sửa trên từng playlist
    public void DeleteAndEditPlaylist(){
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0://sửa tên playlist
                        final Dialog dialogrepair = new Dialog(context);
                        dialogrepair.getWindow();
                        dialogrepair.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialogrepair.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialogrepair.setContentView(R.layout.dialog_repair);
                        TextView dialog_titlerepair = (TextView) dialogrepair.findViewById(R.id.dialog_title_repair);
                        dialog_titlerepair.setText(String.valueOf("Sửa tên playlist"));
                        final EditText edtnewPlayListName=(EditText)dialogrepair.findViewById(R.id.newnamePlayList);
                        Button buttonCancelrepair = (Button) dialogrepair.findViewById(R.id.buttonCancel_repair);
                        buttonCancelrepair.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                dialogrepair.cancel();
                            }
                        });

                        Button buttonOKrepair = (Button) dialogrepair.findViewById(R.id.buttonOK_repair);
                        buttonOKrepair.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                String newName= edtnewPlayListName.getText().toString();
                                edtnewPlayListName.setText(newName);
                                MyDatabaseHelper db=new MyDatabaseHelper(context);
                                db.UpdateNamePlaylist(_playlists.get(position).getIDPlayList(),newName);
                                //ti?n hành l?y toàn b? playlist trong máy
                                _playlists = playlistsManager.loadPlayList(activity);
                                //dua vào adapter d? hi?n th?
                                PlaylistAdapter listPlayListAdapter = new PlaylistAdapter(activity,_playlists);
                                listView.setAdapter(listPlayListAdapter);
                                setSwipeListView();
                                dialogrepair.cancel();
                            }
                        });

                        dialogrepair.show();
                        break;
                    case 1://xóa playlist
                        new android.app.AlertDialog.Builder(context)
                            .setTitle("Xóa khỏi danh sách")
                            .setMessage("Bạn muốn xóa PlayList này ?")
                            .setIcon(R.drawable.adele)
                            .setPositiveButton("Đồng ý",
                                    new DialogInterface.OnClickListener() {
                                        @TargetApi(11)
                                        public void onClick(DialogInterface dialog, int id) {
                                            MyDatabaseHelper db=new MyDatabaseHelper(context);
                                            db.deletePlayList(_playlists.get(position).IDPlayList);
                                            //tiến hành lấy toàn bộ playlist trong máy
                                            _playlists = playlistsManager.loadPlayList(activity);
                                            //đưa vào adapter để hiển thị
                                            PlaylistAdapter listPlayListAdapter = new PlaylistAdapter(activity,_playlists);
                                            listView.setAdapter(listPlayListAdapter);
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
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;


            }
        });

    }

    private void setSwipeListView() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem repairItem = new SwipeMenuItem(context);
                // set item background
                repairItem.setBackground(R.color.greenic);

                // set item width
                repairItem.setWidth(100);
                // set a icon
                repairItem.setIcon(R.drawable.ic_repair);
                // add to menu
                menu.addMenuItem(repairItem);
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
        listView.setMenuCreator(creator);
        //Gọi hàm lắng nghe khi chọn chức năng xóa hoặc sửa trên từng playlist
        DeleteAndEditPlaylist();

    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        MyDatabaseHelper db = new MyDatabaseHelper(this);
        _playlists = db.SearchPlayList(text);
        //đưa vào adapter để hiển thị
        PlaylistAdapter listPlayListAdapter = new PlaylistAdapter(this,_playlists);
        listView.setAdapter(listPlayListAdapter);
        return false;
    }
}
