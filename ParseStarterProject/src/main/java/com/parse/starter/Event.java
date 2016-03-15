package com.parse.starter;

import android.util.Log;

import java.util.Calendar;

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


    private double longitude;
    private double latitude;
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
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }


    public int Convertstarttoint(){
        int starttime=0;
        String[] timesplited=this.getStart_time().split(":");
        int hours=(Integer.parseInt(timesplited[0]));
        int minute=(Integer.parseInt(timesplited[1]));
        starttime=hours*60+minute;
        return starttime;
    }
    public int Convertendtoint(){
        int EndTime=0;
        String[] timesplited=this.getEnd_time().split(":");
        int hours=(Integer.parseInt(timesplited[0]));
        int minute=(Integer.parseInt(timesplited[1]));
        EndTime=hours*60+minute;
        return EndTime;
    }
    public boolean CheckMatches(int time){
        int mystarttime=Convertstarttoint();
        int myendtime=Convertendtoint();
        if(time>mystarttime&&time<myendtime){
            return true;
        }
        return false;
    }
    public boolean CHECK_DATE_MATCHES(){
        boolean metches=false;
        if(date.isEmpty()){
            metches=true;
        }else{
            Calendar cal=Calendar.getInstance();
            int year  = 0;    year  = cal.get(Calendar.YEAR);
            int month = 0;    month = cal.get(Calendar.MONTH);
            int day   = 0;    day   = cal.get(Calendar.DAY_OF_MONTH);
            String[] datesplit=date.split("/");
            Log.i("CHECKINGEVENTSINGLE","DATE:"+date);

            int thisyear=0;    thisyear=Integer.parseInt(datesplit[2]);
            int thismonth=0;   thismonth=Integer.parseInt(datesplit[1]);
            int thisday=0;     thisday=Integer.parseInt(datesplit[0]);


            if(year==thisyear&&month==thismonth&&day==thisday){
                metches=true;
            }
        }
        return metches;
    }
}
