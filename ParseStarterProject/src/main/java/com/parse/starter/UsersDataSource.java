package com.parse.starter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 22/01/2016.
 */
public class UsersDataSource {
    private SQLiteDatabase database;
    private creatingmydbSQLite dbHelper;
    private String[] allColumns = {
            creatingmydbSQLite.COLUMN_ID,
            creatingmydbSQLite.COLUMN_F_NAME,
            creatingmydbSQLite.COLUMN_L_NAME,
            creatingmydbSQLite.COLUMN_ADDRESS,
            creatingmydbSQLite.COLUMN_EMAIL,
            creatingmydbSQLite.COLUMN_PHONE,
            creatingmydbSQLite.COLUMN_USERNAME,
            creatingmydbSQLite.COLUMN_P_ID_USER,
            creatingmydbSQLite.COLUMN_PARENT,
            creatingmydbSQLite.COLUMN_BIRTHDAY,
            creatingmydbSQLite.COLUMN_PASS,
            creatingmydbSQLite.COLUMN_IMAGE,
            creatingmydbSQLite.COLUMN_FAMILY_ID,
            creatingmydbSQLite.COLUMN_CHATID
    };
    public UsersDataSource(Context context) {
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
        Log.i("CHECK_IF_EXIST", "checking exist for:"+PARSE_ID);
        Cursor cursor2 = database.query(creatingmydbSQLite.TABLE_USERS, allColumns, creatingmydbSQLite.COLUMN_P_ID_USER + " =? ",
                new String[]{PARSE_ID}, null, null, null);
        cursor2.moveToFirst();
        if(cursor2.moveToFirst()&&cursor2.getCount()!=0) {
            exist = true;
            Log.i("CHECK_IF_EXIST", "exist");
        }
        cursor2.close();
        return exist;
    }
    public User createUSER(User user) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        user.getUserImage().compress(Bitmap.CompressFormat.PNG, 10, stream);
        byte[] image = stream.toByteArray();
        ContentValues values = new ContentValues();
        values.put(creatingmydbSQLite.COLUMN_F_NAME, user.getFirstname());
        values.put(creatingmydbSQLite.COLUMN_L_NAME, user.getLestName());
        values.put(creatingmydbSQLite.COLUMN_ADDRESS, user.getAddress());
        values.put(creatingmydbSQLite.COLUMN_EMAIL, user.getEmail());
        values.put(creatingmydbSQLite.COLUMN_PHONE, user.getPhoneNumber());
        values.put(creatingmydbSQLite.COLUMN_USERNAME, user.getUserName());
        values.put(creatingmydbSQLite.COLUMN_P_ID_USER, user.getUserParseID());
        if(user.isPerant()){
            values.put(creatingmydbSQLite.COLUMN_PARENT,1);
        }else{
            values.put(creatingmydbSQLite.COLUMN_PARENT,0);
        }
        values.put(creatingmydbSQLite.COLUMN_BIRTHDAY, user.getBirthday());
        values.put(creatingmydbSQLite.COLUMN_PASS, user.getPassword());
        values.put(creatingmydbSQLite.COLUMN_IMAGE, image);
        values.put(creatingmydbSQLite.COLUMN_FAMILY_ID, user.getFamily_id());
        values.put(creatingmydbSQLite.COLUMN_CHATID, user.getChatroom());


        long insertId = database.insert(creatingmydbSQLite.TABLE_USERS, null,
                values);
        Cursor cursor = database.query(creatingmydbSQLite.TABLE_USERS,
                allColumns, creatingmydbSQLite.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        User newuser = cursorToUser(cursor);
        cursor.close();
        return newuser;
    }

    public void DeleteUser(User user) {
        long id = user.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(creatingmydbSQLite.TABLE_USERS, creatingmydbSQLite.COLUMN_ID + " = " + id, null);
    }

    public void DeleteAllUsers(){
        List<User> users = new ArrayList<User>();
        Cursor cursor = database.query(creatingmydbSQLite.TABLE_USERS, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User user = cursorToUser(cursor);
            DeleteUser(user);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
    }

    public int GetTableSize(){
        List<User> users = new ArrayList<User>();
        Cursor cursor = database.query(creatingmydbSQLite.TABLE_USERS, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User user = cursorToUser(cursor);
            users.add(user);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return users.size();
    }
    public void UpdateUser(User user){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        user.getUserImage().compress(Bitmap.CompressFormat.PNG, 10, stream);
        byte[] image = stream.toByteArray();
        ContentValues values = new ContentValues();
        values.put(creatingmydbSQLite.COLUMN_F_NAME, user.getFirstname());
        values.put(creatingmydbSQLite.COLUMN_L_NAME, user.getLestName());
        values.put(creatingmydbSQLite.COLUMN_ADDRESS, user.getAddress());
        values.put(creatingmydbSQLite.COLUMN_EMAIL, user.getEmail());
        values.put(creatingmydbSQLite.COLUMN_PHONE, user.getPhoneNumber());
        values.put(creatingmydbSQLite.COLUMN_USERNAME, user.getUserName());
        values.put(creatingmydbSQLite.COLUMN_P_ID_USER, user.getUserParseID());
        if(user.isPerant()){
            values.put(creatingmydbSQLite.COLUMN_PARENT,1);
        }else{
            values.put(creatingmydbSQLite.COLUMN_PARENT,0);
        }
        values.put(creatingmydbSQLite.COLUMN_BIRTHDAY, user.getBirthday());
        values.put(creatingmydbSQLite.COLUMN_PASS, user.getPassword());
        values.put(creatingmydbSQLite.COLUMN_IMAGE, image);
        values.put(creatingmydbSQLite.COLUMN_FAMILY_ID, user.getFamily_id());

        values.put(creatingmydbSQLite.COLUMN_CHATID, user.getChatroom());

        database.update(creatingmydbSQLite.TABLE_USERS,
                values, creatingmydbSQLite.COLUMN_ID + "=" + user.getId(), null);
    }
    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<User>();

        Cursor cursor = database.query(creatingmydbSQLite.TABLE_USERS, allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User user = cursorToUser(cursor);
            users.add(user);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        //database.close();
        return users;
    }
    private User cursorToUser(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getLong(0));
        user.setFirstname(cursor.getString(1));
        user.setLestName(cursor.getString(2));
        user.setAddress(cursor.getString(3));
        user.setEmail(cursor.getString(4));
        user.setPhoneNumber(cursor.getString(5));
        user.setUserName(cursor.getString(6));
        user.setUserParseID(cursor.getString(7));
        user.setPerant(cursor.getInt(8) == 1 ? true : false);
        user.setBirthday(cursor.getString(9));
        user.setPassword(cursor.getString(10));
        user.setUserImage(BitmapFactory.decodeByteArray(cursor.getBlob(11), 0, cursor.getBlob(11).length));
        user.setFamily_id(cursor.getString(12));
        user.setChatroom(cursor.getString(13));
        return user;
    }
}
