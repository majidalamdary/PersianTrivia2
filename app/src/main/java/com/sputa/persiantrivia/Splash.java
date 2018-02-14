package com.sputa.persiantrivia;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Splash extends AppCompatActivity {

    int screenWidth  = 0;
    int screenHeight = 0;
    public ImageView img_circle1;
    public ImageView img_circle2;
    int x=1;
    Timer tim_circulation;
    Timer tim_check_internet;
    Timer tim_check_deadline;
    boolean check_Internet = false;
    boolean is_rotation = false;
    public MyAsyncTask mm;

    public TextView lbl_connection ;

    public boolean start_time_down_count = false;
    public int time_out = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        ImageView img_spash = (ImageView) findViewById(R.id.img_splash);
        RelativeLayout.LayoutParams lp_coin = new RelativeLayout.LayoutParams((int)(screenWidth*.7), (int)(screenWidth*.7));
        //lp_coin.setMargins(100,400,0,0);
        lp_coin.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        img_spash.setLayoutParams(lp_coin);

        img_circle1 = (ImageView) findViewById(R.id.circle1);
        lbl_connection = (TextView) findViewById(R.id.lbl_connection);
        is_rotation = true;

        tim_circulation   = new Timer("circulation");
        tim_check_deadline = new Timer("check_deadline");
        tim_check_internet = new Timer("check_internet");

        tim_circulation.start();
//        RelativeLayout.LayoutParams layoutParams =
//                (RelativeLayout.LayoutParams)img_spash.getLayoutParams();
//        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
//        img_spash.setLayoutParams(layoutParams);


    }
    public void clk_refresh(View v)
    {
        if(!is_rotation)
        {
            is_rotation = true;
            x=1;
            lbl_connection.setText("در حال اتصال به سرور...");
        }
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

                                if (x == 200) {
                                    check_Internet = true;
//                                    if(!tim_check_internet.isAlive())
//                                        tim_check_internet.start();
                                }
                            }
                        }
                        if(typ.equals("check_internet")) {
//                            if(check_Internet)
//                            {
//                                String
//                                        param="ping";
//                                String
//                                        uname ="";
//
//                                mm =  new MyAsyncTask();
//                                mm.url = (getResources().getString(R.string.site_url) + "general.php?param="+param);
//                                //lbl_connection.setText(mm.url);
//                                mm.execute("");
//                                check_Internet = false;
//                                start_time_down_count = true;
//                                time_out = 600;
//                                if(!tim_check_deadline.isAlive())
//                                    tim_check_deadline.start();
//                            }
                        }
                        if(typ.equals("check_deadline")) {
//                            if (start_time_down_count) {
//                                time_out--;
//                                if(time_out == 0)
//                                {
//                                    start_time_down_count = false;
//                                    lbl_connection.setText("خطا در اتصال");
//                                    is_rotation = false;
//                                }
//                            }
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

              //  lbl_connection.setText(param_str);
                if(param_str.equals("ping")) {
                    if(output_str.equals("ok"))
                    {
                        start_time_down_count = false;
                        lbl_connection.setText("ارتباط برقرار شد");

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
//                                Intent i = new Intent(Splash.this,Board.class);
//                                finish();
//                                startActivity(i);
                            }
                        }, 2000);

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
