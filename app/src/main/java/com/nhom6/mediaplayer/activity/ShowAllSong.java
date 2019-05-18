package com.nhom6.mediaplayer.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
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

import com.nhom6.mediaplayer.adapter.PlaylistAdapter;

import com.nhom6.mediaplayer.model.PlayList;
import com.nhom6.mediaplayer.model.Song;
import com.nhom6.mediaplayer.databinding.ActivityAllSongBinding;
import com.nhom6.mediaplayer.service.MetaDataCompat;

import java.util.ArrayList;

public class ShowAllSong extends AppCompatActivity implements SearchView.OnQueryTextListener {
    Activity activity=this;
    final Context context = this;
    private Button buttonCreatePlaylist;
    private SearchView searchView;
    //khai báo ListView cho adapter
    private SwipeMenuListView listView;
    private ListView listViewAdd;
    PlayListManager playlistsManager = new PlayListManager();
    public ArrayList<PlayList> _playlists = new ArrayList<PlayList>();
    //khai báo SongManager để loadSong
    SongManager songsManager = new SongManager();
    public ArrayList<Song> _songs = new ArrayList<Song>();
    //

    private final MetaDataCompat metaDataCompat = new MetaDataCompat();

    Boolean CheckClickitem = true;

    //dùng để binding
    ActivityAllSongBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_all_song);

        binding = (ActivityAllSongBinding) DataBindingUtil.setContentView(this,        R.layout.activity_all_song);
        //find id ListView
        listView = (SwipeMenuListView) findViewById(R.id.listViewSong);
        searchView = findViewById(R.id.searchView);
        //lấy danh sách bài hát
        int kt=getdata();
        if(kt==0){
            return;
        }
        ListSongAdapter listSongAdapter = new ListSongAdapter(this, _songs);
        listView.setAdapter(listSongAdapter);
        setSwipeListView();

        ClickItem();
        searchView.setOnQueryTextListener(this);
    }

    public int getdata(){
        MyDatabaseHelper db = new MyDatabaseHelper(this);

        Intent intent = this.getIntent();
        //TH tìm kiếm trong main
        if(intent.getSerializableExtra("SearchInMain")!=null) {
            _songs = db.GetListSong();
            String NewText=intent.getSerializableExtra("SearchInMain").toString();
            searchView.setQuery(NewText,false);
            onQueryTextChange(NewText);
            return 1;
        }
        else {
            if (db.CheckTableSong() == 0) {
                Toast.makeText(getApplicationContext(), "Không có bài hát, chọn Scan để quét các bài hát có trong máy..!", Toast.LENGTH_LONG).show();
                return 0;
            } else {
                _songs = db.GetListSong();
                return 1;
            }
        }
    }

    private void setSwipeListView() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "add playlist" item
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
        ClickSwipeMenuItem();
    }

    //xử lí khi chọn 1 trong 2 chức năng của 1 bài hát trong listview
    public void ClickSwipeMenuItem(){
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, final int index) {
                switch (index) {
                    case 0://chọn chức năng thêm bài hát vào playlist
                        final Dialog dialogAdd = new Dialog(context);
                        dialogAdd.requestWindowFeature(Window.FEATURE_CONTEXT_MENU);
                        //load playlist len dialog
                        LayoutInflater inflaterDia = getLayoutInflater();
                        View mView = inflaterDia.inflate(R.layout.dialog_addplaylist, null);
                        listViewAdd = mView.findViewById(R.id.listDialogPL);
                        //tiến hành lấy toàn bộ playlist trong máy
                        _playlists = playlistsManager.loadPlayList(context);
                        //đưa vào adapter để hiển thị
                        PlaylistAdapter listPlayListAdapter = new PlaylistAdapter(activity,_playlists);
                        listViewAdd.setAdapter(listPlayListAdapter);

                        listViewAdd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            //lưu bài hát vào khi chọn playlist đã có
                            public void onItemClick(AdapterView<?> parent, View view, int position1, long id) {
                                MyDatabaseHelper db = new MyDatabaseHelper(context);
                                db.addSongForPlayList(_playlists.get(position1).getIDPlayList(), _songs.get(position).getSongid());
                                Toast.makeText(getApplicationContext(),
                                        "Thêm bài hát vào PlayList thành công..!", Toast.LENGTH_SHORT).show();
                                dialogAdd.cancel();
                            }
                        });
                        dialogAdd.setContentView(mView);
                        dialogAdd.setCancelable(true);
                        Window window = dialogAdd.getWindow();
                        window.setGravity(Gravity.CENTER);
                        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                        window.setBackgroundDrawableResource(R.drawable.borderradius);
                        dialogAdd.show();
                        buttonCreatePlaylist = mView.findViewById(R.id.btnCreatePlayList);

                        //
                        CreatePlaylist(position, dialogAdd);
                        break;
                    case 1://thêm zô danh sách bài hát yêu thích
                        MyDatabaseHelper db = new MyDatabaseHelper(context);
                        int k=db.CheckSongFavorite(_songs.get(position).getSongid());
                        if(k==0) {

                            db.AddSongFavorite(_songs.get(position).getSongid());
                            Toast.makeText(getApplicationContext(),
                                    "Đã thêm vào Yêu Thích...", 50).show();
                        }
                        else {
                            new android.app.AlertDialog.Builder(context)
                                    .setTitle("Bài hát đã có trong yêu thích.")
                                    .setMessage("Bạn muốn xóa khỏi yêu thích..?")
                                    .setIcon(R.drawable.adele)
                                    .setPositiveButton("Đồng ý",
                                            new DialogInterface.OnClickListener() {
                                                @TargetApi(11)
                                                public void onClick(DialogInterface dialog, int id) {
                                                    MyDatabaseHelper db=new MyDatabaseHelper(context);
                                                    db.deleteSongInFavorite(_songs.get(position).getSongid());
                                                    Toast.makeText(getApplicationContext(),
                                                            "Đã xóa khỏi Yêu Thích...", 50).show();
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
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }

    //tạo mới playlist đồng thời thêm bài hát đã chọn vào playlist vừa tạo
    public void CreatePlaylist(final int position, final Dialog dialogAdd) {
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
                final View moduleView = inflatercreate.inflate(R.layout.create_playlist, null);
                alertDialogBuilder.setView(moduleView);
                final EditText edtPlayListName = moduleView.findViewById(R.id.namePlayList);
                // Add action buttons
                alertDialogBuilder.setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {


                    @SuppressLint("WrongConstant")
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String title = edtPlayListName.getText().toString();
                        if (title.equals("")) {
                            Toast.makeText(getApplicationContext(),
                                    "Vui lòng nhập tên trước khi tạo Playlist..!", 50).show();
                            return;
                        }
                        playlistsManager.CreatePlayListAndAddSong(title, context, _songs.get(position).getSongid());
                        Toast.makeText(getApplicationContext(),
                                "Đã thêm vào Playlist..!", 50).show();
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

    public void ClickItem() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(CheckClickitem==false){
                    return;
                }
                CheckClickitem=false;
                //TODO: khi mình intent 1 item sang PlayActivity mình sẽ gửi 2 thứ: position của item và listID ( toàn bộ )
                //Lấy item tại vị trí click
                Song newsong = (Song) parent.getItemAtPosition(position);
                binding.setSong(newsong);

                //lấy listID
                //ArrayList<String> lstUrlSong = GetListUrlSong();
                ArrayList<Integer> lstIDSong = GetListIDSong();
                //tạo bundle

                Bundle Package = new Bundle();
                // nhét thông tin vào bundle
                Package.putInt("position", position);
                Package.putInt("test", 0);
                Package.putInt("IDObject", 0);
                //Package.putStringArrayList("lstUrlSong", lstUrlSong);
                Package.putIntegerArrayList("lstIDSong", lstIDSong);


                //
                final Intent i = new Intent(context, PlayActivity.class);


                MyDatabaseHelper db=new MyDatabaseHelper(context);
                for (Integer item: lstIDSong) {
                    Song song = db.GetInfoSong(item);
                    //nạp vào metadata
                    metaDataCompat.createMediaMetadataCompat(song);
                }

                i.putExtras(Package);
                startActivity(i);
            }
        });

    }

    //TODO: input : ArrayList<Song> , output : ArrayList<String> là các Url của song
    private ArrayList<String> GetListUrlSong() {
        ArrayList<String> lstUrlSong = new ArrayList<String>();

        for (Song item : _songs) {
            lstUrlSong.add(item.getSongUrl());
        }
        return lstUrlSong;
    }

    //TODO: input : ArrayList<Song> , output : ArrayList<Integer> là các ID của song
    private ArrayList<Integer> GetListIDSong() {
        ArrayList<Integer> lstIDSong = new ArrayList<Integer>();

        for (Song item : _songs) {
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
        _songs = db.SearchSong(text,0,0);
        ListSongAdapter listSongAdapter = new ListSongAdapter(this, _songs);
        listView.setAdapter(listSongAdapter);
        setSwipeListView();
        return false;
    }

    @Override
    protected void onResume() {
        CheckClickitem = true;
        super.onResume();
    }
}