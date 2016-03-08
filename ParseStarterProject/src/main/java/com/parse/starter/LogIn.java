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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 23/01/2016.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class LogIn extends Fragment {

    UPDATE_DETAILS_CLASS MY_UPDATE=new UPDATE_DETAILS_CLASS();

    private static MessageDataSourse MessageDB;//user database
    private UsersDataSource UserDB;//user database
    private Button signupbtn;
    private Button loginbtn;

    EditText Password;
    EditText UserName;
    String UserName_STR = "";
    String UserPass_STR = "";
    static protected boolean login=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View LoginView = inflater.inflate(R.layout.login, container, false);
        MessageDB=new MessageDataSourse(getActivity());
        signupbtn=new Button(getActivity());
        signupbtn= (Button)LoginView.findViewById(R.id.sig_up_btn);
        UserName = (EditText) LoginView.findViewById(R.id.Login_User_Name);
        Password = (EditText) LoginView.findViewById(R.id.Login_User_Password);
        loginbtn = new Button(getActivity());
        loginbtn = (Button)LoginView.findViewById(R.id.go_btn);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserName_STR = UserName.getText().toString();
                UserPass_STR = Password.getText().toString();
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
                                                user.setFamily_id(Listobject.get(finalIndx).getString("FAMILY_ID"));
                                                UserDB.createUSER(user);
                                                UserDB.close();
                                                Main2Activity.Mainuserlist.add(user);
                                                login = true;
                                                UserName.setText("");
                                                Password.setText("");
                                                FragmentTransaction mytransaction2 = Main2Activity.myFramManager.beginTransaction();
                                                mytransaction2.hide(Main2Activity.Login_Fram);
                                                mytransaction2.show(Main2Activity.Splach_Fram);
                                                mytransaction2.commit();
                                            }
                                        }
                                    });
                                    Toast.makeText(getActivity(), "wellcom", Toast.LENGTH_SHORT).show();
                                    Log.i("LOGINSTATUS", "wellcom");
                                }
                            } else {
                                Toast.makeText(getActivity(), "user not exist", Toast.LENGTH_SHORT).show();
                                Log.i("LOGINSTATUS", "user not exist");

                            }
                        }

                    });

                    MY_UPDATE.UPDATE_ALL_DETAILS_AND_OPEN_FRAGMENT(getActivity(), true, true);
                    UserDB = new UsersDataSource(getActivity());
                    UserDB.open();
                    ArrayList<User> users=UserDB.getAllUsers();
                    if(users.size()>0){
                        if(users.get(0).isPerant()){
                            Main2Activity.navigationView.inflateMenu(R.menu.activity_main2_drawer);

                        }else{
                            Main2Activity.navigationView.inflateMenu(R.menu.kidmenu);

                        }
                    }
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
}
