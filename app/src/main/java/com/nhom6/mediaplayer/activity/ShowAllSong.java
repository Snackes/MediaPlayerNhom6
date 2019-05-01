package com.nhom6.mediaplayer.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.nhom6.mediaplayer.Database.MyDatabaseHelper;
import com.nhom6.mediaplayer.Manager.SongManager;
import com.nhom6.mediaplayer.R;
import com.nhom6.mediaplayer.adapter.ListSongAdapter;
import com.nhom6.mediaplayer.model.Playlist;
import com.nhom6.mediaplayer.model.Song;

import java.util.ArrayList;
import java.util.List;

public class ShowAllSong extends AppCompatActivity {

    //khai báo ListView cho adapter
    private ListView listView;

    //khai báo SongManager để loadSong
    SongManager songsManager = new SongManager();
    public ArrayList<Song> _songs = new ArrayList<Song>();
    //khai báo song mình sẽ lấy ra để
    //
    Context context;

    private static final int MENU_ITEM_CREATE = 222;
    private static final int MENU_ITEM_PLAYLIST = 333;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_all_song);

        //find id ListView
        listView = (ListView) findViewById(R.id.listViewSong);

        //tiến hành lấy toàn bộ song trong máy
        _songs = songsManager.loadSong(this);
        //đưa vào adapter để hiển thị
        ListSongAdapter listSongAdapter = new ListSongAdapter(this,R.layout.row_item_song,_songs);
        listView.setAdapter(listSongAdapter);
        registerForContextMenu(this.listView);
        context = getApplicationContext();
    }
    @Override
    public void onCreateContextMenu (ContextMenu menu, View view,
                                     ContextMenu.ContextMenuInfo menuInfo){

        super.onCreateContextMenu(menu, view, menuInfo);
        menu.setHeaderTitle("Thêm vào playlist");

        // groupId, itemId, order, title
        menu.add(0, MENU_ITEM_CREATE, 1, "Tạo Playlist");

        MyDatabaseHelper db = new MyDatabaseHelper(this);
        List<Playlist> temp= db.getAllPlaylists();
        for(int i=0;i<temp.size();i++){
            menu.add(0, MENU_ITEM_PLAYLIST,temp.get(i).getIDPlaylist(), temp.get(i).getTitle());
        }
    }
    @Override
    public boolean onContextItemSelected (MenuItem item){
        AdapterView.AdapterContextMenuInfo
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final Song selectedSong = (Song) this.listView.getItemAtPosition(info.position);

        if (item.getItemId() == MENU_ITEM_CREATE) {
            Intent intent = new Intent(this, ShowAllSongInPlaylist.class);
            // Start createPlaylist, có phản hồi.
            //this.startActivityForResult(intent, MY_REQUEST_CODE);
        } else if (item.getItemId() == MENU_ITEM_PLAYLIST) {
            MyDatabaseHelper db=new MyDatabaseHelper(this);
            Playlist playlist=new Playlist();
            playlist.setIDPlaylist(item.getOrder());
            playlist.setIDsong(selectedSong.getSongid());
            playlist.setTitle(item.getTitle().toString());
            db.addSongForPlaylist(playlist);
            Toast.makeText(getApplicationContext(),
                    "thêm được rồi đm!", Toast.LENGTH_LONG).show();
        }
        return true;
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Lấy item tại vị trí click
                Song newsong = (Song) parent.getItemAtPosition(position);

                //tạo bundle

                Bundle Song = new Bundle();
                // nhét thông tin vào bundle
                Song.putString("Songname", newsong.getSongname());
                Song.putString("Artistname", newsong.getArtistname());
                Song.putString("Album", newsong.getAlbum());
                Song.putString("SongUrl", newsong.getSongUrl());
                Song.putString("AlbumArt", newsong.getAlbumArt());
                Song.putInt("ArtistnameId", newsong.getArtistnameId());
                Song.putInt("AlbumId", newsong.getAlbumId());
                Song.putInt("Duration", newsong.getDuration());
                Song.putInt("Songid", newsong.getSongid());
                //
                //
                Intent i = new Intent(context, PlayMedia.class);
                i.putExtras(Song);

                startActivity(i);
            }
        });
    }
}
