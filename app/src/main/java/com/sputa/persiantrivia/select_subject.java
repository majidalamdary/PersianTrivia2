package com.sputa.persiantrivia;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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

public class select_subject extends AppCompatActivity {


    int screenWidth  = 0;
    int screenHeight = 0;



    SQLiteDatabase mydatabase;

    String
            subject1="";
    String
            subject2="";
    String
            subject3="";


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
    int      x=1;
    Timer tim_circulation;
    Boolean  is_rotation = true;
    String last_requested_query="";
    public ImageView img_circle1;
    Button msg_btn1,msg_btn2,msg_btn3;

    String
        g_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_subject);
        //lbl_msg = (TextView) findViewById(R.id.lbl_message);
        fun = new functions();
        font_name = fun.font_name;
        tf = Typeface.createFromAsset(getAssets(),font_name );
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        lay_wait = (RelativeLayout) findViewById(R.id.lay_wait);

        img_circle1 = (ImageView) findViewById(R.id.circle1);
        tim_circulation       = new Timer("circulation");
        tim_circulation.start();

        Intent i = getIntent();
        g_id = i.getStringExtra("g_id");

        get_subjects();

        set_content_size();
        if(MainActivity.music_playing)
            MainActivity.player.start();
    }


    @Override
    public void onPause()
    {
        super.onPause();

        MainActivity.player.pause();
    }
    @Override
    public void onResume(){
        super.onResume();
        if(MainActivity.music_playing)
            MainActivity.player.start();




    }
    private void get_subjects()
    {
        lay_wait.setVisibility(View.VISIBLE);
        x = 1;
        is_requested = true;
        mm = new MyAsyncTask();

        last_requested_query=getResources().getString(R.string.site_url) + "do.php?param=get_subject&uname="+fun.u_name+"&g_id="+g_id+"&rnd="+String.valueOf(new Random().nextInt());
        //  Toast.makeText(getBaseContext(),last_requested_query,Toast.LENGTH_LONG).show();
        mm.url = (last_requested_query);
        mm.execute("");
        LinearLayout lay_main = (LinearLayout) findViewById(R.id.lay_main);
        fun.enableDisableView(lay_main,false);
    }
    private void remove_message()
    {
        LinearLayout lay_main = (LinearLayout) findViewById(R.id.lay_main);

        fun.enableDisableView(lay_main, true);

        RelativeLayout lay_message = (RelativeLayout) findViewById(R.id.lay_message);
        lay_message.setVisibility(View.GONE);
    }

    boolean
            no_connection =false;
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
    private void set_content_size()
    {

//        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        TextView lbl_title = (TextView) findViewById(R.id.lbl_title);
        lbl_title.setTypeface(tf);
        lbl_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.092));

        LinearLayout laybtn_subject1 = (LinearLayout) findViewById(R.id.btn_subject1);
        //LinearLayout.LayoutParams lp_lay_tabBar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)(screenHeight*0.1));
        LinearLayout.LayoutParams lp_laybtn_subject1 = new LinearLayout.LayoutParams((int)(screenWidth*0.8),(int)(screenHeight*0.103));
        laybtn_subject1.setLayoutParams(lp_laybtn_subject1);


        LinearLayout laybtn_subject2 = (LinearLayout) findViewById(R.id.btn_subject2);
        laybtn_subject2.setLayoutParams(lp_laybtn_subject1);

        LinearLayout laybtn_subject3 = (LinearLayout) findViewById(R.id.btn_subject3);
        laybtn_subject3.setLayoutParams(lp_laybtn_subject1);

        TextView txt_btn_subject1 = (TextView) findViewById(R.id.txt_btn_subject1);
        txt_btn_subject1.setTypeface(tf);
        txt_btn_subject1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.062));

        TextView txt_btn_subject2 = (TextView) findViewById(R.id.txt_btn_subject2);
        txt_btn_subject2.setTypeface(tf);
        txt_btn_subject2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.062));

        TextView txt_btn_subject3 = (TextView) findViewById(R.id.txt_btn_subject3);
        txt_btn_subject3.setTypeface(tf);
        txt_btn_subject3.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.062));



    }
    private  void go_exam_board(int subj)
    {

        String
                subject="";
        if(subj==1)
        {
            subject=subject1;
        }
        if(subj==2)
        {
            subject=subject2;
        }
        if(subj==3)
        {
            subject=subject3;
        }
       // Toast.makeText(getBaseContext(),"ssss",Toast.LENGTH_SHORT).show();
        lay_wait.setVisibility(View.VISIBLE);
        x = 1;
        is_requested = true;
        mm = new MyAsyncTask();

        last_requested_query=getResources().getString(R.string.site_url) + "do.php?param=go_exam_board&uname="+fun.u_name+"&g_id="+g_id+"&subject="+subject+"&rnd="+String.valueOf(new Random().nextInt());
        //  Toast.makeText(getBaseContext(),last_requested_query,Toast.LENGTH_LONG).show();
        mm.url = (last_requested_query);
        mm.execute("");
        LinearLayout lay_main = (LinearLayout) findViewById(R.id.lay_main);
        fun.enableDisableView(lay_main,false);

    }
    public void clk_subject1(View view) {
        //   Toast.makeText(getBaseContext(),"3333",Toast.LENGTH_SHORT).show();
        go_exam_board(1);
    }

    public void clk_subject2(View view) {
        go_exam_board(2);
    }


    public void clk_subject3(View view) {
        go_exam_board(3);
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
                if ((param_str.equals("go_exam_board") ) && is_requested) {

                   // Toast.makeText(getBaseContext(),ss,Toast.LENGTH_SHORT).show();
                    start1 = ss.indexOf("<r_id>");
                    end1 = ss.indexOf("</r_id>");
                    String
                        r_id = ss.substring(start1 + 6, end1);
                    Intent i = new Intent(getBaseContext(),GameBoard.class);
                    i.putExtra("r_id",r_id);
                    finish();
                    startActivity(i);

                }
                if ((param_str.equals("get_subject") ) && is_requested) {




                    start1 = ss.indexOf("<subject1>");
                    end1 = ss.indexOf("</subject1>");
                    subject1 = ss.substring(start1 + 10, end1);
                    start1 = ss.indexOf("<subject2>");
                    end1 = ss.indexOf("</subject2>");
                    subject2 = ss.substring(start1 + 10, end1);
                    start1 = ss.indexOf("<subject3>");
                    end1 = ss.indexOf("</subject3>");
                    subject3 = ss.substring(start1 + 10, end1);

                    TextView txt_btn_subject1 = (TextView) findViewById(R.id.txt_btn_subject1);
                    TextView txt_btn_subject2 = (TextView) findViewById(R.id.txt_btn_subject2);
                    TextView txt_btn_subject3 = (TextView) findViewById(R.id.txt_btn_subject3);



                    txt_btn_subject1.setText(subject1);
                    txt_btn_subject2.setText(subject2);
                    txt_btn_subject3.setText(subject3);

                    //Toast.makeText(getBaseContext(),param_str,Toast.LENGTH_SHORT).show();
                    LinearLayout lay_main = (LinearLayout) findViewById(R.id.lay_main);
                    fun.enableDisableView(lay_main,true);
                    lay_wait.setVisibility(View.GONE);
                    is_requested = false;







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
                                    //TextView lbl_message = (TextView) findViewById(R.id.lbl_message);
                                    //  lbl_message.setText("ارتباط قطع شد");
                                    lay_wait.setVisibility(View.GONE);
                                    retry_msg();
                                    is_requested=false;
                                    no_connection = true;
                                    //                                  no_connection = true;
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


}
