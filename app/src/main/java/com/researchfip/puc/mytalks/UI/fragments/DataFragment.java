package com.researchfip.puc.mytalks.UI.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.researchfip.puc.mytalks.R;
import com.researchfip.puc.mytalks.UI.adapters.AppListAdapter;
import com.researchfip.puc.mytalks.UI.adapters.objects.App;
import com.researchfip.puc.mytalks.database.Cell;
import com.researchfip.puc.mytalks.database.DataBaseController;
import com.researchfip.puc.mytalks.general.NetworkStatsHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
        App app = new App ("WhatsApp", "550MB", null);
        App app2 = new App ("Facebook", "250MB", null);
        App app3 = new App ("Instagram", "300MB", null);
        App app4 = new App ("Twitter", "400MB", null);
        appList.add(app);
        appList.add(app2);
        appList.add(app3);
        appList.add(app4);

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
        start = st.getTimeInMillis();
        fillData(start, end);

        int ty = (t.equals("GB")) ? 1000000000 : (t.equals("MB")) ? 1000000 : 1000;
        long aux = used/ty;
        long siz = Long.parseLong(s)/100;
        int pb = 0;
        if(siz != 0) {
            pb = Math.round((aux / siz));
            Log.d("DataUsed#:","use:"+pb);
        }
        Log.d("DataUsed¨&:","use:"+used);
        prg.setProgress(pb);
    }

    private void fillData(long s, long e) {
        Log.d("DataUsed:","filldata");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d("DataUsed:","filldataif");
            NetworkStatsManager networkStatsManager = (NetworkStatsManager) C.getApplicationContext().getSystemService(Context.NETWORK_STATS_SERVICE);
            NetworkStatsHelper networkStatsHelper = new NetworkStatsHelper(networkStatsManager);
            fillNetworkStatsAll(networkStatsHelper, e, s);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void fillNetworkStatsAll(NetworkStatsHelper networkStatsHelper, long s, long e) {
        long mobileRx = networkStatsHelper.getAllRxBytesMobile(C,s,e);
        long mobileTx = networkStatsHelper.getAllTxBytesMobile(C,s,e);
        Log.d("DataUsed:","use:"+used+"    "+mobileRx+"    "+mobileTx);
        used = mobileRx + mobileTx;
    }

}
