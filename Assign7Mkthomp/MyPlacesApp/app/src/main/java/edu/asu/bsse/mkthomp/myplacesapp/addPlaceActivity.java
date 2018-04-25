package edu.asu.bsse.mkthomp.myplacesapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
 * Purpose: an activity that allows the user to add a new place to the database.
 *
 * When a user selects a the "plus" icon from the MainActivity toolbar,
 * this activity is started.  From this screen, the user
 * can input the PlaceDescription details and click the "ADD" button
 * to add the new place to the database.
 *
 * @author Mary Insua mkthomp@asu.edu
 * @version April 13, 2018
 */

public class addPlaceActivity extends AppCompatActivity {

    private EditText name, desc, addrTitle, addrStreet, lat, lon, elevation, cat;
    private String nameToAdd, descToAdd, titleToAdd, addrToAdd, catToAdd;
    private Double latToAdd, lonToAdd, elevationToAdd;
    private Button addBtn;
    private String URL = "http://10.0.2.2:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);

        addBtn = findViewById(R.id.button);
        name = findViewById(R.id.addName);
        desc = findViewById(R.id.addDescription);
        addrTitle = findViewById(R.id.addAddrTitle);
        addrStreet = findViewById(R.id.addAddrStreet);
        lat = findViewById(R.id.addLatitude);
        lon = findViewById(R.id.addLongitude);
        elevation = findViewById(R.id.addElevation);
        cat = findViewById(R.id.addCategory);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlace();
            }
        });
    }

    private void addPlace() {
        try{
            PlaceDB db = new PlaceDB((Context)this);
            SQLiteDatabase plcDB = db.openDB();
            ContentValues hm = new ContentValues();
            //JSONObject jo = new JSONObject();
            PlaceDescription place = new PlaceDescription();

            if (name.getText() != null) {
                nameToAdd = name.getText().toString();
            }else {nameToAdd = "";}
            hm.put("name", nameToAdd);
            //jo.put("name", nameToAdd);
            place.setName(nameToAdd);

            if (addrTitle.getText() != null) {
                titleToAdd = addrTitle.getText().toString();
            }else {titleToAdd = "";}
            hm.put("addressTitle", titleToAdd);
            //jo.put("addressTitle", titleToAdd);
            place.setAddressTitle(titleToAdd);

            if (addrStreet.getText() != null) {
                addrToAdd = addrStreet.getText().toString();
            }else {addrToAdd = "";}
            hm.put("addressStreet", addrToAdd);
            //jo.put("addressStreet", addrToAdd);
            place.setAddressStreet(addrToAdd);

            if (elevation.getText() != null && elevation.getText().toString() != "") {
                elevationToAdd = Double.parseDouble(elevation.getText().toString());
            }else {elevationToAdd = 0.0;}
            hm.put("elevation", elevationToAdd);
            //jo.put("elevation", elevationToAdd);
            place.setElevation(elevationToAdd);

            if (lat.getText() != null && lat.getText().toString() != "") {
                latToAdd = Double.parseDouble(lat.getText().toString());
            }else {latToAdd = 0.0;}
            hm.put("latitude", latToAdd);
            //jo.put("latitude", latToAdd);
            place.setLatitude(latToAdd);

            if (lon.getText() != null && lon.getText().toString() != "") {
                lonToAdd = Double.parseDouble(lon.getText().toString());
            }else {lonToAdd = 0.0;}
            hm.put("longitude", lonToAdd);
            //jo.put("longitude", lonToAdd);
            place.setLongitude(lonToAdd);

            if (desc.getText() != null) {
                descToAdd = desc.getText().toString();
            }else {descToAdd = "";}
            hm.put("description", descToAdd);
            //jo.put("description", descToAdd);
            place.setDescription(descToAdd);

            if (cat.getText() != null) {
                catToAdd = cat.getText().toString();
            }else {catToAdd = "";}
            hm.put("category", catToAdd);
            //jo.put("category", catToAdd);
            place.setCategory(catToAdd);

            //Object[] obj = new Object[]{};
            //obj[0] = jo;

            plcDB.insert("places",null, hm);
            plcDB.close();
            db.close();
            String addedName = name.getText().toString();

            try{
                MethodInformation mi = new MethodInformation(this, URL,"add",
                        new Object[]{place.toJSON()});
                AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
            } catch (Exception ex){
                android.util.Log.w(this.getClass().getSimpleName(),"Exception creating adapter: "+
                        ex.getMessage());
            }

            Intent backToMain;
            backToMain = new Intent(this, MainActivity.class);
            startActivity(backToMain);

        } catch (Exception ex){
            Toast.makeText(this, "Please fill in all fields to add a new place.", Toast.LENGTH_LONG).show();
            android.util.Log.w(this.getClass().getSimpleName(),"Exception adding Place Details: "+
                    ex.getMessage());
        }
    }
}
