package com.sputa.persiantrivia;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import ir.adad.client.Adad;
import ir.adad.client.InterstitialAdListener;

public class SelectRival extends AppCompatActivity {


    int screenWidth  = 0;
    int screenHeight = 0;


    boolean
            request_send=false;


    boolean
            no_connection =false;

    SQLiteDatabase mydatabase;



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
    public RelativeLayout lay_wait;
    public LinearLayout lay_main;
    int      x=1;
    Timer tim_circulation;
    Boolean  is_rotation = true;
    String last_requested_query="";
    public ImageView img_circle1;
    Button msg_btn1,msg_btn2,msg_btn3;


    private InterstitialAdListener mInterstitialAdListener = new InterstitialAdListener() {
        @Override
        public void onAdLoaded() {
            // این بخش زمانی که تبلیغ در برنامه شما بارگزاری می‌شود فراخوانی خواهد شد
          //  Toast.makeText(SelectRival.this, "1", Toast.LENGTH_SHORT).show();
            if(functions.ad_shown.equals("no")) {
                Adad.showInterstitialAd(getBaseContext());
                functions.ad_shown="yes";
            }
        }

        @Override
        public void onAdFailedToLoad() {
            // این بخش زمانی که مشکلی در بارگزاری تبلیغ وجود داشته باشد فراخوانی خواهد شد. به عنوان نمونه قطع شدن اینترنت و یا عدم وجود تبلیغ متناسب با برنامه شما در آن لحظه در سرور عدد
           // Toast.makeText(SelectRival.this, "2", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onInterstitialAdDisplayed() {
            // این بخش زمانی که تبلیغ به حالت نمایش داده شدن درمی‌آید فراخوانی خواهد شد
            //Toast.makeText(SelectRival.this, "3", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onMessageReceive(JSONObject message) {
            //Toast.makeText(SelectRival.this, "4", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRemoveAdsRequested() {
            // این بخش در صورتی که شما در برنامه خود امکان حذف تبلیغات را به کاربر داده باشید و کاربر آن گزینه را انتخاب کند فراخوانی خواهد شد. می‌توانید در این بخش کاربر را به صفحه فروشگاه برنامه خود هدایت کنید
           // Toast.makeText(SelectRival.this, "5", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onInterstitialClosed() {
            // این بخش زمانی که تبلیغ تمام صفحه بسته شود فراخوانی خواهد شد
          //  Toast.makeText(SelectRival.this, "6", Toast.LENGTH_SHORT).show();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Adad.prepareInterstitialAd(mInterstitialAdListener);

        setContentView(R.layout.activity_select_rival);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        fun = new functions();

        lay_main = (LinearLayout) findViewById(R.id.lay_main);

        font_name = fun.font_name;
        tf = Typeface.createFromAsset(getAssets(),font_name );



        fun = new functions();
        font_name = fun.font_name;
        tf = Typeface.createFromAsset(getAssets(),font_name );
        img_circle1 = (ImageView) findViewById(R.id.circle1);
        tim_circulation       = new Timer("circulation");

        tim_circulation.start();


        lay_wait = (RelativeLayout) findViewById(R.id.lay_wait);

        msg_btn1= (Button) findViewById(R.id.btn_msg1);
        msg_btn2= (Button) findViewById(R.id.btn_msg2);
        msg_btn3= (Button) findViewById(R.id.btn_msg3);

        set_content();
        if(MainActivity.music_playing)
            MainActivity.player.start();


        //Adad.showInterstitialAd(this);


       //

    }

    private void set_content()
    {
//        LinearLayout.LayoutParams lp_img_chance_rival = new LinearLayout.LayoutParams((int)(screenWidth*0.62),(int)(screenHeight*0.1));
//        //tab_icon_param.setMargins((int)(((int)(screenHeight*0.083))),(int)(screenHeight*0.01),(int)(screenHeight*0.08),0);
//        ImageView img_btn_chance_rival = (ImageView) findViewById(R.id.img_chance_rival);
//        img_btn_chance_rival.setLayoutParams(lp_img_chance_rival);
//        ImageView img_btn_lookup_name = (ImageView) findViewById(R.id.img_lookup_name);
//        img_btn_lookup_name.setLayoutParams(lp_img_chance_rival);
//        ImageView img_btn_with_friend = (ImageView) findViewById(R.id.img_with_friends);
//        img_btn_with_friend.setLayoutParams(lp_img_chance_rival);
//        ImageView img_btn_look_around = (ImageView) findViewById(R.id.img_look_around);
//        img_btn_look_around.setLayoutParams(lp_img_chance_rival);
//        ImageView img_btn_shake_it= (ImageView) findViewById(R.id.img_shake_it);
//        img_btn_shake_it.setLayoutParams(lp_img_chance_rival);

        TextView txt_level = (TextView) findViewById(R.id.lbl_start_game);
        txt_level.setTypeface(tf);

        TextView txt_chanced_rival = (TextView) findViewById(R.id.txt_chanced_rival);
        txt_chanced_rival.setTypeface(tf);
        TextView txt_search_name = (TextView) findViewById(R.id.txt_search_name);
        txt_search_name.setTypeface(tf);
        TextView txt_look_around = (TextView) findViewById(R.id.txt_look_around);
        txt_look_around.setTypeface(tf);
        TextView txt_shaked = (TextView) findViewById(R.id.txt_shaked);
        txt_shaked.setTypeface(tf);




    }
    @Override
    public void onPause()
    {
        super.onPause();

        MainActivity.player.pause();
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
    public void clk_btn_msg1(View view) {

        Button btn_all = (Button) view;
        // Toast.makeText(getBaseContext(),btn_all.getText().toString(),Toast.LENGTH_SHORT).show();

        if (btn_all.getText().toString().equals("اوکی"))
        {
            remove_message();
        }
        if (btn_all.getText().toString().equals("بله"))
        {
            if(request_send) {
                remove_message();
                lay_wait.setVisibility(View.VISIBLE);
                fun.enableDisableView(lay_main,false);
                x = 1;
                is_requested = true;
                mm = new MyAsyncTask();
                //last_requested_query = getResources().getString(R.string.site_url) + "do.php?param=request_game&uname=" + functions.u_name + "&rival_uname=" + rival_uname;
                //  Toast.makeText(getBaseContext(),last_requested_query,Toast.LENGTH_LONG).show();
                mm.url = (last_requested_query);
                mm.execute("");
            }
        }
        if (btn_all.getText().toString().equals("خروج"))
        {
            finish();
        }


        if(btn_all.getText().toString().equals("تلاش مجدد"))
        {
            if (no_connection)
            {
                run_last_query();
            }
        }

        if(btn_all.getText().toString().equals("خیر"))
        {
            remove_message();
        }

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
        btn_msg1.setVisibility(View.VISIBLE);
        btn_msg2.setVisibility(View.VISIBLE);
        btn_msg3.setVisibility(View.VISIBLE);
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
    private void retry_msg()
    {
        show_msg("اووپس", "عدم ارتباط با سرور", "تلاش مجدد&خروج");
    }

    @Override
    public void onResume(){
        super.onResume();
        if(MainActivity.music_playing)
            MainActivity.player.start();




    }


    public void clk_search_name(View view) {
        Intent i = new Intent(this,friend_list.class);
        i.putExtra("search_type","search_user_list");
        startActivity(i);


    }

    public void clk_friend_list(View view) {
        Intent i = new Intent(this,friend_list.class);
        i.putExtra("search_type","search_friend_list");
        startActivity(i);
    }

    public void btn_look_around(View view) {
        Intent i = new Intent(this,LookAround.class);
        i.putExtra("search_type","search_friend_list");
        startActivity(i);
    }

    public void clk_shake(View view) {
        Intent i = new Intent(this,Shake.class);
        startActivity(i);
    }

    public void clk_random_rival(View view) {

        x = 1;
        is_requested = true;

        lay_wait.setVisibility(View.VISIBLE);
        fun.enableDisableView(lay_main,false);
        mm = new MyAsyncTask();
        last_requested_query = getResources().getString(R.string.site_url) + "do.php?param=random_game&uname=" + functions.u_name+"&rnd="+String.valueOf(new Random().nextInt());;
        //  Toast.makeText(getBaseContext(),last_requested_query,Toast.LENGTH_LONG).show();
        mm.url = (last_requested_query);
        mm.execute("");




    }

    public void clk_back(View view) {
        finish();
    }


    public class Timer extends Thread {

        int oneSecond=1000;
        int value=0;
        String TAG="Timer";
        String typ="";
        //@Override
        public Timer(String type)
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

                                    //  lbl_message.setText("ارتباط قطع شد");
                                    request_send=true;
                                    lay_wait.setVisibility(View.GONE);
                                    retry_msg();
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
                //
                param_str = ss.substring(start1 + 7, end1);

                if ((param_str.equals("random_game") ) && is_requested) {
                    start1 = ss.indexOf("<permitted>");
                    end1 = ss.indexOf("</permitted>");
                    //
                    String permited = ss.substring(start1 + 11, end1);
                    start1 = ss.indexOf("<message>");
                    end1 = ss.indexOf("</message>");
                    //

                    String message = ss.substring(start1 + 9, end1);
                    lay_wait.setVisibility(View.GONE);
                    fun.enableDisableView(lay_main,true);
                    is_requested = false;
                    if(permited.equals("yes"))
                    {
                            finish();
                            Intent i = new Intent(SelectRival.this, BetweenRounds.class);
                            i.putExtra("uig_id", "0");

                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                    }
                    else
                    {
                        show_msg("حریف شانسی",message, "اوکی");
                    }


                   // Toast.makeText(SelectRival.this, output_str, Toast.LENGTH_SHORT).show();




                }


            }
        }





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
