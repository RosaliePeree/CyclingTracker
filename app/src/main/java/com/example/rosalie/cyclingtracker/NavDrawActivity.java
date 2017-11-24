package com.example.rosalie.cyclingtracker;

/**
 * Created by Rose on 07-11-2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.rosalie.cyclingtracker.Database.Ride;
import com.example.rosalie.cyclingtracker.Database.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.rosalie.cyclingtracker.SettingsActivity.MY_PREFS_NAME;

public class NavDrawActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawer;
    protected Toolbar toolbar;
    protected FloatingActionButton fab;
    protected FirebaseAuth mAuth;
    protected DatabaseReference myRef;
    protected FirebaseDatabase database;
    protected FirebaseUser user;
    protected static User currentUser;
    protected static ArrayList<Ride> allRides;
    public static SharedPreferences getSharedPreferences (Context ctxt) {
        return ctxt.getSharedPreferences("FILE", 0);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_draw);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance(); //Connection to the database
        mAuth = FirebaseAuth.getInstance();
        myRef = database.getReference();
        user = mAuth.getCurrentUser();

    if(mAuth.getCurrentUser() != null) {
        myRef.child("users/" + mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() { //Gets the info about the connected user and put it in connected USer
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User use = dataSnapshot.getValue(User.class);
                Log.i(use.getName(), " user");
                currentUser = use;
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
                allRides = new ArrayList<Ride>();
                for(DataSnapshot dsp : dataSnapshot.getChildren()) {
                    //allRides.add((dsp.getValue(Ride.class)));
                    allRides.add(dsp.getValue(Ride.class));
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }else{
        Toast.makeText(NavDrawActivity.this, "Please reconnect !",
                Toast.LENGTH_LONG).show();
    }


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NavDrawActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.nav_whats_up) {
            intent = new Intent(this, WhatsUpActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_log_out) {
            mAuth.signOut();
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
