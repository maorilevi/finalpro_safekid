package com.parse.starter;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Add_Kid extends Fragment {

    private static Thread mythread;
    private static boolean threadRunning;


    private UsersDataSource UserDB;//user database

    private EditText First_Name;
    private EditText Last_Name;
    private EditText User_Name;
    private EditText Password;
    private EditText Confirm_Password;
    private EditText Phone_NUmber;
    private EditText Address;
    private EditText Email;
    private EditText Birthday;

    static protected String First_Name_STR="";
    static protected String Last_Name_STR;
    static protected  String User_Name_STR;
    static protected  String Password_STR;
    static protected  String Confirm_Password_STR;
    static protected  String Phone_NUmber_STR;
    static protected  String Address_STR;
    static protected  String Email_STR;
    static protected  String Birthday_STR;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View Kid=inflater.inflate(R.layout.add__kid, container, false);
        Main2Activity.Addkidscreen=true;
        //Saving al the kid details on parse kids Table..................
        Kid.findViewById(R.id.AddKid_SaveDetails).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                First_Name = (EditText) Kid.findViewById(R.id.AddKid_Fname);
                Last_Name = (EditText) Kid.findViewById(R.id.AddKid_Lname);
                User_Name = (EditText) Kid.findViewById(R.id.AddKid_UserName);
                Password = (EditText) Kid.findViewById(R.id.AddKid_Pass);
                Confirm_Password = (EditText) Kid.findViewById(R.id.AddKid_ConfPass);
                Phone_NUmber = (EditText) Kid.findViewById(R.id.AddKid_Phone);
                Address = (EditText) Kid.findViewById(R.id.AddKid_Address);
                Email = (EditText) Kid.findViewById(R.id.AddKid_Email);
                Birthday = (EditText) Kid.findViewById(R.id.AddKid_Birthday);
                //put all the user kid details value in string....
                First_Name_STR = First_Name.getText().toString();
                Last_Name_STR = Last_Name.getText().toString();
                User_Name_STR = User_Name.getText().toString();
                Password_STR = Password.getText().toString();
                Confirm_Password_STR = Confirm_Password.getText().toString();
                Phone_NUmber_STR = Phone_NUmber.getText().toString();
                Address_STR = Address.getText().toString();
                Email_STR = Email.getText().toString();
                Birthday_STR = Birthday.getText().toString();
                //checking password
                if (Password_STR.matches(Confirm_Password_STR)) {
                    //checking all the text box
                    if (!(First_Name_STR.isEmpty() || Last_Name_STR.isEmpty() || User_Name_STR.isEmpty()
                            || Password_STR.isEmpty() || Phone_NUmber_STR.isEmpty() || Address_STR.isEmpty()
                            || Email_STR.isEmpty() || Birthday_STR.isEmpty())) {
                        Bitmap bitmap = BitmapFactory.decodeFile(Main2Activity.imgDecodableString);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();

                        Bitmap resized = Bitmap.createScaledBitmap(bitmap, 100, 100, true);

                        resized.compress(Bitmap.CompressFormat.PNG, 10, stream);

                        byte[] image = stream.toByteArray();

                        ParseFile file = new ParseFile("androidbegin.png", image);
                        file.saveInBackground();
                        //create new kid rew in parse user table
                        final ParseObject addkid = new ParseObject("USER");
                        //set all details on parse
                        addkid.put("F_name", First_Name_STR);
                        addkid.put("L_name", Last_Name_STR);
                        addkid.put("User_Name", User_Name_STR);
                        addkid.put("Password", Password_STR);
                        addkid.put("Address", Address_STR);
                        addkid.put("Phone_Number", Phone_NUmber_STR);
                        addkid.put("Image", file);
                        addkid.put("Email", Email_STR);
                        addkid.put("Birthday", Birthday_STR);
                        addkid.put("Parent", false);
                        addkid.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    ParseObject conaction = new ParseObject("Parent_Kid");
                                    conaction.put("Parent_ID", Main2Activity.Mainuserlist.get(0).getUserParseID());
                                    conaction.put("Kid_ID", addkid.getObjectId());
                                    conaction.saveInBackground();
                                    UserDB = new UsersDataSource(getActivity());
                                    UserDB.open();
                                    UserDB.close();
                                    ArrayList<User> users = UserDB.getAllUsers();
                                    for (int indx2 = 0; indx2 < users.size() - 1; indx2++) {
                                        ArrayList<String> str = new ArrayList<String>();
                                        str.add(users.get(indx2).getUserParseID());
                                        str.add(addkid.getObjectId());
                                        ParseObject newchatroom = new ParseObject("ChatRoom");
                                        newchatroom.put("usersid", str);
                                        newchatroom.saveInBackground();
                                    }
                                    Chat_Contact_List.fulllist(getActivity());
                                    Schedule_Mange.SetImageeUsers(getActivity());
                                } else {

                                }
                            }
                        });
                        //
                        UserDB = new UsersDataSource(getActivity());
                        UserDB.open();
                        User user = new User();
                        user.setFirstname(First_Name_STR);
                        user.setLestName(Last_Name_STR);
                        user.setUserName(User_Name_STR);
                        //user.setPassword(Password_STR);
                        user.setBirthday(Birthday_STR);
                        user.setAddress(Address_STR);
                        user.setPerant(false);
                        user.setPhoneNumber(Phone_NUmber_STR);
                        user.setUserParseID(addkid.getObjectId());
                        user.setEmail(Email_STR);
                        user.setUserImage(resized);
                        UserDB.createUSER(user);
                        UserDB.close();
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("successful update");
                        alertDialog.setMessage("Kid user name is:" + User_Name_STR +
                                "\nKid password:" + Password_STR);
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //geting this kid parse object id to create a relationship between parent and kid
                            }
                        });
                        alertDialog.show();
                        //clear all the text box and image
                        First_Name.setText("");
                        Last_Name.setText("");
                        User_Name.setText("");
                        Password.setText("");
                        Confirm_Password.setText("");
                        Phone_NUmber.setText("");
                        Address.setText("");
                        Email.setText("");
                        Birthday.setText("");
                        ((ImageView) Kid.findViewById(R.id.AddKid_Image)).clearColorFilter();
                    } else {
                        Toast.makeText(getActivity(), "Mising Diatals", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "The passowrd are not matches", Toast.LENGTH_SHORT).show();
                }
                Chat_Contact_List.fulllist(getActivity());
                Schedule_Mange.SetImageeUsers(getActivity());
            }
        });
        return Kid;
    }
}
