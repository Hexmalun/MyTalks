package com.researchfip.puc.mytalks.UI.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
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
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.researchfip.puc.mytalks.Dialogs.DialogData;
import com.researchfip.puc.mytalks.R;
import com.researchfip.puc.mytalks.UI.MainActivity;
import com.researchfip.puc.mytalks.database.DBPersistence;
import com.researchfip.puc.mytalks.database.DBPersistence2;
import com.researchfip.puc.mytalks.database.DataBaseController;
import com.researchfip.puc.mytalks.database.PhoneData;
import com.researchfip.puc.mytalks.database.PhoneData2;
import com.researchfip.puc.mytalks.general.PhoneInformation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by joaocastro on 23/10/17.
 */

public class HomeFragment extends Fragment {

    private DBPersistence2 persistence;
    int typeService;
    private DataBaseController db;
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
            db = new DataBaseController(getActivity());
            new HomeFragment.LoadDataToRecycler().execute();
        }catch (Exception e){
            e.printStackTrace();
        }
        getSignalInfo(view);
        changeIVWifiStrength(view);
        setDataNumber(view);
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
                    if(m.getAllCellInfo() != null) {
                        final CellInfo info = m.getAllCellInfo().get(0);
                        if (info instanceof CellInfoGsm) {
                            final CellSignalStrengthGsm gsm = ((CellInfoGsm) info).getCellSignalStrength();
                            final CellIdentityGsm idGsm = ((CellInfoGsm) info).getCellIdentity();
                            dbm[0] = "" + gsm.getDbm();
                        } else if (info instanceof CellInfoLte) {
                            final CellSignalStrengthLte lte = ((CellInfoLte) info).getCellSignalStrength();
                            final CellIdentityLte idLte = ((CellInfoLte) info).getCellIdentity();
                            dbm[0] = "" + lte.getDbm();
                        } else if (info instanceof CellInfoCdma) {
                            final CellSignalStrengthCdma cdma = ((CellInfoCdma) info).getCellSignalStrength();
                            final CellIdentityCdma idCdma = ((CellInfoCdma) info).getCellIdentity();
                            dbm[0] = "" + cdma.getDbm();
                        } else if (info instanceof CellInfoWcdma) {
                            final CellSignalStrengthWcdma wcdma = ((CellInfoWcdma) info).getCellSignalStrength();
                            final CellIdentityWcdma idWcdma = ((CellInfoWcdma) info).getCellIdentity();
                            dbm[0] = "" + wcdma.getDbm();
                        }
                        changeIVSIgnalStrength(Integer.parseInt(dbm[0]), V);
                    }
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
            tvstats.setText(" Connected");
            wifiManager.startScan();
            String s = " "+wifiManager.getScanResults().size();
            tvnerby.setText(s);

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
            tvname.setText(" Not Connected");
            tvstats.setText(" Not Connected");
            tvnerby.setText(" 0");
            img.setImageResource(R.mipmap.ic_no_wifi);
        }

    }

    public void setDataNumber(View V){
        if (V != null) {
            ViewGroup parent = (ViewGroup) V.getParent();
            if (parent != null)
                parent.removeView(V);
        }
        try {
            String[] resp = db.getPersonalData();
            if (resp.length <= 1) {
                DialogData newFragment = new DialogData();
                newFragment.show(getFragmentManager(), "dataPicker");
                PackageManager pm = C.getPackageManager();
                List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
                int size = packages.size();
                long [] [] apps = new long [size][4];
                int i = 0;
                long time = System.currentTimeMillis();
                for (ApplicationInfo runningApp : packages) {
                    // Get UID of the selected process
                    int uid = runningApp.uid;
                    long [] l = getTotalBytesManual(uid);
                    long received = l[0];//received amount of each app
                    long send   = l[1];//sent amount of each app
                   // Log.v("DAta:" + C.getPackageManager().getNameForUid(uid) , "Send :" + send + ", Received :" + received);
                    // ApplicationInfo applicationInfo = null;
                    // try {
                    //     applicationInfo = pm.getApplicationInfo(C.getPackageManager().getNameForUid(uid), 0);
                    // } catch (final PackageManager.NameNotFoundException z) {
                    //     z.printStackTrace();
                    //  }
                    //  final String title = (String)((applicationInfo != null) ? pm.getApplicationLabel(applicationInfo) : runningApp.packageName);
                    //  Drawable icon = ((applicationInfo != null)?pm.getApplicationIcon(applicationInfo):null);
                    apps[i][0] = uid;
                    apps[i][1] = send;
                    apps[i][2] = received;
                    apps[i][3] = time;
                    i++;
                }
              //  Log.v("DataFragman.fillData:", "Running apps" + apps.length);
                db.addAllAppData(apps);
            }
        }catch(InflateException e){
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

    private long [] getTotalBytesManual(int localUid){
        File dir = new File("/proc/uid_stat/");
        String[] children = dir.list();
        long [] r = new long [2];
        if(!Arrays.asList(children).contains(String.valueOf(localUid))){
            r[0] = Long.parseLong("0");
            r[1] = Long.parseLong("0");
            return r;
        }
        File uidFileDir = new File("/proc/uid_stat/"+String.valueOf(localUid));
        File uidActualFileReceived = new File(uidFileDir,"tcp_rcv");
        File uidActualFileSent = new File(uidFileDir,"tcp_snd");

        String textReceived = "0";
        String textSent = "0";

        try {
            BufferedReader brReceived = new BufferedReader(new FileReader(uidActualFileReceived));
            BufferedReader brSent = new BufferedReader(new FileReader(uidActualFileSent));
            String receivedLine;
            String sentLine;

            if ((receivedLine = brReceived.readLine()) != null) {
                textReceived = receivedLine;
            }
            if ((sentLine = brSent.readLine()) != null) {
                textSent = sentLine;
            }

        }
        catch (IOException e) {
            Log.v("Erro" , "DataFragment.getTotalBytesManual ");
        }
        r[0] = Long.valueOf(textReceived).longValue();
        r[1] = Long.valueOf(textSent).longValue();
        return r;

    }

}
