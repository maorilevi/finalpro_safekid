package com.parse.starter;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Setting extends Fragment {

    static protected UsersDataSource Udb;
    private Button save_details_btn;
    private Button Logout_btn;
    static protected boolean userchangeimage=false;
    //user
    private ImageView UserImage;
    private EditText UserName;
    private EditText FirstName;
    private EditText LastName;
    private EditText Old_Password;
    private EditText New_Password;
    private EditText Conf_New_Password;
    private EditText PhoneNumber;
    private EditText Email;
    private EditText Birthday;
    private EditText Address;
    private EditText ContactAmergency1;
    private EditText ContactAmergency2;
    private EditText ContactAmergency3;
    private EditText ContactAmergency4;


    static protected Bitmap UserImage_BMP;
    static protected String UserName_STR;
    static protected String FirstName_STR;
    static protected String LastName_STR;
    static protected String Password_STR;
    static protected String PhoneNumber_STR;
    static protected String Email_STR;
    static protected String Birthday_STR;
    static protected String Address_STR;
    static protected String ContactAmergency1_STR;
    static protected String ContactAmergency2_STR;
    static protected String ContactAmergency3_STR;
    static protected String ContactAmergency4_STR;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View setting = inflater.inflate(R.layout.setting, container, false);
        Udb=new UsersDataSource(getActivity());
        save_details_btn=new Button(getActivity());
        save_details_btn=(Button)setting.findViewById(R.id.Setting_saveBTN);
        Logout_btn=new Button(getActivity());
        Logout_btn=(Button)setting.findViewById(R.id.Setting_logoutBTN);
        Old_Password =new EditText(getActivity());
        Old_Password=(EditText)setting.findViewById(R.id.Setting_Oldpassword);
        New_Password=new EditText(getActivity());
        New_Password=(EditText)setting.findViewById(R.id.Setting_Newpassword);
        Conf_New_Password=new EditText(getActivity());
        Conf_New_Password=(EditText)setting.findViewById(R.id.Setting_ConfPassword);
        //connect to layout;
        UserImage = (ImageView) setting.findViewById(R.id.Setting_User_Image);
        UserName = (EditText) setting.findViewById(R.id.Setting_U_Name);
        FirstName = (EditText) setting.findViewById(R.id.Setting_F_Name);
        LastName = (EditText) setting.findViewById(R.id.Setting_L_name);
        PhoneNumber = (EditText) setting.findViewById(R.id.Setting_Phone_Number);
        Email = (EditText) setting.findViewById(R.id.Setting_Email);
        Birthday = (EditText) setting.findViewById(R.id.Setting_Birthday);
        Address = (EditText) setting.findViewById(R.id.Setting_Address);
        ContactAmergency1 = (EditText) setting.findViewById(R.id.Setting_ContactAmergency1);
        ContactAmergency2 = (EditText) setting.findViewById(R.id.Setting_ContactAmergency2);
        ContactAmergency3 = (EditText) setting.findViewById(R.id.Setting_ContactAmergency3);
        ContactAmergency4 = (EditText) setting.findViewById(R.id.Setting_ContactAmergency4);
        //calling to function
        OpenDetails();
        save_details_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> table= ParseQuery.getQuery("USER");
                table.getInBackground(Main2Activity.Mainuserlist.get(0).
                        getUserParseID(), new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if(e==null){
                            Bitmap bitmap;
                            if(userchangeimage){
                                bitmap = BitmapFactory.decodeFile(Main2Activity.imgDecodableString);
                                Bitmap resized = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
                                bitmap=resized;
                            }else{
                                bitmap = Main2Activity.Mainuserlist.get(0).getUserImage();
                            }
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 10, stream);
                            byte[] image = stream.toByteArray();
                            ParseFile file = new ParseFile("androidbegin.png", image);
                            file.saveInBackground();
                            Main2Activity.Mainuserlist.get(0).setFirstname(FirstName.getText().toString());
                            Main2Activity.Mainuserlist.get(0).setLestName(LastName.getText().toString());
                            Main2Activity.Mainuserlist.get(0).setUserName(UserName.getText().toString());
                            Password_STR=Main2Activity.Mainuserlist.get(0).getPassword();
                            if(Old_Password.getText().toString().
                                    matches(Main2Activity.Mainuserlist.get(0).getPassword())){
                                if(New_Password.getText().toString().
                                        matches(Conf_New_Password.getText().toString())){
                                    Password_STR=New_Password.getText().toString();
                                }else{
                                    Toast.makeText(getActivity(),"the password not mathes",Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(getActivity(), "the password not mathes", Toast.LENGTH_SHORT).show();
                            }
                            Main2Activity.Mainuserlist.get(0).setPassword(Password_STR);
                            Main2Activity.Mainuserlist.get(0).setAddress(Address.getText().toString());
                            Main2Activity.Mainuserlist.get(0).setPhoneNumber(PhoneNumber.getText().toString());
                            Main2Activity.Mainuserlist.get(0).setEmail(Email.getText().toString());
                            Main2Activity.Mainuserlist.get(0).setBirthday(Birthday.getText().toString());
                            Main2Activity.Mainuserlist.get(0).setUserImage(bitmap);
                            Udb.open();
                            Udb.UpdateUser(Main2Activity.Mainuserlist.get(0));
                            Udb.close();
                            object.put("F_name", Main2Activity.Mainuserlist.get(0).getFirstname());
                            object.put("L_name", Main2Activity.Mainuserlist.get(0).getLestName());
                            object.put("User_Name", Main2Activity.Mainuserlist.get(0).getUserName());
                            object.put("Password",Main2Activity.Mainuserlist.get(0).getPassword());
                            object.put("Address", Main2Activity.Mainuserlist.get(0).getAddress());
                            object.put("Phone_Number",Main2Activity.Mainuserlist.get(0).getPhoneNumber());
                            object.put("Image", file);
                            object.put("Email", Main2Activity.Mainuserlist.get(0).getEmail());
                            object.put("Birthday", Main2Activity.Mainuserlist.get(0).getBirthday());
                            object.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e==null){
                                        Log.i("save user details from setting","save");

                                    }else{
                                        Log.i("save user details from setting","not save");
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
        Logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(android.R.drawable.ic_dialog_info);
                    builder.setMessage("are you sure you want to log out?");
                    builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            Log.i("setting", "delete/logout");
                            Main2Activity.navigationView.removeAllViews();
                            UsersDataSource usdb=new UsersDataSource(getActivity());
                            usdb.open();
                            usdb.DeleteAllUsers();
                            usdb.close();
                            MessageDataSourse Mdb=new MessageDataSourse(getActivity());
                            Mdb.open();
                            Mdb.DeleteAllMessage();
                            Mdb.close();
                            EventDataSource Edb=new EventDataSource(getActivity());
                            Edb.open();
                            Edb.DeleteAllEvent();
                            Edb.close();
                            FragmentTransaction mytransaction2 = Main2Activity.myFramManager.beginTransaction();
                            //mytransaction2.hide(Main2Activity.Splach_Fram);
                            mytransaction2.show(Main2Activity.Login_Fram);
                            //mytransaction2.hide(Main2Activity.SignUp_Fram);
                            mytransaction2.remove(Main2Activity.Addkid_Fram);
                            mytransaction2.remove(Main2Activity.Schedule_Fram);
                            mytransaction2.remove(Main2Activity.Setting_Fram);
                            mytransaction2.remove(Main2Activity.ChatList_Fram);
                            mytransaction2.commit();
                        }
                    });
                    builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            Log.i("setting", "not log out");
                        }
                    }).show();

            }
        });
        return setting;
    }

    //tacking the user back to menu
    public void menu(View view){
    }
    //this function are to open all the user details on the screen
    public void OpenDetails(){
        User user=new User();
        user=Main2Activity.Mainuserlist.get(0);
        UserName_STR=user.getUserName();
        FirstName_STR=user.getFirstname();
        LastName_STR=user.getLestName();
        Password_STR=user.getPassword();
        PhoneNumber_STR=user.getPhoneNumber();
        Email_STR=user.getEmail();
        Birthday_STR=user.getBirthday();
        Address_STR=user.getAddress();
        ContactAmergency1_STR="";
        ContactAmergency2_STR="";
        ContactAmergency3_STR="";
        ContactAmergency4_STR="";
        UserImage_BMP=user.getUserImage();

        UserImage.setImageBitmap(UserImage_BMP);
        UserName.setText(UserName_STR);
        FirstName.setText(FirstName_STR);
        LastName.setText(LastName_STR);
        //Password.setText(Password_STR);
        PhoneNumber.setText(PhoneNumber_STR);
        Email.setText(Email_STR);
        Birthday.setText(Birthday_STR);
        Address.setText(Address_STR);
        if(Main2Activity.Mainuserlist.get(0).isPerant()){
            ContactAmergency1.setVisibility(View.GONE);
            ContactAmergency2.setVisibility(View.GONE);
            ContactAmergency3.setVisibility(View.GONE);
            ContactAmergency4.setVisibility(View.GONE);
        }
        ContactAmergency1.setText(ContactAmergency1_STR);
        ContactAmergency2.setText(ContactAmergency2_STR);
        ContactAmergency3.setText(ContactAmergency3_STR);
        ContactAmergency4.setText(ContactAmergency4_STR);
    }
    //change image
    public void SettingchooseImage(View view) {
    }


}
