package com.example.rosalie.cyclingtracker;

/**
 * Created by Rose on 07-11-2017.
 */

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rosalie.cyclingtracker.Database.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ProfileActivity extends NavDrawActivity {

    TextView username_text, mail_text;
    ListView list_rides;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_profile, null, false);
        drawer.addView(contentView, 0);

        toolbar.setTitle(R.string.nav_profile);
        username_text = (TextView) findViewById(R.id.username_profile);
        mail_text = (TextView) findViewById(R.id.email_profile);
        list_rides = (ListView) findViewById(R.id.list_of_rides);

        username_text.setText(currentUser.getName());
        mail_text.setText(currentUser.getMail());


        addRideUser(mAuth.getCurrentUser().getUid());



    }

    private void addRideUser(String userID){
        if(currentUser.getRides() == null) {
            myRef.child("users").child(userID).child("rides").child("0").setValue("ride" + allRides.size());
        }
    }
}
