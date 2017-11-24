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
import com.google.maps.android.MarkerManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

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
            String rideName;
            String[] rideNumber;
        for (int i = 0; i < (currentUser.getRides().size()); i++) {

            rideName = currentUser.getRides().get(i);
            rideNumber = rideName.split("ride");
            for(int j = 0; j < allRides.size(); j++){
                if(allRides.get(j).getId() == Integer.parseInt(rideNumber[1])) {
                    latestRides.add(allRides.get(j));
                }
            }
        }

    }else{
        whatnew = (TextView) findViewById(R.id.whatnew);
        whatnew.setText("Please reload the page to your rides");
    }
        Collections.sort(latestRides, new Comparator<Ride>(){
            public int compare(Ride s1, Ride s2) {
                Integer price1 = s1.getId();
                Integer price2 = s2.getId();
                return price1.compareTo(price2);
            }
        });
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

        public int compare(Ride o1, Ride o2) {
                Integer price1 = o1.getId();
                Integer price2 = o2.getId();
                return price1.compareTo(price2);
        }
}
