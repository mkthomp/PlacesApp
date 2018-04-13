package edu.asu.bsse.mkthomp.myplacesapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
 * Purpose: an activity that allows the user to add a new place to the database.
 *
 * When a user selects a the "plus" icon from the MainActivity toolbar,
 * this activity is started.  From this screen, the user
 * can input the PlaceDescription details and click the "ADD" button
 * to add the new place to the database.
 *
 * @author Mary Insua mkthomp@asu.edu
 * @version 1.0
 */

public class addPlaceActivity extends AppCompatActivity {

    private EditText name, desc, addrTitle, addrStreet, lat, lon, elevation, cat;
    private Button addBtn;

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
            hm.put("name", name.getText().toString());
            hm.put("addressTitle", addrTitle.getText().toString());
            hm.put("addressStreet", addrStreet.getText().toString());
            hm.put("elevation", Double.parseDouble(elevation.getText().toString()));
            hm.put("latitude", Double.parseDouble(lat.getText().toString()));
            hm.put("longitude", Double.parseDouble(lon.getText().toString()));
            hm.put("description", desc.getText().toString());
            hm.put("category", cat.getText().toString());
            plcDB.insert("places",null, hm);
            plcDB.close();
            db.close();
            String addedName = name.getText().toString();

            Intent backToMain;
            backToMain = new Intent(this, MainActivity.class);
            startActivity(backToMain);

        } catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"Exception adding student information: "+
                    ex.getMessage());
        }
    }
}
