package com.researchfip.puc.mytalks.general;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Robert Zagórski on 2016-09-09.
 */
@TargetApi(Build.VERSION_CODES.M)
public class NetworkStatsHelper {

    NetworkStatsManager networkStatsManager;
    int packageUid;

    public NetworkStatsHelper(NetworkStatsManager networkStatsManager) {
        this.networkStatsManager = networkStatsManager;
    }

    public NetworkStatsHelper(NetworkStatsManager networkStatsManager, int packageUid) {
        this.networkStatsManager = networkStatsManager;
        this.packageUid = packageUid;
    }

    public long getAllRxBytesMobile(Context context) {
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                    0,
                    System.currentTimeMillis());

        } catch (RemoteException e) {
            return -1;
        }
        return bucket.getRxBytes();
    }

    public long getAllTxBytesMobile(Context context) {
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                    0,
                    System.currentTimeMillis());
        } catch (RemoteException e) {
            return -1;
        }
        return bucket.getTxBytes();
    }


    public long getAllRxBytesMobile(Context context, long start, long end) {
        NetworkStats.Bucket bucket;
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return 0;
            }
            String subscriberID = tm.getSubscriberId();
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE,
                    subscriberID,
                    start,
                    System.currentTimeMillis());
        } catch (RemoteException e) {
            return -1;
        }
        return bucket.getRxBytes();
    }

    public long getAllTxBytesMobile(Context context, long start, long end) {
        NetworkStats.Bucket bucket;
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return 0;
            }
            String subscriberID = tm.getSubscriberId();
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE,
                    subscriberID,
                    start,
                    end);
        } catch (RemoteException e) {
            return -1;
        }
        return bucket.getTxBytes();
    }

    public long getAllRxBytesWifi() {
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI,
                    "",
                    0,
                    System.currentTimeMillis());
        } catch (RemoteException e) {
            return -1;
        }
        return bucket.getRxBytes();
    }

    public long getAllTxBytesWifi() {
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI,
                    "",
                    0,
                    System.currentTimeMillis());
        } catch (RemoteException e) {
            return -1;
        }
        return bucket.getTxBytes();
    }

    public long getPackageRxBytesMobile(Context context,long start, long end) {
        NetworkStats networkStats = null;
        try {
            networkStats = networkStatsManager.queryDetailsForUid(
                    ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                    start,
                    end,
                    packageUid);
        } catch (RemoteException e) {
            return -1;
        }
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        networkStats.getNextBucket(bucket);
        networkStats.getNextBucket(bucket);
        String TAG = "getPackageRxBytesMobile";
        Log.d(TAG, "Bytes Recieved" + bucket.getRxBytes());
        Log.d(TAG, "Bytes Transfered" + bucket.getTxBytes());
        Log.d(TAG, "Bucket UID " + bucket.getUid()+" "+packageUid);
        Log.d(TAG,"StartTime: "+bucket.getStartTimeStamp()+" "+start);
        Log.d(TAG,"EndTime: "+bucket.getEndTimeStamp()+" "+end);
        return bucket.getRxBytes();
    }

    public long getPackageBytesMobile(Context context,long start, long end) {
        long usage = 0L;

        NetworkStats networkStatsByApp;
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getApplicationContext().getSystemService(Context.NETWORK_STATS_SERVICE);

        try {
            networkStatsByApp = networkStatsManager.querySummary(ConnectivityManager.TYPE_MOBILE, getSubscriberId(context, ConnectivityManager.TYPE_MOBILE), start, System.currentTimeMillis());
            do {
                NetworkStats.Bucket bucket = new NetworkStats.Bucket();
                networkStatsByApp.getNextBucket(bucket);
                if (bucket.getUid() == packageUid) {
                    // in some devices this is immediately looping twice
                    // and the second iteration is returning correct value.
                    // So result is returned in the end.
                    usage = (bucket.getRxBytes() + bucket.getTxBytes());
                    // Create a DateFormatter object for displaying date in specified format.
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
                    // Create a calendar object that will convert the date and time value in milliseconds to date.
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(bucket.getStartTimeStamp());
                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.setTimeInMillis(start);
                    //Log.d("Poatata","StartTime: "+formatter.format(calendar.getTime())+"  "+formatter.format(calendar2.getTime()));
                    calendar.setTimeInMillis(bucket.getEndTimeStamp());
                   // Log.d("Poatata","End: "+formatter.format(calendar.getTime()));
                }
            } while (networkStatsByApp.hasNextBucket());

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return usage;

    }

    public long getPackageTxBytesMobile(Context context,long start, long end) {
        NetworkStats networkStats = null;
        try {
            Log.v("DataFragman.fillData:", "App: getvalue");
            networkStats = networkStatsManager.queryDetailsForUid(
                    ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                    start,
                    end,
                    packageUid);
            Log.v("DataFragman.fillData:", "App:valuerecived");
        } catch (RemoteException e) {
            return -1;
        }
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        networkStats.getNextBucket(bucket);
        return bucket.getTxBytes();
    }

    public long getAllPackageBytesMobile(Context context,long start, long end) {
        long usage = 0L;
        NetworkStats networkStatsByApp;
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getApplicationContext().getSystemService(Context.NETWORK_STATS_SERVICE);
        try {
            networkStatsByApp = networkStatsManager.querySummary(ConnectivityManager.TYPE_MOBILE, getSubscriberId(context, ConnectivityManager.TYPE_MOBILE), start, System.currentTimeMillis());
            do {
                NetworkStats.Bucket bucket = new NetworkStats.Bucket();
                networkStatsByApp.getNextBucket(bucket);
                usage = usage + (bucket.getRxBytes() + bucket.getTxBytes());
            } while (networkStatsByApp.hasNextBucket());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return usage;
    }

    public long getPackageRxBytesWifi() {
        NetworkStats networkStats = null;
        try {
            networkStats = networkStatsManager.queryDetailsForUid(
                    ConnectivityManager.TYPE_WIFI,
                    "",
                    0,
                    System.currentTimeMillis(),
                    packageUid);
        } catch (RemoteException e) {
            return -1;
        }
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        networkStats.getNextBucket(bucket);
        return bucket.getRxBytes();
    }

    public long getPackageTxBytesWifi() {
        NetworkStats networkStats = null;
        try {
            networkStats = networkStatsManager.queryDetailsForUid(
                    ConnectivityManager.TYPE_WIFI,
                    "",
                    0,
                    System.currentTimeMillis(),
                    packageUid);
        } catch (RemoteException e) {
            return -1;
        }
        NetworkStats.Bucket bucket = new NetworkStats.Bucket();
        networkStats.getNextBucket(bucket);
        return bucket.getTxBytes();
    }



    private String getSubscriberId(Context context, int networkType) {
        if (ConnectivityManager.TYPE_MOBILE == networkType) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return "AA";
            }
            return tm.getSubscriberId();
        }
        return "";
    }

    public NetworkStats getAllMobile(Context context) {
        NetworkStats ns;
        try {

            ns = networkStatsManager.queryDetails(ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(context, ConnectivityManager.TYPE_MOBILE),
                    0,
                    System.currentTimeMillis());
        } catch (RemoteException e) {
            return null;
        }
        return ns;
    }
}
