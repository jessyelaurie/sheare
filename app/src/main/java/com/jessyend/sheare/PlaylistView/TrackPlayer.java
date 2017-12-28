package com.jessyend.sheare.PlaylistView;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.content.Context;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;

/**
 * The TrackPlayer plays tracks on click.
 * @author Jessye Nana Davies <nanadav2@illinois.edu>
 */
public class TrackPlayer {
    private Context mContext;
    private Player mPlayer;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlayer = ((PlayerService.PlayerBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mPlayer = null;
        }
    };
    /**
     * Initializes the Track player
     * @param c the Playlist activity's context
     */
    public TrackPlayer(Context c, final String previewUrl) {
        mContext = c;
        mContext.bindService(PlayerService.getIntent(mContext), mServiceConnection,
                Activity.BIND_AUTO_CREATE);
    }

    /**
     * Plays the track
     * @param previewUrl spotify Uri for the clicked Track
     */
    public void playTrack(String previewUrl) {
        if (previewUrl == null) {
            Toast.makeText(mContext, "This track doesn't have a preview.", Toast.LENGTH_LONG).show();
            return;
        }

        if (mPlayer == null) return;

        String currentTrackUrl = mPlayer.getCurrentTrack();

        if (currentTrackUrl == null || !currentTrackUrl.equals(previewUrl)) {
            mPlayer.play(previewUrl);
        } else if (mPlayer.isPlaying()) {
            mPlayer.pause();
        } else {
            mPlayer.resume();
        }
    }

}
