package com.jessyend.sheare.UserManagement;

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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.jessyend.sheare.HomeView.HomeActivity;
import com.jessyend.sheare.R;

/**
 * The Signup Activity allows user to create an account.
 * @author Jessye Nana Davies <nanadav2@illinois.edu>
 */
public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";

    private static FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText mEmail, mPassword;
    private Button createButton;
    private String mToken;

    /**
     * override of onCreate for the SignUpActivity
     * @param savedInstanceState the bundle containing the token for current user
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Intent intent = getIntent();
        mToken = intent.getStringExtra("token");

        mEmail = (EditText) findViewById(R.id.emailNew);
        mPassword = (EditText) findViewById(R.id.passwordNew);
        createButton = (Button) findViewById(R.id.create_account);

        mAuth = FirebaseAuth.getInstance();

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAddress = mEmail.getText().toString();
                String pass = mPassword.getText().toString();
                if(!emailAddress.equals("") && !pass.equals("")) {
                    mAuth.createUserWithEmailAndPassword(emailAddress, pass)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "Auth failed",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SignUpActivity.this, "Account created",
                                                Toast.LENGTH_SHORT).show();
                                        DatabaseHandler dbHandler = new DatabaseHandler();
                                        dbHandler.addUserToDatabase(mToken);
                                        Intent i = new Intent(SignUpActivity.this, HomeActivity.class);
                                        i.putExtra("token", mToken);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }



}
