package com.parse.starter;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Admin on 07/01/2016.
 */
public class CheckEventService extends Service {
    private final IBinder mBinder = new MyBinder();
    private UsersDataSource UserDB;//user database
    //List<User> users = UserDB.getAllUsers();

    static protected SharedPreferences sharedPreferences;
    static protected Calendar cal;
    private String MyString;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        UserDB = new UsersDataSource(this);
        UserDB.open();
        List<User> users = UserDB.getAllUsers();
        UserDB.close();
        Log.i("chacking","thread is runing");
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
        Calendar calendar = Calendar.getInstance();
        String ThisDay = dayFormat.format(calendar.getTime());
        ParseQuery<ParseObject> EventTable = ParseQuery.getQuery("Event");
        EventTable.whereEqualTo("Kid_ID",users.get(0).getUserParseID());
        EventTable.whereEqualTo("Day",ThisDay);
        EventTable.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> results, ParseException e) {
                if (e == null) {
                    if (results.size() > 0) {
                        for (int indx = 0; indx < results.size(); indx++) {
                            String[] starttime = results.get(indx).getString("StartEvent").split(":");
                            String[] endtime = results.get(indx).getString("EndEvent").split(":");
                            int start = (Integer.parseInt(starttime[0]) * 60 + Integer.parseInt(starttime[1]));
                            int end = (Integer.parseInt(endtime[0]) * 60 + Integer.parseInt(endtime[1]));
                            cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"));
                            int now = (cal.get(Calendar.HOUR_OF_DAY) * 60) + cal.get(Calendar.MINUTE);
                            if (now >= start && now <= end) {
                                ParseQuery pushQuery = ParseInstallation.getQuery();
                                pushQuery.whereEqualTo("channels", "maor"); // Set the channel
                                ParsePush push = new ParsePush();
                                push.setQuery(pushQuery);// Set our Installation query
                                push.setMessage("kid:");
                                push.sendInBackground();
                            }
                        }
                    }
                } else {

                }
            }
        });
        MyString="maor";
        /*final Handler handler = new Handler();
        TimerTask timertask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(timertask, 0, 5000);*/
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    public class MyBinder extends Binder {
        CheckEventService getService() {
            return CheckEventService.this;
        }
    }

    public String getWordList() {
        return MyString;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}
