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
 * Created by Admin on 09/02/2016.
 */
public class EventDataSource {
    private SQLiteDatabase database;
    private creatingmydbSQLite dbHelper;
    private String[] allColumns = {
            creatingmydbSQLite.COLUMN_EID,
            creatingmydbSQLite.COLUMN_NAME,
            creatingmydbSQLite.COLUMN_START_TIME,
            creatingmydbSQLite.COLUMN_END_TIME,
            creatingmydbSQLite.COLUMN_DAY,
            creatingmydbSQLite.COLUMN_EVENT_ADDRESS,
            creatingmydbSQLite.COLUMN_KID_ID,
            creatingmydbSQLite.COLUMN_P_ID_EVENT,
            creatingmydbSQLite.COLUMN_LOCATION,
            creatingmydbSQLite.COLUMN_EDATE
    };
    public EventDataSource(Context context) {
        dbHelper = new creatingmydbSQLite(context);
    }
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
    public void close() {
        dbHelper.close();
    }


    public boolean exist(Event event){
        Log.i("DELETE_EVENT","checking exist");
        boolean exist=true;
        Cursor cursor2 = database.query(creatingmydbSQLite.TABLE_EVENT, allColumns, creatingmydbSQLite.COLUMN_P_ID_EVENT + " =? ",
                new String[]{event.getParseid()}, null, null, null);
        cursor2.moveToFirst();
        if(!cursor2.moveToFirst()&&cursor2.getCount()==0) {
            exist = false;
        }
        cursor2.close();
        return exist;
    }
    public ArrayList<Event> GetEventByDay(String day){
        Log.i("DBEVENT", "IN DB");
        ArrayList<Event> events=new ArrayList<Event>();
        Cursor cursor2 = database.query(creatingmydbSQLite.TABLE_EVENT,
                allColumns, creatingmydbSQLite.COLUMN_DAY + " =? ",
                new String[]{day}, null, null, null);
        cursor2.moveToFirst();
        while (!cursor2.isAfterLast()) {
            Event event = cursorToEvent(cursor2);
            events.add(event);
            cursor2.moveToNext();
        }
        cursor2.close();
        return events;
    }

