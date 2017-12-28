package com.jessyend.sheare.SearchView;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.SearchView;
import android.widget.SearchView;
import android.view.View;

import com.google.common.base.Joiner;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jessyend.sheare.R;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;

/**
 * The Search Activity supports the search for tracks.
 * @author Jessye Nana Davies <nanadav2@illinois.edu>
 */
public class SearchActivity extends AppCompatActivity implements Search.View {
    static final String EXTRA_TOKEN = "EXTRA_TOKEN";
    private static final String KEY_CURRENT_QUERY = "CURRENT_QUERY";

    private Search.ActionListener mActionListener;

    private LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
    private ScrollListener mScrollListener = new ScrollListener(mLayoutManager);
    private SearchAdapter mAdapter;

    /**
     * override of onCreate for the Search Activity
     * @param savedInstanceState the bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        String token = intent.getStringExtra("token");
        String playlistId = intent.getStringExtra("id");
        String mCurrCount = intent.getStringExtra("count");

        mActionListener = new SearchPresenter(this, this, token, playlistId, mCurrCount);
        mActionListener.init(token);

        // Setup search field
        final SearchView searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mActionListener.search(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mAdapter = new SearchAdapter(this, new SearchAdapter.ItemSelectedListener() {
            @Override
            public void onItemSelected(View itemView, Track item) {
                mActionListener.selectTrack(item);
            }
        });

        RecyclerView resultsList = (RecyclerView) findViewById(R.id.search_results);
        resultsList.setHasFixedSize(true);
        resultsList.setLayoutManager(mLayoutManager);
        resultsList.setAdapter(mAdapter);
        resultsList.addOnScrollListener(mScrollListener);

        if (savedInstanceState != null) {
            String currentQuery = savedInstanceState.getString(KEY_CURRENT_QUERY);
            mActionListener.search(currentQuery);
        }
    }

    private class ScrollListener extends ResultListScrollListener {

        public ScrollListener(LinearLayoutManager layoutManager) {
            super(layoutManager);
        }

        @Override
        public void onLoadMore() {
            mActionListener.loadMoreResults();
        }
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, SearchActivity.class);
    }

    /**
     * Resets the scroll listener
     */
    @Override
    public void reset() {
        mScrollListener.reset();
        mAdapter.clearData();
    }

    /**
     * Adds new playlists
     * @param items new playlists to add.
     */
    @Override
    public void addData(List<Track> items) {
        mAdapter.addData(items);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mActionListener.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mActionListener.resume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActionListener.getCurrentQuery() != null) {
            outState.putString(KEY_CURRENT_QUERY, mActionListener.getCurrentQuery());
        }
    }

    @Override
    protected void onDestroy() {
        mActionListener.destroy();
        super.onDestroy();
    }
}