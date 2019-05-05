package com.nhom6.mediaplayer.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;

import com.nhom6.mediaplayer.Database.MyDatabaseHelper;
import com.nhom6.mediaplayer.Manager.AlbumManager;
import com.nhom6.mediaplayer.R;
import com.nhom6.mediaplayer.adapter.AlbumAdapter;
import com.nhom6.mediaplayer.model.Album;

import java.util.ArrayList;

public class AlbumActivity extends AppCompatActivity {
    GridView gridView;
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
        gridView =  findViewById(R.id.gridViewAB);

        MyDatabaseHelper db=new MyDatabaseHelper(this);
        //Kiểm tra xem trong csdl bảng song đã có dữ liệu chưa?
        if(db.CheckTableAlbum()==0){
            //tiến hành lấy toàn bộ album trong máy
            _albums = albumsManager.loadAlbum(this);
            //đưa songs lấy được vào csdl
            db.addAlbum(_albums);
        }
        else {
            _albums=db.GetListAlbums();
        }

        //tiến hành lấy toàn bộ song trong máy

        //đưa vào adapter để hiển thị
        AlbumAdapter listAlbumAdapter = new AlbumAdapter(this,R.layout.girdview_album,_albums);
        gridView.setAdapter(listAlbumAdapter);
    }
}
