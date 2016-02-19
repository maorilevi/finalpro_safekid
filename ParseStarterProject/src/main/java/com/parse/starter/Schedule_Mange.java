package com.parse.starter;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
@TargetApi(Build.VERSION_CODES.HONEYCOMB)

public class Schedule_Mange extends Fragment {

    static protected boolean newevent=false;
    private UsersDataSource UserDB;//user database

    static protected TextView userName;
    static protected ArrayList<User> users = new ArrayList<User>();
    static protected Button newEventbtn;
    //event Variable
    static protected String Current_day="";
    static protected String EventID="";
    //kid variable
    static protected String kidID="";
    static protected String kidname_STR="";
    static protected String ParentID="";
    //
    static protected ScrollView ScrollViewEvent;
    static protected LinearLayout LayoutEvent;
    static protected HorizontalScrollView ScrollViewKidImage;
    static protected LinearLayout LayotKidImage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View schedule= inflater.inflate(R.layout.schedule_activity, container, false);
        UserDB=new UsersDataSource(getActivity());
        UserDB.open();
        users = UserDB.getAllUsers();
        UserDB.close();
        userName=new TextView(getActivity());
        userName=(TextView)schedule.findViewById(R.id.kidNameListEvent);
        //set default day
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
        Calendar calendar = Calendar.getInstance();
        Current_day = dayFormat.format(calendar.getTime());
        ((TextView) schedule.findViewById(R.id.ListEvent_Today)).setText(Current_day);

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
                setallevent(activity);
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
                    setallevent(activity);
                }
            });
            lay.addView(kidImage);
            LayotKidImage.addView(lay);
        }


    }
    //day function




    //This function displays events by choosing a boy or a different day
    public static void setallevent(final Activity activity){
        LayoutEvent.addView(new TextView(activity));
        LayoutEvent.removeAllViews();
        final ParseQuery<ParseObject> EventTable = ParseQuery.getQuery("Event");
        EventTable.whereEqualTo("Kid_ID", kidID);
        EventTable.whereEqualTo("Day",Current_day);
        EventTable.findInBackground(new FindCallback<ParseObject>() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void done(final List<ParseObject> results, ParseException e) {
                if (e == null) {
                    if (results.size() > 0) {
                        for (int indx = 0; indx < results.size(); indx++) {
                            final int loc = indx;
                            final Event event1 = new Event();
                            event1.setName(results.get(indx).getString("NameEvent"));
                            event1.setDay(results.get(indx).getString("Day"));
                            event1.setAddress(results.get(indx).getString("Address"));
                            event1.setStart_time(results.get(indx).getString("StartEvent"));
                            event1.setEnd_time(results.get(indx).getString("EndEvent"));
                            event1.setKidID(results.get(indx).getString("Kid_ID"));
                            event1.setKidName(kidname_STR);
                            event1.setParseid(results.get(indx).getObjectId());
                            Button event = new Button(activity);
                            event.setText(results.get(indx).getString("NameEvent") +
                                    "\nstart:" + results.get(indx).getString("StartEvent") +
                                    "\nEnd:" + results.get(indx).getString("EndEvent"));
                            event.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Fragment newfram = new Schedule_SingelEvent();
                                    Main2Activity.Lest_Fram = Main2Activity.current_Fram;
                                    Main2Activity.current_Fram = newfram;
                                    FragmentTransaction mytransaction2 = Main2Activity.myFramManager.beginTransaction();
                                    mytransaction2.hide(Main2Activity.Lest_Fram);
                                    mytransaction2.add(R.id.MainRelative, Main2Activity.current_Fram, "newMessage");
                                    mytransaction2.commit();
                                    Schedule_SingelEvent.CurrentEvent = event1;
                                }
                            });
                            LayoutEvent.addView(event);
                        }
                    }
                }
            }
        });
    }
}

