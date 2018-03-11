package com.researchfip.puc.mytalks.UI.adapters.objects;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Castro on 31/10/2017.
 */

public class Calls {
    private int from;
    private int to;
    private LatLng location;
    private String time;

    public Calls(int from, int to, LatLng location, String time) {
        this.from = from;
        this.to = to;
        this.location = location;
        this.time = time;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
