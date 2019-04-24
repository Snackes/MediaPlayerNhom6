package com.nhom6.mediaplayer.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nhom6.mediaplayer.R;
import com.nhom6.mediaplayer.model.Song;

import java.util.ArrayList;
import java.util.List;

public class ListSongAdapter extends ArrayAdapter<Song> {

    private Context context;
    private int resource;
    private ArrayList<Song> arrSong;


    public ListSongAdapter( Context context, int resource,  ArrayList<Song> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.arrSong = objects;
    }

    @NonNull
    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.row_item_song,parent,false);

        ImageView imageSong = (ImageView) convertView.findViewById(R.id.image_songs);
        TextView songName = (TextView) convertView.findViewById(R.id.name_songs);
        TextView artistName = (TextView) convertView.findViewById(R.id.name_artist);


        Song song = arrSong.get(position);

        imageSong.setImageBitmap(song.getAlbumArt());
        songName.setText(song.getSongname());
        artistName.setText(song.getArtistname());

        return convertView;
    }
}
