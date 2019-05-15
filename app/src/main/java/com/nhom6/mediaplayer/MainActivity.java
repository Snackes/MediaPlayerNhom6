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
import com.nhom6.mediaplayer.model.Album;
import com.nhom6.mediaplayer.model.Artist;
import com.nhom6.mediaplayer.model.Song;
import com.nhom6.mediaplayer.databinding.ActivityMainBinding;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private SearchView searchView;
    Context context = this;
    ArrayList<Song> _songs = new ArrayList<Song>();
    ArrayList<Album> _albums=new ArrayList<Album>();
    ArrayList<Artist> _artists =new ArrayList<Artist>();
    Song SongPlaybar=new Song();

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

        binding = (ActivityMainBinding) DataBindingUtil.setContentView(this, R.layout.activity_main);
        //check có tức là đây chỉ gọi để refresh thằng playbar rồi thôi
        Intent intent = this.getIntent();
        if(intent.getSerializableExtra("song") !=null) {
            SongPlaybar = (Song) intent.getSerializableExtra("song");
            setSongPlayBar();
            return;
        }
        setSongPlayBar();
        //kiem tra permission
        CheckUserPermission(this);
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);

    }

    public void setSongPlayBar() {

        //nếu có tương tác thì xét bài đang tương tác cho playbar
        if(SongPlaybar.getSongname()!=null){
            binding.setSong(SongPlaybar);
        }
        //còn không thì xét mặc định cho nó
        else {
            MyDatabaseHelper db = new MyDatabaseHelper(context);
            _songs = db.GetListSong();
            if (_songs.size() != 0) {
                SongPlaybar = _songs.get(0);
                binding.setSong(SongPlaybar);
            } else {
                SongPlaybar.setSongname("default");
                SongPlaybar.setArtistname("default");
                binding.setSong(SongPlaybar);
            }
        }
    }

    public void ShowAllSong(View view) {
        Intent intent = new Intent(context, ShowAllSong.class);
        intent.putExtra("song", SongPlaybar);
        startActivity(intent);
    }

    public void ShowPlayList(View view) {
        Intent intent = new Intent(context, PlaylistActivity.class);
        intent.putExtra("song", SongPlaybar);
        startActivity(intent);
    }

    public void ShowAlbum(View view) {
        Intent intent = new Intent(context, AlbumActivity.class);
        intent.putExtra("song", SongPlaybar);
        startActivity(intent);
    }

    public void ShowPlayScreen(View view) {
        Intent i = new Intent(this, PlayActivity.class);
        startActivity(i);
    }

    public void ShowSinger(View view) {
        Intent intent = new Intent(context, SingerActivity.class);
        intent.putExtra("song", SongPlaybar);
        startActivity(intent);
    }

    public void ShowLoveSong(View view) {
        Intent intent = new Intent(context, LoveActivity.class);
        intent.putExtra("song", SongPlaybar);
        startActivity(intent);
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

            if(SongPlaybar.getAlbum()==null) {
                SongPlaybar = _songs.get(0);
                binding.setSong(SongPlaybar);
            }
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