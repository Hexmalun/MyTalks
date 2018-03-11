package com.researchfip.puc.mytalks.general;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by joaocastro on 06/12/17.
 */

public class Geo implements LocationListener {

    private LocationManager locationManager;
    private String provider;
    private Context context;

    public Geo(Context context) {
        this.context = context;
    }

    @SuppressLint("MissingPermission")
    public double[] getGeoCoordinates() {
        double[] coordinates = new double[2];

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            provider = locationManager.GPS_PROVIDER;
        } else {
            provider = LocationManager.NETWORK_PROVIDER;
        }

        //TODO PERMISSION CHECK
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
//            if(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(provider, 0, 0, this);
//            }else{
//        }

        @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(provider);
        if(location != null){
            coordinates[0] = location.getLatitude();
            coordinates[1] = location.getLongitude();
        }

        stopUpdates();
        return coordinates;
    }

    public String getAddress(double latitude, double longitude){
        String url = "http://maps.google.com/maps/api/geocode/json?latlng="
                + latitude + "," + longitude + "&sensor=true";
        String locationString = "";
        String result = null;

        HTTPHandler httpHandler = new HTTPHandler();
        try {
             result = httpHandler.execute(url).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = null;
        if (result != null && result.length() != 0){
            try {
                jsonObject = new JSONObject(result);
                if(jsonObject.getString("status").equals("OK")){
                    locationString = jsonObject.getJSONArray("results").getJSONObject(0).getString("formatted_address");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return locationString;
    }

    public void stopUpdates (){
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
