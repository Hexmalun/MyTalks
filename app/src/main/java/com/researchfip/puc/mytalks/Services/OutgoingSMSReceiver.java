package com.researchfip.puc.mytalks.Services;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import com.researchfip.puc.mytalks.R;
import com.researchfip.puc.mytalks.database.PersistPhoneData;
import com.researchfip.puc.mytalks.database.PersistPhoneData2;
import com.researchfip.puc.mytalks.general.Geo;
import com.researchfip.puc.mytalks.general.PhoneInformation;

/**
 * Created by joaocastro on 08/11/17.
 */

public class OutgoingSMSReceiver extends ContentObserver {


    private Context context;
    private int initialPos;
    private final Uri uriSMS = Uri.parse("content://sms/sent");
    private PhoneInformation pInfo;
    Cursor cursor;
    private int typeEvent, typeService;


    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    OutgoingSMSReceiver(Handler handler, Context context) {
        super(handler);
        this.context = context;
        this.initialPos = getLastMsgId();
        setpInfo(new PhoneInformation(context));
        typeService = pInfo.getSMS_SERVICE_ID();
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        cursor = context.getContentResolver().query(uriSMS,null,null,null,null);
        if(cursor.moveToFirst()){
            if(initialPos < cursor.getInt(cursor.getColumnIndex("_id"))){
                initialPos = cursor.getInt(cursor.getColumnIndex("_id"));
                typeEvent = pInfo.getOutgoingEventId();
                insertSMS();
            }
        }
        cursor.close();
    }

    private void insertSMS(){
        Geo geo = new Geo(context);
        double[] coordinatesS;
        coordinatesS = geo.getGeoCoordinates();

        String[] timeLog = new String[2];
        timeLog[0] = pInfo.getDateTime();
        timeLog[1] = "";

        String[] targetInfo = new String[2];
        String[] originInfo = new String[2];

        if(typeEvent == pInfo.getOutgoingEventId()){
            targetInfo[0] = cursor.getString(cursor.getColumnIndex("address"));
            targetInfo[1] = pInfo.getContactName(this.context, targetInfo[0]);

            originInfo[0] = pInfo.getPhoneNumber();
            originInfo[1] = context.getString(R.string.eu);
        }

        Thread thread = new Thread(new PersistPhoneData(context,originInfo,targetInfo, timeLog,typeEvent,typeService));
        thread.start();
        Thread thread2 = new Thread(new PersistPhoneData2(context,originInfo, targetInfo, timeLog, typeEvent, typeService, coordinatesS,coordinatesS));
        thread2.start();

        try{
            thread.join();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private int getLastMsgId(){
        int lastMsgId = 0;
        Cursor cursor = this.context.getContentResolver().query(uriSMS, null, null, null, null);
        if(cursor.moveToFirst()){
            lastMsgId = cursor.getInt(cursor.getColumnIndex("_id"));
        }
        cursor.close();
        return lastMsgId;
    }


    public PhoneInformation getpInfo() {
        return pInfo;
    }

    public void setpInfo(PhoneInformation pInfo) {
        this.pInfo = pInfo;
    }
}
