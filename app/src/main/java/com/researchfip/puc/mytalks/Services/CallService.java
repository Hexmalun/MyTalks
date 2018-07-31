package com.researchfip.puc.mytalks.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by joaocastro on 08/11/17.
 */

public class CallService extends Service {

    private CallEventManager callEventManager;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        callEventManager = new CallEventManager(this);
        int res = super.onStartCommand(intent, flags, startId);
        callEventManager.start();
        return res;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callEventManager.stop();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
