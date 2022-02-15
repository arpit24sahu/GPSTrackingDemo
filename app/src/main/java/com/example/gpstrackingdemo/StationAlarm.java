package com.example.gpstrackingdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class StationAlarm extends AppCompatActivity {

    TextView labelSelectStation, labelStationNum, labelStationName;
    TextView labelStationLat, labelStationLong;
    TextView stationNumber, stationName, stationLong, stationLat;
    Button btn_stationAlarmStartTracking;

    Spinner stationDropDown;

    int selectedIndex = 0;

    String[] stationDisplayNamesList = new String[]{"Jaipur", "Delhi", "Mumbai", "Bangalore", "Hyderabad", "Pune"};
    double[] stationLatitudes = new double[]{26.9196, 28.6614, 18.9696, 12.9781, 17.3924, 18.5284};
    double[] stationLongitudes = new double[]{75.7880, 77.2279, 72.8193, 77.5697, 78.4690, 73.8739};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_alarm);

        stationNumber = findViewById(R.id.tv_stationNumber);
        stationName = findViewById(R.id.tv_stationName);
        stationLong = findViewById(R.id.tv_stationLong);
        stationLat = findViewById(R.id.tv_stationLat);
        btn_stationAlarmStartTracking = findViewById(R.id.btn_trackStation);

        btn_stationAlarmStartTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StationAlarm.this, MapsActivityStationAlarm.class);
                i.putExtra("index", selectedIndex);
                startActivity(i);
            }
        });

        stationDropDown = findViewById(R.id.sp_stationDropdown);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, stationDisplayNamesList);
        stationDropDown.setAdapter(adapter);



        stationDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stationNumber.setText(String.valueOf(position));

                selectedIndex = position;

                stationName.setText(String.format("%s Railway Station", (String) parent.getItemAtPosition(position)));
//                stationName.setText((String) parent.getItemAtPosition(position) + " Railway Station");
                stationLat.setText(String.valueOf(stationLatitudes[position]));
                stationLong.setText(String.valueOf(stationLongitudes[position]));
//                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
//        stationDropDown.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
    }

//    @Override
//    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
//        stationNumber.setText(String.valueOf(position));
//
////        switch (position) {
////            case 0:
////                // Whatever you want to happen when the first item gets selected
////                break;
////            case 1:
////                // Whatever you want to happen when the second item gets selected
////                break;
////            case 2:
////                // Whatever you want to happen when the thrid item gets selected
////                break;
////
////        }
//    }



}