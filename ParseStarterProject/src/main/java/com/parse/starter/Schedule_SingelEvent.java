package com.parse.starter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Schedule_SingelEvent extends Fragment {
    public Button backtomanage;
    public Button pravdaybtn;
    public Button nextdaybtn;
    public Button SaveEvent;
    public Button DeleteEventBTN;
    public Button calbtn;
    public Button START;
    public Button END;
    public CheckBox checkBox_day;
    public CheckBox checkBox_date;
    static protected int dayposition=0;
    public ScrollView scrollView;
    static protected boolean newevent=false;
    static protected boolean updateevent=false;

    static protected ProgressBar ProgressUpdate;


    static protected Event CurrentEvent;
    static protected EditText NameEvent;
    static protected EditText AddressEvent;
    static protected TextView day;
    static protected TextView date;
    static protected TextView Set_Start_Time;
    static protected TextView Set_End_Time;
    static protected ArrayList<String> days=new ArrayList<String>();


    public RelativeLayout dayrel;
    public RelativeLayout daterel;
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View singelevent = inflater.inflate(R.layout.schedule_singleevent, container, false);
        if(Main2Activity.MainActionBar.isShowing()){
            Main2Activity.MainActionBar.hide();
        }
        START=(Button)singelevent.findViewById(R.id.Event_Show_startbtn);
        END=(Button)singelevent.findViewById(R.id.Event_Show_endbtn);
        ProgressUpdate=(ProgressBar)singelevent.findViewById(R.id.lodingupdate);
        Main2Activity.text_date = (TextView) singelevent.findViewById(R.id.SingleEv_Date);
        calbtn = (Button) singelevent.findViewById(R.id.calbtn);
        dayrel = (RelativeLayout) singelevent.findViewById(R.id.dayrelative);
        daterel = (RelativeLayout) singelevent.findViewById(R.id.daterelative);
        checkBox_day = (CheckBox) singelevent.findViewById(R.id.checkBoxday);
        checkBox_day.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (checkBox_date.isEnabled()) {
                        checkBox_date.setEnabled(false);
                    }
                    if (!dayrel.isEnabled())
                        dayrel.setEnabled(true);
                    if (!pravdaybtn.isEnabled())
                        pravdaybtn.setEnabled(true);
                    if (!nextdaybtn.isEnabled())
                        nextdaybtn.setEnabled(true);


                    if (calbtn.isEnabled())
                        calbtn.setEnabled(false);
                    if (daterel.isEnabled())
                        daterel.setEnabled(false);
                } else {
                    if (!checkBox_date.isEnabled()) {
                        checkBox_date.setEnabled(true);
                    }
                    if (dayrel.isEnabled())
                        dayrel.setEnabled(false);
                    if (pravdaybtn.isEnabled())
                        pravdaybtn.setEnabled(false);
                    if (nextdaybtn.isEnabled())
                        nextdaybtn.setEnabled(false);
                }
            }
        });
        checkBox_date = (CheckBox) singelevent.findViewById(R.id.checkBoxdate);
        checkBox_date.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (checkBox_day.isEnabled()) {
                        checkBox_day.setEnabled(false);
                    }
                    if (dayrel.isEnabled())
                        dayrel.setEnabled(false);
                    if (dayrel.isEnabled())
                        dayrel.setEnabled(false);
                    if (pravdaybtn.isEnabled())
                        pravdaybtn.setEnabled(false);
                    if (nextdaybtn.isEnabled())
                        nextdaybtn.setEnabled(false);
                    if (!daterel.isEnabled())
                        daterel.setEnabled(true);
                    if (!calbtn.isEnabled())
                        calbtn.setEnabled(true);
                } else {
                    if (!checkBox_day.isEnabled()) {
                        checkBox_day.setEnabled(true);
                        if (daterel.isEnabled())
                            daterel.setEnabled(false);
                        if (calbtn.isEnabled())
                            calbtn.setEnabled(false);
                    }
                }
            }
        });
        days.add("Sunday");
        days.add("Monday");
        days.add("Tuesday");
        days.add("Wednesday");
        days.add("Thursday");
        days.add("Friday");
        days.add("Saturday");

        scrollView = new ScrollView(getActivity());
        scrollView = (ScrollView) singelevent.findViewById(R.id.scrollView);
        pravdaybtn = new Button(getActivity());
        pravdaybtn = (Button) singelevent.findViewById(R.id.pravdaybtn);
        pravdaybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayposition--;
                if(dayposition<0){
                    dayposition=days.size()-1;
                }else if(dayposition>days.size()-1){
                    dayposition=0;
                }
                day.setText(days.get(dayposition));
            }
        });
        nextdaybtn = new Button(getActivity());
        nextdaybtn = (Button) singelevent.findViewById(R.id.nextdaybtn);
        nextdaybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayposition++;
                if(dayposition<0){
                    dayposition=days.size()-1;
                }else if(dayposition>days.size()-1){
                    dayposition=0;
                }
                day.setText(days.get(dayposition));

            }
        });
        if (newevent) {
            if(!checkBox_day.isChecked()){
                checkBox_day.setChecked(true);
            }
            if(checkBox_date.isEnabled()){
                checkBox_date.setEnabled(false);
            }
            if (checkBox_date.isEnabled())
                checkBox_date.setEnabled(false);
            if (!dayrel.isEnabled())
                dayrel.setEnabled(true);
            if (!pravdaybtn.isEnabled())
                pravdaybtn.setEnabled(true);
            if (!nextdaybtn.isEnabled())
                nextdaybtn.setEnabled(true);


            if (calbtn.isEnabled())
                calbtn.setEnabled(false);
            if (daterel.isEnabled())
                daterel.setEnabled(false);
        }else{
            if(!CurrentEvent.getDate().isEmpty()){
                if(!checkBox_date.isEnabled())
                    checkBox_date.setEnabled(true);
                checkBox_date.setChecked(true);
                    if (checkBox_day.isEnabled()) {
                        checkBox_day.setEnabled(false);
                    }
                    if (dayrel.isEnabled())
                        dayrel.setEnabled(false);
                    if (dayrel.isEnabled())
                        dayrel.setEnabled(false);
                    if (pravdaybtn.isEnabled())
                        pravdaybtn.setEnabled(false);
                    if (nextdaybtn.isEnabled())
                        nextdaybtn.setEnabled(false);
                    if (!daterel.isEnabled())
                        daterel.setEnabled(true);
                    if (!calbtn.isEnabled())
                        calbtn.setEnabled(true);
            }else{
                if(!checkBox_day.isEnabled())
                    checkBox_day.setEnabled(true);
                checkBox_day.setChecked(true);
                if (checkBox_date.isEnabled()) {
                    checkBox_date.setEnabled(false);
                }
                if (!dayrel.isEnabled())
                    dayrel.setEnabled(true);
                if (!pravdaybtn.isEnabled())
                    pravdaybtn.setEnabled(true);
                if (!nextdaybtn.isEnabled())
                    nextdaybtn.setEnabled(true);


                if (calbtn.isEnabled())
                    calbtn.setEnabled(false);
                if (daterel.isEnabled())
                    daterel.setEnabled(false);
            }
        }
        //event name
        NameEvent=(EditText)singelevent.findViewById(R.id.SingleEv_Name);
        NameEvent.setText(CurrentEvent.getName());
        //event address
        AddressEvent=(EditText)singelevent.findViewById(R.id.SingleEv_Address);
        AddressEvent.setText(CurrentEvent.getAddress());
        //event day
        day=(TextView)singelevent.findViewById(R.id.SingleEv_Day);
        day.setText(CurrentEvent.getDay());
        //event date
        date=(TextView)singelevent.findViewById(R.id.SingleEv_Date);
        date.setText(CurrentEvent.getDate());
        //event start
        Set_Start_Time=(TextView)singelevent.findViewById(R.id.SingleEv_Start);
        Set_Start_Time.setText(CurrentEvent.getStart_time());
        //event end
        Set_End_Time = (TextView)singelevent.findViewById(R.id.SingleEv_End);
        Set_End_Time.setText(CurrentEvent.getEnd_time());

        backtomanage=new Button(getActivity());
        backtomanage=(Button)singelevent.findViewById(R.id.SingleEv_Menubtn);
        backtomanage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Main2Activity.Lest_Fram=Main2Activity.current_Fram;
                Main2Activity.current_Fram=Main2Activity.Schedule_Fram;
                FragmentTransaction mytransaction2 = Main2Activity.myFramManager.beginTransaction();
                mytransaction2.remove(Main2Activity.Lest_Fram);
                mytransaction2.show(Main2Activity.current_Fram);
                mytransaction2.commit();
                Schedule_Mange.setallevent(getActivity(),Schedule_Mange.Current_day,Schedule_Mange.kidID);
                newevent=false;
                if(!Main2Activity.MainActionBar.isShowing()){
                    Main2Activity.MainActionBar.show();
                }
            }
        });
        DeleteEventBTN=new Button(getActivity());
        DeleteEventBTN=(Button)singelevent.findViewById(R.id.SingleEv_Deletebtn);
        DeleteEventBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder  =new AlertDialog.Builder(getActivity());
                builder.setTitle("Are you sure you want to delete the event");
                builder.setPositiveButton("yse", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete(CurrentEvent, getActivity());
                        Main2Activity.Lest_Fram = Main2Activity.current_Fram;
                        Main2Activity.current_Fram = Main2Activity.Schedule_Fram;
                        FragmentTransaction mytransaction2 = Main2Activity.myFramManager.beginTransaction();
                        mytransaction2.remove(Main2Activity.Lest_Fram);
                        mytransaction2.show(Main2Activity.current_Fram);
                        mytransaction2.commit();
                        Schedule_Mange.setallevent(getActivity(), Schedule_Mange.Current_day, Schedule_Mange.kidID);
                    }
                });
                builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
        SaveEvent=new Button(getActivity());
        SaveEvent=(Button)singelevent.findViewById(R.id.SingleEv_Savebtn);
        SaveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!updateevent){
                    DISABLE_ALL_BUTTON();
                    ProgressUpdate.setVisibility(View.VISIBLE);
                    updateevent=true;
                    final Event event=new Event();
                    if(checkBox_day.isChecked()){
                        event.setDay(day.getText().toString());
                        event.setDate("");
                    }else if(checkBox_date.isChecked()){
                        event.setDay(day.getText().toString());
                        event.setDate(date.getText().toString());
                    }
                    event.setStart_time(Set_Start_Time.getText().toString());
                    event.setEnd_time(Set_End_Time.getText().toString());
                    event.setKidID(Schedule_Mange.kidID);
                    event.setName(NameEvent.getText().toString());
                    event.setAddress(AddressEvent.getText().toString());
                    if(Schedule_SingelEvent.newevent){
                        //Create new event
                        Toast.makeText(getActivity(), "new", Toast.LENGTH_SHORT).show();
                        final ParseObject newevent = new ParseObject("Event");
                        newevent.put("NameEvent", Schedule_SingelEvent.NameEvent.getText().toString());
                        newevent.put("Address",Schedule_SingelEvent.AddressEvent.getText().toString());
                        newevent.put("StartEvent",Schedule_SingelEvent.Set_Start_Time.getText().toString());
                        newevent.put("EndEvent", Schedule_SingelEvent.Set_End_Time.getText().toString());
                        if(checkBox_day.isChecked()){
                            newevent.put("Day",day.getText().toString());
                            newevent.put("Date","");
                        }else if(checkBox_date.isChecked()){
                            newevent.put("Date",date.getText().toString());
                            newevent.put("Day",day.getText().toString());
                        }
                        newevent.put("Kid_ID", Schedule_Mange.kidID);
                        newevent.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e==null){
                                    event.setParseid(newevent.getObjectId());
                                    Toast.makeText(getActivity(), "old"+NameEvent.getText().toString()+" id"+event.getParseid(), Toast.LENGTH_SHORT).show();
                                    EventDataSource edb=new EventDataSource(getActivity());
                                    edb.open();
                                    edb.CreateNewEvent(event);
                                    edb.close();
                                    updateevent=false;
                                    ProgressUpdate.setVisibility(View.INVISIBLE);
                                    ENABLE_ALL_BUTTON();
                                    SEND_PUSH_EVENT(event);
                                }
                            }
                        });
                        Schedule_Mange.newevent=false;
                    }else{
                        final ParseQuery<ParseObject> getevent = ParseQuery.getQuery("Event");
                        getevent.getInBackground(Schedule_SingelEvent.CurrentEvent.getParseid(), new GetCallback<ParseObject>() {
                            @Override
                            public void done(final ParseObject parseObject, ParseException e2) {
                                if (e2 == null) {
                                    parseObject.put("NameEvent", NameEvent.getText().toString());
                                    parseObject.put("Address", AddressEvent.getText().toString());
                                    parseObject.put("StartEvent", Set_Start_Time.getText().toString());
                                    parseObject.put("EndEvent", Set_End_Time.getText().toString());
                                    if (checkBox_day.isChecked()) {
                                        parseObject.put("Day", day.getText().toString());
                                        parseObject.put("Date", "");
                                    } else if (checkBox_date.isChecked()) {
                                        parseObject.put("Date", date.getText().toString());
                                        parseObject.put("Day",  day.getText().toString());
                                    }
                                    parseObject.saveInBackground();
                                    Toast.makeText(getActivity(), "old"+NameEvent.getText().toString()+" id"+CurrentEvent.getParseid(), Toast.LENGTH_SHORT).show();
                                    event.setParseid(parseObject.getObjectId());
                                    EventDataSource edb=new EventDataSource(getActivity());
                                    edb.open();
                                    edb.UpdateEventFROMPARSE(event);
                                    edb.close();
                                    updateevent=false;
                                    ProgressUpdate.setVisibility(View.INVISIBLE);
                                    ENABLE_ALL_BUTTON();
                                    SEND_PUSH_EVENT(event);
                                } else {
                                }
                            }
                        });
                    }
                }
            }
        });
        return singelevent;
    }
    public void delete(final Event event,final Activity activity) {
        final ParseQuery<ParseObject> getevent = ParseQuery.getQuery("Event");
        getevent.getInBackground(Schedule_SingelEvent.CurrentEvent.getParseid(), new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject parseObject, ParseException e2) {
                if (e2 == null){
                    parseObject.deleteInBackground();
                    EventDataSource edb=new EventDataSource(activity);
                    edb.open();
                    if(edb.exist(event)){
                        edb.DeleteEvent(event);
                    }
                    edb.close();
                }
                else
                    delete(event,activity);
            }
        });
    }
    public void SEND_PUSH_EVENT(Event event){
        JSONObject data = new JSONObject();
        try {
            data.put("status", "editevent");
            data.put("EVENT_PARSE_ID", event.getParseid());
            data.put("EVENT_NAME",event.getName());
            data.put("DATE", event.getDate());
            data.put("DAY", event.getDay());
            data.put("ADDRESS",event.getAddress());
            data.put("START",event.getStart_time());
            data.put("END", event.getEnd_time());
            data.put("KID_ID",event.getKidID());
        } catch (JSONException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        ParseQuery pushQuery = ParseInstallation.getQuery();
        pushQuery.whereEqualTo("channels", event.getKidID()); // Set the channel
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery);// Set our Installation query
        push.setData(data);
        push.sendInBackground();

    }
    public void DISABLE_ALL_BUTTON(){
        if(NameEvent.isEnabled())      NameEvent.setEnabled(false);
        if(AddressEvent.isEnabled())   AddressEvent.setEnabled(false);
        if(START.isEnabled())          START.setEnabled(false);
        if(END.isEnabled())            END.setEnabled(false);
        if(backtomanage.isEnabled())   backtomanage.setEnabled(false);
        if(pravdaybtn.isEnabled())     pravdaybtn.setEnabled(false);
        if(nextdaybtn.isEnabled())     nextdaybtn.setEnabled(false);
        if(SaveEvent.isEnabled())      SaveEvent.setEnabled(false);
        if(DeleteEventBTN.isEnabled()) DeleteEventBTN.setEnabled(false);
        if(calbtn.isEnabled())         calbtn.setEnabled(false);
        if(checkBox_day.isEnabled())   checkBox_day.setEnabled(false);
        if(checkBox_date.isEnabled())  checkBox_date.setEnabled(false);
    }
    public void ENABLE_ALL_BUTTON(){
        if(!NameEvent.isEnabled())      NameEvent.setEnabled(true);
        if(!AddressEvent.isEnabled())   AddressEvent.setEnabled(true);
        if(!START.isEnabled())          START.setEnabled(true);
        if(!END.isEnabled())            END.setEnabled(true);
        if(!backtomanage.isEnabled())   backtomanage.setEnabled(true);
        if(!pravdaybtn.isEnabled())     pravdaybtn.setEnabled(true);
        if(!nextdaybtn.isEnabled())     nextdaybtn.setEnabled(true);
        if(!SaveEvent.isEnabled())      SaveEvent.setEnabled(true);
        if(!DeleteEventBTN.isEnabled()) DeleteEventBTN.setEnabled(true);
        if(!calbtn.isEnabled())         calbtn.setEnabled(true);
        if(!checkBox_day.isEnabled())   checkBox_day.setEnabled(true);
        if(!checkBox_date.isEnabled())  checkBox_date.setEnabled(true);
    }
}
