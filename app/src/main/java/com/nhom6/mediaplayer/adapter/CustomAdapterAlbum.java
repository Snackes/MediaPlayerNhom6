package com.nhom6.mediaplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.nhom6.mediaplayer.R;


public class CustomAdapterAlbum extends BaseAdapter {

    Context mContext;
    String lstAlbumName[];
    int imgAlbum[];
    LayoutInflater inflater;

    public CustomAdapterAlbum(Context applicationContext, String[] lstAlbumName, int[] imgAlbum) {
        this.mContext = applicationContext;
        this.lstAlbumName = lstAlbumName;
        this.imgAlbum=imgAlbum;
        inflater = LayoutInflater.from(applicationContext);
    }

    @Override
    public int getCount() {
        return lstAlbumName.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.row_item_album, null);
        TextView album = view.findViewById(R.id.albumNameAD);
        ImageView img = view.findViewById(R.id.imageAlbumAD);
        album.setText(lstAlbumName[i]);
        img.setBackgroundResource(imgAlbum[i]);
        return view;

    }
}
