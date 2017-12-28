package com.jessyend.sheare.PlaylistView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jessyend.sheare.R;
import com.jessyend.sheare.SearchView.SearchActivity;
import com.jessyend.sheare.UserSearchView.UserSearchActivity;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Float.parseFloat;

/**
 * The About Fragment presents the playlist and provides option for adding a track and a collaborator
 * @author Jessye Nana Davies <nanadav2@illinois.edu>
 */
public class AboutFragment extends Fragment {
    private static final String TAG = "AboutFragment";

    private Button addTrack, addCollaborator;
    private ImageButton graph1Button, graph2Button, graph3Button,graph4Button;
    private String mToken;
    private String mPlaylistId;
    private String mPlaylistName;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private String userID;


    private ArrayList<String> mUserIds;
    private ArrayList<String> mUserUrls;
    private ArrayList<String> mUserNums;


    private ArrayList<String> mTracks;
    private ArrayList<Double> mTrackDanceabilities;
    private ArrayList<Double> mTrackEnergy;
    private ArrayList<Double> mTrackLoudness;
    private ArrayList<String> mCollaborators;
    private ArrayList<String> mCounts;

    private String mCurrCount;


    /**
     * @param inflater the inflater for the view
     * @param container the fragment's container
     * @param savedInstanceState bundle containing the token and playlist iD
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(com.jessyend.sheare.R.layout.about_fragment,container,false);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                } else {
                }
            }
        };

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                } else {
                }
            }
        };

        // This updates the playlists shown on changes in the database.
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    updateData(dataSnapshot);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        addTrack = (Button) view.findViewById(com.jessyend.sheare.R.id.addtrack);
        mToken = getArguments().getString("token");
        mPlaylistId = getArguments().getString("id");

        addTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Searching a track",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("token", mToken);
                intent.putExtra("id", mPlaylistId);
                intent.putExtra("count", mCurrCount);
                startActivity(intent);
            }
        });

        addCollaborator = (Button) view.findViewById(com.jessyend.sheare.R.id.addcollaborator);
        addCollaborator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Searching a user",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), UserSearchActivity.class);
                intent.putExtra("token", mToken);
                intent.putExtra("id", mPlaylistId);
                intent.putExtra("name", mPlaylistName);

                Bundle extra = new Bundle();
                extra.putSerializable("usersId", mUserIds);
                extra.putSerializable("usersUrl", mUserUrls);
                extra.putSerializable("usersNum", mUserNums);
                intent.putExtra("extra", extra);

                startActivity(intent);
            }
        });


        graph1Button = (ImageButton) view.findViewById(R.id.graph1button);
        graph1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PieActivity.class);
                Bundle extra = new Bundle();
                extra.putSerializable("users", mCollaborators);
                extra.putSerializable("count", mCounts);
                intent.putExtra("extra", extra);

                startActivity(intent);
            }
        });

        graph2Button = (ImageButton) view.findViewById(R.id.graph2button);
        graph2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GraphActivity.class);
                int[] positions = getTopThreePositions(mTrackEnergy);
                String[] tracks = getTopThreeTracks(positions, mTracks);
                float[] values = getTopThreeValues(positions, mTrackEnergy);
                intent.putExtra("title", mPlaylistName+": Most energetic Tracks");

                Bundle extra = new Bundle();
                extra.putSerializable("tracks", tracks);
                extra.putSerializable("values", values);
                intent.putExtra("extra", extra);

                startActivity(intent);
            }
        });

        graph3Button = (ImageButton) view.findViewById(R.id.graph3button);
        graph3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GraphActivity.class);
                int[] positions = getTopThreePositions(mTrackLoudness);
                String[] tracks = getTopThreeTracks(positions, mTracks);
                float[] values = getTopThreeValues(positions, mTrackLoudness);
                intent.putExtra("title", mPlaylistName+": Loudest Tracks (negative values)");

                Bundle extra = new Bundle();
                extra.putSerializable("tracks", tracks);
                extra.putSerializable("values", values);
                intent.putExtra("extra", extra);

                startActivity(intent);
            }
        });

        graph4Button = (ImageButton) view.findViewById(R.id.graph4button);
        graph4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GraphActivity.class);
                int[] positions = getTopThreePositions(mTrackDanceabilities);
                String[] tracks = getTopThreeTracks(positions, mTracks);
                float[] values = getTopThreeValues(positions, mTrackDanceabilities);
                intent.putExtra("title", mPlaylistName+": Most danceable Tracks");

                Bundle extra = new Bundle();
                extra.putSerializable("tracks", tracks);
                extra.putSerializable("values", values);
                intent.putExtra("extra", extra);

                startActivity(intent);
            }
        });

        return view;

    }

    /**
     * This displays the user's playlists on the Homepage
     * @param dataSnapshot the dataSnapshot received from the event listener.
     * @throws IOException
     * @throws URISyntaxException
     */
    private void updateData(DataSnapshot dataSnapshot) throws IOException, URISyntaxException {
        // Search user data
        mUserIds = new ArrayList<String>();
        mUserUrls = new ArrayList<String>();
        mUserNums = new ArrayList<String>();

        DataSnapshot users = dataSnapshot.child("Users");
        DataSnapshot playlists = dataSnapshot.child("Playlists");
        DataSnapshot playlist = playlists.child(mPlaylistId);

        for(DataSnapshot user : users.getChildren()) {
            mUserNums.add(user.getKey());
            mUserIds.add(user.child("spotifyId").getValue(String.class));
            mUserUrls.add(user.child("profileUrl").getValue(String.class));
        }
        mPlaylistName = playlist.child("name").getValue(String.class);
        mCurrCount = playlist.child("Users").child(userID).getValue(String.class);


        // Graphs data
        mTracks = new ArrayList<String>();
        mTrackDanceabilities = new ArrayList<Double>();
        mTrackEnergy = new ArrayList<Double>();
        mTrackLoudness = new ArrayList<Double>();

        mCollaborators = new ArrayList<String>();
        mCounts = new ArrayList<String>();

        DataSnapshot tracks = playlist.child("Tracks");
        DataSnapshot collaborators = playlist.child("Users");

        for(DataSnapshot track : tracks.getChildren()) {
            mTracks.add(track.child("title").getValue(String.class));
            mTrackDanceabilities.add(track.child("danceability").getValue(Double.class));
            mTrackEnergy.add(track.child("energy").getValue(Double.class));
            mTrackLoudness.add(track.child("loudness").getValue(Double.class));
        }

        for(DataSnapshot collaborator : collaborators.getChildren()) {
            mCounts.add(collaborator.getValue(String.class));
            String userId = collaborator.getKey();
            for(DataSnapshot user : users.getChildren()) {
                if(userId.equals(user.getKey())) {
                    mCollaborators.add(user.child("spotifyId").getValue(String.class));
                }
            }
        }
    }

