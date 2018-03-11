package com.researchfip.puc.mytalks.Services;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by joaocastro on 08/11/17.
 */

public class SMSService extends Service {


    public ContentResolver contentResolver;
    public final String SMS_CONTENT = "content://sms";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        observer();
        return super.onStartCommand(intent, flags, startId);
    }

    public void observer(){
        this.contentResolver = getContentResolver();
        contentResolver.registerContentObserver(Uri.parse(SMS_CONTENT),true, new OutgoingSMSReceiver(new Handler(), SMSService.this));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
