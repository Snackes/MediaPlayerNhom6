package com.nhom6.mediaplayer.activity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.nhom6.mediaplayer.Database.MyDatabaseHelper;
import com.nhom6.mediaplayer.Manager.PlayListManager;
import com.nhom6.mediaplayer.R;
import com.nhom6.mediaplayer.adapter.PlaylistAdapter;
import com.nhom6.mediaplayer.model.PlayList;

import java.io.Serializable;
import java.util.ArrayList;


public class PlaylistActivity extends AppCompatActivity {

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
                                    "Đm chưa nhập tạo cái qq...", Toast.LENGTH_LONG).show();
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

                        TextView dialog_descriptionrepair = (TextView) dialogrepair.findViewById(R.id.dialog_description_repair);
                        final EditText edtnewPlayListName=(EditText)dialogrepair.findViewById(R.id.newnamePlayList);
                        dialog_descriptionrepair.setText(String.valueOf("Muốn sửa thật không ?"));
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

                                //tiến hành lấy toàn bộ playlist trong máy
                                _playlists = playlistsManager.loadPlayList(activity);
                                //đưa vào adapter để hiển thị
                                PlaylistAdapter listPlayListAdapter = new PlaylistAdapter(activity,_playlists);
                                listView.setAdapter(listPlayListAdapter);
                                setSwipeListView();
                                dialogrepair.cancel();
                            }
                        });

                        dialogrepair.show();
                        break;
                    case 1://xóa playlist
                        final Dialog dialog = new Dialog(context);
                        dialog.getWindow();
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        dialog.setContentView(R.layout.dialog_data);
                        TextView dialog_title = (TextView) dialog.findViewById(R.id.dialog_title);
                        dialog_title.setText(String.valueOf("Xóa khỏi danh sách playlist"));

                        TextView dialog_description = (TextView) dialog.findViewById(R.id.dialog_description);
                        dialog_description.setText(String.valueOf("Bạn có muốn xóa playlist này ?"));

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
                                db.deletePlayList(_playlists.get(position).IDPlayList);

                                //tiến hành lấy toàn bộ playlist trong máy
                                _playlists = playlistsManager.loadPlayList(activity);
                                //đưa vào adapter để hiển thị
                                PlaylistAdapter listPlayListAdapter = new PlaylistAdapter(activity,_playlists);
                                listView.setAdapter(listPlayListAdapter);
                                setSwipeListView();
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

}
