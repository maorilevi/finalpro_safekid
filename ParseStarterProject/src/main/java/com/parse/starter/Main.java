/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.annotation.TargetApi;
import android.app.Activity;
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
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
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
public class Main extends ActionBarActivity {

    static protected TextView setting;
    static protected TextView chat;
    static protected TextView alerm;
    static protected TextView location;
    static protected TextView addkid;
    static protected TextView statisc;
    static protected TextView schedule;

    static protected LinearLayout setting_L;
    static protected LinearLayout chat_L;
    static protected LinearLayout alerm_L;
    static protected LinearLayout location_L;
    static protected LinearLayout addkid_L;
    static protected LinearLayout statisc_L;
    static protected LinearLayout schedule_L;

    static protected ImageView settingimage;
    static protected ImageView chatimage;
    static protected ImageView alermimage;
    static protected ImageView locationimage;
    static protected ImageView addkidimage;
    static protected ImageView statiscimage;
    static protected ImageView scheduleimage;

    static protected Fragment Login_Fram = new LogIn();
    static protected Fragment SignUp_Fram = new SignUp_Fram();
    static protected Fragment Splach_Fram = new splachscreen();
    static protected Fragment ChatList_Fram;
    static protected Fragment Schedule_Fram;
    static protected Fragment Setting_Fram;
    static protected Fragment Addkid_Fram = new Add_Kid();
    static protected Fragment current_Fram = new Fragment();
    static protected Fragment Lest_Fram = new Fragment();

