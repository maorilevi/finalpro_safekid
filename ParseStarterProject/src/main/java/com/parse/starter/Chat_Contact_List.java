package com.parse.starter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Chat_Contact_List extends Fragment {
    static protected UsersDataSource UserDB;//user database

    static protected RelativeLayout contectrel;
    static protected ScrollView contectscroll;
    static protected ArrayList<User> contUser=new ArrayList<User>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(!Main2Activity.MainActionBar.isShowing()){
            Main2Activity.MainActionBar.show();
        }
        UserDB=new UsersDataSource(getActivity());
        UserDB.open();
        contUser=UserDB.getAllUsers();
        UserDB.close();
        Main2Activity.ContactList=true;
        View myView = inflater.inflate(R.layout.chat__contact__list, container, false);
        //open contact list withe the function-fullList .....
        contectrel=new RelativeLayout(getActivity());
        contectrel=(RelativeLayout)myView.findViewById(R.id.relscrollcontect);
        contectscroll=new ScrollView(getActivity());
        contectscroll=(ScrollView)myView.findViewById(R.id.ScrollContect);
        fulllist(getActivity());
        return myView;
    }
    //open contact list.....................
    static public int fulllist(final Activity activity) {
        contectrel.removeAllViews();
        UserDB = new UsersDataSource(activity);
        UserDB.open();
        final ArrayList<User> allusers = UserDB.getAllUsers();
        UserDB.close();
        Log.i("openfram", "chat " + allusers.size());
        if (allusers.size() > 0) {
            Log.i("openfram", "chatlistcreate");
            for (int Uindx = 1; Uindx < allusers.size(); Uindx++) {
                Log.i("openfram", "in for");
                final int CpyUndx = Uindx;
                LinearLayout mainlayout = new LinearLayout(activity);
                mainlayout.setOrientation(LinearLayout.HORIZONTAL);
                //mainlayout.setLayoutParams(new ViewGroup.LayoutParams(Main2Activity.Mainwidth, mainlayout.getHeight()));
                LinearLayout name_and_lest_message = new LinearLayout(activity);
                name_and_lest_message.setOrientation(LinearLayout.VERTICAL);
                name_and_lest_message.setLayoutParams(new ViewGroup.
                        LayoutParams((int) (Main2Activity.Mainwidth * 0.5), ViewGroup.LayoutParams.MATCH_PARENT));
                ImageView userImage = new ImageView(activity);
                userImage.setImageBitmap(allusers.get(CpyUndx).getUserImage());
                userImage.setLayoutParams(new LinearLayout.LayoutParams((int) (Main2Activity.Mainwidth * 0.2),
                        ViewGroup.LayoutParams.MATCH_PARENT));
                //user name
                TextView userName = new TextView(activity);
                userName.setText(allusers.get(CpyUndx).getUserName());
                userName.setTypeface(null, Typeface.BOLD);
                userName.setTextColor(Color.BLACK);
                userName.setTextSize(20);
                //user lest message + time for lest message
                TextView LestMessage = new TextView(activity);
                LestMessage.setTextSize(20);
                TextView messagetime = new TextView(activity);
                messagetime.setTextSize(15);
                messagetime.setLayoutParams(new ViewGroup.LayoutParams((int) (Main2Activity.Mainwidth * 0.2),
                        ViewGroup.LayoutParams.MATCH_PARENT));
                MessageDataSourse mmdb = new MessageDataSourse(activity);
                mmdb.open();
                ArrayList<Message> mmlist = mmdb.getAllMessage(allusers.get(CpyUndx).getChatroom());
                mmdb.close();
                if (mmlist.size() > 0) {
                    LestMessage.setText(mmlist.get(mmlist.size() - 1).getMessage());
                    if (!mmlist.get(mmlist.size() - 1).isReade()) {
                        LestMessage.setTypeface(null, Typeface.BOLD);
                        LestMessage.setTextColor(Color.BLACK);
                    }
                    messagetime.setText(mmlist.get(mmlist.size() - 1).getDate());
                    messagetime.setTypeface(null, Typeface.BOLD_ITALIC);
                    messagetime.setTextColor(Color.BLACK);
                } else {
                    LestMessage.setText("Start to chat");
                    messagetime.setText("");
                }
                name_and_lest_message.addView(userName);
                name_and_lest_message.addView(LestMessage);
                mainlayout.addView(userImage);
                mainlayout.addView(name_and_lest_message);
                mainlayout.addView(messagetime);
                mainlayout.setBackgroundResource(R.drawable.singeluser);
                mainlayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fragment newMessage = new Chat_ConversationWindow();
                        FragmentTransaction mytransaction2 = Main2Activity.myFramManager.beginTransaction();
                        Main2Activity.Lest_Fram = Main2Activity.current_Fram;
                        Main2Activity.current_Fram = newMessage;
                        mytransaction2.hide(Main2Activity.Lest_Fram);
                        mytransaction2.add(R.id.MainRelative, Main2Activity.current_Fram, "newMessage");
                        mytransaction2.commit();
                        Chat_ConversationWindow.cont_user = allusers.get(CpyUndx);
                        allusers.get(CpyUndx).setMessCounter(0);
                    }
                });
                //setview(mainlayout);
                contectrel.addView(mainlayout);
            }
        }
        return Main2Activity.Mainuserlist.size();
    }
    public static void setview(LinearLayout layout){
        contectrel.addView(layout);
    }
}
