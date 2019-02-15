package com.techmyriad.weatherapp.helper;

public class Helper {
    public static final String MANAGER_LOCATION = "Manager Location";
    public static final String LOCATION_LIST = "Location List";
    public static final String LOCATION_ERROR_MESSAGE = "Input field must be filled";
    public static final String PREFS_TAG = "prefs";
    public static final String STORED_DATA_FIRST = "data_first";
    public static final String STORED_DATA_SECOND = "data_second";
    public static final String IS_DATA_PRESENT = "isData";
    public static final String SERVER_PATH = "api.openweathermap.org/data/2.5/weather?";
    public static final String SERVER_PARAM_KEY = "q";
    public static final String LOCATION_PREFS = "location_prefs";
    public static String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }
}