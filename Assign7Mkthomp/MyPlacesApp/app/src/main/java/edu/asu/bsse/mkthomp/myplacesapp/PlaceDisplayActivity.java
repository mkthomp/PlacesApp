package edu.asu.bsse.mkthomp.myplacesapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
 * @version 1.0
 */

public class PlaceDisplayActivity extends AppCompatActivity {
    private static final boolean debugon = true;
    private EditText description, addrTitle, addrStreet, lat, lon, elevation, category;
    private TextView name;
    private String myPlaceName;
    private PlaceLibrary places;
    private PlaceDescription myPlace;
    private Button updateBtn;


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

            debug("TESTING --> UPDATE DB", "addrTitle= \t" + hm.get("addressTitle") + "addrStreet= \t" + hm.get("addressStreet")
                                                + "category= \t" + hm.get("category"));

            Intent backToMain;
            backToMain = new Intent(this, MainActivity.class);
            startActivity(backToMain);

        } catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"Exception adding student information: "+
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
        Intent backToMain = new Intent(this, MainActivity.class);
        switch (item.getItemId()) {
            case R.id.delete_icon:
                deletePlace();
                startActivity(backToMain);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        }catch(Exception e){
            android.util.Log.w(this.getClass().getSimpleName()," error trying to delete student");
        }
    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
        // note that inputType and keyboard actions imeOptions must be defined to manage the keyboard
        // these can be defined in the xml as an attribute of the EditText.
        // returning false from this method
        android.util.Log.d(this.getClass().getSimpleName(), "onEditorAction: keycode " +
                ((event == null) ? "null" : event.toString()) + " actionId " + actionId);
        if(actionId== EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE){
            android.util.Log.d(this.getClass().getSimpleName(),"entry is: "+v.getText().toString());
        }
        return false; // without returning false, the keyboard will not disappear or move to next field
    }

    private void debug(String hdr, String msg){
        if(debugon){
            android.util.Log.d(hdr,msg);
        }
    }

}
