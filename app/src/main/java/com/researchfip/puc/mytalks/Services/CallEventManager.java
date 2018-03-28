package com.researchfip.puc.mytalks.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.researchfip.puc.mytalks.database.PersistPhoneData2;
import com.researchfip.puc.mytalks.general.Geo;
import com.researchfip.puc.mytalks.general.PhoneInformation;

/**
 * Created by joaocastro on 08/11/17.
 */

public class CallEventManager {
    private Context context;
    private TelephonyManager tm;
    private PhoneInformation pInfo;
    private CallStateListener callStateListener;
    public final String TAG = "CallEventManager";
    private int typeEvent, typeService;
    private String[] timeLog;
    private String[] originInfo;
    private String[] targetInfo;


    private OutgoingCallReceiver outgoingCallReceiver;

    public CallEventManager(Context context){
        this.context = context;
        timeLog = new String[2];
        originInfo = new String[2];
        targetInfo = new String[2];
        callStateListener = new CallStateListener();
        outgoingCallReceiver = new OutgoingCallReceiver();

        pInfo = new PhoneInformation(context);
        typeService = pInfo.getCALL_SERVICE_ID();
    }

    public void start(){
        tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        tm.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        context.registerReceiver(outgoingCallReceiver, intentFilter);
    }

    public void stop(){
        tm.listen(callStateListener, PhoneStateListener.LISTEN_NONE);
        context.unregisterReceiver(outgoingCallReceiver);
    }

    public int getTypeEvent() {
        return typeEvent;
    }

    public void setTypeEvent(int typeEvent) {
        this.typeEvent = typeEvent;
    }

    /*public void insertCall(){
        Thread thread = new Thread(new PersistPhoneData1(context, originInfo, targetInfo, timeLog, typeEvent, typeService));
        Log.d("CallEventManager", " " + originInfo[0] + " " +timeLog[0]);
        thread.start();
        try{
            thread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }*/

    public void insertCall(double [] cooS,double [] cooE){
        Thread thread = new Thread(new PersistPhoneData2(context, originInfo, targetInfo, timeLog, typeEvent, typeService, cooS, cooE));
        Log.d("CallEventManager", " " + originInfo[0] + " " +timeLog[0]);
        thread.start();
        try{
            thread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }



    public class CallStateListener extends  PhoneStateListener {
        private boolean began = false;
        private boolean misscall = false;
        private double[] coordinatesS;
        private double[] coordinatesE;
        Geo geo = new Geo(context);
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if(state == TelephonyManager.CALL_STATE_OFFHOOK && !began){
                began = true;
                timeLog[0] = pInfo.getDateTime();
                coordinatesS = geo.getGeoCoordinates();

            }
            Log.d("CallState","Entrei aqui");

            switch (state){
                case TelephonyManager.CALL_STATE_RINGING:
                    typeEvent = pInfo.getIncomingEventId();
                    targetInfo[0] = pInfo.getPhoneNumber();
                    targetInfo[1] = "MySelf";
                    originInfo[0] = incomingNumber;
                    originInfo[1] = pInfo.getContactName(context, originInfo[0]);
                    misscall = true;
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d("CallState","Idle");
                    if(began){
                        began = false;
                        Log.d("CallState","began if");
                        timeLog[1] = pInfo.getDateTime();
                        coordinatesE = geo.getGeoCoordinates();
                        //insertCall();
                        insertCall(coordinatesS,coordinatesE);
                    }else if(misscall && !began){
                        timeLog[0] = pInfo.getDateTime();
                        timeLog[1] = pInfo.getDateTime();
                        coordinatesS = geo.getGeoCoordinates();
                        coordinatesE = coordinatesS;
                       // insertCall();
                        insertCall(coordinatesS,coordinatesE);
                    }
                    break;
            }
        }
    }


    public class OutgoingCallReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            setTypeEvent(pInfo.getOutgoingEventId());
            targetInfo[0] = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            targetInfo[1] = pInfo.getContactName(context, targetInfo[0]);
            originInfo[0] = pInfo.getPhoneNumber();
            originInfo[1] = "Eu";
        }
    }
}
