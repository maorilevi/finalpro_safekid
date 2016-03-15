package com.parse.starter;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Admin on 05/03/2016.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Statics_Fram extends Fragment {
    static protected LinearLayout LayotKidImage;
    static protected LinearLayout dayreport;
    static protected LinearLayout monthreport;
    static protected LinearLayout yearreport;
    static protected Calendar cal;


    static protected ArrayList<User> users = new ArrayList<User>();
    static protected TextView userName;
    static protected String kidID="";
    static protected String kidname_STR="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View statics = inflater.inflate(R.layout.static_fram, container, false);

        dayreport=new LinearLayout(getActivity());
        dayreport=(LinearLayout)statics.findViewById(R.id.Static_dailyreportLiner);
        monthreport=new LinearLayout(getActivity());
        monthreport=(LinearLayout)statics.findViewById(R.id.Static_monthlyreportLiner);
        yearreport=new LinearLayout(getActivity());
        yearreport=(LinearLayout)statics.findViewById(R.id.Static_AnnualReportLLiner);



        LayotKidImage=new LinearLayout(getActivity());
        LayotKidImage=(LinearLayout)statics.findViewById(R.id.Static_Layoutimage);
        userName=new TextView(getActivity());
        userName=(TextView)statics.findViewById(R.id.Static_kidnam);
        UsersDataSource UserDB=new UsersDataSource(getActivity());
        UserDB.open();
        users = UserDB.getAllUsers();
        UserDB.close();
        SetImageeUsers(getActivity());
        return statics;
    }
    protected static void SetImageeUsers(final Activity activity){
        LayotKidImage.removeAllViews();
        for (int indx=1;indx<users.size();indx++){
            if(indx==1){
                userName.setText(users.get(indx).getFirstname());
                kidname_STR=users.get(indx).getFirstname();
                kidID=users.get(indx).getUserParseID();
                //OPEN_ALL_STATISTICS(kidID,activity);
            }
            LinearLayout lay=new LinearLayout(activity);
            lay.setOrientation(LinearLayout.VERTICAL);
            lay.setLayoutParams(new ViewGroup.LayoutParams((int) (Main2Activity.Mainwidth * 0.3), (int) (Main2Activity.Mainwidth * 0.3)));
            lay.setPadding(5,5,5,5);
            ImageView kidImage = new ImageView(activity);
            kidImage.setImageBitmap(users.get(indx).getUserImage());
            kidImage.setLayoutParams(new ActionBar.LayoutParams((int) (Main2Activity.Mainwidth * 0.3), (int) (Main2Activity.Mainwidth * 0.3)));
            final int finalIndx = indx;
            kidImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    kidname_STR = users.get(finalIndx).getFirstname();
                    ((TextView) activity.findViewById(R.id.Static_kidnam)).setText(users.get(finalIndx).getFirstname());
                    kidID = users.get(finalIndx).getUserParseID();
              }
            });
            lay.addView(kidImage);
            LayotKidImage.addView(lay);
        }
    }
    /*public static void OPEN_ALL_STATISTICS(String kidid,Activity activity){
        WarningDataSource WDB=new WarningDataSource(activity);
        WDB.open();
        ArrayList<WarningMessage> warlist=WDB.getAllWarning();
        WDB.close();
        if(warlist.size()>0){
            for(int indx=0;indx<warlist.size();indx++){
                cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"));
                String[] date = cal.getTime().toGMTString().split("GMT");
                if(warlist.get(indx).getKid_Parse_id().matches(kidID)){
                    if(warlist.get(indx).MATCHES_TO_THIS_DAY()){
                        TextView textView=new TextView(activity);
                        textView.setText("date:"+warlist.get(indx).getDate()+"\nkid name:"+warlist.get(indx).getKid_name());
                    }else if(warlist.get(indx).MATCHES_TO_THIS_MONTH()){
                        TextView textView=new TextView(activity);
                        textView.setText("date:"+warlist.get(indx).getDate()+"\nkid name:"+warlist.get(indx).getKid_name());

                    }else if(warlist.get(indx).MATCHES_TO_THIS_YEAR()){
                        TextView textView=new TextView(activity);
                        textView.setText("date:"+warlist.get(indx).getDate()+"\nkid name:"+warlist.get(indx).getKid_name());
                    }
                }
            }
        }else{
            TextView warning_4_this_day=new TextView(activity);
            warning_4_this_day.setText("noting for this day");
            dayreport.addView(warning_4_this_day);

            TextView warning_4_this_month=new TextView(activity);
            warning_4_this_month.setText("noting for this month");
            monthreport.addView(warning_4_this_month);

            TextView warning_4_this_year=new TextView(activity);
            warning_4_this_year.setText("noting for this year");
            yearreport.addView(warning_4_this_year);
        }
    }*/
}
