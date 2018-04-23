package edu.asu.bsse.mkthomp.myplacesapp;

/**
 * Created by insuafamily on 4/23/18.
 */

public class MethodInformation {

    public String method;
    public Object[] params;
    public JsonRPCDelegate delegate;
    public String urlString;
    public String resultAsJson;

    MethodInformation(JsonRPCDelegate parent, String urlString, String method, Object[] params){
        this.method = method;
        this.delegate = parent;
        this.urlString = urlString;
        this.params = params;
        this.resultAsJson = "{}";
    }
}
