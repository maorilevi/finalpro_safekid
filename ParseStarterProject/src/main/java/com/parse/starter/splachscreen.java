package com.parse.starter;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Admin on 07/11/2015.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class splachscreen extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.splachscreen, container, false);
        return myView;
    }
}
