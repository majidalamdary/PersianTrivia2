package com.sputa.persiantrivia;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import com.squareup.picasso.Picasso;

import static com.sputa.persiantrivia.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.sputa.persiantrivia.CommonUtilities.SENDER_ID;

public class MainActivity extends AppCompatActivity {
    int screenWidth  = 0;
    int screenHeight = 0;
    functions fun;
    String
            majid="majid";
    String
            font_name = "";
    Typeface tf;
    Boolean
        is_requested = false;
    LinearLayout lay_tabBar;
    public MyAsyncTask mm;
    public    RelativeLayout lay_wait;
    public    LinearLayout lay_main;

    int      x=1;
    Timer1    tim_circulation;
    Boolean  is_rotation = true;
    String last_requested_query="";
    public   ImageView img_circle1;
    Button msg_btn1,msg_btn2,msg_btn3;
    int
            s_current_version=0;
    int
            s_min_version=0;
    boolean
        no_connection =false;



    public static String [] user_name;
    public static String [] user_desc;

    public static String [] user_level;
    public static String [] uig_id;
    public static String [] user_gender;
    public static String [] user_avatar_id;
    public static String [] game_result;

    SQLiteDatabase mydatabase;

    String
            list_type="turn";
    ListView lv;


    TextView txt_request_noti;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
//am broadcast -a android.intent.action.BOOT_COMPLETED

        //list_type="turn";
        lv=(ListView) findViewById(R.id.listView4);


        mydatabase = openOrCreateDatabase("aa", MODE_PRIVATE, null);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        fun = new functions();

        font_name = fun.font_name;

        SharedPreferences  settings1 = getApplicationContext().getSharedPreferences("homeScore", 0);
        String homeScore = settings1.getString("homeScore", "");

        // Toast.makeText(getBaseContext(),homeScore,Toast.LENGTH_SHORT).show();

        if(!homeScore.equals("")) {
            functions.u_name = homeScore;
        }

//        SharedPreferences settings = getApplicationContext().getSharedPreferences("homeScore", 0);
//        fun.u_name =  settings.getString("homeScore","");
//        functions.u_name =  settings.getString("homeScore","");

        tf = Typeface.createFromAsset(getAssets(),font_name );
        img_circle1 = (ImageView) findViewById(R.id.circle1);
        tim_circulation       = new Timer1("circulation");
        tim_circulation.start();
        lay_wait = (RelativeLayout) findViewById(R.id.lay_wait1);
        msg_btn1= (Button) findViewById(R.id.btn_msg1);
        msg_btn2= (Button) findViewById(R.id.btn_msg2);
        msg_btn3= (Button) findViewById(R.id.btn_msg3);

        lay_main = (LinearLayout) findViewById(R.id.lay_main);



        set_content();


        check_gcm();

        get_user_info();


        player = MediaPlayer.create(MainActivity.this, R.raw.bg_music);
        player.setLooping(true);
        player.setVolume(100,100);
        player.start();

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        Intent notifyIntent = new Intent(this,OnetimeAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,  System.currentTimeMillis(),1000*60*60*24, pendingIntent);



        Calendar c = Calendar.getInstance();


        int Month = c.get(Calendar.MONTH);
        int Day = c.get(Calendar.DAY_OF_MONTH);

       // Toast.makeText(MainActivity.this, "qw="+String.valueOf(Month)+"iiii"+String.valueOf(Day), Toast.LENGTH_SHORT).show();

        SharedPreferences shr_last_login_day = getApplicationContext().getSharedPreferences("last_login_day", 0);
        SharedPreferences.Editor editor = shr_last_login_day.edit();
        editor.putString("last_login_day",String.valueOf(Day)) ;
        editor.apply();

