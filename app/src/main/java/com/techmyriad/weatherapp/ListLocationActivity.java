package com.techmyriad.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.techmyriad.weatherapp.adapter.LocationAdapter;
import com.techmyriad.weatherapp.database.DatabaseQuery;
import com.techmyriad.weatherapp.entity.DatabaseLocationObject;
import com.techmyriad.weatherapp.entity.LocationObject;
import com.techmyriad.weatherapp.helper.Helper;
import com.techmyriad.weatherapp.json.LocationMapObject;

import java.util.ArrayList;
import java.util.List;

public class ListLocationActivity extends AppCompatActivity {
    private static final String TAG = ListLocationActivity.class.getSimpleName();
    private DatabaseQuery query;
    private List<DatabaseLocationObject> allLocations;
    private LocationObject locationObject;
    private LocationMapObject locationMapObject;
    private RequestQueue queue;
    private List<LocationObject> allData;
    private LocationAdapter locationAdapter;
    private RecyclerView locationRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_location);
        setTitle(Helper.LOCATION_LIST);
        queue = Volley.newRequestQueue(ListLocationActivity.this);
        allData = new ArrayList<LocationObject>();
        query = new DatabaseQuery(ListLocationActivity.this);
        allLocations = query.getStoredDataLocations();
        if(null != allLocations){
            for(int i = 0; i < allLocations.size(); i++){
                // make volley network call here
                System.out.println("Response printing " + allLocations.get(i).getLocation());
                requestJsonObject(allLocations.get(i));
            }
        }
        Toast.makeText(ListLocationActivity.this, "Count number of locations " + allLocations.size(), Toast.LENGTH_LONG).show();
        ImageButton addLocation = (ImageButton) findViewById(R.id.add_location);
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addLocationIntent = new Intent(ListLocationActivity.this, AddLocationActivity.class);
                startActivity(addLocationIntent);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ListLocationActivity.this);
        locationRecyclerView = (RecyclerView) findViewById(R.id.location_list);
        locationRecyclerView.setLayoutManager(linearLayoutManager);
    }
    private void requestJsonObject(final DatabaseLocationObject paramValue){
        String url ="http://api.openweathermap.org/data/2.5/weather?q="+paramValue.getLocation()+"&APPID=55e1ef2356c6e8084635491b939c5ec8&units=metric";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response " + response);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                locationMapObject = gson.fromJson(response, LocationMapObject.class);
                if (null == locationMapObject) {
                    Toast.makeText(getApplicationContext(), "Nothing was returned", Toast.LENGTH_LONG).show();
                } else {
                    int rowId = paramValue.getId();
                    Long tempVal = Math.round(Math.floor(Double.parseDouble(locationMapObject.getMain().getTemp())));
                    String city = locationMapObject.getName() + ", " + locationMapObject.getSys().getCountry();
                    String weatherInfo = String.valueOf(tempVal) + "<sup>o</sup>, " + Helper.capitalizeFirstLetter(locationMapObject.getWeather().get(0).getDescription());
                    allData.add(new LocationObject(rowId, city, weatherInfo));
                    locationAdapter = new LocationAdapter(ListLocationActivity.this, allData);
                    locationRecyclerView.setAdapter(locationAdapter);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error " + error.getMessage());
            }
        });
        queue.add(stringRequest);
    }

}