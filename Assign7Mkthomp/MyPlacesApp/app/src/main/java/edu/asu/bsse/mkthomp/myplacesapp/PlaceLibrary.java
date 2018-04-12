package edu.asu.bsse.mkthomp.myplacesapp;

/**
 * Created by insuafamily on 4/11/18.
 */

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
 * Created by insuafamily on 4/11/18.
 */

public class PlaceLibrary implements Serializable{
    public Hashtable<String, PlaceDescription> placeCollection;
    private static final boolean debugOn = false;
    private int libCount = 0;

    PlaceLibrary(Activity parent) throws JSONException {
        debug("creating a new student collection");
        placeCollection = new Hashtable<String, PlaceDescription>();
        try {
            this.resetFromJsonFile(parent);
        } catch (Exception ex) {
            android.util.Log.d(this.getClass().getSimpleName(), "error resetting from students json file" + ex.getMessage());
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
                debug("importing student named " + pName + " json is: " + aPlace.toString());
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

}