        SharedPreferences shr_last_login_month = getApplicationContext().getSharedPreferences("last_login_month", 0);
        editor = shr_last_login_month.edit();
        editor.putString("last_login_month",String.valueOf(Month)) ;
        editor.apply();



//        Notification.Builder builder = new Notification.Builder(this);
//        builder.setContentTitle("My Titel");
//        builder.setContentText("This is the Body");
//        builder.setSmallIcon(R.drawable.bomb);



//        Intent notifyIntent1 = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent1 = PendingIntent.getActivity(this, 2, notifyIntent1, PendingIntent.FLAG_UPDATE_CURRENT);
////to be able to launch your activity from the notification
//        builder.setContentIntent(pendingIntent1);
//        Notification notificationCompat = builder.build();
//        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
//        managerCompat.notify(22, notificationCompat);
        if(functions.protected_message_showed==0)
        {
            functions.protected_message_showed=1;
            ifHuaweiAlert();


        }

    }

    private void ifHuaweiAlert() {

        final SharedPreferences settings = getSharedPreferences("ProtectedApps", MODE_PRIVATE);
        final String saveIfSkip = "skipProtectedAppsMessage";
        boolean skipMessage = settings.getBoolean(saveIfSkip, false);
        if (!skipMessage) {
            final SharedPreferences.Editor editor = settings.edit();
            Intent intent = new Intent();
            intent.setClassName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity");
            if (isCallable(intent)) {
                final AppCompatCheckBox dontShowAgain = new AppCompatCheckBox(this);
                dontShowAgain.setText("لطفا دوباره نشان نده");
                dontShowAgain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        editor.putBoolean(saveIfSkip, isChecked);
                        editor.apply();
                    }
                });

                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Huawei Protected Apps")
                        .setMessage(String.format("%s برنامه برای کارکرد بهینه نیاز دارد در حالت حفاظت شده کار کند.%n", getString(R.string.app_name)))
                        .setView(dontShowAgain)
                        .setPositiveButton("برنامه های حفاظت شده", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                huaweiProtectedApps();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            } else {
                editor.putBoolean(saveIfSkip, true);
                editor.apply();
            }
        }
    }

    private boolean isCallable(Intent intent) {
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void huaweiProtectedApps() {
        try {
            String cmd = "am start -n com.huawei.systemmanager/.optimize.process.ProtectActivity";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                cmd += " --user " + getUserSerial();
            }
            Runtime.getRuntime().exec(cmd);
        } catch (IOException ignored) {
        }
    }

    private String getUserSerial() {
        //noinspection ResourceType
        Object userManager = getSystemService("user");
        if (null == userManager) return "";

        try {
            Method myUserHandleMethod = android.os.Process.class.getMethod("myUserHandle", (Class<?>[]) null);
            Object myUserHandle = myUserHandleMethod.invoke(android.os.Process.class, (Object[]) null);
            Method getSerialNumberForUser = userManager.getClass().getMethod("getSerialNumberForUser", myUserHandle.getClass());
            Long userSerial = (Long) getSerialNumberForUser.invoke(userManager, myUserHandle);
            if (userSerial != null) {
                return String.valueOf(userSerial);
            } else {
                return "";
            }
        } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException ignored) {
        }
        return "";
    }

    public static MediaPlayer player;
    public static  Boolean
        music_playing = true;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        //Toast.makeText(this, "11", Toast.LENGTH_SHORT).show();
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "برای خروج بازگشت را دوباره بزنید", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    private void set_content()
    {

        //////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////TAB BAR
        //////////////////////////////////////////////////////////////////////






    RelativeLayout lay_top_menu_right= (RelativeLayout) findViewById(R.id.lay_top_menu_right);

        ConstraintLayout.LayoutParams lp_lay_top_menu_right = (ConstraintLayout.LayoutParams) lay_top_menu_right.getLayoutParams();
        lp_lay_top_menu_right.width = (int)(screenWidth*0.24);
        lp_lay_top_menu_right.height = (int)(screenHeight*0.07);

        //new ConstraintLayout.LayoutParams((int)(screenWidth*0.1),(int)(screenHeight*0.1));
        lay_top_menu_right.setLayoutParams(lp_lay_top_menu_right);

        ImageView img_coin = (ImageView) findViewById(R.id.img_coin);
        ConstraintLayout.LayoutParams lp_default = (ConstraintLayout.LayoutParams) img_coin.getLayoutParams();
        lp_default.width = (int)(screenWidth*0.08);
        lp_default.height = (int)(screenHeight*0.09);
        img_coin.setLayoutParams(lp_default);

        ImageView img_add_coin = (ImageView) findViewById(R.id.img_add_coin);
        ConstraintLayout.LayoutParams lp_default1 = (ConstraintLayout.LayoutParams) img_add_coin.getLayoutParams();
        lp_default1.width = (int)(screenWidth*0.04);
        lp_default1.height = (int)(screenHeight*0.04);
        img_add_coin.setLayoutParams(lp_default1);


        TextView txt_coin = (TextView) findViewById(R.id.txt_coin);
        txt_coin.setTypeface(tf);
        txt_coin.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.05));


        RelativeLayout lay_top_menu_left1= (RelativeLayout) findViewById(R.id.lay_top_menu_left);

        ConstraintLayout.LayoutParams lp_lay_top_menu_left1 = (ConstraintLayout.LayoutParams) lay_top_menu_left1.getLayoutParams();
        lp_lay_top_menu_left1.width = (int)(screenWidth*0.24);
        lp_lay_top_menu_left1.height = (int)(screenHeight*0.07);
        lay_top_menu_left1.setLayoutParams(lp_lay_top_menu_left1);

        ImageView img_bell = (ImageView) findViewById(R.id.img_bell);
        ConstraintLayout.LayoutParams lp_default3 = (ConstraintLayout.LayoutParams) img_bell.getLayoutParams();
        lp_default3.width = (int)(screenWidth*0.08);
        lp_default3.height = (int)(screenHeight*0.09);
        img_bell.setLayoutParams(lp_default3);



        TextView txt_notification = (TextView) findViewById(R.id.txt_notification);
        txt_notification.setTypeface(tf);
        txt_notification.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.05));





        ////////////////////////////////////////////////////////////////////// End OF
        ////////////////////////////////////////////////////////////////////// TAB BAR
        //////////////////////////////////////////////////////////////////////










        //////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////// Profile Place
        //////////////////////////////////////////////////////////////////////



        int
                img_circle_width = (int)(screenWidth * .2);
        int
                img_circle_height = (int)(screenWidth * .2);
        int
                one_in_three_screen = (int)(screenWidth/3);


        TextView txt_level = (TextView) findViewById(R.id.txt_level);
        txt_level.setTypeface(tf);
        txt_level.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.05));






        TextView txt_point = (TextView) findViewById(R.id.txt_point);
        txt_point.setTypeface(tf);
        txt_point.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.05));

        TextView txt_profile_name = (TextView) findViewById(R.id.txt_profile_name);
        txt_profile_name.setTypeface(tf);
        txt_profile_name.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.05));





        int
                img_avatar_width = (int)(screenWidth * .21);
        int
                img_avatar_height = (int)(screenWidth * .21);

        RelativeLayout.LayoutParams lp_img_avatar = new RelativeLayout.LayoutParams(img_avatar_width,img_avatar_height);
        //lp_img_avatar.setMargins(0, 0, (int)((one_in_three_screen-img_circle_width)/2), 0);

        ImageView img_avatar = (ImageView) findViewById(R.id.img_avatar);
        img_avatar.setLayoutParams(lp_img_avatar);



        ////////////////////////////////////////////////////////////////////// End of
        ////////////////////////////////////////////////////////////////////// Profile Place
        //////////////////////////////////////////////////////////////////////



        //////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////// New Game
        //////////////////////////////////////////////////////////////////////



        ImageView img_new_game = (ImageView) findViewById(R.id.img_new_game);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams((int)(screenWidth*0.07),(int)(screenHeight*0.07));
        //lp_img_avatar.setMargins(0, 0, (int)((one_in_three_screen-img_circle_width)/2), 0);


        img_new_game.setLayoutParams(lp1);

        TextView txt_new_game = (TextView) findViewById(R.id.txt_new_game);
        txt_new_game.setTypeface(tf);
        txt_new_game.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.05));











        ////////////////////////////////////////////////////////////////////// End of
        ////////////////////////////////////////////////////////////////////// New Game
        //////////////////////////////////////////////////////////////////////



        //////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////// Cricles
        //////////////////////////////////////////////////////////////////////

