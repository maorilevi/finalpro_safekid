package com.parse.starter;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
@TargetApi(Build.VERSION_CODES.HONEYCOMB)

public class Schedule_Mange extends Fragment {

    static protected boolean newevent=false;
    private UsersDataSource UserDB;//user database
    static protected EventDataSource EventDB;



    public Button day1;
    private Button day2;
    private Button day3;
    private Button day4;
    private Button day5;
    private Button day6;
    private Button day7;


    static protected EventArray allevents;

    static protected TextView userName;
    static protected ArrayList<User> users = new ArrayList<User>();
    static protected Button newEventbtn;
    //event Variable
    static protected String Current_day="";
    //kid variable
    static protected String kidID="";
    static protected String kidname_STR="";
    //
    static protected ScrollView ScrollViewEvent;
    static protected LinearLayout LayoutEvent;
    static protected HorizontalScrollView ScrollViewKidImage;
    static protected LinearLayout LayotKidImage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View schedule= inflater.inflate(R.layout.schedule_activity, container, false);
        //user db
        UserDB=new UsersDataSource(getActivity());
        UserDB.open();
        users = UserDB.getAllUsers();
        UserDB.close();
        //event db
        userName=new TextView(getActivity());
        userName=(TextView)schedule.findViewById(R.id.kidNameListEvent);
        //set default day
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
        Calendar calendar = Calendar.getInstance();
        Current_day = dayFormat.format(calendar.getTime());
        ((TextView) schedule.findViewById(R.id.ListEvent_Today)).setText(Current_day);

