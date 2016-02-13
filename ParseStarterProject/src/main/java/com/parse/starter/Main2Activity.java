package com.parse.starter;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Main2Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    //database values
    private static UsersDataSource UserDB;//user database
    static protected NavigationView navigationView;
    static protected int Mainheight=0;
    static protected int Mainwidth=0;

    static protected ArrayList<User> Mainuserlist=new ArrayList<User>();
    static protected boolean parent=true;
    static protected boolean Addkidscreen = false;
    static protected boolean Updateuser=false;
    static protected boolean Getchatromsid=false;
    static protected boolean isInForeground = false;
    static protected boolean Chatwinopen = false;
    static protected boolean ContactList = false;
    private static int RESULT_LOAD_IMG = 1;
    protected static String imgDecodableString;
    private CheckEventService s;

    //fragment values
    static protected FragmentManager myFramManager;
    static protected Fragment Login_Fram = new LogIn();
    static protected Fragment SignUp_Fram = new SignUp_Fram();
    static protected Fragment Splach_Fram = new splachscreen();
    static protected Fragment ChatList_Fram;
    static protected Fragment Schedule_Fram;
    static protected Fragment Setting_Fram;
    static protected Fragment Addkid_Fram = new Add_Kid();
    static protected Fragment current_Fram = new Fragment();
    static protected Fragment Lest_Fram = new Fragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParseAnalytics.trackAppOpened(getIntent());
        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Mainheight=dm.heightPixels;
        Mainwidth=dm.widthPixels;

        myFramManager=getFragmentManager();
        FragmentTransaction mytransaction1 = myFramManager.beginTransaction();
        mytransaction1.add(R.id.MainRelative, Splach_Fram, "Splach_Fram");
        mytransaction1.commit();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setTitle("SAFE KID"); // set the top title
        getSupportActionBar().hide();
        //gat user list from db
        UserDB=new UsersDataSource(this);
        UserDB.open();
        Mainuserlist=UserDB.getAllUsers();
        UserDB.close();
        if(Mainuserlist.size()>0){
            if(Mainuserlist.get(0).isPerant()){//case user is parent
                UpdateUserDetails();
                getchatids();
                userstatus();
                Log.i("openfram", "1");

                final boolean[] run = {true};
                Thread thread=new Thread(){
                    public void run(){
                        while (run[0]){
                            try {
                                Log.i("openfram","2");
                                sleep(2000);
                                if(Getchatromsid&&Updateuser){
                                    Log.i("openfram", "3");
                                    openFram();
                                    if(getSupportActionBar().isShowing()){
                                        getSupportActionBar().show();
                                    }
                                    run[0] =false;
                                }
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                };thread.start();
            }
        }else {
            FragmentTransaction mytransaction2 = myFramManager.beginTransaction();
            mytransaction2.add(R.id.MainRelative, Login_Fram, "Login_Fram");
            mytransaction2.add(R.id.MainRelative, SignUp_Fram, "SignUp_Fram");
            mytransaction2.hide(Splach_Fram);
            mytransaction2.hide(SignUp_Fram);
            mytransaction2.commit();
        }
    }
    public void UpdateUserDetails(){
        final ArrayList<String> allids = new ArrayList<String>();
        for (int indx = 0; indx < Mainuserlist.size(); indx++) {
            allids.add(Mainuserlist.get(indx).getUserParseID());
        }
        ParseQuery<ParseObject> usertable = ParseQuery.getQuery("USER");
        usertable.whereContainedIn("objectId",allids);
        usertable.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> objects, ParseException e) {
                if(e==null){
                    for(int indx=0;indx<objects.size();indx++){
                        ParseFile fileObject = objects.get(indx).getParseFile("Image");
                        final int finalIndx1 = indx;
                        fileObject.getDataInBackground(new GetDataCallback() {
                            public void done(byte[] data, ParseException e2) {
                                if (e2 == null) {
                                    Bitmap btm = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    User newuser=new User();
                                    for(int x=0;x<Mainuserlist.size();x++){
                                        if(Mainuserlist.get(x).getUserParseID().matches(objects.get(finalIndx1).getObjectId())){
                                            UserDB = new UsersDataSource(Main2Activity.this);
                                            UserDB.open();
                                            Mainuserlist.get(x).setUserImage(btm);
                                            Mainuserlist.get(x).setFirstname(objects.get(finalIndx1).getString("F_name"));
                                            Mainuserlist.get(x).setLestName(objects.get(finalIndx1).getString("L_name"));
                                            Mainuserlist.get(x).setUserName(objects.get(finalIndx1).getString("User_Name"));
                                            Mainuserlist.get(x).setAddress(objects.get(finalIndx1).getString("Address"));
                                            Mainuserlist.get(x).setBirthday(objects.get(finalIndx1).getString("Birthday"));
                                            Mainuserlist.get(x).setPhoneNumber(objects.get(finalIndx1).getString("Phone_Number"));
                                            Mainuserlist.get(x).setEmail(objects.get(finalIndx1).getString("Email"));
                                            Mainuserlist.get(x).setPerant(objects.get(finalIndx1).getBoolean("Parent"));
                                            if(x==0){
                                                Mainuserlist.get(x).setPassword(objects.get(finalIndx1).getString("Password"));
                                            }
                                            UserDB.UpdateUser(Mainuserlist.get(x));
                                            UserDB.close();
                                            Log.i("openfram", "inparse");

                                        }
                                    }
                                }
                            }
                        });
                    }
                    Updateuser=true;
                }
            }
        });
    }
    public void openFram(){
        ChatList_Fram = new Chat_Contact_List();
        Schedule_Fram = new Schedule_Mange();
        Setting_Fram = new Setting_Fram();
        FragmentTransaction mytransaction2 = myFramManager.beginTransaction();
        mytransaction2.add(R.id.MainRelative, ChatList_Fram, "ChatList_Fram");
        mytransaction2.add(R.id.MainRelative, Schedule_Fram, "Schedule_Fram");
        mytransaction2.add(R.id.MainRelative, Setting_Fram, "Setting_Fram");
        mytransaction2.add(R.id.MainRelative, Addkid_Fram, "Addkid_Fram");
        mytransaction2.hide(Addkid_Fram);
        mytransaction2.hide(Splach_Fram);
        mytransaction2.hide(Schedule_Fram);
        mytransaction2.hide(Setting_Fram);
        mytransaction2.hide(ChatList_Fram);
        if (Mainuserlist.size() == 1) {
            current_Fram = Addkid_Fram;
        } else {
            current_Fram = ChatList_Fram;
        }
        mytransaction2.show(current_Fram);
        mytransaction2.commit();
        //Chat_Contact_List.fulllist(this);
    }
    public void getchatids(){
        Log.i("openfram","chatid--"+Integer.toString(Mainuserlist.size()));
        UserDB = new UsersDataSource(this);
        UserDB.open();
        Mainuserlist = UserDB.getAllUsers();
        UserDB.close();
        for(int usindx=1;usindx<Mainuserlist.size();usindx++){
            final int cpyusindx=usindx;
            ArrayList<String> starray=new ArrayList<String>();
            starray.add(Mainuserlist.get(0).getUserParseID());
            starray.add(Mainuserlist.get(usindx).getUserParseID());
            ParseQuery<ParseObject> connectiontable = ParseQuery.getQuery("ChatRoom");
            connectiontable.whereContainedIn("usersid", starray);
            connectiontable.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(final List<ParseObject> objects, ParseException e3) {
                    if (e3 == null) {
                        Log.i("openfram", "inparse");
                        for (int indx = 0; indx < objects.size(); indx++) {
                            UserDB.open();
                            Mainuserlist.get(cpyusindx).setChatroom(objects.get(indx).getObjectId());
                            UserDB.UpdateUser(Mainuserlist.get(cpyusindx));
                            UserDB.close();
                            Log.i("openfram", "inparse");
                        }
                        Getchatromsid=true;
                    }
                }
            });
        }
    }
    public boolean userstatus() {
        ParseQuery<ParseObject> usertable = ParseQuery.getQuery("USER");
        usertable.getInBackground(Mainuserlist.get(0).getUserParseID(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    getSupportActionBar().show();
                    if (object.getBoolean("Parent")) {
                        parent = true;
                    } else {
                        parent = false;
                    }
                }
            }
        });
        if (parent) {
            navigationView.inflateMenu(R.menu.activity_main2_drawer);
        } else {
            navigationView.inflateMenu(R.menu.kidmenu);
        }
        return parent;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.addkid) {
            Chatwinopen=false;
            Lest_Fram = current_Fram;
            current_Fram = Addkid_Fram;
            FragmentTransaction mytransaction3 = myFramManager.beginTransaction();
            mytransaction3.hide(Lest_Fram);
            mytransaction3.show(current_Fram);
            mytransaction3.commit();
        } else if (id == R.id.setting) {
            Chatwinopen=false;
            Lest_Fram = current_Fram;
            current_Fram = Setting_Fram;
            FragmentTransaction mytransaction3 = myFramManager.beginTransaction();
            mytransaction3.hide(Lest_Fram);
            mytransaction3.show(current_Fram);
            mytransaction3.commit();

        } else if (id == R.id.chat) {
            Chatwinopen=false;
            Lest_Fram = current_Fram;
            current_Fram = ChatList_Fram;
            FragmentTransaction mytransaction3 = myFramManager.beginTransaction();
            mytransaction3.hide(Lest_Fram);
            mytransaction3.show(current_Fram);
            mytransaction3.commit();

        } else if (id == R.id.location) {

        } else if (id == R.id.schedule) {
            Chatwinopen=false;
            Lest_Fram = current_Fram;
            current_Fram = Schedule_Fram;
            FragmentTransaction mytransaction3 = myFramManager.beginTransaction();
            mytransaction3.hide(Lest_Fram);
            mytransaction3.show(current_Fram);
            mytransaction3.commit();

        } else if (id == R.id.statisc) {

        }else if (id == R.id.Kchat) {
            Chatwinopen=false;
            Lest_Fram = current_Fram;
            current_Fram = ChatList_Fram;
            FragmentTransaction mytransaction3 = myFramManager.beginTransaction();
            mytransaction3.hide(Lest_Fram);
            mytransaction3.show(current_Fram);
            mytransaction3.commit();

        }else if (id == R.id.Ksetting) {
            Chatwinopen=false;
            Lest_Fram = current_Fram;
            current_Fram = Setting_Fram;
            FragmentTransaction mytransaction3 = myFramManager.beginTransaction();
            mytransaction3.hide(Lest_Fram);
            mytransaction3.show(current_Fram);
            mytransaction3.commit();

        }else if (id == R.id.Kalarm) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isInForeground = true;
        if (!parent) {
            Intent intent = new Intent(this, CheckEventService.class);
            startService(intent);
            bindService(intent, mConnection, Context.BIND_ALLOW_OOM_MANAGEMENT);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isInForeground = false;
        if (!parent)
            unbindService(mConnection);
    }
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder binder) {
            CheckEventService.MyBinder b = (CheckEventService.MyBinder) binder;
            s = b.getService();
        }
        public void onServiceDisconnected(ComponentName className) {
            s = null;
        }
    };
    public void loadImagefromGallery(View view) {
        if (view == findViewById(R.id.SignUp_ChooseImage)) {
            Addkidscreen = false;
        } else {
            Addkidscreen = true;
        }
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                // Set the Image in ImageView after decoding the String
                if (Addkidscreen) {//in case its to add sign up screen
                    ImageView imageView = (ImageView) findViewById(R.id.AddKid_Image);
                    imageView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
                } else {//in case its to add kid screen
                    ImageView imageView = (ImageView) findViewById(R.id.SignUp_Image);
                    imageView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
                }
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }
    public void GoToSignUp(View view) {
        FragmentTransaction mytransaction2 = myFramManager.beginTransaction();
        mytransaction2.hide(Login_Fram);
        mytransaction2.show(SignUp_Fram);
        mytransaction2.commit();
    }
    public void logout(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setMessage("log out ?");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                UserDB = new UsersDataSource(Main2Activity.this);
                UserDB.open();
                UserDB.DeleteAllUsers();
                UserDB.close();
            }
        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        }).show();
    }
    public void ChooseDay(View view) {
        String day = "";
        if (view == view.findViewById(R.id.Sunday_btn)) {
            day = "Sunday";
        } else if (view == view.findViewById(R.id.Monday_btn)) {
            day = "Monday";
        } else if (view == view.findViewById(R.id.Tuesday_btn)) {
            day = "Tuesday";
        } else if (view == view.findViewById(R.id.Wednesday_btn)) {
            day = "Wednesday";
        } else if (view == view.findViewById(R.id.Thursday_btn)) {
            day = "Thursday";
        } else if (view == view.findViewById(R.id.Friday_btn)) {
            day = "Friday";
        } else if (view == view.findViewById(R.id.Saturday_btn)) {
            day = "Saturday";
        }
        Schedule_Mange.Current_day = day;
        //Search events for selected day
        TextView textView=new TextView(Main2Activity.this);
        textView=(TextView) findViewById(R.id.ListEvent_Today);
        textView.setText(Schedule_Mange.Current_day);
        Schedule_Mange.setallevent(Main2Activity.this);
    }
}
