package com.parse.starter;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin on 12/02/2016.
 */
public class JSONCustomReceiver extends ParsePushBroadcastReceiver  {
    private final String TAG = "PUSH_NOTIF";
    static public boolean RECIEV_LOCATION=false;
    @Override
    public void onPushOpen(Context context, Intent intent) {
        Log.i(TAG, "onPushOpen triggered!");
        Intent i = new Intent(context, Main2Activity.class);
        i.putExtras(intent.getExtras());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
    @Override
    public void onPushReceive(Context context, Intent intent) {
        Log.i(TAG, "onPushReceive triggered!");

        JSONObject pushData;
        String alert = null;
        String title = null;
        try {
            pushData = new JSONObject(intent.getStringExtra(JSONCustomReceiver.KEY_PUSH_DATA));
            title = pushData.getString("status");
            //CHECKING PUSH STATUS


            if(title.matches("message")) {// in case its message push
                Message receivemess = new Message();
                receivemess.setParseid(pushData.getString("PARSEID"));
                receivemess.setChatRoom_ID(pushData.getString("CHATROOMID"));
                receivemess.setReceiver(pushData.getString("RECEIVER"));
                receivemess.setSender(pushData.getString("SENDER"));
                receivemess.setDate(pushData.getString("DATE"));
                receivemess.setMessage(pushData.getString("TXT"));
                receivemess.setSide(true);
                Log.i("savingfromreceiver", receivemess.getMessage() + "/" + receivemess.getParseid());
                MessageDataSourse mmDB = new MessageDataSourse(context);
                mmDB.open();
                mmDB.CreateNewMessage(receivemess);
                mmDB.close();
                if (Main2Activity.isInForeground && Main2Activity.ContactList) {
                    Chat_Contact_List.fulllist(Main2Activity.Mainact);
                    if (Main2Activity.Chatwinopen) {
                        if (Chat_ConversationWindow.cont_user.getUserParseID().matches(receivemess.getSender())) {
                            Chat_ConversationWindow.abp.add(receivemess);
                            receivemess.setReade(true);
                            mmDB.open();
                            mmDB.UpdateMessage(receivemess);
                            mmDB.close();
                        } else {
                        }

                    } else {

                    }
                }
            }else if(title.matches("warning")){
                WarningMessage warningMessage=new WarningMessage();
                warningMessage.setKid_name(pushData.getString("KID_NAME"));
                warningMessage.setKid_Parse_id(pushData.getString("KID_ID"));
                warningMessage.setEvent_parse_id(pushData.getString("EVENT_ID"));
                warningMessage.setLoc_X(pushData.getDouble("KID_LOCATION_X"));
                warningMessage.setLoc_y(pushData.getDouble("KID_LOCATION_Y"));
                warningMessage.setDate(pushData.getString("FULL_DATE"));
                warningMessage.setWarning_parse_id(pushData.getString("WAR_P_ID"));
            }else if(title.matches("editevent")){

                Event event=new Event();
                event.setParseid(pushData.getString("EVENT_PARSE_ID"));
                event.setName(pushData.getString("EVENT_NAME"));
                event.setAddress(pushData.getString("ADDRESS"));
                event.setDay(pushData.getString("DAY"));
                event.setDate(pushData.getString("DATE"));
                event.setStart_time(pushData.getString("START"));
                event.setEnd_time(pushData.getString("END"));
                event.setKidID(pushData.getString("KID_ID"));
                EventDataSource EDB=new EventDataSource(context);
                EDB.open();
                if(EDB.CreateNewEvent(event));
                else EDB.UpdateEvent(event);
                EDB.close();

            }else if(title.matches("KID_LOCATION")) {
                if(Main2Activity.isInForeground&&Location.LOCATIONISON){
                    Location.Latitude2=pushData.getDouble("KID_LOCATION_X");
                    Location.Longitude2=pushData.getDouble("KID_LOCATION_Y");
                    RECIEV_LOCATION=true;

                }
            }else if(title.matches("GET_LOCATION")){
                Double x=0.0;
                Double y=0.0;
                GPStrker gp=new GPStrker(context);
                //if gps kid is on
                if (gp.canGetLocation()) {
                    x = gp.getLatitude();
                    y = gp.getLongitude();
                }else{
                    gp.showSettingsAlert();

                }
                    String Parent="";
                UsersDataSource UDB=new UsersDataSource(context);
                UDB.open();
                ArrayList<User> usersList=UDB.getAllUsers();
                for(int indx=0;indx<usersList.size();indx++){
                    if(usersList.get(indx).isPerant()){
                        Parent=usersList.get(indx).getUserParseID();
                    }
                }
                JSONObject data = new JSONObject();
                try {
                    data.put("status", "KID_LOCATION");
                    data.put("KID_ID",usersList.get(0).getUserParseID());
                    data.put("KID_LOCATION_X", x);
                    data.put("KID_LOCATION_Y", y);
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
                ParseQuery pushQuery = ParseInstallation.getQuery();
                pushQuery.whereEqualTo("channels", Parent); // Set the channel
                ParsePush push = new ParsePush();
                push.setQuery(pushQuery);// Set our Installation query
                push.setData(data);
                push.sendInBackground();

            }else if(title.matches("DeletEvent")){
                String parseid=pushData.getString("EVENT_PARSE_ID");
                EventDataSource EDB=new EventDataSource(context);
                EDB.open();
                EDB.DeleteEventByparseID(parseid);
                EDB.close();
            }
        } catch (JSONException e) {}
        Log.i(TAG,"alert is " + alert);
        Log.i(TAG,"title is " + title);

        Intent cIntent = new Intent(JSONCustomReceiver.ACTION_PUSH_OPEN);
        cIntent.putExtras(intent.getExtras());
        cIntent.setPackage(context.getPackageName());

        // WE SHOULD HANDLE DELETE AS WELL
        // BUT IT'S NOT HERE TO SIMPLIFY THINGS!

        PendingIntent pContentIntent = PendingIntent.getBroadcast(context, 0 /*just for testing*/, cIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        if(title.matches("message")) {
            if (Main2Activity.isInForeground && Main2Activity.Chatwinopen) {
                builder
                        .setSound(alarmSound)
                        .setAutoCancel(true);
            } else {
                builder
                        .setSmallIcon(R.drawable.messageicon)
                        .setContentTitle(alert)
                        .setContentText(title)
                        .setContentIntent(pContentIntent)
                        .setSound(alarmSound)
                        .setAutoCancel(true);
            }
        }else if(title.matches("warning")){
            builder
                    .setSmallIcon(R.drawable.alarmicom)
                    .setContentTitle(alert)
                    .setContentText(title)
                    .setContentIntent(pContentIntent)
                    .setSound(alarmSound)
                    .setAutoCancel(true);

        }else if(title.matches("editevent")){
            builder
                    .setAutoCancel(true);
        }
        NotificationManager myNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        myNotificationManager.notify(1, builder.build());
    }
}