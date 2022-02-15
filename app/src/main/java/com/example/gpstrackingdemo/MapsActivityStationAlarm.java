package com.example.gpstrackingdemo;

import static java.lang.Math.sqrt;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.gpstrackingdemo.databinding.ActivityMapsStationAlarmBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;








import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivityStationAlarm extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_station_alarm);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     *
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}




















//public class MapsActivityStationAlarm extends FragmentActivity implements OnMapReadyCallback {
//
//
//    Location currentLocation;
//    FusedLocationProviderClient fusedLocationProviderClient;
//    private static final int REQUEST_CODE = 101;
//
//    Intent intent = getIntent();
//    Bundle extras;
//    int selectedIndex = 0;
//
//
//    int updateFast = 1000;
//    int updateSlow = 3000;
//    int updateSuperSlow = 5000;
//
//
//    Marker currentLocationMarker;
//    CountDownTimer countDownTimer;
//
//    String[] stationDisplayNamesList = new String[]{"Jaipur", "Delhi", "Mumbai", "Bangalore", "Hyderabad", "Pune"};
//    double[] stationLatitudes = new double[]{26.9196, 28.6614, 18.9696, 12.9781, 17.3924, 18.5284};
//    double[] stationLongitudes = new double[]{75.7880, 77.2279, 72.8193, 77.5697, 78.4690, 73.8739};
//
//
//    private GoogleMap mMap;
//    private ActivityMapsStationAlarmBinding binding;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
////        binding = ActivityMapsStationAlarmBinding.inflate(getLayoutInflater());
//        setContentView(R.layout.activity_maps_station_alarm);
////        setContentView(binding.getRoot());
//
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        assert mapFragment != null;
//        mapFragment.getMapAsync(this);
//
//
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//        fetchLocation();
//        updateLocationPeriodically();
//
//    }
//
//
//
//    @Override
//    public void onBackPressed()
//    {
//        try {
//            countDownTimer.cancel();
//            super.onBackPressed();
//        }
//        catch (Error e) {
//            Toast.makeText(this, "Error: " + e, Toast.LENGTH_LONG).show();
//        }
//    }
//
//
//    public void updateLocationPeriodically() {
//        countDownTimer = new CountDownTimer(Long.MAX_VALUE, updateFast) {
//            public void onTick(long millisUntilFinished) {
//                fetchLocation();
//            }
//            public void onFinish() {
//                start();
//            }
//        }.start();
//    }
//
//    private void fetchLocation() {
//        if (ActivityCompat.checkSelfPermission(
//                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
//            return;
//        }
//        Task<Location> task = fusedLocationProviderClient.getLastLocation();
//        task.addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                if (location != null) {
//                    currentLocation = location;
//                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//                    assert supportMapFragment != null;
//                    supportMapFragment.getMapAsync(MapsActivityStationAlarm.this);
//                } else {
//                    Toast.makeText(MapsActivityStationAlarm.this, "Cant find location", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
//    /**
//     * Manipulates the map once available.
//     * This callback is triggered when the map is ready to be used.
//     * This is where we can add markers or lines, add listeners or move the camera. In this case,
//     * we just add a marker near Sydney, Australia.
//     * If Google Play services is not installed on the device, the user will be prompted to install
//     * it inside the SupportMapFragment. This method will only be triggered once the user has
//     * installed Google Play services and returned to the app.
//     */
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        mMap.getUiSettings().setZoomControlsEnabled(true);
//        mMap.getUiSettings().setZoomGesturesEnabled(true);
//        mMap.getUiSettings().setCompassEnabled(true);
//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//
//        intent = getIntent();
//        if(intent!=null){
////            Log.d("crazy", "Intent is not null");
//            extras = intent.getExtras();
//            if(extras!=null){
////                Log.d("crazy", "Extras is not null");
//                selectedIndex = extras.getInt("index");
//                if(selectedIndex>=0 && selectedIndex<=10){
////                    Log.d("crazy", "Type is not null");
////                    Log.d("crazy", String.valueOf(selectedIndex));
//                } else Log.d("crazy", "Type is null LOLZ");
//            } else Log.d("crazy", "Extras is null LOLZ");
//        } else Log.d("crazy", "Intent is null LOLZ");
//
//        mMap = googleMap;
//        LatLng jaipur = new LatLng(stationLatitudes[0], stationLongitudes[0]);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(jaipur));
//
//        for(int i=0; i<stationDisplayNamesList.length; i++){
//            LatLng latLng = new LatLng(stationLatitudes[i], stationLongitudes[i]);
//            if(selectedIndex==i)
//                Objects.requireNonNull(mMap.addMarker(new MarkerOptions().position(latLng).title(stationDisplayNamesList[i]))).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//            else
//                mMap.addMarker(new MarkerOptions().position(latLng).title(stationDisplayNamesList[i]));
//        }
//
//
//
//        if(currentLocation!=null){
//            List<Integer> kk = new ArrayList<Integer>();
//            LatLng currentLocationLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//            LatLng stationLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//            currentLocationMarker = mMap.addMarker(new MarkerOptions().position(currentLocationLatLng));
//
//            if(currentLocationMarker != null)
//                if(isInsideCircle(currentLocationLatLng, stationLatLng, 5))
//                    currentLocationMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
//
////            if(currentLocationMarker != null)
////                if(isInside(latLng)) currentLocationMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
////                else currentLocationMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
////            if(currentLocationMarker != null)
////                if(isInside(latLng)) currentLocationMarker.setTitle("Inside");
////                else currentLocationMarker.setTitle("Outside");
////
////            for(int i=0; i<circularFenceLocations.size(); i++){
////                if(circularFenceLocations.get(i)!=null){
////                    if(isInsideCircle(latLng, circularFenceLocations.get(i), 2)){
////                        currentLocationMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
////                    }
////                }
////            }
//
////            googleMap.animateCamera(CameraUpdateFactory.newLatLng(currentLocationLatLng));
//        }
//
//
//
//
//        // Add a marker in Sydney and move the camera
////        LatLng sydney = new LatLng(-34, 151);
////        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
////        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                fetchLocation();
//            }
//        }
//    }
//
//    public boolean isInsideCircle(LatLng current, LatLng point, double km ){
//        double dist = sqrt( Math.pow(current.latitude-point.latitude, 2) + Math.pow(current.longitude-point.longitude, 2));
//        dist*=111.11;
////        Log.d("dist", String.valueOf(dist));
//        return dist<=km;
//    }
//
//
//}