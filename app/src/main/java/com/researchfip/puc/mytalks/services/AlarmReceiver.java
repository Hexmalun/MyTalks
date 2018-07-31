package com.researchfip.puc.mytalks.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d("Batata","Batata");//get and send location information
        Toast.makeText(context,"Servicos",Toast.LENGTH_LONG);
    }
}