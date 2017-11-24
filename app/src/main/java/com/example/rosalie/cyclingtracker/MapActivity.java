
package com.example.rosalie.cyclingtracker;

/**
 * Created by Filip_PC on 16-11-2017
 */

import com.example.rosalie.cyclingtracker.Database.Ride;
import com.example.rosalie.cyclingtracker.Database.User;
import com.example.rosalie.cyclingtracker.Map.CLocation;
import com.example.rosalie.cyclingtracker.Map.IBaseGpsListener;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.content.Context;
import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, IBaseGpsListener {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private GoogleMap mMap;

    //stuff for drawing the line
    private ArrayList<LatLng> points; //added
    Polyline line; //added
    private static final String TAG = "MainActivity";
    private static final long INTERVAL = 1000 * 60; //1 minute
    private static final float SMALLEST_DISPLACEMENT = 0.25F; //quarter of a meter
    private Location mCurrentLocation;
    private LocationRequest mLocationRequest;

    private boolean stopped = false;
    int buttonState;
    ToggleButton btnStart;
    Button btnStop;
    private Handler mHandler;
    private boolean mStarted;

    public LatLng startPoint;
    public LatLng endPoint;

    TextView currentTime;
    long startTime;
    long activityLength;
    long pauseTime;
    float returnedLowestSpeed = 0;
    float returnedHighestSpeed = 0;
    float lowestSpeed;
    float highestSpeed;
    double iPromiseThisIsTheEndDistance = 0;
    double returnedDistance = 0;

    float setMinimalspeed = 3;
    float setMaximumspeed = 80;
    float bCurrentSpeed = 0;
    public boolean speedCondition = true;
    boolean tooFast = false;
    boolean tooSlow = false;

    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private static User currentUser;
    private static ArrayList<Ride> allRides;



    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setSmallestDisplacement(SMALLEST_DISPLACEMENT); //added
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        points = new ArrayList<LatLng>(); //initialize an arraylist positions on the map

        setContentView(R.layout.activity_map);
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        final LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        builder.setAlwaysShow(true);

        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }*/

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }

        //  LatLng currentLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17));

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
        this.updateSpeed(null);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activity_map);
        mapFragment.getMapAsync(this);

        createLocationRequest();

        btnStart = (ToggleButton) findViewById(R.id.button); //button for starting the exercise
        btnStop = (Button) findViewById(R.id.stop);
        currentTime = (TextView) findViewById(R.id.time123);
        mHandler = new Handler();
        buttonState = 1;    //used for controlling the toggle button
        btnStart.setEnabled(true);

        btnStop.setOnClickListener(new View.OnClickListener() // button for stopping the exercise, returning info to database, and resetting everything to 0
                                   {
                                       @Override
                                       public void onClick(View view) {
                                           if (buttonState == 3) {


                                               stopped = true;
                                               //startPoint = points.get(0);
                                               //endPoint = points.get(points.size() - 1);
                                               activityLength = startTime;
                                               returnedHighestSpeed = highestSpeed;
                                               returnedLowestSpeed = lowestSpeed;


                                               for (int i = 0; i < points.size() - 1; i++) {
                                                   LatLng temp = points.get(i);
                                                   LatLng other = points.get(i + 1);

                                                   double endDistance = distance(temp.latitude, temp.longitude, other.latitude, other.longitude);
                                                   iPromiseThisIsTheEndDistance = iPromiseThisIsTheEndDistance + endDistance;


                                               }

                                               returnedDistance = iPromiseThisIsTheEndDistance;
                                               mStarted = false;
                                               mHandler.removeCallbacks(mRunnable);
                                               buttonState = 1;

                                               Date cDate = new Date();
                                               String fDate = new SimpleDateFormat("dd/MM/yyyy").format(cDate);  //Create date of the activity
                                               points = new ArrayList<LatLng>();
                                               lowestSpeed = 0;
                                               highestSpeed = 0;

                                               mMap.clear();

                                               Intent intent = new Intent(MapActivity.this, WhatsUpActivity.class);

                                               intent.putExtra("done", true);
                                               intent.putExtra("distance", iPromiseThisIsTheEndDistance);
                                               intent.putExtra("date", fDate);
                                               //intent.putExtra("highestSpeed", returnedHighestSpeed); will be implemented later
                                               //intent.putExtra("lowestSpeed", returnedLowestSpeed);
                                               String timeToParse = currentTime.getText().toString();
                                               double timeToSend = 0;
                                               String[] tokens = timeToParse.split(":");
                                               timeToParse = tokens[0] + " " + tokens[1] + " " + tokens[2];
                                               timeToSend = ((Double.parseDouble(tokens[0])) + (Double.parseDouble("0." + tokens[1])));
                                               Log.i("time", timeToParse);
                                               intent.putExtra("time", timeToSend );
                                               startActivityForResult(intent, 1);

                                           }

                                       }

                                   }

        );

        btnStart.setOnClickListener(new View.OnClickListener() // button for starting and pausing the exercise

        {

            @Override
            public void onClick(View view) {

                if (buttonState == 1) {

                    mStarted = true;
                    mHandler.postDelayed(mRunnable, 10L);
                    startTime = System.currentTimeMillis();

                    buttonState = 2;
                } else if (buttonState == 2)

                {

                    mStarted = false;
                    mHandler.removeCallbacks(mRunnable); // add a variable and store the value as double and add it to the counter
                    pauseTime = System.currentTimeMillis();


                    buttonState = 3;
                } else if (buttonState == 3) {


                    mStarted = true;
                    mHandler.postDelayed(mRunnable, 10L);
                    startTime = startTime - pauseTime;
                    mHandler.removeCallbacks(mRunnable);
                    mHandler.postDelayed(mRunnable, 10L);
                    startTime += System.currentTimeMillis();

                    buttonState = 2;
                }
            }




        });
    }

    public void finish() {
        super.finish();
        System.exit(0);
    }

    private void updateSpeed(CLocation location) {
        float nCurrentSpeed = 0;

        if (location != null) {
            nCurrentSpeed = location.getSpeed();

            if (lowestSpeed > nCurrentSpeed) lowestSpeed = nCurrentSpeed;
            else if (highestSpeed < nCurrentSpeed) highestSpeed = nCurrentSpeed;
        }
        if (nCurrentSpeed > setMinimalspeed && nCurrentSpeed < setMaximumspeed)
        {
            speedCondition = true;
            tooFast = false;
            tooSlow = false;
        }
        else if (nCurrentSpeed < setMinimalspeed)
        {
            speedCondition = false;
            tooFast = false;
            tooSlow = true;
        }
        else if (nCurrentSpeed > setMaximumspeed)
        {
            speedCondition = false;
            tooFast = true;
            tooSlow = false;
        }

        Formatter fmt = new Formatter(new StringBuilder());
        fmt.format(Locale.US, "%5.1f", nCurrentSpeed);
        String strCurrentSpeed = fmt.toString();
        strCurrentSpeed = strCurrentSpeed.replace(' ', '0');

        String strUnits = "kilometers/hour";

        TextView txtCurrentSpeed = (TextView) this.findViewById(R.id.txtCurrentSpeed);
        if (tooSlow == true)
        {
            String tooSlow = "You are moving too slow!";
            txtCurrentSpeed.setText(tooSlow);
        }
        else if(tooFast == true )
        {
            String tooFst = "You are moving too fast!";
            txtCurrentSpeed.setText(tooFst);
        }
        else txtCurrentSpeed.setText(strCurrentSpeed + " " + strUnits);
    }

    @Override
    public void onGpsStatusChanged(int event) {
        // TODO Auto-generated method stub

    }

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mStarted) {
                long milis = (System.currentTimeMillis() - startTime);
                long seconds = milis / 1000;
                currentTime.setText(String.format("%03d:%02d:%02d", seconds / 60, seconds % 60, milis % 100));
                mHandler.postDelayed(mRunnable, 10L);
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart fired ..............");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop fired ..............");
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged....");
        CLocation myLocation = new CLocation(location);
        if (mStarted) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            LatLng latLng = new LatLng(latitude, longitude);
            this.updateSpeed(myLocation);
            points.add(latLng);
            addMarker();
            redrawLine();

        }
        mCurrentLocation = location;
    }

    private void redrawLine() {
        if (speedCondition == true) {
            mMap.clear();  //clears all Markers and Polylines
            PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
            for (int i = 0; i < points.size(); i++) {
                LatLng point = points.get(i);
                options.add(point);
            }
            addMarker(); //add Marker in current position
            line = mMap.addPolyline(options); //add Polyline
        }
    }

    private void addMarker() {

        MarkerOptions options = new MarkerOptions();
        LatLng currentLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        options.position(currentLatLng);
        mMap.addMarker(options);
        Log.d(TAG, "Marker added...");

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17));
        Log.d(TAG, "Zoom done...");

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

       /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }*/
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return;
        }
        googleMap.setMyLocationEnabled(true);

    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i("onPause...", "paused");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public double distance(double lat_a, double lng_a, double lat_b, double lng_b) {
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b - lat_a);
        double lngDiff = Math.toRadians(lng_b - lng_a);
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff / 2) * Math.sin(lngDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        int meterConversion = 1609;

        return Double.valueOf(distance * meterConversion).doubleValue();
    }
/*
    private void requestLocation()
    {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        String provider = manager.getBestProvider(criteria, true);
        manager.requestLocationUpdates(provider, 10000, 10, this);
    }
*/


}
