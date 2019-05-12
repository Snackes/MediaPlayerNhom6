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
import com.nhom6.mediaplayer.model.Song;

import java.util.ArrayList;

public class ListSameSongAdapter extends ArrayAdapter<Song> {

    private Context context;
    private int resource;
    private ArrayList<Song> arrSong;


    public ListSameSongAdapter(Context context, int resource, ArrayList<Song> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.arrSong = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.row_item_samesong, parent, false);

        ImageView imageSong = (ImageView) convertView.findViewById(R.id.image_samesong);
        TextView songName = (TextView) convertView.findViewById(R.id.name_samesong);
        TextView artistName = (TextView) convertView.findViewById(R.id.name_sameartist);


        Song song = arrSong.get(position);

        Bitmap bm = BitmapFactory.decodeFile(song.getAlbumArt());
        if (bm != null) {
            imageSong.setImageBitmap(bm);
        }
        else {
            imageSong.setImageResource(R.drawable.adele);
        }
        songName.setText(song.getSongname());
        artistName.setText(song.getArtistname());

        return convertView;
    }
}