//




        TextView lbl_your_turn = (TextView) findViewById(R.id.lbl_your_turn);
        lbl_your_turn.setTypeface(tf);
        lbl_your_turn.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.04));


        TextView lbl_rival_turn = (TextView) findViewById(R.id.lbl_rival_turn);
        lbl_rival_turn.setTypeface(tf);
        lbl_rival_turn.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.04));


        TextView lbl_done_game = (TextView) findViewById(R.id.lbl_done_game);
        lbl_done_game.setTypeface(tf);
        lbl_done_game.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.04));







//
//
//
//        LinearLayout.LayoutParams lp_lay_your_turn = new LinearLayout.LayoutParams((int)(screenWidth*.25),(int)(screenWidth*.25));
//        //lp_img_avatar.setMargins(0, 0, (int)((one_in_three_screen-img_circle_width)/2), 0);
//
//        RelativeLayout lay_your_turn = (RelativeLayout) findViewById(R.id.lay_your_turn);
//        lay_your_turn.setLayoutParams(lp_lay_your_turn);
//
//        TextView txt_your_turn = (TextView) findViewById(R.id.txt_nobat);
//        txt_your_turn.setTypeface(tf);
//        txt_your_turn.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.12));
//
//
//        RelativeLayout lay_wait = (RelativeLayout) findViewById(R.id.lay_wait);
//        lay_wait.setLayoutParams(lp_lay_your_turn);
//
//
//        LinearLayout lay_nobat_wait_text = (LinearLayout) findViewById(R.id.lay_nobat_wait_text);
//
//        LinearLayout.LayoutParams lp_lay_nobat_wait_text = (LinearLayout.LayoutParams) lay_nobat_wait_text.getLayoutParams();
//        lp_lay_nobat_wait_text.setMargins(0, (int) (screenHeight * 0.00), 0, 0);
//        lp_lay_nobat_wait_text.height = (int) (screenHeight*.05);
//        lay_nobat_wait_text.setLayoutParams(lp_lay_nobat_wait_text);
//
//
//        TextView txt_wait = (TextView) findViewById(R.id.txt_wait);
//        txt_wait.setTypeface(tf);
//        txt_wait.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.14));
//
//        TextView lbl_wait = (TextView) findViewById(R.id.lbl_wait);
//        lbl_wait.setTypeface(tf);
//        lbl_wait.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.05));
//
//        TextView lbl_your_turn = (TextView) findViewById(R.id.lbl_your_turn);
//        lbl_your_turn.setTypeface(tf);
//        lbl_your_turn.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.05));
//
//
//
//
//        LinearLayout lay_done_all = (LinearLayout) findViewById(R.id.lay_done_all);
//        //LinearLayout.LayoutParams lp_lay_tabBar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)(screenHeight*0.1));
//        LinearLayout.LayoutParams lp_lay_done_all = (LinearLayout.LayoutParams) lay_done_all.getLayoutParams();
//        lp_lay_done_all.setMargins(0, (int) (screenHeight * 0.00), 0, 0);
//        lp_lay_done_all.height = (int)(screenWidth*.26);
//        lay_done_all.setLayoutParams(lp_lay_done_all);
//
//
//
//
//        LinearLayout.LayoutParams lp_lay_done = new LinearLayout.LayoutParams((int)(screenWidth*.25),(int)(screenWidth*.25));
//        //lp_img_avatar.setMargins(0, 0, (int)((one_in_three_screen-img_circle_width)/2), 0);
//
//        RelativeLayout lay_done = (RelativeLayout) findViewById(R.id.lay_done);
//        lay_done.setLayoutParams(lp_lay_done);
//
//        LinearLayout lay_done_all_text = (LinearLayout) findViewById(R.id.lay_done_all_text);
//
//        LinearLayout.LayoutParams lp_lay_done_all_text = (LinearLayout.LayoutParams) lay_done_all_text.getLayoutParams();
//        lp_lay_done_all_text.setMargins(0, (int) (screenHeight * 0.00), 0, 0);
//        lp_lay_done_all_text.height = (int) (screenHeight*.05);
//        lay_done_all_text.setLayoutParams(lp_lay_done_all_text);
//
//        TextView txt_done = (TextView) findViewById(R.id.txt_done);
//        txt_done.setTypeface(tf);
//        txt_done.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.12));
//
//        TextView lbl_done = (TextView) findViewById(R.id.lbl_done);
//        lbl_done.setTypeface(tf);
//        lbl_done.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.05));









    }

    private void check_gcm()
    {
        Intent i = getIntent();

        String
                name = "aaa";
        String
                email = "qqqq";

        SharedPreferences settings = getApplicationContext().getSharedPreferences("homeScore", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("homeScore", functions.u_name);
        editor.apply();

        try{
        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);

        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(this);

//        lblMessage = (TextView) findViewById(R.id.lbl_message);
//
        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                DISPLAY_MESSAGE_ACTION));

        // Get GCM registration id
        final String regId = GCMRegistrar.getRegistrationId(this);

        //Toast.makeText(this,regId,Toast.LENGTH_LONG).show();

  //      functions.u_name = "majid";
       // Toast.makeText(getBaseContext(),"home->"+functions.u_name,Toast.LENGTH_SHORT).show();

        // Check if regid already presents
        if (regId.equals("")) {
            // Registration is not present, register now with GCM
            GCMRegistrar.register(this, SENDER_ID);
           //  Toast.makeText(getApplicationContext(), "registering with GCM", Toast.LENGTH_LONG).show();





        } else {
            // Device is already registered on GCM
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                // Skips registration.
                // Toast.makeText(getApplicationContext(), "Already registered with GCM " + regId +"-"+functions.reg_id , Toast.LENGTH_LONG).show();
                if (!regId.equals(functions.reg_id)) {
                    mm = new MyAsyncTask();
                    is_requested = true;
                    mm.url = (getResources().getString(R.string.site_url) + "register_gcm.php?param=register_gcm&uname=" + functions.u_name + "&reg_id=" + String.valueOf(regId));
                    mm.execute("");
                }

// Apply the edits!


            } else {
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.




                //lay_wait.setVisibility(View.VISIBLE);

               // Toast.makeText(getApplicationContext(), "not registered with GCM", Toast.LENGTH_LONG).show();

            }
        }
        }
        catch (Exception e1)
        {
            Log.d("majid","error gcm"+e1.getMessage());
        }
    }

    private void remove_message()
    {
        LinearLayout lay_main = (LinearLayout) findViewById(R.id.lay_main);

        fun.enableDisableView(lay_main, true);

        RelativeLayout lay_message = (RelativeLayout) findViewById(R.id.lay_message);
        lay_message.setVisibility(View.GONE);
    }
    private void run_last_query()
    {
        remove_message();
        lay_wait.setVisibility(View.VISIBLE);
        x = 1;
        is_requested = true;

        no_connection=false;
        mm = new MyAsyncTask();

        mm.url = (last_requested_query);
        mm.execute("");

    }
    private void get_user_info()
    {
        lay_wait.setVisibility(View.VISIBLE);
        x = 1;
        is_requested = true;
        mm = new MyAsyncTask();
        last_requested_query=getResources().getString(R.string.site_url) + "get_user_info.php?param=get_user_main_info&list_type="+list_type+"&uname="+functions.u_name+"&rnd="+String.valueOf(new Random().nextInt());
       // Toast.makeText(MainActivity.this, last_requested_query, Toast.LENGTH_SHORT).show();
        mm.url = (last_requested_query);
        mm.execute("");

    }
    public void clk_logout(View v)
    {

        startActivityForResult(new Intent(this,Setting.class),1);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Toast.makeText(getBaseContext(),String.valueOf(requestCode)+"---"+String.valueOf(resultCode),Toast.LENGTH_LONG).show();
        if (requestCode == 1)
            if (resultCode == Activity.RESULT_OK) {
                SharedPreferences settings = getApplicationContext().getSharedPreferences("homeScore", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("homeScore", "");
                editor.apply();
                startActivity(new Intent(MainActivity.this,Register.class));
                finish();
            }
    }
    String tag="majid";
    @Override
    public void onStart()
    {
        super.onStart();
        Log.d(tag, "In the onStart() event");
    }
    @Override
    public void onRestart()
    {
        super.onRestart();
        Log.d(tag, "In the onRestart() event");
    }
    @Override
    public void onPause()
    {
        super.onPause();
        Log.d(tag, "In the onPause() event");
        player.pause();
    }
    @Override
    public void onStop()
    {
        super.onStop();
        Log.d(tag, "In the onStop() event");
      //  player.pause();
    }
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub

        try{
            if(mHandleMessageReceiver!=null)
                unregisterReceiver(mHandleMessageReceiver);
        }catch(Exception e)
        {

        }
        super.onDestroy();

    }
    private void retry_msg()
    {
        show_msg("اووپس", "عدم ارتباط با سرور", "تلاش مجدد&خروج");
    }
    private void show_msg(String msg_title,String msg_text,String msg_buttons) {

        LinearLayout lay_main = (LinearLayout) findViewById(R.id.lay_main);

        fun.enableDisableView(lay_main, false);

        RelativeLayout lay_message = (RelativeLayout) findViewById(R.id.lay_message);
        lay_message.setVisibility(View.VISIBLE);

        TextView txt_msg_title = (TextView) findViewById(R.id.txt_msg_title);
        txt_msg_title.setText(msg_title);

        TextView txt_msg_text = (TextView) findViewById(R.id.txt_msg_text);
        txt_msg_text.setText(msg_text);
        String
                btn1_title = "";
        Button btn_msg1 = (Button) findViewById(R.id.btn_msg1);
        Button btn_msg2 = (Button) findViewById(R.id.btn_msg2);
        Button btn_msg3 = (Button) findViewById(R.id.btn_msg3);
        int
                btn_count = 1;
        btn_msg1.setText("");
        btn_msg2.setText("");
        btn_msg3.setText("");

        for (int i = 0; i < msg_buttons.length(); i++) {
            if (msg_buttons.charAt(i) != '&') {
                if (btn_count == 1) {
                    btn_msg1.setText(btn_msg1.getText() + String.valueOf(msg_buttons.charAt(i)));
                }
                if (btn_count == 2) {
                    btn_msg2.setText(btn_msg2.getText() + String.valueOf(msg_buttons.charAt(i)));
                }
                if (btn_count == 3) {
                    btn_msg3.setText(btn_msg3.getText() + String.valueOf(msg_buttons.charAt(i)));
                }
            } else {
                btn_count++;
            }
        }
        if (btn_count == 1) {
            btn_msg2.setVisibility(View.GONE);
            btn_msg3.setVisibility(View.GONE);
        }
        if (btn_count == 2) {
            btn_msg3.setVisibility(View.GONE);
        }
    }


    public void clk_new_game(View v)
    {
//        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
//        builder1.setMessage("فعلا یکم زوده هنوز یکم کار دارده!")
//                .setCancelable(false)
//                .setPositiveButton("اوکی", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//
//                        //   finish();
//                    }
//                });
//        AlertDialog alert1 = builder1.create();
//        alert1.show();
        if(Integer.valueOf(coin_count)>=50) {
            Intent i = new Intent(MainActivity.this, SelectRival.class);
            startActivity(i);
        }
        else
        {
            Toast.makeText(getBaseContext(),"متاسفانه تعداد سکه های شما برای شروع بازی جدید کافی نمی باشد",Toast.LENGTH_LONG).show();
        }
    }

    public void clk_btn_msg1(View view) {

        Button btn_all = (Button) view;

        if (btn_all.getText().toString().equals("خروج"))
        {
            finish();
        }
        if (btn_all.getText().toString().equals("ادامه"))
        {
            remove_message();
        }
        if (btn_all.getText().toString().equals("آپدیت"))
        {
            String url= "https://cafebazaar.ir/app/com.sputa.persiantrivia/?l=fa";
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
            finish();

//            String url= "http://myket.ir/app/com.sputa.persiantrivia/?l=fa";
//            Intent intent = new Intent();
//            intent.setAction(Intent.ACTION_VIEW);
//            intent.setData(Uri.parse(url));
//            startActivity(intent);
//            finish();



        }

        if (btn_all.getText().toString().equals("اوکی"))
        {
            remove_message();
        }
        if (btn_all.getText().toString().equals("بله"))
        {
            lay_wait.setVisibility(View.VISIBLE);
            x = 1;
            is_requested = true;
            mm = new MyAsyncTask();
            last_requested_query=getResources().getString(R.string.site_url) + "do.php?param=daily_gift&uname="+functions.u_name+"&rnd="+String.valueOf(new Random().nextInt());
            mm.url = (last_requested_query);
            mm.execute("");
            remove_message();
            fun.enableDisableView(lay_main,false);
        }
        if (btn_all.getText().toString().equals("خیر"))
        {
            remove_message();
        }


        if(btn_all.getText().toString().equals("تلاش مجدد"))
        {
            if (no_connection)
            {
                run_last_query();
            }
        }
    }
    public void clk_request_game(View view) {
        Intent i = new Intent(this,Request_game.class);

        startActivity(i);
    }
    public void clk_lay_wait(View view) {
        Intent i = new Intent(this,Game_list.class);
        i.putExtra("list_type","wait");
        startActivity(i);
    }

    public void clk_lay_your_turn(View view) {
        Intent i = new Intent(this,Game_list.class);
        i.putExtra("list_type","turn");
        startActivity(i);
    }

    public void clk_lay_done(View view) {
        Intent i = new Intent(this,Game_list.class);
        i.putExtra("list_type","done");
        startActivity(i);
    }

    public void clk_statistics(View view) {
        Intent i = new Intent(this,Statistics.class);
        startActivity(i);
    }

    public void clk_stotr(View view) {
        Intent i = new Intent(this,Store.class);
        startActivity(i);
    }

    public void clk_gift(View view) {

        show_msg("جایزه","آیا می خواهید سهمیه روزانه 100 سکه را دریافت کنید","بله&خیر");

    }

    public void clk_love(View view) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
        builder1.setMessage("دوست گرامی این برنامه در حال تکمیل شدن می باشد لطفا با نظرات سازنده و امتیاز خوب ما را حمایت کنید،ممنون")
                .setCancelable(false)
                .setPositiveButton("اوکی", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {

                            Intent intent = new Intent(Intent.ACTION_EDIT);
                            intent.setData(Uri.parse("bazaar://details?id=" + "com.sputa.persiantrivia"));
                            intent.setPackage("com.farsitel.bazaar");
                            startActivity(intent);
//
//                            String url= "myket://comment?id=com.sputa.persiantrivia";
//                            Intent intent = new Intent();
//                            intent.setAction(Intent.ACTION_VIEW);
//                            intent.setData(Uri.parse(url));
//                            startActivity(intent);
                           // finish();

                        }
                        catch (Exception e1)
                        {
                            Toast.makeText(MainActivity.this, R.string.sadly_apear_error, Toast.LENGTH_SHORT).show();
                        }

                        //   finish();
                    }
                });
        AlertDialog alert1 = builder1.create();
        alert1.show();
    }

    public void clk_telegram(View view) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("tg://resolve?domain=rival_killer"));
           i.setPackage("org.telegram.messenger");
            MainActivity.this.startActivity(i);
            // ارسال موفق بود
        }
        catch (Exception e) {
            Toast.makeText(getBaseContext(),"متاسفانه تلگرام روی گوشی شما نصب نمی باشد"+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public void stop_music(View view) {
        if(music_playing)
        {
            player.pause();
            music_playing=false;
            ImageView img_music = (ImageView) findViewById(R.id.img_music);
            img_music.setBackground(getResources().getDrawable(R.drawable.music_not));
        }
        else
        {
            player.start();
            music_playing=true;
            ImageView img_music = (ImageView) findViewById(R.id.img_music);
            img_music.setBackground(getResources().getDrawable(R.drawable.music));
        }

    }
    public String selected_game_list="turn";
    public void clk_lay_game_lists(View view) {

        //Toast.makeText(MainActivity.this, "majid", Toast.LENGTH_SHORT).show();
        LinearLayout selected_lay = (LinearLayout) view;
        String idstr = selected_lay.getResources().getResourceName(selected_lay.getId());
        //Toast.makeText(MainActivity.this, , Toast.LENGTH_SHORT).show();
        int
                start=idstr.indexOf("id/");
        String
                name=idstr.substring(start+3,idstr.length()-1);
        ///Toast.makeText(MainActivity.this, name, Toast.LENGTH_SHORT).show();

        LinearLayout lay_your_turn1 = (LinearLayout) findViewById(R.id.lay_your_turn1);
        LinearLayout lay_your_turn2 = (LinearLayout) findViewById(R.id.lay_your_turn2);
        LinearLayout lay_your_turn3 = (LinearLayout) findViewById(R.id.lay_your_turn3);

        LinearLayout lay_rival_turn1 = (LinearLayout) findViewById(R.id.lay_rival_turn1);
        LinearLayout lay_rival_turn2 = (LinearLayout) findViewById(R.id.lay_rival_turn2);
        LinearLayout lay_rival_turn3 = (LinearLayout) findViewById(R.id.lay_rival_turn3);

        LinearLayout lay_done_game1 = (LinearLayout) findViewById(R.id.lay_done_game1);
        LinearLayout lay_done_game2 = (LinearLayout) findViewById(R.id.lay_done_game2);
        LinearLayout lay_done_game3 = (LinearLayout) findViewById(R.id.lay_done_game3);




        lay_your_turn3.setBackgroundColor(Color.WHITE);
        lay_rival_turn3.setBackgroundColor(Color.WHITE);
        lay_done_game3.setBackgroundColor(Color.WHITE);

        lay_your_turn2.setBackgroundColor(Color.BLACK);
        lay_rival_turn2.setBackgroundColor(Color.BLACK);
        lay_done_game2.setBackgroundColor(Color.BLACK);

        lay_your_turn1.setBackgroundColor(Color.parseColor("#89c6b4"));
        lay_rival_turn1.setBackgroundColor(Color.parseColor("#89c6b4"));
        lay_done_game1.setBackgroundColor(Color.parseColor("#89c6b4"));

        if(name.equals("lay_your_turn"))
        {
            lay_your_turn3.setBackgroundColor(Color.BLACK);
            lay_your_turn2.setBackgroundColor(Color.WHITE);
            lay_your_turn1.setBackgroundColor(Color.parseColor("#7cb3a2"));
            if(!list_type.equals("turn"))
            {
                list_type = "turn";
                get_user_info();
            }


        }
        if(name.equals("lay_rival_turn"))
        {
            lay_rival_turn3.setBackgroundColor(Color.BLACK);
            lay_rival_turn2.setBackgroundColor(Color.WHITE);
            lay_rival_turn1.setBackgroundColor(Color.parseColor("#7cb3a2"));
            if(!list_type.equals("wait"))
            {
                list_type = "wait";
                get_user_info();
            }

        }
        if(name.equals("lay_done_game"))
        {
            if(!list_type.equals("done"))
            {
                list_type = "done";
                get_user_info();
            }

            lay_done_game3.setBackgroundColor(Color.BLACK);
            lay_done_game2.setBackgroundColor(Color.WHITE);
            lay_done_game1.setBackgroundColor(Color.parseColor("#7cb3a2"));
        }





    }

    public void clk_question_factory(View view) {
        startActivity(new Intent(this,Question_factory.class));
    }


    public class Timer1 extends Thread {

        int oneSecond=1000;
        int value=0;
        String TAG="Timer";
        String typ="";
        //@Override
        public Timer1(String type)
        {
            typ = type;
        }
        @Override
        public void run() {

            for(;;){


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {





                        if(typ.equals("circulation")) {
                            if(is_rotation) {
                                RelativeLayout.LayoutParams lp_coin = new RelativeLayout.LayoutParams((int) (screenWidth * .15), (int) (screenWidth * .15));
                                //lp_coin.setMargins(100,400,0,0);
                                lp_coin.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                                img_circle1.setLayoutParams(lp_coin);

                                x++;
                                img_circle1.setRotation(x);
                                if(((int)x/100) > fun.request_time_out && is_requested)
                                {
//                                    TextView lbl_message = (TextView) findViewById(R.id.lbl_message);
//                                    lbl_message.setText("ارتباط قطع شد");
                                    lay_wait.setVisibility(View.GONE);
                                    show_msg("اووپس","عدم ارتباط با سرور","تلاش مجدد&خروج");
                                    is_requested=false;
                                    no_connection = true;

                                }
                            }
                        }








                    }
                });


                //   Log.d("majid", String.valueOf(value));
                //Thread.currentThread();
                try {


                    Thread.sleep(oneSecond/100);
                    //	Log.d(TAG, " " + value);
                } catch (InterruptedException e) {
                    System.out.println("timer interrupted");
                    //value=0;
                    e.printStackTrace();
                }
            }
        }



    }

    public String coin_count="";
    private void set_main_info(String output_str)
    {



        int
                start_str = output_str.indexOf("<coin>");
        int
                end_str = output_str.indexOf("</coin>");
        String
            coin_str = output_str.substring(start_str + 6, end_str);
        coin_count = coin_str;


        start_str = output_str.indexOf("<point>");
        end_str = output_str.indexOf("</point>");
        String
                point_str = output_str.substring(start_str + 7, end_str);



        start_str = output_str.indexOf("<level>");
        end_str = output_str.indexOf("</level>");
        Log.d("majid",output_str);
        String
                level_str = output_str.substring(start_str + 7, end_str);



        start_str = output_str.indexOf("<my_turn>");
        end_str = output_str.indexOf("</my_turn>");
        String
                my_turn_str = output_str.substring(start_str + 9, end_str);



        start_str = output_str.indexOf("<not_my_turn>");
        end_str = output_str.indexOf("</not_my_turn>");
        String
                not_my_turn_str = output_str.substring(start_str + 13, end_str);



        start_str = output_str.indexOf("<finished>");
        end_str = output_str.indexOf("</finished>");
        String
                finished_str = output_str.substring(start_str + 10, end_str);

        start_str = output_str.indexOf("<gender>");
        end_str = output_str.indexOf("</gender>");
        String
                gender = output_str.substring(start_str + 8, end_str);

        start_str = output_str.indexOf("<avatar_id>");
        end_str = output_str.indexOf("</avatar_id>");
        String
                avatar_id = output_str.substring(start_str + 11, end_str);


        start_str = output_str.indexOf("<s_min_version>");
        end_str = output_str.indexOf("</s_min_version>");
        s_min_version = Integer.valueOf(output_str.substring(start_str + 15, end_str));


        start_str = output_str.indexOf("<s_current_version>");
        end_str = output_str.indexOf("</s_current_version>");
        s_current_version = Integer.valueOf(output_str.substring(start_str + 19, end_str));

        int versionCode = BuildConfig.VERSION_CODE;


        if(versionCode<s_min_version)
        {
            show_msg("بروزرسانی","برنامه نیاز به بروزرسانی دارد لطفا برنامه را آپدیت کنید","آپدیت&خروج");
        }
        else if (versionCode!=s_current_version && functions.update_message_showed==0)
        {
            Toast.makeText(MainActivity.this, "برنامه نیاز به بروزرسانی دارد لطفا برنامه را آپدیت کنید", Toast.LENGTH_SHORT).show();
            functions.update_message_showed=1;

        }

        if(gender.equals("1"))
        {
            functions.gender="boy";
        }
        else {
            functions.gender="girl";
        }
        final ImageView image = (ImageView) findViewById(R.id.img_avatar);

        if(functions.gender.equals("boy"))
        {
            image.setBackground(getResources().getDrawable(R.drawable.profile_mail));

        }
        else
        {
            image.setBackground(getResources().getDrawable(R.drawable.profile_female));

        }
        functions.avatar_name = fun.u_name+"_"+avatar_id+".jpg";


        // Image url
        String image_url = "http://sputa-app.ir/app/trivia/pic/"+functions.avatar_name;


        Picasso.with(getBaseContext()).load(image_url).fit().into(image);


      //  Toast.makeText(MainActivity.this, image_url, Toast.LENGTH_SHORT).show();
        
        //Toast.makeText(this,coin_str+"-"+level_str+"-"+point_str,Toast.LENGTH_LONG).show();

        TextView txt_point = (TextView) findViewById(R.id.txt_point);
        TextView txt_level = (TextView) findViewById(R.id.txt_level);



        TextView txt_nobat = (TextView) findViewById(R.id.lbl_your_turn);
        TextView txt_wait = (TextView) findViewById(R.id.lbl_rival_turn);
        TextView txt_done = (TextView) findViewById(R.id.lbl_done_game);




        txt_point.setText("امتیاز "+point_str);

        txt_level.setText("سطح "+level_str);

        txt_nobat.setText("نوبت شما ("+my_turn_str+")");
        txt_wait.setText("نوبت رقیب ("+not_my_turn_str+")");
        txt_done.setText("تمام شده ("+finished_str+")");


    }
    @Override
    public void onResume(){
        super.onResume();
        get_user_info();

        Log.d(tag, "In the onResume() event");


        if(MainActivity.music_playing)
            MainActivity.player.start();




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

                if(is_requested) {



                    if (param_str.equals("register_gcm")) {
                        if (output_str.equals("updated")) {
                           // lbl_msg.setText("آپدیت شد");
                            GCMRegistrar.setRegisteredOnServer(getBaseContext(), true);
                        }

                    }




                }
                if (param_str.equals("daily_gift") && is_requested) {
                    start1 = ss.indexOf("<result>");
                    end1 = ss.indexOf("</result>");
                    fun.enableDisableView(lay_main,true);

                    lay_wait.setVisibility(View.GONE);
                    is_requested = false;

                    String res = ss.substring(start1 + 8, end1);


                    if(res.equals("ok"))
                    {

                        lay_wait.setVisibility(View.GONE);
                        is_requested = false;



                        show_msg("","100 سکه به سکه های شما افزوده شد","اوکی");
                    }
                    else if(res.equals("repeated"))
                    {
                        show_msg("","شما امروز سهمیه خود را دریافت کرده اید","اوکی");
                    }
                }


                if (param_str.equals("get_user_main_info") && is_requested) {
                  //  Toast.makeText(getApplicationContext(),output_str,Toast.LENGTH_LONG).show();
                    lay_wait.setVisibility(View.GONE);
                    is_requested = false;
                    //show_msg("sss",output_str,"اوکی");
                    set_main_info(output_str);




                    start1 = ss.indexOf("<list_count>");
                    end1 = ss.indexOf("</list_count>");
                    int  cnt = Integer.valueOf(ss.substring(start1 + 12, end1));
                    start1 = ss.indexOf("<friend_name>");
                    end1 = ss.indexOf("</friend_name>");
                    //        Toast.makeText(getBaseContext(),ss,Toast.LENGTH_LONG).show();
                    if(end1>0) {



                        if (param_str.length() > 0) {
                            int
                                    record_count = cnt;

                            user_name = new String[record_count];
                            user_desc = new String[record_count];
                            user_level = new String[record_count];
                            user_gender = new String[record_count];
                            user_avatar_id = new String[record_count];
                            game_result = new String[record_count];

                            uig_id = new String[record_count];

                            //  resultSet.moveToFirst();
                            //    for(int i=0;i<resultSet.getCount();i++){
                            for (int i = 0; i < cnt; i++) {

                                start1 = ss.indexOf("<lst" + String.valueOf(i) + ">");
                                end1 = ss.indexOf("</lst" + String.valueOf(i) + ">");
                                String lst_item = (ss.substring(start1 + 6, end1));
                                //   Toast.makeText(getBaseContext(),lst_item,Toast.LENGTH_SHORT).show();
                                start1 = lst_item.indexOf("<friend_name>");
                                end1 = lst_item.indexOf("</friend_name>");
                                String
                                        uname = lst_item.substring(start1 + 13, end1);


                                start1 = lst_item.indexOf("<friend_level>");
                                end1 = lst_item.indexOf("</friend_level>");
                                String ulevel = lst_item.substring(start1 + 14, end1);


                                start1 = lst_item.indexOf("<rival_avatar_id>");
                                end1 = lst_item.indexOf("</rival_avatar_id>");
                                String
                                        rival_avatar_id = lst_item.substring(start1 + 17, end1);

                                start1 = lst_item.indexOf("<rival_gender>");
                                end1 = lst_item.indexOf("</rival_gender>");
                                String
                                        rival_gender = lst_item.substring(start1 + 14, end1);

                                start1 = lst_item.indexOf("<remining_hour>");
                                end1 = lst_item.indexOf("</remining_hour>");
                                String r_hour = lst_item.substring(start1 + 15, end1);


                                start1 = lst_item.indexOf("<remining_min>");
                                end1 = lst_item.indexOf("</remining_min>");
                                String r_min = lst_item.substring(start1 + 14, end1);


                                start1 = lst_item.indexOf("<rival_right_ans>");
                                end1 = lst_item.indexOf("</rival_right_ans>");
                                String rival_right_ans = lst_item.substring(start1 + 17, end1);

                                start1 = lst_item.indexOf("<user_right_ans>");
                                end1 = lst_item.indexOf("</user_right_ans>");
                                String user_right_ans = lst_item.substring(start1 + 16, end1);


                                start1 = lst_item.indexOf("<game_result>");
                                end1 = lst_item.indexOf("</game_result>");
                                String game_result1 = lst_item.substring(start1 + 13, end1);


                                start1 = lst_item.indexOf("<uig_id>");
                                end1 = lst_item.indexOf("</uig_id>");
                                String uig_id1 = lst_item.substring(start1 + 8, end1);


                                user_name[i] = uname;
                                user_level[i] = ulevel;
                                user_gender[i] = rival_gender;
                                user_avatar_id[i] = rival_avatar_id;


                                uig_id[i] = uig_id1;

                                String
                                        udesc = "زمان باقی مانده : " + r_hour + " ساعت و " + r_min + "دقیقه";
                                if (list_type.equals("done")) {
                                    user_desc[i] = "تمام شده";

                                    int
                                            int_user_right_ans = 0;
                                    int
                                            int_rival_right_ans = 0;
                                    try {

                                        int_rival_right_ans = Integer.valueOf(rival_right_ans);
                                    } catch (Exception ee) {

                                    }

                                    try {
                                        int_user_right_ans = Integer.valueOf(user_right_ans);

                                    } catch (Exception ee) {

                                    }


                                    game_result[i] = game_result1;
//
//                                    if(int_user_right_ans>int_rival_right_ans)
//                                    {
//                                        game_result[i] = "won";
//                                    }
//                                    else if(int_user_right_ans<int_rival_right_ans)
//                                    {
//                                        game_result[i]="lost";
//                                    }
//                                    else
//                                    {
//                                        game_result[i]="tie";
//                                    }


                                } else {
                                    user_desc[i] = udesc;
                                }
                            }

                            Activity act;
                          //  Toast.makeText(MainActivity.this, String.valueOf(cnt), Toast.LENGTH_SHORT).show();
                            lv.setAdapter(new Game_adapter(MainActivity.this, user_name, user_desc, user_level, uig_id, user_gender, user_avatar_id, game_result, mydatabase, list_type));
                            if(cnt>0)
                                lv.setVisibility(View.VISIBLE);
                            else {
                                lv.setAdapter(null);

                            }

                        }

                    }
                    else
                    {
                        lv.setAdapter(null);
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

    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
         //   String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping
            //WakeLocker.acquire(getApplicationContext());

            /**
             * Take appropriate action on this message
             * depending upon your app requirement
             * For now i am just displaying it on the screen
             * */

            // Showing received message
            //lblMessage.append(newMessage + "\n");
           // Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();

            // Releasing wake lock
            //  WakeLocker.release();
        }
    };



}
