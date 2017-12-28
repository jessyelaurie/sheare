package com.jessyend.sheare.UserSearchView;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.SearchView;
import android.widget.SearchView;
import android.view.View;

import com.jessyend.sheare.R;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

/**
 * The Search Activity supports the search for tracks.
 * @author Jessye Nana Davies <nanadav2@illinois.edu>
 */
public class UserSearchActivity extends AppCompatActivity implements UserSearch.View {
    static final String EXTRA_TOKEN = "EXTRA_TOKEN";
    private static final String KEY_CURRENT_QUERY = "CURRENT_QUERY";

    private UserSearch.ActionListener mActionListener;

    private LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
    private ScrollListener mScrollListener = new ScrollListener(mLayoutManager);
    private UserSearchAdapter mAdapter;
    private List<User> mUsers;
    private ArrayList<String> mUserIds;
    private ArrayList<String> mUserUrls;
    private ArrayList<String> mUserNums;

    /**
     * override of onCreate for the Users Search Activity
     * @param savedInstanceState the bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usersearch);

        Intent intent = getIntent();
        String token = intent.getStringExtra("token");
        String playlistId = intent.getStringExtra("id");
        String playlistName = intent.getStringExtra("name");

        // gets the array of users
        Bundle extra = getIntent().getBundleExtra("extra");
        mUserIds = (ArrayList<String>) extra.getSerializable("usersId");
        mUserUrls = (ArrayList<String>) extra.getSerializable("usersUrl");
        mUserNums = (ArrayList<String>) extra.getSerializable("usersNum");
        mUsers = new ArrayList<User>();
        for(int i = 0; i < mUserIds.size(); i++) {
            User user = new User(mUserIds.get(i), mUserUrls.get(i), mUserNums.get(i));
            mUsers.add(user);
        }

        mActionListener = new UserSearchPresenter(this, this, token, playlistId, playlistName, mUsers);
        mActionListener.init(token);

        final SearchView searchView = (SearchView) findViewById(R.id.user_search_view);
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


        mAdapter = new UserSearchAdapter(this, token, playlistId, playlistName, new UserSearchAdapter.ItemSelectedListener() {
            @Override
            public void onItemSelected(View itemView, User item) {
                mActionListener.selectUser(item);
            }
        });

        RecyclerView resultsList = (RecyclerView) findViewById(R.id.user_search_results);
        resultsList.setHasFixedSize(true);
        resultsList.setLayoutManager(mLayoutManager);
        resultsList.setAdapter(mAdapter);
        resultsList.addOnScrollListener(mScrollListener);

        if (savedInstanceState != null) {
            String currentQuery = savedInstanceState.getString(KEY_CURRENT_QUERY);
            mActionListener.search(currentQuery);
        }
    }

    private class ScrollListener extends UserResultListScrollListener {

        public ScrollListener(LinearLayoutManager layoutManager) {
            super(layoutManager);
        }

        @Override
        public void onLoadMore() {
            mActionListener.loadMoreResults();
        }
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, UserSearchActivity.class);
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
    public void addData(List<User> items) {
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