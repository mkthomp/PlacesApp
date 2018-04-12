package edu.asu.bsse.mkthomp.myplacesapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class PlaceDisplayActivity extends AppCompatActivity {

    private EditText description, addrTitle, addrStreet, lat, lon, elevation, category;
    private TextView name;
    private String myPlaceName;
    private PlaceLibrary places;
    private PlaceDescription myPlace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_display);

        Bundle extras = getIntent().getExtras();

        places = (PlaceLibrary) extras.getSerializable("places");
        myPlaceName = extras.getString("selected");
        myPlace = places.get(myPlaceName);

        name = findViewById(R.id.nameTextView);
        name.setText("Showing Details for " + myPlace.getName());
        description = findViewById(R.id.editDescription);
        description.setText(myPlace.getDescription());
        addrTitle = findViewById(R.id.editAddrTitle);
        addrTitle.setText(myPlace.getAddressTitle());
        addrStreet = findViewById(R.id.editAddrStreet);
        addrStreet.setText(myPlace.getAddressStreet());
        lat = findViewById(R.id.editLatitude);
        lat.setText(myPlace.getLatitude().toString());
        lon = findViewById(R.id.editLongitude);
        lon.setText(myPlace.getLongitude().toString());
        elevation = findViewById(R.id.editElevation);
        elevation.setText(myPlace.getElevation().toString());
        category = findViewById(R.id.editCategory);
        category.setText(myPlace.getCategory());
    }
}
