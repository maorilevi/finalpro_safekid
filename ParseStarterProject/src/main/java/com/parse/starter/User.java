package com.parse.starter;

import android.graphics.Bitmap;

/**
 * Created by Admin on 09/01/2016.
 */
public class User {


    private long id;
    private String Firstname;


    private String Password;
    private String LestName;
    private String UserName;
    private String birthday;
    private String Email;
    private String PhoneNumber;
    private String Address;
    private String UserParseID;
    private boolean perant=false;
    private Bitmap UserImage;

    public User(User user) {
        this.Firstname=user.getFirstname();
        this.LestName=user.getLestName();
        this.UserName=user.getUserName();
        this.birthday=user.getBirthday();
        this.Email=user.getEmail();
        this.PhoneNumber=user.getPhoneNumber();
        this.Address=user.getAddress();
        this.UserParseID=user.getUserParseID();
        this.perant=user.isPerant();
        this.UserImage=user.getUserImage();
        this.Password=user.getPassword();
    }

    public User() {

    }


    public void User(){
    }
    public void User(User p){
        this.Firstname=p.getFirstname();
        this.LestName=p.getLestName();
        this.UserName=p.getUserName();
        this.birthday=p.getBirthday();
        this.Email=p.getEmail();
        this.PhoneNumber=p.getPhoneNumber();
        this.Address=p.getAddress();
        this.UserParseID=p.getUserParseID();
        this.perant=p.isPerant();
        this.UserImage=p.getUserImage();
    }
    public void User(String Firstname,String LestName,String UserName,
                       String birthday,String Email,String PhoneNumber,
                       String Address,String UserParseID,
                       boolean perant,Bitmap UserImage){
        this.Firstname=Firstname;
        this.LestName=LestName;
        this.UserName=UserName;
        this.birthday=birthday;
        this.Email=Email;
        this.PhoneNumber=PhoneNumber;
        this.Address=Address;
        this.UserParseID=UserParseID;
        this.perant=perant;
        this.UserImage=UserImage;
    }

    //get & set
    public String getPassword() {
        return Password;
    }
    public void setPassword(String password) {
        Password = password;
    }
    public String getFirstname() {
        return Firstname;
    }
    public void setFirstname(String firstname) {
        Firstname = firstname;
    }
    public String getLestName() {
        return LestName;
    }
    public void setLestName(String lestName) {
        this.LestName = lestName;
    }
    public String getUserName() {
        return UserName;
    }
    public void setUserName(String userName) {
        UserName = userName;
    }
    public String getBirthday() {
        return birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public String getEmail() {
        return Email;
    }
    public void setEmail(String email) {
        Email = email;
    }
    public String getAddress() {
        return Address;
    }
    public void setAddress(String address) {
        Address = address;
    }
    public String getPhoneNumber() {
        return PhoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }
    public String getUserParseID() {
        return UserParseID;
    }
    public void setUserParseID(String userParseID) {
        UserParseID = userParseID;
    }
    public boolean isPerant() {
        return perant;
    }
    public void setPerant(boolean perant) {
        this.perant = perant;
    }
    public Bitmap getUserImage() {
        return UserImage;
    }
    public void setUserImage(Bitmap userImage) {
        UserImage = userImage;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }




}
