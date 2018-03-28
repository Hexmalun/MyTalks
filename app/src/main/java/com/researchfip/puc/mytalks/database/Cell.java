package com.researchfip.puc.mytalks.database;

/**
 * Created by Mateus on 15/10/2015.
 */
public class Cell {

    private int cell;
    private int averageSignal;
    private int samples;



    private int lac;
    private double lon, lat;
    private String type;
    private String day;
    private String data;
    private String mcc;
    private String mnc;



    public Cell() {
        this.cell = 0;
        this.lon = 0;
        this.lat = 0;
        this.lac = 0;
        this.samples = 0;
        this.averageSignal = 0;
    }

    public Cell(int cell, float lat, float lon, int samples, int averageSignal, String type, int lac) {
        this.cell = cell;
        this.lon = lon;
        this.lat = lat;
        this.lac = lac;
        this.samples = samples;
        this.averageSignal = averageSignal;
        this.type = type;
    }


    public int getCell() {
        return cell;
    }

    public void setCell(int in) {
        this.cell = in;
    }

    public int getSamples() {
        return samples;
    }

    public void setSamples(int in) {
        this.samples = in;
    }

    public int getAverageSignal() {
        return averageSignal;
    }

    public void setAverageSignal(int in) {
        this.averageSignal = in;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double in) {
        this.lon = in;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double in) {
        this.lat = in;
    }

    public String getType() {
        return type;
    }

    public void setType(String in) {
        this.type = in;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String in) {
        this.day = in;
    }

    public String getData() {
        return data;
    }

    public void setData(String in) {
        this.data = in;
    }

    public int getLac() { return lac; }

    public void setLac(int lac) { this.lac = lac; }

    public String getMnc() {
        return mnc;
    }

    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

}
