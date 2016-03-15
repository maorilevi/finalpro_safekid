package com.parse.starter;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

    static public boolean CREATE_CONNECTION=false;
    static public boolean CREATE_CHAT_IDS=false;
    static public boolean USER_IN_DATA_BASE=false;
    static public boolean IN_CREATE_CHAT_ROOM_FUNC=false;

    private UPDATE_DETAILS_CLASS MYUPDATE=new UPDATE_DETAILS_CLASS();

    private boolean UPDATE_USER_ON_PARSE=true;

    static public User NEWKID=new User();

    private UsersDataSource UserDB;//user database

    static public EditText First_Name;
    static public EditText Last_Name;
    static public EditText User_Name;
    static public EditText Password;
    static public EditText Confirm_Password;
    static public EditText Phone_NUmber;
    static public TextView Address;
    static public EditText Email;
    static public TextView Birthday;
    static public ImageView kid_image;


    static public  String First_Name_STR="";
    static public  String Last_Name_STR;
    static public  String User_Name_STR;
    static public  String Password_STR;
    static public  String Confirm_Password_STR;
    static public  String Phone_NUmber_STR;
    static public  String Address_STR;
    static public  String Email_STR;
    static public  String Birthday_STR;
    static public  String Parent_ID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View Kid=inflater.inflate(R.layout.add__kid, container, false);
        Main2Activity.Addkidscreen=true;

        UserDB = new UsersDataSource(getActivity());
        UserDB.open();
        final ArrayList<User> users = UserDB.getAllUsers();
        UserDB.close();
        if(users.size()>0){
            Parent_ID=users.get(0).UserParseID;
        }
        //Saving al the kid details on parse kids Table..................
        Kid.findViewById(R.id.AddKid_SaveDetails).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                First_Name = (EditText) Kid.findViewById(R.id.AddKid_Fname);
                Last_Name = (EditText) Kid.findViewById(R.id.AddKid_Lname);
                User_Name = (EditText) Kid.findViewById(R.id.AddKid_UserName);
                Password = (EditText) Kid.findViewById(R.id.AddKid_Pass);
                Confirm_Password = (EditText) Kid.findViewById(R.id.AddKid_ConfPass);
                Phone_NUmber = (EditText) Kid.findViewById(R.id.AddKid_Phone);
                Address = (TextView) Kid.findViewById(R.id.AddKid_Address);
                Email = (EditText) Kid.findViewById(R.id.AddKid_Email);
                Birthday = (TextView) Kid.findViewById(R.id.AddKid_Birthday);
                kid_image = (ImageView) Kid.findViewById(R.id.AddKid_Image);
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
                        final Bitmap resized = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
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
                        addkid.put("FAMILY_ID", users.get(0).getFamily_id());
                        addkid.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Log.i("ADDKID", "Parent_Kid:" + Parent_ID + "-kid id:" + addkid.getObjectId());
                                    CHAT_IDS(addkid.getObjectId());
                                    ADD_USER_TO_DB(addkid.getObjectId(), resized);
                                    Main2Activity.update(getActivity());
                                } else {
                                    UPDATE_USER_ON_PARSE = false;
                                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
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
    public void CHAT_IDS(final String kid_ID){
        final boolean[] run = {true};
        Thread thread=new Thread(){
            public void run(){
                while (run[0]){
                    try {
                        Log.i("CHATID","2");
                        sleep(500);
                        final UsersDataSource UserDB = new UsersDataSource(getActivity());
                        UserDB.open();
                        final ArrayList<User>  users= UserDB.getAllUsers();
                        UserDB.close();
                        if(users.size()>1&&USER_IN_DATA_BASE){
                            if(!IN_CREATE_CHAT_ROOM_FUNC){
                                IN_CREATE_CHAT_ROOM_FUNC=true;
                                Log.i("CHATID","size:"+Integer.toString( users.size()-1));
                                for (int indx2 = 0; indx2 < users.size()-1; indx2++) {
                                    final int copyindx=indx2;
                                    Log.i("CHATID","4");
                                    final ParseObject newchatroom = new ParseObject("ChatRoom");
                                    newchatroom.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if(e==null) {
                                                String FRIEND_ID=users.get(copyindx).UserParseID;
                                                String CHAT_ROOM_ID=newchatroom.getObjectId();
                                                Log.i("CHATID","5");

                                                CREATE_CONNECTIONS( kid_ID,  FRIEND_ID, CHAT_ROOM_ID );
                                                CREATE_CHAT_IDS = true;
                                                run[0]=false;
                                            }else{
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }

    static public void CLEAN_TEXT_BOX(Activity activity){
        First_Name.setText("");
        Last_Name.setText("");
        User_Name.setText("");
        Password.setText("");
        Confirm_Password.setText("");
        Phone_NUmber.setText("");
        Address.setText("");
        Email.setText("");
        Birthday.setText("");
        kid_image.clearColorFilter();



        IN_CREATE_CHAT_ROOM_FUNC=false;
        USER_IN_DATA_BASE=false;
        CREATE_CHAT_IDS=false;
        Chat_Contact_List.fulllist(activity);
        Schedule_Mange.SetImageeUsers(activity);
    }
    public void ADD_USER_TO_DB(final String kid_id,final Bitmap image_bitmap) {
        UserDB = new UsersDataSource(getActivity());
        UserDB.open();
        NEWKID.setFirstname(First_Name_STR);
        NEWKID.setLestName(Last_Name_STR);
        NEWKID.setUserName(User_Name_STR);
        //user.setPassword(Password_STR);
        NEWKID.setBirthday(Birthday_STR);
        NEWKID.setAddress(Address_STR);
        NEWKID.setPerant(false);
        NEWKID.setPhoneNumber(Phone_NUmber_STR);
        NEWKID.setUserParseID(kid_id);
        NEWKID.setEmail(Email_STR);
        NEWKID.setUserImage(image_bitmap);
        UserDB.createUSER(NEWKID);
        UserDB.close();
        USER_IN_DATA_BASE = true;
    }
    public void CHECK_IF_USER_EXIST(){

    }
    public void CREATE_CONNECTIONS(String NEW_KID_ID,String F_ID,String CHAT_ROOM_ID){
        ParseObject newconaction = new ParseObject("Chat_User");
        newconaction.put("ChatRoom_ID",CHAT_ROOM_ID);
        newconaction.put("User_ID", NEW_KID_ID);
        newconaction.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                }
            }
        });
        ParseObject newconaction2 = new ParseObject("Chat_User");
        newconaction2.put("ChatRoom_ID",CHAT_ROOM_ID);
        newconaction2.put("User_ID",F_ID);
        newconaction2.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                }
            }
        });
    }
}
