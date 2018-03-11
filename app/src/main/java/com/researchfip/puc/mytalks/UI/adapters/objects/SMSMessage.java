package com.researchfip.puc.mytalks.UI.adapters.objects;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by castro on 26/10/2017.
 */

public class SMSMessage {

    private int from;
    private int to;
    private LatLng location;
    private String text;
    private String hour;
    private String date;

    public SMSMessage(int from, int to, LatLng location, String text, String hour, String date) {
        this.from = from;
        this.to = to;
        this.location = location;
        this.text = text;
        this.hour = hour;
        this.date = date;
    }

    @Override
    public String toString() {
        return " From: " + this.from
                + " To: " + this.to +
                " Location: " + this.location;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
