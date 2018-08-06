package com.researchfip.puc.mytalks.UI.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.researchfip.puc.mytalks.Dialogs.DialogData;
import com.researchfip.puc.mytalks.Dialogs.DialogPermission;
import com.researchfip.puc.mytalks.R;
import com.researchfip.puc.mytalks.database.DBPersistence2;
import com.researchfip.puc.mytalks.database.DataBaseController;
import com.researchfip.puc.mytalks.database.PhoneData2;
import com.researchfip.puc.mytalks.general.NetworkStatsHelper;
import com.researchfip.puc.mytalks.general.PhoneInformation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

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
    private ProgressBar prg;
    private long used = 0;
    private TextView size, up, pl;
    String t,s,d;

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
        db = new DataBaseController(C);
        totalSMS = (TextView) view.findViewById(R.id.totalSMS);
        totalCalls = (TextView) view.findViewById(R.id.totalCalls);
        up = (TextView) view.findViewById(R.id.tv_home_maxData);
        pl = (TextView) view.findViewById(R.id.tv_home_minData);
        prg = (ProgressBar)view.findViewById(R.id.pb_home_dataUsage);
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
        AppOpsManager appOps = (AppOpsManager) C.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), C.getPackageName());
        if (mode != AppOpsManager.MODE_ALLOWED) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
        if (mode == AppOpsManager.MODE_ALLOWED) {
            progressBar ();
        } else if (mode != AppOpsManager.MODE_ALLOWED) {
            DialogPermission newFragment = new DialogPermission();
            newFragment.show(getFragmentManager(), "permissions");
            progressBar ();
        }
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



    @TargetApi(Build.VERSION_CODES.M)
    private void fillNetworkStatsAll(NetworkStatsHelper networkStatsHelper, long s, long e) {
        long mobileRx = networkStatsHelper.getAllRxBytesMobile(C,s,e);
        long mobileTx = networkStatsHelper.getAllTxBytesMobile(C,s,e);
        Log.d("DataFragment.FillNetSA:","use:"+used+"    "+mobileRx+"    "+mobileTx);
        used = mobileRx + mobileTx;
    }

    public void progressBar (){
        long start, end;
        String[] resp = db.getPersonalData();
        if (resp.length != 1) {
            t = resp[2];
            s = resp[1];
            d = resp[0];
            //long startTime = calendar.getTimeInMillis();
            String day = d;
            Calendar e = Calendar.getInstance();
            end = e.getTimeInMillis();
            Calendar st = Calendar.getInstance();
            int d = e.get(Calendar.DAY_OF_MONTH);
            int m = e.get(Calendar.MONTH);
            if (d < Integer.parseInt(day)) {
                if (m == Calendar.JANUARY) {
                    st.set(e.get(Calendar.YEAR) + 1, Calendar.DECEMBER, Integer.parseInt(day));
                } else {
                    st.set(e.get(Calendar.YEAR), m - 1, Integer.parseInt(day));
                }
            } else {
                st.set(e.get(Calendar.YEAR), m, Integer.parseInt(day));
            }
            Log.d("DataFragment.Bar1", "use:" + st.getTime());
            start = st.getTimeInMillis();
            try {
                fillData(start, end);
            } catch (PackageManager.NameNotFoundException e1) {
                e1.printStackTrace();
            }

            float ty = (t.equals("GB")) ? 1073741824 : (t.equals("MB")) ? 1048576 : 1024;
            float aux = used / ty;
            if (aux < 0.5) {
                ty = 1048576;
            }
            float siz = Float.parseFloat(s) / 100;
            int pb = 0;
            if (siz != 0) {
                pb = (int) Math.round((aux / siz));
            }
            prg.setProgress(pb);
            DecimalFormat df = new DecimalFormat("#.###");
            df.setRoundingMode(RoundingMode.CEILING);
            if (ty == 1073741824) {
                up.setText(df.format(aux) + " GB");
            } else if (ty == 1048576) {
                up.setText(df.format(used / ty) + " MB");
            } else {
                up.setText(df.format(aux) + " KB");
            }
            if (t.equals("GB")) {
                pl.setText(df.format(Float.parseFloat(s) - (used / 1073741824)) + " GB");
            } else if (t.equals("MB")) {
                pl.setText(df.format(Float.parseFloat(s) - aux) + " MB");
            } else {
                pl.setText(df.format(Float.parseFloat(s) - aux) + " KB");
            }
        }
    }

    private void fillData(long s, long e) throws PackageManager.NameNotFoundException {
        Log.d("DataFragment.Filldata1","filldata");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d("DataFragment.Filldata2:","filldataif");
            NetworkStatsManager networkStatsManager = (NetworkStatsManager) C.getApplicationContext().getSystemService(Context.NETWORK_STATS_SERVICE);
            NetworkStatsHelper networkStatsHelper = new NetworkStatsHelper(networkStatsManager);
            fillNetworkStatsAll(networkStatsHelper, e, s);
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
            Log.d("setDataNumber:","filldataif");
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
                    apps[i][0] = uid;
                    apps[i][1] = send;
                    apps[i][2] = received;
                    apps[i][3] = time;
                    i++;
                }
              //  Log.v("DataFragman.fillData:", "Running apps" + apps.length);
                db.addAllAppData(apps);
                Log.d("setDataNumber:","resp");
            }
        }catch(InflateException e){
            Log.d("setDataNumber:",e.getMessage());
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
