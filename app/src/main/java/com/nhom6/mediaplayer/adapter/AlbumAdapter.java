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
import com.nhom6.mediaplayer.model.Album;
import com.nhom6.mediaplayer.model.Song;

import java.util.ArrayList;
import java.util.List;

public class AlbumAdapter extends ArrayAdapter<Album> {

    private Context context;
    private int resource;
    private ArrayList<Album> arrAlbum;


    public AlbumAdapter( Context context, int resource,  ArrayList<Album> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.arrAlbum = objects;
    }

    @NonNull
    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.girdview_album,parent,false);
        ImageView imageAlbum = (ImageView) convertView.findViewById(R.id.grid_item_imageAlbum);
        TextView albumName = (TextView) convertView.findViewById(R.id.grid_item_nameAlbum);
        Album song = arrAlbum.get(position);
        Bitmap bm = BitmapFactory.decodeFile(song.getAlbumArt());

        if (bm!=null) {
            imageAlbum.setImageBitmap(bm);
        }
        else {
            imageAlbum.setImageResource(R.drawable.adele);
        }
        albumName.setText(song.getAlbumTitle());
        return convertView;
    }
}
