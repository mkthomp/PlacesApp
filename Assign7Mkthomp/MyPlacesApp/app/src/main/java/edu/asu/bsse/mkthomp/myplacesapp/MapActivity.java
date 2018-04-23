package edu.asu.bsse.mkthomp.myplacesapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener,
        DialogInterface.OnClickListener {

    private PlaceLibrary places;
    private GoogleMap myMap;
    private EditText in;
    private LatLng point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        places = new PlaceLibrary();
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapLongClick(LatLng point) {
        this.point = point;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_text));
        in = new EditText(this);
        in.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(in);
        builder.setNegativeButton(getString(R.string.cancel), this);
        builder.setPositiveButton(getString(R.string.ok),this);
        builder.show();
    }

    // DialogInterface.OnClickListener method. Get the result of the Alert View.
    @Override
    public void onClick(DialogInterface dialog, int which){
        String result = (which==DialogInterface.BUTTON_POSITIVE)? getString(R.string.ok):
                getString(R.string.cancel);
        android.util.Log.d(this.getClass().getSimpleName(),"onClick result: "+result+
                " input is: "+in.getText());
        //Need to add place to place library
        myMap.addMarker(new MarkerOptions()
                .position(point)
                .title(in.getText().toString())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
    }

    @Override
    public void onMapReady(GoogleMap map) {
        myMap = map;
        myMap.setOnMapLongClickListener(this);
//        for (String aKey : places.keySet()) {
//            Place aPlace = places.get(aKey);
//            map.addMarker(new MarkerOptions().position(new LatLng(aPlace.lat, aPlace.lon)).title(aKey));
//        }
//        Place tempe = places.get("ASU-Brickyard");
        map.addMarker(new MarkerOptions().position(new LatLng(33.608979, -112.159469)).title("ASU-West"));
        map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(33.608979, -112.159469),
                (float)9.0, (float)0.0, (float)0.0)));

    }
}
