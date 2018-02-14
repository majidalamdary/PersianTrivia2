package com.sputa.persiantrivia;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class Location extends AppCompatActivity {
    Button btnShowLocation;

    // GPSTracker class
    GPSTracker gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        btnShowLocation = (Button) findViewById(R.id.btnShowLocation);

        // Show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Create class object
                gps = new GPSTracker(Location.this);

                // Check if GPS enabled
                if (gps.canGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();


                    double latitude2 = 37.522698;
                    double longitude2 = 45.064610;

                    Double R = 6371000.0; // metres
                    Double φ1 = Math.toRadians(latitude);
                    Double φ2 = Math.toRadians(latitude2);;
                    Double Δφ = Math.toRadians((latitude2-latitude));
                    Double Δλ = Math.toRadians((longitude2-longitude));

                    Double a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
                            Math.cos(φ1) * Math.cos(φ2) *
                                    Math.sin(Δλ/2) * Math.sin(Δλ/2);
                    Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

                    Double d = R * c;
                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_SHORT).show();

                    try {
                        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                        List<Address> addresses = gcd.getFromLocation(latitude,longitude,1);
                        if (addresses.size() > 0)
                            Log.d("majid",addresses.get(0).getLocality());
                    }
                    catch (Exception e1)
                    {
                        Log.d("majid",e1.getMessage());
                    }


                   // Toast.makeText(getApplicationContext(),String.valueOf(d) , Toast.LENGTH_LONG).show();

                } else {
                    // Can't get location.
                    // GPS or network is not enabled.
                    // Ask user to enable GPS/network in settings.
                    gps.showSettingsAlert();
                }
            }
        });
    }
}

///////finding city from location

// http://maps.googleapis.com/maps/api/geocode/json?address=37.515498,45.05938166&sensor=true
