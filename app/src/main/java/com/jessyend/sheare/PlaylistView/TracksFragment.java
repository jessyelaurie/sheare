package com.jessyend.sheare.PlaylistView;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Tracks Fragment displays the playlist tracks
 * @author Jessye Nana Davies <nanadav2@illinois.edu>
 */
public class TracksFragment extends Fragment {
    private static final String TAG = "TracksFragment";
    private RecyclerView tracksView;
    private Button btnTEST;

    private String mToken;
    private String mPlaylistId;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private String userID;

    public ArrayList<String> mTitles;
    public ArrayList<String> mSubtitles;
    public ArrayList<String> mImageUrls;
    public ArrayList<String> mUris;
    List<Map<String, String>> mData;


    private String userCount;

    private TracksAdapter mAdapter;
    private RecyclerView mRecyclerView;


    /**
     * @param inflater the inflater for the view
     * @param container the fragment's container
     * @param savedInstanceState bundle containing the token and playlist iD
     * @return the view for the fragment
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(com.jessyend.sheare.R.layout.tracks_fragment,container,false);
        tracksView = (RecyclerView) view.findViewById(com.jessyend.sheare.R.id.tracks_results);

        mToken = getArguments().getString("token");
        mPlaylistId = getArguments().getString("id");

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

        mData = new ArrayList<Map<String, String>>();
        Map<String, String> map;
        for(int i = 0; i < 1; i++) {
            map = new HashMap<String, String>();
            map.put("title", "initialize");
            map.put("subtitle", "initialize");
            map.put("imageUrl", "initialize");
            mData.add(map);
        }

        mAdapter = new TracksAdapter(getActivity(), mPlaylistId, mData);
        tracksView.setAdapter(mAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        tracksView.setLayoutManager(llm);

        return view;
    }

    /**
     * This displays the user's playlists on the Homepage
     * @param dataSnapshot the dataSnapshot received from the event listener.
     * @throws IOException
     * @throws URISyntaxException
     */
    private void updateData(DataSnapshot dataSnapshot) throws IOException, URISyntaxException {

        mTitles = new ArrayList<String>();
        mSubtitles = new ArrayList<String>();
        mImageUrls = new ArrayList<String>();
        mUris = new ArrayList<String>();

        DataSnapshot playlists = dataSnapshot.child("Playlists");
        DataSnapshot playlist = playlists.child(mPlaylistId);
        DataSnapshot tracks = playlist.child("Tracks");

        for(DataSnapshot track : tracks.getChildren()) {
            mUris.add(track.child("uri").getValue(String.class));
            mTitles.add(track.child("title").getValue(String.class));
            mSubtitles.add(track.child("subtitle").getValue(String.class));
            mImageUrls.add(track.child("imageUrl").getValue(String.class));
        }

        mData = new ArrayList<Map<String, String>>();
        Map<String, String> map;
        for(int i = 0; i < mTitles.size(); i++) {
            map = new HashMap<String, String>();
            map.put("title", mTitles.get(i));
            map.put("subtitle", mSubtitles.get(i));
            map.put("imageUrl", mImageUrls.get(i));
            map.put("uri", mUris.get(i));
            mData.add(map);
        }

        mAdapter.swap(mData);
        mAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Just updated lists", Toast.LENGTH_SHORT).show();
        }
}