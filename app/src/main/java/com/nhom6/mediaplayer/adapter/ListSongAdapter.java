package com.nhom6.mediaplayer.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nhom6.mediaplayer.R;
import com.nhom6.mediaplayer.activity.SaveData;
import com.nhom6.mediaplayer.model.Song;

import java.util.ArrayList;
import java.util.List;

public class ListSongAdapter extends BaseAdapter {
    Activity activity;
    int dm=1;
    private Context context;
    public ArrayList<Song>arrayListsong=new ArrayList<Song>();
    public  ListSongAdapter(Activity activity1,ArrayList<Song>arrayList){
        this.activity=activity1;
        this.arrayListsong=arrayList;
    }
    @Override
    public int getCount() {
        return arrayListsong.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayListsong.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public static class ViewHolder{
        ImageView imageSong;
        TextView songName;
        TextView artistName;
        public ViewHolder(View view) {
            imageSong=(ImageView) view.findViewById(R.id.image_song);
            songName = (TextView) view.findViewById(R.id.name_song);
            artistName = (TextView) view.findViewById(R.id.name_artist);
            view.setTag(this);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        dm++;
        if (convertView == null) {
            convertView = View.inflate(activity.getApplication(),
                    R.layout.row_item_song, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        Object item = getItem(position);
        Song song=(Song)item;
        Bitmap bm = BitmapFactory.decodeFile(song.getAlbumArt());

        if (bm != null) {
            holder.imageSong.setImageBitmap(bm);
        }
        else {
            holder.imageSong.setImageResource(R.drawable.adele);
        }
        holder.songName.setText(song.getSongname());
        holder.artistName.setText(song.getArtistname());

        // Trả về view kết quả.
        return convertView;
    }
}