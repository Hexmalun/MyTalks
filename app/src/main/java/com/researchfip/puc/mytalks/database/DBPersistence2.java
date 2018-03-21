package com.researchfip.puc.mytalks.database;

/**
 * Created by Mateus on 20/03/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by joaocastro on 06/12/17.
 */

public class DBPersistence2 extends SQLiteOpenHelper {

    private static final int DB_VERSION = 5;
    private static final String DB_NAME = "MyTalksv6.db";
    private static final String TABLE_PHODAT = "phoneData2";
    private static final String TABLE_CALLS = "calls";
    private static final String TABLE_SMS = "sms";
    //Commom Column Names
    //Warning! dont change this names.
    private static final String COL_ID = "_id";
    private static final int NUM_COL_ID = 0;
    private static final String COL_IMEI = "imei";
    private static final int NUM_COL_IMEI = 1;

    //Columns Names: SMS and Calls
    private static final String COL_VISIBLE = "visible";
    private static final String COL_SYNCHRONIZED = "synchronized";
    private static final String COL_ORIGIN_NUMBER = "origin_number";
    private static final String COL_ORIGIN_NAME = "origin_name";
    private static final String COL_TARGET_NUMBER = "target_number";
    private static final String COL_TARGET_NAME = "target_name";
    private static final String COL_LATITUDE_S = "latitude_start";
    private static final String COL_LONGITUDE_S = "longitude_start";
    private static final String COL_LATITUDE_E = "latitude_end";
    private static final String COL_LONGITUDE_E = "longitude_end";
    private static final String COL_ADDRESS_S = "address_start";
    private static final String COL_ITIME = "itime";
    private static final String COL_FTIME = "ftime";
    private static final String COL_TYPE_EVENT = "type_event";
    private static final String COL_TYPE_SERVICE = "type_service";
    private static final String COL_SHARE_POSITION = "share_position";


    //columns Order: SMS and Calls
    private static final int NUM_COL_VISIBLE = 2;
    private static final int NUM_COL_SYNCHRONIZED = 3;
    private static final int NUM_COL_ORIGIN_NUMBER = 4;
    private static final int NUM_COL_ORIGIN_NAME = 5;
    private static final int NUM_COL_TARGET_NUMBER = 6;
    private static final int NUM_COL_TARGET_NAME = 7;
    private static final int NUM_COL_LATITUDE_S = 8;
    private static final int NUM_COL_LONGITUDE_S = 9;
    private static final int NUM_COL_ADDRESS_S = 10;
    private static final int NUM_COL_LATITUDE_E = 11;
    private static final int NUM_COL_LONGITUDE_E = 12;
    private static final int NUM_COL_ITIME = 13;
    private static final int NUM_COL_FTIME = 14;
    private static final int NUM_COL_TYPE_EVENT = 15;
    private static final int NUM_COL_TYPE_SERVICE = 16;
    private static final int NUM_COL_SHARE_POSITION = 17;

    //GatewayInfo Table - Column Names
    private static final String COL_NAME = "name";
    private static final String COL_NUMBER = "number";
    //Column Number
    private static final int NUM_COL_NAME = 1;
    private static final int NUM_COL_NUMBER = 2;

    private static final String CREATE_TABLE_PHONE_DATA = "CREATE TABLE "
            + TABLE_PHODAT + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_IMEI + " TEXT NOT NULL, " +
            COL_VISIBLE + " INTEGER NOT NULL, " +
            COL_SYNCHRONIZED + " INTEGER NOT NULL, " +
            COL_ORIGIN_NUMBER + " TEXT NOT NULL, " + COL_ORIGIN_NAME + " TEXT NOT NULL, " +
            COL_TARGET_NUMBER + " TEXT NOT NULL, " + COL_TARGET_NAME + " TEXT NOT NULL, " +
            COL_LATITUDE_S + " REAL NOT NULL, " + COL_LONGITUDE_S + " REAL NOT NULL, " +
            COL_ADDRESS_S + " TEXT, " + COL_ITIME + " TEXT NOT NULL, "  +
            COL_LATITUDE_E + " REAL NOT NULL, " + COL_LONGITUDE_E + " REAL NOT NULL, " +
            COL_FTIME + " TEXT, " +
            COL_TYPE_EVENT + " INTEGER NOT NULL, " +
            COL_TYPE_SERVICE + " INTEGER NOT NULL, " +
            COL_SHARE_POSITION + " INTEGER NOT NULL);";



