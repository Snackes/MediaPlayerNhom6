package com.nhom6.mediaplayer.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nhom6.mediaplayer.R;
import com.nhom6.mediaplayer.model.PlayList;
import com.nhom6.mediaplayer.model.Song;

import java.util.ArrayList;

public class PlaylistAdapter extends BaseAdapter {

    private ArrayList<PlayList> arrPlayList;
    Activity activity;
    private Context context;
    public  PlaylistAdapter(Activity activity1,ArrayList<PlayList>arrayList){
        this.activity=activity1;
        this.arrPlayList=arrayList;
    }
    @Override
    public int getCount() {
        return arrPlayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrPlayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public static class ViewHolderPlayList{
        ImageView imagePlayList;
        TextView playListName;
        public ViewHolderPlayList(View view) {
            imagePlayList=(ImageView) view.findViewById(R.id.image_playlist);
            playListName = (TextView) view.findViewById(R.id.name_playlist);
            view.setTag(this);
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(activity.getApplication(),
                    R.layout.row_item_playlist, null);
            new ViewHolderPlayList(convertView);
        }
        ViewHolderPlayList holder = (ViewHolderPlayList) convertView.getTag();
        Object item = getItem(position);
        PlayList playList=(PlayList)item;

        holder.playListName.setText(playList.getTitle());
        holder.imagePlayList.setImageResource(R.drawable.adele);

        // Trả về view kết quả.
        return convertView;
    }
}