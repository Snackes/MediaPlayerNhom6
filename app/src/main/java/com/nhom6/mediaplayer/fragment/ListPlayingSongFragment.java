package com.nhom6.mediaplayer.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.nhom6.mediaplayer.Database.MyDatabaseHelper;
import com.nhom6.mediaplayer.Manager.SongManager;
import com.nhom6.mediaplayer.R;
import com.nhom6.mediaplayer.activity.PlayActivity;
import com.nhom6.mediaplayer.adapter.ListSongAdapter;
import com.nhom6.mediaplayer.model.Song;
import com.nhom6.mediaplayer.service.MediaBrowserHelper;

import java.util.ArrayList;

import static android.support.v4.media.session.MediaControllerCompat.getMediaController;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListPlayingSongFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListPlayingSongFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListPlayingSongFragment extends Fragment implements SearchView.OnQueryTextListener {

    private SwipeMenuListView listView;
    SongManager songsManager = new SongManager();
    public ArrayList<Song> _songs = new ArrayList<Song>();
    ArrayList<Integer> lstIDSong = new ArrayList<Integer>();

    PlayActivity activity;

    //Khái báo nhận dữ liệu từ playactivity để search
    private static Integer test;
    private static Integer idObject;

    private SearchView searchView;


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
        View rootview = inflater.inflate(R.layout.fragment_list_playing_song, container, false);
        listView = rootview.findViewById(R.id.listPlaying);

        //
        MyDatabaseHelper db = new MyDatabaseHelper(getContext());
        //Kiểm tra xem trong csdl bảng song đã có dữ liệu chưa?

        lstIDSong = getArguments().getIntegerArrayList("listID");
        test = getArguments().getInt("test");
        idObject = getArguments().getInt("IDObject");


        for (Integer item : lstIDSong) {
            Song newsong = db.GetInfoSong(item);
            _songs.add(newsong);
        }


        //đưa vào adapter để hiển thị
/*        ListSongAdapter listSongAdapter = new ListSongAdapter(context,R.layout.row_item_song,_songs);
        listView.setAdapter(listSongAdapter);*/

        ListSongAdapter listSongAdapter = new ListSongAdapter(getActivity(), _songs);
        listView.setAdapter(listSongAdapter);
        searchView = rootview.findViewById(R.id.searchView);
        ClickItem();
        searchView.setOnQueryTextListener(this);
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

    public void ClickItem() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getContext(), "hahaha" + position, Toast.LENGTH_SHORT).show();
                MediaControllerCompat mediaController = MediaBrowserHelper.getMediaController();
                mediaController.getTransportControls().prepareFromMediaId(String.valueOf(position), null);
                // playsong
                mediaController.getTransportControls().play();
            }
        });
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        MyDatabaseHelper db = new MyDatabaseHelper(getContext());
        _songs = db.SearchSong(text,idObject,test);
        ListSongAdapter listSongAdapter = new ListSongAdapter(getActivity(), _songs);
        listView.setAdapter(listSongAdapter);
        ClickItem();
        return false;
    }

}