    public boolean CreateNewEvent(Event event) {
        boolean create=false;
        ContentValues values = new ContentValues();
        values.put(creatingmydbSQLite.COLUMN_NAME,event.getName());
        values.put(creatingmydbSQLite.COLUMN_START_TIME,event.getStart_time());
        values.put(creatingmydbSQLite.COLUMN_END_TIME,event.getEnd_time());
        values.put(creatingmydbSQLite.COLUMN_DAY, event.getDay());
        values.put(creatingmydbSQLite.COLUMN_EVENT_ADDRESS,event.getAddress());
        values.put(creatingmydbSQLite.COLUMN_KID_ID, event.getKidID());
        values.put(creatingmydbSQLite.COLUMN_P_ID_EVENT,event.getParseid());
        values.put(creatingmydbSQLite.COLUMN_LOCATION,event.getLocation());
        values.put(creatingmydbSQLite.COLUMN_EDATE,event.getDate());
        Event newevent=new Event();
        Cursor cursor2 = database.query(creatingmydbSQLite.TABLE_EVENT, allColumns, creatingmydbSQLite.COLUMN_P_ID_EVENT + " =? ",
                new String[]{event.getParseid()}, null, null, null);
        cursor2.moveToFirst();
        if(!cursor2.moveToFirst()&&cursor2.getCount()==0){
            create=true;
            Log.i("UPDATEEVENT", "create new");
            long insertId = database.insert(creatingmydbSQLite.TABLE_EVENT, null,
                    values);
            Cursor cursor = database.query(creatingmydbSQLite.TABLE_EVENT,
                    allColumns, creatingmydbSQLite.COLUMN_EID + " = " + insertId, null,
                    null, null, null);
            cursor.moveToFirst();
            newevent = cursorToEvent(cursor);
            cursor.close();
        }
        cursor2.close();
        return create;
    }
    public void DeleteEvent(Event event) {
        long id = event.getId();
        Log.i("DELETE_EVENT",""+id);
        database.delete(creatingmydbSQLite.TABLE_EVENT, creatingmydbSQLite.COLUMN_EID + " = " + id, null);
    }
    public void DeleteAllEvent(){
        List<Event> events = new ArrayList<Event>();
        Cursor cursor = database.query(creatingmydbSQLite.TABLE_EVENT, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Event event = cursorToEvent(cursor);
            DeleteEvent(event);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
    }
    public int GetTableSize(){
        List<Event> events = new ArrayList<Event>();
        Cursor cursor = database.query(creatingmydbSQLite.TABLE_EVENT, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Event event  = cursorToEvent(cursor);
            events.add(event);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return events.size();
    }
    public void UpdateEventFROMPARSE(Event event){
        Log.i("UPDATEEVENT", "update event");
        ContentValues values = new ContentValues();
        values.put(creatingmydbSQLite.COLUMN_NAME,event.getName());
        values.put(creatingmydbSQLite.COLUMN_START_TIME, event.getStart_time());
        values.put(creatingmydbSQLite.COLUMN_END_TIME,event.getEnd_time());
        values.put(creatingmydbSQLite.COLUMN_DAY, event.getDay());
        values.put(creatingmydbSQLite.COLUMN_EVENT_ADDRESS,event.getAddress());
        values.put(creatingmydbSQLite.COLUMN_KID_ID, event.getKidID());
        values.put(creatingmydbSQLite.COLUMN_P_ID_EVENT, event.getParseid());
        values.put(creatingmydbSQLite.COLUMN_LOCATION, event.getLocation());
        values.put(creatingmydbSQLite.COLUMN_EDATE, event.getDate());
        Cursor cursor2 = database.query(creatingmydbSQLite.TABLE_EVENT, allColumns, creatingmydbSQLite.COLUMN_P_ID_EVENT + " =? ",
                new String[]{event.getParseid()}, null, null, null);
        cursor2.moveToFirst();
        Log.i("UPDATEEVENT", "create new");
        Event eventtoupdate = cursorToEvent(cursor2);
        Log.i("UPDATEEVENT", eventtoupdate.getName());
        database.update(creatingmydbSQLite.TABLE_EVENT,
                values, creatingmydbSQLite.COLUMN_ID + "=" + eventtoupdate.getId(), null);
        cursor2.close();
    }
    public void UpdateEvent(Event event){
        Log.i("UPDATEEVENT", "update event");
        ContentValues values = new ContentValues();
        values.put(creatingmydbSQLite.COLUMN_NAME,event.getName());
        values.put(creatingmydbSQLite.COLUMN_START_TIME,event.getStart_time());
        values.put(creatingmydbSQLite.COLUMN_END_TIME,event.getEnd_time());
        values.put(creatingmydbSQLite.COLUMN_DAY, event.getDay());
        values.put(creatingmydbSQLite.COLUMN_EVENT_ADDRESS,event.getAddress());
        values.put(creatingmydbSQLite.COLUMN_KID_ID, event.getKidID());
        values.put(creatingmydbSQLite.COLUMN_P_ID_EVENT,event.getParseid());
        values.put(creatingmydbSQLite.COLUMN_LOCATION,event.getLocation());
        values.put(creatingmydbSQLite.COLUMN_EDATE,event.getDate());
        database.update(creatingmydbSQLite.TABLE_EVENT,
                values, creatingmydbSQLite.COLUMN_ID + "=" + event.getId(), null);
    }
    public ArrayList<Event> getAllEvents() {
        ArrayList<Event> events = new ArrayList<Event>();
        Cursor cursor = database.query(creatingmydbSQLite.TABLE_EVENT, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Event event = cursorToEvent(cursor);
            events.add(event);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        //database.close();
        return events;
    }
    private Event cursorToEvent(Cursor cursor) {
        Event event=new Event();
        event.setId(cursor.getLong(0));
        event.setName(cursor.getString(1));
        event.setStart_time(cursor.getString(2));
        event.setEnd_time(cursor.getString(3));
        event.setDay(cursor.getString(4));
        event.setAddress(cursor.getString(5));
        event.setKidID(cursor.getString(6));
        event.setParseid(cursor.getString(7));
        event.setLocation(cursor.getLong(8));
        event.setDate(cursor.getString(9));
        return event;
    }
}
