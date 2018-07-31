package com.researchfip.puc.mytalks.UI.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.researchfip.puc.mytalks.R;
import com.researchfip.puc.mytalks.UI.adapters.AppListAdapter;
import com.researchfip.puc.mytalks.UI.adapters.objects.App;
import com.researchfip.puc.mytalks.database.Cell;
import com.researchfip.puc.mytalks.database.DataBaseController;
import com.researchfip.puc.mytalks.general.NetworkStatsHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import mobi.gspd.segmentedbarview.Segment;
import mobi.gspd.segmentedbarview.SegmentedBarView;
import mobi.gspd.segmentedbarview.SegmentedBarViewSideStyle;

/**
 * Created by joaocastro on 23/10/17.
 */

public class DataFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<App> appList;
    private Context C;
    private View V;
    private DataBaseController db;
    private Cell c;
    private ProgressBar prg;
    private long used = 0;
    private TextView size, up, pl,date,min;
    String t,s,d;
    long s_D;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.item_data);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.data_fragment, container, false);
        C = inflater.getContext();
        getSignalInfo(v);
        db = new DataBaseController(C);
        this.appList = new ArrayList<>();
        recyclerView = (RecyclerView) v.findViewById(R.id.rv_apps);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        String[] resp = db.getPersonalData();
        t = resp[2] ;
        s = resp[1] ;
        d = resp[0] ;
        size = (TextView) v.findViewById(R.id.tv_data_pacoteMax);
        date = (TextView) v.findViewById(R.id.date);
        up = (TextView) v.findViewById(R.id.tv_data_usado);
        pl = (TextView) v.findViewById(R.id.tv_data_restante);
        min = (TextView) v.findViewById(R.id.tv_data_pacoteBase);
        prg = (ProgressBar)v.findViewById(R.id.pb_data_usoPacote);
        size.setText(s+" "+t);
        date.setText(d);
        min.setText("0 "+t);
        progressBar ();
       // App app = new App ("WhatsApp", "550MB", null);
       // App app2 = new App ("Facebook", "250MB", null);
       // App app3 = new App ("Instagram", "300MB", null);
       // App app4 = new App ("Twitter", "400MB", null);
       // appList.add(app);
       // appList.add(app2);
       // appList.add(app3);
       // appList.add(app4);
        Toast.makeText(C, android.net.TrafficStats.getTotalRxBytes()+"Bytes", Toast.LENGTH_SHORT).show();
        AppListAdapter adapter = new AppListAdapter(appList);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());;

        return v;
    }

    public void getSignalInfo(final View v) {
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
                    List<Segment> segments = new ArrayList<>();
                    Segment segment = new Segment(-150, -110, "Low", Color.parseColor("#EF3D2F"));
                    segments.add(segment);
                    Segment segment2 = new Segment(-110,  -90, "Average", Color.parseColor("#f4f400"));
                    segments.add(segment2);
                    Segment segment3 = new Segment(-90, 0, "High", Color.parseColor("#8CC63E"));
                    segments.add(segment3);

                    SegmentedBarView barView = (SegmentedBarView) v.findViewById(R.id.bar_view);
                    barView.setSegments(segments);
                    barView.setValue(Float.parseFloat(dbm[0]));
                    barView.setShowDescriptionText(true);
                    barView.setShowSegmentText(false);
                    barView.setValueSignSize(200, 100);
                    barView.setUnit("dBm");
                    barView.setSideStyle(SegmentedBarViewSideStyle.ROUNDED);
                }
            }
            if (ActivityCompat.checkSelfPermission(C, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(C, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                MyPhoneStateListener myListener = new MyPhoneStateListener();
                TelephonyManager telManager = (TelephonyManager) C.getSystemService(Context.TELEPHONY_SERVICE);
                telManager.listen(myListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
            }
        }
    }

    public void progressBar (){
        long start, end;
        //long startTime = calendar.getTimeInMillis();
        String day = date.getText().toString();
        Calendar e = Calendar.getInstance();
        end = e.getTimeInMillis();
        Calendar st = Calendar.getInstance();
        int d = e.get(Calendar.DAY_OF_MONTH);
        int m = e.get(Calendar.MONTH);
        if( d < Integer.parseInt(day)){
            if(m == Calendar.JANUARY){
                st.set(e.get(Calendar.YEAR)+1, Calendar.DECEMBER, Integer.parseInt(day));
            }else{
                st.set(e.get(Calendar.YEAR), m - 1, Integer.parseInt(day));
            }
        }else{
            st.set(e.get(Calendar.YEAR), m, Integer.parseInt(day));
        }
        Log.d("DataFragment.Bar1","use:"+st.getTime());
        start = st.getTimeInMillis();
        try {
            fillData(start, end);
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }

        float ty = (t.equals("GB")) ? 1073741824 : (t.equals("MB")) ? 1048576 : 1024;
        float aux = used/ty;
        if (aux < 0.5){
            ty = 1048576;
        }
        float siz = Float.parseFloat(s)/100;
        int pb = 0;
        if(siz != 0) {
            pb = (int)Math.round((aux / siz));
         }
        prg.setProgress(pb);
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.CEILING);
        if(ty == 1073741824) {
            up.setText(df.format(aux)+" GB");
        }else if(ty == 1048576){
            up.setText(df.format(used/ty)+" MB");
        }else{
            up.setText(df.format(aux)+" KB");
        }

        if(t.equals("GB")) {
            pl.setText(df.format(Float.parseFloat(s)-(used/1073741824)) +" GB");
        }else if(t.equals("MB")){
            pl.setText(df.format(Float.parseFloat(s)- aux)+" MB");
        }else{
            pl.setText(df.format(Float.parseFloat(s)- aux)+" KB");
        }
    }

    private void fillData(long s, long e) throws PackageManager.NameNotFoundException {
        Log.d("DataFragment.Filldata1","filldata");
        this.appList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d("DataFragment.Filldata2:","filldataif");
            NetworkStatsManager networkStatsManager = (NetworkStatsManager) C.getApplicationContext().getSystemService(Context.NETWORK_STATS_SERVICE);
            NetworkStatsHelper networkStatsHelper = new NetworkStatsHelper(networkStatsManager);
             s_D = s;
            fillNetworkStatsAll(networkStatsHelper, e, s_D);
            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTimeInMillis(s);
          }else{
            PackageManager pm = C.getPackageManager();
            List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
            Log.v("DataFragman.fillData:", "Running apps" + packages.size());
            int size = packages.size();
            long[][] apps = new long[size][2];
            int i = 0;
            for (ApplicationInfo runningApp : packages) {
                // Get UID of the selected process
                int uid = runningApp.uid;
                long[] l = getTotalBytesManual(uid);
                long received = l[0];//received amount of each app
                long send = l[1];//sent amount of each app
                apps[i][0] = uid;
                apps[i][1] = send + received;
                i++;
            }
            Log.v("fillData", "");
            apps = db.getAppData2(apps);
            for (int j = 0; j < apps.length; j++) {
                int uid = (int) apps[j][0];
                long totalused = apps[j][1];
                // Log.v("DAta:" + C.getPackageManager().getNameForUid(uid) , "Used:" +totalused);
                ApplicationInfo applicationInfo = null;
                try {
                    applicationInfo = pm.getApplicationInfo(C.getPackageManager().getNameForUid(uid), 0);
                } catch (final PackageManager.NameNotFoundException z) {
                    // z.printStackTrace();
                }
                final String title = (String) ((applicationInfo != null) ? pm.getApplicationLabel(applicationInfo) : C.getPackageManager().getNameForUid(uid));
                Drawable icon = ((applicationInfo != null) ? pm.getApplicationIcon(C.getPackageManager().getNameForUid(uid)) : null);
                //Drawable appIcon = pm.getApplicationIcon("com.google.maps");
                //  Drawable myIcon = getResources().getDrawable( R.drawable.ic_home_wifi );
                if (totalused > 0) {
                    App app = new App(title, totalused + "", icon);
                    appList.add(app);
                }
            }
            Set<App> depdupeCustomers = new LinkedHashSet<>(appList);
            appList.clear();
            appList.addAll(depdupeCustomers);
            Collections.sort(appList);
        }

       // if(send+received > 0) {
        //    App app = new App(title, send + received + "", icon);
         //   appList.add(app);
        //}
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

    private long  getTotalBytesMobileManual(){
        File dir1 = new File("/sys/class/net/rmnet0/statistics/rx_bytes");
        File dir2 = new File("/sys/class/net/rmnet0/statistics/tx_bytes");
        String textReceived = "0";
        String textSent = "0";

        try {
            BufferedReader brReceived = new BufferedReader(new FileReader(dir1));
            BufferedReader brSent = new BufferedReader(new FileReader(dir2));
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
        return Long.valueOf(textReceived).longValue()+Long.valueOf(textSent).longValue();

    }


    @TargetApi(Build.VERSION_CODES.M)
    private long [][] fillNetworkStatsAll(NetworkStatsHelper networkStatsHelper, long s, long e) {
        s = s_D;
        long mobileRx = networkStatsHelper.getAllRxBytesMobile(C,s_D,e);
        long mobileTx = networkStatsHelper.getAllTxBytesMobile(C,s_D,e);
      //  Log.d("DataFragment.FillNetSA:","use:"+used+"    "+mobileRx+"    "+mobileTx);
        used = mobileRx + mobileTx;
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) C.getApplicationContext().getSystemService(Context.NETWORK_STATS_SERVICE);
        PackageManager pm = C.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
     //   Log.d("DataFragman.fillData:", "Running apps" + packages.size());
        int size = packages.size();
        long[][] apps = new long[size][2];
        int i = 0;
        for (ApplicationInfo runningApp : packages) {
            // Get UID of the selected process
            int uid = runningApp.uid;
            NetworkStatsHelper nsh= new NetworkStatsHelper(networkStatsManager, uid);
            apps[i][0] = uid;
            ApplicationInfo applicationInfo = null;
            try {
                applicationInfo = pm.getApplicationInfo(C.getPackageManager().getNameForUid(uid), 0);
            } catch (final PackageManager.NameNotFoundException z) {
                // z.printStackTrace();
            }
            final String title = (String) ((applicationInfo != null) ? pm.getApplicationLabel(applicationInfo) : C.getPackageManager().getNameForUid(uid));
            Drawable icon = null;
            try {
                icon = ((applicationInfo != null) ? pm.getApplicationIcon(C.getPackageManager().getNameForUid(uid)) : null);
            } catch (PackageManager.NameNotFoundException e1) {
                e1.printStackTrace();
            }
          //  Log.d("DataFragman.fillData:", "App:"+  title );


            long received = nsh.getPackageBytesMobile(C,s,e);
            long send = nsh.getPackageTxBytesMobile(C,s,e);
            apps[i][1] = received;
        //    Log.d("DataFragman.fillData:", "App:"+title+"  "+  apps[i][0] +" spend: "+ apps[i][1]);
            i++;
        }
        for (int j = 0; j < apps.length; j++) {
            int uid = (int) apps[j][0];
            long totalused = apps[j][1];
            // Log.v("DAta:" + C.getPackageManager().getNameForUid(uid) , "Used:" +totalused);
            ApplicationInfo applicationInfo = null;
            try {
                applicationInfo = pm.getApplicationInfo(C.getPackageManager().getNameForUid(uid), 0);
            } catch (final PackageManager.NameNotFoundException z) {
                // z.printStackTrace();
            }
            final String title = (String) ((applicationInfo != null) ? pm.getApplicationLabel(applicationInfo) : C.getPackageManager().getNameForUid(uid));
            Drawable icon = null;
            try {
                icon = ((applicationInfo != null) ? pm.getApplicationIcon(C.getPackageManager().getNameForUid(uid)) : null);
            } catch (PackageManager.NameNotFoundException e1) {
                e1.printStackTrace();
            }
            //Drawable appIcon = pm.getApplicationIcon("com.google.maps");
            //  Drawable myIcon = getResources().getDrawable( R.drawable.ic_home_wifi );
            if (totalused > 0) {
                App app = new App(title, totalused + "", icon);
                appList.add(app);
            }
        }
        Collections.sort(appList);
        List<App> appListaux = new ArrayList<>();
        for (int k = 0; k<appList.size(); k++){
            boolean aux = true;
            for (int l = 0; l<appListaux.size(); l++){
                if (appList.get(k).getUsage().equals(appListaux.get(l).getUsage())){
                    aux = false;
                    l = appListaux.size();
                }
            }
           if(aux){
                appListaux.add(appList.get(k));
           }
        }
        appList = appListaux;
        return apps;
    }

}
