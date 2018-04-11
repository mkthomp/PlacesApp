package edu.asu.bsse.mkthomp.myplacesapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener {

    private ListView placesList;
    private PlaceDescription place;
    private PlaceLibrary places;

    private String[] labels;
    private int[] ids;
    private ArrayList<String> fillMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
