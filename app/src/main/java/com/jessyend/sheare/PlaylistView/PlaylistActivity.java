package com.jessyend.sheare.PlaylistView;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jessyend.sheare.R;

/**
 * The Playlist Activity displays the songs for a playlist and its collaborators.
 * It classifies them by color.
 * @author Jessye Nana Davies <nanadav2@illinois.edu>
 */
public class PlaylistActivity extends AppCompatActivity {
    private String mPlaylistId;
    private String mToken;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static DatabaseReference myRef;


    private static final String TAG = "PlaylistActivity";
    private TabsPagerAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

    /**
     * override of onCreate for the Playlist Activity
     * @param savedInstanceState the bundle containing the playlist id and the token
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        Intent intent = getIntent();
        mPlaylistId = intent.getStringExtra("id");
        mToken = intent.getStringExtra("token");

        mSectionsPageAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * setting up the view pager with 3 tabs: about, tracks and collaborators tab.
     * @param viewPager the viewPager containing the Adapter
     */
    private void setupViewPager(ViewPager viewPager) {
        TabsPagerAdapter adapter = new TabsPagerAdapter(getSupportFragmentManager());

        AboutFragment about = new AboutFragment();
        Bundle bundle = new Bundle();
        bundle.putString("token", mToken);
        bundle.putString("id", mPlaylistId);
        about.setArguments(bundle);
        adapter.addFragment(about, "About");


        TracksFragment tracks = new TracksFragment();
        Bundle bundleTracks = new Bundle();
        bundleTracks.putString("token", mToken);
        bundleTracks.putString("id", mPlaylistId);
        tracks.setArguments(bundleTracks);
        adapter.addFragment(tracks, "Tracks");

        CollaboratorsFragment collab = new CollaboratorsFragment();
        Bundle bundleCollab = new Bundle();
        bundleCollab.putString("token", mToken);
        bundleCollab.putString("id", mPlaylistId);
        collab.setArguments(bundleCollab);
        adapter.addFragment(collab, "Collaborators");

        viewPager.setAdapter(adapter);
    }

}