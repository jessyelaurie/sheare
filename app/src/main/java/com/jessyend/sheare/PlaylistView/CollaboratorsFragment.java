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
 * The Collaborators Fragment displays the playlist collaborators
 * @author Jessye Nana Davies <nanadav2@illinois.edu>
 */
public class CollaboratorsFragment extends Fragment {
    private static final String TAG = "CollaboratorsFragment";
    private RecyclerView collabsView;
    private Button btnTEST;

    private String mToken;
    private String mPlaylistId;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private String userID;

    public ArrayList<String> mNames;
    public ArrayList<String> mImageUrls;
    List<Map<String, String>> mData;

    private CollaboratorsAdapter mAdapter;

    /**
     * @param inflater the inflater for the view
     * @param container the fragment's container
     * @param savedInstanceState bundle containing the token and playlist iD
     * @return the view for the fragment
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(com.jessyend.sheare.R.layout.collab_fragment,container,false);
        collabsView = (RecyclerView) view.findViewById(com.jessyend.sheare.R.id.collab_results);

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
            map.put("name", "initialize");
            map.put("picUrl", "initialize");
            mData.add(map);
        }

        mAdapter = new CollaboratorsAdapter(getContext(), mPlaylistId, mData);
        collabsView.setAdapter(mAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        collabsView.setLayoutManager(llm);

        return view;
    }



    /**
     * This displays the user's collaborators on the Playlist Page
     * @param dataSnapshot the dataSnapshot received from the event listener.
     * @throws IOException
     * @throws URISyntaxException
     */
    private void updateData(DataSnapshot dataSnapshot) throws IOException, URISyntaxException {

        mNames = new ArrayList<String>();
        mImageUrls = new ArrayList<String>();

        DataSnapshot playlists = dataSnapshot.child("Playlists");
        DataSnapshot users = dataSnapshot.child("Users");

        DataSnapshot playlist = playlists.child(mPlaylistId);
        DataSnapshot playlistUsers = playlist.child("Users");

        for(DataSnapshot playlistUser : playlistUsers.getChildren()) {
            for(DataSnapshot user : users.getChildren()) {
                if(user.getKey().equals(playlistUser.getKey())) {
                    mNames.add(user.child("spotifyId").getValue(String.class));
                    mImageUrls.add(user.child("profileUrl").getValue(String.class));
                }
            }
        }

        mData = new ArrayList<Map<String, String>>();
        Map<String, String> map;
        for(int i = 0; i < mNames.size(); i++) {
            map = new HashMap<String, String>();
            map.put("name", mNames.get(i));
            map.put("picUrl", mImageUrls.get(i));
            mData.add(map);
        }

        mAdapter.swap(mData);
        mAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Just updated lists", Toast.LENGTH_SHORT).show();
    }
}