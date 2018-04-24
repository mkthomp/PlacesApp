package edu.asu.bsse.mkthomp.myplacesapp;

import android.os.AsyncTask;
import android.os.Looper;

import org.json.JSONArray;

import java.net.URL;

/**
 * Created by insuafamily on 4/23/18.
 */

public class JsonRPCAsyncTask extends AsyncTask<MethodInformation, Integer, MethodInformation> {
    @Override
    protected void onPreExecute() {
        android.util.Log.d(this.getClass().getSimpleName(), "in onPreExecute on " +
                (Looper.myLooper() == Looper.getMainLooper() ? "Main thread" : "Async Thread"));
    }

    @Override
    protected MethodInformation doInBackground(MethodInformation... aRequest) {
        // array of methods to be called. Assume exactly one input, a single MethodInformation object
        android.util.Log.d(this.getClass().getSimpleName(), "in doInBackground on " +
                (Looper.myLooper() == Looper.getMainLooper() ? "Main thread" : "Async Thread"));
        try {
            JSONArray ja = new JSONArray(aRequest[0].params);
            android.util.Log.d(this.getClass().getSimpleName(), "params: " + ja.toString());
            String requestData = "{ \"jsonrpc\":\"2.0\", \"method\":\"" + aRequest[0].method + "\", \"params\":" + ja.toString() +
                    ",\"id\":3}";
            android.util.Log.d(this.getClass().getSimpleName(), "requestData: " + requestData + " url: " + aRequest[0].urlString);
            JsonRPCRequestViaHttp conn = new JsonRPCRequestViaHttp((new URL(aRequest[0].urlString)), null);
            String resultStr = conn.call(requestData);
            aRequest[0].resultAsJson = resultStr;
        } catch (Exception ex) {
            android.util.Log.d(this.getClass().getSimpleName(), "exception in remote call " +
                    ex.getMessage());
        }
        return aRequest[0];
    }

    @Override
    protected void onPostExecute(MethodInformation result) {
        android.util.Log.d(this.getClass().getSimpleName(), "in onPostExecute on " +
                (Looper.myLooper() == Looper.getMainLooper() ? "Main thread" : "Async Thread"));
        android.util.Log.d(this.getClass().getSimpleName(), " result is: " + result.resultAsJson);
        try {
            result.delegate.jsonRPCCallback(result);
        } catch (Exception ex) {
            android.util.Log.d(this.getClass().getSimpleName(), "Exception: " + ex.getMessage());
        }
    }
}
