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

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener {

    private ListView placesList;
    private PlaceDescription place;
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
        place = new PlaceDescription();
        try {
            PlaceDB db = new PlaceDB(this);
            SQLiteDatabase plcDB = db.openDB();
            Cursor cursor = plcDB.rawQuery("select * from places;", new String[]{});
            while(cursor.moveToNext()) {
                place.setName(cursor.getString(0));
                place.setAddressTitle(cursor.getString(1));
                place.setAddressStreet(cursor.getString(2));
                place.setElevation((int) cursor.getDouble(3));
                place.setLatitude((int) cursor.getDouble(4));
                place.setLongitude((int) cursor.getDouble(5));
                place.setDescription(cursor.getString(6));
                place.setCategory(cursor.getString(7));
                placesFromDatabase.addPlace(place.getName(), place);
            }
        }catch (Exception e) {
            android.util.Log.w(this.getClass().getSimpleName(), "unable to create place library");
        }
    }

    private void prepareAdapter(){
//        this.placeNames = placesFromJSONfile.getNames();
//        Arrays.sort(this.placeNames);
//        al = new ArrayList<String>();
//
//        for (int i = 0; i < placeNames.length; i++) {
//            al.add(placeNames[i]);
//        }
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
                calcGreatCircle.putExtra("places", placesFromJSONfile);
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
        String[] studNames = placesFromJSONfile.getNames();
        Arrays.sort(studNames);
        if(position >= 0 && position <= studNames.length) {
            Intent displayPlace = new Intent(this, PlaceDisplayActivity.class);
            displayPlace.putExtra("places", placesFromJSONfile);
            displayPlace.putExtra("selected", studNames[position]);
            this.startActivityForResult(displayPlace, 1);
        }
    }


}
