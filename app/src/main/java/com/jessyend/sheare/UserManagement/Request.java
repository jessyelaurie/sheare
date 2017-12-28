package com.jessyend.sheare.UserManagement;

import android.app.Activity;
import android.content.Context;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.jessyend.sheare.UserSearchView.User;

import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.AudioFeaturesTrack;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;

import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Result;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.UserPrivate;
import retrofit.Callback;

import retrofit.RetrofitError;

/**
 * A URLHelper recovers the Strings (profile and repositories) from the GitHub API
 * @author Jessye Nana Davies <nanadav2@illinois.edu>
 */
public class Request {
    /**
     * creates a playlist with a POST request to the Spotify API
     * @param token the unique token
     * @param playlistName the name for the new playlist
     * @param spotifyId the user's unique spotify id
     * @throws IOException
     * @throws URISyntaxException
     */
    public static void createPlaylist(final String token, final String playlistName, final String spotifyId) throws IOException, URISyntaxException {
        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(token);
        final SpotifyService spotify = api.getService();
        Map<String, Object> options = new HashMap<>();
        options.put("name", playlistName);
        options.put("public", false);
        options.put("collaborative", true);
        options.put("description", "Collaborative playlist for Sheare App");

        spotify.createPlaylist(spotifyId, options, new Callback<Playlist>() {
            @Override
            public void success(Playlist playlist, retrofit.client.Response response) {
                Log.d("Playlist success", playlist.name);
                DatabaseHandler databaseHandler = new DatabaseHandler();
                databaseHandler.addPlaylistToDatabase(playlist, token, spotifyId);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Playlist failure", error.toString());
            }
        });
    }

    /**
     * creates a playlist with a POST request to the Spotify API
     * @param token the token
     * @param trackUri the trackUri
     * @param playlistId the Spotify playlist id
     * @param track to be added
     * @throws IOException
     * @throws URISyntaxException
     */
    public static void addTrack(final String token, final String trackUri, final String playlistId,
                                final Track track, final String spotifyId, final String currCount)
            throws IOException, URISyntaxException, JSONException {
        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(token);
        SpotifyService spotify = api.getService();

        Map<String, Object> options = new HashMap<>();
        options.put("uris", trackUri);
        Map<String, Object> body = new HashMap<>();

        spotify.addTracksToPlaylist(spotifyId, playlistId, options, body, new Callback<Pager<PlaylistTrack>> () {
            @Override
            public void success(Pager<PlaylistTrack> playlistTrack, retrofit.client.Response response) {
                try {
                    Request.getTrackProperties(token, trackUri, playlistId, track, spotifyId, currCount);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Playlist failure", error.toString());
            }
        });
    }

    /**
     * First step to adding a new track;
     * Gets the spotify user id from the API
     * @param token the token
     * @param trackUri the trackUri
     * @param playlistId the Spotify playlist id
     * @param track to be added
     * @throws IOException
     * @throws URISyntaxException
     * @throws JSONException
     */
    public static void addNewTrack(final Context c, final String token, final String trackUri,
                            final String playlistId, final Track track,
                            final String currCount)
            throws IOException, URISyntaxException, JSONException {
        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(token);
        SpotifyService spotify = api.getService();

        spotify.getMe(new Callback<UserPrivate> () {
            @Override
            public void success(UserPrivate user, retrofit.client.Response response) {
                try {
                    Request.followPlaylist(c, token, playlistId, user.id, trackUri, track, currCount);
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

    /**
     * First step to creating a new playlist;
     * Gets the spotify user id from the API
     * @param token the unique token
     * @param playlistName the name for the new playlist
     * @throws IOException
     * @throws URISyntaxException
     * @throws JSONException
     */
    public void createNewPlaylist(final String token, final String playlistName) throws IOException, URISyntaxException, JSONException {
        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(token);
        SpotifyService spotify = api.getService();

        spotify.getMe(new Callback<UserPrivate>() {
            @Override
            public void success(UserPrivate user, retrofit.client.Response response) {
                try {
                    Request.createPlaylist(token, playlistName, user.id);
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

        /**
         * First step to creating a new playlist;
         * Gets the spotify user id from the API
         * @param token the unique token
         * @param a the Activity login
         * @throws IOException
         * @throws URISyntaxException
         * @throws JSONException
         */
    public void getCurrentSpotifyUser(final String token, final Activity a) throws IOException, URISyntaxException, JSONException {
        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(token);
        SpotifyService spotify = api.getService();
        spotify.getMe(new Callback<UserPrivate> () {
            @Override
            public void success(UserPrivate user, retrofit.client.Response response) {
                Toast.makeText(a, "Spotify account: "+ user.id, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Account failure", error.toString());
            }
        });
    }



    /**
     * follow a playlist with a POST request to the Spotify API
     * @param token the unique token
     * @param spotifyId the user's unique spotify id
     * @throws IOException
     * @throws URISyntaxException
     */
    public static void followPlaylist(final Context c, final String token, final String playlistId,
                                      final String spotifyId,
                                      final String trackUri, final Track track,
                                      final String currCount)
            throws IOException, URISyntaxException {
        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(token);
        SpotifyService spotify = api.getService();

        spotify.followPlaylist(spotifyId, playlistId, new Callback<Result>() {
            @Override
            public void success(Result r, retrofit.client.Response response) {
                try {
                    Request.addTrack(token, trackUri, playlistId, track, spotifyId, currCount);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                try {
                    Request.getTrackProperties(token, trackUri, playlistId, track, spotifyId, currCount);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Sends the tracks properties to the database handler for visualizations
     * @param token the unique token
     * @param trackUri the trackUri for the track player
     * @param playlistId the playlist Spotify Id
     * @param track the track that was added
     * @param spotifyId the Spotify username
     * @param currCount the count for the user adding a track
     * @throws IOException
     * @throws URISyntaxException
     * @throws JSONException
     */
    public static void getTrackProperties(String token, final String trackUri,
                                          final String playlistId, final Track track,
                                          String spotifyId, final String currCount)
            throws IOException, URISyntaxException, JSONException {
        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(token);
        SpotifyService spotify = api.getService();
        spotify.getTrackAudioFeatures(track.id, new Callback<AudioFeaturesTrack> () {
            @Override
            public void success(AudioFeaturesTrack playlistTrack, retrofit.client.Response response) {
                DatabaseHandler databaseHandler = new DatabaseHandler();
                databaseHandler.addTrackToDatabase(track, playlistId, currCount, playlistTrack);
            }

            @Override
            public void failure(RetrofitError error) {
                // the analysis was not found
                DatabaseHandler databaseHandler = new DatabaseHandler();
                databaseHandler.addTrackToDatabase(track, playlistId, currCount, null);
            }
        });
    }


    /**
     * Add a new track starting from the Spotify TrackId
     * @param c the Application context
     * @param token the unique token
     * @param trackId the trackId
     * @param playlistId the playlistId
     * @throws IOException
     * @throws URISyntaxException
     * @throws JSONException
     */
    public static void addNewTrackFromId(final Context c, final String token, final String trackId,
                                  final String playlistId)
            throws IOException, URISyntaxException, JSONException {
        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(token);
        SpotifyService spotify = api.getService();

        spotify.getTrack(trackId, new Callback<Track> () {
            @Override
            public void success(Track track, retrofit.client.Response response) {
                try {
                    try {
                        Request.addNewTrack(c, token, track.preview_url, playlistId, track, null);
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