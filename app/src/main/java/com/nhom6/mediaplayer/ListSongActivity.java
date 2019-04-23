package com.nhom6.mediaplayer;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.nhom6.mediaplayer.Database.MyDatabaseHelper;
import com.nhom6.mediaplayer.Manager.SongManager;
import com.nhom6.mediaplayer.model.Playlist;
import com.nhom6.mediaplayer.model.Song;

import java.util.ArrayList;


public class ListSongActivity extends AppCompatActivity {

    Playlist playlist;
    private static final int MODE_CREATE = 1;
    private static final int MODE_EDIT = 2;
    private ArrayAdapter<String> listViewAdapter;
    private int mode;
    private EditText textTitle;
    private ListView ListSong;

    private boolean needRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_song);

        this.textTitle = this.findViewById(R.id.Text_Playlist_Title);
        this.ListSong = this.findViewById(R.id.Listview_Song);
        Intent intent = this.getIntent();
        this.playlist = (Playlist) intent.getSerializableExtra("playlist");
        if(playlist== null)  {
            this.mode = MODE_CREATE;
        } else  {
            this.mode = MODE_EDIT;
            this.textTitle.setText(playlist.getTitle());

            MyDatabaseHelper db = new MyDatabaseHelper(this);
            ArrayList<String> listSong=new ArrayList<String>();//tạo mới 1 list chứa các song
            SongManager sm=new SongManager();
            ArrayList<String> temp= db.GetListSongInPlaylist(this.playlist.getIDPlaylist());
            for(int i=0; i<temp.size();i++){
                Song song = sm.loadSongWithID(this,Integer.parseInt(temp.get(i)));
                listSong.add(song.getSongname());
            }

            this.listViewAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1,listSong);

            // Đăng ký Adapter cho ListView.
            this.ListSong.setAdapter(this.listViewAdapter);

            // Đăng ký Context menu cho ListView.
            registerForContextMenu(this.ListSong);
        }

    }


    // Người dùng Click vào nút Save.
    public void buttonSaveClicked(View view)  {
        MyDatabaseHelper db = new MyDatabaseHelper(this);

        String title = this.textTitle.getText().toString();
        //String content = this.textContent.getText().toString();

        if(title.equals("") /*|| content.equals("")*/) {
            Toast.makeText(getApplicationContext(),
                    "Please enter title & content", Toast.LENGTH_LONG).show();
            return;
        }

        if(mode==MODE_CREATE ) {
            this.playlist= new Playlist(title,7);
            db.addPlaylist(playlist);
        } else  {
/*            this.note.setNoteTitle(title);
            this.note.setNoteContent(content);
            db.updateNote(note);*/
        }

        this.needRefresh = true;
        // Trở lại MainActivity.
        this.onBackPressed();
    }

    // Khi người dùng Click vào button Cancel.
    public void buttonCancelClicked(View view)  {
        // Không làm gì, trở về MainActivity.
        this.onBackPressed();
    }

    // Khi Activity này hoàn thành,
    // có thể cần gửi phản hồi gì đó về cho Activity đã gọi nó.
    @Override
    public void finish() {

        // Chuẩn bị dữ liệu Intent.
        Intent data = new Intent();
        // Yêu cầu MainActivity refresh lại ListView hoặc không.
        data.putExtra("needRefresh", needRefresh);

        // Activity đã hoàn thành OK, trả về dữ liệu.
        this.setResult(Activity.RESULT_OK, data);
        super.finish();
    }

}
