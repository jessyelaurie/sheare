package com.jessyend.sheare.UserSearchView;

import android.content.Context;
import android.support.annotation.Nullable;

import com.jessyend.sheare.UserManagement.DatabaseHandler;
import com.jessyend.sheare.UserManagement.Request;

import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.models.Track;

/**
 * The Search Presenter(Action Listener) for the Search Adapter.
 * @author Jessye Nana Davies <nanadav2@illinois.edu>
 */
public class UserSearchPresenter implements UserSearch.ActionListener {

    private static final String TAG = UserSearchPresenter.class.getSimpleName();
    public static final int PAGE_SIZE = 20;

    private final Context mContext;
    private final UserSearch.View mView;
    private String mCurrentQuery;

    private UserSearchPager mSearchPager;
    private UserSearchPager.CompleteListener mSearchListener;

    private String mToken;
    private String mPlaylistId, mPlaylistName;
    private List<User> mUsers;

    public UserSearchPresenter(Context context, UserSearch.View view, String token, String playlistId, String playlistName, List<User> users) {
        mContext = context;
        mView = view;
        mToken = token;
        mPlaylistId = playlistId;
        mPlaylistName = playlistName;
        mUsers = users;
    }

    @Override
    public void init(String accessToken) {
        SpotifyApi spotifyApi = new SpotifyApi();

        if (accessToken != null) {
            spotifyApi.setAccessToken(accessToken);
        } else {
        }

        mSearchPager = new UserSearchPager(mUsers);
    }


    @Override
    public void search(@Nullable String searchQuery) {
        if (searchQuery != null && !searchQuery.isEmpty() && !searchQuery.equals(mCurrentQuery)) {
            mCurrentQuery = searchQuery;
            mView.reset();
            mSearchListener = new UserSearchPager.CompleteListener() {
                @Override
                public void onComplete(List<User> items) {
                    mView.addData(items);
                }
            };
            mSearchPager.getFirstPage(searchQuery, PAGE_SIZE, mSearchListener);
        }
    }


    @Override
    public void destroy() {
    }

    @Override
    @Nullable
    public String getCurrentQuery() {
        return mCurrentQuery;
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void loadMoreResults() {
        mSearchPager.getNextPage(mSearchListener);
    }

    @Override
    public void selectUser(User item) {
        DatabaseHandler dbh = new DatabaseHandler();
        dbh.addUserToPlaylist(item, mPlaylistId, mPlaylistName);
    }
}
