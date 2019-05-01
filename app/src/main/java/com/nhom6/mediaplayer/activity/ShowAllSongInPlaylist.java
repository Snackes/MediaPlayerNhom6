package com.nhom6.mediaplayer.activity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.nhom6.mediaplayer.Database.MyDatabaseHelper;
import com.nhom6.mediaplayer.MainActivity;
import com.nhom6.mediaplayer.Manager.SongManager;
import com.nhom6.mediaplayer.R;
import com.nhom6.mediaplayer.adapter.ListSongAdapter;
import com.nhom6.mediaplayer.model.Playlist;
import com.nhom6.mediaplayer.model.Song;

import java.util.ArrayList;


public class ShowAllSongInPlaylist extends AppCompatActivity {

    Playlist playlist;
    private static final int MODE_CREATE = 1;
    private static final int MODE_EDIT = 2;
    private ArrayAdapter<String> listViewAdapter;
    private int mode;
    private EditText textTitle;
    private ListView ListSong;
    Context context;
    private boolean needRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_song_in_playlist);

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

            SongManager sm=new SongManager();
            ArrayList<String> listIDsong= db.GetListSongInPlaylist(this.playlist.getIDPlaylist());
            if(listIDsong.size()==0){
                Toast.makeText(getApplicationContext(),
                        "Méo có gì trong này", Toast.LENGTH_LONG).show();
                // Trở lại MainActivity.
                this.onBackPressed();
            }
            ArrayList<Song> _songs = new ArrayList<Song>();
            for(int i=0; i<listIDsong.size();i++){
                Song song = sm.loadSongWithID(this,Integer.parseInt(listIDsong.get(i)));
                _songs.add(song);
            }

            //đưa vào adapter để hiển thị
            ListSongAdapter listSongAdapter = new ListSongAdapter(this,R.layout.row_item_song,_songs);
            ListSong.setAdapter(listSongAdapter);
            registerForContextMenu(this.ListSong);
            context = getApplicationContext();
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
            db.addPlaylist(title);
        } else  {
/*            this.note.setNoteTitle(title);
            this.note.setNoteContent(content);
            db.updateNote(note);*/
        }

        this.needRefresh = true;
        //Trở lại MainActivity.
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


