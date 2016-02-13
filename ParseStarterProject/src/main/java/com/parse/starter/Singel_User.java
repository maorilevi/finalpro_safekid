package com.parse.starter;

import android.graphics.Bitmap;

/**
 * Created by Admin on 06/10/2015.
 */
public class Singel_User {
    protected String UserName;
    protected Bitmap UserImage;
    protected String UserParseID;
    protected String chatRoomID;
    public Singel_User(boolean left, String userName) {
        UserName = userName;
        this.left = left;
    }
    public String getUserParseID() {
        return UserParseID;
    }
    public void setUserParseID(String userParseID) {
        UserParseID = userParseID;
    }
    public Bitmap getUserImage() {
        return UserImage;
    }
    public void setUserImage(Bitmap userImage) {
        UserImage = userImage;
    }
    protected boolean left;
    public String getUserName() {
        return UserName;
    }
    public void setUserName(String userName) {
        UserName = userName;
    }
    public boolean isLeft() {
        return left;
    }
    public void setLeft(boolean left) {
        this.left = left;
    }
    public String getChatRoomID() {
        return chatRoomID;
    }

    public void setChatRoomID(String chatRoomID) {
        this.chatRoomID = chatRoomID;
    }
    public Singel_User(){

    }
    public Singel_User(Singel_User p0) {
        this.left=true;
        this.UserName=p0.getUserName();
        this.UserImage=p0.getUserImage();
        this.UserParseID=p0.getUserParseID();
        this.chatRoomID=p0.getChatRoomID();
    }
    public Singel_User(Boolean left, Bitmap bitmap, String UserName, String UserParseID){
        super();
        this.left=left;
        this.UserName=UserName;
        this.UserImage=bitmap;
        this.UserParseID=UserParseID;
    }
}
