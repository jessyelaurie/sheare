package com.jessyend.sheare.UserManagement;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jessyend.sheare.R;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * The Spotify Login Activity authenticates the user to the Spotify account.
 * @author Jessye Nana Davies <nanadav2@illinois.edu>
 */
@TargetApi(21)
public class SpotifyLoginActivity extends AppCompatActivity {
    private static final String CLIENT_ID = "93d3362b07764c89a9fd1952afb5a489";
    private static final String CLIENT_SECRET = "5017b383e8994d4f970456ed1452b818";
    private static final String REDIRECT_URI = "sheareprotocol://callback";

    /**
     * override of onCreate for the Spotify Login Activity.
     * @param savedInstanceState the bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"user-read-private", "playlist-modify-private", "playlist-read-private","playlist-read-collaborative",
                "playlist-modify-public", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginInBrowser(this, request);
    }

    /**
     * Called on response from the Spotify Authentication web page.
     * @param intent the opened intent
     */
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Uri uri = intent.getData();
        if (uri != null) {
            AuthenticationResponse response = AuthenticationResponse.fromUri(uri);
            switch (response.getType()) {
                case TOKEN:
                    Intent i = new Intent(SpotifyLoginActivity.this, LoginActivity.class);
                    String mToken = response.getAccessToken();
                    Request r = new Request();
                    try {
                        r.getCurrentSpotifyUser(mToken, this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    i.putExtra("token", mToken);
                    startActivity(i);
                    finish();
                    break;

                case ERROR:
                    break;
                default:
            }
        }
    }
}
