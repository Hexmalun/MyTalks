package com.researchfip.puc.mytalks.database;

/**
 * Created by joaocastro on 06/12/17.
 */

public class PhoneData {

    private int id;
    private String imei;
    private String originNumber;
    private String originName;
    private String targetNumber;
    private String targetName;
    private Double latitude;
    private Double longitude;
    private String address;
    private String iTime;
    private String fTime;
    private int typeEvent;
    private int typeService;
    private int sharedPosition;

    public PhoneData(String imei, String originNumber,
                     String originName, String targetNumber,
                     String targetName,
                     Double latitude, Double longitude,
                     String address, String iTime,
                     String fTime, int typeEvent,
                     int typeService, int sharedPosition) {
        this.imei = imei;
        this.originNumber = originNumber;
        this.originName = originName;
        this.targetNumber = targetNumber;
        this.targetName = targetName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.iTime = iTime;
        this.fTime = fTime;
        this.typeEvent = typeEvent;
        this.typeService = typeService;
        this.sharedPosition = sharedPosition;
    }

    public PhoneData() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getOriginNumber() {
        return originNumber;
    }

    public void setOriginNumber(String originNumber) {
        this.originNumber = originNumber;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getTargetNumber() {
        return targetNumber;
    }

    public void setTargetNumber(String targetNumber) {
        this.targetNumber = targetNumber;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getiTime() {
        return iTime;
    }

    public void setiTime(String iTime) {
        this.iTime = iTime;
    }

    public String getfTime() {
        return fTime;
    }

    public void setfTime(String fTime) {
        this.fTime = fTime;
    }

    public int getTypeEvent() {
        return typeEvent;
    }

    public void setTypeEvent(int typeEvent) {
        this.typeEvent = typeEvent;
    }

    public int getTypeService() {
        return typeService;
    }

    public void setTypeService(int typeService) {
        this.typeService = typeService;
    }

    public int getSharedPosition() {
        return sharedPosition;
    }

    public void setSharedPosition(int sharedPosition) {
        this.sharedPosition = sharedPosition;
    }

    @Override
    public String toString() {
        return "PhoneData [" +
                "id=" + id +
                ", imei=" + imei +
                ", originNumber=" + originNumber +
                ", originName=" + originName  +
                ", targetNumber=" + targetNumber  +
                ", targetName=" + targetName +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", address=" + address +
                ", iTime=" + iTime +
                ", fTime=" + fTime +
                ", typeEvent=" + typeEvent +
                ", typeService=" + typeService +
                ", sharedPosition=" + sharedPosition +
                "]";
    }
}
