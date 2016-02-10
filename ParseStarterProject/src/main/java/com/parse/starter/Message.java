package com.parse.starter;

/**
 * Created by Admin on 09/02/2016.
 */
public class Message {
    private long id;
    protected boolean left;
    private String message;
    private String Date;
    private String Sender;
    private String Receiver;
    private String Parseid;
    public Message() {
    }
    public Message(Message obj) {
        this.left = obj.left;
        this.message = obj.message;
        this.Date = obj.getDate();
        this.Sender = obj.getSender();
        this.Receiver = obj.getReceiver();
        this.Parseid = obj.getParseid();

    }

    public Message(boolean left, String message){
        super();
        this.left=left;
        this.message=message;
    }
    public Message(boolean left, String message, String date, String sender, String receiver, String parseid) {
        this.left = left;
        this.message = message;
        Date = date;
        Sender = sender;
        Receiver = receiver;
        Parseid = parseid;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getParseid() {
        return Parseid;
    }
    public void setParseid(String parseid) {
        Parseid = parseid;
    }
    public String getReceiver() {
        return Receiver;
    }
    public void setReceiver(String receiver) {
        Receiver = receiver;
    }
    public String getSender() {
        return Sender;
    }
    public void setSender(String sender) {
        Sender = sender;
    }
    public String getDate() {
        return Date;
    }
    public void setDate(String date) {
        Date = date;
    }
    public boolean isLeft() {
        return left;
    }
    public void setLeft(boolean left) {
        this.left = left;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
}
