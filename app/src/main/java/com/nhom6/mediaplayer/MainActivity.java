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
import com.nhom6.mediaplayer.Manager.AlbumManager;
import com.nhom6.mediaplayer.Manager.ArtistManager;
import com.nhom6.mediaplayer.Manager.SongManager;
import com.nhom6.mediaplayer.activity.AlbumActivity;
import com.nhom6.mediaplayer.activity.LoveActivity;
import com.nhom6.mediaplayer.activity.PlayActivity;
import com.nhom6.mediaplayer.activity.PlaylistActivity;
import com.nhom6.mediaplayer.activity.ShowAllSong;
import com.nhom6.mediaplayer.activity.SingerActivity;
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
    ArrayList<Album> _albums = new ArrayList<Album>();
    ArrayList<Artist> _artists = new ArrayList<Artist>();
    View activity;

    //dùng để binding
    ActivityMainBinding binding;

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
        setSongPlayBar();
        LayoutInflater inflaterDia = getLayoutInflater();
        activity = inflaterDia.inflate(R.layout.activity_all_song, null);
        //kiem tra permission
        CheckUserPermission(this);
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);

    }

    public void setSongPlayBar() {
        Song setSong = new Song();
        if (setSong.getSongname() != null) {
            binding.setSong(setSong);
        } else {
            setSong.setSongname("default");
            setSong.setArtistname("default");
            binding.setSong(setSong);
        }

    }

    public void ShowAllSong(View view) {
        Intent i = new Intent(this, ShowAllSong.class);
        startActivity(i);
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
        return false;
    }
}
