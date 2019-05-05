package com.nhom6.mediaplayer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.nhom6.mediaplayer.activity.AlbumActivity;
import com.nhom6.mediaplayer.activity.LoveActivity;
import com.nhom6.mediaplayer.activity.PlayScreenActivity;
import com.nhom6.mediaplayer.activity.PlaylistActivity;
import com.nhom6.mediaplayer.activity.ShowAllSong;
import com.nhom6.mediaplayer.activity.SingerActivity;

public class MainActivity extends AppCompatActivity {

    public static final int PLAYSCREEN_RESULT = 1;
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
    public void ShowPlayList(View view)
    {
        Intent i = new Intent(this, PlaylistActivity.class) ;
        startActivity(i);
    }
    public  void ShowAlbum(View view)
    {
        Intent i=new Intent(this, AlbumActivity.class);
        startActivity(i);
    }
    public  void ShowPlayScreen(View view)
    {
        Intent i=new Intent(this, PlayScreenActivity.class);
        startActivity(i);
    }
    public  void ShowSinger(View view)
    {
        Intent i = new Intent(this, SingerActivity.class);
        startActivity(i);
    }
    public  void ShowLoveSong(View view)
    {
        Intent i=new Intent(this, LoveActivity.class);
        startActivity(i);
    }
    public void clickScan(View view)
    {
        Snackbar.make(view,"Đang quét", Snackbar.LENGTH_LONG)
                .setAction("No action", null).show();
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
