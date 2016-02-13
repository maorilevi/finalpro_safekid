package com.parse.starter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Chat_Contact_List extends Fragment {
    static protected UsersDataSource UserDB;//user database

    protected static Singel_User_Array_Adapter singeluserAdapArry;
    private ListView list;
    private Button backTomenu;
    static protected RelativeLayout contectrel;
    static protected ScrollView contectscroll;
    static protected ArrayList<User> contUser=new ArrayList<User>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        fulllist(getActivity());
        return myView;
    }

    //open contact list.....................
    static public int fulllist(final Activity activity) {
        final boolean[] run = {true};
        Thread thread=new Thread(){
            public void run(){
                while (run[0]){
                    try {
                        sleep(1000);
                        run[0] =false;
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        };thread.start();
        UserDB=new UsersDataSource(activity);
        UserDB.open();
        final ArrayList<User> allusers =UserDB.getAllUsers();
        UserDB.close();
        contectrel.removeAllViews();
        for(int Uindx=1;Uindx<allusers.size();Uindx++){
            final int CpyUndx=Uindx;
            final Singel_User singelUser=new Singel_User();
            singelUser.setUserImage(allusers.get(Uindx).getUserImage());
            singelUser.setUserName(allusers.get(Uindx).getFirstname());
            singelUser.setUserParseID(allusers.get(Uindx).getUserParseID());
            LinearLayout mainlayout = new LinearLayout(activity);
            mainlayout.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout name_and_lest_message = new LinearLayout(activity);
            name_and_lest_message.setOrientation(LinearLayout.VERTICAL);
            name_and_lest_message.setLayoutParams(new ViewGroup.
                    LayoutParams((int) (contectrel.getWidth() * 0.6), ViewGroup.LayoutParams.MATCH_PARENT));
            ImageView userImage = new ImageView(activity);
            userImage.setImageBitmap(allusers.get(CpyUndx).getUserImage());

            TextView userName = new TextView(activity);
            userName.setText(allusers.get(CpyUndx).getUserName());
            userName.setTextSize(30);
            TextView LestMessage = new TextView(activity);
            LestMessage.setTextSize(30);
            TextView messagetime = new TextView(activity);
            messagetime.setTextSize(30);
            MessageDataSourse mmdb = new MessageDataSourse(activity);
            mmdb.open();
            ArrayList<Message> mmlist = mmdb.getAllMessage(allusers.get(CpyUndx).getChatroom());
            mmdb.close();
            if (mmlist.size() > 0) {
                LestMessage.setText(mmlist.get(mmlist.size() - 1).getMessage());
                if(!mmlist.get(mmlist.size()-1).isReade())
                    LestMessage.setTypeface(null, Typeface.BOLD);
                messagetime.setText(mmlist.get(mmlist.size() - 1).getDate());
                messagetime.setTypeface(null,Typeface.BOLD_ITALIC);
            } else {
                LestMessage.setText("");
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
            contectrel.addView(mainlayout);
        }
        //UserDB.close();
        return Main2Activity.Mainuserlist.size();
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    //start adapter class............................................//////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    public class Singel_User_Array_Adapter extends ArrayAdapter<User> {
        private TextView User_Name;
        private ImageView UserImage;
        private TextView messcount;
        private TextView LestMess;
        private TextView TimeMess;
        private List<User> Contact_List = new ArrayList<User>();
        private RelativeLayout layout;

        public Singel_User_Array_Adapter(Context context, int chat) {
            super(context, chat);
        }
        public void add(User object) {
            Contact_List.add(object);
            super.add(object);
        }
        public int getCount() {
            return this.Contact_List.size();
        }
        public User getItem(int index) {
            return this.Contact_List.get(index);
        }
        public View getView(int position, View ConvertView, ViewGroup perent) {
            View v = ConvertView;
            if (v == null) {
                LayoutInflater infleter = (LayoutInflater) this.getContext().
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = infleter.inflate(R.layout.singel_person, perent, false);
            }
            layout = (RelativeLayout) v.findViewById(R.id.user);
            final User userobj = getItem(position);
            LestMess=(TextView)v.findViewById(R.id.LestMessage);
            TimeMess=(TextView)v.findViewById(R.id.timelestmessage);
            messcount=(TextView)v.findViewById(R.id.MessCount);
            User_Name = (TextView) v.findViewById(R.id.singel_user_name);
            UserImage=(ImageView)v.findViewById(R.id.SingleUserImage);
            messcount.setText(Integer.toString(userobj.getMessCounter()));
            UserImage.setImageBitmap(userobj.UserImage);
            User_Name.setText(userobj.UserName);
            layout.setGravity(Gravity.LEFT);//object side
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment newMessage = new Chat_ConversationWindow();
                    FragmentTransaction mytransaction2 = Main2Activity.myFramManager.beginTransaction();
                    Main2Activity.Lest_Fram = Main2Activity.current_Fram;
                    Main2Activity.current_Fram = newMessage;
                    mytransaction2.hide(Main2Activity.Lest_Fram);
                    mytransaction2.add(R.id.MainRelative, Main2Activity.current_Fram, "newMessage");
                    mytransaction2.commit();
                    Chat_ConversationWindow.cont_user=userobj;
                    messcount.setText("0");
                    userobj.setMessCounter(0);
                }
            });
            return v;
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    //End adapter class..............................................//////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
}
