package com.parse.starter;

import java.util.Calendar;

/**
 * Created by Admin on 27/02/2016.
 */
public class WarningMessage {

    private String kid_name="";
    private String kid_Parse_id="";
    private String date="";
    private String event_parse_id="";
    private long id=0;
    private String warning_parse_id="";
    private double loc_X=0;
    private double loc_y=0;

    public WarningMessage() {
    }

    public String getWarning_parse_id() {
        return warning_parse_id;
    }
    public void setWarning_parse_id(String warning_parse_id) {
        this.warning_parse_id = warning_parse_id;
    }
    public String getKid_name() {
        return kid_name;
    }
    public void setKid_name(String kid_name) {
        this.kid_name = kid_name;
    }
    public double getLoc_y() {
        return loc_y;
    }
    public void setLoc_y(double loc_y) {
        this.loc_y = loc_y;
    }
    public String getEvent_parse_id() {
        return event_parse_id;
    }
    public void setEvent_parse_id(String event_parse_id) {
        this.event_parse_id = event_parse_id;
    }
    public double getLoc_X() {
        return loc_X;
    }
    public void setLoc_X(double loc_X) {
        this.loc_X = loc_X;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getKid_Parse_id() {
        return kid_Parse_id;
    }
    public void setKid_Parse_id(String kid_Parse_id) {
        this.kid_Parse_id = kid_Parse_id;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public boolean MATCHES_TO_THIS_DAY(){
        boolean metches=false;
        Calendar cal=Calendar.getInstance();
        int year  = 0;    year  = cal.get(Calendar.YEAR);
        int month = 0;    month = cal.get(Calendar.MONTH);
        int day   = 0;    day   = cal.get(Calendar.DAY_OF_MONTH);
        String[] datesplit=date.split("/");
        int thisyear=0;    thisyear=Integer.parseInt(datesplit[2]);
        int thismonth=0;   thismonth=Integer.parseInt(datesplit[1]);
        int thisday=0;     thisday=Integer.parseInt(datesplit[0]);
        if(year==thisyear&&month==thismonth&&day==thisday){
            metches=true;
        }
        return metches;
    }
    public boolean MATCHES_TO_THIS_MONTH(){
        boolean metches=false;
        Calendar cal=Calendar.getInstance();
        int year  = 0;    year  = cal.get(Calendar.YEAR);
        int month = 0;    month = cal.get(Calendar.MONTH);
        String[] datesplit=date.split("/");
        int thisyear=0;    thisyear=Integer.parseInt(datesplit[2]);
        int thismonth=0;   thismonth=Integer.parseInt(datesplit[1]);
        if(year==thisyear&&month==thismonth){
            metches=true;
        }
        return metches;
    }
    public boolean MATCHES_TO_THIS_YEAR(){
        boolean metches=false;
        Calendar cal=Calendar.getInstance();
        int year  = 0;    year  = cal.get(Calendar.YEAR);
        String[] datesplit=date.split("/");
        int thisyear=0;    thisyear=Integer.parseInt(datesplit[2]);
        if(year==thisyear){
            metches=true;
        }
        return metches;
    }

}
