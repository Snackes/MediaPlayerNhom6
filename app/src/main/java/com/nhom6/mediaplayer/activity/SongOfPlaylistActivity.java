package com.nhom6.mediaplayer.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.nhom6.mediaplayer.Database.MyDatabaseHelper;
import com.nhom6.mediaplayer.R;
import com.nhom6.mediaplayer.adapter.ListSongAdapter;
import com.nhom6.mediaplayer.model.Album;
import com.nhom6.mediaplayer.model.Artist;
import com.nhom6.mediaplayer.model.PlayList;
import com.nhom6.mediaplayer.model.Song;
import com.nhom6.mediaplayer.service.MetaDataCompat;

import java.util.ArrayList;

public class SongOfPlaylistActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    Activity activity=this;
    int kt=0;
    int test=0;
    //
    Boolean CheckClickitem = true;
    //

    int idObject=0;
    PlayList playlist;
    Album album;
    Artist artist;
    TextView PlayListName;
    private SearchView searchView;
    SwipeMenuListView LvSongInPlayList;
    ArrayList<Song> _songs = new ArrayList<Song>();
    final Context context = this;

    private final MetaDataCompat metaDataCompat = new MetaDataCompat();
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
        getdata();
        //đưa vào adapter để hiển thị
        //ListSongAdapter listSongAdapter = new ListSongAdapter(this, R.layout.row_item_song, _songs);
        ListSongAdapter listSongAdapter = new ListSongAdapter(this,_songs);
        LvSongInPlayList.setAdapter(listSongAdapter);
        setSwipeListView();
        ClickItem();
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
    }

    public void getdata(){
        Intent intent = this.getIntent();

        //TH show tất cả bài hát có trong 1 playlist được chọn
        if(intent.getSerializableExtra("playlist")!=null) {
            test=1;
            this.playlist = (PlayList) intent.getSerializableExtra("playlist");
            this.PlayListName.setText(playlist.getTitle());
            MyDatabaseHelper db = new MyDatabaseHelper(this);
            idObject=this.playlist.getIDPlayList();
            _songs = db.GetListSongInPlayList(idObject);
            kt=1;
        }
        //TH show tất cả bài hát có trong 1 album được chọn
        if(intent.getSerializableExtra("Album")!=null) {
            test=2;
            this.album= (Album) getIntent().getSerializableExtra("Album");
            this.PlayListName.setText(album.getAlbumTitle());
            MyDatabaseHelper db = new MyDatabaseHelper(this);
            idObject=this.album.getAlbumID();
            _songs = db.GetListSongInAlbum(idObject);
        }
        //TH show tất cả bài hát có trong 1 ca sĩ được chọn
        if(intent.getSerializableExtra("Singer")!=null) {
            test=3;
            this.artist= (Artist) getIntent().getSerializableExtra("Singer");
            this.PlayListName.setText(artist.getArtistName());
            MyDatabaseHelper db = new MyDatabaseHelper(this);
            idObject=this.artist.getArtistID();
            _songs = db.GetListSongOfArtist(idObject);
        }

        //Kiểm tra List bài hát có gì không, nếu không có thì thoát
        if (_songs.size() == 0) {
            Toast.makeText(getApplicationContext(), "Méo có gì trong này", Toast.LENGTH_LONG).show();
            // Trở lại MainActivity.
            this.onBackPressed();
            return;
        }
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


        //nếu là bài hát trong playlist mới cho xóa,còn trong album hoặc ca sĩ thì không
        // set creator
        if(kt==1) {
            LvSongInPlayList.setMenuCreator(creator);
            DeleteSongInPlaylist();
        }
    }

    public void DeleteSongInPlaylist(){
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
                        dialog_title.setText("Xóa khỏi playlist...!");

                        TextView dialog_description = (TextView) dialog.findViewById(R.id.dialog_description);
                        dialog_description.setText("Bạn có muốn xóa bài hát?");

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
                                _songs = db.GetListSongInPlayList(playlist.getIDPlayList());
                                ListSongAdapter listSongAdapter = new ListSongAdapter(activity,_songs);
                                LvSongInPlayList.setAdapter(listSongAdapter);
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

    public void ClickItem() {
        LvSongInPlayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                Package.putInt("test", test);
                Package.putInt("IDObject", idObject);

                //Package.putStringArrayList("lstUrlSong",lstUrlSong);
                Package.putIntegerArrayList("lstIDSong",lstIDSong);
                //
                //



                final Intent i = new Intent(context, PlayActivity.class);

//                Intent i1 = new Intent(context, PlayActivity.class);
//                i1.putExtras(Package);

                metaDataCompat.music.clear();
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

        for (Song item : _songs)
        {
            lstUrlSong.add(item.getSongUrl());
        }
        return lstUrlSong;
    }

    //TODO: input : ArrayList<Song> , output : ArrayList<Integer> là các ID của song
    private ArrayList<Integer> GetListIDSong() {
        ArrayList<Integer> lstIDSong = new ArrayList<Integer>();

        for (Song item : _songs)
        {
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
        _songs = db.SearchSong(text,idObject,test);
        ListSongAdapter listSongAdapter = new ListSongAdapter(this,_songs);
        LvSongInPlayList.setAdapter(listSongAdapter);
        setSwipeListView();
        return false;
    }

    @Override
    protected void onResume() {
        CheckClickitem = true;
        super.onResume();
    }
}
