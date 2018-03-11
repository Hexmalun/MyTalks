package com.researchfip.puc.mytalks.general;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

/**
 * Created by joaocastro on 08/11/17.
 */

public class PhoneInformation {

    Context context;

    public static final int SMS_SERVICE_ID = 1;
    public static final int CALL_SERVICE_ID = 2;
    public static final int OUTGOING_EVENT_ID = 0;
    public static final int INCOMING_EVENT_ID = 1;
    private TelephonyManager telephonyManager;


    public PhoneInformation(Context context) {
        this.context = context;
        this.telephonyManager = (TelephonyManager) this.context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @SuppressLint("HardwareIds")
    public String getPhoneNumber() {
        String number = "";
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
         && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            number = this.telephonyManager.getLine1Number();
        } else {
            Log.d("PHONENUMBER", "Erro de permissao!!!!!!!!!!!!!");
        }
        return number;
    }

    public String get10DigitNumber() {
        return getPhoneNumber().substring(2);
    }

    @SuppressLint("HardwareIds")
    public String getImei() {
        String imei = "";
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            imei = this.telephonyManager.getDeviceId();
        }else{
            Log.d("IMEINUMBER", "Erro de permissao!!!!!!!!!!!!!");
        }
        return imei;
    }

    public boolean contactExists(Context context, String number){
        Uri uri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));
        String[] mPhoneNumberProjection = {ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME};
        Cursor cursor = context.getContentResolver().query(uri, mPhoneNumberProjection, null, null, null);

        try{
            if(cursor.moveToFirst()){
                return true;
            }
        }finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return false;
    }

    public String getContactName(Context context, String number){
        Uri uri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));
        String[] mPhoneNumberProjection = {ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME};
        Cursor cursor = context.getContentResolver().query(uri, mPhoneNumberProjection, null, null, null);

        try{
            if(cursor.moveToFirst()){
                return cursor.getString(2);
            }
        }finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return "unknown";
    }

    public String getDateTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public int getSMS_SERVICE_ID(){
        return SMS_SERVICE_ID;
    }

    public int getCALL_SERVICE_ID(){
        return CALL_SERVICE_ID;
    }

    public int getOutgoingEventId() {
        return OUTGOING_EVENT_ID;
    }

    public int getIncomingEventId() {
        return INCOMING_EVENT_ID;
    }
}
