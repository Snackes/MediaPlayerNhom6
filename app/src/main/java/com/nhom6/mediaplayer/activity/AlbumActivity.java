package com.nhom6.mediaplayer.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

import com.nhom6.mediaplayer.Database.MyDatabaseHelper;
import com.nhom6.mediaplayer.Manager.AlbumManager;
import com.nhom6.mediaplayer.R;
import com.nhom6.mediaplayer.adapter.AlbumAdapter;
import com.nhom6.mediaplayer.adapter.ListSongAdapter;
import com.nhom6.mediaplayer.model.Album;

import java.util.ArrayList;

public class AlbumActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private GridView gridViewAlbum;
    private SearchView searchView;
    final Context context=this;
    //khai báo SongManager để loadSong
    AlbumManager albumsManager = new AlbumManager();
    public ArrayList<Album> _albums = new ArrayList<Album>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title

        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_album);

        //find id GirdView
        gridViewAlbum =  findViewById(R.id.gridViewAB);

        int kt=getdata();
        if(kt==0){
            return;
        }

        //đưa vào adapter để hiển thị
        AlbumAdapter listAlbumAdapter = new AlbumAdapter(this,R.layout.girdview_album,_albums);
        gridViewAlbum.setAdapter(listAlbumAdapter);
        ClickItemAlbum();

        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);
    }

    public int getdata(){
        MyDatabaseHelper db=new MyDatabaseHelper(this);
        //Kiểm tra xem trong csdl bảng song đã có dữ liệu chưa?
        if(db.CheckTableAlbum()==0){
            Toast.makeText(getApplicationContext(), "Album trống, chọn Scan để quét album có trong máy..!", Toast.LENGTH_LONG).show();
            return 0;
        }
        else {
            _albums=db.GetListAlbums();
            return 1;
        }
    }

    //Xử lí khi click vào 1 album bất kì
    public void ClickItemAlbum(){
        gridViewAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //Xem danh sách bài hát khi chọn vào 1 album
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Album album1=_albums.get(position);
                Intent intent = new Intent(context, SongOfPlaylistActivity.class);
                intent.putExtra("Album", album1);
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
        _albums = db.SearchAlbum(text);
        AlbumAdapter listAlbumAdapter = new AlbumAdapter(this,R.layout.girdview_album,_albums);
        gridViewAlbum.setAdapter(listAlbumAdapter);
        return false;
    }
}
