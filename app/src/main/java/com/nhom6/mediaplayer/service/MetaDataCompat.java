package com.nhom6.mediaplayer.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;

import com.nhom6.mediaplayer.Database.MyDatabaseHelper;
import com.nhom6.mediaplayer.model.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class MetaDataCompat {
    public static final TreeMap<String, MediaMetadataCompat> music = new TreeMap<>();

    public static String getRoot() {
        return "root";
    }


    public static String getMusicPath(Context context, String mediaId) {
        MyDatabaseHelper db = new MyDatabaseHelper(context);
        Song song = db.GetInfoSong(Integer.parseInt(mediaId));
        return song.getSongUrl();
    }

    public static Bitmap getAlbumBitmap(Context context, String mediaId) {
        MyDatabaseHelper db = new MyDatabaseHelper(context);
        Song song = db.GetInfoSong(Integer.parseInt(mediaId));
        return BitmapFactory.decodeFile(song.getAlbumArt());
    }

    public static List<MediaBrowserCompat.MediaItem> getMediaItems() {
        List<MediaBrowserCompat.MediaItem> result = new ArrayList<>();
        for (MediaMetadataCompat metadata : music.values()) {
            result.add(
                    new MediaBrowserCompat.MediaItem(
                            metadata.getDescription(), MediaBrowserCompat.MediaItem.FLAG_PLAYABLE));
        }
        return result;
    }

    public static MediaMetadataCompat getMetadata(Context context, String mediaId) {
        MediaMetadataCompat metadataWithoutBitmap = music.get(mediaId);
        Bitmap albumArt = getAlbumBitmap(context, mediaId);

        // Since MediaMetadataCompat is immutable, we need to create a copy to set the album art.
        // We don't set it initially on all items so that they don't take unnecessary memory.
        MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder();
        for (String key :
                new String[]{
                        MediaMetadataCompat.METADATA_KEY_MEDIA_ID,
                        MediaMetadataCompat.METADATA_KEY_ALBUM,
                        MediaMetadataCompat.METADATA_KEY_ARTIST,
                        MediaMetadataCompat.METADATA_KEY_GENRE,
                        MediaMetadataCompat.METADATA_KEY_TITLE
                }) {

            builder.putString(key, metadataWithoutBitmap.getString(key));
        }

        builder.putLong(
                MediaMetadataCompat.METADATA_KEY_DURATION,
                metadataWithoutBitmap.getLong(MediaMetadataCompat.METADATA_KEY_DURATION));
        builder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArt);
        return builder.build();
    }

    public void clearMediaMetadataCompat() {
        music.clear();

    }

    public void createMediaMetadataCompat(Song song) {

        music.put(
                String.valueOf(song.getSongid()),
                new MediaMetadataCompat.Builder()
                        .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, String.valueOf(song.getSongid()))
                        .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, song.getAlbum())
                        .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, song.getArtistname())
                        .putLong(MediaMetadataCompat.METADATA_KEY_DURATION,
                                TimeUnit.MILLISECONDS.convert(song.getDuration(), TimeUnit.MILLISECONDS))
                        .putString(
                                MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI,
                                song.getAlbumArt())
                        .putString(
                                MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI,
                                song.getAlbumArt())
                        .putString(MediaMetadataCompat.METADATA_KEY_TITLE, song.getSongname())
                        .build()

        );

    }


}
