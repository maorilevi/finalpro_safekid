package com.parse.starter;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 23/01/2016.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class LogIn extends Fragment {
    private UsersDataSource UserDB;//user database

    Button Go;
    EditText Password;
    EditText UserName;
    String UserName_STR = "";
    String UserPass_STR = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View LoginView = inflater.inflate(R.layout.login, container, false);

        UserName = (EditText) LoginView.findViewById(R.id.Login_User_Name);
        Password = (EditText) LoginView.findViewById(R.id.Login_User_Password);

        LoginView.findViewById(R.id.go_btn).setOnClickListener(new View.OnClickListener() {
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
                                                Main2Activity.parent=Listobject.get(finalIndx).getBoolean("Parent");
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
                                            }
                                        }
                                    });
                                    Toast.makeText(getActivity(), "wellcom", Toast.LENGTH_SHORT).show();
                                    //check user status--------------------------------------------
                                    if (Listobject.get(indx1).getBoolean("Parent")) {
                                        ParseQuery<ParseObject> connectiontable = ParseQuery.getQuery("Parent_Kid");
                                        connectiontable.whereEqualTo("Parent_ID", Listobject.get(indx1).getObjectId());
                                        ParseQuery<ParseObject> usertable = ParseQuery.getQuery("USER");
                                        usertable.whereMatchesKeyInQuery("objectId", "Kid_ID", connectiontable);
                                        final int finalIndx1 = indx1;
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
                                        connectiontable1.whereMatches("Kid_ID", Listobject.get(indx1).getObjectId());
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
                                                        if (object.get(indx).getObjectId() != Listobject.get(finalIndx).getObjectId()) {
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
                                }
                                getchatids();
                            } else {
                                Toast.makeText(getActivity(), "user not exist", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
                }
            }
        });
        return LoginView;
    }
    public void getchatids(){
        UserDB = new UsersDataSource(getActivity());
        UserDB.open();
        final ArrayList<User> users=UserDB.getAllUsers();
        UserDB.close();
        for(int usindx=1;usindx<users.size();usindx++){
            final int cpyusindx=usindx;
            ArrayList<String> starray=new ArrayList<String>();
            starray.add(users.get(0).getUserParseID());
            starray.add(users.get(usindx).getUserParseID());
            ParseQuery<ParseObject> connectiontable = ParseQuery.getQuery("ChatRoom");
            connectiontable.whereContainedIn("usersid", starray);
            connectiontable.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(final List<ParseObject> objects, ParseException e3) {
                    if (e3 == null) {
                        for (int indx = 0; indx < objects.size(); indx++) {
                            UserDB.open();
                            users.get(cpyusindx).setChatroom(objects.get(indx).getObjectId());
                            UserDB.UpdateUser(users.get(cpyusindx));
                            UserDB.close();
                        }
                    }
                }
            });
        }
    }
}
