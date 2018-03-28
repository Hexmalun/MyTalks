package com.researchfip.puc.mytalks.UI.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.researchfip.puc.mytalks.R;
import com.researchfip.puc.mytalks.UI.MainActivity;
import com.researchfip.puc.mytalks.database.DBPersistence;
import com.researchfip.puc.mytalks.database.DBPersistence2;
import com.researchfip.puc.mytalks.database.DataBaseController;
import com.researchfip.puc.mytalks.database.PhoneData;
import com.researchfip.puc.mytalks.database.PhoneData2;
import com.researchfip.puc.mytalks.general.PhoneInformation;

import java.security.PublicKey;
import java.util.ArrayList;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by joaocastro on 23/10/17.
 */

public class HomeFragment extends Fragment {

    private DBPersistence2 persistence;
    int typeService;
    private Cursor cursor;
    TextView totalSMS;
    TextView totalCalls;
    int sms = 0;
    int calls = 0;
    private Context C;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.item_home);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        C = inflater.getContext();
        totalSMS = (TextView) view.findViewById(R.id.totalSMS);
        totalCalls = (TextView) view.findViewById(R.id.totalCalls);
        try{
            persistence = new DBPersistence2(getActivity());
            new HomeFragment.LoadDataToRecycler().execute();
        }catch (Exception e){
            e.printStackTrace();
        }
        getSignalInfo(view);
        changeIVWifiStrength(view);
        return view;
    }

    public void getTotalSMS(Cursor cursor){
        int receivedAndSend = 0;
        PhoneData2[] phoneData = new PhoneData2[cursor.getCount()];
        for(int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            phoneData[i] = persistence.cursorToPhoneData(cursor);
            receivedAndSend++;
        }
        //Log.d("Table String",persistence.getTableAsString());
        sms = receivedAndSend;
    }

    public void getTotalCalls(Cursor cursor){
        int receivedAndSend = 0;
        PhoneData2[] phoneData = new PhoneData2[cursor.getCount()];
        for(int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            phoneData[i] = persistence.cursorToPhoneData(cursor);
            receivedAndSend++;
        }
        //Log.d("Table String",persistence.getTableAsString());
        calls = receivedAndSend;
    }

    public void getSignalInfo(final View V) {
        final String[] dbm = new String[1];
        final int[] permissionCheckNS = {ContextCompat.checkSelfPermission(C,
                Manifest.permission.ACCESS_NETWORK_STATE)};
        if (permissionCheckNS[0] == PackageManager.PERMISSION_GRANTED) {
             final TelephonyManager m = (TelephonyManager) C.getSystemService(Context.TELEPHONY_SERVICE);
             class MyPhoneStateListener extends PhoneStateListener {
                @Override
                public void onSignalStrengthsChanged(SignalStrength ss) {
                    super.onSignalStrengthsChanged(ss);
                    if (ActivityCompat.checkSelfPermission(C, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                      return;
                    }
                    final CellInfo info = m.getAllCellInfo().get(0);
                    if (info instanceof CellInfoGsm) {
                        final CellSignalStrengthGsm gsm = ((CellInfoGsm) info).getCellSignalStrength();
                        final CellIdentityGsm idGsm = ((CellInfoGsm) info).getCellIdentity();
                        dbm[0] = ""+ gsm.getDbm();
                    } else if (info instanceof CellInfoLte) {
                        final CellSignalStrengthLte lte = ((CellInfoLte) info).getCellSignalStrength();
                        final CellIdentityLte idLte = ((CellInfoLte) info).getCellIdentity();
                        dbm[0] = ""+ lte.getDbm();
                    } else if (info instanceof CellInfoCdma) {
                        final CellSignalStrengthCdma cdma = ((CellInfoCdma) info).getCellSignalStrength();
                        final CellIdentityCdma idCdma = ((CellInfoCdma) info).getCellIdentity();
                        dbm[0] = ""+cdma.getDbm();
                    } else if (info instanceof CellInfoWcdma) {
                        final CellSignalStrengthWcdma wcdma = ((CellInfoWcdma) info).getCellSignalStrength();
                        final CellIdentityWcdma idWcdma = ((CellInfoWcdma) info).getCellIdentity();
                        dbm[0] = ""+ wcdma.getDbm();
                    }
                    changeIVSIgnalStrength(Integer.parseInt(dbm[0]),V);
                }
            }
            if (ActivityCompat.checkSelfPermission(C, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(C, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                MyPhoneStateListener myListener = new MyPhoneStateListener();
                TelephonyManager telManager = (TelephonyManager) C.getSystemService(Context.TELEPHONY_SERVICE);
                telManager.listen(myListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
            }
        }
    }

    public void changeIVSIgnalStrength(int dbm, View v){
        ImageView img = (ImageView) v.findViewById(R.id.antenna_signal);
        TextView tv = (TextView) v.findViewById(R.id.signal_quality);
        TextView tvcom = (TextView) v.findViewById(R.id.data_con);
        final ConnectivityManager connMgr = (ConnectivityManager)
                C.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo data = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (data.isConnectedOrConnecting()) {
            tvcom.setText("Connected");
        }
        if (-1 * dbm >= 150) {
            img.setImageResource(R.drawable.ic_signal_cellular_outline_black_48dp);
            tv.setText("No Signal");
        } else if (-1 * dbm >= 110) {
            img.setImageResource(R.drawable.ic_signal_cellular_1_black_48dp);
            tv.setText("Bad");
        } else if (-1 * dbm < 110 && -1 * dbm > 90) {
            img.setImageResource(R.drawable.ic_signal_cellular_2_black_48dp);
            tv.setText("Average");
        } else {
            img.setImageResource(R.drawable.ic_signal_cellular_3_black_48dp);
            tv.setText("Good");
        }


    }

    public void changeIVWifiStrength(View v){
        ImageView img = (ImageView) v.findViewById(R.id.wifi_signal);
        TextView tvname = (TextView) v.findViewById(R.id.tv_home_wifiName);
        TextView tvstats = (TextView) v.findViewById(R.id.tv_home_wifiStateConnected);
        TextView tvnerby = (TextView) v.findViewById(R.id.num_wifi);
        final ConnectivityManager connMgr = (ConnectivityManager)
                C.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifi.isConnectedOrConnecting()) {
            WifiManager wifiManager = (WifiManager) C.getSystemService(Context.WIFI_SERVICE);
            int numberOfLevels = 3;
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
            tvname.setText(wifiInfo.getSSID());
            tvstats.setText("Connected");
            tvnerby.setText("d");

            if (level == 0) {
                img.setImageResource(R.mipmap.ic_low_wifi);
                //  tv.setText("Bad");
            } else if (level == 1) {
                img.setImageResource(R.mipmap.ic_average_wifi);
                //  tv.setText("Average");
            } else {
                img.setImageResource(R.mipmap.ic_good_wifi);
                //  tv.setText("Good");
            }
        }else{
            tvname.setText("Not Connected");
            tvstats.setText("Not Connected");
            tvnerby.setText("0");
            img.setImageResource(R.mipmap.ic_no_wifi);
        }

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
