package com.jessyend.sheare.UserManagement;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jessyend.sheare.HomeView.HomeActivity;
import com.jessyend.sheare.R;

import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;


/**
 * The Login Activity supports user login and sign up to the application with email and password.
 * @author Jessye Nana Davies <nanadav2@illinois.edu>
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private static FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText mEmail, mPassword;
    private Button signInButton, signUpButton;
    private String mToken;

    /**
     * override of onCreate for the Login Activity
     * @param savedInstanceState the bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        Intent intent = getIntent();
        mToken = intent.getStringExtra("token");

        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        signInButton = (Button) findViewById(R.id.email_sign_in_button);
        signUpButton = (Button) findViewById(R.id.email_sign_up_button);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(LoginActivity.this, "Signed in", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(LoginActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();

                }
            }
        };

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAddress = mEmail.getText().toString();
                String pass = mPassword.getText().toString();
                if(!emailAddress.equals("") && !pass.equals("")){
                    mAuth.signInWithEmailAndPassword(emailAddress,pass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Valid credentials", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                i.putExtra("token", mToken);
                                startActivity(i);
                                finish();
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "You didn't fill in all the fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                i.putExtra("token", mToken);
                startActivity(i);
                finish();
            }
        });
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

    /**
     * Signing out the user.
     * @param c the activity context
     */
    public static void signOut(Context c) {
        mAuth.signOut();
        Toast.makeText(c, "Valid credentials", Toast.LENGTH_SHORT).show();
    }
}
