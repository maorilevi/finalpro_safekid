package com.parse.starter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 28/02/2016.
 */
public class UPDATE_DETAILS_CLASS {
    private int COUNT_CHAT_ROOM_ID=0;
    private int COUNT_USERS=0;
    private boolean UPDATE_USER=false;
    private boolean UPDATE_EVENT=false;

    public UPDATE_DETAILS_CLASS() {
    }

    public void UPDATE_USERS(final Context context) {
        Log.i("USER_UPDATE", "IN FUNC");

        final boolean[] run = {true};
        Thread thread=new Thread(){
            public void run(){
                while (run[0]){
                    try {
                        sleep(500);
                        COUNT_USERS=0;
                        final UsersDataSource UserDB = new UsersDataSource(context);
                        UserDB.open();
                        final ArrayList<User> users = UserDB.getAllUsers();
                        UserDB.close();
                        if(users.size()>0){
                            run[0]=false;
                            Log.i("USER_UPDATE", "IN FUNC 2");
                            ParseQuery<ParseObject> TABLE = ParseQuery.getQuery("USER");
                            TABLE.whereMatches("FAMILY_ID", users.get(0).Family_id);
                            TABLE.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(final List<ParseObject> objects, ParseException e) {
                                    if (e == null) {
                                        Log.i("USER_UPDATE", "IN PARSE LIST SIZE" + Integer.toString(objects.size()));
                                        COUNT_USERS=objects.size();
                                        for (int indx = 0; indx < objects.size(); indx++) {
                                            Log.i("USER_UPDATE", "IN PARSE 2");
                                            ParseFile fileObject = objects.get(indx).getParseFile("Image");
                                            final int finalIndx1 = indx;
                                            fileObject.getDataInBackground(new GetDataCallback() {
                                                public void done(byte[] data, ParseException e2) {
                                                    if (e2 == null) {
                                                        Bitmap btm = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                        UsersDataSource UserDB = new UsersDataSource(context);
                                                        UserDB.open();
                                                        User user = new User();
                                                        user.setUserImage(btm);
                                                        user.setFirstname(objects.get(finalIndx1).getString("F_name"));
                                                        user.setLestName(objects.get(finalIndx1).getString("L_name"));
                                                        user.setUserName(objects.get(finalIndx1).getString("User_Name"));
                                                        user.setAddress(objects.get(finalIndx1).getString("Address"));
                                                        user.setBirthday(objects.get(finalIndx1).getString("Birthday"));
                                                        user.setPhoneNumber(objects.get(finalIndx1).getString("Phone_Number"));
                                                        user.setEmail(objects.get(finalIndx1).getString("Email"));
                                                        user.setPerant(objects.get(finalIndx1).getBoolean("Parent"));
                                                        user.setUserParseID(objects.get(finalIndx1).getObjectId());
                                                        user.setPassword(objects.get(finalIndx1).getString("Password"));
                                                        if (UserDB.CHECK_IF_EXIST(user.getUserParseID())) {
                                                            UserDB.UpdateUser(user);
                                                            Log.i("USER_UPDATE", "USER NAME:" + user.getFirstname());
                                                        } else {
                                                            UserDB.createUSER(user);
                                                            Log.i("USER_CREATE", "USER NAME:" + user.getFirstname());
                                                        }
                                                        UserDB.close();
                                                        UPDATE_USER = true;
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            });
                        }

                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }


    public void CHECK_THE_MESSAGES(final Context context,final String CHAT_ROOM_ID,final String ME,final String FRIEND){
        Log.i("CHECK_MESSAGE", "IN FUNCTION");
        ParseQuery<ParseObject> GetMessageTable = ParseQuery.getQuery("Message");
        GetMessageTable.whereMatches("Chat_ID", CHAT_ROOM_ID);
        GetMessageTable.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> object, ParseException e) {
                if (e == null) {
                    Log.i("CHECK_MESSAGE", "IN PARSE");
                    for (int indx = 0; indx < object.size(); indx++) {
                        MessageDataSourse MessageDB = new MessageDataSourse(context);
                        MessageDB.open();
                        ArrayList<Message> allmess = new ArrayList<Message>();
                        allmess = MessageDB.getAllMessage(CHAT_ROOM_ID);
                        MessageDB.close();
                        boolean exist = false;
                        for (int indx3 = 0; indx3 < allmess.size(); indx3++) {
                            if (allmess.get(indx3).getParseid().matches(object.get(indx).getObjectId())) {
                                exist = true;
                            }
                        }
                        if (!exist) {
                            final int indx2 = indx;
                            Message newmessage = new Message();
                            newmessage.setDate(object.get(indx2).getString("Time"));
                            newmessage.setSender(object.get(indx2).getString("Sender"));
                            newmessage.setReceiver(object.get(indx2).getString("Receiver"));
                            newmessage.setMessage(object.get(indx2).getString("txt"));
                            newmessage.setChatRoom_ID(CHAT_ROOM_ID);
                            newmessage.setParseid(object.get(indx2).getObjectId());
                            if (object.get(indx2).getString("Receiver").matches(ME) &&
                                    object.get(indx2).getString("Sender").matches(FRIEND)) {
                                newmessage.setSide(true);
                                object.get(indx2).put("Read", true);
                                MessageDB = new MessageDataSourse(context);
                                MessageDB.open();
                                MessageDB.CreateNewMessage(newmessage);
                                MessageDB.close();
                                object.get(indx2).saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                        } else {

                                        }
                                    }
                                });
                            } else {
                                MessageDB = new MessageDataSourse(context);
                                MessageDB.open();
                                newmessage.setSide(false);
                                MessageDB.CreateNewMessage(newmessage);
                                MessageDB.close();
                            }
                        }
                    }
                }
            }
        });
    }
    public void UPDATE_CHAT_ROOM_ID(final Context context) {
        COUNT_CHAT_ROOM_ID = 0;
        final boolean[] run = {true};
        Thread thread = new Thread() {
            public void run() {
                while (run[0]) {
                    try {
                        sleep(500);
                        final UsersDataSource UserDB = new UsersDataSource(context);
                        UserDB.open();
                        final ArrayList<User> users = UserDB.getAllUsers();
                        UserDB.close();
                        if (UPDATE_USER&&COUNT_USERS==users.size()) {
                            Log.i("GET_CHAT_ID", "IN FUNCTION");
                            Log.i("GET_CHAT_ID", Integer.toString(users.size()));
                            if (users.size() > 1) {
                                run[0] = false;
                                final String FIRST_USER_ID = users.get(0).getUserParseID();
                                Log.i("GET_CHAT_ID", "IN IF-SIZE");
                                ParseQuery<ParseObject> table1 = ParseQuery.getQuery("Chat_User");
                                table1.whereMatches("User_ID", FIRST_USER_ID);
                                ParseQuery<ParseObject> table2 = ParseQuery.getQuery("Chat_User");
                                table2.whereMatchesKeyInQuery("ChatRoom_ID", "ChatRoom_ID", table1);
                                table2.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> objects, ParseException e) {
                                        if (e == null) {
                                            Log.i("GET_CHAT_ID", "PARSE LIST SIZE-"+Integer.toString(objects.size()));
                                            for (int P_INDX = 0, U_INDX = 1; P_INDX < objects.size() && U_INDX < users.size(); ) {

                                                String CHAT_ROOM_USER2 = objects.get(P_INDX).getString("ChatRoom_ID");
                                                String USER_ID = objects.get(P_INDX).getString("User_ID");
                                                Log.i("GET_CHAT_ID", "IN PARSE-"+CHAT_ROOM_USER2+"/UID:"+USER_ID+"/FIRSUSER:"+FIRST_USER_ID);
                                                if(FIRST_USER_ID!=USER_ID){
                                                    if (users.get(U_INDX).getUserParseID().matches(USER_ID)) {
                                                        Log.i("GET_CHAT_ID", "UPDATE:" + CHAT_ROOM_USER2 +
                                                                "-USER NAME:" + users.get(U_INDX).getFirstname());
                                                        UserDB.open();
                                                        users.get(U_INDX).setChatroom(CHAT_ROOM_USER2);
                                                        UserDB.UpdateUser(users.get(U_INDX));
                                                        CHECK_THE_MESSAGES(
                                                                context,
                                                                CHAT_ROOM_USER2,
                                                                USER_ID,
                                                                FIRST_USER_ID
                                                        );
                                                        COUNT_CHAT_ROOM_ID++;
                                                        Log.i("GET_CHAT_ID", "COUNT:" + Integer.toString(COUNT_CHAT_ROOM_ID));
                                                        UserDB.close();
                                                        U_INDX++;
                                                        P_INDX=0;
                                                    }else{
                                                        P_INDX++;
                                                    }
                                                }else{
                                                    P_INDX++;
                                                }
                                            }
                                        } else {

                                        }
                                    }
                                });
                            } else if (users.size() == 1) {
                                Log.i("GET_CHAT_ID", "one user");
                                run[0] = false;
                            }

                        }
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }
    public void UPDATE_ALL_EVENT(final Context context){
        ////
        UPDATE_EVENT=false;
        final boolean[] run = {true};
        Thread thread=new Thread(){
            public void run(){
                while (run[0]){
                    try {
                        sleep(500);
                        Log.i("UPDATE_EVENT", "RUN");
                        if(UPDATE_USER){
                            Log.i("UPDATE_EVENT", "IN FUNC");
                            final UsersDataSource UserDB = new UsersDataSource(context);
                            UserDB.open();
                            final ArrayList<User>  users= UserDB.getAllUsers();
                            UserDB.close();
                            ParseQuery<ParseObject> EventTable = ParseQuery.getQuery("Event");
                            if(users.get(0).isPerant()){
                                Log.i("UPDATE_EVENT", "PARENT IF");
                                ArrayList<String> kidsid=new ArrayList<String>();
                                for(int indx=1;indx<users.size();indx++) {
                                    kidsid.add(users.get(indx).getUserParseID());
                                }
                                EventTable.whereContainedIn("Kid_ID", kidsid);
                            }else{
                                EventTable.whereEqualTo("Kid_ID",users.get(0).getUserParseID());
                            }
                            if(users.size()>1){
                                EventTable.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(final List<ParseObject> results, ParseException e) {
                                        if (e == null) {
                                            if (results.size() > 0) {
                                                for (int indx = 0; indx < results.size(); indx++) {
                                                    Event event = new Event();
                                                    event.setParseid(results.get(indx).getObjectId());
                                                    event.setName(results.get(indx).getString("NameEvent"));
                                                    event.setAddress(results.get(indx).getString("Address"));
                                                    event.setStart_time(results.get(indx).getString("StartEvent"));
                                                    event.setEnd_time(results.get(indx).getString("EndEvent"));
                                                    event.setKidID(results.get(indx).getString("Kid_ID"));
                                                    event.setDay(results.get(indx).getString("Day"));
                                                    event.setDate(results.get(indx).getString("Date"));
                                                    EventDataSource eventDB = new EventDataSource(context);
                                                    eventDB.open();
                                                    if (eventDB.CreateNewEvent(event)) {
                                                    } else {
                                                        eventDB.UpdateEventFROMPARSE(event);
                                                    }
                                                    eventDB.close();
                                                }
                                            }
                                            Log.i("UPDATE_EVENT", "NO EVENT1");
                                            UPDATE_EVENT = true;
                                            run[0] = false;
                                        } else {
                                            Log.i("UPDATE_EVENT", "NO EVENT2");
                                            UPDATE_EVENT = true;
                                            run[0] = false;
                                        }
                                    }
                                });
                            }else if(users.size()==1){
                                Log.i("UPDATE_EVENT", "ONE USER");
                                run[0] =false;
                                UPDATE_EVENT = true;

                            }
                        }
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }

    public void UPDATE_ALL_DETAILS_AND_OPEN_FRAGMENT(final Activity activity, final boolean login,boolean openfram){
        UPDATE_USERS(activity);
        UPDATE_CHAT_ROOM_ID(activity);
        UPDATE_ALL_EVENT(activity);
        if(openfram){
            final boolean[] run = {true};
            Thread thread=new Thread(){
                public void run(){
                    while (run[0]){
                        try {
                            Log.i("OPENFRAM","LOGIN AND MAIN");
                            sleep(500);
                            final UsersDataSource UserDB = new UsersDataSource(activity);
                            UserDB.open();
                            final ArrayList<User>  users= UserDB.getAllUsers();
                            UserDB.close();
                            if(UPDATE_EVENT){
                                Log.i("OPENFRAM","UPDATE_EVENT=true");
                            }else{
                                Log.i("OPENFRAM","UPDATE_EVENT=false");
                            }
                            if(COUNT_CHAT_ROOM_ID==COUNT_USERS-1){
                                Log.i("OPENFRAM","COUNT_CHAT_ROOM_ID=true");
                            }else{
                                Log.i("OPENFRAM","COUNT_CHAT_ROOM_ID=false");
                            }
                            if(UPDATE_USER){
                                Log.i("OPENFRAM","UPDATE_USER=true");
                            }else{
                                Log.i("OPENFRAM","UPDATE_USER=false");
                            }
                            Log.i("OPENFRAM", "BEFOR IF:"+Integer.toString(COUNT_CHAT_ROOM_ID)+" LIST SIZE:"+Integer.toString(users.size()));
                            if (UPDATE_EVENT&&(COUNT_CHAT_ROOM_ID==COUNT_USERS-1)&&UPDATE_USER){
                                Log.i("OPENFRAM","LOGIN AND MAIN IN IF");
                                OPENFRAM(activity, login,users.size());
                                run[0] =false;
                            }
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            };
            thread.start();
        }else{
            final boolean[] run = {true};
            Thread thread=new Thread(){
                public void run(){
                    while (run[0]){
                        try {
                            Log.i("OPENFRAM", "ADD KID");
                            sleep(500);
                            final UsersDataSource UserDB = new UsersDataSource(activity);
                            UserDB.open();
                            final ArrayList<User>  users= UserDB.getAllUsers();
                            UserDB.close();
                            if(UPDATE_EVENT){
                                Log.i("OPENFRAM","UPDATE_EVENT=true");
                            }else{
                                Log.i("OPENFRAM","UPDATE_EVENT=false");
                            }
                            if(COUNT_CHAT_ROOM_ID==COUNT_USERS-1){
                                Log.i("OPENFRAM","COUNT_CHAT_ROOM_ID=true");
                            }else{
                                Log.i("OPENFRAM","COUNT_CHAT_ROOM_ID=false");
                            }
                            if(!UPDATE_USER){
                                Log.i("OPENFRAM","UPDATE_USER=true");
                            }else{
                                Log.i("OPENFRAM","UPDATE_USER=false");
                            }
                            if (UPDATE_EVENT&&(COUNT_CHAT_ROOM_ID==COUNT_USERS-1)&&UPDATE_USER){
                                Log.i("OPENFRAM","ADD KID IN IF");
                                Chat_Contact_List.fulllist(activity);
                                Schedule_Mange.SetImageeUsers(activity);
                                run[0] =false;
                            }
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            };
            thread.start();
        }
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void OPENFRAM(final Activity activity,final boolean login,final int user_list_size) {
        final boolean[] run = {true};
        new Thread() {
            public void run() {
                while (run[0]) {
                    try {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final UsersDataSource UserDB = new UsersDataSource(activity);
                                UserDB.open();
                                final ArrayList<User> users = UserDB.getAllUsers();
                                UserDB.close();
                                Log.i("openfram", "openfram");
                                Main2Activity.ChatList_Fram = new Chat_Contact_List();
                                Main2Activity.Schedule_Fram = new Schedule_Mange();
                                Main2Activity.Setting_Fram = new Setting();
                                Main2Activity.Location_Fram=new Location();
                                Main2Activity.statistic_Fram = new Statics_Fram();
                                FragmentTransaction mytransaction2 = Main2Activity.myFramManager.beginTransaction();
                                mytransaction2.add(R.id.MainRelative, Main2Activity.ChatList_Fram, "ChatList_Fram");
                                mytransaction2.add(R.id.MainRelative, Main2Activity.Schedule_Fram, "Schedule_Fram");
                                mytransaction2.add(R.id.MainRelative, Main2Activity.Setting_Fram, "Setting_Fram");
                                mytransaction2.add(R.id.MainRelative, Main2Activity.Addkid_Fram, "Addkid_Fram");
                                mytransaction2.add(R.id.MainRelative, Main2Activity.Location_Fram, "Location_Fram");

                                mytransaction2.add(R.id.MainRelative, Main2Activity.statistic_Fram, "statistic_Fram");
                                if (!login) {
                                    mytransaction2.add(R.id.MainRelative, Main2Activity.Login_Fram, "Login_Fram");
                                    mytransaction2.add(R.id.MainRelative, Main2Activity.SignUp_Fram, "SignUp_Fram");
                                }
                                mytransaction2.hide(Main2Activity.Location_Fram);
                                mytransaction2.hide(Main2Activity.statistic_Fram);
                                mytransaction2.hide(Main2Activity.Splach_Fram);
                                mytransaction2.hide(Main2Activity.Login_Fram);
                                mytransaction2.hide(Main2Activity.SignUp_Fram);
                                mytransaction2.hide(Main2Activity.Addkid_Fram);
                                mytransaction2.hide(Main2Activity.Splach_Fram);
                                mytransaction2.hide(Main2Activity.Schedule_Fram);
                                mytransaction2.hide(Main2Activity.Setting_Fram);
                                mytransaction2.hide(Main2Activity.ChatList_Fram);
                                if (user_list_size == 1) {
                                    Main2Activity.current_Fram = Main2Activity.Addkid_Fram;
                                } else {
                                    Main2Activity.current_Fram = Main2Activity.ChatList_Fram;
                                }
                                mytransaction2.show(Main2Activity.current_Fram);
                                mytransaction2.commit();
                                if (users.size() > 0) {
                                    if (users.get(0).isPerant()) {
                                        Main2Activity.navigationView.inflateMenu(R.menu.activity_main2_drawer);

                                    } else {
                                        Main2Activity.navigationView.inflateMenu(R.menu.kidmenu);
                                    }
                                }
                                run[0] = false;
                            }
                        });
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
