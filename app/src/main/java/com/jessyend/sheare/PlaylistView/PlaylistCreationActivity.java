package com.jessyend.sheare.PlaylistView;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jessyend.sheare.UserManagement.Request;
import com.jessyend.sheare.HomeView.HomeActivity;
import com.jessyend.sheare.R;

import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * The PlaylistCreation Activity supports the creation of a playlist.
 * The user picks a name, a photo and their color for a new playlist.
 * @author Jessye Nana Davies <nanadav2@illinois.edu>
 */
public class PlaylistCreationActivity extends Activity {

    private static final String TAG = "PlaylistCreationActivity";
    private static final int RESULT_LOAD_IMAGE = 2;
    private String mToken;
    private FirebaseDatabase mFirebaseDatabase;
    private static FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static DatabaseReference myRef;
    /**
     * override of onCreate for the PlaylistCreation Activity
     * @param savedInstanceState the bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

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
                Object value = dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        Intent intent = getIntent();
        mToken = intent.getStringExtra("token");
        setContentView(R.layout.activity_playlistcreation);
    }

    /**
     * Click listener to the button "Browse Image Library"
     * @param view the clicked view
     */
    public void browseImageLibrary(View view) {
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    /**
     * Override of the onActivityResult to download the selected image from the Library
     * @param requestCode the requestCode to check
     * @param resultCode the resultCode
     * @param data the possible image
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            ImageView imageView = (ImageView) findViewById(R.id.libraryimage);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            this.recreate();
        }
    }

    /**
     * Click listener to the button "Create playlist". Redirects to the Request class
     * @param view the clicked view
     * @throws IOException
     * @throws URISyntaxException
     */
    public void createPlaylist(View view) throws IOException, URISyntaxException, JSONException {
        EditText name = (EditText)findViewById(R.id.playlistname);
        String playlistName = name.getText().toString();
        if(playlistName.equals("")) {
            playlistName = "New Playlist";
        }
        Request request = new Request();
        request.createNewPlaylist(mToken, playlistName);
        Intent intent = new Intent(PlaylistCreationActivity.this, HomeActivity.class);
        intent.putExtra("token", mToken);
        startActivity(intent);
        Toast.makeText(PlaylistCreationActivity.this, "New playlist created.", Toast.LENGTH_SHORT).show();

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