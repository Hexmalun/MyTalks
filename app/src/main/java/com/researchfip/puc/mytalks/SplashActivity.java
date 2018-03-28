package com.researchfip.puc.mytalks;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.researchfip.puc.mytalks.Services.CallService;
import com.researchfip.puc.mytalks.Services.SMSService;
import com.researchfip.puc.mytalks.Services.getMeasurements;
import com.researchfip.puc.mytalks.UI.MainActivity;

public class SplashActivity extends AppCompatActivity {


    Intent serviceSMS, serviceCall;
    private final int MY_PERMISSIONS_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int ok = PackageManager.PERMISSION_GRANTED;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == ok
            && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == ok
            && ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == ok
            && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) == ok
            && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == ok
            && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == ok
            && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == ok
            && ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_BOOT_COMPLETED) == ok
            && ContextCompat.checkSelfPermission(this, Manifest.permission.PROCESS_OUTGOING_CALLS) == ok
            && ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == ok
            && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == ok
            && ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) == ok
            && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == ok){


            if(!isSMSServiceRunning()){
                serviceSMS = new Intent(this, SMSService.class);
                startService(serviceSMS);
            }

            if(!isCallServiceRunning()) {
                serviceCall = new Intent(this, CallService.class);
                startService(serviceCall);
            }

            if(!isGetMServiceRunning()) {
                serviceCall = new Intent(this, getMeasurements.class);
                startService(serviceCall);
            }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    gotoMain();
                }
            }, 4000);

        }else{
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_SMS,
                            Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.RECEIVE_BOOT_COMPLETED,
                            Manifest.permission.PROCESS_OUTGOING_CALLS,
                            Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.SYSTEM_ALERT_WINDOW,
                            Manifest.permission.READ_PHONE_STATE},
                            MY_PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if(!isSMSServiceRunning()){
                        serviceSMS = new Intent(this, SMSService.class);
                        startService(serviceSMS);
                    }

                    if(!isCallServiceRunning()) {
                        serviceCall = new Intent(this, CallService.class);
                        startService(serviceCall);
                    }

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            gotoMain();
                        }
                    }, 2000);

                } else {
                    Toast.makeText(this, "Favor permitir leitura de SMS", Toast.LENGTH_SHORT).show();
                }
        }
    }

    public boolean isSMSServiceRunning(){
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)){
            if(SMSService.class.getName().equals(serviceInfo.service.getClassName())){
                return true;
            }
        }
        return false;
    }

    public boolean isCallServiceRunning(){
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)){
            if(CallService.class.getName().equals(serviceInfo.service.getClassName())){
                return true;
            }
        }
        return false;
    }

    public boolean isGetMServiceRunning(){
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)){
            if(getMeasurements.class.getName().equals(serviceInfo.service.getClassName())){
                return true;
            }
        }
        return false;
    }

    public void gotoMain(){
        Intent intent = new Intent(SplashActivity.this,
                MainActivity.class);
        startActivity(intent);
        finish();
    }
}
