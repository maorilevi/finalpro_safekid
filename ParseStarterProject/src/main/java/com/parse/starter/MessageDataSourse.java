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
public class MessageDataSourse {
    private SQLiteDatabase database;
    private creatingmydbSQLite dbHelper;
    private String[] allColumns = {
            creatingmydbSQLite.COLUMN_MID,
            creatingmydbSQLite.COLUMN_TEXT,
            creatingmydbSQLite.COLUMN_SENDER,
            creatingmydbSQLite.COLUMN_RECEIVER,
            creatingmydbSQLite.COLUMN_PARSEID,
            creatingmydbSQLite.COLUMN_DATE
    };
    public MessageDataSourse(Context context) {
        dbHelper = new creatingmydbSQLite(context);
    }
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
    public void close() {
        dbHelper.close();
    }

    public Message CreateNewMessage(Message message) {
        ContentValues values = new ContentValues();
        values.put(creatingmydbSQLite.COLUMN_TEXT,message.getMessage());
        values.put(creatingmydbSQLite.COLUMN_SENDER,message.getSender());
        values.put(creatingmydbSQLite.COLUMN_RECEIVER, message.getReceiver());
        values.put(creatingmydbSQLite.COLUMN_PARSEID,message.getParseid());
        values.put(creatingmydbSQLite.COLUMN_DATE, message.getDate());
        long insertId = database.insert(creatingmydbSQLite.TABLE_MESSAGE, null,
                values);
        Cursor cursor = database.query(creatingmydbSQLite.TABLE_MESSAGE,
                allColumns, creatingmydbSQLite.COLUMN_MID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Message newMessage = cursorToUser(cursor);
        cursor.close();
        return newMessage;
    }
    public void DeleteMessage(Message message) {
        long id = message.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(creatingmydbSQLite.TABLE_MESSAGE, creatingmydbSQLite.COLUMN_MID + " = " + id, null);
    }
    public void DeleteAllMessage(){
        List<Message> messages = new ArrayList<Message>();
        Cursor cursor = database.query(creatingmydbSQLite.TABLE_EVENT, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Message message = cursorToUser(cursor);
            messages.add(message);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        int listsize=messages.size();
        for(int indx=0;indx<listsize;indx++)
            database.delete(creatingmydbSQLite.TABLE_MESSAGE, creatingmydbSQLite.COLUMN_MID + " = " + 0,null);
    }
    public int GetTableSize(){
        List<Message> messages = new ArrayList<Message>();
        Cursor cursor = database.query(creatingmydbSQLite.TABLE_MESSAGE, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Message message  = cursorToUser(cursor);
            messages.add(message);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return messages.size();
    }
    public void UpdateMessage(Message message){
        ContentValues values = new ContentValues();
        values.put(creatingmydbSQLite.COLUMN_TEXT,message.getMessage());
        values.put(creatingmydbSQLite.COLUMN_SENDER,message.getSender());
        values.put(creatingmydbSQLite.COLUMN_RECEIVER, message.getReceiver());
        values.put(creatingmydbSQLite.COLUMN_PARSEID,message.getParseid());
        values.put(creatingmydbSQLite.COLUMN_DATE, message.getDate());
        database.update(creatingmydbSQLite.TABLE_MESSAGE,
                values, creatingmydbSQLite.COLUMN_ID + "=" + message.getId(), null);
    }
    public ArrayList<Message> getAllMessage() {
        ArrayList<Message> messages = new ArrayList<Message>();

        Cursor cursor = database.query(creatingmydbSQLite.TABLE_EVENT, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Message message = cursorToUser(cursor);
            messages.add(message);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        //database.close();
        return messages;
    }
    private Message cursorToUser(Cursor cursor) {
        Message message=new Message();
        message.setId(cursor.getLong(0));
        message.setMessage(cursor.getString(1));
        message.setSender(cursor.getString(2));
        message.setReceiver(cursor.getString(3));
        message.setParseid(cursor.getString(4));
        message.setDate(cursor.getString(5));
        return message;
    }
}
