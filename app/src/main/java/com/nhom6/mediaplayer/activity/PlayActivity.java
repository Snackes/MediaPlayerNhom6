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

import com.nhom6.mediaplayer.MainActivity;
import com.nhom6.mediaplayer.R;
import com.nhom6.mediaplayer.service.BackgroundAudioService;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class PlayActivity extends AppCompatActivity {

    public ImageButton btn_play;
    public CircleImageView img;
    public  TextView nameSong ;
    public TextView nameArtist;

    //
    public String Songname;
    public String Artistname;
    public String Album;
    public String SongUrl;
    public String AlbumArt;
    public Integer ArtistnameId;
    public Integer AlbumId;
    public Integer Duration;
    public Integer Songid;
    public Integer posotion;
    //
    Bundle Package;
    ArrayList<String> lstUrlSong = new ArrayList<String>();
    //

    private static final int STATE_PAUSED = 0;
    private static final int STATE_PLAYING = 1;
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

        //
        img = (CircleImageView) findViewById(R.id.imgMusic);
        nameSong = (TextView) findViewById(R.id.songTitle);
        nameArtist = (TextView) findViewById(R.id.artist);
        btn_play = (ImageButton) findViewById(R.id.btnPlayMenu);
        //

        //nhận intent từ activity kia
        Intent i = getIntent();
        //lấy bundle
        Package = i.getExtras();

        posotion = Package.getInt("position");
        SongUrl = Package.getString("SongUrl");
        AlbumArt = Package.getString("AlbumArt");
        Songname = Package.getString("SongName");
        Artistname = Package.getString("ArtistName");
        lstUrlSong = Package.getStringArrayList("lstUrlSong");




        //

        //decode Bitmap
        Bitmap bm = BitmapFactory.decodeFile(AlbumArt);
        // nếu bitmap null == không có hình ta sẽ thay bằng hình mặc định
        if(bm != null)
        {
            img.setImageBitmap(bm);
        }
        else
        {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.adele);
            img.setImageBitmap(bitmap);
        }
        nameSong.setText(Songname);
        nameArtist.setText(Artistname);




        //

        mediaBrowserCompat = new MediaBrowserCompat(this, new ComponentName(this, BackgroundAudioService.class),
                connectionCallback, getIntent().getExtras());
        mediaBrowserCompat.connect();
        ///











    }


    public void PlaySong(View view)
    {

            if(currentState == STATE_PAUSED)
                {
                    mediaControllerCompat.getTransportControls().play();
                    currentState = STATE_PLAYING;
                }else
                {
                    if(mediaControllerCompat.getPlaybackState().getState() == PlaybackStateCompat.STATE_PLAYING)
                    {
                        mediaControllerCompat.getTransportControls().pause();
                    }
                    currentState = STATE_PAUSED;
                }
    }
    public void NextSong(View view)
    {
        if(currentState == STATE_PAUSED)
        {
            mediaControllerCompat.getTransportControls().skipToNext();
            currentState = STATE_PLAYING;
        }else
        {
            if(mediaControllerCompat.getPlaybackState().getState() == PlaybackStateCompat.STATE_PLAYING)
            {
                mediaControllerCompat.getTransportControls().skipToNext();
            }
            currentState = STATE_PAUSED;
        }

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

//        btn_play.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(currentState == STATE_PAUSED)
//                {
//                    mediaControllerCompat.getTransportControls().play();
//                    currentState = STATE_PLAYING;
//                }else
//                {
//                    if(mediaControllerCompat.getPlaybackState().getState() == PlaybackStateCompat.STATE_PLAYING)
//                    {
//                        mediaControllerCompat.getTransportControls().pause();
//                    }
//                    currentState = STATE_PAUSED;
//                }
//            }
//        });
    }

    public void showMainSreen(View view) {
        try {

            Intent intent = new Intent(this, MainActivity.class); //Call GreetingActivity
            this.startActivityForResult(intent, MY_REQUEST_CODE);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    private MediaBrowserCompat.ConnectionCallback connectionCallback = new MediaBrowserCompat.ConnectionCallback()
    {
        @Override
        public void onConnected() {
            super.onConnected();
            try {
                mediaControllerCompat = new MediaControllerCompat(PlayActivity.this, mediaBrowserCompat.getSessionToken());
                mediaControllerCompat.registerCallback(mediaControllerCompatCallback);

                mediaControllerCompat.setMediaController(PlayActivity.this, mediaControllerCompat);

                mediaControllerCompat.getMediaController(PlayActivity.this);
                mediaControllerCompat.getTransportControls().playFromMediaId(String.valueOf(SongUrl) ,Package);







            } catch( RemoteException e ) {
                e.printStackTrace();
            }
        }
    };
    private MediaControllerCompat.Callback mediaControllerCompatCallback = new MediaControllerCompat.Callback() {

        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            super.onPlaybackStateChanged(state);
            if( state == null ) {
                return;
            }

            switch( state.getState() ) {
                case PlaybackStateCompat.STATE_PLAYING: {
                    currentState = STATE_PLAYING;
                    btn_play.setImageResource(R.drawable.ic_pause_new);
                    break;
                }
                case PlaybackStateCompat.STATE_PAUSED: {
                    currentState = STATE_PAUSED;
                    btn_play.setImageResource(R.drawable.ic_play_new);
                    break;
                }
            }
        }
    };







}
