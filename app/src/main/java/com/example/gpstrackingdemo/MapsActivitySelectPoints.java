package com.example.gpstrackingdemo;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.gpstrackingdemo.databinding.ActivityMapsSelectPointsBinding;

import java.util.List;

public class MapsActivitySelectPoints extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsSelectPointsBinding binding;

    List<LatLng> fenceLocations;

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

        // Add a marker in Sydney and move the camera

//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


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
}