    /**
     * Returns the top three highest values position in the ArrayList
     * @param values the values to compare
     * @return the positions
     */
    private int[] getTopThreePositions(ArrayList<Double> values) {
        int firstPosition = 0;
        float firstPositionVal = -1000;
        int secondPosition = 0;
        float secondPositionVal = -1000;
        int thirdPosition = 0;
        float thirdPositionVal = -1000;

        for(int i = 0; i<values.size(); i++) {
            if(values.get(i) > firstPositionVal) {
                firstPosition = i;
                firstPositionVal = Double.valueOf(values.get(i)).floatValue();
            }
            else if (values.get(i) > secondPositionVal) {
                secondPosition = i;
                secondPositionVal = Double.valueOf(values.get(i)).floatValue();
            }
            else if(values.get(i) > thirdPositionVal) {
                thirdPosition = i;
                thirdPositionVal = Double.valueOf(values.get(i)).floatValue();
            }
        }

        int[] positions = new int[3];
        positions[0] = firstPosition;
        positions[1] = secondPosition;
        positions[2] = thirdPosition;
        return positions;
    }

    /**
     * Gets the top three track titles for the positions
     * @param topPositions the positions
     * @param tracks the tracks
     * @return a String array
     */
    private String[] getTopThreeTracks(int[] topPositions, ArrayList<String> tracks) {
        String[] topTracks = new String[3];
        topTracks[0] = new String(tracks.get(topPositions[0]));
        topTracks[1] = new String(tracks.get(topPositions[1]));
        topTracks[2] = new String(tracks.get(topPositions[2]));
        return topTracks;
    }

    /**
     * Gets the top three track values for the positions
     * @param topPositions the positions
     * @param values the tracks
     * @return a float array
     */
    private float[] getTopThreeValues(int[] topPositions, ArrayList<Double> values) {
        float[] topValues = new float[3];
        int i = 1;
        if(values.get(topPositions[0])<0) {
            i = (-1);
        }
        topValues[0] = i*Double.valueOf(values.get(topPositions[0])).floatValue();
        topValues[1] = i*Double.valueOf(values.get(topPositions[1])).floatValue();
        topValues[2] = i*Double.valueOf(values.get(topPositions[2])).floatValue();
        return topValues;
    }
}