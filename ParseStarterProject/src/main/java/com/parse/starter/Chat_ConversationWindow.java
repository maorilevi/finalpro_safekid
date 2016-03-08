package com.parse.starter;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.database.DataSetObserver;
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

    private String userMessage;
    private String MY_ID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Main2Activity.Chatwinopen = true;
        MessageDB = new MessageDataSourse(getActivity());
        //Main.conwin=true;
        View myView = inflater.inflate(R.layout.chat_conversation_win, container, false);
        if(Main2Activity.MainActionBar.isShowing()){
            Main2Activity.MainActionBar.hide();
        }
        cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"));
        cal.getTime().toGMTString();
        UserDB = new UsersDataSource(getActivity());
        UserDB.open();
        List<User> users = UserDB.getAllUsers();
        MY_ID = users.get(0).getUserParseID();
        UserDB.close();
        //set friend image
        ((ImageView) myView.findViewById(R.id.ConversationUserImage)).setImageBitmap(cont_user.getUserImage());
        //set friend first name
        ((TextView) myView.findViewById(R.id.user_name_chat_win)).setText(cont_user.getUserName());
        //open old message
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
        back = (Button) myView.findViewById(R.id.cht2list);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Main2Activity.Lest_Fram = Main2Activity.current_Fram;
                Main2Activity.current_Fram = Main2Activity.ChatList_Fram;
                FragmentTransaction mytransaction2 = Main2Activity.myFramManager.beginTransaction();
                mytransaction2.remove(Main2Activity.Lest_Fram);
                mytransaction2.show(Main2Activity.current_Fram);
                mytransaction2.commit();
                Main2Activity.Chatwinopen = false;
                Chat_Contact_List.fulllist(getActivity());
                if (!Main2Activity.MainActionBar.isShowing()) {
                    Main2Activity.MainActionBar.show();
                }
            }
        });
        Log.i("check1", cont_user.getChatroom() + "/" + cont_user.getUserParseID() + "/" + MY_ID);
        OpenHistoryFromPhoneDB(cont_user.getChatroom(), cont_user.getUserParseID(), MY_ID);

        return myView;
    }
    private void send() {
        final String[] time=cal.getTime().toGMTString().split("GMT");
        UserDB = new UsersDataSource(getActivity());
        UserDB.open();
        final List<User> users = UserDB.getAllUsers();
        UserDB.close();
        final Message newmessageDB = new Message();
        final ParseObject newmessage = new ParseObject("Message");
        newmessage.put("Sender", MY_ID);
        newmessage.put("Receiver", cont_user.getUserParseID());
        newmessage.put("txt", userMessage);
        newmessage.put("Read", false);
        newmessage.put("Time",time[0]);
        newmessage.put("Chat_ID", cont_user.getChatroom());
        newmessageDB.setDate(time[0]);
        newmessageDB.setSender(MY_ID);
        newmessageDB.setReceiver(cont_user.getUserParseID());
        newmessageDB.setMessage(userMessage);
        newmessageDB.setChatRoom_ID(cont_user.getChatroom());
        newmessageDB.setSide(false);
        abp.add(newmessageDB);
        chattext.setText("");
        newmessage.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    MessageDB = new MessageDataSourse(getActivity());
                    MessageDB.open();
                    newmessageDB.setParseid(newmessage.getObjectId());
                    MessageDB.CreateNewMessage(newmessageDB);
                    MessageDB.close();
                    JSONObject data = new JSONObject();
                    try {
                        data.put("sendername", cont_user.getUserName());
                        data.put("status", "message");
                        data.put("SENDER", MY_ID);
                        data.put("RECEIVER", cont_user.getUserParseID());
                        data.put("DATE", newmessageDB.getDate());
                        data.put("CHATROOMID", newmessageDB.getChatRoom_ID());
                        data.put("TXT", newmessageDB.getMessage());
                        data.put("PARSEID", newmessageDB.getParseid());
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
                } else {
                    Toast.makeText(getActivity(), "connection problem", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //open history chat from parse
    private void OpenHistoryFromPhoneDB(String chatroom, String Friend, String Me) {
        Log.i("checkin message", chatroom + "/" + Friend + "/" + Me);
        MessageDB = new MessageDataSourse(getActivity());
        MessageDB.open();
        ArrayList<Message> allmessage = MessageDB.getAllMessage(chatroom);
        MessageDB.close();
        if (allmessage.size() > 0) {
            Log.i("showmessage", allmessage.get(0).getMessage());
            for (int indx = 0; indx < allmessage.size(); indx++) {
                if (allmessage.get(indx).getReceiver().matches(Friend) && allmessage.get(indx).getSender().matches(Me)) {
                    allmessage.get(indx).setSide(false);
                    Message mss = new Message();
                    mss = allmessage.get(indx);
                    mss.setReade(true);
                    MessageDB = new MessageDataSourse(getActivity());
                    MessageDB.open();
                    MessageDB.UpdateMessage(mss);
                    MessageDB.close();
                    abp.add(mss);
                    Log.i("showmessage", mss.getMessage());
                } else if (allmessage.get(indx).getReceiver().matches(Me) && allmessage.get(indx).getSender().matches(Friend)) {
                    allmessage.get(indx).setSide(true);
                    Message mss = new Message();
                    mss = allmessage.get(indx);
                    mss.setReade(true);
                    MessageDB = new MessageDataSourse(getActivity());
                    MessageDB.open();
                    MessageDB.UpdateMessage(mss);
                    MessageDB.close();
                    Log.i("showmessage", mss.getMessage());
                    abp.add(mss);
                }
            }
        } else {
            Toast.makeText(getActivity(), "empty", Toast.LENGTH_SHORT).show();
        }
    }
}