package com.researchfip.puc.mytalks.UI.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.researchfip.puc.mytalks.R;
import com.researchfip.puc.mytalks.database.DBPersistence;
import com.researchfip.puc.mytalks.database.PhoneData;
import com.researchfip.puc.mytalks.general.PhoneInformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joaocastro on 06/12/17.
 */

@SuppressLint("ValidFragment")
public class CallsAndSMSFragment extends Fragment {

    private DBPersistence persistence;
    int typeService;
    private Cursor cursor;


    //Elements
    TextView tvTypeEventEnviados;
    TextView tvTypeEventEnviadosValue;
    TextView tvTypeEventRecebidos;
    TextView tvTypeEventRecebidosValue;
    TextView tvTypeEventListName;
    ListView rvTypeEventList;
    int received = 0;
    int send = 0;

    private View view;

    MapView mMapView;
    private GoogleMap googleMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.callsandsms_fragment, container, false);
        Bundle bundle = this.getArguments();
        this.typeService = bundle.getInt("TYPE_EVENT");

        tvTypeEventEnviados = (TextView) view.findViewById(R.id.tv_typeEvent_enviados);
        tvTypeEventEnviadosValue = (TextView) view.findViewById(R.id.tv_typeEvent_enviados_valor);
        tvTypeEventRecebidos = (TextView) view.findViewById(R.id.tv_typeEvent_recebidos);
        tvTypeEventRecebidosValue = (TextView) view.findViewById(R.id.tv_typeEvent_recebidos_valor);
        tvTypeEventListName = (TextView) view.findViewById(R.id.tv_typeevent_listname);
        rvTypeEventList = (ListView) view.findViewById(R.id.lv_typevent_list);
        rvTypeEventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
             //   Log.d("Clicou:  ",persistence.getTableAsString());
                final Dialog dialog = new Dialog(getActivity());
                final Cursor c = (Cursor)(rvTypeEventList.getItemAtPosition(position));
                String num = c.getColumnName(8)+ "  "+ c.getString(8) +c.getColumnName(9)+"   "+ c.getString(9);
                Log.d("Clicou:  ",num);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                /////make map clear
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

                dialog.setContentView(R.layout.dialogmap);////your custom content

                MapView mMapView = (MapView) dialog.findViewById(R.id.mapView);
                MapsInitializer.initialize(getActivity());

                mMapView.onCreate(dialog.onSaveInstanceState());
                mMapView.onResume();


                mMapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(final GoogleMap googleMap) {
                        LatLng posisiabsen1 = new LatLng( Double.parseDouble(c.getString(8)), Double.parseDouble(c.getString(9))); ////your lat lng
                      //  LatLng posisiabsen2 = new LatLng(-19.927503, -43.948980); ////your lat lng
                        googleMap.addMarker(new MarkerOptions().position(posisiabsen1).title("Yout title")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(posisiabsen1));
                        googleMap.getUiSettings().setZoomControlsEnabled(true);
                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                     /*   googleMap.addMarker(new MarkerOptions().position(posisiabsen2).title("Yout title")
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                        List<LatLng> path = new ArrayList();


                        //Execute Directions API request
                        GeoApiContext context = new GeoApiContext.Builder()
                                .apiKey("AIzaSyDmv0DLyHsiEhMbFqQMAKs_TIOn-sXNNpc")
                                .build();
                        DirectionsApiRequest req = DirectionsApi.getDirections(context, "-19.921603, -43.939367", "-19.927503, -43.948980");
                        try {
                            DirectionsResult res = req.await();

                            //Loop through legs and steps to get encoded polylines of each step
                            if (res.routes != null && res.routes.length > 0) {
                                DirectionsRoute route = res.routes[0];

                                if (route.legs !=null) {
                                    for(int i=0; i<route.legs.length; i++) {
                                        DirectionsLeg leg = route.legs[i];
                                        if (leg.steps != null) {
                                            for (int j=0; j<leg.steps.length;j++){
                                                DirectionsStep step = leg.steps[j];
                                                if (step.steps != null && step.steps.length >0) {
                                                    for (int k=0; k<step.steps.length;k++){
                                                        DirectionsStep step1 = step.steps[k];
                                                        EncodedPolyline points1 = step1.polyline;
                                                        if (points1 != null) {
                                                            //Decode polyline and add points to list of route coordinates
                                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    EncodedPolyline points = step.polyline;
                                                    if (points != null) {
                                                        //Decode polyline and add points to list of route coordinates
                                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                                        for (com.google.maps.model.LatLng coord : coords) {
                                                            path.add(new LatLng(coord.lat, coord.lng));
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } catch(Exception ex) {
                            Log.e("Erro", ex.getLocalizedMessage());
                        }

                        //Draw the polyline
                        if (path.size() > 0) {
                            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
                            googleMap.addPolyline(opts);
                        }*/

                    }
                });


                Button dialogButton = (Button) dialog.findViewById(R.id.button2);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });
        if(typeService == PhoneInformation.SMS_SERVICE_ID){
            tvTypeEventEnviados.setText(getString(R.string.tv_sms_enviados));
            tvTypeEventRecebidos.setText(getString(R.string.tv_sms_recebidos));
            tvTypeEventListName.setText(getString(R.string.tv_sms_listname));
            tvTypeEventEnviadosValue.setText(send+"");
            tvTypeEventRecebidosValue.setText(received +"");
        } else {
            tvTypeEventEnviados.setText(getString(R.string.tv_call_enviados));
            tvTypeEventRecebidos.setText(getString(R.string.tv_call_recebidos));
            tvTypeEventListName.setText(getString(R.string.tv_call_listname));
            tvTypeEventEnviadosValue.setText(send+"");
            tvTypeEventRecebidosValue.setText(received +"");
        }

        try{
            persistence = new DBPersistence(getActivity());
            new LoadDataToRecycler().execute();
        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }

    public void getReceivedAndSend(Cursor cursor){
        int[] receivedAndSend = new int[2];
        PhoneData[] phoneData = new PhoneData[cursor.getCount()];
        for(int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            phoneData[i] = persistence.cursorToPhoneData(cursor);
            if((phoneData[i].getOriginName().equals("Eu")) ||
               (phoneData[i].getOriginName().equals("Myself"))){
                receivedAndSend[1]++;
            }else{
                receivedAndSend[0]++;
            }
        }
      //  Log.d("Table String",persistence.getTableAsString());
        received = receivedAndSend[0];
        send = receivedAndSend[1];
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(typeService == PhoneInformation.SMS_SERVICE_ID){
            getActivity().setTitle("Mensagens");

        }else{
            getActivity().setTitle("Ligações");

        }
    }

    public class LoadDataToRecycler extends AsyncTask<String, Void, String> {

        private SimpleCursorAdapter adapter;


        @Override
        protected String doInBackground(String... strings) {
            cursor = persistence.getPhoneDataToViews(typeService);
            if(cursor != null){
                String[] columns = null;
                int[] to = null;

                if(typeService == PhoneInformation.SMS_SERVICE_ID){
                    columns = new String[]{
                            DBPersistence.getColOriginName(),
                           // DBPersistence.getColOriginNumber(),
                            DBPersistence.getColTargetName(),
                          //  DBPersistence.getColTargetNumber(),
                            DBPersistence.getColItime(),
                            DBPersistence.getColAddress()};

                    to = new int[] {R.id.tv_sms_item_origin,
                            R.id.tv_sms_item_target,
                            R.id.tv_sms_item_sms,
                            R.id.tv_sms_item_address};
                    Log.d("SMS Origin",columns[0]);
                    adapter = new SimpleCursorAdapter(getActivity(),R.layout.sms_item,cursor,columns,to,0);
                    getReceivedAndSend(cursor);
                    } else if (typeService == PhoneInformation.CALL_SERVICE_ID){

                    columns = new String[]{
                            DBPersistence.getColOriginName(),
                            //DBPersistence.getColOriginNumber(),
                            DBPersistence.getColTargetName(),
                            //DBPersistence.getColTargetNumber(),
                            DBPersistence.getColItime(),
                            DBPersistence.getColFtime(),
                            DBPersistence.getColAddress()};
                    to = new int[] {
                            R.id.tv_call_item_origin,
                            R.id.tv_call_item_target,
                            R.id.tv_call_item_itime,
                            R.id.tv_call_item_ftime,
                            R.id.tv_call_item_address};
                    Log.d("Calls Origin",columns[0]);
                    adapter = new SimpleCursorAdapter(getActivity(), R.layout.call_item, cursor, columns, to, 0);
                    getReceivedAndSend(cursor);
                }


            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            ListView listView = null;
            listView = (ListView) view.findViewById(R.id.lv_typevent_list);
            tvTypeEventEnviadosValue.setText(send+"");
            tvTypeEventRecebidosValue.setText(received +"");
            if(listView != null && adapter != null){
                listView.setAdapter(adapter);
            }
        }

    }

    @Override
    public void onDestroy() {
        if(cursor != null)
        cursor.close();
        if(persistence != null)
        persistence.closeDB();
        super.onDestroy();
    }
}


