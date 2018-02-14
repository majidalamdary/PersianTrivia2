package com.sputa.persiantrivia;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Random;

public class OnetimeAlarmReceiver extends BroadcastReceiver {
    private static    int NOTIFICATION_ID = 3;
    public MyAsyncTask mm;
    String last_requested_query;
    Context conx;
    public void onReceive(Context context, Intent intent) {



        Intent intent1 = new Intent(context, MyNewIntentService.class);
        context.startService(intent1);


        Calendar c = Calendar.getInstance();


        int Month = c.get(Calendar.MONTH);
        int Day = c.get(Calendar.DAY_OF_MONTH);

        SharedPreferences settings1 = context.getSharedPreferences("last_login_month", 0);
        int last_login_month =Integer.valueOf(settings1.getString("last_login_month", ""));

        settings1 = context.getSharedPreferences("last_login_day", 0);
        int last_login_day = Integer.valueOf(settings1.getString("last_login_day", ""));

     //   Toast.makeText(context, String.valueOf(last_login_month), Toast.LENGTH_SHORT).show();
        int diff_day =0;
        boolean long_seen=false;
        if(Month==last_login_month)
        {
            diff_day = Day-last_login_day;
        //    Toast.makeText(context, "diff="+ String.valueOf(diff_day), Toast.LENGTH_SHORT).show();
            if(diff_day>3)
            {
                long_seen=true;
            }
        }
        else if((Month-1)==last_login_month)
        {
            diff_day = (Day+30)-last_login_day;
            if(diff_day>3)
            {
                long_seen=true;
            }
        }
        else if(Month==0 && last_login_month==11)
        {
            diff_day = (Day+30)-last_login_day;
            if(diff_day>3)
            {
                long_seen=true;
            }
        }
        else
        {
            long_seen=true;
        }
       // long_seen=true;
        if(long_seen)
        {
            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentTitle("قهرمان");
            builder.setContentText("خیلی وقته نیستی قهرمان!!! نمی خوایی رقیبای جدیدتو به چالش بکشی؟!");
            builder.setSmallIcon(R.mipmap.icn);
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(alarmSound);
            Intent notifyIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    //to be able to launch your activity from the notification
            builder.setContentIntent(pendingIntent);

            Notification notificationCompat = builder.build();

            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
            managerCompat.notify(NOTIFICATION_ID, notificationCompat);
            NOTIFICATION_ID++;
        }














//        Notification.Builder builder = new Notification.Builder(context);
//        builder.setContentTitle("My Titel");
//        builder.setContentText("This is the Body");
//        builder.setSmallIcon(R.drawable.bomb);
//        Intent notifyIntent = new Intent(context, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
////to be able to launch your activity from the notification
//        builder.setContentIntent(pendingIntent);
//        Notification notificationCompat = builder.build();
//        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
//        managerCompat.notify(NOTIFICATION_ID, notificationCompat);
//        NOTIFICATION_ID++;

        SharedPreferences  settings2 = context.getSharedPreferences("homeScore", 0);
        String homeScore = settings2.getString("homeScore", "");
        // Toast.makeText(getBaseContext(),homeScore,Toast.LENGTH_SHORT).show();
        if(!homeScore.equals("")) {
            functions.u_name = homeScore;
        }

        if(Day!=last_login_day) {
            mm = new MyAsyncTask();
            last_requested_query = context.getResources().getString(R.string.site_url) + "do.php?param=get_daily_info&uname=" + functions.u_name+"&rnd="+String.valueOf(new Random().nextInt());
            mm.url = (last_requested_query);
            mm.execute("");
            conx = context;
        }

    }





    private class MyAsyncTask extends AsyncTask<String, Integer, Double> {


        public String ss = "", url = "";


        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub

            //  dd=params[0];
            try {
                postData();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }



