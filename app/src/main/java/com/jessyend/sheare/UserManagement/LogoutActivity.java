package com.jessyend.sheare.UserManagement;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

@TargetApi(21)
/**
 * The Logout Activity supports user logout and opens the logout webpage for Spotify account
 * @author Jessye Nana Davies <nanadav2@illinois.edu>
 */
public class LogoutActivity extends AppCompatActivity {

    /**
     * override of onCreate for the Logout Activity
     * @param savedInstanceState the bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://accounts.spotify.com"));
        startActivity(browserIntent);
        finish();
    }
}
