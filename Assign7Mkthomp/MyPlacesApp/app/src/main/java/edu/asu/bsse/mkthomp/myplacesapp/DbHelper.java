package edu.asu.bsse.mkthomp.myplacesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by insuafamily on 4/24/18.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = DbHelper.class.getSimpleName();
    private String URL = "http://10.0.2.2:8080";
    private String dbPath;

    private JSONObject res;
    private static final String DB_NAME = "placesdb.db";
    private static final int DB_VERSION = 1;
    Context context;
    SQLiteDatabase db;
    public JSONObject plcObj;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        dbPath = context.getFilesDir().getPath()+"/";
        //res = resource;
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_PLACES_TABLE = "CREATE TABLE " + DbContract.MenuEntry.TABLE_NAME + "(" +
                DbContract.MenuEntry.COLUMN_NAME + " TEXT UNIQUE NOT NULL, " +
                DbContract.MenuEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                DbContract.MenuEntry.COLUMN_ADDRESSTITLE + " TEXT NUT NULL, " +
                DbContract.MenuEntry.COLUMN_ADDRESSSTREET + " TEXT NOT NULL, " +
                DbContract.MenuEntry.COLUMN_LATITUDE + " DOUBLE NOT NULL, " +
                DbContract.MenuEntry.COLUMN_LONGITUDE + " DOUBLE NOT NULL, " +
                DbContract.MenuEntry.COLUMN_ELEVATION + " DOUBLE NOT NULL, " +
                DbContract.MenuEntry.COLUMN_CATEGORY + " TEXT NOT NULL " + ");";

        db.execSQL(SQL_CREATE_PLACES_TABLE);
        Log.d(TAG, "Database Created Successfully");

        try {
            readDataFromServer();
            readDataToDb(db);
        }catch (IOException ex) {
            ex.printStackTrace();
        }catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void readDataFromServer() {
        try{
            MethodInformation mi = new MethodInformation(this, URL,"getNames",
                    new Object[]{});
            AsyncCollectionConnect ac = (AsyncCollectionConnect) new AsyncCollectionConnect().execute(mi);
        } catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"Exception creating adapter: "+
                    ex.getMessage());
        }
    }

    private void readDataToDb(SQLiteDatabase db) throws IOException, JSONException {
        final String NAME = "name";
        final String ADDRESSTITLE = "addressTitle";
        final String ADDRESSSTREET = "addressStreet";
        final String ELEVATION = "elevation";
        final String LATITUDE = "latitude";
        final String LONGITUDE = "longitude";
        final String DESCRIPTION = "description";
        final String CATEGORY = "category";

        if (plcObj != null) {
            try {
                String jsonDataString = plcObj.toString();
                JSONArray jsonArray = new JSONArray(jsonDataString);

                for (int i = 0; i < jsonArray.length(); ++i) {
                    String name;
                    String addressTitle;
                    String addressStreet;
                    String elevation;
                    String latitude;
                    String longitude;
                    String description;
                    String category;

                    JSONObject place = jsonArray.getJSONObject(i);

                    name = place.getString(NAME);
                    addressTitle = place.getString(ADDRESSTITLE);
                    addressStreet = place.getString(ADDRESSSTREET);
                    elevation = place.getString(ELEVATION);
                    latitude = place.getString(LATITUDE);
                    longitude = place.getString(LONGITUDE);
                    description = place.getString(DESCRIPTION);
                    category = place.getString(CATEGORY);

                    ContentValues placeValues = new ContentValues();

                    placeValues.put(DbContract.MenuEntry.COLUMN_NAME, name);
                    placeValues.put(DbContract.MenuEntry.COLUMN_ADDRESSSTREET, addressStreet);
                    placeValues.put(DbContract.MenuEntry.COLUMN_ADDRESSTITLE, addressTitle);
                    placeValues.put(DbContract.MenuEntry.COLUMN_LATITUDE, latitude);
                    placeValues.put(DbContract.MenuEntry.COLUMN_LONGITUDE, longitude);
                    placeValues.put(DbContract.MenuEntry.COLUMN_ELEVATION, elevation);
                    placeValues.put(DbContract.MenuEntry.COLUMN_DESCRIPTION, description);
                    placeValues.put(DbContract.MenuEntry.COLUMN_CATEGORY, category);

                    db.insert(DbContract.MenuEntry.TABLE_NAME, null, placeValues);
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }

    public SQLiteDatabase openDB() throws SQLException {
        String myPath = dbPath + DB_NAME;
        db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

        return db;
    }

    @Override
    public synchronized void close() {
        if(db != null)
            db.close();
        super.close();
    }
}
