package com.nhom6.mediaplayer.model;

import com.nhom6.mediaplayer.R;

public enum ModelObject {

    Play(R.string.red, R.layout.activity_play),
    DSPhat(R.string.blue, R.layout.activity_same_song);
    private int mTitleResId;
    private int mLayoutResId;

    ModelObject(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}
