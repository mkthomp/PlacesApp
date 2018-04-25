package edu.asu.bsse.mkthomp.myplacesapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
 * Purpose: an activity that displays a place description.
 *
 * When a user selects a place from the MainActivity, the descriptors for the
 * place are displayed in the PlaceDisplayActivity.  From this screen, the user
 * can update the PlaceDescription details or delete the place from the database
 * using the trash can icon in the toolbar.
 *
 * @author Mary Insua mkthomp@asu.edu
 * @version April 13, 2018
 */

public class PlaceDisplayActivity extends AppCompatActivity {
    private EditText description, addrTitle, addrStreet, lat, lon, elevation, category;
    private TextView name;
    private String myPlaceName;
    private PlaceLibrary places;
    private PlaceDescription myPlace;
    private Button updateBtn, mapBtn;
    private AlertDialog deleteAlert;
    private String URL = "http://10.0.2.2:8080";

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
        
        updateBtn = findViewById(R.id.button);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePlace();
            }
        });
        mapBtn = findViewById(R.id.button2);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMap();
            }
        });
    }

    private void updatePlace() {
        try{
            PlaceDB db = new PlaceDB((Context)this);
            SQLiteDatabase plcDB = db.openDB();
            ContentValues hm = new ContentValues();
            String whereClause = "name="+ "\'" + myPlaceName.toString() + "\'";
            hm.put("name", myPlaceName);
            hm.put("addressTitle", addrTitle.getText().toString());
            hm.put("addressStreet", addrStreet.getText().toString());
            hm.put("elevation", Double.parseDouble(elevation.getText().toString()));
            hm.put("latitude", Double.parseDouble(lat.getText().toString()));
            hm.put("longitude", Double.parseDouble(lon.getText().toString()));
            hm.put("description", description.getText().toString());
            hm.put("category", category.getText().toString());
            plcDB.update("places", hm, whereClause,null);
            plcDB.close();
            db.close();

            Toast.makeText(this, myPlaceName + " successfully updated!", Toast.LENGTH_SHORT).show();

            Intent backToMain;
            backToMain = new Intent(this, MainActivity.class);
            startActivity(backToMain);

        } catch (Exception ex){
            Toast.makeText(this, "Unable to update " + myPlaceName + ".", Toast.LENGTH_SHORT).show();
            android.util.Log.w(this.getClass().getSimpleName(),"Exception Updating Place: "+
                    ex.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        android.util.Log.d(this.getClass().getSimpleName(), "called onCreateOptionsMenu()");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.place_display_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
     * Implement onOptionsItemSelected(MenuItem item){} to handle clicks of buttons that are
     * in the action bar.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        android.util.Log.d(this.getClass().getSimpleName(), "called onOptionsItemSelected()");
        //Intent backToMain = new Intent(this, MainActivity.class);
        switch (item.getItemId()) {
            case R.id.delete_icon:
                setUpDeleteAlert();
                deleteAlert.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showMap() {
        Intent i = new Intent(this, MapActivity.class);
        i.putExtra("places", places);
        i.putExtra("selected", myPlaceName);
        startActivity(i);
    }

    private void deletePlace() {
        android.util.Log.d(this.getClass().getSimpleName(), "remove this Place");
        String delete = "delete from places where places.name=?;";
        try {
            PlaceDB db = new PlaceDB((Context) this);
            SQLiteDatabase plcDB = db.openDB();
            plcDB.execSQL(delete, new String[]{myPlaceName});
            plcDB.close();
            db.close();

            try{
                MethodInformation mi = new MethodInformation(this, URL,"remove",
                        new Object[]{myPlaceName});
                AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
            } catch (Exception ex){
                android.util.Log.w(this.getClass().getSimpleName(),"Exception creating adapter: "+
                        ex.getMessage());
            }

            Intent backToMain = new Intent(this, MainActivity.class);
            startActivity(backToMain);
        }catch(Exception e){
            android.util.Log.w(this.getClass().getSimpleName()," error trying to delete place");
        }
    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
        android.util.Log.d(this.getClass().getSimpleName(), "onEditorAction: keycode " +
                ((event == null) ? "null" : event.toString()) + " actionId " + actionId);
        if(actionId== EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE){
            android.util.Log.d(this.getClass().getSimpleName(),"entry is: "+v.getText().toString());
        }
        return false; // without returning false, the keyboard will not disappear or move to next field
    }

    private void setUpDeleteAlert() {
        deleteAlert = new AlertDialog.Builder(this).create();
        deleteAlert.setMessage("Delete Place from Database?");
        deleteAlert.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletePlace();
                    }
                });
        deleteAlert.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                });
    }

}
