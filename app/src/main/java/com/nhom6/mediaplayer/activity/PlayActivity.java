package com.nhom6.mediaplayer.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.nhom6.mediaplayer.Database.MyDatabaseHelper;
import com.nhom6.mediaplayer.MainActivity;
import com.nhom6.mediaplayer.Manager.PlayListManager;
import com.nhom6.mediaplayer.R;
import com.nhom6.mediaplayer.adapter.CustomPagerAdapter;
import com.nhom6.mediaplayer.adapter.PlaylistAdapter;
import com.nhom6.mediaplayer.fragment.ListPlayingSongFragment;
import com.nhom6.mediaplayer.fragment.SongPlayingFragment;
import com.nhom6.mediaplayer.model.PlayList;
import com.nhom6.mediaplayer.model.Song;
import com.nhom6.mediaplayer.service.MediaBrowserHelper;
import com.nhom6.mediaplayer.service.MediaSeekBar;
import com.nhom6.mediaplayer.service.MetaDataCompat;
import com.nhom6.mediaplayer.service.MusicService;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class PlayActivity extends AppCompatActivity implements SongPlayingFragment.OnFragmentInteractionListener, ListPlayingSongFragment.OnFragmentInteractionListener {

    final Context context = this;

    //khai báo ListView cho dialog
    private ListView listDialog;
    private Button buttonCreatePlaylist;


    //khai báo SongManager để loadSong
    PlayListManager playlistsManager = new PlayListManager();
    public ArrayList<PlayList> _playlists = new ArrayList<PlayList>();


    //khai báo các nút

    private ImageButton btn_add_love;
    private ImageButton btn_play_pause;
    private ImageButton btn_next;
    private ImageButton btn_previous;
    private ImageButton btn_repeat;
    private ImageButton btn_shuffle;
    public MediaSeekBar seekBar  ;
    //khai báo để search
    private static Integer test;
    private static Integer idObject;

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

    //format time
    private SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");

    //boolean shuffle
    private boolean isShuff;
    //boolean repeat
    private boolean isRepeat;
    // tạo adapter pager
    public static CustomPagerAdapter customPagerAdapter;
    //
    //tạo 2 Fragment
    public static SongPlayingFragment songPlayingFragment;
    public static ListPlayingSongFragment listSongPlayingFragment;
    //
    ViewPager viewPager;

    //các khai báo cho service
    private MediaBrowserHelper mMediaBrowserHelper;
    private final MetaDataCompat metaDataCompat = new MetaDataCompat();


    private boolean mIsPlaying;
    private boolean mIsRepeat = false;
    private boolean mIsShuffe = false;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_play);
        //init View
        initView();
        //

        //
        // lấy data từ Intent
        getDataIntent();

        //set bundle cho  fragment playing
        Bundle fragmentPlaying = new Bundle();
        fragmentPlaying.putString("Title", song.getSongname());
        fragmentPlaying.putString("Artist", song.getArtistname());
        fragmentPlaying.putString("Image", song.getAlbumArt());
        fragmentPlaying.putInt("SongID", song.getSongid());
        //set bundle cho fragment listplaying
        Bundle fragmentListPlaying = new Bundle();
        fragmentListPlaying.putIntegerArrayList("listID", lstIDSong);
        fragmentListPlaying.putInt("test", test);
        fragmentListPlaying.putInt("IDObject",idObject);

        //
        //init fragment
        songPlayingFragment = new SongPlayingFragment();
        //đưa bundle chứa thông tin qua fragment
        songPlayingFragment.setArguments(fragmentPlaying);
        //
        listSongPlayingFragment = new ListPlayingSongFragment();
        listSongPlayingFragment.setArguments(fragmentListPlaying);
        //


        // tạo view pager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        customPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager());
        customPagerAdapter.AddFragment(songPlayingFragment);
        customPagerAdapter.AddFragment(listSongPlayingFragment);
        viewPager.setAdapter(customPagerAdapter);


        mMediaBrowserHelper = new MediaBrowserConnection(this);
        mMediaBrowserHelper.registerCallback(new MediaBrowserListener());


    }

    public void ShowMainSreen(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void showPlayListDialog(View view) {
        final Dialog dialogAdd = new Dialog(context);
        dialogAdd.requestWindowFeature(Window.FEATURE_CONTEXT_MENU);
        //load playlist len dialog
        LayoutInflater inflaterDia = getLayoutInflater();
        View mView = inflaterDia.inflate(R.layout.dialog_addplaylist, null);
        listDialog = mView.findViewById(R.id.listDialogPL);
        //tiến hành lấy toàn bộ playlist trong máy
        _playlists = playlistsManager.loadPlayList(context);
        //đưa vào adapter để hiển thị
        PlaylistAdapter listPlayListAdapter = new PlaylistAdapter(this, _playlists);
        listDialog.setAdapter(listPlayListAdapter);

        listDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //lưu bài hát vào khi chọn playlist đã có
            public void onItemClick(AdapterView<?> parent, View view, int position1, long id) {
                MyDatabaseHelper db = new MyDatabaseHelper(context);
                db.addSongForPlayList(_playlists.get(position1).getIDPlayList(), songID);
                Toast.makeText(getApplicationContext(),
                        "Thêm bài hát vào PlayList thành công..!", Toast.LENGTH_SHORT).show();
                dialogAdd.cancel();
            }
        });
        dialogAdd.setContentView(mView);
        dialogAdd.setCancelable(true);
        Window window = dialogAdd.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(R.drawable.borderradius);
        dialogAdd.show();
        buttonCreatePlaylist = mView.findViewById(R.id.btnCreatePlayList);

        //
        CreatePlaylist(position, dialogAdd);

    }

    //tạo mới playlist đồng thời thêm bài hát đã chọn vào playlist vừa tạo
    public void CreatePlaylist(final int position, final Dialog dialogAdd) {
        buttonCreatePlaylist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialogAdd.hide();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);
                // set title
                alertDialogBuilder.setTitle("Create PlayList");
                // Get the layout inflater
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                final LayoutInflater inflatercreate = getLayoutInflater();
                final View moduleView = inflatercreate.inflate(R.layout.create_playlist, null);
                alertDialogBuilder.setView(moduleView);
                final EditText edtPlayListName = moduleView.findViewById(R.id.namePlayList);
                // Add action buttons
                alertDialogBuilder.setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {


                    @SuppressLint("WrongConstant")
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String title = edtPlayListName.getText().toString();
                        if (title.equals("")) {
                            Toast.makeText(getApplicationContext(),
                                    "Vui lòng nhập tên trước khi tạo Playlist..!", 50).show();
                            return;
                        }
                        playlistsManager.CreatePlayListAndAddSong(title, context, songID);
                        Toast.makeText(getApplicationContext(),
                                "Đã thêm vào Playlist..!", 50).show();
                    }
                })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        });
    }

    public void clickLove(View view) {
        MyDatabaseHelper db = new MyDatabaseHelper(context);
        int k = db.CheckSongFavorite(songID);
        if (k == 0) {
            db.AddSongFavorite(songID);
            Snackbar.make(view, "Đã đưa vào mục yêu thích", Snackbar.LENGTH_LONG)
                    .setAction("No action", null).show();


            songPlayingFragment.ChangeIcon();
        } else {
            db = new MyDatabaseHelper(context);
            db.deleteSongInFavorite(songID);
            Snackbar.make(view, "Đã xóa khỏi mục yêu thích", Snackbar.LENGTH_LONG)
                    .setAction("No action", null).show();
            songPlayingFragment.ChangeIcon();

        }

    }



    @Override
    protected void onStart() {
        super.onStart();
        mMediaBrowserHelper.onStart();
        //
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        songPlayingFragment.getSeekBar().disconnectController();
        mMediaBrowserHelper.onStop();
        Toast.makeText(this, "hahaha", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mSeekBarAudio.disconnectController();


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void getDataIntent() {
        //nhận intent từ activity kia
        Intent i = getIntent();
        //lấy bundle

        Package = i.getExtras();
        //lstUrlSong = Package.getStringArrayList("lstUrlSong");
        lstIDSong = Package.getIntegerArrayList("lstIDSong");
        test = Package.getInt("test");
        idObject = Package.getInt("IDObject");


        position = 0;
        position = Package.getInt("position");

        //lấy id theo position
        songID = lstIDSong.get(position);

        //tạo object Song để chứa
        song = db.GetInfoSong(songID);
        SaveData.setSongId(songID);

    }

    private void initView() {



        final ClickListener clickListener = new ClickListener();
        findViewById(R.id.btnPre).setOnClickListener(clickListener);
        findViewById(R.id.btnPlayMenu).setOnClickListener(clickListener);
        findViewById(R.id.btnNext).setOnClickListener(clickListener);
        findViewById(R.id.btnShuf).setOnClickListener(clickListener);
        findViewById(R.id.btnRepeat).setOnClickListener(clickListener);

        //

        //

        btn_add_love = (ImageButton) findViewById(R.id.btnLove);

        btn_play_pause = (ImageButton) findViewById(R.id.btnPlayMenu);
        btn_next = (ImageButton) findViewById(R.id.btnNext);
        btn_previous = (ImageButton) findViewById(R.id.btnPre);
        btn_repeat = (ImageButton) findViewById(R.id.btnRepeat);
        btn_shuffle = (ImageButton) findViewById(R.id.btnShuf);

    }

    /**
     * Convenience class to collect the click listeners together.
     * <p>
     * In a larger app it's better to split the listeners out or to use your favorite
     * library.
     */
    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnPre:
                    mMediaBrowserHelper.getTransportControls().skipToPrevious();
                    break;
                case R.id.btnPlayMenu:
                    if (mIsPlaying) {
                        mMediaBrowserHelper.getTransportControls().pause();
                    } else {
                        mMediaBrowserHelper.getTransportControls().play();
                    }
                    break;
                case R.id.btnNext:
                    mMediaBrowserHelper.getTransportControls().skipToNext();
                    break;
                case R.id.btnRepeat:
                    if(isRepeat == true)
                    {
                        mMediaBrowserHelper.getTransportControls().setRepeatMode(PlaybackStateCompat.REPEAT_MODE_NONE);
                        isRepeat = false;
                        btn_repeat.setColorFilter(ContextCompat.getColor(context,R.color.pinkwhite));
                        Toast.makeText(context, "repeatMode off", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        mMediaBrowserHelper.getTransportControls().setRepeatMode(PlaybackStateCompat.REPEAT_MODE_ONE);
                        isRepeat = true;

                        btn_repeat.setColorFilter(ContextCompat.getColor(context,R.color.pinkic));
                        Toast.makeText(context, "repeatMode on", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case R.id.btnShuf:
                    if(isShuff == true)
                    {
                        mMediaBrowserHelper.getTransportControls().setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_NONE);
                        isShuff = false;
                        Toast.makeText(context, "shuffMode off", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        mMediaBrowserHelper.getTransportControls().setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_ALL);
                        isShuff = true;
                        Toast.makeText(context, "shuffMode on", Toast.LENGTH_SHORT).show();
                    }


                    break;


            }
        }

    }
    /**
     * Customize the connection to our {@link android.support.v4.media.MediaBrowserServiceCompat}
     * and implement our app specific desires.
     */
    private class MediaBrowserConnection extends MediaBrowserHelper {
        private MediaBrowserConnection(Context context) {
            super(context, MusicService.class);
        }



        @Override
        protected void onDisconnected() {
            super.onDisconnected();
        }

        @Override

        protected void onConnected(@NonNull MediaControllerCompat mediaController) {
            songPlayingFragment.getSeekBar().setMediaController(mediaController);

            songPlayingFragment.getSeekBar().setCurrentTime(songPlayingFragment.getPlayingTime());
            songPlayingFragment.getSeekBar().setDurationTime(songPlayingFragment.getTotalTime());

//            songPlayingFragment.setPlayingTime();



        }

        @Override
        protected void onClearQueue(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children) {
            super.onClearQueue(parentId, children);
            final MediaControllerCompat mediaController = getMediaController();
            // Queue up all media items for this simple sample.
            for (final MediaBrowserCompat.MediaItem mediaItem : children) {
                mediaController.removeQueueItem(mediaItem.getDescription());

            }
        }
        @Override
        protected void onChildrenLoaded(@NonNull String parentId,
                                        @NonNull List<MediaBrowserCompat.MediaItem> children) {

            final MediaControllerCompat mediaController = getMediaController();

            super.onChildrenLoaded(parentId, children);
            // Queue up all media items for this simple sample.
            for (final MediaBrowserCompat.MediaItem mediaItem : children) {
                mediaController.addQueueItem(mediaItem.getDescription());
            }
            //List<MediaBrowserCompat.MediaItem> children11=new ArrayList<>();


            SaveData.setFlag(true);
            SaveData.setCountList(children.size());

            // Call prepare now so pressing play just works.
            mediaController.getTransportControls().prepareFromMediaId(String.valueOf(position)  ,null);
            // playsong
            mediaController.getTransportControls().play();

        }
    }

    /**
     * Implementation of the {@link MediaControllerCompat.Callback} methods we're interested in.
     * <p>
     * Here would also be where one could override
     * {@code onQueueChanged(List<MediaSessionCompat.QueueItem> queue)} to get informed when items
     * are added or removed from the queue. We don't do this here in order to keep the UI
     * simple.
     */
    private class MediaBrowserListener extends MediaControllerCompat.Callback {
        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat playbackState) {
            mIsPlaying = playbackState != null && playbackState.getState() == PlaybackStateCompat.STATE_PLAYING;

            if(mIsPlaying == true)
            {
                btn_play_pause.setImageResource(R.drawable.ic_pause_new);
            }
            else{
                btn_play_pause.setImageResource(R.drawable.ic_play_new);
            }
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat mediaMetadata) {
            if (mediaMetadata == null) {

                return;
            }
            songPlayingFragment.ChangeTitleSong(
                    mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE));
            songPlayingFragment.ChangeArtistSong(
                    mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ARTIST));
            songPlayingFragment.ChangeImg(MetaDataCompat.getAlbumBitmap(
                    context,
                    mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)));
        }


        @Override
        public void onSessionDestroyed() {
            super.onSessionDestroyed();

        }


        @Override
        public void onQueueChanged(List<MediaSessionCompat.QueueItem> queue) {

            super.onQueueChanged(queue);
        }
    }

}

