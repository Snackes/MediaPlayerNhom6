package com.nhom6.mediaplayer.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nhom6.mediaplayer.Database.MyDatabaseHelper;
import com.nhom6.mediaplayer.Manager.PlaylistManager;
import com.nhom6.mediaplayer.R;
import com.nhom6.mediaplayer.model.Playlist;

import java.util.ArrayList;

public class ShowAllPlaylist extends AppCompatActivity {

    PlaylistManager playlistManager=new PlaylistManager();
    private ListView listView;
    //
    // .private static final int MENU_ITEM_VIEW = 111;
    private static final int MENU_ITEM_EDIT = 222;
    private static final int MENU_ITEM_CREATE = 333;
    private static final int MENU_ITEM_DELETE = 444;


    private static final int MY_REQUEST_CODE = 1000;

    private ArrayList<Playlist> ListPlaylist = new ArrayList<Playlist>();
    private ArrayAdapter<Playlist> listViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_show_all_playlist);


        listView = findViewById(R.id.listView);
        MyDatabaseHelper db = new MyDatabaseHelper(this);
        db.createDefaultPlaylistsIfNeed();
        ListPlaylist=  db.getAllPlaylists();
        // Định nghĩa một Adapter.
        // 1 - Context
        // 2 - Layout cho các dòng.
        // 3 - ID của TextView mà dữ liệu sẽ được ghi vào
        // 4 - Danh sách dữ liệu.

        this.listViewAdapter = new ArrayAdapter<Playlist>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, ListPlaylist);

        // Đăng ký Adapter cho ListView.
        this.listView.setAdapter(this.listViewAdapter);

        // Đăng ký Context menu cho ListView.
        registerForContextMenu(this.listView);
    }

    @Override
    public void onCreateContextMenu (ContextMenu menu, View view,
                                     ContextMenu.ContextMenuInfo menuInfo){

        super.onCreateContextMenu(menu, view, menuInfo);
        menu.setHeaderTitle("Select The Action");

        // groupId, itemId, order, title
        menu.add(0, MENU_ITEM_CREATE, 1, "Tạo playlist");
        menu.add(0, MENU_ITEM_EDIT, 2, "Sửa playlist");
        menu.add(0, MENU_ITEM_DELETE, 4, "Xóa Playlist");
    }
    @Override
    public boolean onContextItemSelected (MenuItem item){
        AdapterView.AdapterContextMenuInfo
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final Playlist selectedPlaylist = (Playlist) this.listView.getItemAtPosition(info.position);

        if (item.getItemId() == MENU_ITEM_CREATE) {
            Intent intent = new Intent(this, ShowAllSongInPlaylist.class);
            // Start createPlaylist, có phản hồi.
            this.startActivityForResult(intent, MY_REQUEST_CODE);
        } else if (item.getItemId() == MENU_ITEM_EDIT) {
            Intent intent = new Intent(this, ShowAllSongInPlaylist.class);
            intent.putExtra("playlist", selectedPlaylist);
            // Start AddEditPlaylistActivity, có phản hồi.
            this.startActivityForResult(intent, MY_REQUEST_CODE);
        } else if (item.getItemId() == MENU_ITEM_DELETE) {
            // Hỏi trước khi xóa.
            new AlertDialog.Builder(this)
                    .setMessage(selectedPlaylist.getTitle() + ". Are you sure you want to delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            playlistManager.deletePlaylist(selectedPlaylist);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        } else {
            return false;
        }
        return true;
    }



    // Khi AddEditPlaylistActivity hoàn thành, nó gửi phản hồi lại.
    // (Nếu bạn đã start nó bằng cách sử dụng startActivityForResult()  )
    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        if (resultCode == Activity.RESULT_OK && requestCode == MY_REQUEST_CODE) {
            boolean needRefresh = data.getBooleanExtra("needRefresh", true);
            // Refresh ListView
            if (needRefresh) {
                this.ListPlaylist.clear();
                MyDatabaseHelper db = new MyDatabaseHelper(this);
                this.ListPlaylist = db.getAllPlaylists();
                // Thông báo dữ liệu thay đổi (Để refresh ListView).
                this.listViewAdapter.notifyDataSetChanged();
            }
        }
    }
}
