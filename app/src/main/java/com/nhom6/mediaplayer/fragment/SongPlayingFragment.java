package com.nhom6.mediaplayer.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nhom6.mediaplayer.Database.MyDatabaseHelper;
import com.nhom6.mediaplayer.R;
import com.nhom6.mediaplayer.service.MediaSeekBar;
import com.nhom6.mediaplayer.service.MediaTextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SongPlayingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SongPlayingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SongPlayingFragment extends Fragment {
    //view
    CircleImageView imgSong;
    TextView titleSong;
    TextView artistSong;

    
    MediaTextView playingTime;
    MediaTextView totalTime;




    MediaSeekBar seekBar;
    
    ImageButton btn_love;
    Integer songID;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SongPlayingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SongPlayingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SongPlayingFragment newInstance(String param1, String param2) {
        SongPlayingFragment fragment = new SongPlayingFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_song_playing, container, false);
        //set animation
        TextView txtSongTil = rootView.findViewById(R.id.songTitle);
        TranslateAnimation animation = new TranslateAnimation(1000.0f, -1000.0f, 0.0f, 0.0f); // new TranslateAnimation (float fromXDelta,float toXDelta, float fromYDelta, float toYDelta)

        animation.setDuration(7000); // animation duration
        animation.setRepeatCount(100); // animation repeat count
        animation.setFillAfter(false);
        txtSongTil.startAnimation(animation);//your_view for mine is imageView

        //init view
        imgSong = rootView.findViewById(R.id.imgMusic);
        titleSong = rootView.findViewById(R.id.songTitle);
        artistSong = rootView.findViewById(R.id.artist);
        //
        seekBar = new MediaSeekBar(getContext());
        seekBar = rootView.findViewById(R.id.seekBar);

        playingTime = rootView.findViewById(R.id.playingTime);
        totalTime = rootView.findViewById(R.id.totalTime);

        btn_love=rootView.findViewById(R.id.btnLove);
        //set màu đen thành trắng -- lấy icon màu đen chi zậy ??? dcm!!
        btn_love.setColorFilter(ContextCompat.getColor(getContext(),R.color.pinkwhite));

        //
        if (BitmapFactory.decodeFile(getArguments().getString("Image")) != null) {

            imgSong.setImageBitmap(BitmapFactory.decodeFile(getArguments().getString("Image")));
        } else {
            imgSong.setImageResource(R.drawable.adele);
        }
        titleSong.setText(getArguments().getString("Title"));
        artistSong.setText(getArguments().getString("Artist"));
        songID = getArguments().getInt("SongID");
        ChangeIcon();
        //
        // Inflate the layout for this fragment
        return rootView;


    }

    public void ChangeIcon() {
        int check = 0;
        MyDatabaseHelper db = new MyDatabaseHelper(getContext());
        check = db.CheckSongFavorite(songID);
        if (check == 1) {
            btn_love.setImageResource(R.drawable.ic_loved);
        } else {
            btn_love.setImageResource(R.drawable.ic_love);
        }
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

    public void ChangeTitleSong(String title)
    {
        titleSong.setText(title);
    }
    public void ChangeArtistSong(String artist)
    {
        artistSong.setText(artist);
    }
    public void ChangeImg(Bitmap bm)
    {
        if(bm != null)
        {
            imgSong.setImageBitmap(bm);
        }
        else{
            imgSong.setImageResource(R.drawable.adele);
        }

    }

    public MediaSeekBar getSeekBar() {
        return seekBar;
    }

    public void setSeekBar(MediaSeekBar seekBar) {
        this.seekBar = seekBar;
    }
    public void ChangeTotalTime(long totalTime )
    {
        this.totalTime.setText("" + totalTime);
    }
    public void ChangePlayingTime(long playingTime)
    {
        this.playingTime.setText("" + playingTime);
    }

    public MediaTextView getPlayingTime() {
        return playingTime;
    }

    public void setPlayingTime(MediaTextView playingTime) {
        this.playingTime = playingTime;
    }

    public MediaTextView getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(MediaTextView totalTime) {
        this.totalTime = totalTime;
    }
}
