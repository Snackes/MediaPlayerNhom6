package com.nhom6.mediaplayer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.nhom6.mediaplayer.model.Song;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Manager songsManager = new Manager();
    public ArrayList<Song> _songs = new ArrayList<Song>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        checkUserPermission();









    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 123:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission ok", Toast.LENGTH_SHORT).show();

                    //tien hanh lay list songs tu divice

                    _songs = songsManager.loadSong(this);

                    //
                }else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    checkUserPermission();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }
    }

    private void checkUserPermission()
    {
        if(Build.VERSION.SDK_INT>=23){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},123);
                return;
            }
        }



    }
}
