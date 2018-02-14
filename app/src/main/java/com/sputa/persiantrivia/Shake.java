package com.sputa.persiantrivia;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class Shake extends AppCompatActivity {
    private ShakeListener mShaker;

    int screenWidth  = 0;
    int screenHeight = 0;
    functions fun;
    String
            font_name = "";
    Typeface tf;
    Boolean
        shaked = false;
    int      x=1,x2=1,shake_timer=0;
    Timer tim_circulation;
    Boolean  is_rotation = true;

    public ImageView img_circle1;
    public ImageView img_circle2;
    public MyAsyncTask mm;
    public RelativeLayout lay_wait;
    Boolean
            is_requested = false;
    String last_requested_query="";
    Boolean
        wait_for_shake=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        fun = new functions();

        font_name = fun.font_name;
        tf = Typeface.createFromAsset(getAssets(),font_name);


        lay_wait = (RelativeLayout) findViewById(R.id.lay_wait);

        LinearLayout.LayoutParams lp_img_shake = new LinearLayout.LayoutParams((int)(screenWidth*0.6),(int)(screenHeight*0.4));

        ImageView img_shake = (ImageView) findViewById(R.id.img_shake);
        img_shake.setLayoutParams(lp_img_shake);

        LinearLayout.LayoutParams lp_img_plz_wait = new LinearLayout.LayoutParams((int)(screenWidth*0.065),(int)(screenHeight*0.04));
        final ImageView img_plz_wait = (ImageView) findViewById(R.id.img_please_wait);
        img_plz_wait.setLayoutParams(lp_img_plz_wait);

        TextView txt_level = (TextView) findViewById(R.id.txt_welldone);
        txt_level.setTypeface(tf);
        txt_level.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.11));

        TextView txt_message = (TextView) findViewById(R.id.txt_message);
        txt_message.setTypeface(tf);
        txt_message.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.09));

        final Vibrator vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        if(!isNetworkAvailable())
        {
            txt_message.setText("خطا در اتصال به اینترنت");
        }
       // Toast.makeText(this,functions.u_name,Toast.LENGTH_SHORT).show();
        mShaker = new ShakeListener(this);
        mShaker.setOnShakeListener(new ShakeListener.OnShakeListener () {
            public void onShake()
            {
                if(!shaked && isNetworkAvailable()) {
                   shake_it();
                }
            }
        });

        img_circle1 = (ImageView) findViewById(R.id.circle1);
        img_circle2 = (ImageView) findViewById(R.id.img_please_wait);
        is_rotation=true;
        tim_circulation       = new Timer("circulation");

        tim_circulation.start();
        if(MainActivity.music_playing)
            MainActivity.player.start();
    }
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:

                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };
    @Override
    public void onResume()
    {
        mShaker.resume();
        super.onResume();
        if(MainActivity.music_playing)
            MainActivity.player.start();
    }


    public void onBackPressed() {
        if(!shaked)
        {
            finish();
        }
    }
    @Override
    public void onPause()
    {
        mShaker.pause();
        mm = new MyAsyncTask();
        last_requested_query=getResources().getString(R.string.site_url) + "do.php?param=cancel_shaked&uname="+functions.u_name+"&rnd="+String.valueOf(new Random().nextInt());
        //  Toast.makeText(getBaseContext(),last_requested_query,Toast.LENGTH_LONG).show();
        mm.url = (last_requested_query);
        mm.execute("");
        Log.d("majid","pause");
        super.onPause();
        MainActivity.player.pause();
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void clk_btn(View view) {
shake_it();



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
    private void retry_msg()
    {
        show_msg("اووپس", "عدم ارتباط با سرور", "تلاش مجدد&خروج");
    }

    public void shooken(View view) {
        shake_it();
    }
    public void shake_it()
    {
        final Vibrator vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        final ImageView img_plz_wait = (ImageView) findViewById(R.id.img_please_wait);
        vibe.vibrate(300);

        final ImageView img_shake = (ImageView) findViewById(R.id.img_shake);
        img_shake.setAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_out));

        Animation zoomout = AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_out);
        img_shake.setAnimation(zoomout);
        img_shake.startAnimation(zoomout);
        TextView txt_welldone = (TextView) findViewById(R.id.txt_welldone);
        txt_welldone.setVisibility(View.VISIBLE);
        TextView txt_message = (TextView) findViewById(R.id.txt_message);
        txt_message.setText("لطفا کمی صبر کنید");
        img_plz_wait.setVisibility(View.VISIBLE);

        x = 1;
        is_requested = true;
        mm = new MyAsyncTask();
        last_requested_query=getResources().getString(R.string.site_url) + "do.php?param=shaked&uname="+functions.u_name+"&rnd="+String.valueOf(new Random().nextInt());
        //  Toast.makeText(getBaseContext(),last_requested_query,Toast.LENGTH_LONG).show();
        mm.url = (last_requested_query);
        mm.execute("");
        shaked = true;
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



                        if(wait_for_shake)
                        {
                            shake_timer++;
                            if(((int)shake_timer/100) > 10 )
                            {
                                wait_for_shake=false;
                                x = 1;
                                is_requested = true;
                                mm = new MyAsyncTask();
                                last_requested_query=getResources().getString(R.string.site_url) + "do.php?param=get_shaked&uname="+functions.u_name+"&rnd="+String.valueOf(new Random().nextInt());
                                //  Toast.makeText(getBaseContext(),last_requested_query,Toast.LENGTH_LONG).show();
                                mm.url = (last_requested_query);
                                mm.execute("");
                            }

                            if(!functions.shaked_uname.equals(""))
                            {
                                TextView txt_message = (TextView) findViewById(R.id.txt_message);
                                txt_message.setText("حریف پیدا شد:"+functions.shaked_uname);
                                wait_for_shake = false;
                                is_requested = false;
                                lay_wait.setVisibility(View.VISIBLE);
                                TextView txt_wait_message = (TextView) findViewById(R.id.txt_wait_message);
                                txt_wait_message.setText("حریف پیدا شد:"+functions.shaked_uname+"\n"+"لطفا تا شروع بازی صبر کنید");



                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent i = new Intent(getBaseContext(), BetweenRounds.class);
                                        i.putExtra("uig_id", functions.shaked_g_id);
                                        startActivity(i);
                                        finish();

                                    }
                                }, 3000);
                            }
                        }

                        if(typ.equals("circulation")) {

                            img_circle2.setRotation(x2);
                            x2++;
                            if(is_rotation) {
                                RelativeLayout.LayoutParams lp_coin = new RelativeLayout.LayoutParams((int) (screenWidth * .15), (int) (screenWidth * .15));
                                //lp_coin.setMargins(100,400,0,0);
                                lp_coin.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                                img_circle1.setLayoutParams(lp_coin);
                                //img_circle2.setLayoutParams(lp_coin);

                                img_circle1.setRotation(x);
                                x++;

                                if(((int)x/100) > 5 && is_requested)
                                {
                                    //TextView lbl_message = (TextView) findViewById(R.id.lbl_message);
                                    //  lbl_message.setText("ارتباط قطع شد");
                                    //lay_wait.setVisibility(View.GONE);
                                   // retry_msg();
                                    TextView txt_message = (TextView) findViewById(R.id.txt_message);
                                    txt_message.setText("خطا در اتصال به سرور");
                                    is_requested=false;
                                    shaked=false;
                                  //  no_connection = true;

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

                param_str = ss.substring(start1 + 7, end1);

                if ((param_str.equals("shaked") && is_requested)) {
                   //  Toast.makeText(getBaseContext(),ss,Toast.LENGTH_LONG).show();
                   // lay_wait.setVisibility(View.GONE);
                    start1 = ss.indexOf("<rival_found>");
                    end1 = ss.indexOf("</rival_found>");
                    String
                       rival_found = ss.substring(start1 + 13, end1);
//                    Toast.makeText(getBaseContext(),ss,Toast.LENGTH_LONG).show();
//                    Toast.makeText(getBaseContext(),ss,Toast.LENGTH_LONG).show();
//                    Toast.makeText(getBaseContext(),ss,Toast.LENGTH_LONG).show();
//                    Toast.makeText(getBaseContext(),ss,Toast.LENGTH_LONG).show();
//                    Toast.makeText(getBaseContext(),ss,Toast.LENGTH_LONG).show();
//                    Toast.makeText(getBaseContext(),ss,Toast.LENGTH_LONG).show();

                    if(rival_found.equals("1"))
                    {
                        start1 = ss.indexOf("<rival_uname>");
                        end1 = ss.indexOf("</rival_uname>");
                        String
                                rival = ss.substring(start1 + 13, end1);
                        TextView txt_message = (TextView) findViewById(R.id.txt_message);
                        txt_message.setText("حریف پیدا شد:"+rival);
                        lay_wait.setVisibility(View.VISIBLE);
                        TextView txt_wait_message = (TextView) findViewById(R.id.txt_wait_message);
                        txt_wait_message.setText("حریف پیدا شد:"+rival+"\n"+"لطفا تا شروع بازی صبر کنید");

                        start1 = ss.indexOf("<g_id>");
                        end1 = ss.indexOf("</g_id>");
                        final String
                                g_id1 = ss.substring(start1 + 6, end1);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(getBaseContext(), BetweenRounds.class);
                                i.putExtra("uig_id", g_id1);
                                startActivity(i);
                                finish();


                            }
                        }, 3000);
                    }
                    else
                    {
                        functions.shaked_uname="";
                        wait_for_shake=true;
                        shake_timer=0;
                    }
                    is_requested=false;
                }
                if ((param_str.equals("get_shaked") && is_requested)) {
                    //  Toast.makeText(getBaseContext(),ss,Toast.LENGTH_LONG).show();
                    // lay_wait.setVisibility(View.GONE);
                    start1 = ss.indexOf("<rival_found>");
                    end1 = ss.indexOf("</rival_found>");
                    String
                            rival_found = ss.substring(start1 + 13, end1);
                   // Toast.makeText(getBaseContext(),rival_found,Toast.LENGTH_LONG).show();
                    if(rival_found.equals("1"))
                    {
                        start1 = ss.indexOf("<rival_uname>");
                        end1 = ss.indexOf("</rival_uname>");
                        String
                                rival = ss.substring(start1 + 13, end1);
                        TextView txt_message = (TextView) findViewById(R.id.txt_message);
                        txt_message.setText("حریف پیدا شد:"+rival);
                        lay_wait.setVisibility(View.VISIBLE);
                        TextView txt_wait_message = (TextView) findViewById(R.id.txt_wait_message);
                        txt_wait_message.setText("حریف پیدا شد:"+rival+"\n"+"لطفا تا شروع بازی صبر کنید");

                        start1 = ss.indexOf("<g_id>");
                        end1 = ss.indexOf("</g_id>");
                        final String
                                g_id = ss.substring(start1 + 6, end1);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(getBaseContext(), BetweenRounds.class);
                                i.putExtra("uig_id", g_id);
                                startActivity(i);
                                finish();


                            }
                        }, 3000);
                    }
                    else
                    {
                        TextView txt_message = (TextView) findViewById(R.id.txt_message);
                        txt_message.setText("حریفی پیدا نشد");
                        shaked=false;
                    }
                    is_requested=false;
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
