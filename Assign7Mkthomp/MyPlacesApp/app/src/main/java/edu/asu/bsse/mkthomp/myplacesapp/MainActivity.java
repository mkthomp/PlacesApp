package edu.asu.bsse.mkthomp.myplacesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener {

    private ListView placesList;
    private PlaceDescription place;
    private PlaceLibrary places;

    private String[] labels;
    private int[] ids;
    private ArrayList<String> fillMaps;

    private String[] placeNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        placesList = (ListView)findViewById(R.id.placeListView);

        try {
            places = new PlaceLibrary(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.prepareAdapter();
        ArrayAdapter aa = new ArrayAdapter(this, R.layout.place_list_item, R.id.place_name, fillMaps);
        placesList.setAdapter(aa);
        placesList.setOnItemClickListener(this);
    }

    private void prepareAdapter(){
        this.placeNames = places.getNames();
        Arrays.sort(this.placeNames);
        fillMaps = new ArrayList<String>();

        for (int i = 0; i < placeNames.length; i++) {
            fillMaps.add(placeNames[i]);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        String[] studNames = places.getNames();
        Arrays.sort(studNames);
        if(position >= 0 && position <= studNames.length) {
            Intent displayPlace = new Intent(this, PlaceDisplayActivity.class);
            displayPlace.putExtra("places", places);
            displayPlace.putExtra("selected", studNames[position-1]);
            this.startActivityForResult(displayPlace, 1);
        }
    }


}
