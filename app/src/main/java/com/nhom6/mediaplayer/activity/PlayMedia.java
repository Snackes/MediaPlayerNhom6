package com.nhom6.mediaplayer.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.nhom6.mediaplayer.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayMedia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_play_media);



        CircleImageView img = (CircleImageView) findViewById(R.id.img_Song);
        TextView nameSong = (TextView) findViewById(R.id.Songname);
        TextView nameArtist = (TextView) findViewById(R.id.songArtist);

        //nhận intent từ activity kia
        Intent i = getIntent();
        //lấy bundle
        Bundle Song = i.getExtras();
        String Songname = Song.getString("Songname");
        String Artistname = Song.getString("Artistname");
        String Album = Song.getString("Album");
        String SongUrl = Song.getString("SongUrl");
        String AlbumArt = Song.getString("AlbumArt");
        Integer ArtistnameId = Song.getInt("ArtistnameId");
        Integer AlbumId = Song.getInt("AlbumId");
        Integer Duration = Song.getInt("Duration");
        Integer Songid = Song.getInt("Songid");



        //

        //decode Bitmap
        Bitmap bm = BitmapFactory.decodeFile(AlbumArt);
        // nếu bitmap null == không có hình ta sẽ thay bằng hình mặc định
        if(bm != null)
        {
            img.setImageBitmap(bm);
        }
        else
        {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.adele);
            img.setImageBitmap(bitmap);
        }


        nameSong.setText(Songname);
        nameArtist.setText(Artistname);

    }
}
