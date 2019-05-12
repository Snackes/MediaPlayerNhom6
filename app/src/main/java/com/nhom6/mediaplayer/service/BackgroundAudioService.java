package com.nhom6.mediaplayer.service;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;

import com.nhom6.mediaplayer.Database.MyDatabaseHelper;
import com.nhom6.mediaplayer.R;
import com.nhom6.mediaplayer.model.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BackgroundAudioService extends MediaBrowserServiceCompat implements MediaPlayer.OnCompletionListener, AudioManager.OnAudioFocusChangeListener {


    ////////////////////////
    // list danh sách Url


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
    //
    public int returnPosition;
    //
    public boolean isShuffle;
    public boolean isRepeat;

    //
    public Random rand;

    public ArrayList<String> lstUrl = new ArrayList<String>();
    //list danh sách ID
    public ArrayList<Integer> lstID = new ArrayList<Integer>();
    //vị trí của position truyền vào
    public Integer position;
    // gọi database để load bài hát theo id
    MyDatabaseHelper db = new MyDatabaseHelper(this);

    public Song song;
    ////////////////////////
    //

    //tạo mediplayer
    private MediaPlayer mediaPlayer;
    private MediaSessionCompat mediaSessionCompat;

    ////////////////////////



    private BroadcastReceiver headPhoneReceiver = new BroadcastReceiver() {
        //nếu như đang phát nhạc mà cắm tai nghe vào thì nó pause
        @Override
        public void onReceive(Context context, Intent intent) {
            if( mediaPlayer != null && mediaPlayer.isPlaying() ) {
                mediaPlayer.pause();
            }
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();

        initMediaPlayer();
        initMediaSession();
        initNoisyReceiver();
    }


    private void initMediaPlayer()
    {
        if(mediaPlayer != null)
        {
            mediaPlayer.release();
            mediaPlayer = null;
            mediaSessionCompat.release();
        }


        mediaPlayer =  new MediaPlayer();
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
    }
    private void initMediaSession()
    {
        ComponentName mediaButtonReceiver = new ComponentName(getApplicationContext(), MediaButtonReceiver.class);
        mediaSessionCompat = new MediaSessionCompat(getApplicationContext(),"Tag", mediaButtonReceiver, null);

        mediaSessionCompat.setCallback(mediaSessionCallBack);
        mediaSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS );

        Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        mediaButtonIntent.setClass(this,MediaButtonReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,mediaButtonIntent,0);
        mediaSessionCompat.setMediaButtonReceiver(pendingIntent);

        setSessionToken(mediaSessionCompat.getSessionToken());
    }

    private void initNoisyReceiver()
    {
        IntentFilter filter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(headPhoneReceiver, filter);
    }

    private MediaSessionCompat.Callback mediaSessionCallBack = new MediaSessionCompat.Callback() {

        //play prev song
        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();

            // kiểm tra vị trí có đang ở đầu danh sách hay ko
            if ( position > 0 )
            {
                //tạo song mới để cập nhật

                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(lstUrl.get(position - 1));
                    mediaPlayer.prepareAsync();
                    mediaPlayer.start();
                    position = position  - 1 ;
                    song = db.GetInfoSong(lstID.get(position));
                    //return position to activity
                    returnPosition=position;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //nếu đang ở đầu danh sách, play bài cuối cùng của danh sách
            else // play last song
            {
                mediaPlayer.reset();
                try {
                    mediaPlayer.reset();

                    mediaPlayer.setDataSource(lstUrl.get(lstUrl.size() - 1));
                    mediaPlayer.prepareAsync();
                    mediaPlayer.start();
                    position = lstUrl.size() - 1 ;
                    //tạo song mới để cập nhật
                    song = db.GetInfoSong(lstID.get(position));
                    //return position to activity
                    returnPosition = position;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            initMediaSessionMetadata();
            showPauseNotification();

        }

        //play next song
        @Override
        public void onSkipToNext() {
            super.onSkipToNext();

            //kiểm tra repeat một bài hát
            if (isRepeat)
            {
                //lặp lại position
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(lstUrl.get(position));
                    mediaPlayer.prepareAsync();
                    mediaPlayer.start();
                    song = db.GetInfoSong(lstID.get(position));
                    //return position to activity
                    returnPosition = position;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                if (isShuffle)
                {
                    //random ngẫu nhiên vị trí bài hát trong list
                    int newSong = position;
                    while(newSong==position){
                        newSong=rand.nextInt(lstUrl.size());
                    }
                    position=newSong;

                    //tạo song mới để cập nhật

                    try {
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(lstUrl.get(position));
                        mediaPlayer.prepareAsync();
                        mediaPlayer.start();
                        song = db.GetInfoSong(lstID.get(position));
                        //return position to activity
                        returnPosition = position;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    // kiểm tra vị trí đã nằm cuối danh sách hay chưa
                    if (position < lstID.size() - 1)
                    {
                        //tạo song mới để cập nhật

                        try {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(lstUrl.get(position + 1));
                            mediaPlayer.prepareAsync();
                            mediaPlayer.start();
                            position = position + 1;
                            song = db.GetInfoSong(lstID.get(position));
                            //return position to activity
                            returnPosition = position;

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    //nếu nằm cuối, play bài đầu tiên trong danh sách
                    else // play first song
                    {
                        mediaPlayer.reset();
                        try {
                            mediaPlayer.reset();

                            mediaPlayer.setDataSource(lstUrl.get(0));
                            mediaPlayer.prepareAsync();
                            mediaPlayer.start();
                            position = 0;
                            //tạo song mới để cập nhật
                            song = db.GetInfoSong(lstID.get(position));
                            //return position to activity
                            returnPosition = position;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            initMediaSessionMetadata();
            showPauseNotification();

        }

        //play song after prepare
        @Override
        public void onPlay() {
            super.onPlay();
            if( !successfullyRetrievedAudioFocus() ) {
                return;
            }

            mediaSessionCompat.setActive(true);
            setMediaPlayBackState(PlaybackStateCompat.STATE_PLAYING);

            showPlayingNotification();
            mediaPlayer.start();
        }

        //pause song
        @Override
        public void onPause() {
            super.onPause();
            if( mediaPlayer.isPlaying() ) {
                mediaPlayer.pause();
                setMediaPlayBackState(PlaybackStateCompat.STATE_PAUSED);
                showPauseNotification();
            }
        }

        //prepare song for play
        @Override
        public void onPlayFromMediaId(String mediaId, Bundle Package) {
            super.onPlayFromMediaId(mediaId, Package);

            lstUrl = Package.getStringArrayList("lstUrlSong");
            lstID = Package.getIntegerArrayList("lstIDSong");
            position = Package.getInt("position");

            // lấy song theo id
            song = db.GetInfoSong(lstID.get(position));

            try {

                try {
                    mediaPlayer.setDataSource(lstUrl.get(position));

                } catch (IllegalStateException e) {
                    mediaPlayer.release();
                    initMediaPlayer();
                    mediaPlayer.setDataSource(lstUrl.get(position));
                }

                initMediaSessionMetadata();

            } catch (IOException e) {
                return;
            }

            try {
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //repeat mode
        @Override
        public void onSetRepeatMode(int repeatMode) {
            super.onSetRepeatMode(repeatMode);
            if (repeatMode==PlaybackStateCompat.REPEAT_MODE_ONE)
            {
                isRepeat= true;
            }
            else {
                isRepeat = false;
            }
        }

        //shuffle mode
        @Override
        public void onSetShuffleMode(int shuffleMode) {
            super.onSetShuffleMode(shuffleMode);
            if (shuffleMode==PlaybackStateCompat.SHUFFLE_MODE_ALL)
            {
                isShuffle = true;
            }
            else {
                isShuffle = false;
            }
        }

        //seek song to
        @Override
        public void onSeekTo(long pos) {
            super.onSeekTo(pos);
            mediaPlayer.seekTo((int)pos);
        }
    };

    //trả về vị trí bài đang phát
    public int returnPosition()
    {
        return returnPosition;
    }

    //lấy thời gian đang chạy
    public int getCurrentPositionSong()
    {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.abandonAudioFocus(this);
        unregisterReceiver(headPhoneReceiver);
        mediaSessionCompat.release();
        NotificationManagerCompat.from(this).cancel(1);
    }


    //notification các action

    private void showPlayingNotification()
    {
        NotificationCompat.Builder builder = MediaStyleHelper.from(BackgroundAudioService.this, mediaSessionCompat);
        if(builder == null)
        {
            return;
        }


        builder.addAction(new NotificationCompat.Action(R.drawable.ic_pause_new,"Pause", MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_PLAY_PAUSE)));
        builder.setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView().setMediaSession(mediaSessionCompat.getSessionToken()));
        builder.setSmallIcon(R.mipmap.ic_launcher);
        NotificationManagerCompat.from(BackgroundAudioService.this).notify(1, builder.build());
    }
    private void showPauseNotification()
    {
        NotificationCompat.Builder builder = MediaStyleHelper.from(BackgroundAudioService.this, mediaSessionCompat);
        if(builder == null)
        {
            return;
        }


        builder.addAction(new NotificationCompat.Action(R.drawable.ic_play_new,"Pause", MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_PLAY_PAUSE)));
        builder.setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView().setMediaSession(mediaSessionCompat.getSessionToken()));
        builder.setSmallIcon(R.mipmap.ic_launcher);
        NotificationManagerCompat.from(BackgroundAudioService.this).notify(1, builder.build());
    }
    private void setMediaPlayBackState(int state)
    {
        PlaybackStateCompat.Builder playbackstateBuilder = new PlaybackStateCompat.Builder();
        if (state == PlaybackStateCompat.STATE_PLAYING)
        {
            playbackstateBuilder.setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_PAUSE);
        }
        else
        {
            playbackstateBuilder.setActions(PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_PLAY);
        }
        playbackstateBuilder.setState(state, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN,0);
        mediaSessionCompat.setPlaybackState(playbackstateBuilder.build());



    }

    private void initMediaSessionMetadata()
    {
        //chỗ này là notification ở màn hình khóa
        MediaMetadataCompat.Builder metadataBuilder = new MediaMetadataCompat.Builder();
        //Notification icon in card
        metadataBuilder.putBitmap(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON,Screen(song));
        metadataBuilder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART,Screen(song));
        //
        metadataBuilder.putBitmap(MediaMetadataCompat.METADATA_KEY_ART,Screen(song));
        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, song.getSongname());
        metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, song.getArtistname());
        metadataBuilder.putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, 1);
        metadataBuilder.putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, 1);


        mediaSessionCompat.setMetadata(metadataBuilder.build());


    }
    private boolean successfullyRetrievedAudioFocus() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        int result = audioManager.requestAudioFocus(this,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        return result == AudioManager.AUDIOFOCUS_GAIN;
    }



    @Override
    public void onAudioFocusChange(int focusChange) {
        switch( focusChange ) {
            case AudioManager.AUDIOFOCUS_LOSS: {
                if( mediaPlayer.isPlaying() ) {
                    mediaPlayer.stop();
                }
                break;
            }
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT: {
                mediaPlayer.pause();
                break;
            }
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK: {
                if( mediaPlayer != null ) {
                    mediaPlayer.setVolume(0.3f, 0.3f);
                }
                break;
            }
            case AudioManager.AUDIOFOCUS_GAIN: {
                if( mediaPlayer != null ) {
                    if( !mediaPlayer.isPlaying() ) {
                        mediaPlayer.start();
                    }
                    mediaPlayer.setVolume(1.0f, 1.0f);
                }
                break;
            }
        }

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        /*if( this.mediaPlayer != null ) {
            this.mediaPlayer.release();
        }*/
        mediaSessionCallBack.onSkipToNext();
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int i, @Nullable Bundle bundle) {
        if(TextUtils.equals(clientPackageName, getPackageName())) {
            return new BrowserRoot(getString(R.string.app_name), null);
        }

        return null;
    }

    @Override
    public void onLoadChildren(@NonNull String s, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        result.sendResult(null);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MediaButtonReceiver.handleIntent(mediaSessionCompat, intent);
        return super.onStartCommand(intent, flags, startId);
    }

    //hàm này đưa vào cái song trả về ảnh của song đó
    public Bitmap Screen(Song song)
    {
        //decode Bitmap
        Bitmap bm = BitmapFactory.decodeFile(song.getAlbumArt());
        // nếu bitmap null == không có hình ta sẽ thay bằng hình mặc định
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.adele);
        if(bm != null)
        {
            return bm;
        }
        else
        {
            return bitmap;
        }

    }
}
