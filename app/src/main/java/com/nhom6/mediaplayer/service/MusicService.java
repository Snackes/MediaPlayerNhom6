package com.nhom6.mediaplayer.service;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.nhom6.mediaplayer.activity.SaveData;
import com.nhom6.mediaplayer.notification.MediaNotificationManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class MusicService extends MediaBrowserServiceCompat {

    private static final String TAG = MusicService.class.getSimpleName();

    public static final String REPEAT_ACTION = "com.nhom6.mediaplayer.service.repeat";
    public static final String SHUFFLE_ACTION = "com.nhom6.mediaplayer.service.shuffle";


    private MediaSessionCompat mSession;
    private PlayerAdapter mPlayback;
    private MediaNotificationManager mMediaNotificationManager;
    private MediaSessionCallback mCallback;
    private boolean mServiceInStartedState;
    private Random rand = new Random();
    private boolean isShuff = false;
    private int dem=1;


    //
    public final List<MediaSessionCompat.QueueItem> mPlaylist = new ArrayList<>();
    private int mQueueIndex = -1;
    private List<MediaBrowserCompat.MediaItem> childrenOLD=new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();
        // Create a new MediaSession.
        mSession = new MediaSessionCompat(this, "MusicService");
        mCallback = new MediaSessionCallback();
        mSession.setCallback(mCallback);
        mSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_QUEUE_COMMANDS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        setSessionToken(mSession.getSessionToken());

        mMediaNotificationManager = new MediaNotificationManager(this);

        mPlayback = new MediaPlayerAdapter(this, new MediaPlayerListener());
        Log.d(TAG, "onCreate: MusicService creating MediaSession, and MediaNotificationManager");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        //stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaNotificationManager.onDestroy();
        mPlayback.stop();
        mSession.release();
        Log.d(TAG, "onDestroy: MediaPlayerAdapter stopped, and MediaSession released");
    }

    public MusicService() {
    }
    public void dmm(List<MediaBrowserCompat.MediaItem> children){
        childrenOLD.addAll(children);
    }

    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName,
                                 int clientUid,
                                 Bundle rootHints) {
        return new BrowserRoot(MetaDataCompat.getRoot(), null);
    }

    @Override
    public void onLoadChildren(
            @NonNull final String parentMediaId,
            @NonNull final Result<List<MediaBrowserCompat.MediaItem>> result) {
        result.sendResult(MetaDataCompat.getMediaItems());
    }

    // MediaSession Callback: Transport Controls -> MediaPlayerAdapter
    public class MediaSessionCallback extends MediaSessionCompat.Callback {





        private MediaMetadataCompat mPreparedMedia;


        @Override
        public void onAddQueueItem(MediaDescriptionCompat description) {

            boolean Flag= SaveData.getFlag();
            int CountList=SaveData.getCountList();
            if(dem==1) {
                if (Flag == true) {
                    mPlaylist.clear();
                    mQueueIndex = -1;
                }
            }
            if(dem==CountList){
                dem=0;
            }
            dem=dem+1;
            mPlaylist.add(new MediaSessionCompat.QueueItem(description, description.hashCode()));
            mQueueIndex = (mQueueIndex == -1) ? 0 : mQueueIndex;
            mSession.setQueue(mPlaylist);
        }

        @Override
        public void onRemoveQueueItem(MediaDescriptionCompat description) {
            mPlaylist.remove(new MediaSessionCompat.QueueItem(description, description.hashCode()));
            mQueueIndex = (mPlaylist.isEmpty()) ? -1 : mQueueIndex;
            mSession.setQueue(mPlaylist);
        }


        @Override
        public void onPrepareFromMediaId(String position, Bundle extras) {
            super.onPrepareFromMediaId(position, extras);

            if (position == null && extras == null) {
                if (mQueueIndex < 0 && mPlaylist.isEmpty()) {
                    // Nothing to play.
                    return;
                }
                final String mediaId = mPlaylist.get(mQueueIndex).getDescription().getMediaId();
                mPreparedMedia = MetaDataCompat.getMetadata(MusicService.this, mediaId);
                mSession.setMetadata(mPreparedMedia);
            } else {

                if (Integer.valueOf(position) < 0 && mPlaylist.isEmpty()) {
                    // Nothing to play.
                    return;
                }
                mQueueIndex = Integer.valueOf(position);
                final String mediaId = mPlaylist.get(Integer.valueOf(position)).getDescription().getMediaId();
                mPreparedMedia = MetaDataCompat.getMetadata(MusicService.this, mediaId);
                mSession.setMetadata(mPreparedMedia);
            }
            if (!mSession.isActive()) {
                mSession.setActive(true);
            }
            //mPlaylist.clear();

        }

        @Override
        public void onPrepare() {
            if (mQueueIndex < 0 && mPlaylist.isEmpty()) {
                // Nothing to play.
                return;
            }

            final String mediaId = mPlaylist.get(mQueueIndex).getDescription().getMediaId();
            mPreparedMedia = MetaDataCompat.getMetadata(MusicService.this, mediaId);
            mSession.setMetadata(mPreparedMedia);
            if (!mSession.isActive()) {
                mSession.setActive(true);
            }


        }


        @Override
        public void onPlay() {
            if (!isReadyToPlay()) {
                // Nothing to play.
                return;
            }

            if (mPreparedMedia == null) {
                onPrepareFromMediaId(null, null);
            }

            mPlayback.playFromMedia(mPreparedMedia);
            //mPlaylist.clear();
            Log.d(TAG, "onPlayFromMediaId: MediaSession active");
        }


        @Override
        public void onPause() {
            mPlayback.pause();
        }

        @Override
        public void onStop() {
            mPlayback.stop();
            mSession.setActive(false);
        }

        @Override
        public void onSkipToNext() {

            if (isShuff == true) {
                mQueueIndex = rand.nextInt(mPlaylist.size() - 1);
            } else {
                mQueueIndex = (++mQueueIndex % mPlaylist.size());
            }

            mPreparedMedia = null;
            onPlay();
        }

        @Override
        public void onSkipToPrevious() {
            mQueueIndex = mQueueIndex > 0 ? mQueueIndex - 1 : mPlaylist.size() - 1;
            mPreparedMedia = null;
            onPlay();
        }

        @Override
        public void onSetRepeatMode(int repeatMode) {
            super.onSetRepeatMode(repeatMode);
            if (repeatMode == 0)//false
            {
                mPlayback.onLooping(false);

            } else {
                mPlayback.onLooping(true);

            }

        }

        @Override
        public void onSetShuffleMode(int shuffleMode) {
            super.onSetShuffleMode(shuffleMode);

            if (shuffleMode == 1) //true
            {
                isShuff = true;
            } else {
                isShuff = false;
            }


//            if(shuffleMode != 0) //true
//            {
//                mQueueIndex = rand.nextInt(mPlaylist.size()-1);
//            }


        }


        @Override
        public void onSeekTo(long pos) {
            mPlayback.seekTo(pos);
            onPlay();
        }


        private boolean isReadyToPlay() {
            return (!mPlaylist.isEmpty());
        }
    }


    // MediaPlayerAdapter Callback: MediaPlayerAdapter state -> MusicService.
    public class MediaPlayerListener extends PlaybackInfoListener {

        private final ServiceManager mServiceManager;

        MediaPlayerListener() {
            mServiceManager = new ServiceManager();
        }

        @Override
        public void onPlaybackStateChange(PlaybackStateCompat state) {
            // Report the state to the MediaSession.
            mSession.setPlaybackState(state);

            // Manage the started state of this service.
            switch (state.getState()) {
                case PlaybackStateCompat.STATE_PLAYING: {
                    mServiceManager.moveServiceToStartedState(state);

                    break;
                }

                case PlaybackStateCompat.STATE_PAUSED:
                    mServiceManager.updateNotificationForPause(state);
                    break;
                case PlaybackStateCompat.STATE_STOPPED:
                    mServiceManager.moveServiceOutOfStartedState(state);
                    break;
                case PlaybackStateCompat.STATE_CONNECTING:
                    mCallback.onSkipToNext();
                    break;
//                case PlaybackStateCompat.REPEAT_MODE_NONE:
//                    mCallback.onRepeatMode(false);
//                    break;
//                case PlaybackStateCompat.REPEAT_MODE_GROUP:


            }
        }

        @Override
        public void onPlaybackCompleted() {


        }

        class ServiceManager {

            private void moveServiceToStartedState(PlaybackStateCompat state) {
                Notification notification =
                        mMediaNotificationManager.getNotification(
                                mPlayback.getCurrentMedia(), state, getSessionToken());

                if (!mServiceInStartedState) {
                    ContextCompat.startForegroundService(
                            MusicService.this,
                            new Intent(MusicService.this, MusicService.class));
                    mServiceInStartedState = true;
                }

                startForeground(MediaNotificationManager.NOTIFICATION_ID, notification);
            }

            private void updateNotificationForPause(PlaybackStateCompat state) {
                stopForeground(false);
                Notification notification =
                        mMediaNotificationManager.getNotification(
                                mPlayback.getCurrentMedia(), state, getSessionToken());
                mMediaNotificationManager.getNotificationManager()
                        .notify(MediaNotificationManager.NOTIFICATION_ID, notification);
            }

            private void moveServiceOutOfStartedState(PlaybackStateCompat state) {
                stopForeground(true);
                stopSelf();
                mServiceInStartedState = false;
            }
        }

    }


}

