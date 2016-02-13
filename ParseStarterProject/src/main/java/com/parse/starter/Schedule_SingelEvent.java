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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Schedule_SingelEvent extends Fragment {
    public Button backtomanage;
    public Button SaveStarttime;
    public Button SaveEndtime;
    public Button SaveEvent;

    public Button DeleteEventBTN;
    static protected boolean newevent=false;
    static protected Event CurrentEvent;
    static protected TextView NameEvent;
    static protected TextView AddressEvent;
    static protected TextView day;
    static protected TextView Set_Start_Time;
    static protected TextView Set_End_Time;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View singelevent = inflater.inflate(R.layout.schedule__event1, container, false);
        NameEvent=(TextView)singelevent.findViewById(R.id.event_name);
        AddressEvent=(TextView)singelevent.findViewById(R.id.event_address);
        day=(TextView)singelevent.findViewById(R.id.eventday);
        Set_Start_Time=(TextView)singelevent.findViewById(R.id.event_start_txt);
        Set_End_Time=(TextView)singelevent.findViewById(R.id.event__end_txt);
        NameEvent.setText(CurrentEvent.getName());
        AddressEvent.setText(CurrentEvent.getAddress());
        day.setText(CurrentEvent.getDay());
        Set_Start_Time.setText(CurrentEvent.getStart_time());
        Set_End_Time.setText(CurrentEvent.getEnd_time());
        backtomanage=new Button(getActivity());
        backtomanage=(Button)singelevent.findViewById(R.id.BackToManageEvent);
        backtomanage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Main2Activity.Lest_Fram=Main2Activity.current_Fram;
                Main2Activity.current_Fram=Main2Activity.Schedule_Fram;
                FragmentTransaction mytransaction2 = Main2Activity.myFramManager.beginTransaction();
                mytransaction2.remove(Main2Activity.Lest_Fram);
                mytransaction2.show(Main2Activity.current_Fram);
                mytransaction2.commit();
            }
        });
        DeleteEventBTN=new Button(getActivity());
        DeleteEventBTN=(Button)singelevent.findViewById(R.id.Event_delete_btn);
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
        SaveStarttime=new Button(getActivity());
        SaveStarttime=(Button)singelevent.findViewById(R.id.Event_Show_startbtn);
        SaveStarttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePicker mytime=new TimePicker(getActivity());
                int Hours_int=((TimePicker)singelevent.findViewById(R.id.timePicker)).getCurrentHour();
                int Minute_int=((TimePicker)singelevent.findViewById(R.id.timePicker)).getCurrentMinute();
                String Hours="";
                String Minute="";
                if(Hours_int<10){
                    Hours=("0"+Integer.toString(Hours_int));
                }else{
                    Hours=(Integer.toString(Hours_int));
                }
                if(Minute_int<10){
                    Minute=("0"+Integer.toString(Minute_int));
                }else{
                    Minute=(Integer.toString(Minute_int));
                }
                Set_Start_Time.setText(Hours + ":" + Minute);
            }
        });
        SaveEndtime=new Button(getActivity());
        SaveEndtime=(Button)singelevent.findViewById(R.id.Event_Show_endtime);
        SaveEndtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePicker mytime=new TimePicker(getActivity());
                int Hours_int=((TimePicker)singelevent.findViewById(R.id.timePicker)).getCurrentHour();
                int Minute_int=((TimePicker)singelevent.findViewById(R.id.timePicker)).getCurrentMinute();
                String Hours="";
                String Minute="";
                if(Hours_int<10){
                    Hours=("0"+Integer.toString(Hours_int));
                }else{
                    Hours=(Integer.toString(Hours_int));
                }
                if(Minute_int<10){
                    Minute=("0"+Integer.toString(Minute_int));
                }else{
                    Minute=(Integer.toString(Minute_int));
                }
                Set_End_Time.setText(Hours + ":" + Minute);
            }
        });
        SaveEvent=new Button(getActivity());
        SaveEvent=(Button)singelevent.findViewById(R.id.Event_Save_btn);
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
                    newevent.put("Day", Schedule_Mange.Current_day);
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
                                parseObject.put("NameEvent", Schedule_SingelEvent.NameEvent.getText().toString());
                                parseObject.put("Address", Schedule_SingelEvent.AddressEvent.getText().toString());
                                parseObject.put("StartEvent", Schedule_SingelEvent.Set_Start_Time.getText().toString());
                                parseObject.put("EndEvent", Schedule_SingelEvent.Set_End_Time.getText().toString());
                                parseObject.put("Day", Schedule_SingelEvent.day.getText().toString());
                                parseObject.saveInBackground();
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
