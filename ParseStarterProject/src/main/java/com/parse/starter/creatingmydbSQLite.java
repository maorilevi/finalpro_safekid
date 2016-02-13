package com.parse.starter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Admin on 22/01/2016.
 */
public class creatingmydbSQLite extends SQLiteOpenHelper {


    //users database
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_F_NAME = "fname";
    public static final String COLUMN_L_NAME = "lname";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PARSEKEYID = "parsesid";
    public static final String COLUMN_BIRTHDAY = "birthday";
    public static final String COLUMN_PARENT = "parent";
    public static final String COLUMN_PASS="pass";
    public static final String COLUMN_IMAGE="image";
    public static final String COLUMN_CHATID="chatid";
    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION =1;
    // Database creation sql statement
    private static final String DATABASE_CREATE =
            "create table " + TABLE_USERS +
                    "(" + COLUMN_ID + " integer primary key autoincrement, "
                    + COLUMN_F_NAME
                    + " text,"
                    + COLUMN_L_NAME
                    + " text,"
                    + COLUMN_ADDRESS
                    + " text,"
                    + COLUMN_EMAIL
                    + " text,"
                    + COLUMN_PHONE
                    + " text,"
                    + COLUMN_USERNAME
                    + " text,"
                    + COLUMN_PARSEKEYID
                    + " text,"
                    + COLUMN_PARENT
                    + " integer,"
                    + COLUMN_BIRTHDAY
                    + " text,"
                    + COLUMN_PASS
                    + " text,"
                    + COLUMN_IMAGE
                    + " BLOB,"
                    + COLUMN_CHATID
                    + " text)";
    //message database
    public static final String TABLE_MESSAGE = "message";
    public static final String COLUMN_MID = "_id";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_SENDER = "sender";
    public static final String COLUMN_RECEIVER = "receiver";
    public static final String COLUMN_PARSEID = "parseid";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_CHATROOM="chatroom";
    public static final String COLUMN_SIDE="side";
    public static final String COLUMN_READE="reade";

    private static final String DATABASE_MESSAGE_CREATE =
            "create table " + TABLE_MESSAGE +
                    "(" + COLUMN_MID + " integer primary key autoincrement, "
                    + COLUMN_TEXT
                    + " text,"
                    + COLUMN_SENDER
                    + " text,"
                    + COLUMN_RECEIVER
                    + " text,"
                    + COLUMN_PARSEID
                    + " text,"
                    + COLUMN_DATE
                    + " text,"
                    + COLUMN_CHATROOM
                    + " text,"
                    + COLUMN_READE
                    + " Integer)";
    //event database
    public static final String TABLE_EVENT = "event";
    public static final String COLUMN_EID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_START_TIME = "start";
    public static final String COLUMN_END_TIME = "end";
    public static final String COLUMN_DAY = "day";
    public static final String COLUMN_EVENT_ADDRESS = "address";
    public static final String COLUMN_KID_ID = "kidid";
    public static final String COLUMN_PARSE = "parse";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_EDATE = "date";
    private static final String DATABASE_EVENT_CREATE =
            "create table " + TABLE_EVENT +
                    "(" + COLUMN_EID + " integer primary key autoincrement, "
                    + COLUMN_NAME
                    + " text,"
                    + COLUMN_START_TIME
                    + " text,"
                    + COLUMN_END_TIME
                    + " text,"
                    + COLUMN_DAY
                    + " text,"
                    + COLUMN_EVENT_ADDRESS
                    + " text,"
                    + COLUMN_KID_ID
                    + " text,"
                    + COLUMN_PARSE
                    + " text,"
                    + COLUMN_LOCATION
                    + " integer,"
                    + COLUMN_EDATE
                    + " text)";
    public creatingmydbSQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
        database.execSQL(DATABASE_MESSAGE_CREATE);
        database.execSQL(DATABASE_EVENT_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(creatingmydbSQLite.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_MESSAGE_CREATE);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_EVENT_CREATE);
        onCreate(db);

    }

}