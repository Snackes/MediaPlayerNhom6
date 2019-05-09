package com.nhom6.mediaplayer.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.nhom6.mediaplayer.Manager.SongManager;
import com.nhom6.mediaplayer.R;
import com.nhom6.mediaplayer.SwipeTouch.OnSwipeTouchListener;
import com.nhom6.mediaplayer.adapter.ListSameSongAdapter;
import com.nhom6.mediaplayer.model.Song;

import java.util.ArrayList;

public class SameSongActivity extends AppCompatActivity {
    final Context context = this;
    private ListView listSameSong;
    //khai báo SongManager để loadSong
    SongManager songsManager = new SongManager();
    public ArrayList<Song> _songs = new ArrayList<Song>();
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_same_song);
        listSameSong = (ListView) findViewById(R.id.listSameSong);

        //tiến hành lấy song tuong tu trong máy
        _songs = songsManager.loadSong(this);
        //đưa vào adapter để hiển thị
        ListSameSongAdapter listSameSongAdapter = new ListSameSongAdapter(this, R.layout.row_item_samesong, _songs);
        listSameSong.setAdapter(null);

        ConstraintLayout mView;
        mView = findViewById(R.id.layoutSameSong);
        mView.setOnTouchListener(new OnSwipeTouchListener(SameSongActivity.this) {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }

            public void onSwipeTop() {
                Toast.makeText(SameSongActivity.this, "top", Toast.LENGTH_SHORT).show();

            }

            public void onSwipeRight() {
                Intent i = new Intent(context, PlayActivity.class);
                startActivity(i);
            }

            public void onSwipeLeft() {
                Toast.makeText(SameSongActivity.this, "left", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeBottom() {
                Toast.makeText(SameSongActivity.this, "bottom", Toast.LENGTH_SHORT).show();
            }

        });

    }
}
