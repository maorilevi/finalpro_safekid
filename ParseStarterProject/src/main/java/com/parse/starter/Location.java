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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Location extends Fragment implements OnMapReadyCallback {

    static protected GoogleMap googleMap;

    static final LatLng TutorialsPoint = new LatLng(21 , 57);

    String Latitude;
    String Longitude;
    static public Double Latitude2=1.22;
    static public Double Longitude2=1.22;

    protected static String LatitudeParant1 ;
    protected static String LongitudeParant1;
    static protected String kidID="";
    static protected String kidname_STR="";

    static protected LinearLayout IMG_Liner;
    static protected TextView Kidnemes;
    protected static String ParentId;
    protected static Button event;
    protected static Button event1;
    protected static int flag;
    protected static String TitleName="My_Location";
    boolean k=true;
    static public boolean LOCATIONISON=false;
    static protected ArrayList<User> userslist;
    private UsersDataSource UDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myview = inflater.inflate(R.layout.location, container, false);
        LOCATIONISON = true;

        UDB = new UsersDataSource(getActivity());
        UDB.open();
        userslist = UDB.getAllUsers();
        UDB.close();
        IMG_Liner = new LinearLayout(getActivity());
        IMG_Liner = (LinearLayout) myview.findViewById(R.id.Location_Kidimg);
        Kidnemes = (TextView) myview.findViewById(R.id.Location_Kidname);


        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        }
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        onMapReady(googleMap);
        SetImageeUsers(getActivity());
        return myview;
    }

    protected static void SetImageeUsers(final Activity activity){
        IMG_Liner.removeAllViews();
        for (int indx=1;indx<userslist.size();indx++){
            if(indx==1){
                Kidnemes.setText(userslist.get(indx).getFirstname());
                kidname_STR=userslist.get(indx).getFirstname();
                kidID=userslist.get(indx).getUserParseID();
                getlocation(kidID,activity);
            }
            LinearLayout lay=new LinearLayout(activity);
            lay.setOrientation(LinearLayout.VERTICAL);
            lay.setLayoutParams(new ViewGroup.LayoutParams((int)
                    (Main2Activity.Mainwidth * 0.3), (int) (Main2Activity.Mainwidth * 0.3)));
            lay.setPadding(5,5,5,5);
            ImageView kidImage = new ImageView(activity);
            kidImage.setImageBitmap(userslist.get(indx).getUserImage());
            kidImage.setLayoutParams(new ActionBar.LayoutParams((int)
                    (Main2Activity.Mainwidth * 0.3), (int) (Main2Activity.Mainwidth * 0.3)));
            final int finalIndx = indx;
            kidImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    kidname_STR = userslist.get(finalIndx).getFirstname();
                    Kidnemes.setText(userslist.get(finalIndx).getFirstname());
                    kidID = userslist.get(finalIndx).getUserParseID();
                    getlocation(kidID,activity);
                }
            });
            lay.addView(kidImage);
            IMG_Liner.addView(lay);
        }
    }

    static public void getlocation(String kidID,final Activity activity){
        JSONObject data = new JSONObject();
        try {
            data.put("status", "GET_LOCATION");
        } catch (JSONException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        ParseQuery pushQuery = ParseInstallation.getQuery();
        pushQuery.whereEqualTo("channels", kidID); // Set the channel
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery);// Set our Installation query
        push.setData(data);
        push.sendInBackground();
        final boolean[] run = {true};
        new Thread() {
            public void run() {
                while (run[0]) {
                    try {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (JSONCustomReceiver.RECIEV_LOCATION){
                                    Toast.makeText(activity,"geting location",Toast.LENGTH_SHORT).show();
                                    JSONCustomReceiver.RECIEV_LOCATION=false;
                                    updatemap(googleMap);
                                }
                            }
                        });
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
    static public void updatemap(GoogleMap googleMap){
        if(googleMap!=null){
            LatLng KidLocation = new LatLng(Latitude2, Longitude2);
            googleMap.clear();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(KidLocation, 16));
            googleMap.addMarker(new MarkerOptions().position(KidLocation).title(Latitude2.toString() + '\n' + Longitude2.toString()));
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
    }
}
