package com.parse.starter;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 23/01/2016.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class LogIn extends Fragment {
    private static MessageDataSourse MessageDB;//user database
    private UsersDataSource UserDB;//user database
    private Button signupbtn;
    private Button loginbtn;
    EditText Password;
    EditText UserName;
    String UserName_STR = "";
    String UserPass_STR = "";
    static protected boolean reternfromparse=false;
    static protected boolean login=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View LoginView = inflater.inflate(R.layout.login, container, false);
        MessageDB=new MessageDataSourse(getActivity());
        signupbtn=new Button(getActivity());
        signupbtn=(Button)LoginView.findViewById(R.id.sig_up_btn);
        UserName = (EditText) LoginView.findViewById(R.id.Login_User_Name);
        Password = (EditText) LoginView.findViewById(R.id.Login_User_Password);
        loginbtn =new Button(getActivity());
        loginbtn =(Button)LoginView.findViewById(R.id.go_btn);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserName_STR = UserName.getText().toString();
                UserPass_STR = Password.getText().toString();
                Toast.makeText(getActivity(), UserName_STR + "--" + UserPass_STR, Toast.LENGTH_SHORT).show();
                if (!(UserName_STR.isEmpty() || UserPass_STR.isEmpty())) {
                    ParseQuery<ParseObject> GetMessageTable = ParseQuery.getQuery("USER");
                    GetMessageTable.whereMatches("User_Name", UserName_STR);
                    GetMessageTable.whereMatches("Password", UserPass_STR);
                    GetMessageTable.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(final List<ParseObject> Listobject, ParseException e) {
                            if (e == null) {
                                for (int indx1 = 0; indx1 < Listobject.size(); indx1++) {
                                    ParsePush.subscribeInBackground(Listobject.get(indx1).getObjectId());
                                    final int finalIndx = indx1;
                                    //SAVE USER DETAILS IN APP DATABASE
                                    ParseFile fileObject = (ParseFile) Listobject.get(indx1).get("Image");
                                    fileObject.getDataInBackground(new GetDataCallback() {
                                        public void done(byte[] data, ParseException e2) {
                                            if (e2 == null) {
                                                UserDB = new UsersDataSource(getActivity());
                                                UserDB.open();
                                                UserDB.DeleteAllUsers();
                                                Main2Activity.parent = Listobject.get(finalIndx).getBoolean("Parent");
                                                Bitmap btm = BitmapFactory.decodeFile(Main2Activity.imgDecodableString);
                                                btm = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                User user = new User();
                                                user.setUserImage(btm);
                                                user.setLestName(Listobject.get(finalIndx).getString("F_name"));
                                                user.setFirstname(Listobject.get(finalIndx).getString("L_name"));
                                                user.setUserName(Listobject.get(finalIndx).getString("User_Name"));
                                                user.setPassword(Listobject.get(finalIndx).getString("Password"));
                                                user.setBirthday(Listobject.get(finalIndx).getString("Birthday"));
                                                user.setAddress(Listobject.get(finalIndx).getString("Address"));
                                                user.setPerant(Listobject.get(finalIndx).getBoolean("Parent"));
                                                user.setPhoneNumber(Listobject.get(finalIndx).getString("Phone_Number"));
                                                user.setUserParseID(Listobject.get(finalIndx).getObjectId());
                                                user.setEmail(Listobject.get(finalIndx).getString("Email"));
                                                UserDB.createUSER(user);
                                                UserDB.close();
                                                Main2Activity.Mainuserlist.add(user);
                                                login=true;
                                            }
                                        }
                                    });
                                    Toast.makeText(getActivity(), "wellcom", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "user not exist", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
                    getusersdetails();
                }
            }
        });
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction mytransaction2 = Main2Activity.myFramManager.beginTransaction();
                mytransaction2.hide(Main2Activity.Login_Fram);
                mytransaction2.show(Main2Activity.SignUp_Fram);
                mytransaction2.commit();
            }
        });
        return LoginView;
    }
    public void getusersdetails() {
        final boolean[] run = {true};
        Thread thread = new Thread() {
            public void run() {
                while (run[0]) {
                    try {
                        sleep(1000);
                        Log.i("openfram", "get all user details ");
                        if (login) {

                            UsersDataSource udb = new UsersDataSource(getActivity());
                            udb.open();
                            final ArrayList<User> userlist = udb.getAllUsers();
                            udb.close();
                            //check user status--------------------------------------------
                            if (userlist.get(0).isPerant()) {
                                ParseQuery<ParseObject> connectiontable = ParseQuery.getQuery("Parent_Kid");
                                connectiontable.whereEqualTo("Parent_ID", userlist.get(0).getUserParseID());
                                ParseQuery<ParseObject> usertable = ParseQuery.getQuery("USER");
                                usertable.whereMatchesKeyInQuery("objectId", "Kid_ID", connectiontable);
                                usertable.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(final List<ParseObject> objects, ParseException e3) {
                                        if (e3 == null) {
                                            for (int indx = 0; indx < objects.size(); indx++) {
                                                ParseFile fileObject = (ParseFile) objects.get(indx).get("Image");
                                                final int finalIndx3 = indx;
                                                fileObject.getDataInBackground(new GetDataCallback() {
                                                    public void done(byte[] data, ParseException e4) {
                                                        if (e4 == null) {
                                                            UserDB = new UsersDataSource(getActivity());
                                                            UserDB.open();
                                                            Bitmap btm = BitmapFactory.decodeFile(Main2Activity.imgDecodableString);
                                                            btm = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                            User user = new User();
                                                            user.setUserImage(btm);
                                                            user.setFirstname(objects.get(finalIndx3).getString("F_name"));
                                                            user.setLestName(objects.get(finalIndx3).getString("L_name"));
                                                            user.setUserName(objects.get(finalIndx3).getString("User_Name"));
                                                            //user.setPassword(objects.get(indx1).getString("Password"));
                                                            user.setBirthday(objects.get(finalIndx3).getString("Birthday"));
                                                            user.setAddress(objects.get(finalIndx3).getString("Address"));
                                                            user.setPerant(objects.get(finalIndx3).getBoolean("Parent"));
                                                            user.setPhoneNumber(objects.get(finalIndx3).getString("Phone_Number"));
                                                            user.setUserParseID(objects.get(finalIndx3).getObjectId());
                                                            user.setEmail(objects.get(finalIndx3).getString("Email"));
                                                            UserDB.createUSER(user);
                                                            UserDB.close();
                                                            Main2Activity.Mainuserlist.add(user);
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }
                                });
                            } else {
                                //in case user is kid
                                ParseQuery<ParseObject> connectiontable1 = ParseQuery.getQuery("Parent_Kid");
                                connectiontable1.whereMatches("Kid_ID", userlist.get(0).getUserParseID());
                                ParseQuery<ParseObject> parnttable = ParseQuery.getQuery("USER");
                                parnttable.whereMatchesKeyInQuery("objectId", "Parent_ID", connectiontable1);
                                parnttable.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(final List<ParseObject> objects, ParseException e5) {
                                        if (e5 == null) {
                                            for (int indx = 0; indx < objects.size(); indx++) {
                                                ParseFile fileObject = (ParseFile) objects.get(indx).get("Image");
                                                final int finalIndx1 = indx;
                                                fileObject.getDataInBackground(new GetDataCallback() {
                                                    public void done(byte[] data, ParseException e6) {
                                                        if (e6 == null) {
                                                            UserDB = new UsersDataSource(getActivity());
                                                            UserDB.open();
                                                            Bitmap btm = BitmapFactory.decodeFile(Main2Activity.imgDecodableString);
                                                            btm = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                            User user = new User();
                                                            user.setUserImage(btm);
                                                            user.setLestName(objects.get(finalIndx1).getString("F_name"));
                                                            user.setFirstname(objects.get(finalIndx1).getString("L_name"));
                                                            user.setUserName(objects.get(finalIndx1).getString("User_Name"));
                                                            //user.setPassword(Listobject.get(indx1).getString("Password"));
                                                            user.setBirthday(objects.get(finalIndx1).getString("Birthday"));
                                                            user.setAddress(objects.get(finalIndx1).getString("Address"));
                                                            user.setPerant(objects.get(finalIndx1).getBoolean("Parent"));
                                                            user.setPhoneNumber(objects.get(finalIndx1).getString("Phone_Number"));
                                                            user.setUserParseID(objects.get(finalIndx1).getObjectId());
                                                            user.setEmail(objects.get(finalIndx1).getString("Email"));
                                                            UserDB.createUSER(user);
                                                            UserDB.close();
                                                            Main2Activity.Mainuserlist.add(user);
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }
                                });
                                ParseQuery<ParseObject> connectiontable2 = ParseQuery.getQuery("Parent_Kid");
                                connectiontable2.whereMatchesKeyInQuery("Parent_ID", "Parent_ID", connectiontable1);
                                ParseQuery<ParseObject> usertable = ParseQuery.getQuery("USER");
                                usertable.whereMatchesKeyInQuery("objectId", "Kid_ID", connectiontable2);
                                usertable.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(final List<ParseObject> object, ParseException e7) {
                                        if (e7 == null) {
                                            for (int indx = 0; indx < object.size(); indx++) {
                                                if (object.get(indx).getObjectId() != userlist.get(0).getUserParseID()) {
                                                    ParseFile fileObject = (ParseFile) object.get(indx).get("Image");
                                                    final int finalIndx1 = indx;
                                                    fileObject.getDataInBackground(new GetDataCallback() {
                                                        public void done(byte[] data, ParseException e8) {
                                                            if (e8 == null) {
                                                                UserDB = new UsersDataSource(getActivity());
                                                                UserDB.open();
                                                                Bitmap btm = BitmapFactory.decodeFile(Main2Activity.imgDecodableString);
                                                                btm = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                                User user = new User();
                                                                user.setUserImage(btm);
                                                                user.setLestName(object.get(finalIndx1).getString("F_name"));
                                                                user.setFirstname(object.get(finalIndx1).getString("L_name"));
                                                                user.setUserName(object.get(finalIndx1).getString("User_Name"));
                                                                //user.setPassword(Listobject.get(indx1).getString("Password"));
                                                                user.setBirthday(object.get(finalIndx1).getString("Birthday"));
                                                                user.setAddress(object.get(finalIndx1).getString("Address"));
                                                                user.setPerant(object.get(finalIndx1).getBoolean("Parent"));
                                                                user.setPhoneNumber(object.get(finalIndx1).getString("Phone_Number"));
                                                                user.setUserParseID(object.get(finalIndx1).getObjectId());
                                                                user.setEmail(object.get(finalIndx1).getString("Email"));
                                                                UserDB.createUSER(user);
                                                                UserDB.close();
                                                                Main2Activity.Mainuserlist.add(user);
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                            getchatids();
                            openfram();
                            sleep(1000);
                            run[0] = false;
                        }
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }
    public void getchatids() {
        Log.i("openfram", "getchatids");
        final boolean[] run = {true};
        Thread thread=new Thread(){
            public void run(){
                while (run[0]){
                    try {
                        sleep(1000);
                        UsersDataSource Udb=new UsersDataSource(getActivity());
                        Udb.open();
                        final ArrayList<User> users = Udb.getAllUsers();
                        Udb.close();
                        final ArrayList<User> myuserList=users;
                        if(!reternfromparse){
                            Log.i("openfram", Integer.toString(myuserList.size()));
                            for (int usindx = 1; usindx < myuserList.size(); usindx++) {
                                final int cpyusindx = usindx;
                                ArrayList<String> starray = new ArrayList<String>();
                                starray.add(myuserList.get(0).getUserParseID());
                                starray.add(myuserList.get(usindx).getUserParseID());
                                ParseQuery<ParseObject> connectiontable = ParseQuery.getQuery("ChatRoom");
                                connectiontable.whereContainedIn("usersid", starray);
                                connectiontable.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(final List<ParseObject> objects, ParseException e3) {
                                        if (e3 == null) {
                                            for (int indx = 0; indx < objects.size(); indx++) {
                                                UserDB.open();
                                                myuserList.get(cpyusindx).setChatroom(objects.get(indx).getObjectId());
                                                UserDB.UpdateUser(myuserList.get(cpyusindx));
                                                UserDB.close();
                                                checkingmessage(myuserList.get(cpyusindx).getUserParseID(),
                                                        myuserList.get(0).getUserParseID(),objects.get(indx).getObjectId());
                                            }
                                            reternfromparse = true;
                                            run[0]=false;
                                            if (myuserList.get(0).isPerant()) {
                                                Main2Activity.navigationView.inflateMenu(R.menu.activity_main2_drawer);
                                            } else {
                                                Main2Activity.navigationView.inflateMenu(R.menu.kidmenu);
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        };thread.start();
    }
    public void openfram(){
        final boolean[] run = {true};
        Thread thread=new Thread(){
            public void run(){
                while (run[0]){
                    try {
                        sleep(2000);
                        Log.i("openfram", "befor reternfromparse");
                        if(reternfromparse){
                            Log.i("openfram", "lunch from login");
                            UserDB = new UsersDataSource(getActivity());
                            UserDB.open();
                            Main2Activity.Mainuserlist=UserDB.getAllUsers();
                            UserDB.close();
                            Main2Activity.ChatList_Fram = new Chat_Contact_List();
                            Main2Activity.Schedule_Fram = new Schedule_Mange();
                            Main2Activity.Setting_Fram = new Setting_Fram();
                            FragmentTransaction mytransaction2 = Main2Activity.myFramManager.beginTransaction();
                            mytransaction2.hide(Main2Activity.SignUp_Fram);
                            mytransaction2.hide(Main2Activity.Login_Fram);
                            mytransaction2.add(R.id.MainRelative, Main2Activity.ChatList_Fram, "ChatList_Fram");
                            Log.i("openfram", "ChatList_Fram");
                            mytransaction2.add(R.id.MainRelative, Main2Activity.Schedule_Fram, "ChatList_Fram");
                            Log.i("openfram", "ChatList_Fram");
                            mytransaction2.add(R.id.MainRelative, Main2Activity.Setting_Fram, "Setting_Fram");
                            Log.i("openfram", "Setting_Fram");
                            mytransaction2.add(R.id.MainRelative, Main2Activity.Addkid_Fram, "Addkid_Fram");
                            Log.i("openfram", "Addkid_Fram");
                            mytransaction2.hide(Main2Activity.Addkid_Fram);
                            mytransaction2.hide(Main2Activity.Schedule_Fram);
                            mytransaction2.hide(Main2Activity.Setting_Fram);
                            mytransaction2.hide(Main2Activity.ChatList_Fram);
                            if (Main2Activity.Mainuserlist.size() == 1) {
                                Main2Activity.current_Fram = Main2Activity.Addkid_Fram;
                            } else {
                                Main2Activity.current_Fram = Main2Activity.ChatList_Fram;
                            }
                            mytransaction2.show(Main2Activity.current_Fram);
                            mytransaction2.commit();
                            sleep(2000);
                            run[0] =false;
                        }
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        };thread.start();
    }

    private void checkingmessage(final String friend, final String me, final String chatRoomID) {
        ParseQuery<ParseObject> GetMessageTable = ParseQuery.getQuery("Message");
        GetMessageTable.whereMatches("Chat_ID", chatRoomID);
        GetMessageTable.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> object, ParseException e) {
                if (e == null) {
                    for (int indx = 0; indx < object.size(); indx++) {
                        MessageDB = new MessageDataSourse(getActivity());
                        MessageDB.open();
                        ArrayList<Message> allmess = new ArrayList<Message>();
                        allmess = MessageDB.getAllMessage(chatRoomID);
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
                            newmessage.setChatRoom_ID(chatRoomID);
                            newmessage.setParseid(object.get(indx2).getObjectId());
                            if (object.get(indx2).getString("Receiver").matches(me) && object.get(indx2).getString("Sender").matches(friend)) {
                                newmessage.setSide(true);
                                object.get(indx2).put("Read", true);
                                MessageDB = new MessageDataSourse(getActivity());
                                MessageDB.open();
                                MessageDB.CreateNewMessage(newmessage);
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
                                MessageDB.CreateNewMessage(newmessage);
                                MessageDB.close();
                            }
                        }
                    }
                }
            }
        });
    }
}
