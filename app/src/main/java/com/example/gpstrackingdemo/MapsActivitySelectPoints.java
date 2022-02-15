package com.example.gpstrackingdemo;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.gpstrackingdemo.databinding.ActivityMapsSelectPointsBinding;

import java.util.List;

public class MapsActivitySelectPoints extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsSelectPointsBinding binding;

    Intent intent = getIntent();
    Bundle extras;
    int value;
    String type;

    List<LatLng> fenceLocations;
    List<LatLng> circularFenceLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsSelectPointsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_monitor);
        mapFragment.getMapAsync(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);


        intent = getIntent();
        if(intent!=null){
            Log.d("crazy", "Intent is not null");
            extras = intent.getExtras();
            if(extras!=null){
                Log.d("crazy", "Extras is not null");
                type = extras.getString("type");
                if(type!=null){
                    Log.d("crazy", "Type is not null");
                    Log.d("crazy", type);
                } else Log.d("crazy", "Type is null LOLZ");
            } else Log.d("crazy", "Extras is null LOLZ");
        } else Log.d("crazy", "Intent is null LOLZ");

        if (type!=null && type.equals("circle")) {

            MyApplication myApplication = (MyApplication)getApplicationContext();
            circularFenceLocations = myApplication.getCircularFenceLocations();
            Log.d("POS", circularFenceLocations.toString());
            for(int i=0; i<circularFenceLocations.size(); i++){
                mMap.addMarker(new MarkerOptions().position(circularFenceLocations.get(i)).title(circularFenceLocations.get(i).toString()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            }

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng point) {
//                allPoints.add(point);

                    if(circularFenceLocations.size()<=3) {
                        circularFenceLocations.add(new LatLng(point.latitude, point.longitude));
                        mMap.addMarker(new MarkerOptions().position(point).title(point.toString()));
                    } else {
                        Toast.makeText(MapsActivitySelectPoints.this, "Select 4 points only LOLZZ", Toast.LENGTH_SHORT).show();
                    }
//                mMap.clear();
//                mMap.addMarker(new MarkerOptions().position(point).title(point.toString()));
                }
            });

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    LatLng latLng = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                    circularFenceLocations.remove(latLng);
                    marker.remove();
                    return false;
                }
            });

        } else {

            MyApplication myApplication = (MyApplication)getApplicationContext();
            fenceLocations = myApplication.getFenceLocations();
            Log.d("POS", fenceLocations.toString());
            for(int i=0; i<fenceLocations.size(); i++){
                mMap.addMarker(new MarkerOptions().position(fenceLocations.get(i)).title(fenceLocations.get(i).toString()));
            }

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng point) {
//                allPoints.add(point);

                    if(fenceLocations.size()<=3) {
                        fenceLocations.add(new LatLng(point.latitude, point.longitude));
                        mMap.addMarker(new MarkerOptions().position(point).title(point.toString()));
                    } else {
                        Toast.makeText(MapsActivitySelectPoints.this, "Select 4 points only", Toast.LENGTH_SHORT).show();
                    }
//                mMap.clear();
//                mMap.addMarker(new MarkerOptions().position(point).title(point.toString()));
                }
            });

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    LatLng latLng = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                    fenceLocations.remove(latLng);
                    marker.remove();
                    return false;
                }
            });




        }
        // Add a marker in Sydney and move the camera

//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


    }
}