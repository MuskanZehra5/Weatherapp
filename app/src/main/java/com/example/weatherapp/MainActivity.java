package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.Manifest;

public class MainActivity extends AppCompatActivity {

    RelativeLayout homerl;
    ProgressBar progressbar;
    TextView cityname,weathercond, temperature;
    ImageView back, searchicon,weatherimg;
    TextInputEditText citytext;
    RecyclerView weather_recyclerview;
    ArrayList<Weathermodel> weathermodelArrayList;
    WeatherAdapter weatheradapter;
    LocationManager locationManager;
    int Permission_code = 1;
    String cityName ="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        full screen kr dy ga app ko it means k no status bar wagera will be displayed
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);
        homerl = findViewById(R.id.homerel);
        progressbar = findViewById(R.id.loading);
        cityname = findViewById(R.id.cityname);
        back = findViewById(R.id.backgroundimg);
        citytext = findViewById(R.id.edittext);
        searchicon = findViewById(R.id.search);
        weathercond = findViewById(R.id.weathercond);
        temperature = findViewById(R.id.temperature);
        weather_recyclerview = findViewById(R.id.recyclerview);
        weatherimg = findViewById(R.id.weatherimg);
        weatheradapter = new WeatherAdapter(this,weathermodelArrayList);
        weather_recyclerview.setAdapter(weatheradapter);

//        YE KRNY K BAAD U HAVE TO ADD PERMISSION IN THE MANIFEST FILE
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED ) && ( ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED  )){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},Permission_code);
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        cityName = getcityname(location.getLongitude(), location.getLatitude());


        getWeatherinfo(cityName);

        searchicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ci = citytext.getText().toString();
                if(ci.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter city name", Toast.LENGTH_SHORT).show();
                }
                else{
                    cityname.setText(cityName);
                    getWeatherinfo(ci);
                }

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == Permission_code){

            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permissions Granted!", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Please provide Permissions...", Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }

    private String getcityname(double longitude, double latitude){
        String city_name = "not found";
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());

        try{
            List<Address> addresses = gcd.getFromLocation(latitude,longitude,10);

            for(Address adr: addresses){
                if(adr!=null){
                    String city = adr.getLocality();
                    if(city!=null && !city.equals("")){
                        city_name = city;
                    }
                    else{
                        Log.d("TAG", "CITY NOT FOUND ");
                        Toast.makeText(this,"User city not found",Toast.LENGTH_SHORT).show();
                    }

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return city_name;
    }

    private void getWeatherinfo(String city_name){
        String url = "http://api.weatherapi.com/v1/current.json?key=9decea3f9792473dabb80955231303&q="+cityname+"&aqi=no";
        cityname.setText(city_name);
        RequestQueue rq = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressbar.setVisibility(View.GONE);
                homerl.setVisibility(View.VISIBLE);
                weathermodelArrayList.clear();

                try {
                    String temp= response.getJSONObject("current").getString("temp_c");
                    temperature.setText(temp +"°C");
                    int isday = response.getJSONObject("current").getInt("isDay");
                    String condition = response.getJSONObject("current").getString("text");
                    String conditionicon = response.getJSONObject("current").getString("icon");
                    Picasso.get().load("http:".concat(conditionicon)).into(weatherimg);

                    weathercond.setText(condition);
                    if(isday == 1){
                        Picasso.get().load(R.drawable.morning).into(back);
                    }else{

                        Picasso.get().load(R.drawable.night).into(back);
                    }

                    JSONObject forecastobj = response.getJSONObject("forecast");
                    JSONObject forecast1 = forecastobj.getJSONArray("forecastday").getJSONObject(0);

                    JSONArray hourarray = forecast1.getJSONArray("hour");

                    for(int i =0; i<hourarray.length();i++){
                        JSONObject hourobj = hourarray.getJSONObject(i);
                        String time = hourobj.getString("time");
                        String temper = hourobj.getString("temp_c");
                        String img = hourobj.getJSONObject("condition").getString("icon");
                        String wind = hourobj.getString("wind_kph");
                        weathermodelArrayList.add(new Weathermodel(time,temper,img,wind));
                    }

                    weatheradapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Please enter valid city name!", Toast.LENGTH_SHORT).show();

            }
        });

        rq.add(jsonObjectRequest);

    }
}