package com.jessyend.sheare.UserSearchView;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

/**
 * The abstract UserSearch class supports the Search Activity.
 * @author Jessye Nana Davies <nanadav2@illinois.edu>
 */
public class UserSearch {

    public interface View {
        void reset();

        void addData(List<User> items);
    }

    public interface ActionListener {

        void init(String token);

        String getCurrentQuery();

        void search(String searchQuery);

        void loadMoreResults();

        void selectUser(User item);

        void resume();

        void pause();

        void destroy();

    }
}