package com.researchfip.puc.mytalks.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Calendar;


public class DataBaseController extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Cells";
    private static final String TABLE_CELL = "cell";
    private static final String TABLE_CELLR = "cellreal";
    private static final String TABLE_CELLH = "cellhour";
    private static final String TABLE_CELLW = "cellweek";
    private static final String TABLE_OPERA = "operadoras";
    private static final String TABLE_PER = "personal";
    private static final String KEY_CELL = "cellId";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LON = "lon";
    private static final String KEY_SAMPLES = "samples";
    private static final String KEY_DAY = "day";
    private static final String KEY_CREATEDD = "createdD";
    private static final String KEY_CREATEDH = "createdH";
    private static final String KEY_TYPE = "type";
    private static final String KEY_DWEEK = "dayWeek";
    private static final String KEY_MCC = "mcc";
    private static final String KEY_MNC = "mnc";
    private static final String KEY_SIG = "sig";
    private static final String KEY_PAIS = "pais";
    private static final String KEY_CC = "cc";
    private static final String KEY_OPERA = "nome";
    private static final String KEY_SIZE = "size";
    private static final String KEY_NUMBER = "number";
    private static final String KEY_LAC = "lacs";
    private static final String CREATE_cell_TABLE = "CREATE TABLE cell ( cellId INTEGER," +
            "lat REAL, lon REAL, samples REAL, day INTEGER, type TEXT)";
    private static final String CREATE_cellreal_TABLE = "CREATE TABLE cellreal ( cellId INTEGER," +
            "lat REAL, lon REAL, samples REAL, createdD INTEGER, createdH INTEGER, dayWeek INTEGER, type TEXTR)";
    private static final String CREATE_cellhour_TABLE = "CREATE TABLE cellhour ( cellId INTEGER, day INTEGER, samples TEXT, type TEXT)";
    private static final String CREATE_cellweek_TABLE = "CREATE TABLE cellweek (dayWeek INTEGER, samples TEXT)";
    private static final String CREATE_operadoras_TABLE = "CREATE TABLE operadoras (mcc TEXT, mnc TEXT, sig TEXT, pais TEXT, cc TEXT, nome TEXT)";
    private static final String CREATE_personal_TABLE = "CREATE TABLE personal (day TEXT, size TEXT, type TEXT, number TEXT)";
    private static final String CREATE_aux_TABLE = "CREATE TABLE aux (size TEXT)";
    private Context c;
    private static final String CREATE_AppUse_TABLE = "CREATE TABLE appuse (uid TEXT, total INTEGER, last INTEGER, time TEXT)";
    private static final String CREATE_Apptimes_TABLE = "CREATE TABLE app (uid TEXT, name TEXT, send INTEGER, recieve INTEGER, time_start TEXT, type INTEGER)";


    public DataBaseController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        c = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_cell_TABLE);
        db.execSQL(CREATE_cellreal_TABLE);
        db.execSQL(CREATE_cellhour_TABLE);
        db.execSQL(CREATE_cellweek_TABLE);
        db.execSQL(CREATE_operadoras_TABLE);
        db.execSQL(CREATE_personal_TABLE);
        db.execSQL(CREATE_aux_TABLE);
        db.execSQL(CREATE_AppUse_TABLE);
        db.execSQL(CREATE_Apptimes_TABLE);
        String aux = "SELECT "+KEY_CC+" FROM "+TABLE_OPERA;
        Cursor auxc = db.rawQuery(aux,null);
        if(!auxc.moveToFirst()){
            fillCodes(db);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS cell");
        db.execSQL("DROP TABLE IF EXISTS cellreral");
        db.execSQL("DROP TABLE IF EXISTS cellhour");
        db.execSQL("DROP TABLE IF EXISTS cellweek");
        db.execSQL("DROP TABLE IF EXISTS personal");
        db.execSQL("DROP TABLE IF EXISTS appuse");
        db.execSQL("DROP TABLE IF EXISTS app");
        this.onCreate(db);
    }

    public void updateCell() {
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Calendar c = Calendar.getInstance();
        String samples = "SELECT " + KEY_CELL + ", AVG(" + KEY_SAMPLES + "), AVG(" + KEY_LAT + "), AVG(" + KEY_LON + "), type FROM " + TABLE_CELLR + " WHERE " + KEY_LON + " != 0 and " + KEY_LAT + " !=0  GROUP BY "+ KEY_CELL+" ORDER BY "+KEY_CELL;
        Cursor samReal = db.rawQuery(samples,null);
        samReal.moveToFirst();
        for(int i = 0; i < samReal.getCount(); i++) {
            long mediaLat = 0;
            long mediaLon = 0 ;
            long mediasamp = 0;
            String cell = "SELECT "+KEY_CELL+" , "+KEY_SAMPLES+" , "+KEY_LAT+" , "+KEY_LON+" FROM "+TABLE_CELL+" WHERE "+KEY_CELL+" = "+samReal.getString(0);
            Cursor retorCell = db.rawQuery(cell, null);
            if(retorCell.moveToFirst()){
                ContentValues values = new ContentValues();
                values.put(KEY_CELL, samReal.getInt(0));
                values.put(KEY_LAT, (retorCell.getFloat(2)+samReal.getFloat(2))/2);
                values.put(KEY_LON, (retorCell.getFloat(3)+samReal.getFloat(3))/2);
                values.put(KEY_SAMPLES, (retorCell.getFloat(1) + samReal.getFloat(1))/2);
                values.put(KEY_DAY,c.get(Calendar.DATE));
                values.put(KEY_TYPE,samReal.getString(4));
                db.update(TABLE_CELL,values, KEY_CELL + " = ?",
                        new String[] { String.valueOf(samReal.getString(0)) });
                System.out.println("Primeirso: "+(mediaLat/2)+" e "+(mediaLon/2));
            }else{
                // 2. create ContentValues to add key "column"/value
                ContentValues values = new ContentValues();
                values.put(KEY_CELL, samReal.getString(0));
                values.put(KEY_LAT, samReal.getFloat(2));
                values.put(KEY_LON, samReal.getFloat(3));
                values.put(KEY_SAMPLES, samReal.getFloat(1));
                values.put(KEY_DAY, c.get(Calendar.DATE));
                values.put(KEY_TYPE,samReal.getString(4));
                // 3. insert
                db.insert(TABLE_CELL, // table
                        null, //nullColumnHack
                        values); // key/value -> keys = column names/ values = column values
                System.out.println("Segundo: "+samReal.getFloat(2)+" e "+samReal.getFloat(3));
            }

        //    latReal.moveToNext();
        //    lonReal.moveToNext();
            samReal.moveToNext();
        }
        Cursor retor = db.query(TABLE_CELL, new String[]{KEY_CELL, KEY_DAY}, null, null, null, null, null, null);
        retor.moveToFirst();
        for(int i = 0; i < retor.getCount(); i++){
            int d = c.get(Calendar.DATE);
            int aux = retor.getInt(1);
            if((aux+7)%31 < d) {
                db.delete(TABLE_CELL, KEY_CELL + " = ?",
                        new String[]{String.valueOf(retor.getInt(0))});
            }
            retor.moveToNext();
        }

        // 4. close
      //  db.close();
    }

    public void updateCellWeek() {
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Calendar c = Calendar.getInstance();
        String samples = "SELECT AVG("+KEY_SAMPLES+") FROM "+TABLE_CELLR;
        Cursor samReal = db.rawQuery(samples,null);
        samReal.moveToFirst();

        long mediasamp = 0;
        int aux1 = 0;
        if(c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            aux1 = 7;
        }else{
            aux1 = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        Cursor retorCell = db.query(TABLE_CELLW, new String[]{KEY_SAMPLES}, KEY_DWEEK + "=?", new String[]{""+aux1}, null, null, null, null);
        if(retorCell.moveToFirst()){
            mediasamp += retorCell.getFloat(0);
            mediasamp += samReal.getFloat(0);
            ContentValues values = new ContentValues();
            values.put(KEY_SAMPLES, mediasamp/2);
            values.put(KEY_DWEEK,c.get(Calendar.DAY_OF_WEEK));
            db.update(TABLE_CELLW,values, KEY_DWEEK + " = ?",
                    new String[] { ""+aux1});
        }else{
            mediasamp += samReal.getFloat(0);
            // 2. create ContentValues to add key "column"/value
            ContentValues values = new ContentValues();
            values.put(KEY_SAMPLES, mediasamp);
            values.put(KEY_DWEEK,c.get(Calendar.DAY_OF_WEEK));
            // 3. insert
            db.insert(TABLE_CELLW, // table
                    null, //nullColumnHack
                    values); // key/value -> keys = column names/ values = column values
        }

        // 4. close
       // db.close();
    }

    public void addCellR(Cell cell) {
        // 1. get reference to writable DB

        SQLiteDatabase db = this.getWritableDatabase();
        Calendar c = Calendar.getInstance();
        Cursor retor = db.query(TABLE_CELLR, new String[]{KEY_CELL, KEY_CREATEDD}, KEY_CREATEDD+"=?", new String[]{"" + c.get(Calendar.DATE)}, null, null, null, null);
        if(!retor.moveToFirst()){
            updateCell();
            updateCellWeek();
           //  synchronized(db) {
                db.execSQL("DROP TABLE IF EXISTS cellreal");
                String CREATE_cellreal_TABLE = "CREATE TABLE cellreal ( cellId INTEGER, " +
                        "lat REAL, lon REAL," +
                        "samples REAL, createdD INTEGER, createdH INTEGER, dayWeek INTEGER, type TEXT)";
                db.execSQL(CREATE_cellreal_TABLE);
            //}

        }
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_CELL, cell.getCell());
        values.put(KEY_LAT, cell.getLat());
        values.put(KEY_LON, cell.getLon());
        values.put(KEY_SAMPLES, cell.getSamples());
        values.put(KEY_DWEEK,c.get(Calendar.DAY_OF_WEEK));
        values.put(KEY_CREATEDD,c.get(Calendar.DATE));
        values.put(KEY_CREATEDH,c.get(Calendar.HOUR));
        values.put(KEY_TYPE,cell.getType());

        // 3. insert
        db.insert(TABLE_CELLR, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public void addPersonalData(Cell cell) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor retor = db.query(TABLE_PER, new String[]{KEY_DAY}, null, null, null, null, null, null);
        if(!retor.moveToFirst()){
            ContentValues values = new ContentValues();
            values.put(KEY_DAY, cell.getDay());
            values.put(KEY_TYPE,cell.getType());
            values.put(KEY_SIZE,cell.getData());
            values.put(KEY_NUMBER,cell.getNumber());

            // 3. insert
            db.insert(TABLE_PER, // table
                    null, //nullColumnHack
                    values); // key/value -> keys = column names/ values = column values
        }else{
            ContentValues values = new ContentValues();
            db.execSQL("DROP TABLE IF EXISTS personal");
            db.execSQL(CREATE_personal_TABLE);
            values.put(KEY_DAY, cell.getDay());
            values.put(KEY_TYPE,cell.getType());
            values.put(KEY_SIZE,cell.getData());
            values.put(KEY_NUMBER,cell.getNumber());
            // 3. insert
            db.insert(TABLE_PER, // table
                    null, //nullColumnHack
                    values); // key/value -> keys = column names/ values = column values
        }


        // 4. close
        db.close();
    }

    public String[] getPersonalData(){
        String samples = "SELECT "+KEY_DAY+", "+KEY_SIZE+", type, number FROM "+TABLE_PER;
        String[] resp = {""};
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.rawQuery(samples, null); // h. limit

        // 3. if we got results get the first one
        if (cursor.moveToFirst()) {
            resp = new String[4];
            resp[0] = cursor.getString(0);
            resp[1] = cursor.getString(1);
            resp[2] = cursor.getString(2);
            resp[3] = cursor.getString(3);
        }
        return resp;
    }

    public void addAuxData(Cell cell) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor retor = db.query("aux", new String[]{KEY_SIZE}, null, null, null, null, null, null);
        if(!retor.moveToFirst()){
            ContentValues values = new ContentValues();
            values.put(KEY_SIZE,cell.getData());

            // 3. insert
            db.insert("aux", // table
                    null, //nullColumnHack
                    values); // key/value -> keys = column names/ values = column values
        }else{
            ContentValues values = new ContentValues();
            db.execSQL("DROP TABLE IF EXISTS aux");
            db.execSQL(CREATE_aux_TABLE);
            values.put(KEY_SIZE,cell.getData());
            // 3. insert
            db.insert("aux", // table
                    null, //nullColumnHack
                    values); // key/value -> keys = column names/ values = column values
        }


        // 4. close
        db.close();
    }

    public String[] getAuxData(){
        String samples = "SELECT "+KEY_SIZE+" FROM "+"aux";
        String[] resp = {""};
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.rawQuery(samples, null); // h. limit

        // 3. if we got results get the first one
        if (cursor.moveToFirst()) {
            resp[0] = cursor.getString(0);
        }
        return resp;
    }

   public String[] [] getMedicoesDia(){
        String samples = "SELECT "+KEY_CREATEDH+", AVG("+KEY_SAMPLES+"), type FROM "+TABLE_CELLR+" GROUP BY "+ KEY_CREATEDH+" ORDER BY "+KEY_CREATEDH;
        String[] [] resp = {{""}};
       // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.rawQuery(samples, null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null) {
            cursor.moveToFirst();
            resp = new String[cursor.getCount()][2];
            for (int i = 0; i < cursor.getCount(); i++) {
                resp[i][0] = cursor.getString(0);
                resp[i][1] = cursor.getString(1);
                cursor.moveToNext();
            }
        }
       return resp;
    }

    public String[] [] getAntenas(){
        String samples = "SELECT "+KEY_CELL+", "+KEY_SAMPLES+", "+KEY_LAT+", "+KEY_LON+", type FROM "+TABLE_CELL;
        String[] [] resp = {{""}};
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor = db.rawQuery(samples, null); // h. limit

        // 3. if we got results get the first one
        if (cursor.moveToFirst()) {
            resp = new String[cursor.getCount()][5];
            for (int i = 0; i < cursor.getCount(); i++) {
                resp[i][0] = cursor.getString(0);
                resp[i][1] = cursor.getString(1);
                resp[i][2] = cursor.getString(2);
                resp[i][3] = cursor.getString(3);
                resp[i][4] = cursor.getString(4);
                cursor.moveToNext();
            }
        }else {
            samples = "SELECT " + KEY_CELL + ", AVG(" + KEY_SAMPLES + "),  AVG(" + KEY_LAT + "),  AVG(" + KEY_LON + "), type FROM " + TABLE_CELLR + " GROUP BY " + KEY_CELL;
            cursor = db.rawQuery(samples, null);
            cursor.moveToFirst();
            resp = new String[cursor.getCount()][5];
             for (int i = 0; i < cursor.getCount(); i++) {
                resp[i][0] = cursor.getString(0);
                resp[i][1] = cursor.getString(1);
                resp[i][2] = cursor.getString(2);
                resp[i][3] = cursor.getString(3);
                resp[i][4] = cursor.getString(4);
                cursor.moveToNext();
            }
        }
        return resp;
    }

    public void fillCodes(SQLiteDatabase db){
        FillTables ft = new FillTables();
        String[] [] cont = ft.getContCodes();
        for (String[] aCont : cont) {
            ContentValues values = new ContentValues();
            values.put(KEY_MCC, aCont[0]);
            values.put(KEY_MNC, aCont[1]);
            values.put(KEY_SIG, aCont[2]);
            values.put(KEY_PAIS, aCont[3]);
            values.put(KEY_CC, aCont[4]);
            values.put(KEY_OPERA, aCont[5]);
            db.insert(TABLE_OPERA, null, values);
        }
       // db.close();

    }

    public String[] getNames(String mcc, String mnc){
        String names = "SELECT "+KEY_PAIS+", "+KEY_OPERA+" FROM "+TABLE_OPERA+" WHERE "+KEY_MCC+" = "+mcc+" and "+KEY_MNC + " = "+mnc;
        String[] resp = {mcc,mnc};
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor = db.rawQuery(names, null); // h. limit
        // 3. if we got results get the first one
        if (cursor.moveToFirst()) {
            resp[0] = cursor.getString(0);
            resp[1] = cursor.getString(1);
        }
        return resp;
    }

    public void addAppData(long uid, long send, long recieve, String time) {
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("uid", uid);
        values.put("total", send + recieve);
        values.put("last", 0);
        values.put("time", time);
        // 3. insert
        db.insert("appuse", // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public void addAllAppData(long [][] data) {
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("appuse", null, null);
        // 2. create ContentValues to add key "column"/value
      //  Cursor retor = db.rawQuery("SELECT * FROM appuse", null);
        for (int i = 0; i < data.length; i++) {
            //if(retor.moveToFirst()) {
            boolean aux = true;
            for(int j = i+1; j <data.length && aux;j++){
                if(data[i][0] == data[j][0]) {
                    aux = false;
                }
            }
            if(aux){
                Log.v("DB.getAppData2:","COlocou no banco");
                ContentValues values = new ContentValues();
                values.put("uid", data[i][0]);
                values.put("total", data[i][1] + data[i][2]);
                values.put("last", 0);
                values.put("time", data[i][3]);
                // 3. insert
                db.insert("appuse", // table
                        null, //nullColumnHack
                        values); // key/value -> keys = column names/ values = column values
            }
        }
        // 4. close
        db.close();
    }

    public long [][] getAppData(long [][] apps) {
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor retor = db.rawQuery("SELECT * FROM appuse", null);
        retor.moveToFirst();
        while (!retor.isAfterLast()) {
            int uid = Integer.parseInt(retor.getString(0));
            int total = Integer.parseInt(retor.getString(1));
            long last = Long.parseLong(retor.getString(2));
            long time = Long.parseLong(retor.getString(3));
            long m = 259200*10000;
            if (System.currentTimeMillis()- time < m){
                int i = 0;
                while (apps[i][0] != uid){
                    i++;
                }
                long l = apps[i][1] - total;
                if(l < last){
                    last = last + l;
                }else{
                    last = l;
                }
                apps[i][1] = last;
                ContentValues cv = new ContentValues();
                cv.put("last",last); //These Fields should be your String values of actual column names
                db.update("appuse", cv, "uid="+uid, null);
            } else{
                ContentValues cv = new ContentValues();
                cv.put("last",last);
                db.update("appuse", cv, "uid="+uid, null);
            }
            retor.moveToNext();
        }
        db.close();
        return apps;
    }

    public long [][] getAppData2(long [][] apps) {
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor retor = db.rawQuery("SELECT time FROM appuse LIMIT 1", null);
        if(retor.moveToFirst()){
            Log.v("DB.getAppData2:", "Running apps");
        }
        long t = Long.parseLong(retor.getString(0));
        for(int i = 0; i < apps.length; i++){
            boolean aux = true;
            for(int j = i+1; j < apps.length && aux;j++){
                if(apps[i][0] == apps[j][0]) {
                    aux = false;
                }
            }
            db = this.getWritableDatabase();
            retor = db.rawQuery("SELECT * FROM appuse WHERE uid ="+ apps[i][0], null);
            if(retor.moveToFirst()) {
                int uid = Integer.parseInt(retor.getString(0));
                int total = Integer.parseInt(retor.getString(1));
                long last = Long.parseLong(retor.getString(2));
                long time = Long.parseLong(retor.getString(3));
                long m = 259200000;
                if ((System.currentTimeMillis() - time)/10< m) {
                    long l = apps[i][1] - total;
                    if (l < last) {
                        if (l < 0) last = last - l;
                        else last = last + l;
                    } else {
                        last = l;
                    }
                    apps[i][1] = last;
                    ContentValues cv = new ContentValues();
                    cv.put("last", last); //These Fields should be your String values of actual column names
                    db.update("appuse", cv, "uid=" + uid, null);
                } else {
                    ContentValues cv = new ContentValues();
                    cv.put("last", last);
                    db.update("appuse", cv, "uid=" + uid, null);
                }
            }else{
                addAppData(apps[i][0], apps[i][1], 0, ""+t);
            }
        }
        db.close();
        return apps;
    }
}