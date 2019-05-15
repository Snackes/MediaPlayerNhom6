package com.nhom6.mediaplayer.service;

import android.content.Context;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class MediaTextView extends AppCompatTextView {


    private MediaControllerCompat mMediaController;
    private ControllerCallback mControllerCallback;


    public MediaTextView(Context context) {
        super(context);
    }

    public MediaTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MediaTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void getDuration(String duration)
    {
        setText(duration);
    }
    public void getCurrent(String curr)
    {
        setText(curr);
    }
    public void setMediaController(final MediaControllerCompat mediaController) {
        if (mediaController != null) {
            mControllerCallback = new ControllerCallback();
            mediaController.registerCallback(mControllerCallback);
        } else if (mMediaController != null) {
            mMediaController.unregisterCallback(mControllerCallback);
            mControllerCallback = null;
        }
        mMediaController = mediaController;
    }

    public void disconnectController() {
        if (mMediaController != null) {
            mMediaController.unregisterCallback(mControllerCallback);
            mControllerCallback = null;
            mMediaController = null;
        }
    }
    private class ControllerCallback extends MediaControllerCompat.Callback

    {

    }

}
