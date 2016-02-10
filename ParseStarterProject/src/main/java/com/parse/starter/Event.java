package com.parse.starter;

/**
 * Created by Admin on 08/02/2016.
 */
public class Event {

    private String name="";
    private String address="";
    private String Start_time="00:00";
    private String End_time="00:00";
    private String day="";
    private String parseid="";
    private String KidID="";
    private String KidName="";
    private long id;
    private long Location;
    private String date;
    public Event(String name, String address, String start_time, String end_time, String day, String parseid, String kidID, String kidName) {
        this.name = name;
        this.address = address;
        Start_time = start_time;
        End_time = end_time;
        this.day = day;
        this.parseid = parseid;
        KidID = kidID;
        KidName = kidName;
    }
    public Event() {

    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public long getLocation() {
        return Location;
    }
    public void setLocation(long location) {
        Location = location;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getStart_time() {
        return Start_time;
    }
    public void setStart_time(String start_time) {
        Start_time = start_time;
    }
    public String getEnd_time() {
        return End_time;
    }
    public void setEnd_time(String end_time) {
        End_time = end_time;
    }
    public String getDay() {
        return day;
    }
    public void setDay(String day) {
        this.day = day;
    }
    public String getParseid() {
        return parseid;
    }
    public void setParseid(String parseid) {
        this.parseid = parseid;
    }
    public String getKidID() {
        return KidID;
    }
    public void setKidID(String kidID) {
        KidID = kidID;
    }
    public String getKidName() {
        return KidName;
    }
    public void setKidName(String kidName) {
        KidName = kidName;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
}
