package com.example.rosalie.cyclingtracker;

/**
 * Created by Rose on 07-11-2017.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rosalie.cyclingtracker.Database.Ride;
import com.example.rosalie.cyclingtracker.Database.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ProfileActivity extends NavDrawActivity {

    TextView username_text, mail_text, distance_text, time_text, averagespeed_text, date_text;


    @SuppressLint("SetTextI18n")
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
        distance_text = (TextView) findViewById(R.id.distance_lastride);
        averagespeed_text = (TextView) findViewById(R.id.averagespeed_lastride);
        date_text = (TextView) findViewById(R.id.date_lastride);
        time_text = (TextView) findViewById(R.id.time_lastride);

        if (currentUser.getRides() != null) {
            int index = currentUser.getRides().size();
            String toParse = currentUser.getRides().get(index - 1);
            String[] yolo = toParse.split("ride");
            index = Integer.parseInt(yolo[1]);
            boolean found = false;
            int counter = 1;
            Ride latest = new Ride();
            while (!found) {
                if (allRides.get(counter).getId() == index) {
                    found = true;
                    latest = allRides.get(counter);
                    distance_text.setText("Distance : " + String.valueOf(latest.getDistance()));
                    averagespeed_text.setText("Average Speed : " + String.valueOf(latest.getAverage_speed()));
                    date_text.setText("Date : " + latest.getDate());
                    time_text.setText("Time : " + String.valueOf(latest.getTime()));
                } else {
                    counter++;
                }
            }

        } else {
            distance_text.setText("");
            averagespeed_text.setText("");
            date_text.setText("No record to display !");
            time_text.setText("");
        }

        username_text.setText(currentUser.getName());
        mail_text.setText(currentUser.getMail());


        //addRideUser(mAuth.getCurrentUser().getUid());


    }

    private void addRideUser(String userID) {

    }
}
