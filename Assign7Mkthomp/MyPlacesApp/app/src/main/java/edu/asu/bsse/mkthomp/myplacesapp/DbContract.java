package edu.asu.bsse.mkthomp.myplacesapp;

import android.provider.BaseColumns;

/**
 * Created by insuafamily on 4/24/18.
 */

public class DbContract {

    public static final class MenuEntry implements BaseColumns {
        public static final String TABLE_NAME = "places";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_ADDRESSTITLE = "addressTitle";
        public static final String COLUMN_ADDRESSSTREET = "addressStreet";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_ELEVATION = "elevation";
        public static final String COLUMN_CATEGORY = "category";
    }
}
