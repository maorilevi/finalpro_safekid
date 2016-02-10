package com.parse.starter;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Schedule_SingelEvent extends Fragment {
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
        View singelevent = inflater.inflate(R.layout.schedule__event1, container, false);
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
        return singelevent;
    }

}
