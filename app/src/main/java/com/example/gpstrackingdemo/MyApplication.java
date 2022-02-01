package com.example.gpstrackingdemo;

import android.app.Application;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyApplication extends Application {

    private static MyApplication singleton;

    private List<Location> myLocations;
    private List<LatLng> fenceLocations;

    public List<Location> getMyLocations() {
        return myLocations;
    }

    public void setMyLocations(List<Location> myLocations) {
        this.myLocations = myLocations;
    }

    public List<LatLng> getFenceLocations() {
        return fenceLocations;
    }

    public LatLng getLoc(String dir) {
        if(fenceLocations.size()<=3) return null;
        List<LatLng> temp = fenceLocations;
        Collections.sort(temp, new Comparator<LatLng>() {
            @Override
            public int compare(LatLng a, LatLng b) {
                return Double.compare(a.latitude, b.latitude);
            }
        });
        switch (dir) {
            case "nw" : {
                if(temp.get(2).longitude<temp.get(3).longitude) return temp.get(2);
                else return temp.get(3);
            }
            case "sw" : {
                if(temp.get(0).longitude<temp.get(1).longitude) return temp.get(0);
                else return temp.get(1);
            }
            case "ne" : {
                if(temp.get(2).longitude<temp.get(3).longitude) return temp.get(3);
                else return temp.get(2);
            }
            case "se" : {
                if(temp.get(0).longitude<temp.get(1).longitude) return temp.get(1);
                else return temp.get(0);
            }
            default: return null;
        }

    }

    public void setFenceLocations(List<LatLng> fenceLocations) {
        this.fenceLocations = fenceLocations;
    }

    public MyApplication getInstance() {
        return singleton;
    }

    public void onCreate() {
        super.onCreate();
        singleton = this;
        myLocations = new ArrayList<>();
        fenceLocations = new ArrayList<>();
    }

}
