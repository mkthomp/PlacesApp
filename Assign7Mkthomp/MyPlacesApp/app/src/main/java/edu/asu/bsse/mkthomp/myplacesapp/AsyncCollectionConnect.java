package edu.asu.bsse.mkthomp.myplacesapp;

import android.os.AsyncTask;
import android.os.Looper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by insuafamily on 4/23/18.
 */

public class AsyncCollectionConnect extends AsyncTask<MethodInformation, Integer, MethodInformation>{
    @Override
    protected void onPreExecute(){
        android.util.Log.d(this.getClass().getSimpleName(),"in onPreExecute on "+
                (Looper.myLooper() == Looper.getMainLooper()?"Main thread":"Async Thread"));
    }

    @Override
    protected MethodInformation doInBackground(MethodInformation... aRequest){
        // array of methods to be called. Assume exactly one input, a single MethodInformation object
        android.util.Log.d(this.getClass().getSimpleName(),"in doInBackground on "+
                (Looper.myLooper() == Looper.getMainLooper()?"Main thread":"Async Thread"));
        try {
            JSONArray ja = new JSONArray(aRequest[0].params);
            android.util.Log.d(this.getClass().getSimpleName(),"params: "+ja.toString());
            String requestData = "{ \"jsonrpc\":\"2.0\", \"method\":\""+aRequest[0].method+"\", \"params\":"+ja.toString()+
                    ",\"id\":3}";
            android.util.Log.d(this.getClass().getSimpleName(),"requestData: "+requestData+" url: "+aRequest[0].urlString);
            JsonRPCRequestViaHttp conn = new JsonRPCRequestViaHttp((new URL(aRequest[0].urlString)), aRequest[0].parent);
            String resultStr = conn.call(requestData);
            aRequest[0].resultAsJson = resultStr;
        }catch (Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(),"exception in remote call "+
                    ex.getMessage());
        }
        return aRequest[0];
    }

    @Override
    protected void onPostExecute(MethodInformation res){
        android.util.Log.d(this.getClass().getSimpleName(), "in onPostExecute on " +
                (Looper.myLooper() == Looper.getMainLooper() ? "Main thread" : "Async Thread"));
        android.util.Log.d(this.getClass().getSimpleName(), " resulting is: " + res.resultAsJson);
        try {
            if (res.method.equals("getNames")) {
                JSONObject jo = new JSONObject(res.resultAsJson);
                JSONObject placesObj = new JSONObject();
                JSONArray ja = jo.getJSONArray("result");
                ArrayList<String> al = new ArrayList<String>();
                for (int i = 0; i < ja.length(); i++) {
                    al.add(ja.getString(i));
                }
                String[] names = al.toArray(new String[0]);
                Arrays.sort(names);
//                res.parent.aa.clear();
//                for (int i = 0; i < names.length; i++) {
//                    res.parent.aa.add(names[i]);
//                }
//                res.parent.aa.notifyDataSetChanged();
                if (names.length > 0){
                    try{
                        // got the list of place names from the server, so now create a new async task
                        // to get the student information about the first student and populate the UI with
                        // that student's information.
                        for (int i = 0; i < names.length; i++) {
                            MethodInformation mi = new MethodInformation(null, res.parent, res.urlString, "get",
                                    new String[]{names[i]});
                            AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
                        }
                    } catch (Exception ex){
                        android.util.Log.w(this.getClass().getSimpleName(),"Exception processing spinner selection: "+
                                ex.getMessage());
                    }
                }
            } else if (res.method.equals("get")) {
                JSONObject jo = new JSONObject(res.resultAsJson);
                PlaceDescription aPlace = new PlaceDescription(jo.getJSONObject("result"));
                res.parent.placesFromServer.addPlace(aPlace.getName() , aPlace);
            } else if (res.method.equals("add")){
                try{
                    // finished adding a place. refresh the list of places by going back to the server for names
                    MethodInformation mi = new MethodInformation(null, res.parent, res.urlString, "getNames", new Object[]{ });
                    AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
                } catch (Exception ex){
                    android.util.Log.w(this.getClass().getSimpleName(),"Exception processing getNames: "+
                            ex.getMessage());
                }
            } else if (res.method.equals("remove")) {
                try {
                    // finished removing a place. refresh the list of places by going back to the server for names
                    MethodInformation mi = new MethodInformation(null, res.parent, res.urlString, "getNames", new Object[]{});
                    AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
                } catch (Exception ex) {
                    android.util.Log.w(this.getClass().getSimpleName(), "Exception processing getNames: " +
                            ex.getMessage());
                }
            }
        }catch (Exception ex){
            android.util.Log.d(this.getClass().getSimpleName(),"Exception: "+ex.getMessage());
        }
    }
}