    private static UsersDataSource UserDB;//user database
    private static int RESULT_LOAD_IMG = 1;
    protected static String imgDecodableString;
    static protected boolean parent = true;
    static protected boolean Addkidscreen = false;
    static protected ArrayList<User> users = new ArrayList<User>();
    static protected ActionBar actionBar;
    static protected FragmentManager mymanager;
    static protected LinearLayout LNavigat;
    private ScrollView ScrollNevigat;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private CheckEventService s;
    static protected int Mainheight=0;
    static protected int Mainwidth=0;
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Mainheight=dm.heightPixels;
        Mainwidth=dm.widthPixels;
        UserDB = new UsersDataSource(this);
        UserDB.open();
        users = UserDB.getAllUsers();
        UserDB.close();
        actionBar = getSupportActionBar(); // or getActionBar();
        getSupportActionBar().setTitle("SAFE KID"); // set the top title
        getSupportActionBar().setIcon(R.drawable.ic_menu_camera);
        getSupportActionBar().setLogo(R.drawable.ic_menu_camera);
        getSupportActionBar().hide();
        ParseAnalytics.trackAppOpened(getIntent());
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.openbar, R.string.closebar);
        //drawerLayout.setDrawerListener(actionBarDrawerToggle);
        //drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        LNavigat = new LinearLayout(this);
        LNavigat.setOrientation(LinearLayout.VERTICAL);
        LNavigat.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ScrollNevigat = (ScrollView) findViewById(R.id.NavigatscrollView);
        ScrollNevigat.addView(LNavigat);

        setting = new TextView(this);
        chat = new TextView(this);
        alerm = new TextView(this);
        location = new TextView(this);
        addkid = new TextView(this);
        statisc = new TextView(this);
        schedule = new TextView(this);

        setting_L = new LinearLayout(this);
        chat_L = new LinearLayout(this);
        alerm_L = new LinearLayout(this);
        location_L = new LinearLayout(this);
        addkid_L = new LinearLayout(this);
        statisc_L = new LinearLayout(this);
        schedule_L = new LinearLayout(this);

        settingimage = new ImageView(this);
        chatimage = new ImageView(this);
        alermimage = new ImageView(this);
        locationimage = new ImageView(this);
        addkidimage = new ImageView(this);
        statiscimage = new ImageView(this);
        scheduleimage = new ImageView(this);

        mymanager = getFragmentManager();
        FragmentTransaction mytransaction1 = mymanager.beginTransaction();
        mytransaction1.add(R.id.MainRelative, Splach_Fram, "Splach_Fram");
        mytransaction1.commit();
        //checking user status
        if (users.size() > 0) {
            UpdateContacts(Main.this);
            ParseQuery<ParseObject> usertable = ParseQuery.getQuery("USER");
            usertable.getInBackground(users.get(0).getUserParseID(), new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        getSupportActionBar().show();
                        if (object.getBoolean("Parent")) {
                            parent = true;
                        } else {
                            parent = false;
                        }
                        //create menus option
                        ChatList_Fram = new Chat_Contact_List();
                        Schedule_Fram = new Schedule_Mange();
                        Setting_Fram = new Setting_Fram();
                        FragmentTransaction mytransaction2 = mymanager.beginTransaction();
                        mytransaction2.add(R.id.MainRelative, ChatList_Fram, "ChatList_Fram");
                        mytransaction2.add(R.id.MainRelative, Schedule_Fram, "Schedule_Fram");
                        mytransaction2.add(R.id.MainRelative, Setting_Fram, "Setting_Fram");
                        mytransaction2.add(R.id.MainRelative, Addkid_Fram, "Addkid_Fram");
                        mytransaction2.hide(Addkid_Fram);
                        mytransaction2.hide(Splach_Fram);
                        mytransaction2.hide(Schedule_Fram);
                        mytransaction2.hide(Setting_Fram);
                        mytransaction2.hide(ChatList_Fram);
                        if (users.size() == 1) {
                            current_Fram = Addkid_Fram;
                        } else {
                            current_Fram = ChatList_Fram;
                        }
                        mytransaction2.show(current_Fram);
                        mytransaction2.commit();
                        CreateNevagtionOption();
                    }
                }
            });
        } else {
            FragmentTransaction mytransaction2 = mymanager.beginTransaction();
            mytransaction2.add(R.id.MainRelative, Login_Fram, "Login_Fram");
            mytransaction2.add(R.id.MainRelative, SignUp_Fram, "SignUp_Fram");
            mytransaction2.hide(Splach_Fram);
            mytransaction2.hide(SignUp_Fram);
            mytransaction2.commit();
        }
    }
    static public void UpdateContacts(final Activity context) {
        final ArrayList<String> allids = new ArrayList<String>();
        for (int indx = 0; indx < users.size(); indx++) {
            allids.add(users.get(indx).getUserParseID());
        }
        if (parent) {
            ParseQuery<ParseObject> P_K_Table = ParseQuery.getQuery("Parent_Kid");
            P_K_Table.whereMatches("Parent_ID", users.get(0).getUserParseID());
            ParseQuery<ParseObject> usertable2 = ParseQuery.getQuery("USER");
            usertable2.whereMatchesKeyInQuery("objectId", "Kid_ID", P_K_Table);
            usertable2.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(final List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        for (int indx = 0; indx < objects.size(); indx++) {
                            for (int indx2 = 0; indx2 < users.size(); indx2++) {
                                if (users.get(indx2).getUserParseID() == objects.get(indx).getObjectId()) {
                                    ParseFile fileObject = objects.get(indx).getParseFile("Image");
                                    final int finalIndx = indx2;
                                    final int finalIndx1 = indx;
                                    fileObject.getDataInBackground(new GetDataCallback() {
                                        public void done(byte[] data, ParseException e2) {
                                            if (e2 == null) {
                                                Bitmap btm = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                UserDB = new UsersDataSource(context);
                                                UserDB.open();
                                                users.get(finalIndx).setUserImage(btm);
                                                users.get(finalIndx).setFirstname(objects.get(finalIndx1).getString("F_name"));
                                                users.get(finalIndx).setLestName(objects.get(finalIndx1).getString("L_name"));
                                                users.get(finalIndx).setUserName(objects.get(finalIndx1).getString("User_Name"));
                                                users.get(finalIndx).setAddress(objects.get(finalIndx1).getString("Address"));
                                                users.get(finalIndx).setBirthday(objects.get(finalIndx1).getString("Birthday"));
                                                users.get(finalIndx).setPhoneNumber(objects.get(finalIndx1).getString("Phone_Number"));
                                                users.get(finalIndx).setEmail(objects.get(finalIndx1).getString("Email"));
                                                if (finalIndx == 0) {
                                                    users.get(finalIndx).setPassword(objects.get(finalIndx1).getString("Password"));
                                                }
                                                UserDB.UpdateUser(users.get(finalIndx));
                                                UserDB.close();

                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            });
        } else {
            ParseQuery<ParseObject> P_K_Table = ParseQuery.getQuery("Parent_Kid");
            P_K_Table.whereMatches("Kid_ID", users.get(0).getUserParseID());
            ParseQuery<ParseObject> P_K_Table2 = ParseQuery.getQuery("Parent_Kid");
            P_K_Table2.whereMatchesKeyInQuery("Parent_ID", "Parent_ID", P_K_Table);
            ParseQuery<ParseObject> userstable = ParseQuery.getQuery("USER");
            userstable.whereMatchesKeyInQuery("objectId", "Kid_ID", P_K_Table2);
            userstable.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(final List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        for (int indx = 0; indx < objects.size(); indx++) {
                            for (int indx2 = 0; indx2 < users.size(); indx2++) {
                                if (users.get(indx2).getUserParseID() == objects.get(indx).getObjectId()) {
                                    ParseFile fileObject = objects.get(indx).getParseFile("Image");
                                    final int finalIndx = indx2;
                                    final int finalIndx1 = indx;
                                    fileObject.getDataInBackground(new GetDataCallback() {
                                        public void done(byte[] data, ParseException e2) {
                                            if (e2 == null) {
                                                Bitmap btm = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                UserDB = new UsersDataSource(context);
                                                UserDB.open();
                                                users.get(finalIndx).setUserImage(btm);
                                                users.get(finalIndx).setFirstname(objects.get(finalIndx1).getString("F_name"));
                                                users.get(finalIndx).setLestName(objects.get(finalIndx1).getString("L_name"));
                                                users.get(finalIndx).setUserName(objects.get(finalIndx1).getString("User_Name"));
                                                users.get(finalIndx).setAddress(objects.get(finalIndx1).getString("Address"));
                                                users.get(finalIndx).setBirthday(objects.get(finalIndx1).getString("Birthday"));
                                                users.get(finalIndx).setPhoneNumber(objects.get(finalIndx1).getString("Phone_Number"));
                                                users.get(finalIndx).setEmail(objects.get(finalIndx1).getString("Email"));
                                                if (finalIndx == 0) {
                                                    users.get(finalIndx).setPassword(objects.get(finalIndx1).getString("Password"));
                                                }
                                                UserDB.UpdateUser(users.get(finalIndx));
                                                UserDB.close();
                                            }
                                        }
                                    });
                                }
                            }

                        }

                    }
                }
            });
            userstable.whereMatchesKeyInQuery("objectId", "Parent_ID", P_K_Table);
            userstable.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(final List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        for (int indx = 0; indx < objects.size(); indx++) {
                            for (int indx2 = 0; indx2 < users.size(); indx2++) {
                                if (users.get(indx2).getUserParseID() == objects.get(indx).getObjectId()) {
                                    ParseFile fileObject = objects.get(indx).getParseFile("Image");
                                    final int finalIndx = indx2;
                                    final int finalIndx1 = indx;
                                    fileObject.getDataInBackground(new GetDataCallback() {
                                        public void done(byte[] data, ParseException e2) {
                                            if (e2 == null) {
                                                Bitmap btm = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                UserDB = new UsersDataSource(context);
                                                UserDB.open();
                                                users.get(finalIndx).setUserImage(btm);
                                                users.get(finalIndx).setFirstname(objects.get(finalIndx1).getString("F_name"));
                                                users.get(finalIndx).setLestName(objects.get(finalIndx1).getString("L_name"));
                                                users.get(finalIndx).setUserName(objects.get(finalIndx1).getString("User_Name"));
                                                users.get(finalIndx).setAddress(objects.get(finalIndx1).getString("Address"));
                                                users.get(finalIndx).setBirthday(objects.get(finalIndx1).getString("Birthday"));
                                                users.get(finalIndx).setPhoneNumber(objects.get(finalIndx1).getString("Phone_Number"));
                                                users.get(finalIndx).setEmail(objects.get(finalIndx1).getString("Email"));
                                                if (finalIndx == 0) {
                                                    users.get(finalIndx).setPassword(objects.get(finalIndx1).getString("Password"));
                                                }
                                                UserDB.UpdateUser(users.get(finalIndx));
                                                UserDB.close();
                                            }
                                        }
                                    });
                                }
                            }

                        }
                    }
                }
            });
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (!parent) {
            Intent intent = new Intent(this, CheckEventService.class);
            startService(intent);
            bindService(intent, mConnection, Context.BIND_ALLOW_OOM_MANAGEMENT);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
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
        FragmentTransaction mytransaction2 = mymanager.beginTransaction();
        mytransaction2.hide(Login_Fram);
        mytransaction2.show(SignUp_Fram);
        mytransaction2.commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        LinearLayout layout = new LinearLayout(this);
        if (id == R.id.mymenu) {
            if (drawerLayout.isDrawerOpen(ScrollNevigat)) {
                drawerLayout.closeDrawer(ScrollNevigat);
            } else {
                drawerLayout.openDrawer(ScrollNevigat);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    static public void CreateNevagtionOption() {
        //SETTING////////////////////////////////////////////////
        setting.setText(" setting ");
        setting.setTextSize(20);
        settingimage.setImageResource(R.drawable.setting);
        settingimage.setLayoutParams(new ViewGroup.LayoutParams(50, 50));
        settingimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lest_Fram = current_Fram;
                current_Fram = Setting_Fram;
                FragmentTransaction mytransaction3 = mymanager.beginTransaction();
                mytransaction3.hide(Lest_Fram);
                mytransaction3.show(current_Fram);
                mytransaction3.commit();
            }
        });
        setting_L.setOrientation(LinearLayout.HORIZONTAL);
        setting_L.addView(settingimage);
        setting_L.addView(setting);
        //CHAT////////////////////////////////////////////////
        chat.setText(" chat ");
        chat.setTextSize(20);
        chatimage.setImageResource(R.drawable.chat);
        chatimage.setLayoutParams(new ViewGroup.LayoutParams(50, 50));
        chat_L.setOrientation(LinearLayout.HORIZONTAL);
        chat_L.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lest_Fram = current_Fram;
                current_Fram = ChatList_Fram;
                FragmentTransaction mytransaction3 = mymanager.beginTransaction();
                mytransaction3.hide(Lest_Fram);
                mytransaction3.show(current_Fram);
                mytransaction3.commit();
            }
        });
        chat_L.addView(chatimage);
        chat_L.addView(chat);
        //ALERM////////////////////////////////////////////////
        alermimage.setImageResource(R.drawable.alarm);
        alermimage.setLayoutParams(new ViewGroup.LayoutParams(50, 50));
        alerm_L.setOrientation(LinearLayout.HORIZONTAL);
        alerm_L.addView(alermimage);
        alerm_L.addView(alerm);
        alerm.setText(" alerm ");
        alerm.setTextSize(20);
        //LOCATION////////////////////////////////////////////////
        location.setText(" location ");
        location.setTextSize(20);
        locationimage.setImageResource(R.drawable.maps);
        locationimage.setLayoutParams(new ViewGroup.LayoutParams(50, 50));
        location_L.setOrientation(LinearLayout.HORIZONTAL);
        location_L.addView(locationimage);
        location_L.addView(location);
        //SCHEDULE////////////////////////////////////////////////
        schedule.setText(" schedule ");
        schedule.setTextSize(20);
        scheduleimage.setImageResource(R.drawable.calendar);
        scheduleimage.setLayoutParams(new ViewGroup.LayoutParams(50, 50));
        schedule_L.setOrientation(LinearLayout.HORIZONTAL);
        schedule_L.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lest_Fram = current_Fram;
                current_Fram = Schedule_Fram;
                FragmentTransaction mytransaction3 = mymanager.beginTransaction();
                mytransaction3.hide(Lest_Fram);
                mytransaction3.show(current_Fram);
                mytransaction3.commit();
            }
        });
        schedule_L.addView(scheduleimage);
        schedule_L.addView(schedule);
        //ADDKID////////////////////////////////////////////////
        addkid.setText(" addkid ");
        addkid.setTextSize(20);
        addkidimage.setImageResource(R.drawable.add);
        addkidimage.setLayoutParams(new ViewGroup.LayoutParams(50, 50));
        addkid_L.setOrientation(LinearLayout.HORIZONTAL);
        addkid_L.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lest_Fram = current_Fram;
                current_Fram = Addkid_Fram;
                FragmentTransaction mytransaction3 = mymanager.beginTransaction();
                mytransaction3.hide(Lest_Fram);
                mytransaction3.show(current_Fram);
                mytransaction3.commit();
            }
        });
        addkid_L.addView(addkidimage);
        addkid_L.addView(addkid);
        //STATISC////////////////////////////////////////////////
        statisc.setText(" statisc ");
        statisc.setTextSize(20);
        statiscimage.setImageResource(R.drawable.statisc);
        statiscimage.setLayoutParams(new ViewGroup.LayoutParams(50, 50));
        statisc_L.setOrientation(LinearLayout.HORIZONTAL);
        statisc_L.addView(statiscimage);
        statisc_L.addView(statisc);

        LNavigat.addView(chat_L);
        LNavigat.addView(setting_L);
        if (parent) {
            LNavigat.addView(schedule_L);
            LNavigat.addView(location_L);
            LNavigat.addView(statisc_L);
            LNavigat.addView(addkid_L);
        } else {
            LNavigat.addView(alerm_L);
        }

    }

    static public void Openafterlogin(Activity context) {
        if (users.size() > 0) {
            UpdateContacts(context);
            ParseQuery<ParseObject> usertable = ParseQuery.getQuery("USER");
            usertable.getInBackground(users.get(0).getUserParseID(), new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        actionBar.show();
                        if (object.getBoolean("Parent")) {
                            parent = true;
                        } else {
                            parent = false;
                        }
                        //create menus option

                        ChatList_Fram = new Chat_Contact_List();
                        Schedule_Fram = new Schedule_Mange();
                        Setting_Fram = new Setting_Fram();
                        FragmentTransaction mytransaction2 = mymanager.beginTransaction();
                        mytransaction2.remove(Login_Fram);
                        mytransaction2.remove(SignUp_Fram);
                        mytransaction2.add(R.id.MainRelative, ChatList_Fram, "ChatList_Fram");
                        mytransaction2.add(R.id.MainRelative, Schedule_Fram, "Schedule_Fram");
                        mytransaction2.add(R.id.MainRelative, Setting_Fram, "Setting_Fram");
                        mytransaction2.add(R.id.MainRelative, Addkid_Fram, "Addkid_Fram");
                        mytransaction2.hide(Addkid_Fram);
                        mytransaction2.hide(Splach_Fram);
                        mytransaction2.hide(Schedule_Fram);
                        mytransaction2.hide(Setting_Fram);
                        mytransaction2.hide(ChatList_Fram);
                        if (users.size() == 1) {
                            current_Fram = Addkid_Fram;
                        } else {
                            current_Fram = ChatList_Fram;
                        }
                        mytransaction2.show(current_Fram);
                        mytransaction2.commit();
                        CreateNevagtionOption();
                    }
                }
            });
        }else{
            FragmentTransaction mytransaction2 = mymanager.beginTransaction();
            mytransaction2.remove(ChatList_Fram);
            mytransaction2.remove(Schedule_Fram);
            mytransaction2.remove(Setting_Fram);
            mytransaction2.remove(Addkid_Fram);
            mytransaction2.add(R.id.MainRelative, Login_Fram, "Login_Fram");
            mytransaction2.add(R.id.MainRelative, SignUp_Fram, "SignUp_Fram");
            mytransaction2.hide(SignUp_Fram);
            mytransaction2.commit();
        }
    }
    public void logout(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setMessage("log out ?");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                UserDB = new UsersDataSource(Main.this);
                UserDB.open();
                UserDB.DeleteAllUsers();
                UserDB.close();
                Openafterlogin(Main.this);
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
        TextView textView=new TextView(Main.this);
        textView=(TextView) findViewById(R.id.ListEvent_Today);
        textView.setText(Schedule_Mange.Current_day);
        Schedule_Mange.setallevent(Main.this);
    }
    ///singel event function
    //choose time function
    public void savetime(View view){
        TimePicker mytime=new TimePicker(this);
        int Hours_int=((TimePicker)findViewById(R.id.timePicker)).getCurrentHour();
        int Minute_int=((TimePicker)findViewById(R.id.timePicker)).getCurrentMinute();
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
        Toast.makeText(getApplication(), Hours + ":" + Minute, Toast.LENGTH_SHORT).show();
        if(view==findViewById(R.id.Event_Show_startbtn)) {
            Schedule_SingelEvent.Set_Start_Time.setText(Hours + ":" + Minute);
        }else{
            Schedule_SingelEvent.Set_End_Time.setText(Hours + ":" + Minute);
        }
    }
    public void saveevent(final View view){
        if(Schedule_SingelEvent.newevent){
            //Create new event
            Toast.makeText(getApplication(), "new", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplication(), "old", Toast.LENGTH_SHORT).show();
                        parseObject.put("NameEvent", Schedule_SingelEvent.NameEvent.getText().toString());
                        parseObject.put("Address", Schedule_SingelEvent.AddressEvent.getText().toString());
                        parseObject.put("StartEvent", Schedule_SingelEvent.Set_Start_Time.getText().toString());
                        parseObject.put("EndEvent", Schedule_SingelEvent.Set_End_Time.getText().toString());
                        parseObject.put("Day",Schedule_SingelEvent.day.getText().toString());
                        parseObject.saveInBackground();
                        AlertDialog alertDialog = new AlertDialog.Builder(Main.this).create();
                        alertDialog.setTitle("successful update");
                        alertDialog.setMessage("your changes is update");
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Lest_Fram=current_Fram;
                                current_Fram=Schedule_Fram;
                                FragmentTransaction mytransaction2 = Main.mymanager.beginTransaction();
                                mytransaction2.remove(Main.Lest_Fram);
                                mytransaction2.show(current_Fram);
                                mytransaction2.commit();
                            }
                        });
                        alertDialog.show();
                    } else {
                        saveevent(view);
                    }
                }
            });
        }
    }
    //back to main list event
    public void backtolistevent(View view){
        Lest_Fram=current_Fram;
        current_Fram=Schedule_Fram;
        FragmentTransaction mytransaction2 = Main.mymanager.beginTransaction();
        mytransaction2.remove(Main.Lest_Fram);
        mytransaction2.show(current_Fram);
        mytransaction2.commit();
    }
    public void DeleteEvent(View view){
        AlertDialog.Builder builder  =new AlertDialog.Builder(Main.this);
        builder.setTitle("Are you sure you want to delete the event");
        builder.setPositiveButton("yse",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Lest_Fram=current_Fram;
                current_Fram=Schedule_Fram;
                FragmentTransaction mytransaction2 = Main.mymanager.beginTransaction();
                mytransaction2.remove(Main.Lest_Fram);
                mytransaction2.show(current_Fram);
                mytransaction2.commit();
                delete();
            }
        });
        builder.setNegativeButton("no",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
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
}
