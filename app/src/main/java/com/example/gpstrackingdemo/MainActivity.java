package com.example.gpstrackingdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int FAST_UPDATE_INTERVAL = 2;
    private static final int PERMISSION_FINE_LOCATION = 99;

    List<Button> fenceButtons;

    TextView tv_lat, tv_lon, tv_altitude, tv_accuracy, tv_speed, tv_sensor, tv_updates, tv_address;
    TextView tv_wayPointCounts;
    TextView tv_nw, tv_sw, tv_ne, tv_se;
    Button btn_f1, btn_f2, btn_f3, btn_f4;
    Button btn_monitor, btn_selectPoints, btn_refresh;
    Button btn_newWaypoint, btn_showWayPointList, btn_showMap;
    Button btn_stationAlarm;
    Switch sw_locationupdates, sw_gps;

    Boolean updateOn = false;

    Location currentLocation;
    List<Location> savedLocations;

    List<LatLng> fenceLocations;

    LocationRequest locationRequest;

    LocationCallback locationCallBack;

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_lat = findViewById(R.id.tv_lat);
        tv_lon = findViewById(R.id.tv_lon);
        tv_altitude = findViewById(R.id.tv_altitude);
        tv_accuracy = findViewById(R.id.tv_accuracy);
        tv_speed = findViewById(R.id.tv_speed);
        tv_sensor = findViewById(R.id.tv_sensor);
        tv_updates = findViewById(R.id.tv_updates);
        tv_address = findViewById(R.id.tv_address);
        sw_gps = findViewById(R.id.sw_gps);
        sw_locationupdates = findViewById(R.id.sw_locationsupdates);
        btn_newWaypoint = findViewById(R.id.btn_newWayPoint);
        btn_showWayPointList = findViewById(R.id.btn_showWayPointList);
        btn_showMap = findViewById(R.id.btn_showMap);
        btn_selectPoints = findViewById(R.id.btn_selectPoints);
        btn_monitor = findViewById(R.id.btn_monitor);
        btn_refresh = findViewById(R.id.btn_refresh);
        tv_wayPointCounts = findViewById(R.id.tv_countOfCrumbs);
        btn_stationAlarm = findViewById(R.id.btn_station_alarm);
        btn_f1 = findViewById(R.id.btn_f1);
        btn_f2 = findViewById(R.id.btn_f2);
        btn_f3 = findViewById(R.id.btn_f3);
        btn_f4 = findViewById(R.id.btn_f4);
        fenceButtons = new ArrayList<>(); //[, , , btn_f4];
        fenceButtons.add(btn_f1);
        fenceButtons.add(btn_f2);
        fenceButtons.add(btn_f3);
        fenceButtons.add(btn_f4);

        tv_nw = findViewById(R.id.tv_nw);
        tv_sw = findViewById(R.id.tv_sw);
        tv_ne = findViewById(R.id.tv_ne);
        tv_se = findViewById(R.id.tv_se);

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        locationCallBack = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                updateUIValues(locationResult.getLastLocation());
            }
        };

        btn_newWaypoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get gps location

                //add the new location to the global list
                MyApplication myApplication = (MyApplication) getApplicationContext();
                savedLocations = myApplication.getMyLocations();
                savedLocations.add(currentLocation);
                tv_wayPointCounts.setText(Integer.toString(savedLocations.size()));
            }
        });


        btn_showWayPointList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ShowSavedLocationsList.class);
                startActivity(i);
            }
        });


        btn_stationAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, StationAlarm.class);
                startActivity(i);
            }
        });

        btn_showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(i);
            }
        });

        btn_selectPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MapsActivitySelectPoints.class);
                startActivity(i);
            }
        });

        for (int t = 0; t < fenceButtons.size(); t++) {
            int finalT = t;
            fenceButtons.get(t).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, MapsActivitySelectPoints.class);
                    i.putExtra("index", finalT);
                    i.putExtra("type", "circle");
                    startActivity(i);
                }
            });
        }


        btn_monitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MapsActivityMonitor.class);
                startActivity(i);
            }
        });
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUIValues(null);
            }
        });


        sw_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sw_gps.isChecked()) {
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    tv_sensor.setText("Using GPS sensors");
                } else {
                    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                    tv_sensor.setText("Using Towers + Wifi");
                }
            }
        });

        sw_locationupdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sw_locationupdates.isChecked()) {
                    startLocationUpdates();
                } else {
                    stopLocationUpdates();
                }
            }
        });

        updateGPS();
    }

    private void stopLocationUpdates() {
        tv_updates.setText("Location is NOT being updated");

        tv_lat.setText("Not available");
        tv_lon.setText("Not available");
        tv_speed.setText("Not available");
        tv_address.setText("Not available");
        tv_accuracy.setText("Not available");
        tv_altitude.setText("Not available");
        tv_sensor.setText("Not available");

        fusedLocationProviderClient.removeLocationUpdates(locationCallBack);

    }

    private void startLocationUpdates() {
        tv_updates.setText("Location is being updated");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_FINE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateGPS();;
                } else {
                    Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }

    }


    private void updateGPS() {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(@NonNull Location location) {

                    updateUIValues(location);
                    currentLocation = location;
                }
            });
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FINE_LOCATION);
            }
        }



    }

    private void updateUIValues(Location location) {

        MyApplication myApplication = (MyApplication)getApplicationContext();
        savedLocations = myApplication.getMyLocations();
        fenceLocations = myApplication.getFenceLocations();
        //show the number of waypoitns saved

        tv_wayPointCounts.setText(Integer.toString(fenceLocations.size()));
//        tv_wayPointCounts.setText(Integer.toString(savedLocations.size()));


        tv_nw.setText(getNw());
        tv_sw.setText(getSw());
        tv_ne.setText(getNe());
        tv_se.setText(getSe());


        if(location==null) return;
        tv_lat.setText(String.valueOf(location.getLatitude()));
        tv_lon.setText(String.valueOf(location.getLongitude()));
        tv_accuracy.setText(String.valueOf(location.getAccuracy()));

        if (location.hasAltitude()) {
            tv_altitude.setText(String.valueOf(location.getAltitude()));
        } else {
            tv_altitude.setText("Not Available");
        }
        if (location.hasSpeed()) {
            tv_speed.setText(String.valueOf(location.hasSpeed()));
        } else {
            tv_speed.setText("Not Available");
        }

    }

    String getNw(){
        MyApplication myApplication = (MyApplication)getApplicationContext();
        if(myApplication.getLoc("nw")==null) return "Not Available";
        return myApplication.getLoc("nw").toString();
    }
    String getSw(){
        MyApplication myApplication = (MyApplication)getApplicationContext();
        if(myApplication.getLoc("sw")==null) return "Not Available";
        return myApplication.getLoc("sw").toString();
    }
    String getNe(){
        MyApplication myApplication = (MyApplication)getApplicationContext();
        if(myApplication.getLoc("ne")==null) return "Not Available";
        return myApplication.getLoc("ne").toString();
    }
    String getSe(){
        MyApplication myApplication = (MyApplication)getApplicationContext();
        if(myApplication.getLoc("se")==null) return "Not Available";
        return myApplication.getLoc("se").toString();
    }
}