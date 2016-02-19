package com.parse.starter;

import android.annotation.TargetApi;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Schedule_SingelEvent extends Fragment {
    public Button backtomanage;
    public Button pravdaybtn;
    public Button nextdaybtn;
    public Button SaveEvent;
    public Button DeleteEventBTN;
    public Button calbtn;
    public CheckBox checkBoxday;
    public CheckBox checkBoxdate;
    static protected int dayposition=0;
    public ScrollView scrollView;
    static protected boolean newevent=false;
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
        Main2Activity.text_date = (TextView) singelevent.findViewById(R.id.SingleEv_Date);
        calbtn = (Button) singelevent.findViewById(R.id.calbtn);
        dayrel = (RelativeLayout) singelevent.findViewById(R.id.dayrelative);
        daterel = (RelativeLayout) singelevent.findViewById(R.id.daterelative);
        checkBoxday = (CheckBox) singelevent.findViewById(R.id.checkBoxday);
        checkBoxday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (checkBoxdate.isEnabled()) {
                        checkBoxdate.setEnabled(false);
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
                    if (!checkBoxdate.isEnabled()) {
                        checkBoxdate.setEnabled(true);
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
        checkBoxdate = (CheckBox) singelevent.findViewById(R.id.checkBoxdate);
        checkBoxdate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (checkBoxday.isEnabled()) {
                        checkBoxday.setEnabled(false);
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
                    if (!checkBoxday.isEnabled()) {
                        checkBoxday.setEnabled(true);
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
            if(!checkBoxday.isChecked()){
                checkBoxday.setChecked(true);
            }
            if(checkBoxdate.isEnabled()){
                checkBoxdate.setEnabled(false);
            }
            if (checkBoxdate.isEnabled())
                checkBoxdate.setEnabled(false);
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
            if(CurrentEvent.getDay().isEmpty()){
                if(!checkBoxdate.isEnabled())
                    checkBoxdate.setEnabled(true);
                checkBoxdate.setChecked(true);
                    if (checkBoxday.isEnabled()) {
                        checkBoxday.setEnabled(false);
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
                if(!checkBoxday.isEnabled())
                    checkBoxday.setEnabled(true);
                checkBoxday.setChecked(true);
                if (checkBoxdate.isEnabled()) {
                    checkBoxdate.setEnabled(false);
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
                        Main2Activity.Lest_Fram = Main2Activity.current_Fram;
                        Main2Activity.current_Fram = Main2Activity.Schedule_Fram;
                        FragmentTransaction mytransaction2 = Main2Activity.myFramManager.beginTransaction();
                        mytransaction2.remove(Main2Activity.Lest_Fram);
                        mytransaction2.show(Main2Activity.current_Fram);
                        mytransaction2.commit();
                        delete();
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
                if(Schedule_SingelEvent.newevent){
                    //Create new event
                    Toast.makeText(getActivity(), "new", Toast.LENGTH_SHORT).show();
                    ParseObject newevent = new ParseObject("Event");
                    newevent.put("NameEvent", Schedule_SingelEvent.NameEvent.getText().toString());
                    newevent.put("Address",Schedule_SingelEvent.AddressEvent.getText().toString());
                    newevent.put("StartEvent",Schedule_SingelEvent.Set_Start_Time.getText().toString());
                    newevent.put("EndEvent", Schedule_SingelEvent.Set_End_Time.getText().toString());
                    if(checkBoxday.isChecked()){
                        newevent.put("Day",day.getText().toString());
                        newevent.put("Date","");
                    }else if(checkBoxdate.isChecked()){
                        newevent.put("Date",date.getText().toString());
                        newevent.put("Day","");
                    }
                    newevent.put("Kid_ID", Schedule_Mange.kidID);
                    newevent.saveInBackground();
                    Schedule_Mange.newevent=false;
                }else{
                    final ParseQuery<ParseObject> getevent = ParseQuery.getQuery("Event");
                    getevent.getInBackground(Schedule_SingelEvent.CurrentEvent.getParseid(), new GetCallback<ParseObject>() {
                        @Override
                        public void done(final ParseObject parseObject, ParseException e2) {
                            if (e2 == null) {
                                Toast.makeText(getActivity(), "old", Toast.LENGTH_SHORT).show();
                                parseObject.put("NameEvent", NameEvent.getText().toString());
                                parseObject.put("Address", AddressEvent.getText().toString());
                                parseObject.put("StartEvent", Set_Start_Time.getText().toString());
                                parseObject.put("EndEvent",Set_End_Time.getText().toString());
                                if(checkBoxday.isChecked()){
                                    parseObject.put("Day",day.getText().toString());
                                    parseObject.put("Date","");
                                }else if(checkBoxdate.isChecked()){
                                    parseObject.put("Date",date.getText().toString());
                                    parseObject.put("Day","");
                                }
                                parseObject.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if(e==null){
                                        }else{

                                        }
                                    }
                                });
                                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                alertDialog.setTitle("successful update");
                                alertDialog.setMessage("your changes is update");
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Main2Activity.Lest_Fram = Main2Activity.current_Fram;
                                        Main2Activity.current_Fram = Main2Activity.Schedule_Fram;
                                        FragmentTransaction mytransaction2 = Main2Activity.myFramManager.beginTransaction();
                                        mytransaction2.remove(Main2Activity.Lest_Fram);
                                        mytransaction2.show(Main2Activity.current_Fram);
                                        mytransaction2.commit();
                                    }
                                });
                                alertDialog.show();
                            } else {
                            }
                        }
                    });
                }
            }
        });
        return singelevent;
    }
    public void delete() {
        final ParseQuery<ParseObject> getevent = ParseQuery.getQuery("Event");
        getevent.getInBackground(Schedule_SingelEvent.CurrentEvent.getParseid(), new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject parseObject, ParseException e2) {
                if (e2 == null)
                    parseObject.deleteInBackground();
                else
                    delete();
            }
        });
    }
    public String getTime(){
        String time="";

        return time;
    }
}
