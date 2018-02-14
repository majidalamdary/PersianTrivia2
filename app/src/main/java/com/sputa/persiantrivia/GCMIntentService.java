package com.sputa.persiantrivia;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;
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
import java.util.List;
import java.util.Random;

import static com.sputa.persiantrivia.CommonUtilities.SENDER_ID;
import static com.sputa.persiantrivia.CommonUtilities.displayMessage;

public class GCMIntentService extends GCMBaseIntentService {


    public MyAsyncTask mm;
    private static final String TAG = "GCMIntentService";

    public GCMIntentService() {
        super(SENDER_ID);
    }

    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        mm = new MyAsyncTask();

        mm.url = (getResources().getString(R.string.site_url) + "register_gcm.php?param=register_gcm&uname="+functions.u_name+"&reg_id=" + registrationId+"&rnd="+String.valueOf(new Random().nextInt()));
        mm.execute("");

       // displayMessage(context, "Your device registred with GCM");
       // Log.d("NAME", MainActivity.name);
       // ServerUtilities.register(context, MainActivity.name, MainActivity.email, registrationId);
    }

    /**
     * Method called on device un registred
     * */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
      //  displayMessage(context, getString(R.string.gcm_unregistered));
      //  ServerUtilities.unregister(context, registrationId);
    }

    /**
     * Method called on Receiving a new message
     * */
    @Override
    protected void onMessage(Context context, Intent intent) {
        try {
            Log.i(TAG, "Received message");
            String message = intent.getExtras().getString("price");

            // displayMessage(context, message);
            // notifies user
            generateNotification(context, message);
        }
        catch (Exception e1)
        {

        }

    }

    /**
     * Method called on receiving a deleted message
     * */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");

    }

    /**
     * Method called on Error
     * */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
    //    displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
//        displayMessage(context, getString(R.string.gcm_recoverable_error,
//                errorId));
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */


    static int notify_id=0;
    private static void generateNotification(Context context, String message) {

        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
//        int icon = R.drawable.success;
//        long when = System.currentTimeMillis();
//        NotificationManager notificationManager = (NotificationManager)
//                context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notification = new Notification(icon, message, when);
//
//        String title = context.getString(R.string.app_name);
//
//        Intent notificationIntent = new Intent(context, MainActivity.class);
//        // set intent so it does not start a new activity
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent intent =
//                PendingIntent.getActivity(context, 0, notificationIntent, 0);
//        notification.setLatestEventInfo(context, title, message, intent);
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//
//        // Play default notification sound
//        notification.defaults |= Notification.DEFAULT_SOUND;
//
//        // Vibrate if vibrate is enabled
//        notification.defaults |= Notification.DEFAULT_VIBRATE;
//        notificationManager.notify(0, notification);


        int
                start=message.indexOf("<output>");
        int
                end=message.indexOf("</output>");
        String
                output_str="";
        String
                param_str = "";
        if(end>0 && message.length()>0) {
            output_str = message.substring(start + 8, end);
            int
                    start1 = message.indexOf("<param>");
            int
                    end1 = message.indexOf("</param>");

            param_str = message.substring(start1 + 7, end1);

            if (param_str.equals("message")) {

                Intent i =new Intent(context,MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 1,i , 0);

               // Boolean forground=isForeground("com.sputa.persiantrivia",context);
                //
                Notification noti = new Notification.Builder(context)
                        .setContentTitle("پیام")
                        .setContentText(output_str)
                        .setSmallIcon(R.mipmap.icn)
                        .setContentIntent(pendingIntent)
                        .build();
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

                noti.flags |= Notification.FLAG_AUTO_CANCEL;
                //noti.sound = Uri.parse("android.resource://" +context.getPackageName() + "/" + R.raw.sound_in);
                noti.defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;



                notificationManager.notify(notify_id,noti);
                notify_id++;





            }
            if (param_str.equals("open_url")) {



               start1 = output_str.indexOf("<url>");
                end1 = output_str.indexOf("</url>");

                String
                        url= output_str.substring(start1 + 5, end1);
                start1 = output_str.indexOf("<message>");
                end1 = output_str.indexOf("</message>");

                String
                        message1= output_str.substring(start1 + 9, end1);



                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 1,i , 0);


                //
                Notification noti = new Notification.Builder(context)
                        .setContentTitle("پیام")
                        .setContentText(message1)
                        .setSmallIcon(R.mipmap.icn)
                        .setContentIntent(pendingIntent)
                        .build();
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

                noti.flags |= Notification.FLAG_AUTO_CANCEL;
                //noti.sound = Uri.parse("android.resource://" +context.getPackageName() + "/" + R.raw.sound_in);
                noti.defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;



                notificationManager.notify(notify_id,noti);
                notify_id++;



            }

            if (param_str.equals("shaked")) {


                start1 = output_str.indexOf("<rival_uname>");
                end1 = output_str.indexOf("</rival_uname>");

                functions.shaked_uname= output_str.substring(start1 + 13, end1);
                start1 = output_str.indexOf("<g_id>");
                end1 = output_str.indexOf("</g_id>");

                functions.shaked_g_id= output_str.substring(start1 + 6, end1);
            }



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
                    output_str="";
            String
                    param_str = "";
            if(end>0 && ss.length()>0) {
                output_str = ss.substring(start + 8, end);
                int
                        start1 = ss.indexOf("<param>");
                int
                        end1 = ss.indexOf("</param>");

                param_str = ss.substring(start1 + 7, end1);

               // if(is_requested)
                {
                    if (param_str.equals("register_gcm")) {
                        if (output_str.equals("updated")) {
                            // lbl_msg.setText("آپدیت شد");
                            GCMRegistrar.setRegisteredOnServer(getBaseContext(), true);

                        }

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