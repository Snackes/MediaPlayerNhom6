package com.nhom6.mediaplayer.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.nhom6.mediaplayer.Database.MyDatabaseHelper;
import com.nhom6.mediaplayer.Manager.ArtistManager;
import com.nhom6.mediaplayer.Manager.PlayListManager;
import com.nhom6.mediaplayer.R;
import com.nhom6.mediaplayer.adapter.ListSongAdapter;
import com.nhom6.mediaplayer.adapter.PlaylistAdapter;
import com.nhom6.mediaplayer.adapter.SingerAdapter;
import com.nhom6.mediaplayer.model.Album;
import com.nhom6.mediaplayer.model.Artist;
import com.nhom6.mediaplayer.model.PlayList;

import java.util.ArrayList;

public class SingerActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    //khai báo ListView cho adapter
    private ListView listViewSinger;
    final Context context=this;
    private SearchView searchView;
    //khai báo artistManager để loadSong
    ArtistManager artistsManager = new ArtistManager();
    public ArrayList<Artist> _artists = new ArrayList<Artist>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_singer);

        //find id ListView
        listViewSinger = (ListView) findViewById(R.id.listViewSinger);
        //lấy danh sách ca sĩ
        getdata();
        //đưa vào adapter để hiển thị
        SingerAdapter listArtistAdapter = new SingerAdapter(this,R.layout.row_item_singer,_artists);
        listViewSinger.setAdapter(listArtistAdapter);
        ClickItemArtist();

        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
    }
    public void getdata(){
        MyDatabaseHelper db=new MyDatabaseHelper(this);
        //Kiểm tra xem trong csdl bảng song đã có dữ liệu chưa?
        if(db.CheckTableSinger()==0){
            //tiến hành lấy toàn bộ song trong máy
            _artists = artistsManager.loadArtist(this);
            //đưa songs lấy được vào csdl
            db.addSinger(_artists);
        }
        else {
            _artists=db.GetListSinger();
        }
    }

    //Xử lí khi click vào 1 ca sĩ bất kì
    public void ClickItemArtist(){
        listViewSinger.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //Xem danh sách bài hát khi chọn vào 1 album
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist=_artists.get(position);
                Intent intent = new Intent(context, SongOfPlaylistActivity.class);
                intent.putExtra("Singer", artist);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        MyDatabaseHelper db = new MyDatabaseHelper(this);
        _artists = db.SearchSinger(text);
        SingerAdapter listArtistAdapter = new SingerAdapter(this,R.layout.row_item_singer,_artists);
        listViewSinger.setAdapter(listArtistAdapter);
        return false;
    }
}
