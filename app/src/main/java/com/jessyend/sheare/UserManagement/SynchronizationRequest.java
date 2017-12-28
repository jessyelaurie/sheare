package com.jessyend.sheare.UserManagement;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.AudioFeaturesTrack;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by jessyend on 12/12/17.
 */

public class SynchronizationRequest {

    public static void getTracksForPlaylist(final String token, final String playlistId,
                                            final String userId, final Context c,
                                            final ArrayList<String>  DBtracks)
            throws IOException, URISyntaxException, JSONException {
        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(token);
        SpotifyService spotify = api.getService();

        spotify.getPlaylistTracks(userId, playlistId, new Callback<Pager<PlaylistTrack>>() {
            @Override
            public void success(Pager<PlaylistTrack> tracks, retrofit.client.Response response) {
                ArrayList<Track> mTracks = new ArrayList<Track>();
                for(int i = 0; i < tracks.items.size(); i++) {
                    PlaylistTrack pt = tracks.items.get(i);
                    mTracks.add(pt.track);
                }
                DatabaseHandler dbh = new DatabaseHandler();
                try {
                    dbh.synchronize(mTracks, DBtracks, userId, playlistId, token, c);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    public void getAllTracks(final String token, final String playlistId, final Context c,
                             final ArrayList<String>  DBtracks)
            throws IOException, URISyntaxException, JSONException {
        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(token);
        SpotifyService spotify = api.getService();

        spotify.getMe(new Callback<UserPrivate> () {
            @Override
            public void success(UserPrivate user, retrofit.client.Response response) {
                try {
                    try {
                        SynchronizationRequest.getTracksForPlaylist(token, playlistId, user.id, c,
                                DBtracks);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Playlist failure", error.toString());
            }
        });
    }
}
