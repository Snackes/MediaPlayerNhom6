package com.nhom6.mediaplayer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.Toast;

import com.nhom6.mediaplayer.Database.MyDatabaseHelper;
<<<<<<< HEAD
=======
import com.nhom6.mediaplayer.Manager.AlbumManager;
import com.nhom6.mediaplayer.Manager.ArtistManager;
import com.nhom6.mediaplayer.Manager.SongManager;
>>>>>>> 1973ab634604896e1e30c6f948d795648cfcf63e
import com.nhom6.mediaplayer.activity.AlbumActivity;
import com.nhom6.mediaplayer.activity.LoveActivity;
import com.nhom6.mediaplayer.activity.PlayActivity;
import com.nhom6.mediaplayer.activity.PlaylistActivity;
import com.nhom6.mediaplayer.activity.ShowAllSong;
import com.nhom6.mediaplayer.activity.SingerActivity;
import com.nhom6.mediaplayer.activity.SongOfPlaylistActivity;
import com.nhom6.mediaplayer.databinding.ActivityAllSongBinding;
import com.nhom6.mediaplayer.model.Album;
import com.nhom6.mediaplayer.model.Artist;
import com.nhom6.mediaplayer.model.Song;
import com.nhom6.mediaplayer.databinding.ActivityMainBinding;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public static final int PLAYSCREEN_RESULT = 1;
    private SearchView searchView;
    Context context = this;
    ArrayList<Song> _songs = new ArrayList<Song>();
<<<<<<< HEAD
    View activity;
=======
    ArrayList<Album> _albums=new ArrayList<Album>();
    ArrayList<Artist> _artists =new ArrayList<Artist>();
>>>>>>> 1973ab634604896e1e30c6f948d795648cfcf63e

    //dùng để binding
    ActivityMainBinding binding;

<<<<<<< HEAD

    //khai báo các thứ cần thiết để chơi nhạc
    private static Song song;
    private static Integer songID;
    // bundle nhận intent
    Bundle Package;
    // position của bài hát trong list mà intent gửi qua
    public Integer position;
    ArrayList<String> lstUrlSong = new ArrayList<String>();
    ArrayList<Integer> lstIDSong = new ArrayList<Integer>();
    //tạo object Database
    MyDatabaseHelper db = new MyDatabaseHelper(this);

    //các state
=======
>>>>>>> 1973ab634604896e1e30c6f948d795648cfcf63e
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_main);

        //set binding
        binding = (ActivityMainBinding) DataBindingUtil.setContentView(this, R.layout.activity_main);
<<<<<<< HEAD
        getDataIntent();
=======
>>>>>>> 1973ab634604896e1e30c6f948d795648cfcf63e
        setSongPlayBar();
        LayoutInflater inflaterDia = getLayoutInflater();
        //kiem tra permission
        CheckUserPermission(this);
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);


    }

<<<<<<< HEAD
    private void getDataIntent() {
        //nhận intent từ activity kia
        Intent i = getIntent();
        if (i != null) {
            //lấy bundle
            Package = i.getExtras();
            if (Package != null) {
                lstUrlSong = Package.getStringArrayList("lstUrlSong");
                lstIDSong = Package.getIntegerArrayList("lstIDSong");
                position = Package.getInt("position");

                //lấy id theo position
                songID = lstIDSong.get(position);

                //tạo object Song để chứa
                song = db.GetInfoSong(songID);
            }
        } else song = new Song();
        //
    }

    public void setSongPlayBar() {
        if (song!= null) {
            binding.setSong(song);
        } else {
            Song setSong=new Song();
            setSong.setSongname("default");
            setSong.setArtistname("default");
            binding.setSong(setSong);
=======
    public void setSongPlayBar() {
        MyDatabaseHelper db=new MyDatabaseHelper(context);
        _songs=db.GetListSong();
        Song song = new Song();
        if (_songs.size()!=0) {
            song=_songs.get(0);
            binding.setSong(song);
        } else {
            song.setSongname("default");
            song.setArtistname("default");
            binding.setSong(song);
>>>>>>> 1973ab634604896e1e30c6f948d795648cfcf63e
        }


    }

    public void ShowAllSong(View view) {
<<<<<<< HEAD
        Intent i = new Intent(this, ShowAllSong.class);
        startActivity(i);
=======
        Song song=new Song();
        if (_songs.size()!=0) {
            song=_songs.get(0);
        }

        Intent intent = new Intent(context, ShowAllSong.class);
        intent.putExtra("song", song);
        startActivity(intent);
>>>>>>> 1973ab634604896e1e30c6f948d795648cfcf63e
    }

    public void ShowPlayList(View view) {
        Intent i = new Intent(this, PlaylistActivity.class);
        startActivity(i);
    }

    public void ShowAlbum(View view) {
        Intent i = new Intent(this, AlbumActivity.class);
        startActivity(i);
    }

    public void ShowPlayScreen(View view) {
        Intent i = new Intent(this, PlayActivity.class);
        startActivity(i);
    }

    public void ShowSinger(View view) {
        Intent i = new Intent(this, SingerActivity.class);
        startActivity(i);
    }

    public void ShowLoveSong(View view) {
        Intent i = new Intent(this, LoveActivity.class);
        startActivity(i);
    }

    public void clickScan(View view) {
        Snackbar.make(view, "Đang quét", Snackbar.LENGTH_LONG)
                .setAction("No action", null).show();

        MyDatabaseHelper db = new MyDatabaseHelper(this);
        //tiến hành lấy toàn bộ song trong máy
        SongManager songManager=new SongManager();
        AlbumManager albumManager=new AlbumManager();
        ArtistManager artistManager=new ArtistManager();

        _songs = songManager.loadSong(this);
        _albums = albumManager.loadAlbum(this);
        _artists=artistManager.loadArtist(this);

        if(_songs.size()==0){
            Snackbar.make(view, "Không có bài hát trong máy", Snackbar.LENGTH_LONG)
                    .setAction("No action", null).show();
        }
        else {
            Snackbar.make(view, "Quét hoàn tất", Snackbar.LENGTH_LONG)
                    .setAction("No action", null).show();
            //đưa songs lấy được vào csdl
            db.addSong(_songs);
            db.addAlbum(_albums);
            db.addSinger(_artists);
            Song song=new Song();
            song=_songs.get(0);
            binding.setSong(song);
        }
    }

    private void CheckUserPermission(Context context) {
        //TODO: nếu chạy lần đầu thì sẽ vào đây xin permission
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 111);
                return;
            }
        }

        //TODO: nếu đã có permission rồi thì những lần sau sẽ chạy vào đây

        // do something
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 111:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission ok", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    CheckUserPermission(this);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Intent intent = new Intent(context, ShowAllSong.class);
        intent.putExtra("SearchInMain", query);
        startActivity(intent);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
<<<<<<< HEAD
        Intent intent = new Intent(context, ShowAllSong.class);
        intent.putExtra("SearchInMain", newText);
        startActivity(intent);
=======
>>>>>>> 1973ab634604896e1e30c6f948d795648cfcf63e
        return false;
    }
}
