package com.nhom6.mediaplayer.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.nhom6.mediaplayer.Database.MyDatabaseHelper;
import com.nhom6.mediaplayer.Manager.SongManager;
import com.nhom6.mediaplayer.R;
import com.nhom6.mediaplayer.activity.PlayActivity;
import com.nhom6.mediaplayer.adapter.ListSongAdapter;
import com.nhom6.mediaplayer.model.Song;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListPlayingSongFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListPlayingSongFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListPlayingSongFragment extends Fragment {

    private SwipeMenuListView listView;
    SongManager songsManager = new SongManager();
    public ArrayList<Song> _songs = new ArrayList<Song>();
    ArrayList<Integer> lstIDSong = new ArrayList<Integer>();

    PlayActivity activity ;






    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ListPlayingSongFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListPlayingSongFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListPlayingSongFragment newInstance(String param1, String param2) {
        ListPlayingSongFragment fragment = new ListPlayingSongFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview =  inflater.inflate(R.layout.fragment_list_playing_song, container, false);
        listView = rootview.findViewById(R.id.listPlaying);

        //
        MyDatabaseHelper db=new MyDatabaseHelper(getContext());
        //Kiểm tra xem trong csdl bảng song đã có dữ liệu chưa?

        lstIDSong = getArguments().getIntegerArrayList("listID");

        for (Integer item : lstIDSong ) {
            Song newsong = db.GetInfoSong(item);
            _songs.add(newsong);
        }



        //đưa vào adapter để hiển thị
/*        ListSongAdapter listSongAdapter = new ListSongAdapter(context,R.layout.row_item_song,_songs);
        listView.setAdapter(listSongAdapter);*/

        ListSongAdapter listSongAdapter = new ListSongAdapter(getActivity(),_songs);
        listView.setAdapter(listSongAdapter);
        setSwipeListView();
        ClickItem();

        //
        activity = (PlayActivity) getActivity();

        //



        return rootview;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private void setSwipeListView() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "add playlist" item
                SwipeMenuItem plusItem = new SwipeMenuItem(getContext());
                // set item background
                plusItem.setBackground(R.color.greenic);
                // set item width
                plusItem.setWidth(100);
                // set a icon
                plusItem.setIcon(R.drawable.ic_add);
                // add to menu
                menu.addMenuItem(plusItem);

                SwipeMenuItem loveItem = new SwipeMenuItem(getContext());
                // set item background
                loveItem.setBackground(R.color.pinkwhite);
                // set item width
                loveItem.setWidth(100);
                // set a icon
                loveItem.setIcon(R.drawable.ic_love);
                // add to menu
                menu.addMenuItem(loveItem);
            }
        };
        // set creator
        listView.setMenuCreator(creator);
        ClickItemSong();
    }
    public void ClickItemSong(){
//        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
////            @SuppressLint("WrongConstant")
//            @Override
//            public boolean onMenuItemClick(final int position, SwipeMenu menu, final int index) {
//                switch (index) {
//                    case 0://chọn chức năng thêm bài hát vào playlist
//                        final Dialog dialogAdd = new Dialog(getContext());
//                        dialogAdd.requestWindowFeature(Window.FEATURE_CONTEXT_MENU);
//                        //load playlist len dialog
//                        LayoutInflater inflaterDia = getLayoutInflater();
//                        View mView = inflaterDia.inflate(R.layout.dialog_addplaylist, null);
//                        listViewAdd = mView.findViewById(R.id.listDialogPL);
//                        //tiến hành lấy toàn bộ playlist trong máy
//                        _playlists = playlistsManager.loadPlayList(context);
//                        //đưa vào adapter để hiển thị
//                        PlaylistAdapter listPlayListVAdapter = new PlaylistAdapter(context, R.layout.row_item_playlist_view, _playlists);
//                        listViewAdd.setAdapter(listPlayListVAdapter);
//                        listViewAdd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            //lưu bài hát vào khi chọn playlist đã có
//                            public void onItemClick(AdapterView<?> parent, View view, int position1, long id) {
//                                MyDatabaseHelper db= new MyDatabaseHelper(context);
//                                db.addSongForPlayList(_playlists.get(position1).getIDPlayList(),_songs.get(position).getSongid());
//                                Toast.makeText(getApplicationContext(),
//                                        "Thêm bài hát vào PlayList thành công..!", Toast.LENGTH_SHORT).show();
//                                dialogAdd.cancel();
//                            }
//                        });
//                        dialogAdd.setContentView(mView);
//                        dialogAdd.setCancelable(true);
//                        Window window=dialogAdd.getWindow();
//                        window.setGravity(Gravity.CENTER);
//                        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//                        window.setBackgroundDrawableResource(R.drawable.borderradius);
//                        dialogAdd.show();
//                        buttonCreatePlaylist = mView.findViewById(R.id.btnCreatePlayList);
//
//                        //
//                        CreatePlaylist(position,dialogAdd);
//                        break;
//                    case 1://thêm zô danh sách bài hát yêu thích
//                        MyDatabaseHelper db=new MyDatabaseHelper(context);
//                        db.AddSongFavorite(_songs.get(position).getSongid());
//                        Toast.makeText(getApplicationContext(),
//                                "Đã thêm vào Yêu Thích...", 50).show();
//                        break;
//                }
//                // false : close the menu; true : not close the menu
//                return false;
//            }
//        });
    }
    public void ClickItem()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "hahaha" + position, Toast.LENGTH_SHORT).show();

            }
        });
    }
}
