package com.nhom6.mediaplayer.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.nhom6.mediaplayer.Database.MyDatabaseHelper;
import com.nhom6.mediaplayer.R;
import com.nhom6.mediaplayer.adapter.CustomPagerAdapter;
import com.nhom6.mediaplayer.fragment.ListPlayingSongFragment;
import com.nhom6.mediaplayer.fragment.SongPlayingFragment;
import com.nhom6.mediaplayer.model.Song;
import com.nhom6.mediaplayer.service.BackgroundAudioService;

import java.util.ArrayList;


public class PlayActivity extends AppCompatActivity implements SongPlayingFragment.OnFragmentInteractionListener, ListPlayingSongFragment.OnFragmentInteractionListener {


    //khai báo các nút
    ImageButton btn_play_pause;
    ImageButton btn_next;
    ImageButton btn_previous;
    ImageButton btn_repeat;
    ImageButton btn_shuffle;

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
    private static final int STATE_PAUSED = 0;
    private static final int STATE_PLAYING = 1;
    private int currentState;
    private MediaBrowserCompat mediaBrowserCompat;
    private MediaControllerCompat mediaControllerCompat;



    // tạo adapter pager
    public static CustomPagerAdapter customPagerAdapter;
    //
    //tạo 2 Fragment
    public static SongPlayingFragment songPlayingFragment;
    public static ListPlayingSongFragment listSongPlayingFragment;
    //
    ViewPager viewPager;







    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_play);


        //init View
        initView();

        // lấy data từ Intent
        getDataIntent();

        //set view cho các fragment
        Bundle viewFragmentPlaying = new Bundle();
        viewFragmentPlaying.putString("Title",song.getSongname());
        viewFragmentPlaying.putString("Artist",song.getArtistname());
        viewFragmentPlaying.putString("Image",song.getAlbumArt());


        // tạo view pager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //init fragment
        songPlayingFragment = new SongPlayingFragment();
        //đưa bundle chứa thông tin qua fragment
        songPlayingFragment.setArguments(viewFragmentPlaying);
        //

        listSongPlayingFragment = new ListPlayingSongFragment();
        customPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager());
        customPagerAdapter.AddFragment(songPlayingFragment);
        customPagerAdapter.AddFragment(listSongPlayingFragment);
        viewPager.setAdapter(customPagerAdapter);

        //

        mediaBrowserCompat = new MediaBrowserCompat(this, new ComponentName(this, BackgroundAudioService.class),
                connectionCallback, getIntent().getExtras());
        mediaBrowserCompat.connect();

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
        //đổi hình theo
        if ( position < lstIDSong.size() -1 ) // check if next song is there or not
        {
            int idNext = lstIDSong.get( position + 1);
            song = db.GetInfoSong(idNext);
            position = position +1;
            //Screen(song);
        }
        else
        {
            int idNext = lstIDSong.get(0);
            song = db.GetInfoSong(idNext);
            position = 0;
            //Screen(song);
        }


        //mỗi lần next Song sẽ tiến hành pause =>> start lại

        //pause
        mediaControllerCompat.getTransportControls().pause();
        currentState = STATE_PAUSED;
        //next

        if(mediaControllerCompat.getPlaybackState().getState() == PlaybackStateCompat.STATE_PAUSED)
        {
            mediaControllerCompat.getTransportControls().skipToNext();
            currentState = STATE_PAUSED;


        }else
        {
            if(mediaControllerCompat.getPlaybackState().getState() == PlaybackStateCompat.STATE_PLAYING)
            {
                mediaControllerCompat.getTransportControls().skipToNext();
                currentState = STATE_PLAYING;
            }
        }


    }
    public void PreviousSong(View view)
    {
        //đổi hình theo
        if ( position > 0 ) // check if next song is there or not
        {
            int idNext = lstIDSong.get( position -1 );
            song = db.GetInfoSong(idNext);
            position = position - 1;
            //Screen(song);
        }
        else
        {
            int idNext = lstIDSong.get(lstIDSong.size() -1);
            song = db.GetInfoSong(idNext);
            position = lstIDSong.size() -1 ;
            //Screen(song);
        }


        //mỗi lần next Song sẽ tiến hành pause =>> start lại

        //pause
        mediaControllerCompat.getTransportControls().pause();
        currentState = STATE_PAUSED;
        //next

        if(mediaControllerCompat.getPlaybackState().getState() == PlaybackStateCompat.STATE_PAUSED)
        {
            mediaControllerCompat.getTransportControls().skipToPrevious();
            currentState = STATE_PAUSED;


        }else
        {
            if(mediaControllerCompat.getPlaybackState().getState() == PlaybackStateCompat.STATE_PLAYING)
            {
                mediaControllerCompat.getTransportControls().skipToPrevious();
                currentState = STATE_PLAYING;
            }
        }


    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    private void getDataIntent()
    {
        //nhận intent từ activity kia
        Intent i = getIntent();
        //lấy bundle
        Package = i.getExtras();
        lstUrlSong = Package.getStringArrayList("lstUrlSong");
        lstIDSong = Package.getIntegerArrayList("lstIDSong");
        position = Package.getInt("position");

        //lấy id theo position
        songID = lstIDSong.get(position);

        //tạo object Song để chứa
        song = db.GetInfoSong(songID);
        //
    }
    private void initView()
    {
        btn_play_pause = (ImageButton) findViewById(R.id.btnPlayMenu);
        btn_next = (ImageButton) findViewById(R.id.btnNext);
        btn_previous = (ImageButton) findViewById(R.id.btnPre);
        btn_repeat = (ImageButton) findViewById(R.id.btnRepeat);
        btn_shuffle =  (ImageButton) findViewById(R.id.btnShuf);
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
                mediaControllerCompat.getTransportControls().playFromMediaId("chokhoinull" ,Package);
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
                    btn_play_pause.setImageResource(R.drawable.ic_pause_new);
                    break;
                }
                case PlaybackStateCompat.STATE_PAUSED: {
                    currentState = STATE_PAUSED;
                    btn_play_pause.setImageResource(R.drawable.ic_play_new);
                    break;
                }
            }
        }
    };

}
