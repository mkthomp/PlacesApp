package edu.asu.bsse.mkthomp.myplacesapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Copyright (c) 2018 Mary Insua,
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Purpose: an Android App that uses a SQLite3 database to display places and their descriptions.
 *
 * The app allows the user to view, edit, and update places from the database,
 * and add new places to the database.
 *
 * @author Mary Insua mkthomp@asu.edu
 * @version 1.0
 */


public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener {
    private static final boolean debugon = true;

    private ListView placesList;
    //private PlaceDescription place;
    private PlaceLibrary placesFromJSONfile, placesFromDatabase;

    private String[] labels;
    private int[] ids;
    private ArrayList<String> al;

    private String[] placeNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        placesList = (ListView)findViewById(R.id.placeListView);

        try {
            placesFromJSONfile = new PlaceLibrary(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.prepareAdapter();
        this.createPlaceLibrary();
        ArrayAdapter aa = new ArrayAdapter(this, R.layout.place_list_item, R.id.place_name, al);
        placesList.setAdapter(aa);
        placesList.setOnItemClickListener(this);
    }

    private void createPlaceLibrary() {
        placesFromDatabase = new PlaceLibrary();
        //place = new PlaceDescription();
        try {
            PlaceDB db = new PlaceDB(this);
            SQLiteDatabase plcDB = db.openDB();
            Cursor cursor = plcDB.rawQuery("select * from places;", new String[]{});
            while(cursor.moveToNext()) {
                PlaceDescription place = new PlaceDescription();
                place.setName(cursor.getString(0));
                place.setAddressTitle(cursor.getString(1));
                place.setAddressStreet(cursor.getString(2));
                place.setElevation((int) cursor.getDouble(3));
                place.setLatitude((int) cursor.getDouble(4));
                place.setLongitude((int) cursor.getDouble(5));
                place.setDescription(cursor.getString(6));
                place.setCategory(cursor.getString(7));
                placesFromDatabase.addPlace(place.getName(), place);
                debug("PlaceDB --> checkDB","PlaceLibrary has PlaceName: "+ place.getName()+
                        "\tAddressTitle: "+place.getAddressTitle()+"\tAddressStreet: "+place.getAddressStreet()+"\tElevation: "+place.getElevation()
                        +"\tLatitude: "+place.getLatitude()+"\tLongitude: "+place.getLongitude()+"\tDescription: "+place.getDescription() +"\tCategory: "+place.getCategory());
            }
        }catch (Exception e) {
            android.util.Log.w(this.getClass().getSimpleName(), "unable to create place library");
        }
    }

    private void prepareAdapter(){

        /*
         * Prepares the Array Adapter using the JOSN file places.json which is located in the raw resouce folder
         */
//        this.placeNames = placesFromJSONfile.getNames();
//        Arrays.sort(this.placeNames);
//        al = new ArrayList<String>();
//
//        for (int i = 0; i < placeNames.length; i++) {
//            al.add(placeNames[i]);
//        }

         /*
         * Prepares the Array Adapter using a database
         */
        try {
            PlaceDB db = new PlaceDB(this);
            SQLiteDatabase plcDB = db.openDB();
            Cursor cursor = plcDB.rawQuery("select name from places;", new String[]{});
            al = new ArrayList<>();
            while(cursor.moveToNext()) {
                al.add(cursor.getString(0));
            }
        }catch(Exception e) {
            android.util.Log.w(this.getClass().getSimpleName(), "unable to prepare adapter");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        android.util.Log.d(this.getClass().getSimpleName(), "called onCreateOptionsMenu()");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
     * Implement onOptionsItemSelected(MenuItem item){} to handle clicks of buttons that are
     * in the action bar.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        android.util.Log.d(this.getClass().getSimpleName(), "called onOptionsItemSelected()");
        Intent addPlace = new Intent(this, addPlaceActivity.class);
        Intent calcGreatCircle = new Intent(this, calcGreatCircleActivity.class);
        switch (item.getItemId()) {
            case R.id.action_calcGreatCircle:
                //calcGreatCircle.putExtra("places", placesFromJSONfile);
                calcGreatCircle.putExtra("places", placesFromDatabase);
                startActivity(calcGreatCircle);
                return true;
            case R.id.action_addPlace:
                startActivity(addPlace);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        //String[] studNames = placesFromJSONfile.getNames();
        String[] studNames = placesFromDatabase.getNames();
        Arrays.sort(studNames);
        if(position >= 0 && position <= studNames.length) {
            Intent displayPlace = new Intent(this, PlaceDisplayActivity.class);
            //displayPlace.putExtra("places", placesFromJSONfile);
            displayPlace.putExtra("places", placesFromDatabase);
            displayPlace.putExtra("selected", studNames[position]);
            this.startActivityForResult(displayPlace, 1);
        }
    }

    private void debug(String hdr, String msg){
        if(debugon){
            android.util.Log.d(hdr,msg);
        }
    }

}