        protected void onPostExecute(Double result) {

            int
                    start=ss.indexOf("<output>");
            int
                    end=ss.indexOf("</output>");
            String
                    output_str=ss.substring(start + 8, end);
            String
                    param_str = "";
            if(end>0 && ss.length()>0) {
                output_str = ss.substring(start + 8, end);
                int
                        start1 = ss.indexOf("<param>");
                int
                        end1 = ss.indexOf("</param>");

                param_str = ss.substring(start1 + 7, end1);


               // Toast.makeText(conx, ss, Toast.LENGTH_SHORT).show();
                if (param_str.equals("get_daily_info") ) {

                    start1 = ss.indexOf("<type>");
                    end1 = ss.indexOf("</type>");

                    String
                         type= ss.substring(start1 + 6, end1);

                    start1 = ss.indexOf("<result>");
                    end1 = ss.indexOf("</result>");

                    String
                            result1= ss.substring(start1 + 8, end1);

                    if(type.equals("message") && result1.length()>0)
                    {
                        Intent i =new Intent(conx,MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(conx, 1,i , 0);

                        // Boolean forground=isForeground("com.sputa.persiantrivia",context);
                        //
                        Notification noti = new Notification.Builder(conx)
                                .setContentTitle("پیام")
                                .setContentText(result1)
                                .setSmallIcon(R.mipmap.icn)
                                .setContentIntent(pendingIntent)
                                .build();
                        NotificationManager notificationManager = (NotificationManager) conx.getSystemService(conx.NOTIFICATION_SERVICE);

                        noti.flags |= Notification.FLAG_AUTO_CANCEL;
                        //noti.sound = Uri.parse("android.resource://" +context.getPackageName() + "/" + R.raw.sound_in);
                        noti.defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;



                        notificationManager.notify(NOTIFICATION_ID,noti);
                        NOTIFICATION_ID++;
                    }
                    if(type.equals("link") && result1.length()>0)
                    {


                        start1 = output_str.indexOf("<url>");
                        end1 = output_str.indexOf("</url>");

                        String
                                url= output_str.substring(start1 + 5, end1);

                        start1 = output_str.indexOf("<output_str>");
                        end1 = output_str.indexOf("</output_str>");

                        String
                                output= output_str.substring(start1 + 12, end1);

                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(conx, 1,i , 0);


                        //
                        Notification noti = new Notification.Builder(conx)
                                .setContentTitle("پیام")
                                .setContentText(output+url)
                                .setSmallIcon(R.mipmap.icn)
                                .setContentIntent(pendingIntent)
                                .build();
                        NotificationManager notificationManager = (NotificationManager) conx.getSystemService(conx.NOTIFICATION_SERVICE);

                        noti.flags |= Notification.FLAG_AUTO_CANCEL;
                        //noti.sound = Uri.parse("android.resource://" +context.getPackageName() + "/" + R.raw.sound_in);
                        noti.defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;



                        notificationManager.notify(NOTIFICATION_ID,noti);
                        NOTIFICATION_ID++;
                    }


                }

            }
        }



        //Toast.makeText(getBaseContext(),"ver="+http_result,Toast.LENGTH_SHORT).show();
//            AlertDialog.Builder builder1 = new AlertDialog.Builder(getBaseContext());
//            builder1.setTitle(getResources().getString(R.string.update));
//            builder1.setMessage(getResources().getString(R.string.need_update_message));
//            builder1.setCancelable(true);
//            builder1.setNeutralButton(android.R.string.ok,
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            dialog.cancel();
//                        }
//                    });
//


//            AlertDialog alert11 = builder1.create();
//            alert11.show();


        protected void onProgressUpdate(Integer... progress) {
            //pb.setProgress(progress[0]);
        }

        public void postData() throws IOException {
            HttpClient httpclient = new DefaultHttpClient(); // Create HTTP Client
            HttpGet httpget = new HttpGet(url); // Set the action you want to do
            HttpResponse response = httpclient.execute(httpget); // Executeit
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent(); // Create an InputStream with the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) // Read line by line
                sb.append(line);

            String resString = sb.toString(); // Result is here
            ss = resString;
            //Log.d("majid", resString);
            is.close();
        }
    }

}