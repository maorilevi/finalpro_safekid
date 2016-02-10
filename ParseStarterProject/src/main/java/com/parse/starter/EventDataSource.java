package com.parse.starter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

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
            creatingmydbSQLite.COLUMN_PARSE,
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

    public Event CreateNewEvent(Event event) {
        ContentValues values = new ContentValues();
        values.put(creatingmydbSQLite.COLUMN_NAME,event.getName());
        values.put(creatingmydbSQLite.COLUMN_START_TIME,event.getStart_time());
        values.put(creatingmydbSQLite.COLUMN_END_TIME,event.getEnd_time());
        values.put(creatingmydbSQLite.COLUMN_DAY, event.getDay());
        values.put(creatingmydbSQLite.COLUMN_EVENT_ADDRESS,event.getAddress());
        values.put(creatingmydbSQLite.COLUMN_KID_ID, event.getKidID());
        values.put(creatingmydbSQLite.COLUMN_PARSE,event.getParseid());
        values.put(creatingmydbSQLite.COLUMN_LOCATION,event.getLocation());
        values.put(creatingmydbSQLite.COLUMN_EDATE,event.getDate());
        long insertId = database.insert(creatingmydbSQLite.TABLE_EVENT, null,
                values);
        Cursor cursor = database.query(creatingmydbSQLite.TABLE_EVENT,
                allColumns, creatingmydbSQLite.COLUMN_EID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Event newEvent = cursorToUser(cursor);
        cursor.close();
        return newEvent;
    }
    public void DeleteEvent(Event event) {
        long id = event.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(creatingmydbSQLite.TABLE_EVENT, creatingmydbSQLite.COLUMN_EID + " = " + id, null);
    }
    public void DeleteAllEvent(){
        List<Event> events = new ArrayList<Event>();
        Cursor cursor = database.query(creatingmydbSQLite.TABLE_EVENT, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Event event = cursorToUser(cursor);
            events.add(event);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        int listsize=events.size();
        for(int indx=0;indx<listsize;indx++)
            database.delete(creatingmydbSQLite.TABLE_EVENT, creatingmydbSQLite.COLUMN_EID + " = " + 0,null);
    }
    public int GetTableSize(){
        List<Event> events = new ArrayList<Event>();
        Cursor cursor = database.query(creatingmydbSQLite.TABLE_EVENT, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Event event  = cursorToUser(cursor);
            events.add(event);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return events.size();
    }
    public void UpdateEvent(Event event){
        ContentValues values = new ContentValues();
        values.put(creatingmydbSQLite.COLUMN_NAME,event.getName());
        values.put(creatingmydbSQLite.COLUMN_START_TIME,event.getStart_time());
        values.put(creatingmydbSQLite.COLUMN_END_TIME,event.getEnd_time());
        values.put(creatingmydbSQLite.COLUMN_DAY, event.getDay());
        values.put(creatingmydbSQLite.COLUMN_EVENT_ADDRESS,event.getAddress());
        values.put(creatingmydbSQLite.COLUMN_KID_ID, event.getKidID());
        values.put(creatingmydbSQLite.COLUMN_PARSE,event.getParseid());
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
            Event event = cursorToUser(cursor);
            events.add(event);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        //database.close();
        return events;
    }
    private Event cursorToUser(Cursor cursor) {
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
