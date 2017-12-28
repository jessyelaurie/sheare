package com.jessyend.sheare.UserSearchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.client.Response;

/**
 * The UserSearch Pager for the UserSearch view.
 * @author Jessye Nana Davies <nanadav2@illinois.edu>
 */
public class UserSearchPager {
    private int mCurrentOffset;
    private int mPageSize;
    private String mCurrentQuery;
    private List<User> mUsers;

    public interface CompleteListener {
        void onComplete(List<User> items);
    }

    public UserSearchPager(List<User> users) {
        mUsers = users;
    }

    public void getFirstPage(String query, int pageSize, CompleteListener listener) {
        mCurrentOffset = 0;
        mPageSize = pageSize;
        mCurrentQuery = query;
        getData(query, 0, pageSize, listener);
    }

    public void getNextPage(CompleteListener listener) {
    }

    private void getData(String query, int offset, final int limit, final CompleteListener listener) {
        List<User> resultUsers = new ArrayList<User>();
        for(int i = 0; i < mUsers.size(); i++) {
            if(mUsers.get(i).getSpotifyId().toLowerCase().contains(query.toLowerCase())) {
                resultUsers.add(mUsers.get(i));
            }
        }
        listener.onComplete(resultUsers);
    }
}

