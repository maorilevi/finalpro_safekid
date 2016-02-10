package com.parse.starter;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Chat_ConversationWindow extends Fragment {
    private ChatArrayAdapter abp;
    private ListView list;
    private EditText chattext;
    private Button send;
    private Button back;
    private UsersDataSource UserDB;//user database
    static protected User cont_user;
    static protected Calendar cal;

    static protected String FriendFirstName="";
    static protected String FriendParseObjectID="";
    static protected Bitmap FriendImage;

    static protected Fragment me;
    private String userMessage;
    private String MY_ID;
    private static Thread mythread;
    private static boolean threadRunning;
    private boolean side = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Main.conwin=true;
        View myView=inflater.inflate(R.layout.chat_conversation_win, container, false);
        cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"));
        cal.getTime().toGMTString();
        UserDB = new UsersDataSource(getActivity());
        UserDB.open();
        List<User> users = UserDB.getAllUsers();
        MY_ID=users.get(0).getUserParseID();
        UserDB.close();
        //set friend image
        ((ImageView)myView.findViewById(R.id.ConversationUserImage)).setImageBitmap(FriendImage);
        //set friend first name
        ((TextView)myView.findViewById(R.id.user_name_chat_win)).setText(FriendFirstName);
        //open old message
        GetingHistoryChat(FriendParseObjectID, MY_ID);
        //checking every 2.5 seconds new message
        checkingmessage(FriendParseObjectID, MY_ID);
        send = (Button) myView.findViewById(R.id.btnsend);
        list = (ListView) myView.findViewById(R.id.listmessage);
        abp = new ChatArrayAdapter(getActivity(), R.layout.chat_single_message);
        chattext = (EditText) myView.findViewById(R.id.chat);
        //*send message --------------------------------------------------------------
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!chattext.getText().toString().isEmpty()) {
                    //update message on parse
                    userMessage = chattext.getText().toString();
                    send();
                }
            }
        });
        list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        list.setAdapter(abp);
        abp.registerDataSetObserver(new DataSetObserver() {
            public void OnChanged() {
                super.onChanged();
                list.setSelection(abp.getCount() - 1);
            }
        });
        back=(Button)myView.findViewById(R.id.cht2list);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Main.Lest_Fram=Main.current_Fram;
                Main.current_Fram=Main.ChatList_Fram;
                FragmentTransaction mytransaction2 = Main.mymanager.beginTransaction();
                mytransaction2.remove(Main.Lest_Fram);
                mytransaction2.show(Main.current_Fram);
                mytransaction2.commit();
            }
        });
        return myView;
    }


    //*sanding function ...
    private boolean sendChatMessage() {
        abp.add(new Message(side, chattext.getText().toString()+"\n"+ cal.get(Calendar.HOUR_OF_DAY)+":"
                +((cal.get(Calendar.MINUTE)<10)?"0"+cal.get(Calendar.MINUTE):cal.get(Calendar.MINUTE))));
        chattext.setText("");
        //*side = !side;
        return true;
    }
    private void send(){
        UserDB = new UsersDataSource(getActivity());
        UserDB.open();
        List<User> users = UserDB.getAllUsers();
        UserDB.close();
        sendChatMessage();
        ParseObject newmessage = new ParseObject("Message");
        newmessage.put("Sender",MY_ID);
        newmessage.put("Receiver",FriendParseObjectID);
        newmessage.put("txt", userMessage);
        newmessage.put("Read", false);
        newmessage.put("Time", cal.get(Calendar.HOUR_OF_DAY)+":"+
                ((cal.get(Calendar.MINUTE)<10)?"0"+cal.get(Calendar.MINUTE):cal.get(Calendar.MINUTE)));
        newmessage.saveInBackground();
        ParseQuery pushQuery = ParseInstallation.getQuery();
        pushQuery.whereEqualTo("channels", FriendParseObjectID); // Set the channel
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery);// Set our Installation query
        push.setMessage(users.get(0).getUserName() + " send you new message");
        push.sendInBackground();

    }
    private void checkingmessage(final String friend,final String me){
        threadRunning=true;
        mythread = new Thread() {
            public void run() {
                while (threadRunning) {
                    try {
                        ParseQuery<ParseObject> GetMessageTable = ParseQuery.getQuery("Message");
                        GetMessageTable.whereMatches("Sender",friend);
                        GetMessageTable.whereMatches("Receiver", me);
                        GetMessageTable.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> object, ParseException e) {
                                if(e==null){
                                    for (int indx = 0; indx < object.size(); indx++) {
                                        if(!object.get(indx).getBoolean("Read")){
                                            if(object.get(indx).getString("Sender").matches(friend))
                                            object.get(indx).put("Read",true);
                                            object.get(indx).saveInBackground();
                                            abp.add(new Message(!side,object.get(indx).getString("txt") +"\n"
                                                    +object.get(indx).getString("Time")));
                                        }
                                    }
                                }
                            }
                        });
                        Thread.sleep(2500);
                    }catch (InterruptedException ex) {
                    }
                }
            }
        };mythread.start();
    }
    //open history chat from parse
    private void GetingHistoryChat(final String friend,final String me){


        ParseQuery<ParseObject> GetMessageTable = ParseQuery.getQuery("Message");
        GetMessageTable.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> object, ParseException e) {
                if(e==null){
                    for (int indx = 0; indx < object.size(); indx++) {
                        //if its my message
                        if(object.get(indx).getString("Receiver").matches(friend)&&
                                object.get(indx).getString("Sender").matches(me)){
                            abp.add(new Message(side, object.get(indx).getString("txt")+"\n"+object.get(indx).getString("Time")));
                        }
                        else {
                            if (object.get(indx).getString("Receiver").matches(me) &&
                                    object.get(indx).getString("Sender").matches(friend)) {
                                abp.add(new Message(!side, object.get(indx).getString("txt") + "\n" + object.get(indx).getString("Time")));
                            }
                        }
                    }
                }
            }
        });
    }
}
