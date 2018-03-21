package com.researchfip.puc.mytalks.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.researchfip.puc.mytalks.R;
import com.researchfip.puc.mytalks.database.DBPersistence;
import com.researchfip.puc.mytalks.database.PersistPhoneData;
import com.researchfip.puc.mytalks.database.PersistPhoneData2;
import com.researchfip.puc.mytalks.database.PhoneData;
import com.researchfip.puc.mytalks.general.Geo;
import com.researchfip.puc.mytalks.general.PhoneInformation;

/**
 * Created by joaocastro on 08/11/17.
 */

public class IncomingSMSReceiver extends BroadcastReceiver {

    private PhoneInformation pInfo;
    private Context context;
    private int typeEvent, typeService;
    private DBPersistence persistence;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();

            SmsMessage[] msgs = null;
            String msg_from;
            if(bundle!= null){
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for( int i = 0; i < msgs.length; i++){
                       msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                       msg_from = msgs[i].getOriginatingAddress();
                       insertSMS(msg_from);
                    }

//                    PhoneData[] phoneData =  persistence.getPhoneDataToSynchronization();
//                    for( int i = 0; i < phoneData.length; i++)
//                        System.out.println(phoneData[i].toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }


    public void insertSMS(String originNumber){
        Geo geo = new Geo(context);
        double[] coordinatesS;
        coordinatesS = geo.getGeoCoordinates();
        pInfo = new PhoneInformation(this.context);

        this.typeEvent = pInfo.getIncomingEventId();
        this.typeService = pInfo.getSMS_SERVICE_ID();

        String[] timeLog = new String[2];
        timeLog[0] = pInfo.getDateTime();
        timeLog[1] = "";

        String[] targetInfo = new String[2];
        String[] originInfo = new String[2];

        originInfo[0] = originNumber;
        originInfo[1] = pInfo.getContactName(this.context, originInfo[0]);
        targetInfo[0] = pInfo.getPhoneNumber();
        targetInfo[1] = context.getString(R.string.eu);

        Log.d("IncomingSMS", originInfo[0] + " " + targetInfo[0] + " " + timeLog[0]);

        Thread thread = new Thread(new PersistPhoneData(context,originInfo, targetInfo, timeLog, typeEvent, typeService));
        thread.start();
        Thread thread2 = new Thread(new PersistPhoneData2(context,originInfo, targetInfo, timeLog, typeEvent, typeService, coordinatesS,coordinatesS));
        thread2.start();
        try{
            thread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
