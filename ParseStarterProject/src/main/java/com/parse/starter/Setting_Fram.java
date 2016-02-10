package com.parse.starter;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;


public class Setting_Fram extends Fragment {

    private static int RESULT_LOAD_IMG = 1;
    protected static String imgDecodableString;

    //user
    private ImageView UserImage;
    private EditText UserName;
    private EditText FirstName;
    private EditText LastName;
    private EditText Password;
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
        return setting;
    }

    //tacking the user back to menu
    public void menu(View view){
    }
    //this function are to open all the user details on the screen
    public void OpenDetails(){
        User user=new User();
        user=Main.users.get(0);
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
        ContactAmergency1.setText(ContactAmergency1_STR);
        ContactAmergency2.setText(ContactAmergency2_STR);
        ContactAmergency3.setText(ContactAmergency3_STR);
        ContactAmergency4.setText(ContactAmergency4_STR);
    }


    //change image
    public void SettingchooseImage(View view) {
    }


}
