package com.sputa.persiantrivia;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.NotificationManagerCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MyNewIntentService extends IntentService {
    private static    int NOTIFICATION_ID = 3;


    public MyNewIntentService() {
        super("MyNewIntentService");
    }

    String
            last_requested_query="";

    @Override
    protected void onHandleIntent(Intent intent) {


//
//        Notification.Builder builder = new Notification.Builder(this);
//        builder.setContentTitle("My Titel");
//        builder.setContentText("This is the Body");
//        builder.setSmallIcon(R.drawable.bomb);
//        Intent notifyIntent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
////to be able to launch your activity from the notification
//        builder.setContentIntent(pendingIntent);
//        Notification notificationCompat = builder.build();
//        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
//        managerCompat.notify(NOTIFICATION_ID, notificationCompat);
//        NOTIFICATION_ID++;
//










//        mm = new MyAsyncTask();
//        last_requested_query=getResources().getString(R.string.site_url) + "get_user_info.php?param=get_user_main_info&uname="+functions.u_name;
//        mm.url = (last_requested_query);
//        mm.execute("");



    }


}