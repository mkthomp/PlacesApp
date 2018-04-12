package edu.asu.bsse.mkthomp.myplacesapp;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PlaceDisplayActivity extends AppCompatActivity {

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
            //crsDB.execSQL(delete);
            plcDB.execSQL(delete, new String[]{myPlaceName});
            plcDB.close();
            db.close();
        }catch(Exception e){
            android.util.Log.w(this.getClass().getSimpleName()," error trying to delete student");
        }
    }

}
