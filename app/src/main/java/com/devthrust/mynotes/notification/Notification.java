package com.devthrust.mynotes.notification;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.devthrust.mynotes.NoteActivity;
import com.devthrust.mynotes.R;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Notification  {
    private Notification(){

    }

    public static void makeNotification(Context context, String Channel_id, Uri noteUri){
        Intent intent = new Intent(context, NoteActivity.class);
        intent.setData(noteUri);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Channel_id);
        builder.setSmallIcon(R.drawable.ic_menu_slideshow);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentTitle("hello");
        builder.setContentText("i am here")
        .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(context, 0,intent,PendingIntent.FLAG_UPDATE_CURRENT));

        builder.setStyle(new NotificationCompat.BigTextStyle().bigText("There are times in life, we try our best to do the little we know how to do, " +
                "i think the text is actually small and all are displayed at once and we have to modify it to sooth what we are doing here").setBigContentTitle("things we have learnt").setSummaryText("it's a summary"));
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(Channel_id, 0,builder.build());


    }
}
