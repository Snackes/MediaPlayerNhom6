package com.nhom6.mediaplayer.service;


import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

/**
 * Listener to provide state updates from {@link MediaPlayerAdapter} (the media player)
 * to {link MusicService} (the service that holds our {@link MediaSessionCompat}.
 */
public abstract class PlaybackInfoListener {
    public abstract void onPlaybackStateChange(PlaybackStateCompat state);

    public abstract void onPlaybackCompleted();
}
