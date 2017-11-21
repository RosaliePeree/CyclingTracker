package com.example.rosalie.cyclingtracker;

/**
 * Created by Rose on 07-11-2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.rosalie.cyclingtracker.Database.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends NavDrawActivity implements View.OnClickListener{

    Button buttonSignIn, buttonSignUp, buttonSubmitSignIn, buttonSubmitSignOn;
    EditText emailSignIn, pswdSignIn, pswdSignUp, usernameSignUp, emailSignUp;
    LinearLayout fragmentMain, fragmentSignIn, fragmentSignUp;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTheme(android.R.style.Theme_DeviceDefault_Light_DarkActionBar);
        setButtonsListener();

        emailSignIn = (EditText) findViewById(R.id.edit_text_username);
        pswdSignIn = (EditText) findViewById(R.id.edit_text_passwd);
        pswdSignUp =  (EditText) findViewById(R.id.edit_text_create_passwd);
        emailSignUp = (EditText) findViewById(R.id.edit_text_create_email);
        usernameSignUp = (EditText) findViewById(R.id.edit_text_create_username);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if(user != null)
        {
            Intent intent = new Intent(this,WhatsUpActivity.class);
            startActivity(intent);
        }
    }

    public void setButtonsListener(){
        fragmentMain = (LinearLayout) findViewById(R.id.layout_fragment_main);
        fragmentSignIn = (LinearLayout) findViewById(R.id.layout_fragment_sign_in);
        fragmentSignUp = (LinearLayout) findViewById(R.id.layout_fragment_sign_up);
        //Set up the visibility to only display the main layout, aka the two first buttons to sign in and sign out
        fragmentMain.setVisibility(View.VISIBLE);
        fragmentSignUp.setVisibility(View.GONE);
        fragmentSignIn.setVisibility(View.GONE);

        //Find all the buttons on the layout and add listeners to them
        buttonSignIn = (Button)findViewById(R.id.button_sign_in);
        buttonSignUp = (Button)findViewById(R.id.button_sign_up);
        buttonSubmitSignIn = (Button)findViewById(R.id.submit_button_sign_in);
        buttonSubmitSignOn = (Button)findViewById(R.id.submit_button_sign_on);
        buttonSignIn.setOnClickListener(this);
        buttonSignUp.setOnClickListener(this);
        buttonSubmitSignIn.setOnClickListener(this);
        buttonSubmitSignOn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.button_sign_in:
                //Hide the previous buttons and display the new buttons
                fragmentMain.setVisibility(View.GONE);
                fragmentSignIn.setVisibility(View.VISIBLE);
                break;

            case R.id.button_sign_up:
                fragmentMain.setVisibility(View.GONE);
                fragmentSignUp.setVisibility(View.VISIBLE);
                break;

            case R.id.submit_button_sign_in:
                Toast.makeText(this, "Check the info in the database", Toast.LENGTH_SHORT).show();
                /* Insert database code here (Greg) */
                if (pswdSignIn.getText().toString().isEmpty() || emailSignIn.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Empty field(s).", Toast.LENGTH_SHORT).show();
                }else {
                    mAuth.signInWithEmailAndPassword(emailSignIn.getText().toString(), pswdSignIn.getText().toString())
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(MainActivity.this, "Authentication Successful.", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(MainActivity.this, WhatsUpActivity.class);
                                        startActivity(intent);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                break;

            case R.id.submit_button_sign_on:
                Toast.makeText(this,"Add the info in the database",Toast.LENGTH_SHORT).show();
                /* Insert database code here (Greg) */
                if (pswdSignUp.getText().toString().isEmpty() || emailSignUp.getText().toString().isEmpty() || usernameSignUp.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Empty field(s).", Toast.LENGTH_SHORT).show();
                }else {

                    mAuth.createUserWithEmailAndPassword(emailSignUp.getText().toString(), pswdSignUp.getText().toString())
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        User newUser = new User(emailSignUp.getText().toString(), usernameSignUp.getText().toString(), null);
                                        myRef.child("users").child(user.getUid()).setValue(newUser);

                                        Toast.makeText(MainActivity.this, "Creation of account successful. Please sign in",
                                                Toast.LENGTH_SHORT).show();
                                        user = mAuth.getCurrentUser();
                                        if(user != null) {
                                            Intent intent = new Intent(getBaseContext(), WhatsUpActivity.class);
                                            startActivity(intent);
                                        }
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        FirebaseAuthException e = (FirebaseAuthException )task.getException();
                                        Toast.makeText(MainActivity.this, "Failed Registration: "+e.getMessage(), Toast.LENGTH_LONG).show();
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Log.e("LoginActivity", "Failed Registration", e);
                                        Toast.makeText(MainActivity.this, "Creation of account failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
                break;
        }
    }

    /* Those two functions are used to handle the back key press event, to not have it go out of the activity */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        fragmentSignIn.setVisibility(View.GONE);
        fragmentSignUp.setVisibility(View.GONE);
        fragmentMain.setVisibility(View.VISIBLE);
    }
}




