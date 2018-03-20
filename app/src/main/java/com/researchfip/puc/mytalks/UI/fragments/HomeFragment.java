package com.researchfip.puc.mytalks.UI.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.researchfip.puc.mytalks.R;
import com.researchfip.puc.mytalks.UI.MainActivity;
import com.researchfip.puc.mytalks.database.DBPersistence;
import com.researchfip.puc.mytalks.database.PhoneData;
import com.researchfip.puc.mytalks.general.PhoneInformation;

import java.security.PublicKey;
import java.util.ArrayList;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by joaocastro on 23/10/17.
 */

public class HomeFragment extends Fragment {

    private DBPersistence persistence;
    int typeService;
    private Cursor cursor;
    TextView totalSMS;
    TextView totalCalls;
    int sms = 0;
    int calls = 0;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.item_home);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        totalSMS = (TextView) view.findViewById(R.id.totalSMS);
        totalCalls = (TextView) view.findViewById(R.id.totalCalls);
        try{
            persistence = new DBPersistence(getActivity());
            new HomeFragment.LoadDataToRecycler().execute();
        }catch (Exception e){
            e.printStackTrace();
        }

        return view;
    }

    public void getTotalSMS(Cursor cursor){
        int receivedAndSend = 0;
        PhoneData[] phoneData = new PhoneData[cursor.getCount()];
        for(int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            phoneData[i] = persistence.cursorToPhoneData(cursor);
            receivedAndSend++;
        }
        Log.d("Table String",persistence.getTableAsString());
        sms = receivedAndSend;
    }

    public void getTotalCalls(Cursor cursor){
        int receivedAndSend = 0;
        PhoneData[] phoneData = new PhoneData[cursor.getCount()];
        for(int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            phoneData[i] = persistence.cursorToPhoneData(cursor);
            receivedAndSend++;
        }
        Log.d("Table String",persistence.getTableAsString());
        calls = receivedAndSend;
    }

    public class LoadDataToRecycler extends AsyncTask<String, Void, String> {

        private SimpleCursorAdapter adapter;


        @Override
        protected String doInBackground(String... strings) {
            cursor = persistence.getPhoneDataToViews(PhoneInformation.SMS_SERVICE_ID);
            getTotalSMS(cursor);
            cursor = persistence.getPhoneDataToViews(PhoneInformation.CALL_SERVICE_ID);
            getTotalCalls(cursor);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            totalSMS.setText(sms+"");
            totalCalls.setText(calls +"");
        }

    }

}
