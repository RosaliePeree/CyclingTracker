package com.example.rosalie.cyclingtracker;

import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseRequests extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    public TextView yolo;
    public TextView yolo2;
    public EditText email_text;
    public EditText pswd_text;

    private User connectedUser;
    private ArrayList<Ride> allRides;

    private static final String TAG = "MainActivity";

        @Override
        protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setContentView(R.layout.activity_main);

            FirebaseDatabase database = FirebaseDatabase.getInstance(); //Connection to the database
            // //= database.getReference("message");


            mAuth = FirebaseAuth.getInstance();

            pswd_text = (EditText) findViewById(R.id.password); //Inputs for pswd and email
            email_text = (EditText) findViewById(R.id.email);
            yolo = (TextView) findViewById(R.id.yolo); //Textviews for tests
            yolo2 = (TextView) findViewById(R.id.yolo2);

            allRides = new ArrayList<Ride>();


            //myRef.setValue("Hello, World!");

            myRef = database.getReference(); //Ref to the database

            //The following function are in the on create because their are Async
            myRef.child("users/" + mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() { //Gets the info about the connected user and put it in connected USer
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User use = dataSnapshot.getValue(User.class);
                    Log.i(use.getName(), " user");
                    connectedUser = use;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            myRef.child("rides").addValueEventListener(new ValueEventListener() { //gets all of the Rides and store them in the allRides list

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Ride ride = dataSnapshot.getValue(Ride.class);
                //Log.i(ride.getDate(), " date");

                for(DataSnapshot dsp : dataSnapshot.getChildren()) {
                    allRides.add((dsp.getValue(Ride.class)));
                    Log.i(dsp.getValue(Ride.class).getDate(), " date");
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

            Date cDate = new Date();
            String fDate = new SimpleDateFormat("dd/MM/yy").format(cDate); //Gets the date of today


            writeNewRide(25, fDate, 400, 5, 600); //Creates the Ride5
        }

        @Override
        public void onStart() {
            super.onStart();
            // Check if user is signed in (non-null) and update UI accordingly.
            FirebaseUser currentUser = mAuth.getCurrentUser();
            updateUI(currentUser);
        }

    private void updateUI(FirebaseUser currentUser) {

        yolo.setText(currentUser.getUid());
        yolo2.setText(connectedUser.getName());
    }

    public void signIn(View view){ //To connect as a user

        String email = email_text.getText().toString();
        String password = pswd_text.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(DatabaseRequests.this, "Empty field.", Toast.LENGTH_SHORT).show();
        }else {

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(DatabaseRequests.this, "Authentication Successful.", Toast.LENGTH_SHORT).show();
                                updateUI(user);

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(DatabaseRequests.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);

                            }

                        }
                    });
        }
    }

    private void writeNewRide(double average_speed, String date, double distance, int id, double time){ //Creates a new ride
        Ride newRide = new Ride( average_speed,  date,  distance,  id,  time);

        myRef.child("rides").child("ride"+id).setValue(newRide);
    }
}