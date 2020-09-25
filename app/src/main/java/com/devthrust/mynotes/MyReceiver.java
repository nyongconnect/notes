package com.devthrust.mynotes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {
    public static final  String ACTION_COURSE_EVENT = "com.example.android.pets.Events";
    public static final  String EXTRA_COURSE_ID = "com.example.android.pets.courseId";
    public static final  String EXTRA_COURSE_MESSAGE = "com.example.android.pets.message";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_COURSE_EVENT)){
            String id =intent.getStringExtra(EXTRA_COURSE_ID);
            String message = intent.getStringExtra(EXTRA_COURSE_MESSAGE);
        }

    }
}
