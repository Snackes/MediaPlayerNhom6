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
import com.nhom6.mediaplayer.model.PlayList;
import com.nhom6.mediaplayer.model.Song;

import java.util.ArrayList;

public class PlaylistAdapter extends ArrayAdapter<PlayList> {

    private Context context;
    private int resource;
    private ArrayList<PlayList> arrPlayList;


    public PlaylistAdapter( Context context, int resource,  ArrayList<PlayList> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.arrPlayList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.row_item_playlist,parent,false);

        ImageView imagePlayList = (ImageView) convertView.findViewById(R.id.image_playlist);
        TextView playListName = (TextView) convertView.findViewById(R.id.name_playlist);



        PlayList playList = arrPlayList.get(position);

        imagePlayList.setImageResource(R.drawable.adele);
        playListName.setText(playList.getTitle());

        return convertView;
    }
}