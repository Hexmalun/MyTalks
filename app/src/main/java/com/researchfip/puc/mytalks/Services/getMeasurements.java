package com.researchfip.puc.mytalks.Services;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.TrafficStats;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
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
import android.widget.RemoteViews;
import android.widget.Toast;

import com.researchfip.puc.mytalks.R;
import com.researchfip.puc.mytalks.UI.MainActivity;
import com.researchfip.puc.mytalks.database.Cell;
import com.researchfip.puc.mytalks.database.DataBaseController;
import com.researchfip.puc.mytalks.database.SendData;

import java.util.List;

/**
 * Created by Mateus on 10/06/2016.
 */
public class getMeasurements extends Service implements LocationListener {

    private LocationManager locationManager;
    private Cell c;
    private Cell save;
    private DataBaseController db;
    private Cell aux;
private Context context;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public getMeasurements() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int startId, int i) {
        c = new Cell();
        aux = new Cell();
        db = new DataBaseController(this);
        context = this;
        final TelephonyManager m = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location;
            if (Build.VERSION.SDK_INT >= 21) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            } else {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            if (location != null) {
                onLocationChanged(location);
            } else {
                c.setLat(0);
                c.setLon(0);
            }
        } else {
            c.setLat(0);
            c.setLon(0);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) {
            class MyPhoneStateListener extends PhoneStateListener {
                @Override
                public void onSignalStrengthsChanged(SignalStrength ss) {
                    super.onSignalStrengthsChanged(ss);
                    String Type = "";
                    switch (m.getNetworkType()) {
                        case 1:
                            Type = "GPRS";
                            break;
                        case 2:
                            Type = "EDGE";
                            break;
                        case 3:
                            Type = "UMTS";
                            break;
                        case 4:
                            Type = "CDMA";
                            break;
                        case 5:
                            Type = "EVDO-0";
                            break;
                        case 6:
                            Type = "EVDO-A";
                            break;
                        case 7:
                            Type = "1xRTT";
                            break;
                        case 8:
                            Type = "HSDPA";
                            break;
                        case 9:
                            Type = "HSUPA";
                            break;
                        case 10:
                            Type = "HSPA";
                            break;
                        case 11:
                            Type = "iDen";
                            break;
                        case 12:
                            Type = "EVDO-B";
                            break;
                        case 13:
                            Type = "LTE";
                            break;
                        case 14:
                            Type = "eHRPD";
                            break;
                        case 15:
                            Type = "HSPA+";
                            break;
                        case 16:
                            Type = "GSM";
                            break;
                        case 0:
                            Type = "Nenhuma";
                            break;
                    }
                    c.setType(Type);
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    if(m.getAllCellInfo()!= null) {
                        final CellInfo info = m.getAllCellInfo().get(0);
                        if (info instanceof CellInfoGsm) {
                            final CellSignalStrengthGsm gsm = ((CellInfoGsm) info).getCellSignalStrength();
                            final CellIdentityGsm idGsm = ((CellInfoGsm) info).getCellIdentity();
                            c.setSamples(gsm.getDbm());
                            c.setCell(idGsm.getCid());
                            c.setLac(idGsm.getLac());
                            c.setMnc("" + idGsm.getMnc());
                        } else if (info instanceof CellInfoLte) {
                            final CellSignalStrengthLte lte = ((CellInfoLte) info).getCellSignalStrength();
                            final CellIdentityLte idLte = ((CellInfoLte) info).getCellIdentity();
                            c.setSamples(lte.getDbm());
                            c.setCell(idLte.getCi());
                            c.setLac(idLte.getTac());
                            c.setMnc("" + idLte.getMnc());
                        } else if (info instanceof CellInfoCdma) {
                            final CellSignalStrengthCdma cdma = ((CellInfoCdma) info).getCellSignalStrength();
                            final CellIdentityCdma idCdma = ((CellInfoCdma) info).getCellIdentity();
                            c.setSamples(cdma.getDbm());
                            c.setCell(idCdma.getBasestationId());
                        } else if (info instanceof CellInfoWcdma) {
                            final CellSignalStrengthWcdma wcdma = ((CellInfoWcdma) info).getCellSignalStrength();
                            final CellIdentityWcdma idWcdma = ((CellInfoWcdma) info).getCellIdentity();
                            c.setSamples(wcdma.getDbm());
                            c.setCell(idWcdma.getCid());
                            c.setLac(idWcdma.getLac());
                            c.setMnc("" + idWcdma.getMnc());
                        }
                        if (-1 * c.getSamples() >= 110) {
                            createNotification("vermelho", "Sinal Ruim, Não Utilize os Dados");
                        } else if (-1 * c.getSamples() < 110 && -1 * c.getSamples() > 90) {
                            createNotification("amarelo", "Fique Atento, Utilize Somente Se Necessário");
                        } else {
                            createNotification("verde", "Sinal Bom, Sem Problemas");
                        }

                        if (aux.getCell() - c.getCell() != 0 || c.getSamples() != aux.getSamples()) {
                            db.addCellR(c);
                            //Log.d("Table String", "new");
                            // sendMeasurement(c);
                            aux.setCell(c.getCell());
                            aux.setSamples(c.getSamples());
                        }
                    }
                }
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                MyPhoneStateListener myListener = new MyPhoneStateListener();
                TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                //4.4.2
                telManager.listen(myListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(Location location) {
        c.setLat(location.getLatitude());
        c.setLon(location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * This sample demonstrates notifications with custom content views.
     *
     * <p>On API level 16 and above a big content view is also defined that is used for the
     * 'expanded' notification. The notification is created by the NotificationCompat.Builder.
     * The expanded content view is set directly on the {@link android.app.Notification} once it has been build.
     * (See {@link android.app.Notification#bigContentView}.) </p>
     *
     * <p>The content views are inflated as {@link android.widget.RemoteViews} directly from their XML layout
     * definitions using {@link android.widget.RemoteViews#RemoteViews(String, int)}.</p>
     */
    private void createNotification(String cor, String text) {
        // BEGIN_INCLUDE(notificationCompat)
        android.support.v4.app.NotificationCompat.Builder builder = new android.support.v4.app.NotificationCompat.Builder(this);
        // END_INCLUDE(notificationCompat)

        // BEGIN_INCLUDE(intent)
        //Create Intent to launch this Activity again if the notification is clicked.
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(this, 0, i,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(intent);
        // END_INCLUDE(intent)

        // BEGIN_INCLUDE(ticker)
        // Sets the ticker text
        builder.setTicker(getResources().getString(R.string.custom_notification));

        // Sets the small icon for the ticker
        if (cor.equals("verde")) {
            builder.setSmallIcon(R.drawable.verde);
        } else if (cor.equals("vermelho")) {
            builder.setSmallIcon(R.drawable.vermelho);
        } else {
            builder.setSmallIcon(R.drawable.amarelo);
        }
        // END_INCLUDE(ticker)

        // BEGIN_INCLUDE(buildNotification)
        // Cancel the notification when clicked
        builder.setAutoCancel(true);

        // Build the notification
        Notification notification = builder.build();
        // END_INCLUDE(buildNotification)

        // BEGIN_INCLUDE(customLayout)
        // Inflate the notification layout as RemoteViews
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification);

        // Set text on a TextView in the RemoteViews programmatically.
        contentView.setTextViewText(R.id.textView, text);
        if (cor.equals("verde")) {
            contentView.setImageViewResource(R.id.imageView, R.drawable.verde);
        } else if (cor.equals("vermelho")) {
            contentView.setImageViewResource(R.id.imageView, R.drawable.vermelho);
        } else {
            contentView.setImageViewResource(R.id.imageView, R.drawable.amarelo);
        }


        /* Workaround: Need to set the content view here directly on the notification.
         * NotificationCompatBuilder contains a bug that prevents this from working on platform
         * versions HoneyComb.
         * See https://code.google.com/p/android/issues/detail?id=30495
         */
        notification.contentView = contentView;

        // Add a big content view to the notification if supported.
        // Support for expanded notifications was added in API level 16.
        // (The normal contentView is shown when the notification is collapsed, when expanded the
        // big content view set here is displayed.)
        if (Build.VERSION.SDK_INT >= 16) {
            // Inflate and set the layout for the expanded notification view
            RemoteViews expandedView =
                    new RemoteViews(getPackageName(), R.layout.notification_expanded);
            expandedView.setTextViewText(R.id.textViewEx, text);
            if (cor.equals("verde")) {
                expandedView.setImageViewResource(R.id.imageViewEx, R.drawable.verde);
            } else if (cor.equals("vemelho")) {
                expandedView.setImageViewResource(R.id.imageViewEx, R.drawable.vermelho);
            } else {
                expandedView.setImageViewResource(R.id.imageViewEx, R.drawable.amarelo);
            }
            notification.bigContentView = expandedView;
        }
        // END_INCLUDE(customLayout)
        // START_INCLUDE(notify)
        // Use the NotificationManager to show the notification
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0, notification);
        // END_INCLUDE(notify)
    }

    private void sendMeasurement(Cell ce) {
        save = ce;
        final ConnectivityManager connMgr = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifi.isConnectedOrConnecting()) {
            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString();
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String[] resp = db.getNames("724", save.getMnc());

            SendData sd = new SendData(this);
            sd.execute("set", "table_cell", "" + save.getCell(), "" + resp[1], "" + c.getLat(), "" + c.getLon());
            sd = new SendData(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            sd.execute("set", "table_measurements", "" + save.getCell(), "" + telephonyManager.getDeviceId(), "" + save.getSamples(),
                    "" + save.getLat(), "" + save.getLon(), ts);
            sd = new SendData(this);
            sd.execute("set", "cellphones", "" + telephonyManager.getDeviceId(),
                    android.os.Build.VERSION.RELEASE + "-" + android.os.Build.VERSION.SDK_INT,
                    android.os.Build.MODEL + "-" + android.os.Build.PRODUCT);
        }

    }



}
