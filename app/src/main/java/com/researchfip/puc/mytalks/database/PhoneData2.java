package com.researchfip.puc.mytalks.database;

/**
 * Created by joaocastro on 06/12/17.
 */

public class PhoneData2 {

    private int id;
    private String imei;
    private String originNumber;
    private String originName;
    private String targetNumber;
    private String targetName;
    private Double latitudeS;
    private Double longitudeS;
    private Double latitudeE;
    private Double longitudeE;
    private String addressS;
    private String iTime;
    private String fTime;
    private int typeEvent;
    private int typeService;
    private int sharedPosition;

    public PhoneData2(String imei, String originNumber,
                      String originName, String targetNumber,
                      String targetName,
                      Double latitudeS, Double longitudeS,
                      Double latitudeE, Double longitudeE,
                      String addressE, String iTime,
                      String fTime, int typeEvent,
                      int typeService, int sharedPosition) {
        this.imei = imei;
        this.originNumber = originNumber;
        this.originName = originName;
        this.targetNumber = targetNumber;
        this.targetName = targetName;
        this.latitudeS = latitudeS;
        this.longitudeS = longitudeS;
        this.latitudeE = latitudeE;
        this.longitudeE = longitudeE;
        this.addressS = addressS;
        this.iTime = iTime;
        this.fTime = fTime;
        this.typeEvent = typeEvent;
        this.typeService = typeService;
        this.sharedPosition = sharedPosition;
    }

    public PhoneData2() {

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

    public Double getLatitudeS() {
        return latitudeS;
    }

    public void setLatitudeS(Double latitude) {
        this.latitudeS = latitude;
    }

    public Double getLongitudeS() {
        return longitudeS;
    }

    public void setLongitudeS(Double longitude) {
        this.longitudeS = longitude;
    }

    public Double getLatitudeE() {
        return latitudeE;
    }

    public void setLatitudeE(Double latitude) {
        this.latitudeE = latitude;
    }

    public Double getLongitudeE() {
        return longitudeE;
    }

    public void setLongitudeE(Double longitude) {
        this.longitudeE = longitude;
    }

    public String getAddressS() {
        return addressS;
    }

    public void setAddressS(String address) {
        this.addressS = address;
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
                ", latitudeS=" + latitudeS +
                ", longitudeS=" + longitudeS +
                ", latitudeE=" + latitudeE +
                ", longitudeE=" + longitudeE +
                ", addressS=" + addressS +
                ", iTime=" + iTime +
                ", fTime=" + fTime +
                ", typeEvent=" + typeEvent +
                ", typeService=" + typeService +
                ", sharedPosition=" + sharedPosition +
                "]";
    }
}
