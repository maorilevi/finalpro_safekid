package com.parse.starter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.parse.ParseAnalytics;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Main2Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //google maps
    private static int FUNC_ADDRESS1_IMAGE2 = 0;
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    protected static LatLng Lat;
    protected static String AddressEvent;



    public static final String BROADCAST = "PACKAGE_NAME.android.action.MyScheduleReceiver";
    UPDATE_DETAILS_CLASS MY_UPDATE = new UPDATE_DETAILS_CLASS();


    static protected TextView text_date;
    static protected TextView text_Time;
    static protected ActionBar MainActionBar;
    //database values
    private static UsersDataSource UserDB;//user database
    static protected NavigationView navigationView;
    static protected int Mainheight = 0;
    static protected int Mainwidth = 0;

    static protected ArrayList<User> Mainuserlist = new ArrayList<User>();
    static protected boolean parent = true;
    static protected int screncount = 0;
    static protected boolean Addkidscreen = false;
    static protected boolean Updateuser = false;
    static protected boolean Getchatromsid = false;
    static protected boolean isInForeground = false;
    static protected boolean Chatwinopen = false;
    static protected boolean ContactList = false;
    static protected Activity Mainact;
    private static int RESULT_LOAD_IMG = 1;
    protected static String imgDecodableString;
    private CheckEventService s;

    //fragment values
    static protected FragmentManager myFramManager;
    static protected Fragment Login_Fram = new LogIn();
    static protected Fragment SignUp_Fram = new SignUp();
    static protected Fragment Splach_Fram = new splachscreen();
    static protected Fragment ChatList_Fram;
    static protected Fragment Schedule_Fram;
    static protected Fragment Setting_Fram;
    static protected Fragment Location_Fram;
    static protected Fragment Addkid_Fram = new Add_Kid();
    static protected Fragment current_Fram = new Fragment();
    static protected Fragment Lest_Fram = new Fragment();
    static protected Fragment statistic_Fram;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParseAnalytics.trackAppOpened(getIntent());
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        isInForeground = true;
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Mainact = Main2Activity.this;
        text_date = new TextView(this);
        text_Time = new TextView(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Mainheight = dm.heightPixels;
        Mainwidth = dm.widthPixels;
        myFramManager = getFragmentManager();
        FragmentTransaction mytransaction1 = myFramManager.beginTransaction();
        mytransaction1.add(R.id.MainRelative, Splach_Fram, "Splach_Fram");
        mytransaction1.commit();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        MainActionBar = getSupportActionBar();
        getSupportActionBar().setTitle("SAFE KID"); // set the top title
        getSupportActionBar().setIcon(R.drawable.menu_item_chat);
        getSupportActionBar().hide();
        //gat user list from db
        UserDB = new UsersDataSource(this);
        UserDB.open();
        Mainuserlist = UserDB.getAllUsers();
        UserDB.close();
        if (Mainuserlist.size() > 0) {
            parent = Mainuserlist.get(0).isPerant();
            if (Mainuserlist.get(0).isPerant()) {//case user is parent
                MY_UPDATE.UPDATE_ALL_DETAILS_AND_OPEN_FRAGMENT(Main2Activity.this, false, true);
                //closeservice();
            } else {
                MY_UPDATE.UPDATE_ALL_DETAILS_AND_OPEN_FRAGMENT(Main2Activity.this, false, true);
            }
        } else {
            FragmentTransaction mytransaction2 = myFramManager.beginTransaction();
            mytransaction2.add(R.id.MainRelative, Login_Fram, "Login_Fram");
            mytransaction2.add(R.id.MainRelative, SignUp_Fram, "SignUp_Fram");
            mytransaction2.hide(Splach_Fram);
            mytransaction2.hide(SignUp_Fram);
            mytransaction2.commit();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.addkid) {
            Chatwinopen = false;
            Lest_Fram = current_Fram;
            current_Fram = Addkid_Fram;
            FragmentTransaction mytransaction3 = myFramManager.beginTransaction();
            mytransaction3.hide(Lest_Fram);
            mytransaction3.show(current_Fram);
            mytransaction3.commit();
            getSupportActionBar().setIcon(R.drawable.menu_item_addkid);
        } else if (id == R.id.setting) {
            Chatwinopen = false;
            Lest_Fram = current_Fram;
            current_Fram = Setting_Fram;
            FragmentTransaction mytransaction3 = myFramManager.beginTransaction();
            mytransaction3.hide(Lest_Fram);
            mytransaction3.show(current_Fram);
            mytransaction3.commit();
            getSupportActionBar().setIcon(R.drawable.menu_item_setting);


        } else if (id == R.id.chat) {
            Chatwinopen = false;
            Lest_Fram = current_Fram;
            current_Fram = ChatList_Fram;
            FragmentTransaction mytransaction3 = myFramManager.beginTransaction();
            mytransaction3.hide(Lest_Fram);
            mytransaction3.show(current_Fram);
            mytransaction3.commit();
            getSupportActionBar().setIcon(R.drawable.menu_item_chat);

        } else if (id == R.id.location) {
            Chatwinopen = false;
            Lest_Fram = current_Fram;
            current_Fram = Location_Fram;
            FragmentTransaction mytransaction3 = myFramManager.beginTransaction();
            mytransaction3.hide(Lest_Fram);
            mytransaction3.show(current_Fram);
            mytransaction3.commit();
            getSupportActionBar().setIcon(R.drawable.menu_item_location);

        } else if (id == R.id.schedule) {
            Chatwinopen = false;
            Lest_Fram = current_Fram;
            current_Fram = Schedule_Fram;
            FragmentTransaction mytransaction3 = myFramManager.beginTransaction();
            mytransaction3.hide(Lest_Fram);
            mytransaction3.show(current_Fram);
            mytransaction3.commit();
            getSupportActionBar().setIcon(R.drawable.calicon);


        } else if (id == R.id.statisc) {
            Chatwinopen = false;
            Lest_Fram = current_Fram;
            current_Fram = statistic_Fram;
            FragmentTransaction mytransaction3 = myFramManager.beginTransaction();
            mytransaction3.hide(Lest_Fram);
            mytransaction3.show(current_Fram);
            mytransaction3.commit();
            getSupportActionBar().setIcon(R.drawable.menu_item_statisc);

        } else if (id == R.id.Kchat) {
            Chatwinopen = false;
            Lest_Fram = current_Fram;
            current_Fram = ChatList_Fram;
            FragmentTransaction mytransaction3 = myFramManager.beginTransaction();
            mytransaction3.hide(Lest_Fram);
            mytransaction3.show(current_Fram);
            mytransaction3.commit();
            getSupportActionBar().setIcon(R.drawable.menu_item_chat);


        } else if (id == R.id.Ksetting) {
            Chatwinopen = false;
            Lest_Fram = current_Fram;
            current_Fram = Setting_Fram;
            FragmentTransaction mytransaction3 = myFramManager.beginTransaction();
            mytransaction3.hide(Lest_Fram);
            mytransaction3.show(current_Fram);
            mytransaction3.commit();
            getSupportActionBar().setIcon(R.drawable.menu_item_setting);
        } else if (id == R.id.Kalarm) {
            getSupportActionBar().setIcon(R.drawable.alarmicom);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isInForeground = true;
        UserDB = new UsersDataSource(this);
        UserDB.open();
        Mainuserlist = UserDB.getAllUsers();
        UserDB.close();
        final boolean[] run = {true};
        new Thread() {
            public void run() {
                while (run[0]) {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (Mainuserlist.size() > 0) {
                                    if (!Mainuserlist.get(0).isPerant()) {
                                        Intent i = new Intent(Main2Activity.this, CheckEventService.class);
                                        startService(i);
                                        IntentFilter intentFilter = new IntentFilter(BROADCAST);
                                        MyScheduleReceiver myReceiver = new MyScheduleReceiver();
                                        registerReceiver(myReceiver, intentFilter);
                                        Intent intent2 = new Intent(BROADCAST);
                                        Bundle extras = new Bundle();
                                        extras.putString("send_data", "test");
                                        intent2.putExtras(extras);
                                        sendBroadcast(intent2);
                                        run[0] = false;
                                    } else {
                                        run[0] = false;
                                    }
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


    public static void update(final Activity activity) {
        final boolean[] run = {true};
        new Thread() {
            public void run() {
                while (run[0]) {
                    try {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (Add_Kid.CREATE_CHAT_IDS && Add_Kid.USER_IN_DATA_BASE) {
                                    AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                                    alertDialog.setTitle("successful update");
                                    alertDialog.setMessage("Kid user name is:"
                                            + Add_Kid.User_Name_STR +
                                            "\nKid password:"
                                            + Add_Kid.Password_STR);
                                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Add_Kid.CLEAN_TEXT_BOX(activity);
                                            run[0] = false;
                                        }
                                    });
                                    alertDialog.show();
                                    run[0] = false;
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

    @Override
    protected void onPause() {
        super.onPause();
        isInForeground = false;
        //unbindService(mConnection);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder binder) {
            CheckEventService.MyBinder b = (CheckEventService.MyBinder) binder;
            s = b.getService();
        }

        public void onServiceDisconnected(ComponentName className) {
            //s = null;
        }
    };

    public void PicAddraesEvent(View view) {
        try {
            FUNC_ADDRESS1_IMAGE2=1;
            if(view==findViewById(R.id.SignUp_Address)){
                screncount = 4;
            }else if(view==findViewById(R.id.AddKid_Address)){
                screncount = 5;
            }else if(view==findViewById(R.id.SingleEv_Address)){
                screncount = 6;
            }else if(view==findViewById(R.id.Setting_Address)) {
                screncount = 7;
            }
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            builder.setLatLngBounds(BOUNDS_MOUNTAIN_VIEW);
            startActivityForResult((builder.build(this)), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException
                | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }
    public void loadImagefromGallery(View view) {
        FUNC_ADDRESS1_IMAGE2=2;
        if (view == findViewById(R.id.SignUp_ChooseImage)) {
            screncount = 1;
            Addkidscreen = false;
        } else if (view == findViewById(R.id.AddKid_ChooseImage)) {
            Addkidscreen = true;
            screncount = 2;
        } else if (view == findViewById(R.id.Setting_changeimgBTN)) {
            Setting.userchangeimage = true;
            screncount = 3;
        }
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (FUNC_ADDRESS1_IMAGE2 == 2) {
                super.onActivityResult(requestCode, resultCode, data);
                Toast.makeText(this, "image", Toast.LENGTH_SHORT).show();
                // When an Image is picked
                if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
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
                    switch (screncount) {
                        case 1:
                            ImageView imageView = (ImageView) findViewById(R.id.SignUp_Image);
                            imageView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
                            break;
                        case 2:
                            ImageView imageView2 = (ImageView) findViewById(R.id.AddKid_Image);
                            imageView2.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
                            break;
                        case 3:
                            ImageView imageView3 = (ImageView) findViewById(R.id.Setting_User_Image);
                            imageView3.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
                            break;
                        default:
                            break;
                    }
                } else {
                    Toast.makeText(this, "You haven't picked Image",
                            Toast.LENGTH_LONG).show();
                }
            } else if (FUNC_ADDRESS1_IMAGE2 == 1) {
                Toast.makeText(this, "address", Toast.LENGTH_SHORT).show();
                if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK && null != data) {
                    Toast.makeText(this, "address 2", Toast.LENGTH_SHORT).show();
                    final Place place = PlacePicker.getPlace(data, this);
                    final CharSequence name = place.getName();
                    Lat = place.getLatLng();
                    Schedule_SingelEvent.longitude = Lat.longitude;
                    Schedule_SingelEvent.latitude = Lat.latitude;
                    final CharSequence address = place.getAddress();
                    String attributions = (String) place.getId();
                    Toast.makeText(this, "address 3:"+attributions, Toast.LENGTH_SHORT).show();

                    if (attributions == null) {
                        attributions = "";
                    }
                    AddressEvent=name.toString()+" "+address.toString();
                    //Schedule_SingelEvent.ReturnAddras(AddressEvent);

                    switch (screncount) {
                        case 4:
                            ((TextView) findViewById(R.id.SignUp_Address)).setText(AddressEvent);
                            break;
                        case 5:
                            ((TextView) findViewById(R.id.AddKid_Address)).setText(AddressEvent);
                            break;
                        case 6:
                            ((TextView) findViewById(R.id.SingleEv_Address)).setText(AddressEvent);
                            break;
                        case 7:
                            ((TextView) findViewById(R.id.Setting_Address)).setText(AddressEvent);
                            break;
                    }
                } else {
                    Toast.makeText(this, "address xxx", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    private int hour = 0;
    private int minute = 0;
    private int year = 0;
    private int month = 0;
    private int day = 0;
    private int dayfromweek = 0;
    static final int DATE_DIALOG_ID = 100;
    static final int TIME_DIALOG_ID = 1111;
    static int DATAINDX=0;
    public void opendatetime(View view) {
        if (view == findViewById(R.id.Event_Show_endbtn)) {
            text_Time = (TextView) findViewById(R.id.SingleEv_End);
            showDialog(TIME_DIALOG_ID);
        } else if (view == findViewById(R.id.Event_Show_startbtn)) {
            text_Time = (TextView) findViewById(R.id.SingleEv_Start);
            showDialog(TIME_DIALOG_ID);
        } else if (view == findViewById(R.id.calbtn)){
            DATAINDX=1;
            showDialog(DATE_DIALOG_ID);
        }else if(view==findViewById(R.id.SignUp_Birthday)){
            DATAINDX=2;
            showDialog(DATE_DIALOG_ID);
        }else if(view==findViewById(R.id.AddKid_Birthday)){
            DATAINDX=3;
            showDialog(DATE_DIALOG_ID);
        }else if(view==findViewById(R.id.Setting_Birthday)){
            DATAINDX=4;
            showDialog(DATE_DIALOG_ID);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                final Calendar DateCal = Calendar.getInstance();
                year = DateCal.get(Calendar.YEAR);
                month = DateCal.get(Calendar.MONTH);
                day = DateCal.get(Calendar.DAY_OF_MONTH);
                // Show current date
                return new DatePickerDialog(this, datePickerListener, year, month, day);
            case TIME_DIALOG_ID:

                final Calendar TimeCal = Calendar.getInstance(TimeZone.getTimeZone("GMT+2:00"));
                // Current Hour
                hour = TimeCal.get(Calendar.HOUR_OF_DAY);
                // Current Minute
                minute = TimeCal.get(Calendar.MINUTE);
                // set current time into output textview
                // set time picker as current time
                return new TimePickerDialog(this, timePickerListener, hour, minute,
                        false);
        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
            // TODO Auto-generated method stub
            hour = hourOfDay;
            minute = minutes;

            updateTime(hour, minute);
        }
    };

    private void updateTime(int hours, int mins) {
        String timeSet = "";
        if (hours > 23) {
            hours -= 23;
        }
        if (mins > 60) {
            mins -= 60;
        }
        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);
        String hoursSTR;
        if (hours < 10)
            hoursSTR = "0" + hours;
        else {
            hoursSTR = String.valueOf(hours);
        }
        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hoursSTR).append(':').append(minutes).toString();
        text_Time.setText(aTime);
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            String Day = "";
            String Month = "";
            String Year = "";
            if (month < 10) {
                Month = "0" + Integer.toString(month);
            } else {
                Month = Integer.toString(month);
            }
            if ((day + 1) < 10) {
                Day = "0" + Integer.toString(day + 1);
            } else {
                Day = Integer.toString(day + 1);
            }
            Year = Integer.toString(year);
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day);
            dayfromweek = cal.get(Calendar.DAY_OF_WEEK);
            String day_STR = "";
            switch (dayfromweek) {
                case 1:day_STR = "Sunday";break;
                case 2:day_STR = "Monday";break;
                case 3:day_STR = "Tuesday";break;
                case 4:day_STR = "Wednesday";break;
                case 5:day_STR = "Thursday";break;
                case 6:day_STR = "Friday";break;
                case 7:day_STR = "Saturday";break;
                default:break;
            }
            switch (DATAINDX){
                case 1:
                    ((TextView) findViewById(R.id.SingleEv_Day)).setText(day_STR);
                    text_date.setText(Day + "/" + Month + "/" + Year);
                    break;
                case 2:
                    ((TextView)findViewById(R.id.SignUp_Birthday)).setText(Day + "/" + Month + "/" + Year);
                    break;
                case 3:
                    ((TextView)findViewById(R.id.AddKid_Birthday)).setText(Day + "/" + Month + "/" + Year);
                    break;
                case 4:
                    ((TextView)findViewById(R.id.Setting_Birthday)).setText(Day + "/" + Month + "/" + Year);
                    break;
                default:
                    break;
            }
            DATAINDX=0;
        }
    };

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    protected void closeservice() {
        if (isMyServiceRunning(CheckEventService.class)) {
            Intent intent = new Intent(this, CheckEventService.class);
            stopService(intent);
        }
    }

    static public void closekeybord(Activity activity) {
        activity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
        );
    }
}
