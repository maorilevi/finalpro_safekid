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
public class MessageDataSourse {
    private SQLiteDatabase database;
    private creatingmydbSQLite dbHelper;
    private String[] allColumns = {
            creatingmydbSQLite.COLUMN_MID,
            creatingmydbSQLite.COLUMN_TEXT,
            creatingmydbSQLite.COLUMN_SENDER,
            creatingmydbSQLite.COLUMN_RECEIVER,
            creatingmydbSQLite.COLUMN_PARSEID,
            creatingmydbSQLite.COLUMN_DATE,
            creatingmydbSQLite.COLUMN_CHATROOM,
            creatingmydbSQLite.COLUMN_READE
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

    public boolean CreateNewMessage(Message message) {
        boolean create=false;
        ContentValues values = new ContentValues();
        values.put(creatingmydbSQLite.COLUMN_TEXT, message.getMessage());
        values.put(creatingmydbSQLite.COLUMN_SENDER, message.getSender());
        values.put(creatingmydbSQLite.COLUMN_RECEIVER, message.getReceiver());
        values.put(creatingmydbSQLite.COLUMN_PARSEID, message.getParseid());
        values.put(creatingmydbSQLite.COLUMN_DATE, message.getDate());
        values.put(creatingmydbSQLite.COLUMN_CHATROOM, message.getChatRoom_ID());
        if(message.isReade()){
            values.put(creatingmydbSQLite.COLUMN_READE,1);
        }else{
            values.put(creatingmydbSQLite.COLUMN_READE,0);
        }
        ArrayList<Message> messlis=new ArrayList<Message>();
        messlis=getAllMessage(message.getChatRoom_ID());
        if(messlis.size()>30){
            DeleteMessage(messlis.get(messlis.size()-1));
        }
        Message newMessage=new Message();
        Cursor cursor2 = database.query(creatingmydbSQLite.TABLE_MESSAGE, allColumns, creatingmydbSQLite.COLUMN_PARSEID + " =? ",
                new String[]{message.getParseid()}, null, null, null);
        cursor2.moveToFirst();
        if(!cursor2.moveToFirst()&&cursor2.getCount()==0){
            create=true;
            Log.i("MessageCreate","saving in db");
            long insertId = database.insert(creatingmydbSQLite.TABLE_MESSAGE, null,
                    values);
            Cursor cursor = database.query(creatingmydbSQLite.TABLE_MESSAGE,
                    allColumns, creatingmydbSQLite.COLUMN_MID + " = " + insertId, null,
                    null, null, null);
            cursor.moveToFirst();
            newMessage = cursorToMessage(cursor);
            cursor.close();
        }
        cursor2.close();
        return create;
    }

    public void DeleteMessage(Message message) {
        long id = message.getId();
        Log.i("Message deleted with id", "id:" + id);
        database.delete(creatingmydbSQLite.TABLE_MESSAGE, creatingmydbSQLite.COLUMN_MID + " = " + id, null);
    }
    public void DeleteAllMessage(){
        List<Message> messages = new ArrayList<Message>();
        Cursor cursor = database.query(creatingmydbSQLite.TABLE_MESSAGE, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Message message = cursorToMessage(cursor);
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
            Message message  = cursorToMessage(cursor);
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
        values.put(creatingmydbSQLite.COLUMN_PARSEID, message.getParseid());
        values.put(creatingmydbSQLite.COLUMN_DATE, message.getDate());
        values.put(creatingmydbSQLite.COLUMN_CHATROOM, message.getChatRoom_ID());
        if(message.isReade()){
            values.put(creatingmydbSQLite.COLUMN_READE,1);
        }else{
            values.put(creatingmydbSQLite.COLUMN_READE,0);
        }
        database.update(creatingmydbSQLite.TABLE_MESSAGE,
                values, creatingmydbSQLite.COLUMN_ID + "=" + message.getId(), null);
    }
    public ArrayList<Message> getAllMessage(String x) {
        ArrayList<Message> messages = new ArrayList<Message>();
        Cursor cursor = database.query(creatingmydbSQLite.TABLE_MESSAGE, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Message message = cursorToMessage(cursor);
            if(message!=null){
                if(message.getChatRoom_ID().matches(x)){
                    messages.add(message);
                }
            }
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        //database.close();
        return messages;
    }
    private Message cursorToMessage(Cursor cursor) {
        Message message=new Message();
        message.setId(cursor.getLong(0));
        message.setMessage(cursor.getString(1));
        message.setSender(cursor.getString(2));
        message.setReceiver(cursor.getString(3));
        message.setParseid(cursor.getString(4));
        message.setDate(cursor.getString(5));
        message.setChatRoom_ID(cursor.getString(6));
        message.setReade(cursor.getInt(7)==1?true:false);
        return message;
    }
}
