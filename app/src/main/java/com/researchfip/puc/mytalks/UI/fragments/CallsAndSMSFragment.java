package com.researchfip.puc.mytalks.UI.fragments;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.researchfip.puc.mytalks.R;
import com.researchfip.puc.mytalks.database.DBPersistence;
import com.researchfip.puc.mytalks.database.PhoneData;
import com.researchfip.puc.mytalks.general.PhoneInformation;

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
        Log.d("Table String",persistence.getTableAsString());
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
                            DBPersistence.getColOriginNumber(),
                            DBPersistence.getColTargetName(),
                            DBPersistence.getColTargetNumber(),
                            DBPersistence.getColItime(),
                            DBPersistence.getColAddress()};

                    to = new int[] {R.id.tv_sms_item_origin,
                            R.id.tv_sms_item_target,
                            R.id.tv_sms_item_sms,
                            R.id.tv_sms_item_address};
                    adapter = new SimpleCursorAdapter(getActivity(),R.layout.sms_item,cursor,columns,to,0);
                    getReceivedAndSend(cursor);
                    } else if (typeService == PhoneInformation.CALL_SERVICE_ID){

                    columns = new String[]{
                            DBPersistence.getColOriginName(),
                            DBPersistence.getColOriginNumber(),
                            DBPersistence.getColTargetName(),
                            DBPersistence.getColTargetNumber(),
                            DBPersistence.getColItime(),
                            DBPersistence.getColFtime(),
                            DBPersistence.getColAddress()};
                    to = new int[] {
                            R.id.tv_call_item_origin,
                            R.id.tv_call_item_target,
                            R.id.tv_call_item_itime,
                            R.id.tv_call_item_ftime,
                            R.id.tv_call_item_address};
                }
                adapter = new SimpleCursorAdapter(getActivity(), R.layout.call_item, cursor, columns, to, 0);
                    getReceivedAndSend(cursor);
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


