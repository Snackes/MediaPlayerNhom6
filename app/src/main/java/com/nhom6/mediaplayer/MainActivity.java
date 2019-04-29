package com.nhom6.mediaplayer;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.nhom6.mediaplayer.Database.MyDatabaseHelper;
import com.nhom6.mediaplayer.Manager.AlbumManager;
import com.nhom6.mediaplayer.Manager.ArtistManager;
import com.nhom6.mediaplayer.Manager.SongManager;
import com.nhom6.mediaplayer.activity.ShowAllSong;
import com.nhom6.mediaplayer.model.Album;
import com.nhom6.mediaplayer.model.Artist;
import com.nhom6.mediaplayer.model.Playlist;
import com.nhom6.mediaplayer.model.Song;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SongManager songsManager = new SongManager();
    public ArrayList<Song> _songs = new ArrayList<Song>();
    //
    AlbumManager albumsManager =  new AlbumManager();
    public ArrayList<Album> _album = new ArrayList<Album>();
    //
    ArtistManager artistManager =  new ArtistManager();
    public  ArrayList<Artist> _artist = new ArrayList<Artist>();

    private ListView listView;
    //
    // .private static final int MENU_ITEM_VIEW = 111;
    private static final int MENU_ITEM_EDIT = 222;
    private static final int MENU_ITEM_CREATE = 333;
    private static final int MENU_ITEM_DELETE = 444;


    private static final int MY_REQUEST_CODE = 1000;

    private final List<Playlist> ListPlaylist = new ArrayList<Playlist>();
    private ArrayAdapter<Playlist> listViewAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_main);

        CheckUserPermission(this);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        //kiem tra permission
        CheckUserPermission(this);

        MyDatabaseHelper db = new MyDatabaseHelper(this);
        db.createDefaultPlaylistsIfNeed();
        //db.getMaxIDPlaylist(); lấy maxid của playlist đã có trong csdl

        List<Playlist> list=  db.getAllPlaylists();
        ArrayList<Playlist> temp=new ArrayList<Playlist>();
        boolean flag=true;
        temp.add(list.get(0));
        for(int i=1;i<list.size();i++){
            flag=true;
            for(int j=0;j<temp.size();j++){
                if(temp.get(j).getIDPlaylist()==list.get(i).getIDPlaylist()){
                    flag=false;
                }
            }
            if(flag==true) {
                temp.add(list.get(i));
            }
        }
        this.ListPlaylist.addAll(temp);
        // Định nghĩa một Adapter.
        // 1 - Context
        // 2 - Layout cho các dòng.
        // 3 - ID của TextView mà dữ liệu sẽ được ghi vào
        // 4 - Danh sách dữ liệu.

        this.listViewAdapter = new ArrayAdapter<Playlist>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, this.ListPlaylist);

        // Đăng ký Adapter cho ListView.
        this.listView.setAdapter(this.listViewAdapter);

        // Đăng ký Context menu cho ListView.
        registerForContextMenu(this.listView);
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
        if(_songs.isEmpty())
        {
            _songs = songsManager.loadSong(this);
            _album = albumsManager.loadAlbum(this);
            _artist = artistManager.loadArtist(this);

            Song song = songsManager.loadSongWithID(this,33030);

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


    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenu.ContextMenuInfo menuInfo)    {

        super.onCreateContextMenu(menu, view, menuInfo);
        menu.setHeaderTitle("Select The Action");

        // groupId, itemId, order, title
        menu.add(0, MENU_ITEM_CREATE , 1, "Create Playlist");
        menu.add(0, MENU_ITEM_EDIT , 2, "Edit Playlist");
        menu.add(0, MENU_ITEM_DELETE, 4, "Delete Playlist");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final Playlist selectedPlaylist = (Playlist) this.listView.getItemAtPosition(info.position);

        if(item.getItemId() == MENU_ITEM_CREATE){
            Intent intent = new Intent(this, ListSongActivity.class);
            // Start createPlaylist, có phản hồi.
            this.startActivityForResult(intent, MY_REQUEST_CODE);
        }
        else if(item.getItemId() == MENU_ITEM_EDIT ){
            Intent intent = new Intent(this, ListSongActivity.class);
            intent.putExtra("playlist", selectedPlaylist);

            // Start AddEditPlaylistActivity, có phản hồi.
            this.startActivityForResult(intent,MY_REQUEST_CODE);
        }
        else if(item.getItemId() == MENU_ITEM_DELETE){
            // Hỏi trước khi xóa.
            new AlertDialog.Builder(this)
                    .setMessage(selectedPlaylist.getTitle()+". Are you sure you want to delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            deletePlaylist(selectedPlaylist);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        else {
            return false;
        }
        return true;
    }

    // Người dùng đồng ý xóa một playlist.
    private void deletePlaylist(Playlist playlist)  {
        MyDatabaseHelper db = new MyDatabaseHelper(this);
        db.deletePlaylist(playlist);
        this.ListPlaylist.remove(playlist);
        // Refresh ListView.
        this.listViewAdapter.notifyDataSetChanged();
    }


    // Khi AddEditPlaylistActivity hoàn thành, nó gửi phản hồi lại.
    // (Nếu bạn đã start nó bằng cách sử dụng startActivityForResult()  )
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == MY_REQUEST_CODE ) {
            boolean needRefresh = data.getBooleanExtra("needRefresh",true);
            // Refresh ListView
            if(needRefresh) {
                this.ListPlaylist.clear();
                MyDatabaseHelper db = new MyDatabaseHelper(this);
                List<Playlist> list=  db.getAllPlaylists();
                ArrayList<Playlist> temp=new ArrayList<Playlist>();
                boolean flag=true;
                temp.add(list.get(0));
                for(int i=1;i<list.size();i++){
                    flag=true;
                    for(int j=0;j<temp.size();j++){
                        if(temp.get(j).getIDPlaylist()==list.get(i).getIDPlaylist()){
                            flag=false;
                        }
                    }
                    if(flag==true) {
                        temp.add(list.get(i));
                    }
                }
                this.ListPlaylist.addAll(temp);
                // Thông báo dữ liệu thay đổi (Để refresh ListView).
                this.listViewAdapter.notifyDataSetChanged();
            }
        }
    }
}
