package com.jessyend.sheare.UserManagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.jessyend.sheare.HomeView.HomeActivity;
import com.jessyend.sheare.R;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.SpotifyPlayer;


/**
 * The Main Activity handles OAuth with Spotify and user login.
 * @author Jessye Nana Davies <nanadav2@illinois.edu>
 */

public class MainActivity extends Activity implements
        SpotifyPlayer.NotificationCallback, ConnectionStateCallback
{
    private static final String CLIENT_ID = "93d3362b07764c89a9fd1952afb5a489";
    private static final String CLIENT_SECRET = "5017b383e8994d4f970456ed1452b818";
    private static final String REDIRECT_URI = "sheareprotocol://callback";
    private static final int REQUEST_CODE = 1234;

    /**
     * override of onCreate for the Main Activity
     * @param savedInstanceState the bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, SpotifyLoginActivity.class);
        startActivity(intent);
    }

    /**
     * Override of the function onActivityResult
     * Called after the app has been authenticated bt the user
     * @param requestCode the requestCode to check
     * @param resultCode the resultCode
     * @param intent the Intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                String token = response.getAccessToken();
                goToHomepage(token);
            }
        }
    }

    /**
     * Called onDestroy of the App
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * Called on playback event.
     * @param playerEvent
     */
    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d("MainActivity", "Playback event received: " + playerEvent.name());
        switch (playerEvent) {
            // Handle event type as necessary
            default:
                break;
        }
    }


    /**
     * Called on playback error.
     * @param error
     */
    @Override
    public void onPlaybackError(Error error) {
        Log.d("MainActivity", "Playback error received: " + error.name());
        switch (error) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    /**
     * Called when the user logs in.
     */
    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");
    }

    /**
     * Called when the user has authorized the app for the requested scope
     * @param token the token
     */
    public void goToHomepage(String token) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("token", token);
        startActivity(intent);
    }

    /**
     * called when the User logs out.
     */
    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    /**
     * called when login fails
     */
    @Override
    public void onLoginFailed(Error error) {
        Log.d("MainActivity", "Login failed");
    }

    /**
     * called on temporary errors
     */
    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    /**
     * called when connection message received
     */
    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }
}