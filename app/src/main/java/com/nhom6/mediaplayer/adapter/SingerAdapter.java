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
import com.nhom6.mediaplayer.model.Artist;
import com.nhom6.mediaplayer.model.PlayList;
import com.nhom6.mediaplayer.model.Song;

import java.util.ArrayList;

public class SingerAdapter extends ArrayAdapter<Artist> {

    private Context context;
    private int resource;
    private ArrayList<Artist> arrArtist;


    public SingerAdapter( Context context, int resource,  ArrayList<Artist> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.arrArtist = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.row_item_singer,parent,false);

        ImageView imageArtist = (ImageView) convertView.findViewById(R.id.image_singer);
        TextView ArtistName = (TextView) convertView.findViewById(R.id.name_singer);



        Artist artist = arrArtist.get(position);

        imageArtist.setImageResource(R.drawable.ic_singer);
        ArtistName.setText(artist.getArtistName());
        return convertView;
    }
}