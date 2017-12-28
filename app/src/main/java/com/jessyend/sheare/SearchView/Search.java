package com.jessyend.sheare.SearchView;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

/**
 * The abstract Search class supports the Search Activity.
 * @author Jessye Nana Davies <nanadav2@illinois.edu>
 */
public class Search {

    public interface View {
        void reset();

        void addData(List<Track> items);
    }

    public interface ActionListener {

        void init(String token);

        String getCurrentQuery();

        void search(String searchQuery);

        void loadMoreResults();

        void selectTrack(Track item);

        void resume();

        void pause();

        void destroy();

    }
}