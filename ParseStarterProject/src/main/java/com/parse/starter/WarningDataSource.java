package com.parse.starter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 05/03/2016.
 */
public class WarningDataSource {
    private SQLiteDatabase database;
    private creatingmydbSQLite dbHelper;
    private String[] allColumns = {
            creatingmydbSQLite.COLUMN_WARID,
            creatingmydbSQLite.COLUMN_KIDID,
            creatingmydbSQLite.COLUMN_KID_NAME,
            creatingmydbSQLite.COLUMN_EVENT_ID,
            creatingmydbSQLite.COLUMN_WAR_DATE,
            creatingmydbSQLite.COLUMN_WAR_P_ID,
            creatingmydbSQLite.COLUMN_LOC_x,
            creatingmydbSQLite.COLUMN_LOC_Y,
    };
    public WarningDataSource(Context context) {
        dbHelper = new creatingmydbSQLite(context);
    }
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
    public void close() {
        dbHelper.close();
    }
    public boolean CHECK_IF_EXIST(String PARSE_ID){
        boolean exist=false;
        Log.i("CHECK_IF_EXIST", "checking exist for:" + PARSE_ID);
        Cursor cursor2 = database.query(creatingmydbSQLite.TABLE_WARNING, allColumns, creatingmydbSQLite.COLUMN_WAR_P_ID + " =? ",
                new String[]{PARSE_ID}, null, null, null);
        cursor2.moveToFirst();
        if(cursor2.moveToFirst()&&cursor2.getCount()!=0) {
            exist = true;
            Log.i("CHECK_IF_EXIST", "exist");
        }
        cursor2.close();
        return exist;
    }
    public WarningMessage CreateWarning(WarningMessage warningMessage) {
        ContentValues values = new ContentValues();
        values.put(creatingmydbSQLite.COLUMN_KIDID, warningMessage.getKid_Parse_id());
        values.put(creatingmydbSQLite.COLUMN_KID_NAME, warningMessage.getKid_name());
        values.put(creatingmydbSQLite.COLUMN_EVENT_ID, warningMessage.getEvent_parse_id());
        values.put(creatingmydbSQLite.COLUMN_WAR_DATE, warningMessage.getDate());
        values.put(creatingmydbSQLite.COLUMN_P_ID_USER,warningMessage.getWarning_parse_id());
        values.put(creatingmydbSQLite.COLUMN_LOC_x,warningMessage.getLoc_X());
        values.put(creatingmydbSQLite.COLUMN_LOC_Y,warningMessage.getLoc_y());

        long insertId = database.insert(creatingmydbSQLite.TABLE_WARNING, null,
                values);
        Cursor cursor = database.query(creatingmydbSQLite.TABLE_WARNING,
                allColumns, creatingmydbSQLite.COLUMN_WARID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        WarningMessage newwarningMessage = cursorToWarMeassage(cursor);
        cursor.close();
        return newwarningMessage;
    }

    public void DeleteWarning(WarningMessage warningMessage) {
        long id = warningMessage.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(creatingmydbSQLite.TABLE_WARNING, creatingmydbSQLite.COLUMN_WARID + " = " + id, null);
    }

    public void DeleteAllWarning(){
        Cursor cursor = database.query(creatingmydbSQLite.TABLE_WARNING, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            WarningMessage warningMessage = cursorToWarMeassage(cursor);
            DeleteWarning(warningMessage);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
    }

    public int GetTableSize(){
        List<WarningMessage> warningMessages = new ArrayList<WarningMessage>();
        Cursor cursor = database.query(creatingmydbSQLite.TABLE_WARNING, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            WarningMessage warningMessage = cursorToWarMeassage(cursor);
            warningMessages.add(warningMessage);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return warningMessages.size();
    }
    public void UpdateWarning(WarningMessage warningMessage){
        ContentValues values = new ContentValues();
        values.put(creatingmydbSQLite.COLUMN_KIDID,warningMessage.getKid_Parse_id());
        values.put(creatingmydbSQLite.COLUMN_KID_NAME, warningMessage.getKid_name());
        values.put(creatingmydbSQLite.COLUMN_EVENT_ID, warningMessage.getEvent_parse_id());
        values.put(creatingmydbSQLite.COLUMN_WAR_DATE, warningMessage.getDate());
        values.put(creatingmydbSQLite.COLUMN_WAR_P_ID,warningMessage.getWarning_parse_id());
        values.put(creatingmydbSQLite.COLUMN_LOC_x, warningMessage.getLoc_X());
        values.put(creatingmydbSQLite.COLUMN_LOC_Y,warningMessage.getLoc_y());

        database.update(creatingmydbSQLite.TABLE_WARNING,
                values, creatingmydbSQLite.COLUMN_WARID + "=" + warningMessage.getId(), null);
    }
    public ArrayList<WarningMessage> getAllKidWarning(String kidid) {
        ArrayList<WarningMessage> warlist = new ArrayList<WarningMessage>();

            Cursor cursor2 = database.query(
                    creatingmydbSQLite.TABLE_WARNING,
                    allColumns, creatingmydbSQLite.COLUMN_KIDID + " =? ",
                    new String[]{kidid},
                    null,
                    null,
                    null
            );
            cursor2.moveToFirst();
            while (!cursor2.isAfterLast()) {
                WarningMessage war = cursorToWarMeassage(cursor2);
                warlist.add(war);
                cursor2.moveToNext();
            }
            cursor2.close();
        return warlist;
    }
    public ArrayList<WarningMessage> getAllWarning() {
        ArrayList<WarningMessage> WarningMessages = new ArrayList<WarningMessage>();

        Cursor cursor = database.query(creatingmydbSQLite.TABLE_WARNING, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            WarningMessage warningMessage = cursorToWarMeassage(cursor);
            WarningMessages.add(warningMessage);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        //database.close();
        return WarningMessages;
    }
    private WarningMessage cursorToWarMeassage(Cursor cursor) {
        WarningMessage warningMessage = new WarningMessage();
        warningMessage.setId(cursor.getLong(0));
        warningMessage.setKid_Parse_id(cursor.getString(1));
        warningMessage.setKid_name(cursor.getString(2));
        warningMessage.setEvent_parse_id(cursor.getString(3));
        warningMessage.setDate(cursor.getString(4));
        warningMessage.setWarning_parse_id(cursor.getString(5));
        warningMessage.setLoc_X(cursor.getDouble(6));
        warningMessage.setLoc_y(cursor.getDouble(7));
        return warningMessage;
    }

}
