package com.parse.starter;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Chat_ConversationWindow extends Fragment {
    static protected ChatArrayAdapter abp;
    private ListView list;
    private EditText chattext;
    private Button send;
    private Button back;
    private UsersDataSource UserDB;//user database
    static protected User cont_user;
    static protected Calendar cal;
    private static MessageDataSourse MessageDB;//user database


    static protected String ChatRoom_ID="";
    static protected String FriendFirstName="";
    static protected String FParseID="";
    static protected Bitmap FriendImage;

    static protected Fragment me;
    private String userMessage;
    private String MY_ID;
    protected static Thread mythread;
    private static boolean threadRunning;
    private boolean side = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Main2Activity.Chatwinopen=true;
        MessageDB=new MessageDataSourse(getActivity());
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
        ((ImageView) myView.findViewById(R.id.ConversationUserImage)).setImageBitmap(cont_user.getUserImage());
        //set friend first name
        ((TextView)myView.findViewById(R.id.user_name_chat_win)).setText(cont_user.getUserName());
        //open old message
        FParseID=cont_user.getUserParseID();
        Toast.makeText(getActivity(),FParseID,Toast.LENGTH_LONG).show();
        //GetingHistoryChat(FriendParseObjectID, MY_ID);
        //checking every 2.5 seconds new message
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
                Main2Activity.Lest_Fram = Main2Activity.current_Fram;
                Main2Activity.current_Fram = Main2Activity.ChatList_Fram;
                FragmentTransaction mytransaction2 = Main2Activity.myFramManager.beginTransaction();
                mytransaction2.remove(Main2Activity.Lest_Fram);
                mytransaction2.show(Main2Activity.current_Fram);
                mytransaction2.commit();
                Main2Activity.Chatwinopen=false;
                Chat_Contact_List.fulllist(getActivity());
            }
        });
        Log.i("check1", cont_user.getChatroom() + "/" + cont_user.getUserParseID() + "/" + MY_ID);
        OpenHistoryFromPhoneDB(cont_user.getChatroom(), cont_user.getUserParseID(), MY_ID);
        //checkingmessage(cont_user.getUserParseID(), MY_ID, cont_user.getChatroom());
        Thread thread =new Thread(){
            public void run(){
                while (true){

                }

            }
        };

        return myView;
    }
    private void send(){
        UserDB = new UsersDataSource(getActivity());
        UserDB.open();
        final List<User> users = UserDB.getAllUsers();
        UserDB.close();
        final Message newmessageDB=new Message();
        final ParseObject newmessage = new ParseObject("Message");
        newmessage.put("Sender",MY_ID);
        newmessage.put("Receiver", cont_user.getUserParseID());
        newmessage.put("txt", userMessage);
        newmessage.put("Read", false);
        newmessage.put("Time", cal.get(Calendar.HOUR_OF_DAY) + ":" +
                ((cal.get(Calendar.MINUTE) < 10) ? "0" + cal.get(Calendar.MINUTE) : cal.get(Calendar.MINUTE)));
        newmessage.put("Chat_ID", cont_user.getChatroom());
        newmessage.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    MessageDB = new MessageDataSourse(getActivity());
                    MessageDB.open();
                    newmessageDB.setDate(cal.get(Calendar.HOUR_OF_DAY) + ":" +
                            ((cal.get(Calendar.MINUTE) < 10) ? "0" + cal.get(Calendar.MINUTE) : cal.get(Calendar.MINUTE)));
                    newmessageDB.setSender(MY_ID);
                    newmessageDB.setReceiver(cont_user.getUserParseID());
                    newmessageDB.setMessage(userMessage);
                    newmessageDB.setChatRoom_ID(cont_user.getChatroom());
                    newmessageDB.setSide(false);
                    newmessageDB.setParseid(newmessage.getObjectId());
                    MessageDB.CreateNewMessage(newmessageDB);
                    MessageDB.close();
                    abp.add(newmessageDB);
                    JSONObject data = new JSONObject();
                    try {
                        data.put("alert",cont_user.getUserName());
                        data.put("title","new message from:");
                        data.put("SENDER",MY_ID);
                        data.put("RECEIVER",cont_user.getUserParseID());
                        data.put("DATE", newmessageDB.getDate());
                        data.put("CHATROOMID",newmessageDB.getChatRoom_ID());
                        data.put("TXT",newmessageDB.getMessage());
                        data.put("PARSEID",newmessageDB.getParseid());

                        data.put("badge", "Increment");
                        data.put("sound", "cheering.caf");
                    } catch (JSONException e2) {
                        // TODO Auto-generated catch block
                        e2.printStackTrace();
                    }
                    ParseQuery pushQuery = ParseInstallation.getQuery();
                    pushQuery.whereEqualTo("channels", cont_user.getUserParseID()); // Set the channel
                    ParsePush push = new ParsePush();
                    push.setQuery(pushQuery);// Set our Installation query
                    push.setMessage(users.get(0).getUserName() + " send you new message");
                    push.setData(data);
                    push.sendInBackground();
                    chattext.setText("");

                } else {
                    Toast.makeText(getActivity(),"PROBLOEM",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void checkingmessage(final String friend,final String me,final String chatRoomID){
        threadRunning=true;
        mythread = new Thread() {
            public void run() {
                while (threadRunning) {
                    try {
                        ParseQuery<ParseObject> GetMessageTable = ParseQuery.getQuery("Message");
                        GetMessageTable.whereMatches("Chat_ID", chatRoomID);
                        GetMessageTable.whereEqualTo("Read", false);
                        GetMessageTable.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> object, ParseException e) {
                                if (e == null) {
                                    for (int indx = 0; indx < object.size(); indx++) {
                                        if (!object.get(indx).getBoolean("Read")) {
                                            MessageDB = new MessageDataSourse(getActivity());
                                            MessageDB.open();
                                            ArrayList<Message> allmess=new ArrayList<Message>();
                                            allmess=MessageDB.getAllMessage(chatRoomID);
                                            MessageDB.close();
                                            boolean exist=false;
                                            for(int indx3=0;indx3<allmess.size();indx3++){
                                                if(allmess.get(indx3).getParseid().matches(object.get(indx).getObjectId())){
                                                    exist=true;
                                                }
                                            }
                                            if(exist) {
                                                final int indx2 = indx;
                                                ParseObject parseObject = new ParseObject("Message");
                                                Message newmessage = new Message();
                                                newmessage.setDate(object.get(indx2).getString("Time"));
                                                newmessage.setSender(object.get(indx2).getString("Sender"));
                                                newmessage.setReceiver(object.get(indx2).getString("Receiver"));
                                                newmessage.setMessage(object.get(indx2).getString("txt"));
                                                newmessage.setChatRoom_ID(chatRoomID);
                                                newmessage.setParseid(object.get(indx2).getObjectId());
                                                if (object.get(indx2).getString("Receiver").matches(me) && object.get(indx2).getString("Sender").matches(friend)) {
                                                    newmessage.setSide(true);
                                                    object.get(indx2).put("Read", true);
                                                    MessageDB = new MessageDataSourse(getActivity());
                                                    MessageDB.open();
                                                    if (MessageDB.CreateNewMessage(newmessage)) {
                                                        abp.add(newmessage);
                                                    }
                                                    MessageDB.close();
                                                    object.get(indx2).saveInBackground(new SaveCallback() {
                                                        @Override
                                                        public void done(ParseException e) {
                                                            if (e == null) {

                                                            }
                                                        }
                                                    });
                                                } else {
                                                    MessageDB = new MessageDataSourse(getActivity());
                                                    MessageDB.open();
                                                    newmessage.setSide(false);
                                                    if (MessageDB.CreateNewMessage(newmessage)) {
                                                        abp.add(newmessage);
                                                    }
                                                    MessageDB.close();
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        });
                        Thread.sleep(3500);
                    }catch (InterruptedException ex) {
                    }
                }
            }
        };mythread.start();
    }
    //open history chat from parse
    private void OpenHistoryFromPhoneDB(String chatroom,String Friend,String Me){
        Log.i("checkin message",chatroom+"/"+Friend+"/"+Me);
        MessageDB=new MessageDataSourse(getActivity());
        MessageDB.open();
        ArrayList<Message> allmessage=MessageDB.getAllMessage(chatroom);
        MessageDB.close();
        if(allmessage.size()>0){
            Log.i("showmessage", allmessage.get(0).getMessage());
            for(int indx=0;indx<allmessage.size();indx++){
                if(allmessage.get(indx).getReceiver().matches(Friend)&& allmessage.get(indx).getSender().matches(Me)){
                    allmessage.get(indx).setSide(false);
                    Message mss=new Message();
                    mss=allmessage.get(indx);
                    mss.setReade(true);
                    MessageDB=new MessageDataSourse(getActivity());
                    MessageDB.open();
                    MessageDB.UpdateMessage(mss);
                    MessageDB.close();
                    abp.add(mss);
                    Log.i("showmessage",mss.getMessage() );
                }
                else if (allmessage.get(indx).getReceiver().matches(Me)&& allmessage.get(indx).getSender().matches(Friend)) {
                    allmessage.get(indx).setSide(true);
                    Message mss=new Message();
                    mss=allmessage.get(indx);
                    mss.setReade(true);
                    MessageDB=new MessageDataSourse(getActivity());
                    MessageDB.open();
                    MessageDB.UpdateMessage(mss);
                    MessageDB.close();
                    Log.i("showmessage",mss.getMessage() );
                    abp.add(mss);
                }
            }

        }else {
            Toast.makeText(getActivity(),"empty",Toast.LENGTH_SHORT).show();
        }
    }
}
