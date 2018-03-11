package com.researchfip.puc.mytalks.database;

import android.content.Context;

import com.researchfip.puc.mytalks.general.Geo;
import com.researchfip.puc.mytalks.general.PhoneInformation;

/**
 * Created by joaocastro on 06/12/17.
 */

public class PersistPhoneData implements Runnable {

    Context context;
    PhoneInformation phoneInformation;
    DBPersistence persistence;
    PhoneData phoneData;
    Geo geo = new Geo(context);
    int id, typeEvent, typeService;
    String targetNumber;
    boolean isShareAvailable; //this is private (Dont forget)


    public PersistPhoneData(Context context, String[] originInfo, String[] targetInfo,
                            String[] timeLog, int typeEvent, int typeService){
        this.context = context;
        phoneInformation = new PhoneInformation(context);
        persistence = new DBPersistence(context);
        geo = new Geo(context);
        phoneData = new PhoneData();
        phoneData.setImei(phoneInformation.getImei());
        phoneData.setOriginNumber(originInfo[0]);
        phoneData.setOriginName(originInfo[1]);
        phoneData.setTargetNumber(targetInfo[0]);
        phoneData.setTargetName(targetInfo[1]);
        phoneData.setiTime(timeLog[0]);
        phoneData.setfTime(timeLog[1]);
        phoneData.setTypeEvent(typeEvent);
        phoneData.setTypeService(typeService);

        this.typeEvent = typeEvent;
        this.targetNumber = normalizeNumber(targetInfo[0]);
        this.typeService = typeService;

        isShareAvailable = false;
        setCoordinates();
    }

    private void setCoordinates(){
        double[] coordinates = geo.getGeoCoordinates();
        phoneData.setLatitude(coordinates[0]);
        phoneData.setLongitude(coordinates[1]);
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
        if(phoneData.getLatitude() != 0.0 && phoneData.getLongitude() != 0.0){
            isShareAvailable = true;
            if(!phoneInformation.isConnected()){
                address = "";
            }else {
                address = geo.getAddress(phoneData.getLatitude(), phoneData.getLongitude());
            }
        }

        phoneData.setAddress(address);
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
