package com.example.rosalie.cyclingtracker;

/**
 * Created by Rose on 07-11-2017.
 */

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rosalie.cyclingtracker.Database.Ride;
import com.example.rosalie.cyclingtracker.Database.RideAdapter;

import java.util.ArrayList;

public class WhatsUpActivity extends NavDrawActivity {

    ListView listView;
    TextView whatnew;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_whats_up, null, false);
        drawer.addView(contentView, 0);

        toolbar.setTitle(R.string.nav_whats_up);

        listView = (ListView) findViewById(R.id.list_of_rides);
        ArrayList<Ride> latestRides = new ArrayList<Ride>();

    if(allRides != null) {
        for (int i = (allRides.size() - 5); i < (allRides.size() - 1); i++) {
            latestRides.add(allRides.get(i));
        }
    }else{
        whatnew = (TextView) findViewById(R.id.whatnew);
        whatnew.setText("Please reload the page to see the latest rides made by the community");
    }


        RideAdapter rideAdapter = new RideAdapter(this, latestRides);
        listView.setAdapter(rideAdapter);




        Bundle bundle = this.getIntent().getExtras();

        if(bundle != null) {
            if (this.getIntent().getExtras().get("done") != null) { // Register in the databse here because the google map activity is a fragement
                bundle = null;
                getIntent().removeExtra("done");
                int actualID = allRides.size() + 1;
                double distance = (double) this.getIntent().getExtras().get("distance");
                String date = (String) this.getIntent().getExtras().get("date");
                double time = (double) this.getIntent().getExtras().get("time");


                if (currentUser.getRides() == null) {

                    myRef.child("users").child(mAuth.getCurrentUser().getUid()).child("rides").child("0").setValue("ride" + actualID);
                } else {
                    myRef.child("users").child(mAuth.getCurrentUser().getUid()).child("rides").child(currentUser.getRides().size() + "").setValue("ride" + actualID);

                }
                Ride newRide = new Ride((distance / time), date, distance, actualID, time); //Average speed in m/s

                myRef.child("rides").child("ride" + actualID).setValue(newRide);
            }
        }






    }
}
