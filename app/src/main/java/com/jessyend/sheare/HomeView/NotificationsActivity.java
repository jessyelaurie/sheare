package com.jessyend.sheare.HomeView;

import android.app.Activity;
import android.os.Bundle;

import com.jessyend.sheare.R;

/**
 * The Notifications Activity displays the notifications for the App (new member to a playlist,
 * new songs added, ..)
 * @author Jessye Nana Davies <nanadav2@illinois.edu>
 */
public class NotificationsActivity extends Activity {
    /**
     * override of onCreate for the Notifications Activity
     * @param savedInstanceState the bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
    }
}