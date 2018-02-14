package com.sputa.persiantrivia;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

public class New_question extends AppCompatActivity {
    functions fun;
    String
            majid="majid";
    String
            font_name = "";
    Typeface tf;
    int
        let_save=1;

    public LinearLayout lay_main;
    int screenWidth  = 0;
    int screenHeight = 0;
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

    Spinner dropdown;

    public String search_type ="";




    boolean
            no_connection =false;

    String
        q_id="106";

    String[] items ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question);


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




        dropdown = (Spinner)findViewById(R.id.spinner1);



        if(MainActivity.music_playing)
            MainActivity.player.start();

        Intent i =getIntent();
        q_id = i.getStringExtra("q_id");
        lay_wait.setVisibility(View.VISIBLE);
        fun.enableDisableView(lay_main, false);
        x = 1;
        is_requested = true;
        mm = new MyAsyncTask();
        last_requested_query = getResources().getString(R.string.site_url) + "do.php?param=get_designed_question&uname=" + functions.u_name+"&q_id="+q_id+"&rnd="+String.valueOf(new Random().nextInt()) ;
        //  Toast.makeText(getBaseContext(),last_requested_query,Toast.LENGTH_LONG).show();
        mm.url = (last_requested_query);
        mm.execute("");
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
    public String btn_typ="";
    public void clk_btn_msg1(View view) {

        Button btn_all = (Button) view;
        //   Toast.makeText(getBaseContext(),btn_all.getText().toString(),Toast.LENGTH_SHORT).show();

        if (btn_all.getText().toString().equals("اوکی"))
        {
            remove_message();
        }
        if (btn_all.getText().toString().equals("بله"))
        {
           // if(request_send)
            {
                remove_message();
                lay_wait.setVisibility(View.VISIBLE);
                fun.enableDisableView(lay_main,false);
                x = 1;
                is_requested = true;
                mm = new MyAsyncTask();
                //last_requested_query = getResources().getString(R.string.site_url) + "do.php?param=reply_request_game&uname=" + functions.u_name + "&rival_uname=" + rival_uname+"&typ=accepted";
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

    private void set_content()
    {



        TextView lbl_subject = (TextView) findViewById(R.id.lbl_subject);
        lbl_subject.setTypeface(tf);
        lbl_subject.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.065));


        TextView lbl_question = (TextView) findViewById(R.id.lbl_question);
        lbl_question.setTypeface(tf);
        lbl_question.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.065));


        TextView lbl_correct = (TextView) findViewById(R.id.lbl_correct);
        lbl_correct.setTypeface(tf);
        lbl_correct.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.045));

        TextView lbl_wrong1 = (TextView) findViewById(R.id.lbl_wrong1);
        lbl_wrong1.setTypeface(tf);
        lbl_wrong1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.045));


        TextView lbl_wrong2 = (TextView) findViewById(R.id.lbl_wrong2);
        lbl_wrong2.setTypeface(tf);
        lbl_wrong2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.045));


        TextView lbl_wrong3 = (TextView) findViewById(R.id.lbl_wrong3);
        lbl_wrong3.setTypeface(tf);
        lbl_wrong3.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.045));


        TextView lbl_save = (TextView) findViewById(R.id.lbl_save);
        lbl_save.setTypeface(tf);
        lbl_save.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.055));


        TextView lbl_new_question = (TextView) findViewById(R.id.lbl_new_question);
        lbl_new_question.setTypeface(tf);
        lbl_new_question.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.055));


        TextView lbl_return = (TextView) findViewById(R.id.lbl_return);
        lbl_return.setTypeface(tf);
        lbl_return.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.055));


        EditText txt_question = (EditText) findViewById(R.id.txt_question);
        txt_question.setTypeface(tf);
        txt_question.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.055));


        EditText txt_correct = (EditText) findViewById(R.id.txt_correct);
        txt_correct.setTypeface(tf);
        txt_correct.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.055));


        EditText txt_wrong1 = (EditText) findViewById(R.id.txt_wrong1);
        txt_wrong1.setTypeface(tf);
        txt_wrong1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.055));


        EditText txt_wrong2 = (EditText) findViewById(R.id.txt_wrong2);
        txt_wrong2.setTypeface(tf);
        txt_wrong2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.055));


        EditText txt_wrong3 = (EditText) findViewById(R.id.txt_wrong3);
        txt_wrong3.setTypeface(tf);
        txt_wrong3.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.055));




    }

    public void clk_save(View view) {
        if(let_save==1) {
            TextView txt_question = (TextView) findViewById(R.id.txt_question);
            TextView txt_correct = (TextView) findViewById(R.id.txt_correct);
            TextView txt_wrong1 = (TextView) findViewById(R.id.txt_wrong1);
            TextView txt_wrong2 = (TextView) findViewById(R.id.txt_wrong2);
            TextView txt_wrong3 = (TextView) findViewById(R.id.txt_wrong3);


            String
                    str_question = txt_question.getText().toString();


            String
                    str_correct = txt_correct.getText().toString();
            String
                    str_wrong1 = txt_wrong1.getText().toString();
            String
                    str_wrong2 = txt_wrong2.getText().toString();
            String
                    str_wrong3 = txt_wrong3.getText().toString();

            // Toast.makeText(New_question.this, String.valueOf(str_question.length()), Toast.LENGTH_SHORT).show();


            Boolean
                    flag = false;

            if (str_question.length() > 100) {
                flag = true;
                Toast.makeText(New_question.this, "طول روی سوال خیلی زیاد است نباید بیشتر از 100 کاراکتر باشد", Toast.LENGTH_SHORT).show();
            }
            if (str_correct.length() > 30) {
                flag = true;
                Toast.makeText(New_question.this, "طول پاسخ صحیح خیلی زیاد است نباید بیشتر از 30 کاراکتر باشد", Toast.LENGTH_SHORT).show();
            }
            if (str_wrong1.length() > 30) {
                flag = true;
                Toast.makeText(New_question.this, "طول پاسخ غلط 1 خیلی زیاد است نباید بیشتر از 30 کاراکتر باشد", Toast.LENGTH_SHORT).show();
            }
            if (str_wrong2.length() > 30) {
                flag = true;
                Toast.makeText(New_question.this, "طول پاسخ غلط 2 خیلی زیاد است نباید بیشتر از 30 کاراکتر باشد", Toast.LENGTH_SHORT).show();
            }
            if (str_wrong3.length() > 30) {
                flag = true;
                Toast.makeText(New_question.this, "طول پاسخ غلط 3 خیلی زیاد است نباید بیشتر از 30 کاراکتر باشد", Toast.LENGTH_SHORT).show();
            }


            if (str_question.length() == 0) {
                flag = true;
                Toast.makeText(New_question.this, "متن سوال خالی است", Toast.LENGTH_SHORT).show();
            }
            if (str_correct.length() == 0) {
                flag = true;
                Toast.makeText(New_question.this, "پاسخ صحیح خالی است", Toast.LENGTH_SHORT).show();
            }
            if (str_wrong1.length() == 0) {
                flag = true;
                Toast.makeText(New_question.this, "پاسخ غلط 1 خالی است", Toast.LENGTH_SHORT).show();
            }
            if (str_wrong2.length() == 0) {
                flag = true;
                Toast.makeText(New_question.this, "پاسخ غلط 2 خالی است", Toast.LENGTH_SHORT).show();
            }
            if (str_wrong3.length() == 0) {
                flag = true;
                Toast.makeText(New_question.this, "پاسخ غلط 3 خالی است", Toast.LENGTH_SHORT).show();
            }

            if (!flag) {
                //if(q_id.equals("0"))
                {
                    lay_wait.setVisibility(View.VISIBLE);
                    fun.enableDisableView(lay_main, false);
                    x = 1;
                    is_requested = true;
                    mm = new MyAsyncTask();
                    String str_subject = dropdown.getSelectedItem().toString();
                    last_requested_query = getResources().getString(R.string.site_url) + "do.php?param=save_question&uname=" + functions.u_name + "&q_id=" + q_id + "&subject=" + Uri.encode(str_subject) + "&question=" + Uri.encode(str_question) + "&correct=" + Uri.encode(str_correct) + "&wrong1=" + Uri.encode(str_wrong1) + "&wrong2=" + Uri.encode(str_wrong2) + "&wrong3=" + Uri.encode(str_wrong3)+"&rnd="+String.valueOf(new Random().nextInt());
                    //  Toast.makeText(getBaseContext(),last_requested_query,Toast.LENGTH_LONG).show();
                    TextView txt_question1 = (TextView) findViewById(R.id.txt_question);
                    // txt_question1.setText(last_requested_query);
                    mm.url = (last_requested_query);
                    mm.execute("");
                }

            }
        }
        else
        {
            Toast.makeText(New_question.this, "این سوال را نمی توانید ویرایش کنید", Toast.LENGTH_SHORT).show();
        }
    }

    public void clk_new_question(View view) {

        TextView txt_question = (TextView) findViewById(R.id.txt_question);
        TextView txt_correct = (TextView) findViewById(R.id.txt_correct);
        TextView txt_wrong1 = (TextView) findViewById(R.id.txt_wrong1);
        TextView txt_wrong2 = (TextView) findViewById(R.id.txt_wrong2);
        TextView txt_wrong3 = (TextView) findViewById(R.id.txt_wrong3);

        txt_question.setText("");
        txt_correct.setText("");
        txt_wrong1.setText("");
        txt_wrong2.setText("");
        txt_wrong3.setText("");
        q_id="0";
    }

    public void clk_return(View view) {
        finish();
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
//                Toast.makeText(getBaseContext(),ss,Toast.LENGTH_LONG).show();
//                Toast.makeText(getBaseContext(),param_str,Toast.LENGTH_LONG).show();
                if ((param_str.equals("get_designed_question")))
                {
                    //   Toast.makeText(getBaseContext(),ss,Toast.LENGTH_LONG).show();
                    lay_wait.setVisibility(View.GONE);
                    fun.enableDisableView(lay_main,true);
                    is_requested = false;
                    //Toast.makeText(New_question.this, ss, Toast.LENGTH_SHORT).show();
                    String[] items_tmp = new String[20];
                    int
                        sub_count=0;
                    String
                            subs="";
                    for(int i=0;i<output_str.length()-1;i++)
                    {
                        if(output_str.charAt(i)!='&')
                        {
                            subs=subs+output_str.charAt(i);
                        }
                        else
                        {
                            sub_count++;
                            items_tmp[sub_count]=subs;
                            subs="";
                        }
                    }

                    String[] items = new String[sub_count-1];
                    for(int i=1;i<sub_count;i++) {
                        items[i-1]=items_tmp[i];
                    }


                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(New_question.this, android.R.layout.simple_spinner_dropdown_item, items);
                    dropdown.setAdapter(adapter);
                    if(!q_id.equals("0")) {
                        TextView txt_question = (TextView) findViewById(R.id.txt_question);
                        TextView txt_correct = (TextView) findViewById(R.id.txt_correct);
                        TextView txt_wrong1 = (TextView) findViewById(R.id.txt_wrong1);
                        TextView txt_wrong2 = (TextView) findViewById(R.id.txt_wrong2);
                        TextView txt_wrong3 = (TextView) findViewById(R.id.txt_wrong3);

                        start1 = ss.indexOf("<question>");
                        end1 = ss.indexOf("</question>");
                        String
                                question = ss.substring(start1 + 10, end1);
                        start1 = ss.indexOf("<status>");
                        end1 = ss.indexOf("</status>");
                        String
                                status = ss.substring(start1 + 8, end1);

                        txt_question.setText(question);
                        start1 = ss.indexOf("<correct>");
                        end1 = ss.indexOf("</correct>");
                        String
                                correct = ss.substring(start1 + 9, end1);
                        txt_correct.setText(correct);

                        start1 = ss.indexOf("<wrong1>");
                        end1 = ss.indexOf("</wrong1>");
                        String
                                wrong1 = ss.substring(start1 + 8, end1);
                        txt_wrong1.setText(wrong1);

                        start1 = ss.indexOf("<wrong2>");
                        end1 = ss.indexOf("</wrong2>");
                        String
                                wrong2 = ss.substring(start1 + 8, end1);
                        txt_wrong2.setText(wrong2);

                        start1 = ss.indexOf("<wrong3>");
                        end1 = ss.indexOf("</wrong3>");
                        String
                                wrong3 = ss.substring(start1 + 8, end1);
                        start1 = ss.indexOf("<subject>");
                        end1 = ss.indexOf("</subject>");
                        String
                                subject = ss.substring(start1 + 9, end1);

                        txt_wrong3.setText(wrong3);
                        int pos = 0;
                        for (int i = 0; i < sub_count - 1; i++) {
                            if (items[i].equals(subject)) {
                                pos = i;
                            }
                        }
                        dropdown.setSelection(pos);
                        RelativeLayout lay_save= (RelativeLayout) findViewById(R.id.lay_save);
                        if(!status.equals("unconfirmed")) {
                            let_save=0;
                            lay_save.setBackground(getResources().getDrawable(R.drawable.light_grey_btn));
                        }

                    }

                }
                if ((param_str.equals("save_question")))
                {
                    lay_wait.setVisibility(View.GONE);
                    fun.enableDisableView(lay_main,true);
                    is_requested = false;
                    //Toast.makeText(New_question.this, ss, Toast.LENGTH_SHORT).show();
                    start1 = ss.indexOf("<result>");
                    end1 = ss.indexOf("</result>");
                    String
                            res = ss.substring(start1 + 8, end1);
                    if(res.length()>0)
                    {
                        q_id=res;
                        Toast.makeText(New_question.this, "سوال با موفقیت ذخیره شد", Toast.LENGTH_SHORT).show();
                    }
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
                                    TextView lbl_message = (TextView) findViewById(R.id.lbl_message);
                                    //  lbl_message.setText("ارتباط قطع شد");
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


}
