package com.researchfip.puc.mytalks.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Castro on 22/11/2017.
 */

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equalsIgnoreCase("android.intent.action.BOOT_COMPLETED")){
            Intent sms_service = new Intent(context, SMSService.class);
            context.startService(sms_service);

            Intent call_service = new Intent(context, CallService.class);
            context.startService(call_service);

            Intent measuremets_service = new Intent(context, getMeasurements.class);
            context.startService(measuremets_service);
        }
    }
}
