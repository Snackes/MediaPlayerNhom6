package com.nhom6.mediaplayer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.nhom6.mediaplayer.Manager.AlbumManager;
import com.nhom6.mediaplayer.Manager.ArtistManager;
import com.nhom6.mediaplayer.Manager.SongManager;
import com.nhom6.mediaplayer.activity.ShowAllSong;
import com.nhom6.mediaplayer.model.Album;
import com.nhom6.mediaplayer.model.Artist;
import com.nhom6.mediaplayer.model.Song;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_main);


        //kiem tra permission
        CheckUserPermission(this);


    }


    public void ShowAllSong(View view)
    {
        Intent i = new Intent(this, ShowAllSong.class) ;
        startActivity(i);
    }

    private void CheckUserPermission(Context context)
    {
        //TODO: nếu chạy lần đầu thì sẽ vào đây xin permission
        if(Build.VERSION.SDK_INT >= 23)
        {
            if(ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                  != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},111);
                return;
            }
        }

        //TODO: nếu đã có permission rồi thì những lần sau sẽ chạy vào đây

        // do something
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case 111:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this, "Permission ok", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    CheckUserPermission(this);
                }
                break;
                default: super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