    public DBPersistence2(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * onCreate Method that will be called to first create of DB
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PHONE_DATA);
        Log.d("DATABASE", "CREATED");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHODAT);

        onCreate(db);
    }


    /**
     * Closing Database
     */
    public void closeDB(){
        SQLiteDatabase db = this.getReadableDatabase();
        if(db != null && db.isOpen())
            db.close();
    }

    //////////////////////////
    ////// PHONE DATA ///////
    /////////////////////////

    public synchronized int insertPhoneData(PhoneData2 data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_IMEI, data.getImei());
        values.put(COL_VISIBLE,1);
        values.put(COL_SYNCHRONIZED, 0);
        values.put(COL_ORIGIN_NUMBER, data.getOriginNumber());
        values.put(COL_ORIGIN_NAME, data.getOriginName());
        values.put(COL_TARGET_NUMBER, data.getTargetNumber());
        values.put(COL_TARGET_NAME, data.getTargetName());
        values.put(COL_LATITUDE_S, data.getLatitudeS());
        values.put(COL_LONGITUDE_S, data.getLongitudeS());
        values.put(COL_LATITUDE_E, data.getLatitudeE());
        values.put(COL_LONGITUDE_E, data.getLongitudeE());
        values.put(COL_ADDRESS_S, data.getAddressS());
        values.put(COL_ITIME, data.getiTime());
        values.put(COL_FTIME, data.getfTime());
        values.put(COL_TYPE_EVENT, data.getTypeEvent());
        values.put(COL_TYPE_SERVICE, data.getTypeService());
        values.put(COL_SHARE_POSITION, data.getSharedPosition());

        long i = db.insert(TABLE_PHODAT, null, values);
        Log.d("DATABASE", "INSERTED " + i);
        return (getIdLastPhoneData());
    }

    public PhoneData2 getPhoneDataByID(String id, int typeService){
        PhoneData2 phoneData;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PHODAT
                + " WHERE " + COL_TYPE_SERVICE + " = " + typeService
                + " AND " + COL_ID + " LIKE \"" + id + "\"" ,null );

        phoneData = cursorToPhoneData(cursor);
        return phoneData;
    }

    public String getTableAsString() {
        String tableString = String.format("Table %s:\n", TABLE_PHODAT);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor allRows  = db.rawQuery("SELECT * FROM " + TABLE_PHODAT, null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }
        return tableString;
    }

    public PhoneData2[] getPhoneDataToSynchronization(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PHODAT
                + " WHERE "  + COL_SYNCHRONIZED + "= 0",null);
        PhoneData2[] datas = new PhoneData2[cursor.getCount()];
        int position = 0;
        while(cursor.moveToNext()){
            datas[position]  = cursorToPhoneData(cursor);
            position++;
        }
        cursor.close();
        return datas;
    }

    public Cursor getAllDataToCursor(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PHODAT + " ORDER BY " + COL_ID, null);
        return cursor;
    }

    /**
     * This method is used to retrieve information to ListViews
     * @param typeService
     * @return
     */

    public Cursor getPhoneDataToViews(int typeService){
        SQLiteDatabase db = this.getWritableDatabase();

        return db.rawQuery("SELECT * FROM " + TABLE_PHODAT
                + " WHERE " + COL_VISIBLE + " = 1 AND "
                + COL_TYPE_SERVICE + " = " + typeService
                + " ORDER BY "  + COL_ID, null);
    }


    public Cursor getPhoneDataByNumber(String inputText, int typeService){
        SQLiteDatabase db = this.getWritableDatabase();

        return db.rawQuery("SELECT * FROM " + TABLE_PHODAT
                + " WHERE " + COL_VISIBLE + " = 1 AND " + COL_TYPE_SERVICE
                + " = " + typeService + " AND " + COL_TARGET_NUMBER
                + " LIKE '%" + inputText + "%' OR " + COL_TARGET_NAME
                + " LIKE '%" + inputText + "%' OR " + COL_ORIGIN_NUMBER
                + " LIKE '%" + inputText + "%' OR " + COL_ORIGIN_NAME
                + " LIKE '%" + inputText + "%' ORDER BY " + COL_ID, null);
    }



    public int updatePhoneData(int id, PhoneData2 data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_IMEI, data.getImei());
        values.put(COL_ORIGIN_NUMBER, data.getOriginNumber());
        values.put(COL_ORIGIN_NAME, data.getOriginName());
        values.put(COL_TARGET_NUMBER, data.getTargetNumber());
        values.put(COL_TARGET_NAME, data.getTargetName());
        values.put(COL_LATITUDE_S, data.getLatitudeS());
        values.put(COL_LONGITUDE_S, data.getLongitudeS());
        values.put(COL_LATITUDE_E, data.getLatitudeE());
        values.put(COL_LONGITUDE_E, data.getLongitudeE());
        values.put(COL_ADDRESS_S, data.getAddressS());
        values.put(COL_ITIME, data.getiTime());
        values.put(COL_FTIME, data.getfTime());
        values.put(COL_TYPE_EVENT, data.getTypeEvent());
        values.put(COL_TYPE_SERVICE, data.getTypeService());
        values.put(COL_SHARE_POSITION, data.getSharedPosition());

        return db.update(TABLE_PHODAT, values, COL_ID + " = " + id, null);
    }

    public void setPhoneDataSynchronized(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_SYNCHRONIZED, 1);
        db.update(TABLE_PHODAT, values, COL_ID + " = " + id, null);
    }

    public synchronized void setSharedPositionOnPhoneData(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_SHARE_POSITION, 1);
        db.update(TABLE_PHODAT, values, COL_ID + " = " + id, null);
    }

    public int setPhoneDataInvisible(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_VISIBLE, 0);
        return db.update(TABLE_PHODAT, values, COL_ID + " = " + id, null);
    }

    public int removeAllPhoneData(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_PHODAT, null,null);
    }




    /**
     * Function to return the last phone data on db
     * @return int id
     */
    private synchronized int getIdLastPhoneData(){
        SQLiteDatabase db = this.getWritableDatabase();
        int answer = -1;
        Cursor cursor = db.rawQuery("SELECT MAX("+ COL_ID + ") FROM " + TABLE_PHODAT, null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            answer = cursor.getInt(NUM_COL_ID);
        }
        cursor.close();
        return answer;
    }

    /**
     * Method to convert Cursor to PhoneData
     * @param cursor  cursor
     * @return PhoneData or Null
     */

    public PhoneData2 cursorToPhoneData(Cursor cursor){
        PhoneData2 data = null;
        if (cursor.getCount() != 0){
            data = new PhoneData2();
            data.setId(cursor.getInt(NUM_COL_ID));
            data.setImei(cursor.getString(NUM_COL_IMEI));
            data.setOriginNumber(cursor.getString(NUM_COL_ORIGIN_NUMBER));
            data.setOriginName(cursor.getString(NUM_COL_ORIGIN_NAME));
            data.setTargetNumber(cursor.getString(NUM_COL_TARGET_NUMBER));
            data.setTargetName(cursor.getString(NUM_COL_TARGET_NAME));
            data.setLatitudeS(cursor.getDouble(NUM_COL_LATITUDE_S));
            data.setLongitudeS(cursor.getDouble(NUM_COL_LONGITUDE_S));
            data.setAddressS(cursor.getString(NUM_COL_ADDRESS_S));
            data.setLatitudeE(cursor.getDouble(NUM_COL_LATITUDE_E));
            data.setLongitudeE(cursor.getDouble(NUM_COL_LONGITUDE_E));
            data.setiTime(cursor.getString(NUM_COL_ITIME));
            data.setfTime(cursor.getString(NUM_COL_FTIME));
            data.setTypeEvent(cursor.getInt(NUM_COL_TYPE_EVENT));
            data.setTypeService(cursor.getInt(NUM_COL_TYPE_SERVICE));
            data.setSharedPosition(cursor.getInt(NUM_COL_SHARE_POSITION));
        }
        return data;
    }



    public static int getDbVersion() {
        return DB_VERSION;
    }

    public static String getDbName() {
        return DB_NAME;
    }

    public static String getTablePhodat() {
        return TABLE_PHODAT;
    }

    public static String getColId() {
        return COL_ID;
    }

    public static int getNumColId() {
        return NUM_COL_ID;
    }

    public static String getColImei() {
        return COL_IMEI;
    }

    public static int getNumColImei() {
        return NUM_COL_IMEI;
    }

    public static String getColVisible() {
        return COL_VISIBLE;
    }

    public static String getColSynchronized() {
        return COL_SYNCHRONIZED;
    }

    public static String getColOriginNumber() {
        return COL_ORIGIN_NUMBER;
    }

    public static String getColOriginName() {
        return COL_ORIGIN_NAME;
    }

    public static String getColTargetNumber() {
        return COL_TARGET_NUMBER;
    }

    public static String getColTargetName() {
        return COL_TARGET_NAME;
    }

    public static String getColLatitudeS() {
        return COL_LATITUDE_S;
    }

    public static String getColLongitudeS() {
        return COL_LONGITUDE_S;
    }

    public static String getColAddress_S() {
        return COL_ADDRESS_S;
    }

    public static String getColLatitudeE() {
        return COL_LATITUDE_E;
    }

    public static String getColLongitudeE() {
        return COL_LONGITUDE_E;
    }

     public static String getColItime() {
        return COL_ITIME;
    }

    public static String getColFtime() {
        return COL_FTIME;
    }

    public static String getColTypeEvent() {
        return COL_TYPE_EVENT;
    }

    public static String getColTypeService() {
        return COL_TYPE_SERVICE;
    }

    public static String getColSharePosition() {
        return COL_SHARE_POSITION;
    }

    public static int getNumColVisible() {
        return NUM_COL_VISIBLE;
    }

    public static int getNumColSynchronized() {
        return NUM_COL_SYNCHRONIZED;
    }

    public static int getNumColOriginNumber() {
        return NUM_COL_ORIGIN_NUMBER;
    }

    public static int getNumColOriginName() {
        return NUM_COL_ORIGIN_NAME;
    }

    public static int getNumColTargetNumber() {
        return NUM_COL_TARGET_NUMBER;
    }

    public static int getNumColTargetName() {
        return NUM_COL_TARGET_NAME;
    }

    public static int getNumColLatitudeS() {
        return NUM_COL_LATITUDE_S;
    }

    public static int getNumColLongitudeS() {
        return NUM_COL_LONGITUDE_S;
    }

    public static int getNumColAddressS() {
        return NUM_COL_ADDRESS_S;
    }

    public static int getNumColLatitudeE() {
        return NUM_COL_LATITUDE_E;
    }

    public static int getNumColLongitudeE() {
        return NUM_COL_LONGITUDE_E;
    }

    public static int getNumColItime() {
        return NUM_COL_ITIME;
    }

    public static int getNumColFtime() {
        return NUM_COL_FTIME;
    }

    public static int getNumColTypeEvent() {
        return NUM_COL_TYPE_EVENT;
    }

    public static int getNumColTypeService() {
        return NUM_COL_TYPE_SERVICE;
    }

    public static int getNumColSharePosition() {
        return NUM_COL_SHARE_POSITION;
    }

    public static String getColName() {
        return COL_NAME;
    }

    public static String getColNumber() {
        return COL_NUMBER;
    }

    public static int getNumColName() {
        return NUM_COL_NAME;
    }

    public static int getNumColNumber() {
        return NUM_COL_NUMBER;
    }

    public static String getCreateTablePhoneData() {
        return CREATE_TABLE_PHONE_DATA;
    }
}
