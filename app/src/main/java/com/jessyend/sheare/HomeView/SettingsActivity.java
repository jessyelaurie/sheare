package com.jessyend.sheare.HomeView;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jessyend.sheare.R;

/**
 * The Settings Activity allows the user to change their credentials and profile picture.
 * @author Jessye Nana Davies <nanadav2@illinois.edu>
 */
public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";

    private static FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private static DatabaseReference myRef;

    private EditText mEmail, mPassword, mPicture;
    private Button emailButton;
    private Button passwordButton;
    private Button pictureButton;

    private String mToken;

    /**
     * override of onCreate for the Settings Activity
     * @param savedInstanceState the bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent = getIntent();
        mToken = intent.getStringExtra("token");

        mEmail = (EditText) findViewById(R.id.change_email);
        mPassword = (EditText) findViewById(R.id.change_password);
        mPicture = (EditText) findViewById(R.id.change_profile_pic);

        emailButton = (Button) findViewById(R.id.change_email_button);
        passwordButton = (Button) findViewById(R.id.change_password_button);
        pictureButton = (Button) findViewById(R.id.change_profile_pic_button);

        mAuth = FirebaseAuth.getInstance();

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAddress = mEmail.getText().toString();
                if(!emailAddress.equals("")) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if(user!=null) {
                        user.updateEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User email address updated.");
                                    Toast.makeText(SettingsActivity.this, "Email address updated", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(SettingsActivity.this, "Email address update failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });

        passwordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = mPassword.getText().toString();
                if(!password.equals("")) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if(user!=null) {
                        user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User password updated.");
                                    Toast.makeText(SettingsActivity.this, "Password updated", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(SettingsActivity.this, "Password update failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });


        pictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = mPicture.getText().toString();
                if(!url.equals("")) {
                    mFirebaseDatabase = FirebaseDatabase.getInstance();
                    myRef = mFirebaseDatabase.getReference();
                    String key = myRef.getKey();
                    FirebaseUser user = mAuth.getCurrentUser();
                    if(user != null) {
                        myRef.child("Users").child(user.getUid()).child("profileUrl").setValue(url);
                        Toast.makeText(SettingsActivity.this, "Profile picture changed", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(SettingsActivity.this, "No user logged in.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
