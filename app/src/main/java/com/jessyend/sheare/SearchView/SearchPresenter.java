package com.jessyend.sheare.SearchView;

import android.content.Context;
import android.support.annotation.Nullable;

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
public class SearchPresenter implements Search.ActionListener {

    private static final String TAG = SearchPresenter.class.getSimpleName();
    public static final int PAGE_SIZE = 20;

    private final Context mContext;
    private final Search.View mView;
    private String mCurrentQuery;
    private String mCurrCount;

    private SearchPager mSearchPager;
    private SearchPager.CompleteListener mSearchListener;

    private String mToken;
    private String mPlaylistId;

    public SearchPresenter(Context context, Search.View view, String token, String playlistId, String count) {
        mContext = context;
        mView = view;
        mToken = token;
        mPlaylistId = playlistId;
        mCurrCount = count;
    }

    @Override
    public void init(String accessToken) {
        SpotifyApi spotifyApi = new SpotifyApi();

        if (accessToken != null) {
            spotifyApi.setAccessToken(accessToken);
        } else {
        }

        mSearchPager = new SearchPager(spotifyApi.getService());
    }


    @Override
    public void search(@Nullable String searchQuery) {
        if (searchQuery != null && !searchQuery.isEmpty() && !searchQuery.equals(mCurrentQuery)) {
            mCurrentQuery = searchQuery;
            mView.reset();
            mSearchListener = new SearchPager.CompleteListener() {
                @Override
                public void onComplete(List<Track> items) {
                    mView.addData(items);
                }

                @Override
                public void onError(Throwable error) {
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
    public void selectTrack(Track item) {
        String trackUri = item.uri;
        Request trackRequest = new Request();
        try {
            trackRequest.addNewTrack(mContext, mToken, trackUri, mPlaylistId, item, mCurrCount);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
