package com.researchfip.puc.mytalks.database;

/**
 * Created by Mateus on 04/11/2016.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by ProgrammingKnowledge on 1/5/2016.
 */
public class SendData extends AsyncTask<String,Void,String> {
    Context context;
    AlertDialog alertDialog;
    public SendData(Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String login_url="";
        if(type.equals("get")) {
            try {
                login_url = "http://leasdle01.icei.pucminas.br/getInf2.php?";
                String table = params[1];
                String id = params[2];
                String id_mark = params[3];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("id","UTF-8")+"="+ URLEncoder.encode(id,"UTF-8")+"&"
                        + URLEncoder.encode("idmark","UTF-8")+"="+ URLEncoder.encode(id_mark,"UTF-8")+"&"
                        + URLEncoder.encode("tabela","UTF-8")+"="+ URLEncoder.encode(table,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type.equals("set")) {
            try {
                login_url = "http://leasdle01.icei.pucminas.br/setInf2.php?";
                String tabela = params[1];
                String id = params[2];
                String value = params[3];
                String value2 = params[4];
                String value3 = "";
                String value4 = "";
                String value5 = "";
                if(params.length>5) {
                    value3 = params[5];
                    if(params.length>6) {
                        value4 = params[6];
                        value5 = params[7];
                    }
                }
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = "";
                if(tabela.equals("cellphone")){
                   post_data = URLEncoder.encode("id","UTF-8")+"="+ URLEncoder.encode(id,"UTF-8")+"&"
                           + URLEncoder.encode("tabela","UTF-8")+"="+ URLEncoder.encode(tabela,"UTF-8")+"&"
                           + URLEncoder.encode("value1","UTF-8")+"="+ URLEncoder.encode(value,"UTF-8")+"&"
                           + URLEncoder.encode("value2","UTF-8")+"="+ URLEncoder.encode(value2,"UTF-8");
                }else if (tabela.equals("table_cell")){
                    post_data = URLEncoder.encode("id","UTF-8")+"="+ URLEncoder.encode(id,"UTF-8")+"&"
                            + URLEncoder.encode("tabela","UTF-8")+"="+ URLEncoder.encode(tabela,"UTF-8")+"&"
                            + URLEncoder.encode("value1","UTF-8")+"="+ URLEncoder.encode(value,"UTF-8")+"&"
                            + URLEncoder.encode("value2","UTF-8")+"="+ URLEncoder.encode(value2,"UTF-8")+"&"
                            + URLEncoder.encode("value3","UTF-8")+"="+ URLEncoder.encode(value3,"UTF-8");
                }else {
                    post_data = URLEncoder.encode("id","UTF-8")+"="+ URLEncoder.encode(id,"UTF-8")+"&"
                            + URLEncoder.encode("tabela","UTF-8")+"="+ URLEncoder.encode(tabela,"UTF-8")+"&"
                            + URLEncoder.encode("value1","UTF-8")+"="+ URLEncoder.encode(value,"UTF-8")+"&"
                            + URLEncoder.encode("value2","UTF-8")+"="+ URLEncoder.encode(value2,"UTF-8")+"&"
                            + URLEncoder.encode("value3","UTF-8")+"="+ URLEncoder.encode(value3,"UTF-8")+"&"
                            + URLEncoder.encode("value4","UTF-8")+"="+ URLEncoder.encode(value4,"UTF-8")+"&"
                            + URLEncoder.encode("value5","UTF-8")+"="+ URLEncoder.encode(value5,"UTF-8");
                    System.out.println(post_data);
                }

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                System.out.println("Salvo:   "+result);
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        //alertDialog = new AlertDialog.Builder(context).create();
     //   alertDialog.setTitle("Login Status");
    }

    @Override
    protected void onPostExecute(String result) {
        System.out.print("asdasd         "+result);

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}