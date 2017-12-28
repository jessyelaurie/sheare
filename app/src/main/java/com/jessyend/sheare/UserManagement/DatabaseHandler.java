package com.jessyend.sheare.UserManagement;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.common.base.Joiner;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jessyend.sheare.HomeView.HomeActivity;
import com.jessyend.sheare.UserSearchView.User;

import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.AudioFeaturesTrack;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.Track;

/**
 * The DatabaseHandler interacts with the Database to add new users, new playlists and new tracks.
 * @author Jessye Nana Davies <nanadav2@illinois.edu>
 */
public class DatabaseHandler {

    private FirebaseDatabase mFirebaseDatabase;
    private static FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static DatabaseReference myRef;

    /**
     * Initializes a database handler
     */
    public DatabaseHandler() {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                } else {
                }
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object value = dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    /**
     * Adds a new track to the database
     * @param track the track to add
     * @param playlistId the id of the playlist holding the track
     */
    public static void addTrackToDatabase(Track track, String playlistId, String currCount,
                                          AudioFeaturesTrack trackFeatures) {
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();

        // incrementing the count of tracks added to the playlist for the user
        // currCount is null when the owner is updating the Spotify playlist from collaborators
        // new tracks additions.
        if(currCount!=null) {
            int count = Integer.parseInt(currCount);
            count = count+1;
            // Incrementing the track count for the user
            myRef.child("Playlists").child(playlistId).child("Users").child(userId).
                    setValue(String.valueOf(count));
        }

        String trackId = track.id;
        String title = track.name;
        List<String> names = new ArrayList<>();
        for (ArtistSimple i : track.artists) {
            names.add(i.name);
        }
        Joiner joiner = Joiner.on(", ");
        String subtitle = joiner.join(names);
        String imageUrl = track.album.images.get(0).url;
        String trackUri = track.preview_url;

        // Setting the basic track properties
        myRef.child("Playlists").child(playlistId).child("Tracks").child(trackId).
                child("title").setValue(title);
        myRef.child("Playlists").child(playlistId).child("Tracks").child(trackId).
                child("subtitle").setValue(subtitle);
        myRef.child("Playlists").child(playlistId).child("Tracks").child(trackId).
                child("imageUrl").setValue(imageUrl);
        myRef.child("Playlists").child(playlistId).child("Tracks").child(trackId).
                child("uri").setValue(trackUri);

        // Setting track features for visualizations
        if(trackFeatures != null) {
            myRef.child("Playlists").child(playlistId).child("Tracks").child(trackId).
                    child("energy").setValue(trackFeatures.energy);
            myRef.child("Playlists").child(playlistId).child("Tracks").child(trackId).
                    child("danceability").setValue(trackFeatures.danceability);
            myRef.child("Playlists").child(playlistId).child("Tracks").child(trackId).
                    child("loudness").setValue(trackFeatures.loudness);
        }
        else {
            myRef.child("Playlists").child(playlistId).child("Tracks").child(trackId).
                    child("energy").setValue("0");
            myRef.child("Playlists").child(playlistId).child("Tracks").child(trackId).
                    child("danceability").setValue("0");
            myRef.child("Playlists").child(playlistId).child("Tracks").child(trackId).
                    child("loudness").setValue("0");
        }
    }

    /**
     * Adds the newly created playlist to the database and to the user's playlist
     * @param playlist to add to the database
     * @param token the user's token
     * @param owner the playlist's owner
     */
    public static void addPlaylistToDatabase(Playlist playlist, String token, String owner) {
        String playlistId = playlist.id;
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        myRef.child("Users").child(userId).child("Playlists").child(playlistId).setValue(playlist.name);
        myRef.child("Playlists").child(playlistId).child("Users").child(userId).setValue(true);
        myRef.child("Playlists").child(playlistId).child("name").setValue(playlist.name);
    }


    /**
     * Add a new user that created an account to the database
     * @param mToken the user's token
     */
    public static void addUserToDatabase(String mToken) {
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(mToken);
        SpotifyService spotify = api.getService();
        String defaultProfile = "https://www.jetphotos.com/assets/img/user.png";
        myRef.child("Users").child(userId).child("profileUrl").setValue(defaultProfile);
    }

    /**
     * Add a new user that created an account to the database
     * @param user the user to be added to collaborators
     * @param playlistId the playlist's unique id
     * @param playlistName the playlist's name
     */
    public static void addUserToPlaylist(User user, String playlistId, String playlistName) {
        myRef.child("Playlists").child(playlistId).child("Users").child(user.getUserNum()).setValue("0");
        myRef.child("Users").child(user.getUserNum()).child("Playlists").child(playlistId).setValue(playlistName);
    }

    /**
     * Synchronizes Spotify with the database
     * @param token the unique token
     * @param c the context
     * @param databasePlaylists the playlists
     */
    public static void synchronizeSpotifyWithDatabase(String token, Context c, Map<String,
            ArrayList<String>>  databasePlaylists) throws JSONException, IOException, URISyntaxException {

        SynchronizationRequest sr = new SynchronizationRequest();
        for ( String key : databasePlaylists.keySet() ) {
            sr.getAllTracks(token, key, c, databasePlaylists.get(key));
        }
    }

    public static void synchronize(ArrayList<Track> spotifyTracks, ArrayList<String> dbTracks,
                                   String userId, String playlistId, String token, Context c)
            throws JSONException, IOException, URISyntaxException {
        for(String trackId : dbTracks) {
            boolean trackFound = false;
            for(Track t : spotifyTracks) {
                if(t.id.equals(trackId)) {
                    trackFound = true;
                }
            }
            if(!trackFound) {
                Request request = new Request();
                request.addNewTrackFromId(c, token, trackId, playlistId);
            }
        }
    }
}
