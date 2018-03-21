package com.researchfip.puc.mytalks.database;

import android.content.Context;
import android.util.Log;

import com.researchfip.puc.mytalks.general.Geo;
import com.researchfip.puc.mytalks.general.PhoneInformation;

/**
 * Created by joaocastro on 06/12/17.
 */

public class PersistPhoneData2 implements Runnable {

    Context context;
    PhoneInformation phoneInformation;
    DBPersistence2 persistence;
    PhoneData2 phoneData;
    Geo geo = new Geo(context);
    int id, typeEvent, typeService;
    String targetNumber;
    boolean isShareAvailable; //this is private (Dont forget)


    public PersistPhoneData2(Context context, String[] originInfo, String[] targetInfo,
                             String[] timeLog, int typeEvent, int typeService, double [] cooS,double [] cooE){
        this.context = context;
        Log.d("Saved ","sms");

        phoneInformation = new PhoneInformation(context);
        persistence = new DBPersistence2(context);
        geo = new Geo(context);
        phoneData = new PhoneData2();
        phoneData.setImei(phoneInformation.getImei());
        phoneData.setOriginNumber(originInfo[0]);
        phoneData.setOriginName(originInfo[1]);
        phoneData.setTargetNumber(targetInfo[0]);
        phoneData.setTargetName(targetInfo[1]);
        phoneData.setiTime(timeLog[0]);
        phoneData.setfTime(timeLog[1]);
        phoneData.setTypeEvent(typeEvent);
        phoneData.setTypeService(typeService);
        phoneData.setLatitudeS(cooS[0]);
        phoneData.setLongitudeS(cooS[1]);
        phoneData.setLatitudeE(cooE[0]);
        phoneData.setLongitudeE(cooE[1]);
        this.typeEvent = typeEvent;
        this.targetNumber = normalizeNumber(targetInfo[0]);
        this.typeService = typeService;

        isShareAvailable = false;

    }

    private void setCoordinatesS(){
        double[] coordinates = geo.getGeoCoordinates();
        phoneData.setLatitudeS(coordinates[0]);
        phoneData.setLongitudeS(coordinates[1]);
    }

    private void setCoordinatesE(){
        double[] coordinates = geo.getGeoCoordinates();
        phoneData.setLatitudeE(coordinates[0]);
        phoneData.setLongitudeE(coordinates[1]);
    }

    private String normalizeNumber(String number){
        String answer = "";
        if ( number != null ){
            answer = number.replaceAll("[-() ]", "");
        }
        return answer;
    }


    @Override
    public void run() {
        String address = "";
        if(phoneData.getLatitudeS() != 0.0 && phoneData.getLongitudeS() != 0.0){
            isShareAvailable = true;
            if(!phoneInformation.isConnected()){
                address = "";
            }else {
                address = geo.getAddress(phoneData.getLatitudeS(), phoneData.getLongitudeS());
            }
        }

        phoneData.setAddressS(address);
        try {
            this.id = insertPhoneData();

            if(persistence!= null){
                persistence.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private int insertPhoneData(){
        int result = persistence.insertPhoneData(phoneData);
        phoneData = null;
        if(persistence != null){
            persistence.closeDB();
        }
        return result;
    }
}
