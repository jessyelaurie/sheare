package com.jessyend.sheare.HomeView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jessyend.sheare.UserManagement.DatabaseHandler;
import com.jessyend.sheare.UserManagement.LoginActivity;
import com.jessyend.sheare.UserManagement.LogoutActivity;
import com.jessyend.sheare.UserManagement.Request;
import com.jessyend.sheare.PlaylistView.PlaylistActivity;
import com.jessyend.sheare.PlaylistView.PlaylistCreationActivity;
import com.jessyend.sheare.R;
import com.jessyend.sheare.SearchView.SearchActivity;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The Home Activity is the homepage of the app. It redirects to other Activities.
 * @author Jessye Nana Davies <nanadav2@illinois.edu>
 */
public class HomeActivity extends Activity {
    public String mToken;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private String userID;
    private ListView mListView;
    private boolean firstUpdateSinceLoggedIn;

    private Map<String, ArrayList<String>> mDatabasePlaylists;

    /**
     * override of onCreate for the Home Activity
     * @param savedInstanceState the bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firstUpdateSinceLoggedIn = true;

        Intent intent = getIntent();
        mToken = intent.getStringExtra("token");
        setContentView(R.layout.activity_home);

        mListView = (ListView) findViewById(R.id.listview);

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
                    showData(dataSnapshot);
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


        // This creates the menu buttons.

        ImageView icon = new ImageView(this);

        String PACKAGE_NAME = getApplicationContext().getPackageName();
        int imgId1 = getResources().getIdentifier(PACKAGE_NAME+":drawable/ic_menu", null, null);
        icon.setImageBitmap(BitmapFactory.decodeResource(getResources(),imgId1));


        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon).build();

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);

        ImageView itemIcon1 = new ImageView(this);
        int imgId = getResources().getIdentifier(PACKAGE_NAME+":drawable/ic_add", null, null);
        itemIcon1.setImageBitmap(BitmapFactory.decodeResource(getResources(),imgId));
        SubActionButton button1 = itemBuilder.setContentView(itemIcon1).build();


        ImageView itemIcon2 = new ImageView(this);
        int imgId2 = getResources().getIdentifier(PACKAGE_NAME+":drawable/ic_music", null, null);
        itemIcon2.setImageBitmap(BitmapFactory.decodeResource(getResources(),imgId2));
        SubActionButton button2 = itemBuilder.setContentView(itemIcon2).build();

        ImageView itemIcon3 = new ImageView(this);
        int imgId3 = getResources().getIdentifier(PACKAGE_NAME+":drawable/ic_settings", null, null);
        itemIcon3.setImageBitmap(BitmapFactory.decodeResource(getResources(),imgId3));
        SubActionButton button3 = itemBuilder.setContentView(itemIcon3).build();

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .attachTo(actionButton)
                .build();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPlaylistCreation(v);
            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSearch(v);
            }
        });


        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSettings(v);
            }
        });
    }


    /**
     * This displays the user's playlists on the Homepage
     * @param dataSnapshot the dataSnapshot received from the event listener.
     * @throws IOException
     * @throws URISyntaxException
     */
    private void showData(DataSnapshot dataSnapshot) throws IOException, URISyntaxException {
        DataSnapshot users = dataSnapshot.child("Users");
        DataSnapshot playlists = dataSnapshot.child("Playlists");

        final ArrayList<String> playlistsIdsArr = new ArrayList<>();
        final ArrayList<String> playlistsArr = new ArrayList<>();


        for(DataSnapshot ds : users.getChildren()){
            if(ds.getKey().equals(userID)) {
                DataSnapshot playlists1 = ds.child("Playlists");
                for(DataSnapshot d : playlists1.getChildren()) {
                    Request req = new Request();
                    playlistsIdsArr.add((String)d.getKey());
                    playlistsArr.add((String)d.getValue());
                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, playlistsArr);
            mListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
                {
                    Toast.makeText(HomeActivity.this, playlistsIdsArr.get(position), Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(HomeActivity.this, PlaylistActivity.class);
                    String id = playlistsIdsArr.get(position);
                    i.putExtra("id", id);
                    i.putExtra("token", mToken);
                    startActivity(i);
                }
            });
        }

        if(firstUpdateSinceLoggedIn) {
            firstUpdateSinceLoggedIn = false;
            mDatabasePlaylists = new HashMap<String, ArrayList<String>>();
            DataSnapshot user = users.child(userID);
            DataSnapshot userPlaylists = user.child("Playlists");
            for(DataSnapshot playlist : userPlaylists.getChildren()) {
                String id = playlist.getKey();
                DataSnapshot playlistData = playlists.child(id);
                DataSnapshot tracks = playlistData.child("Tracks");
                ArrayList<String> tracksForPlaylist = new ArrayList<>();
                for (DataSnapshot track : tracks.getChildren()) {
                    tracksForPlaylist.add(track.getKey());
                }
                mDatabasePlaylists.put(id, tracksForPlaylist);
            }

            DatabaseHandler dbh = new DatabaseHandler();
            try {
                dbh.synchronizeSpotifyWithDatabase(mToken, HomeActivity.this, mDatabasePlaylists);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Click listener for the create playlist button
     * @param view the view clicked
     */
    public void goToPlaylistCreation(View view) {
        Intent intent = new Intent(HomeActivity.this, PlaylistCreationActivity.class);
        intent.putExtra("token", mToken);
        startActivity(intent);
    }

    /**
     * Click listener for the notifications button
     * @param view the view clicked
     */
    public void goToNotifications(View view) {
        Intent intent = new Intent(HomeActivity.this, NotificationsActivity.class);
        startActivity(intent);
    }

    /**
     * Click listener for the search button
     * @param view the view clicked
     */
    public void goToSearch(View view) {
        Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
        intent.putExtra("token", mToken);
        startActivity(intent);
    }

    /**
     * Click listener for the playlists buttons
     * @param view the view clicked
     */
    public void goToPlaylist(View view) {
        Intent intent = new Intent(HomeActivity.this, PlaylistActivity.class);
        startActivity(intent);
    }

    /**
     * Click listener for the playlists buttons
     * @param view the view clicked
     */
    public void goToSettings(View view) {
        Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * Click listener for the logout button
     * @param view the view clicked
     */
    public void logout(View view) {
        LoginActivity.signOut(getApplicationContext());
        Intent intent = new Intent(this, LogoutActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
