package com.parse.starter;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Admin on 07/11/2015.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class splachscreen extends Fragment {
    private ImageButton REFRESH;
    private ProgressBar PROG;
    private TextView MESSAGE_REF;
    private ImageView REFRESH_ICON;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View splash = inflater.inflate(R.layout.splachscreen, container, false);
        REFRESH=(ImageButton)splash.findViewById(R.id.refreshlogin);
        MESSAGE_REF=(TextView)splash.findViewById(R.id.refreshmessage);
        REFRESH_ICON=(ImageView)splash.findViewById(R.id.refreshicon);
        PROG=(ProgressBar)splash.findViewById(R.id.progressBar);
        final boolean[] run = {true};
        Thread thread=new Thread(){
            public void run(){
                while (run[0]){
                    try {
                        Log.i("checkinglogin", "1");
                        sleep(5000);
                        if(Main2Activity.Getchatromsid&&Main2Activity.Updateuser){
                            Log.i("checkinglogin", "2");
                            run[0] =false;
                        }else{
                            //REFRESH.setVisibility(View.VISIBLE);
                            //MESSAGE_REF.setVisibility(View.VISIBLE);
                            //REFRESH_ICON.setVisibility(View.VISIBLE);
                        }
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        };
        thread.start();
        REFRESH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //REFRESH.setVisibility(View.INVISIBLE);
                //MESSAGE_REF.setVisibility(View.INVISIBLE);
                //REFRESH_ICON.setVisibility(View.INVISIBLE);
            }
        });
        return splash;
    }
}
