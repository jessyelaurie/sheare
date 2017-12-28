package com.jessyend.sheare.PlaylistView;

import android.support.annotation.Nullable;

public interface Player {

    void play(String url);

    void pause();

    void resume();

    boolean isPlaying();

    @Nullable
    String getCurrentTrack();

    void release();
}
