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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.ByteArrayOutputStream;

/**
 * Created by Admin on 23/01/2016.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SignUp_Fram extends Fragment {
    static protected boolean InSignUp=true;



    private Button Save;
    private EditText FirstName;
    private EditText LastName;
    private EditText UserName;
    private EditText Password;
    private EditText ConfirmPasword;
    private EditText PhonNamber;
    private EditText Email;
    private EditText Address;
    private EditText Birthday;

    private String userFirstName;
    private String UserLastName;
    private String UserNAME;
    private String UserPassword;
    private String UserConfirmPassword;
    private String UserPhonNaber;
    private String UserEmail;
    private String UserAddress;
    private String UserBirthday;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView=inflater.inflate(R.layout.signup, container, false);

        //casting EditText
        LastName=(EditText)myView.findViewById(R.id.SignUp_Lname);
        FirstName=(EditText)myView.findViewById(R.id.SignUp_Fname);
        UserName=(EditText)myView.findViewById(R.id.SignUp_Username);
        Password=(EditText)myView.findViewById(R.id.SignUp_Pass);
        ConfirmPasword=(EditText)myView.findViewById(R.id.SignUp_ConfPass);
        PhonNamber=(EditText)myView.findViewById(R.id.SignUp_Phone);
        Email=(EditText)myView.findViewById(R.id.SignUp_Email);
        Address=(EditText)myView.findViewById(R.id.SignUp_Address);
        Birthday=(EditText)myView.findViewById(R.id.SignUp_Birthday);
        // OnClick on Save Button
        myView.findViewById(R.id.SignUp_SaveDetails).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //&ver = EditText.ToString
                UserLastName = LastName.getText().toString();
                userFirstName = FirstName.getText().toString();
                UserAddress = Address.getText().toString();
                UserEmail = Email.getText().toString();
                UserPhonNaber = PhonNamber.getText().toString();
                UserNAME = UserName.getText().toString();
                UserPassword = Password.getText().toString();
                UserConfirmPassword = ConfirmPasword.getText().toString();
                UserBirthday = Birthday.getText().toString();
                //checking if the username is already taken
                //checking password
                if (UserPassword.matches(UserConfirmPassword)) {
                    // checking all the the user details
                    if (!(userFirstName.isEmpty() || UserLastName.isEmpty() || UserAddress.isEmpty() || UserEmail.isEmpty() || UserPhonNaber.isEmpty()
                            || UserNAME.isEmpty() || UserPassword.isEmpty() || UserConfirmPassword.isEmpty())) {

                        Bitmap bitmap = BitmapFactory.decodeFile(Main.imgDecodableString);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();

                        Bitmap resized = Bitmap.createScaledBitmap(bitmap, 100, 100, true);

                        resized.compress(Bitmap.CompressFormat.PNG, 100, stream);

                        byte[] image = stream.toByteArray();

                        ParseFile file = new ParseFile("androidbegin.png", image);
                        file.saveInBackground();
                        //save user on server
                        ParseObject signup = new ParseObject("USER");
                        signup.put("F_name", userFirstName);
                        signup.put("L_name", UserLastName);
                        signup.put("User_Name", UserNAME);
                        signup.put("Password", UserPassword);
                        signup.put("Address", UserAddress);
                        signup.put("Phone_Number", UserPhonNaber);
                        signup.put("Image", file);
                        signup.put("Email", UserEmail);
                        signup.put("Birthday", UserBirthday);
                        signup.put("Parent", true);
                        signup.saveInBackground();
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Welcome " + userFirstName);
                        alertDialog.setMessage("your user name is:" + UserNAME +
                                "\nyour password:" + UserPassword);
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog closed
                                FragmentTransaction mytransaction2 = Main.mymanager.beginTransaction();
                                mytransaction2.hide(Main.SignUp_Fram);
                                mytransaction2.show(Main.Login_Fram);
                                mytransaction2.commit();
                            }
                        });
                        alertDialog.show();
                        //save user details in app database

                        InSignUp = false;
                        // case that all input as in the table go page back
                    } else {
                        Toast.makeText(getActivity(), "Details incorrect", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            }
        });

        return myView;
    }
}
