package com.parse.starter;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Chat_Contact_List extends Fragment {
    private UsersDataSource UserDB;//user database

    private static Singel_User_Array_Adapter singeluserAdapArry;
    private ListView list;
    private Button backTomenu;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.chat__contact__list, container, false);
        //connect list and adapter ....
        list=(ListView)myView.findViewById(R.id.chat_contect_list);
        singeluserAdapArry=new Singel_User_Array_Adapter(getActivity(), R.layout.singel_person);
        list.setAdapter(singeluserAdapArry);
        //open contact list withe the function-fullList .....
        fulllist();
        return myView;
    }

    //open contact list.....................
    static public int fulllist() {
        singeluserAdapArry.clear();
        for(int indx=1;indx<Main.users.size();indx++){
            singeluserAdapArry.add(new Singel_User(true,Main.users.get(indx).getUserImage(), Main.users.get(indx).getFirstname(),
                    Main.users.get(indx).getUserParseID()));
        }
        //UserDB.close();
        return Main.users.size();
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    //start adapter class............................................//////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
    public class Singel_User_Array_Adapter extends ArrayAdapter<Singel_User> {
        private TextView User_Name;
        private ImageView UserImage;
        private List<Singel_User> Contact_List = new ArrayList<Singel_User>();
        private RelativeLayout layout;

        public Singel_User_Array_Adapter(Context context, int chat) {
            super(context, chat);
        }
        public void add(Singel_User object) {
            Contact_List.add(object);
            super.add(object);
        }
        public int getCount() {
            return this.Contact_List.size();
        }
        public Singel_User getItem(int index) {
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
            final Singel_User userobj = getItem(position);
            User_Name = (TextView) v.findViewById(R.id.singel_user_name);
            UserImage=(ImageView)v.findViewById(R.id.SingleUserImage);
            UserImage.setImageBitmap(userobj.UserImage);
            User_Name.setText(userobj.UserName);
            layout.setGravity(Gravity.LEFT);//object side
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment newMessage = new Chat_ConversationWindow();
                    FragmentTransaction mytransaction2 = Main.mymanager.beginTransaction();
                    Main.Lest_Fram = Main.current_Fram;
                    Main.current_Fram = newMessage;
                    mytransaction2.hide(Main.Lest_Fram);
                    mytransaction2.add(R.id.MainRelative, Main.current_Fram, "newMessage");
                    mytransaction2.commit();
                    Chat_ConversationWindow.FriendParseObjectID = userobj.getUserParseID();
                    Chat_ConversationWindow.FriendFirstName = userobj.getUserName();
                    Chat_ConversationWindow.FriendImage=userobj.getUserImage();
                }
            });
            return v;
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////
    //End adapter class..............................................//////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////
}
