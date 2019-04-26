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

public class CustomAdapter extends ArrayAdapter<Song> {

    private Context context;
    private int resource;
    private ArrayList<Song> arrSong;


    public CustomAdapter( Context context, int resource,  ArrayList<Song> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.arrSong = objects;
    }

    @NonNull
    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.row_item_allsong,parent,false);

        ImageView imageSong = (ImageView) convertView.findViewById(R.id.imageViewAD);
        TextView songName = (TextView) convertView.findViewById(R.id.songNameAD);
        TextView artistName = (TextView) convertView.findViewById(R.id.singerAD);


        Song song = arrSong.get(position);

        imageSong.setImageBitmap(song.getAlbumArt());
        songName.setText(song.getSongname());
        artistName.setText(song.getArtistname());

        return convertView;
    }
}
