package com.nhom6.mediaplayer.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nhom6.mediaplayer.Database.MyDatabaseHelper;
import com.nhom6.mediaplayer.MainActivity;
import com.nhom6.mediaplayer.R;
import com.nhom6.mediaplayer.model.Song;
import com.nhom6.mediaplayer.service.BackgroundAudioService;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class PlayActivity extends AppCompatActivity {


    public ImageButton btn_play;
    public CircleImageView img;
    public  TextView nameSong ;
    public TextView nameArtist;

    //
    public Song song;
//    public String Songname;
//    public String Artistname;
//    public String Album;
//    public String SongUrl;
//    public String AlbumArt;
//    public Integer ArtistnameId;
//    public Integer AlbumId;
//    public Integer Duration;
    public Integer Songid;

    //
    Bundle Package;
    public Integer position;
    ArrayList<String> lstUrlSong = new ArrayList<String>();
    ArrayList<Integer> lstIDSong = new ArrayList<Integer>();
    //
    //tạo object Database
    MyDatabaseHelper db = new MyDatabaseHelper(this);
    //

    private static final int STATE_PAUSED = 0;
    private static final int STATE_PLAYING = 1;
    private static final int STATE_SHUFFLE = 2;
    private static final int STATE_REPEAT = 3;
    private int currentState;
    private MediaBrowserCompat mediaBrowserCompat;
    private MediaControllerCompat mediaControllerCompat;






    public static final int MY_REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_play);

    }



}