        day1=(Button)schedule.findViewById(R.id.Sunday_btn);
        day1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Current_day = "Sunday";
                Log.i("SCHEDULEMANGER","kidid:"+kidID+" ");
                TextView textView=(TextView)schedule.findViewById(R.id.ListEvent_Today);
                textView.setText(Schedule_Mange.Current_day);
                setallevent(getActivity(),Current_day,kidID);
            }
        });
        day2=(Button)schedule.findViewById(R.id.Monday_btn);
        day2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Current_day = "Monday";
                TextView textView=(TextView)schedule.findViewById(R.id.ListEvent_Today);
                textView.setText(Current_day);
                setallevent(getActivity(), Current_day, kidID);
            }
        });
        day3=(Button)schedule.findViewById(R.id.Tuesday_btn);
        day3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Current_day = "Tuesday";
                TextView textView=(TextView)schedule.findViewById(R.id.ListEvent_Today);
                textView.setText(Current_day);
                setallevent(getActivity(), Current_day, kidID);
            }
        });
        day4=(Button)schedule.findViewById(R.id.Wednesday_btn);
        day4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Current_day = "Wednesday";
                TextView textView=(TextView)schedule.findViewById(R.id.ListEvent_Today);
                textView.setText(Current_day);
                setallevent(getActivity(), Current_day, kidID);
            }
        });
        day5=(Button)schedule.findViewById(R.id.Thursday_btn);
        day5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Current_day = "Thursday";
                TextView textView=(TextView)schedule.findViewById(R.id.ListEvent_Today);
                textView.setText(Current_day);
                setallevent(getActivity(), Current_day, kidID);
            }
        });
        day6=(Button)schedule.findViewById(R.id.Friday_btn);
        day6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Current_day = "Friday";
                TextView textView=(TextView)schedule.findViewById(R.id.ListEvent_Today);
                textView.setText(Current_day);
                setallevent(getActivity(), Current_day, kidID);
            }
        });
        day7=(Button)schedule.findViewById(R.id.Saturday_btn);
        day7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Current_day = "Saturday";
                TextView textView=(TextView)schedule.findViewById(R.id.ListEvent_Today);
                textView.setText(Current_day);
                setallevent(getActivity(), Current_day, kidID);
            }
        });
        //create image list
        ScrollViewKidImage=new HorizontalScrollView(getActivity());
        ScrollViewKidImage=(HorizontalScrollView)schedule.findViewById(R.id.scrollView_kidsImage);
        LayotKidImage =new LinearLayout(getActivity());
        LayotKidImage.setOrientation(LinearLayout.HORIZONTAL);
        ScrollViewKidImage.addView(LayotKidImage);
        //create event list
        ScrollViewEvent=(ScrollView)schedule.findViewById(R.id.scrollevent);
        LayoutEvent = new LinearLayout(getActivity());
        LayoutEvent.setOrientation(LinearLayout.VERTICAL);
        ScrollViewEvent.addView(LayoutEvent);
        //open user Image
        SetImageeUsers(getActivity());
        newEventbtn=new Button(getActivity());
        newEventbtn=(Button)schedule.findViewById(R.id.neweventbtn);
        newEventbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Event event1=new Event();
                event1.setKidID(kidID);
                event1.setDay(Current_day);
                Fragment newfram=new Schedule_SingelEvent();
                Main2Activity.Lest_Fram=Main2Activity.current_Fram;
                Main2Activity.current_Fram=newfram;
                FragmentTransaction mytransaction2 = Main2Activity.myFramManager.beginTransaction();
                mytransaction2.hide(Main2Activity.Lest_Fram);
                mytransaction2.add(R.id.MainRelative, Main2Activity.current_Fram, "newfram");
                mytransaction2.commit();
                Schedule_SingelEvent.CurrentEvent=event1;
                Schedule_SingelEvent.newevent=true;
                for(int indx=0;indx<Schedule_SingelEvent.days.size();indx++){
                    if(Schedule_SingelEvent.days.get(indx).matches(Current_day)){
                        Schedule_SingelEvent.dayposition=indx;
                    }
                }
            }
        });
        return schedule;
    }
    protected static void SetImageeUsers(final Activity activity){
        LayotKidImage.removeAllViews();
        for (int indx=1;indx<users.size();indx++){
            if(indx==1){
                userName.setText(users.get(indx).getFirstname());
                kidname_STR=users.get(indx).getFirstname();
                kidID=users.get(indx).getUserParseID();
                setallevent(activity,Current_day,kidID);
            }
            LinearLayout lay=new LinearLayout(activity);
            lay.setOrientation(LinearLayout.VERTICAL);
            lay.setLayoutParams(new ViewGroup.LayoutParams((int) (Main2Activity.Mainwidth * 0.3), (int) (Main2Activity.Mainwidth * 0.3)));
            lay.setPadding(5,5,5,5);
            ImageView kidImage = new ImageView(activity);
            kidImage.setImageBitmap(users.get(indx).getUserImage());
            //set new height and width to user image
            //int width = //ScrollViewKidImage.getHeight();
            //int height = //ScrollViewKidImage.getHeight();
            //LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width, height);
            kidImage.setLayoutParams(new ActionBar.LayoutParams((int) (Main2Activity.Mainwidth * 0.3), (int) (Main2Activity.Mainwidth * 0.3)));
            final int finalIndx = indx;
            kidImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    kidname_STR = users.get(finalIndx).getFirstname();
                    ((TextView) activity.findViewById(R.id.kidNameListEvent)).setText(users.get(finalIndx).getFirstname());
                    kidID = users.get(finalIndx).getUserParseID();
                    setallevent(activity,Current_day,kidID);
                }
            });
            lay.addView(kidImage);
            LayotKidImage.addView(lay);
        }
    }
    //This function displays events by choosing a boy or a different day
    protected static void setallevent(Activity activity,String day,String kidid){
        Log.i("SCHEDULEMANGER","2 kidid:"+kidID+" day:"+day);

        EventDB=new EventDataSource(activity);
        EventDB.open();
        allevents=new EventArray();
        allevents.setListevent(EventDB.getAllEvents());
        EventDB.close();
        LayoutEvent.addView(new TextView(activity));
        LayoutEvent.removeAllViews();
        final ArrayList<Event> kidsevent=allevents.GET_EVENTS_BY_KID_ID_AND_DAY(kidid,day);
        Log.i("SCHEDULEMANGER","3 size:"+kidsevent.size());
        for(int indx=0;indx<kidsevent.size();indx++){
            Log.i("SCHEDULEMANGER","4 kidid:"+kidID+" ");
            LinearLayout layout=new LinearLayout(activity);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setBackgroundResource(R.drawable.backgroundevent);
            layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            //event name
            TextView EventName=new TextView(activity);
            EventName.setText("EVENT:" + kidsevent.get(indx).getName());
            EventName.setTextSize(20);
            EventName.setTypeface(null, Typeface.BOLD);
            EventName.setTextColor(Color.BLACK);
            //the time when event start
            TextView Start=new TextView(activity);
            Start.setText("START:" + kidsevent.get(indx).getStart_time());
            Start.setTextSize(20);
            Start.setTypeface(null, Typeface.BOLD);
            Start.setTextColor(Color.BLACK);
            //the time when event end
            TextView End=new TextView(activity);
            End.setText("END:" + kidsevent.get(indx).getEnd_time());
            End.setTextSize(20);
            End.setTypeface(null, Typeface.BOLD);
            End.setTextColor(Color.BLACK);
            TextView Address=new TextView(activity);
            Address.setText("ADDRESS:" + kidsevent.get(indx).getAddress());
            Address.setTextSize(20);
            Address.setTypeface(null, Typeface.BOLD);
            Address.setTextColor(Color.BLACK);
            layout.addView(EventName);
            layout.addView(Address);
            layout.addView(Start);
            layout.addView(End);
            //event date
            if(!kidsevent.get(indx).getDate().isEmpty()){
                TextView date=new TextView(activity);
                date.setText("DATE:" + kidsevent.get(indx).getDate());
                date.setTextSize(20);
                date.setTypeface(null, Typeface.BOLD);
                date.setTextColor(Color.BLACK);
                layout.addView(date);
            }
            //layout.setGravity(View.TEXT_ALIGNMENT_CENTER);
            final int finalIndx = indx;
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment newfram = new Schedule_SingelEvent();
                    Main2Activity.Lest_Fram = Main2Activity.current_Fram;
                    Main2Activity.current_Fram = newfram;
                    FragmentTransaction mytransaction2 = Main2Activity.myFramManager.beginTransaction();
                    mytransaction2.hide(Main2Activity.Lest_Fram);
                    mytransaction2.add(R.id.MainRelative, Main2Activity.current_Fram, "Schedule_SingelEvent");
                    mytransaction2.commit();
                    Schedule_SingelEvent.CurrentEvent = kidsevent.get(finalIndx);
                }
            });
            LayoutEvent.addView(layout);
        }
    }
}

