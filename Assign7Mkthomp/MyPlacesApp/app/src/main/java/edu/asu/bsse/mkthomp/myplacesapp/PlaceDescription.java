package edu.asu.bsse.mkthomp.myplacesapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

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
 * Purpose: a container for Place attributes
 *
 * Users of this class may access and set values of each attribute.
 * A PlaceDescription object can serialze to and from a Json representation.
 *
 * @author Mary Insua mkthomp@asu.edu
 * @version April 13, 2018
 */

public class PlaceDescription implements Serializable{
    public String name, description, category, addressTitle, addressStreet;
    public double elevation, latitude, longitude;

    public PlaceDescription() {
        name = "";
        description = "";
        category = "";
        addressTitle = "";
        addressStreet = "";
        elevation = 0.0;
        latitude = 0.0;
        longitude = 0.0;
    }

    public PlaceDescription(String jsonStr){
        try{
            JSONObject jObj = new JSONObject(jsonStr);
            name = jObj.getString("name");
            description = jObj.getString("description");
            category = jObj.getString("category");
            addressTitle = jObj.getString("address-title");
            addressStreet = jObj.getString("address-street");
            elevation = jObj.getDouble("elevation");
            latitude = jObj.getDouble("latitude");
            longitude = jObj.getDouble("longitude");

        }catch (Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(), "error getting Student from json string");
        }
    }

    PlaceDescription(JSONObject placeJson) throws JSONException {
        JSONObject placeObj = new JSONObject();
        placeObj = placeJson;

        this.name = placeObj.getString("name");
        this.description = placeObj.getString("description");
        this.category = placeObj.getString("category");
        this.addressTitle = placeObj.getString("address-title");
        this.addressStreet = placeObj.getString("address-street");
        this.elevation = placeObj.getDouble("elevation");
        this.latitude = placeObj.getDouble("latitude");
        this.longitude = placeObj.getDouble("longitude");
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getAddressTitle() {
        return addressTitle;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public Double getElevation() {
        return elevation;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAddressTitle(String addressTitle) {
        this.addressTitle = addressTitle;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public void setElevation(Double elevation) {this.elevation = elevation;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject temp = new JSONObject();

        temp.put("name", this.name);
        temp.put("description", this.description);
        temp.put("category", this.category);
        temp.put("address-title", this.addressTitle);
        temp.put("address-street", this.addressStreet);
        temp.put("elevation", this.elevation);
        temp.put("longitude", this.longitude);
        temp.put("latitude", this.latitude);

        return temp;
    }
}


