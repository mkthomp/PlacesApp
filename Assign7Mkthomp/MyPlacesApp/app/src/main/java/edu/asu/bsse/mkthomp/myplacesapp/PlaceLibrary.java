package edu.asu.bsse.mkthomp.myplacesapp;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;

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
 * Purpose: a Hashtable that holds a collection of place names and their corresponding
 * PlaceDescription.
 *
 * Users of this class may access the names and PlaceDescriptions within the collection,
 * add new PlaceDescriptions to the collection or initialize a new collection from a
 * JSON file.
 *
 * @author Mary Insua mkthomp@asu.edu
 * @version April 13 2018
 */

public class PlaceLibrary implements Serializable{
    public Hashtable<String, PlaceDescription> placeCollection;
    private static final boolean debugOn = false;
    private int libCount = 0;

    PlaceLibrary() {
        placeCollection = new Hashtable<String, PlaceDescription>();
    }

    PlaceLibrary(Activity parent) throws JSONException {
        debug("creating a new places collection");
        placeCollection = new Hashtable<String, PlaceDescription>();
        try {
            this.resetFromJsonFile(parent);
        } catch (Exception ex) {
            android.util.Log.d(this.getClass().getSimpleName(), "error resetting from places json file" + ex.getMessage());
        }
    }


    private void debug(String message) {
        if (debugOn)
            android.util.Log.d(this.getClass().getSimpleName(), "debug: " + message);
    }

    public boolean resetFromJsonFile(Activity parent) {
        boolean ret = true;
        try {
            placeCollection.clear();
            InputStream is = parent.getApplicationContext().getResources().openRawResource(R.raw.places);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            // note that the json is in a multiple lines of input so need to read line-by-line
            StringBuffer sb = new StringBuffer();
            while (br.ready()) {
                sb.append(br.readLine());
            }
            String placesJsonStr = sb.toString();
            JSONObject placesJson = new JSONObject(new JSONTokener(placesJsonStr));
            Iterator<String> it = placesJson.keys();
            while (it.hasNext()) {
                String pName = it.next();
                JSONObject aPlace = placesJson.optJSONObject(pName);
                debug("importing place named " + pName + " json is: " + aPlace.toString());
                if (aPlace != null) {
                    PlaceDescription place = new PlaceDescription(aPlace.toString());
                    placeCollection.put(pName, place);
                }
            }
        } catch (Exception ex) {
            android.util.Log.d(this.getClass().getSimpleName(), "Exception reading json file: " + ex.getMessage());
            ret = false;
        }
        return ret;
    }

    public String[] getNames() {
        String[] ret = {};
        if (placeCollection.size() > 0) {
            ret = (String[]) (placeCollection.keySet()).toArray(new String[0]);
        }
        return ret;
    }

    public PlaceDescription get(String aName) {
        PlaceDescription ret = new PlaceDescription();
        PlaceDescription aPlace = placeCollection.get(aName);
        if (aPlace != null) {
            ret = aPlace;
        }
        return ret;
    }

    public void addPlace(String aName, PlaceDescription aPlace) {
        placeCollection.put(aName, aPlace);
    }

}
