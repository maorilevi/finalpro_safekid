package com.parse.starter;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Admin on 12/02/2016.
 */
public class JSONCustomReceiver extends ParsePushBroadcastReceiver  {
    private final String TAG = "PUSH_NOTIF";

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
            alert = pushData.getString("alert");
            title = pushData.getString("title");
            Message receivemess=new Message();

            receivemess.setParseid(pushData.getString("PARSEID"));
            receivemess.setChatRoom_ID(pushData.getString("CHATROOMID"));
            receivemess.setReceiver(pushData.getString("RECEIVER"));
            receivemess.setSender(pushData.getString("SENDER"));
            receivemess.setDate(pushData.getString("DATE"));
            receivemess.setMessage(pushData.getString("TXT"));
            receivemess.setSide(true);
            Log.i("savingfromreceiver", receivemess.getMessage() + "/" + receivemess.getParseid());
            MessageDataSourse mmDB=new MessageDataSourse(context);
            mmDB.open();
            mmDB.CreateNewMessage(receivemess);
            mmDB.close();
            if(Main2Activity.isInForeground&&Main2Activity.Chatwinopen){
                if(Chat_ConversationWindow.cont_user.getUserParseID().matches(receivemess.getSender())){
                    Chat_ConversationWindow.abp.add(receivemess);
                    receivemess.setReade(true);
                    mmDB.open();
                    mmDB.UpdateMessage(receivemess);
                    mmDB.close();
                }else{
                }
            }
        } catch (JSONException e) {}

        Log.i(TAG,"alert is " + alert);
        Log.i(TAG,"title is " + title);

        Intent cIntent = new Intent(JSONCustomReceiver.ACTION_PUSH_OPEN);
        cIntent.putExtras(intent.getExtras());
        cIntent.setPackage(context.getPackageName());

        // WE SHOULD HANDLE DELETE AS WELL
        // BUT IT'S NOT HERE TO SIMPLIFY THINGS!

        PendingIntent pContentIntent =
                PendingIntent.getBroadcast(context, 0 /*just for testing*/, cIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        if(Main2Activity.isInForeground&&Main2Activity.Chatwinopen){
            builder
                    .setSound(alarmSound)
                    .setAutoCancel(true);
        }else{
            builder
                    .setSmallIcon(R.drawable.icon)
                    .setContentTitle(alert)
                    .setContentText(title)
                    .setContentIntent(pContentIntent)
                    .setSound(alarmSound)
                    .setAutoCancel(true);
        }
        NotificationManager myNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        myNotificationManager.notify(1, builder.build());
    }
}