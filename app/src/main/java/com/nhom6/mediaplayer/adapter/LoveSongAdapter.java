package com.nhom6.mediaplayer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nhom6.mediaplayer.R;
import com.nhom6.mediaplayer.model.LoveSong;
import com.nhom6.mediaplayer.model.Song;

import java.util.ArrayList;

public class LoveSongAdapter extends ArrayAdapter<LoveSong> {

    private Context context;
    private int resource;
    private ArrayList<LoveSong> arrLoveSong;


    public LoveSongAdapter( Context context, int resource,  ArrayList<LoveSong> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.arrLoveSong = objects;
    }

    @NonNull
    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.row_item_lovesong,parent,false);

        ImageView imageSong = (ImageView) convertView.findViewById(R.id.image_lovesong);
        TextView songName = (TextView) convertView.findViewById(R.id.name_lovesong);
        TextView artistName = (TextView) convertView.findViewById(R.id.name_loveartist);


        LoveSong lovesong = arrLoveSong.get(position);

        Bitmap bm = BitmapFactory.decodeFile(lovesong.getAlbumArt());

        if (bm != null) {
            imageSong.setImageBitmap(bm);
        }
        else {
            imageSong.setImageResource(R.drawable.adele);
        }
        songName.setText(lovesong.getSongname());
        artistName.setText(lovesong.getArtistname());

        return convertView;
    }
}
