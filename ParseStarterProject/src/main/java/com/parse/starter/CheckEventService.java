package com.parse.starter;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    public Context context = this;


    static public double LENGTH_OF_TIME_EVENT=0;
    static public double LENGTH_OF_TIME_STAY=0;
    static public String event_id;
    static public double x = 0;
    static public double y = 0;
    static public String[] full_date = {""};
    static protected Calendar cal;

    @Override
    public void onCreate() {
        //Toast.makeText(this, "Service created!", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        UserDB = new UsersDataSource(context);
        UserDB.open();
        List<User> users = UserDB.getAllUsers();
        UserDB.close();
        if (users.size() > 0) {
            if (!users.get(0).isPerant()) {
                Log.i("chacking", "thread is runing");
                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
                Calendar calendar = Calendar.getInstance();
                String ThisDay = dayFormat.format(calendar.getTime());
                cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"));
                full_date = cal.getTime().toGMTString().split("GMT");
                Log.i("SERVICE", "DAY" + ThisDay);
                EventDataSource eventDB = new EventDataSource(context);
                eventDB.open();
                ArrayList<Event> eventlist = eventDB.GetEventByDay(ThisDay);
                eventDB.close();
                int now = 0;
                Log.i("SERVICE", "list size" + Integer.toString(eventlist.size()));
                Toast.makeText(getApplication(), "LIST SIZE:" + Integer.toString(eventlist.size()), Toast.LENGTH_SHORT).show();
                now = (cal.get(Calendar.HOUR_OF_DAY) * 60) + cal.get(Calendar.MINUTE);
                for (int indx = 0; indx < eventlist.size(); indx++) {
                    Log.i("SERVICE", "in list");
                    Toast.makeText(getApplication(), "IN LIST", Toast.LENGTH_SHORT).show();
                    boolean Eventend=false;
                    if (eventlist.get(indx).CheckMatches(now) && eventlist.get(indx).CHECK_DATE_MATCHES()) {
                        Log.i("SERVICE", "in event");
                        Toast.makeText(getApplication(), "IN EVENT NOW", Toast.LENGTH_SHORT).show();
                        event_id = eventlist.get(indx).getParseid();
                        GPStrker gp=new GPStrker(context);
                        //if gps kid is on
                        if (gp.canGetLocation())
                        {x = gp.getLatitude();y = gp.getLongitude();}
                        else
                        {gp.showSettingsAlert();}

                        if(x!=eventlist.get(indx).getLatitude()||y!=eventlist.get(indx).getLongitude()){
                            Toast.makeText(getApplication(), "not right location", Toast.LENGTH_SHORT).show();
                            SEND_PUSH_TO_PARENT();
                            final SharedPreferences prefernces = PreferenceManager.getDefaultSharedPreferences(this);
                            final SharedPreferences.Editor editor = prefernces.edit();
                            float longtime=prefernces.getFloat("timeout", 0);
                            longtime+=0.5;
                            editor.putFloat("timeout",longtime).apply();

                        }

                        if(eventlist.get(indx).Convertendtoint()+1==now){
                            LENGTH_OF_TIME_EVENT=eventlist.get(indx).Convertendtoint()-eventlist.get(indx).Convertstarttoint();
                            CREATE_EVENT_REPORT();
                        }
                    }
                }
            }
        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    public void SEND_PUSH_TO_PARENT() {
        UsersDataSource db = new UsersDataSource(context);
        db.open();
        final ArrayList<User> list = db.getAllUsers();
        db.close();
        String parentid = "";
        for (int indx = 0; indx < list.size(); indx++) {
            if (list.get(indx).isPerant()) {
                parentid = list.get(indx).getUserParseID();
            }
        }
        final String PID = parentid;
        JSONObject data = new JSONObject();
        try {
            data.put("status", "warning");
            data.put("KID_ID", list.get(0).getUserParseID());
            //      ?
            data.put("KID_LOCATION_X", x);
            //      ?
            data.put("KID_LOCATION_Y", y);
            data.put("EVENT_ID", event_id);
            data.put("FULL_DATE", full_date[0]);
            data.put("KID_NAME", list.get(0).getFirstname());
        } catch (JSONException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        ParseQuery pushQuery = ParseInstallation.getQuery();
        pushQuery.whereEqualTo("channels", PID); // Set the channel
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery);// Set our Installation query
        push.setData(data);
        push.sendInBackground();
    }

    public void CREATE_EVENT_REPORT(){
        final SharedPreferences prefernces = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = prefernces.edit();
        LENGTH_OF_TIME_STAY=prefernces.getFloat("timeout", 0);
        editor.putFloat("timeout",0).apply();
        UsersDataSource db = new UsersDataSource(context);
        db.open();
        final ArrayList<User> list = db.getAllUsers();
        db.close();
        final ParseObject REPORT = new ParseObject("EventReport");
        REPORT.put("Event_ID",event_id);
        REPORT.put("Kid_ID",list.get(0).getUserParseID());
        REPORT.put("LengthEvent",LENGTH_OF_TIME_EVENT);
        REPORT.put("LengthStay",LENGTH_OF_TIME_STAY);
        REPORT.put("Date",full_date[0]);
        REPORT.saveInBackground();

    }
}
