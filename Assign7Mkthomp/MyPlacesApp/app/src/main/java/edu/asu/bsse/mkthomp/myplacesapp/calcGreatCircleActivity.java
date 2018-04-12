package edu.asu.bsse.mkthomp.myplacesapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class calcGreatCircleActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener {

    private Spinner spin1, spin2;
    private TextView greatCircleDistance, initialBearing;
    private PlaceLibrary places;
    private PlaceDescription place1, place2;
    private String[] placeNames;
    private Double lat1, lon1, lat2, lon2;
    private String gcd, ib;
    private NumberFormat format1, format2;
    int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_great_circle);
        counter = 0;

        Bundle extras = getIntent().getExtras();

        spin1 = findViewById(R.id.spinner1);
        spin2 = findViewById(R.id.spinner2);

        greatCircleDistance = findViewById(R.id.gcdOutput);
        initialBearing = findViewById(R.id.bearingOutput);
        format1 = new DecimalFormat("#.####");
        format2 = new DecimalFormat("#.#");

        places = (PlaceLibrary) extras.getSerializable("places");

        this.prepareAdapter();
        ArrayAdapter aa = new ArrayAdapter(this, R.layout.simple_spinner_item,
                new ArrayList<>(Arrays.asList(placeNames)));
        aa.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spin1.setAdapter(aa);
        spin1.setOnItemSelectedListener(this);
        spin2.setAdapter(aa);
        spin2.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        Spinner spinner = (Spinner) parent;
        String name;
        if(spinner.getId() == R.id.spinner1) {
            name = placeNames[position];
            place1 = places.get(name);
            lat1 = place1.getLatitude();
            lon1 = place1.getLongitude();
            Toast toast = Toast.makeText(this, (name + " was picked for place 1"), Toast.LENGTH_SHORT);
            toast.show();
        }else if(spinner.getId() == R.id.spinner2) {
            name = placeNames[position];
            place2 = places.get(name);
            lat2 = place2.getLatitude();
            lon2 = place2.getLongitude();
            Toast toast = Toast.makeText(this, (name + " was picked for place 2"), Toast.LENGTH_SHORT);
            toast.show();
        }

        if(counter > 0) {
            calculateGreatCircleDistance(lat1, lon1, lat2, lon2);
            calculateInitialBearing(lat1, lon1, lat2, lon2);
        }

        if (gcd != null) {
            greatCircleDistance.setText(gcd + " KM");
        }

        if (ib != null) {
            initialBearing.setText(ib + " Degrees");
        }
        counter++;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void prepareAdapter() {
        placeNames = places.getNames();
        Arrays.sort(placeNames);
    }

    private void calculateGreatCircleDistance(Double latitude1, Double longitude1, Double latitude2, Double longitude2) {
        double x1 = Math.toRadians(latitude1);
        double y1 = Math.toRadians(longitude1);
        double x2 = Math.toRadians(latitude2);
        double y2 = Math.toRadians(longitude2);

        double a = Math.pow(Math.sin((x2-x1)/2), 2)
                + Math.cos(x1) * Math.cos(x2) * Math.pow(Math.sin((y2-y1)/2), 2);

        // great circle distance in radians
        double angle2 = 2 * Math.asin(Math.min(1, Math.sqrt(a)));

        // convert back to degrees
        angle2 = Math.toDegrees(angle2);

        // each degree on a great circle of Earth is 60 nautical miles
        double distance2 = 60 * angle2;

        gcd = format1.format(distance2 * 1.852);

    }

    private void calculateInitialBearing(Double latitude1, Double longitude1, Double latitude2, Double longitude2) {
        double x1 = Math.toRadians(latitude1);
        double y1 = Math.toRadians(longitude1);
        double x2 = Math.toRadians(latitude2);
        double y2 = Math.toRadians(longitude2);

        double x = Math.sin(y2 - y1) * Math.cos(x2);
        double y = Math.cos(x1) * Math.sin(x2) - Math.sin(x1) * Math.cos(x2) * Math.cos(y2-y1);
        double bearing = Math.toDegrees(Math.atan2(y,x));

        ib = format2.format(bearing);
    }
}
