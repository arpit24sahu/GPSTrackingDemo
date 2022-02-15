package com.example.gpstrackingdemo;

import static java.lang.Math.atan2;
import static java.lang.Math.sqrt;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.gpstrackingdemo.databinding.ActivityMapsMonitorBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class MapsActivityMonitor extends FragmentActivity implements OnMapReadyCallback {

    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;

    int updateFast = 1000;
    int updateSlow = 3000;
    int updateSuperSlow = 5000;

    Marker currentLocationMarker;
    List<LatLng> circularFenceLocations;

    private GoogleMap mMap;
    private ActivityMapsMonitorBinding binding;

    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsMonitorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_monitor);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();
        updateLocationPeriodically();
    }

    @Override
    public void onBackPressed()
    {
        try {
            countDownTimer.cancel();
            super.onBackPressed();
        }
        catch (Error e) {
            Toast.makeText(this, "Error: " + e, Toast.LENGTH_LONG).show();
        }
    }


    public void updateLocationPeriodically() {
        countDownTimer = new CountDownTimer(Long.MAX_VALUE, updateFast) {
            public void onTick(long millisUntilFinished) {
                fetchLocation();
            }
            public void onFinish() {
                start();
            }
        }.start();
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_monitor);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(MapsActivityMonitor.this);
                } else {
                    Toast.makeText(MapsActivityMonitor.this, "Cant find location", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

        MyApplication myApplication = (MyApplication)getApplicationContext();
        circularFenceLocations = myApplication.getCircularFenceLocations();
        if(myApplication.getFenceLocations().size()>=4) {
            mMap.addMarker(new MarkerOptions().position(myApplication.getLoc("nw")).title("nw"));
            mMap.addMarker(new MarkerOptions().position(myApplication.getLoc("sw")).title("sw"));
            mMap.addMarker(new MarkerOptions().position(myApplication.getLoc("ne")).title("ne"));
            mMap.addMarker(new MarkerOptions().position(myApplication.getLoc("se")).title("se"));
        } else {
            Toast.makeText(MapsActivityMonitor.this, "Not Selected", Toast.LENGTH_SHORT).show();
        }

        for(int i=0; i<circularFenceLocations.size(); i++){
            if(circularFenceLocations.get(i)!=null){
                mMap.addMarker(new MarkerOptions().position(circularFenceLocations.get(i)));
            }
        }

        if(currentLocation!=null){
            List<Integer> kk = new ArrayList<Integer>();
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            currentLocationMarker = mMap.addMarker(new MarkerOptions().position(latLng));
            if(currentLocationMarker != null)
                if(isInside(latLng)) currentLocationMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                else currentLocationMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            if(currentLocationMarker != null)
                if(isInside(latLng)) currentLocationMarker.setTitle("Inside");
                else currentLocationMarker.setTitle("Outside");

            for(int i=0; i<circularFenceLocations.size(); i++){
                if(circularFenceLocations.get(i)!=null){
                    if(isInsideCircle(latLng, circularFenceLocations.get(i), 2)){
                        currentLocationMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                    }
                }
            }

//            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation();
            }
        }
    }

    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId, boolean inside) {

        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public boolean isInside(LatLng current){
        MyApplication myApplication = (MyApplication)getApplicationContext();
        LatLng nw = myApplication.getLoc("nw");
        LatLng sw = myApplication.getLoc("sw");
        LatLng se = myApplication.getLoc("se");
        LatLng ne = myApplication.getLoc("ne");
        List<LatLng> poly = new ArrayList<LatLng>();
        poly.add(nw); poly.add(sw); poly.add(se); poly.add(ne);
        Log.d("checkInside", String.valueOf(pointIsInPoly(current,poly)));

        addLineMarkers(nw,sw);
        addLineMarkers(sw,se);
        addLineMarkers(se,ne);
        addLineMarkers(ne,nw);
        return pointIsInPoly(current,poly);
    }

    public boolean isInsideCircle(LatLng current, LatLng point, double km ){
        double dist = sqrt( Math.pow(current.latitude-point.latitude, 2) + Math.pow(current.longitude-point.longitude, 2));
        dist*=111.11;
        Log.d("dist", String.valueOf(dist));
        return dist<=km;
    }

    public void addLineMarkers(LatLng a, LatLng b){
        double delta_lat = (b.latitude-a.latitude)/3;
        double delta_lon = (b.longitude-a.longitude)/3;
        for(int i=1; i<3; i++){
            mMap.addMarker(new MarkerOptions().position(new LatLng(a.latitude+delta_lat*i,a.longitude+delta_lon*i)));
        }
    }

    public boolean pointIsInPoly(LatLng p, List<LatLng> polygon) {
        if(p==null) return false;
        if(polygon.size()<=3) return false;
        if(polygon.get(0)==null||polygon.get(1)==null||polygon.get(2)==null||polygon.get(3)==null)
            return false;
        boolean isInside = false;
        double minX = polygon.get(0).latitude, maxX = polygon.get(0).latitude;
        double minY = polygon.get(0).longitude , maxY = polygon.get(0).longitude;
        for (int n = 1; n < polygon.size(); n++) {
            LatLng q = polygon.get(n);
            minX = Math.min(q.latitude, minX);
            maxX = Math.max(q.latitude, maxX);
            minY = Math.min(q.longitude , minY);
            maxY = Math.max(q.longitude , maxY);
        }

        if (p.latitude < minX || p.latitude > maxX || p.longitude  < minY || p.longitude  > maxY) {
            return false;
        }

        int i, j;
        for (i=0, j= polygon.size() - 1; i < polygon.size(); j = i++) {
            if ( (polygon.get(i).longitude  > p.longitude ) != (polygon.get(j).longitude  > p.longitude ) &&
                    p.latitude < (polygon.get(j).latitude - polygon.get(i).latitude) * (p.longitude  - polygon.get(i).longitude ) / (polygon.get(j).longitude  - polygon.get(i).longitude ) + polygon.get(i).latitude ) {
                isInside = !isInside;
            }
        }

        return isInside;
    }

}