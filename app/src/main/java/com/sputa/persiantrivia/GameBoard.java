package com.sputa.persiantrivia;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class GameBoard extends AppCompatActivity {
    int screenWidth  = 0;
    int screenHeight = 0;

    functions fun;
    String
            font_name = "";
    Typeface tf;
    Timer tim;
    String
            last_requested_query="";
    int
            already_answered = 0 , second_chance = 0, already_two_removed = 0, already_get_extra_time = 0, already_get_people = 0;
    private TextView textView,txt_question,txt_question1,txt_ans1,txt_ans2,txt_ans3,txt_ans4,txt_ans1_others,txt_ans2_others,txt_ans3_others,txt_ans4_others;
    String[]  question = new String[10];

    String[]  ans1 = new String[10];
    String[]  ans2 = new String[10];
    String[]  ans3 = new String[10];
    String[]  ans4 = new String[10];

    String[]  ans1_others = new String[10];
    String[]  ans2_others = new String[10];
    String[]  ans3_others = new String[10];
    String[]  ans4_others = new String[10];

    int[]  right_answer     = new int[10];
    int[]  selected_answer  = new int[10];
    int[]  opinion_question  = new int[10];
    int    right_answered=0;
    String r_id="6";
    public ImageView img_circle1;
    public ImageView img_circle2;
    int x=1;
    Timer tim_circulation;
    Timer tim_check_internet;
    Timer tim_check_deadline;
    Timer tim_next_question;

    Timer tim_question_deadline;

    boolean check_Internet = false;
    boolean is_requested = false;

    boolean is_rotation = false;
    public MyAsyncTask mm;
    public MyAsyncTask mm1;
    public MyAsyncTask mm2;
    public MyAsyncTask mm3;
    public MyAsyncTask mm4;

    public TextView lbl_connection ;

    public boolean start_time_down_count = false;
    public int time_out = 200;
    public RelativeLayout lay_wait ;
    float
        question_deadline = 10;
    float
        progress_bar = 1;

    int question_num =1;

    int coin_count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fun = new functions();
        setContentView(R.layout.activity_game_board);
        lay_wait = (RelativeLayout) findViewById(R.id.lay_wait);
        lay_wait.setVisibility(View.VISIBLE);

        Intent i=getIntent();
        r_id = i.getStringExtra("r_id");


        mm =  new MyAsyncTask();
        {
            String
                    param="get_question";
            String
                    uname ="";
            is_requested=true;
            mm.url = (getResources().getString(R.string.site_url) + "get_question.php?param=" + param + "&uname=" + functions.u_name+"&r_id="+r_id+"&rnd="+String.valueOf(new Random().nextInt()));
            //Toast.makeText(GameBoard.this, mm.url, Toast.LENGTH_LONG).show();
            last_requested_query=mm.url;
            mm.execute("");
        }



        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        LinearLayout lay_between = (LinearLayout) findViewById(R.id.lay_between);
        lay_between.setVisibility(View.INVISIBLE);

        font_name = fun.font_name;
        tf = Typeface.createFromAsset(getAssets(),font_name );


        img_circle1 = (ImageView) findViewById(R.id.circle1);
        lbl_connection = (TextView) findViewById(R.id.lbl_connection);
        is_rotation = true;

        tim_circulation       = new Timer("circulation");
        tim_check_deadline    = new Timer("check_deadline");
        tim_check_internet    = new Timer("check_internet");
        tim_question_deadline = new Timer("question_deadline");



        txt_question = (TextView) findViewById(R.id.txt_question);
        txt_question1 = (TextView) findViewById(R.id.txt_question1);

        txt_ans1 = (TextView) findViewById(R.id.txt_ans1);
        txt_ans2 = (TextView) findViewById(R.id.txt_ans2);
        txt_ans3 = (TextView) findViewById(R.id.txt_ans3);
        txt_ans4 = (TextView) findViewById(R.id.txt_ans4);

        txt_ans1_others = (TextView) findViewById(R.id.txt_others1);
        txt_ans2_others = (TextView) findViewById(R.id.txt_others2);
        txt_ans3_others = (TextView) findViewById(R.id.txt_others3);
        txt_ans4_others = (TextView) findViewById(R.id.txt_others4);

        tim_circulation.start();
        set_content_size();
        set_message_content_size();
        //show_msg("سلام","حال شما چطور است؟","بلی&خیر");

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


    @Override
    public void onBackPressed() {
    }
    private  void set_message_content_size()
    {



//        LinearLayout lay_msg_btn_container = (LinearLayout) findViewById(R.id.lay_inner_message);
//        //LinearLayout.LayoutParams lp_lay_tabBar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)(screenHeight*0.1));
//        RelativeLayout.LayoutParams lp_lay_msg_btn_container = new RelativeLayout.LayoutParams((int)(screenWidth*.8),RelativeLayout.LayoutParams.WRAP_CONTENT);
//        lay_msg_btn_container.setLayoutParams(lp_lay_msg_btn_container);

        TextView txt_msg_title = (TextView) findViewById(R.id.txt_msg_title);
        txt_msg_title.setTypeface(tf);
        txt_msg_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.072));

        TextView txt_msg_text = (TextView) findViewById(R.id.txt_msg_text);
        txt_msg_text.setTypeface(tf);
        txt_msg_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.055));

        Button btn_msg1 = (Button) findViewById(R.id.btn_msg1);
        btn_msg1.setTypeface(tf);
        btn_msg1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.045));

        Button btn_msg2 = (Button) findViewById(R.id.btn_msg2);
        btn_msg2.setTypeface(tf);
        btn_msg2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.045));

        Button btn_msg3 = (Button) findViewById(R.id.btn_msg3);
        btn_msg3.setTypeface(tf);
        btn_msg3.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.045));






    }

    private void show_msg(String msg_title,String msg_text,String msg_buttons)
    {

        LinearLayout lay_main = (LinearLayout) findViewById(R.id.lay_main);

        enableDisableView(lay_main,false);

        LinearLayout lay_between = (LinearLayout) findViewById(R.id.lay_between);

        enableDisableView(lay_between,false);


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
        btn_msg1.setText("");
        btn_msg2.setText("");
        btn_msg3.setText("");

        int
                btn_count=1;
        for(int i=0;i<msg_buttons.length();i++)
        {
            if(msg_buttons.charAt(i) !='&')
            {
                if(btn_count==1)
                {
                    btn_msg1.setText(btn_msg1.getText()+String.valueOf(msg_buttons.charAt(i)));
                }
                if(btn_count==2)
                {
                    btn_msg2.setText(btn_msg2.getText()+String.valueOf(msg_buttons.charAt(i)));
                }
                if(btn_count==3)
                {
                    btn_msg3.setText(btn_msg3.getText()+String.valueOf(msg_buttons.charAt(i)));
                }
            }
            else{

                btn_count++;
            }
        }
        if(btn_count==1)
        {
            btn_msg2.setVisibility(View.GONE);
            btn_msg3.setVisibility(View.GONE);
        }
        if(btn_count==2)
        {
            btn_msg3.setVisibility(View.GONE);
        }



    }
    public static void enableDisableView(View view, boolean enabled) {
        view.setEnabled(enabled);
        if ( view instanceof ViewGroup ) {
            ViewGroup group = (ViewGroup)view;

            for ( int idx = 0 ; idx < group.getChildCount() ; idx++ ) {
                enableDisableView(group.getChildAt(idx), enabled);
            }
        }
    }
    private void set_content_size()
    {





        TextView txt_opponent_name = (TextView) findViewById(R.id.txt_opponent_name);
        txt_opponent_name.setTypeface(tf);
        txt_opponent_name.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.042));

        TextView txt_your_name = (TextView) findViewById(R.id.txt_your_name);
        txt_your_name.setTypeface(tf);
        txt_your_name.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.042));

        TextView txt_subject= (TextView) findViewById(R.id.txt_subject);
        txt_subject.setTypeface(tf);
        txt_subject.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.039));

        TextView txt_opponent_name1 = (TextView) findViewById(R.id.txt_opponent_name1);
        txt_opponent_name1.setTypeface(tf);
        txt_opponent_name1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.042));

        TextView txt_your_name1 = (TextView) findViewById(R.id.txt_your_name1);
        txt_your_name1.setTypeface(tf);
        txt_your_name1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.042));

        TextView txt_subject1= (TextView) findViewById(R.id.txt_subject1);
        txt_subject1.setTypeface(tf);
        txt_subject1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.039));

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        LinearLayout lay_speaker= (LinearLayout) findViewById(R.id.lay_speaker);
        //LinearLayout.LayoutParams lp_lay_tabBar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)(screenHeight*0.1));
        LinearLayout.LayoutParams lp_lay_speaker = new LinearLayout.LayoutParams((int)(screenHeight*0.15),(int)(screenHeight*0.042));
        lay_speaker.setLayoutParams(lp_lay_speaker);

        LinearLayout lay_coin= (LinearLayout) findViewById(R.id.lay_coin);
        //LinearLayout.LayoutParams lp_lay_tabBar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)(screenHeight*0.1));
        LinearLayout.LayoutParams lp_lay_coin = new LinearLayout.LayoutParams((int)(screenHeight*0.15),(int)(screenHeight*0.042));
        lay_coin.setLayoutParams(lp_lay_coin);




        TextView txt_coin_count = (TextView) findViewById(R.id.txt_coin_count);
        txt_coin_count.setTypeface(tf);
        txt_coin_count.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.042));

        TextView txt_mute = (TextView) findViewById(R.id.txt_mute);
        txt_mute.setTypeface(tf);
        txt_mute.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.042));

        TextView txt_question_num = (TextView) findViewById(R.id.txt_question_num);
        txt_question_num.setTypeface(tf);
        txt_question_num.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.059));

        TextView txt_question_num1 = (TextView) findViewById(R.id.txt_question_num1);
        txt_question_num1.setTypeface(tf);
        txt_question_num1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.059));


        TextView lbl_question_num = (TextView) findViewById(R.id.lbl_question_num);
        lbl_question_num.setTypeface(tf);
        lbl_question_num.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.059));

        TextView lbl_question_num1 = (TextView) findViewById(R.id.lbl_question_num1);
        lbl_question_num1.setTypeface(tf);
        lbl_question_num1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.059));




//
//        TextView txt_point = (TextView) findViewById(R.id.txt_point);
//        txt_point.setTypeface(tf);
//        txt_point.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.059));
//



        LinearLayout lay_speaker1= (LinearLayout) findViewById(R.id.lay_speaker1);
        //LinearLayout.LayoutParams lp_lay_tabBar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)(screenHeight*0.1));
        LinearLayout.LayoutParams lp_lay_speaker1 = new LinearLayout.LayoutParams((int)(screenHeight*0.15),(int)(screenHeight*0.042));
        lay_speaker1.setLayoutParams(lp_lay_speaker1);

        LinearLayout lay_coin1= (LinearLayout) findViewById(R.id.lay_coin1);
        //LinearLayout.LayoutParams lp_lay_tabBar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)(screenHeight*0.1));
        LinearLayout.LayoutParams lp_lay_coin1 = new LinearLayout.LayoutParams((int)(screenHeight*0.15),(int)(screenHeight*0.042));
        lay_coin1.setLayoutParams(lp_lay_coin1);

        TextView txt_coin_count1 = (TextView) findViewById(R.id.txt_coin_count1);
        txt_coin_count1.setTypeface(tf);
        txt_coin_count1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.042));

        TextView txt_mute1 = (TextView) findViewById(R.id.txt_mute1);
        txt_mute1.setTypeface(tf);
        txt_mute1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.042));

//        TextView txt_point1 = (TextView) findViewById(R.id.txt_point1);
//        txt_point1.setTypeface(tf);
//        txt_point1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.059));

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        TextView txt_question = (TextView) findViewById(R.id.txt_question);
        txt_question.setTypeface(tf);
        txt_question.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));


        TextView txt_question1 = (TextView) findViewById(R.id.txt_question1);
        txt_question1.setTypeface(tf);
        txt_question1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




        TextView txt_ans1 = (TextView) findViewById(R.id.txt_ans1);
        txt_ans1.setTypeface(tf);
        txt_ans1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));

        TextView txt_ans2 = (TextView) findViewById(R.id.txt_ans2);
        txt_ans2.setTypeface(tf);
        txt_ans2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));


        TextView txt_ans3 = (TextView) findViewById(R.id.txt_ans3);
        txt_ans3.setTypeface(tf);
        txt_ans3.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));

        TextView txt_ans4 = (TextView) findViewById(R.id.txt_ans4);
        txt_ans4.setTypeface(tf);
        txt_ans4.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));



        TextView txt_others1 = (TextView) findViewById(R.id.txt_others1);
        txt_others1.setTypeface(tf);
        txt_others1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.032));


        TextView txt_others2 = (TextView) findViewById(R.id.txt_others2);
        txt_others2.setTypeface(tf);
        txt_others2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.032));


        TextView txt_others3 = (TextView) findViewById(R.id.txt_others3);
        txt_others3.setTypeface(tf);
        txt_others3.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.032));


        TextView txt_others4 = (TextView) findViewById(R.id.txt_others4);
        txt_others4.setTypeface(tf);
        txt_others4.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.032));


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




        int
                img_coin_width_height = (int)(screenHeight*.045);


        LinearLayout.LayoutParams lp_img_coin = new LinearLayout.LayoutParams(img_coin_width_height,img_coin_width_height);

        ImageView img_coin_people = (ImageView) findViewById(R.id.img_coin_people);
        img_coin_people.setLayoutParams(lp_img_coin);
        ImageView img_coin_remove = (ImageView) findViewById(R.id.img_coin_remove);
        img_coin_remove.setLayoutParams(lp_img_coin);
        ImageView img_coin_extra_time = (ImageView) findViewById(R.id.img_coin_extra_time);
        img_coin_extra_time.setLayoutParams(lp_img_coin);
        ImageView img_coin_second_cjance = (ImageView) findViewById(R.id.img_coin_second_cjance);
        img_coin_second_cjance.setLayoutParams(lp_img_coin);



        TextView txt_remove_cost = (TextView) findViewById(R.id.txt_remove_cost);
        txt_remove_cost.setTypeface(tf);
        txt_remove_cost.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.039));

        TextView txt_people_cost = (TextView) findViewById(R.id.txt_people_cost);
        txt_people_cost.setTypeface(tf);
        txt_people_cost.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.039));

        TextView txt_extra_timr_cost = (TextView) findViewById(R.id.txt_extra_timr_cost);
        txt_extra_timr_cost.setTypeface(tf);
        txt_extra_timr_cost.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.039));

        TextView txt_second_chance_cost = (TextView) findViewById(R.id.txt_second_chance_cost);
        txt_second_chance_cost.setTypeface(tf);
        txt_second_chance_cost.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.039));


        LinearLayout.LayoutParams lp_img_people = new LinearLayout.LayoutParams((int) (screenWidth * 0.079),(int) (screenWidth * 0.079));
        ImageView img_people = (ImageView) findViewById(R.id.img_people);
        img_people.setLayoutParams(lp_img_people);

        LinearLayout.LayoutParams lp_img_bomb = new LinearLayout.LayoutParams((int) (screenWidth * 0.079),(int) (screenWidth * 0.079));
        ImageView img_bomb = (ImageView) findViewById(R.id.img_bomb);
        img_bomb.setLayoutParams(lp_img_bomb);


        LinearLayout.LayoutParams lp_img_extra_time = new LinearLayout.LayoutParams((int) (screenWidth * 0.079),(int) (screenWidth * 0.079));
        ImageView img_extra_time = (ImageView) findViewById(R.id.img_extra_time);
        img_extra_time.setLayoutParams(lp_img_extra_time);


        LinearLayout.LayoutParams lp_img_second_chance = new LinearLayout.LayoutParams((int) (screenWidth * 0.079),(int) (screenWidth * 0.079));
        ImageView img_second_chance = (ImageView) findViewById(R.id.img_second_chance);
        img_second_chance.setLayoutParams(lp_img_second_chance);



        TextView txt_people = (TextView) findViewById(R.id.txt_people);
        txt_people.setTypeface(tf);
        txt_people.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.041));

        TextView txt_remove = (TextView) findViewById(R.id.txt_remove);
        txt_remove.setTypeface(tf);
        txt_remove.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.039));

        TextView txt_extra_time = (TextView) findViewById(R.id.txt_extra_time);
        txt_extra_time.setTypeface(tf);
        txt_extra_time.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.041));

        TextView txt_second_chance = (TextView) findViewById(R.id.txt_second_chance);
        txt_second_chance.setTypeface(tf);
        txt_second_chance.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.041));


        TextView txt_make_angry = (TextView) findViewById(R.id.txt_make_angry);
        txt_make_angry.setTypeface(tf);
        txt_make_angry.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.049));

        TextView txt_send_laughing_image = (TextView) findViewById(R.id.txt_send_laughing_image);
        txt_send_laughing_image.setTypeface(tf);
        txt_send_laughing_image.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.046));


        TextView txt_send_laughing_sound = (TextView) findViewById(R.id.txt_send_laughing_sound);
        txt_send_laughing_sound.setTypeface(tf);
        txt_send_laughing_sound.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.046));


        TextView txt_dislike = (TextView) findViewById(R.id.txt_dislike);
        txt_dislike.setTypeface(tf);
        txt_dislike.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.046));

        TextView txt_like = (TextView) findViewById(R.id.txt_like);
        txt_like.setTypeface(tf);
        txt_like.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.046));

        TextView txt_continue = (TextView) findViewById(R.id.txt_continue);
        txt_continue.setTypeface(tf);
        txt_continue.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.046));





    }



    public void clk_mute(View v)
    {

        if(MainActivity.music_playing) {
            TextView tv = (TextView) findViewById(R.id.txt_mute);
            tv.setText("پخش ");
            TextView tv1 = (TextView) findViewById(R.id.txt_mute1);
            tv1.setText("پخش ");
            MainActivity.music_playing=false;
            MainActivity.player.pause();
        }
        else
        {
            MainActivity.music_playing=true;
            TextView tv = (TextView) findViewById(R.id.txt_mute);
            tv.setText("بی صدا");
            TextView tv1 = (TextView) findViewById(R.id.txt_mute1);
            tv1.setText("بی صدا");
            MainActivity.player.start();
        }



    }

    private void check_answer(int answer, LinearLayout lay)
    {
        //Toast.makeText(getBaseContext(),String.valueOf(second_chance),Toast.LENGTH_SHORT).show();
        if(already_answered == 0)
        {
            if(answer == right_answer[question_num])
            {
                lay.setBackground(getResources().getDrawable(R.drawable.green_btn));
               // lay.setBackgroundColor(Color.parseColor("green"));
                right_answered++;
                already_answered=1;
                if(answer!=0)
                    selected_answer[question_num]=answer;
            }
            else
            {
                if(second_chance != 1 || answer == 0)
                {
                    if(answer != 0)
                        lay.setBackground(getResources().getDrawable(R.drawable.red_btn));
                    LinearLayout lay1 = (LinearLayout) findViewById(R.id.lay_ans1);
                    LinearLayout lay2 = (LinearLayout) findViewById(R.id.lay_ans2);
                    LinearLayout lay3 = (LinearLayout) findViewById(R.id.lay_ans3);
                    LinearLayout lay4 = (LinearLayout) findViewById(R.id.lay_ans4);
                    if(right_answer[question_num] == 1) {
                        lay1.setBackground(getResources().getDrawable(R.drawable.green_btn));

                    }
                    if(right_answer[question_num] == 2)
                        lay2.setBackground(getResources().getDrawable(R.drawable.green_btn));
                    if(right_answer[question_num] == 3)
                        lay3.setBackground(getResources().getDrawable(R.drawable.green_btn));
                    if(right_answer[question_num] == 4)
                        lay4.setBackground(getResources().getDrawable(R.drawable.green_btn));
                    already_answered=1;
                    if(answer!=0)
                        selected_answer[question_num]=answer;
                }
                else
                {
                    if(answer != 0)
                        lay.setBackground(getResources().getDrawable(R.drawable.black_btn));
                    second_chance = 3;

                }



            }
            if(already_answered==1) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        // next_question();
                        LinearLayout lay_between = (LinearLayout) findViewById(R.id.lay_between);
                        lay_between.setVisibility(View.VISIBLE);
                        LinearLayout lay_main = (LinearLayout) findViewById(R.id.lay_main);
                        enableDisableView(lay_main, false);

                    }
                }, 1000);
            }
        }
    }
    public void next_question()
    {

        String
                param="save_game";
        String
                uname ="";
        if(question_num==1) {
            mm1 =  new MyAsyncTask();
            mm1.url = (getResources().getString(R.string.site_url) + "save_game.php?param=" + param + "&uname=" + functions.u_name + "&r_id=" + r_id + "&q1=" + String.valueOf(selected_answer[1]) + "&q1_opinion=" + String.valueOf(opinion_question[1]) + "&q2=" + String.valueOf(selected_answer[2]) + "&q2_opinion=" + String.valueOf(opinion_question[2]) + "&q3=" + String.valueOf(selected_answer[3]) + "&q3_opinion=" + String.valueOf(opinion_question[3]) + "&q4=" + String.valueOf(selected_answer[4]) + "&q4_opinion=" + String.valueOf(opinion_question[4]) + "&coin_count=" + String.valueOf(coin_count) + "&question_number=" + String.valueOf(question_num) + "&right_answered=" + String.valueOf(right_answered)+"&rnd="+String.valueOf(new Random().nextInt()));
            mm1.execute("");
        }
        if(question_num==2) {
            mm2 =  new MyAsyncTask();
            mm2.url = (getResources().getString(R.string.site_url) + "save_game.php?param=" + param + "&uname=" + functions.u_name + "&r_id=" + r_id + "&q1=" + String.valueOf(selected_answer[1]) + "&q1_opinion=" + String.valueOf(opinion_question[1]) + "&q2=" + String.valueOf(selected_answer[2]) + "&q2_opinion=" + String.valueOf(opinion_question[2]) + "&q3=" + String.valueOf(selected_answer[3]) + "&q3_opinion=" + String.valueOf(opinion_question[3]) + "&q4=" + String.valueOf(selected_answer[4]) + "&q4_opinion=" + String.valueOf(opinion_question[4]) + "&coin_count=" + String.valueOf(coin_count) + "&question_number=" + String.valueOf(question_num) + "&right_answered=" + String.valueOf(right_answered)+"&rnd="+String.valueOf(new Random().nextInt()));
            mm2.execute("");
        }
        if(question_num==3) {
            mm3 =  new MyAsyncTask();
            mm3.url = (getResources().getString(R.string.site_url) + "save_game.php?param=" + param + "&uname=" + functions.u_name + "&r_id=" + r_id + "&q1=" + String.valueOf(selected_answer[1]) + "&q1_opinion=" + String.valueOf(opinion_question[1]) + "&q2=" + String.valueOf(selected_answer[2]) + "&q2_opinion=" + String.valueOf(opinion_question[2]) + "&q3=" + String.valueOf(selected_answer[3]) + "&q3_opinion=" + String.valueOf(opinion_question[3]) + "&q4=" + String.valueOf(selected_answer[4]) + "&q4_opinion=" + String.valueOf(opinion_question[4]) + "&coin_count=" + String.valueOf(coin_count) + "&question_number=" + String.valueOf(question_num) + "&right_answered=" + String.valueOf(right_answered)+"&rnd="+String.valueOf(new Random().nextInt()));
            mm3.execute("");
        }
        if(question_num==4) {

            LinearLayout lay_main = (LinearLayout) findViewById(R.id.lay_main);
            LinearLayout lay_mbetween = (LinearLayout) findViewById(R.id.lay_between);

            fun.enableDisableView(lay_main,false);
            fun.enableDisableView(lay_mbetween,false);

            lay_wait.setVisibility(View.VISIBLE);
            x = 1;
            is_requested = true;




            mm4 =  new MyAsyncTask();
            mm4.url = (getResources().getString(R.string.site_url) + "save_game.php?param=" + param + "&uname=" + functions.u_name + "&r_id=" + r_id + "&q1=" + String.valueOf(selected_answer[1]) + "&q1_opinion=" + String.valueOf(opinion_question[1]) + "&q2=" + String.valueOf(selected_answer[2]) + "&q2_opinion=" + String.valueOf(opinion_question[2]) + "&q3=" + String.valueOf(selected_answer[3]) + "&q3_opinion=" + String.valueOf(opinion_question[3]) + "&q4=" + String.valueOf(selected_answer[4]) + "&q4_opinion=" + String.valueOf(opinion_question[4]) + "&coin_count=" + String.valueOf(coin_count) + "&question_number=" + String.valueOf(question_num) + "&right_answered=" + String.valueOf(right_answered)+"&rnd="+String.valueOf(new Random().nextInt()));
            last_requested_query=mm4.url;
            mm4.execute("");




        }
        txt_ans1_others.setVisibility(View.GONE);
        txt_ans2_others.setVisibility(View.GONE);
        txt_ans3_others.setVisibility(View.GONE);
        txt_ans4_others.setVisibility(View.GONE);



        if(question_num<functions.question_count) {
//            LinearLayout lay_second_chance_cover = (LinearLayout) findViewById(R.id.lay_second_chance_cover);
//            lay_second_chance_cover.setVisibility(View.GONE);
//            LinearLayout lay_remove_two = (LinearLayout) findViewById(R.id.lay_remove_cover);
//            lay_remove_two.setVisibility(View.GONE);
//
//            LinearLayout lay_extra_time = (LinearLayout) findViewById(R.id.lay_extra_time_cover);
//            lay_extra_time.setVisibility(View.GONE);

            LinearLayout lay1 = (LinearLayout) findViewById(R.id.lay_ans1);
            LinearLayout lay2 = (LinearLayout) findViewById(R.id.lay_ans2);
            LinearLayout lay3 = (LinearLayout) findViewById(R.id.lay_ans3);
            LinearLayout lay4 = (LinearLayout) findViewById(R.id.lay_ans4);
            LinearLayout lay1_father = (LinearLayout) findViewById(R.id.lay_ans1_father);
            LinearLayout lay2_father = (LinearLayout) findViewById(R.id.lay_ans2_father);
            LinearLayout lay3_father = (LinearLayout) findViewById(R.id.lay_ans3_father);
            LinearLayout lay4_father = (LinearLayout) findViewById(R.id.lay_ans4_father);
            lay1.setBackground(getResources().getDrawable(R.drawable.blue_btn));
            lay2.setBackground(getResources().getDrawable(R.drawable.blue_btn));
            lay3.setBackground(getResources().getDrawable(R.drawable.blue_btn));
            lay4.setBackground(getResources().getDrawable(R.drawable.blue_btn));


            lay1.setVisibility(View.VISIBLE);
            lay2.setVisibility(View.VISIBLE);
            lay3.setVisibility(View.VISIBLE);
            lay4.setVisibility(View.VISIBLE);
            lay1_father.setVisibility(View.VISIBLE);
            lay2_father.setVisibility(View.VISIBLE);
            lay3_father.setVisibility(View.VISIBLE);
            lay4_father.setVisibility(View.VISIBLE);


            question_num++;
            TextView txt_question_num= (TextView) findViewById(R.id.txt_question_num);
            txt_question_num.setText(String.valueOf(question_num));
            TextView txt_question_num1= (TextView) findViewById(R.id.txt_question_num1);
            txt_question_num1.setText(String.valueOf(question_num));
            already_answered = 0;
//            already_two_removed = 0;
//            already_get_extra_time = 0;
            txt_question.setText(question[question_num]);
            txt_question1.setText(question[question_num]);

            txt_ans1.setText(ans1[question_num]);
            txt_ans2.setText(ans2[question_num]);
            txt_ans3.setText(ans3[question_num]);
            txt_ans4.setText(ans4[question_num]);
            txt_ans1_others.setText(String.valueOf(Integer.valueOf(ans1_others[question_num])*100));
            txt_ans2_others.setText(String.valueOf(Integer.valueOf(ans2_others[question_num])*100));
            txt_ans3_others.setText(String.valueOf(Integer.valueOf(ans3_others[question_num])*100));
            txt_ans4_others.setText(String.valueOf(Integer.valueOf(ans4_others[question_num])*100));

            progress_bar = 1;
        }
        else
        {


            //show_msg("اتمام","آزمون شما به پایان رسید آیا می خواهید خارج شوید؟","بله&خیر&نمی دانم");
        }



    }
    public void clk_second_chance(View v)
    {

        if(already_answered==0 && second_chance==0) {
            if(coin_count>=30) {
                if (second_chance == 0)
                    second_chance = 1;
                LinearLayout lay_second_chance_cover = (LinearLayout) findViewById(R.id.lay_second_chance_cover);
                lay_second_chance_cover.setVisibility(View.VISIBLE);
                coin_count-=30;
                TextView txt_coint_count = (TextView) findViewById(R.id.txt_coin_count);
                TextView txt_coint_count1 = (TextView) findViewById(R.id.txt_coin_count1);
                txt_coint_count.setText(String.valueOf(coin_count));
                txt_coint_count1.setText(String.valueOf(coin_count));
            }
            else
            {
                Toast.makeText(getBaseContext(),"تعداد سکه ها کاافی نیست",Toast.LENGTH_SHORT).show();
            }

        }
    }
    public void clk_remove(View v)
    {
        if(already_two_removed == 0 && already_answered == 0)
        {
            if(coin_count>=30) {
                already_two_removed=1;
                int
                        last_removed = 0;
                for (int i = 1; i <= 2; i++) {
                    boolean
                            flag = false;
                    int
                            removable = 0;
                    while (flag == false) {
                        removable = new Random().nextInt(4) + 1;
                        if (removable != right_answer[question_num] && removable != last_removed)
                            flag = true;
                    }
                    last_removed = removable;
                    LinearLayout lay1 = (LinearLayout) findViewById(R.id.lay_ans1);
                    LinearLayout lay2 = (LinearLayout) findViewById(R.id.lay_ans2);
                    LinearLayout lay3 = (LinearLayout) findViewById(R.id.lay_ans3);
                    LinearLayout lay4 = (LinearLayout) findViewById(R.id.lay_ans4);
                    LinearLayout lay1_father = (LinearLayout) findViewById(R.id.lay_ans1_father);
                    LinearLayout lay2_father = (LinearLayout) findViewById(R.id.lay_ans2_father);
                    LinearLayout lay3_father = (LinearLayout) findViewById(R.id.lay_ans3_father);
                    LinearLayout lay4_father = (LinearLayout) findViewById(R.id.lay_ans4_father);

                    if (removable == 1) {
                        lay1.setVisibility(LinearLayout.INVISIBLE);
                        lay1_father.setVisibility(LinearLayout.INVISIBLE);
                    }
                    if (removable == 2) {
                        lay2.setVisibility(LinearLayout.INVISIBLE);
                        lay2_father.setVisibility(LinearLayout.INVISIBLE);
                    }
                    if (removable == 3) {
                        lay3.setVisibility(LinearLayout.INVISIBLE);
                        lay3_father.setVisibility(LinearLayout.INVISIBLE);
                    }
                    if (removable == 4) {
                        lay4.setVisibility(LinearLayout.INVISIBLE);
                        lay4_father.setVisibility(LinearLayout.INVISIBLE);
                    }

                    LinearLayout lay_remove_two = (LinearLayout) findViewById(R.id.lay_remove_cover);
                    lay_remove_two.setVisibility(View.VISIBLE);
                }
                coin_count-=30;
                TextView txt_coint_count = (TextView) findViewById(R.id.txt_coin_count);
                TextView txt_coint_count1 = (TextView) findViewById(R.id.txt_coin_count1);
                txt_coint_count.setText(String.valueOf(coin_count));
                txt_coint_count1.setText(String.valueOf(coin_count));
            }
            else
            {
                Toast.makeText(getBaseContext(),"تعداد سکه ها کاافی نیست",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void clk_extra_time(View v) {

        if(already_get_extra_time==0 && already_answered == 0) {
            if(coin_count>=30) {
                float
                        increase_bar = (screenWidth / question_deadline) * 5;
                progress_bar -= increase_bar;
                if (progress_bar <= 0)
                    progress_bar = 1;
                LinearLayout lay_extra_time_cover = (LinearLayout) findViewById(R.id.lay_extra_time_cover);
                lay_extra_time_cover.setVisibility(View.VISIBLE);
                coin_count-=30;
                TextView txt_coint_count = (TextView) findViewById(R.id.txt_coin_count);
                TextView txt_coint_count1 = (TextView) findViewById(R.id.txt_coin_count1);
                txt_coint_count.setText(String.valueOf(coin_count));
                txt_coint_count1.setText(String.valueOf(coin_count));
                already_get_extra_time = 1;
            }
            else
            {
                Toast.makeText(getBaseContext(),"تعداد سکه ها کاافی نیست",Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void clk_ans1(View v)
    {
        LinearLayout lay = (LinearLayout) findViewById(R.id.lay_ans1);
        check_answer(1,lay);
    }
    public void clk_ans2(View v)
    {
        LinearLayout lay = (LinearLayout) findViewById(R.id.lay_ans2);
        check_answer(2,lay);
    }
    public void clk_ans3(View v)
    {
        LinearLayout lay = (LinearLayout) findViewById(R.id.lay_ans3);
        check_answer(3,lay);
    }
    public void clk_ans4(View v)
    {
        LinearLayout lay = (LinearLayout) findViewById(R.id.lay_ans4);
        check_answer(4, lay);
    }
    public void clk_btn_msg1(View view) {

        Button btn_all = (Button) view;

        if (btn_all.getText().toString().equals("خروج"))
        {
            finish();
        }


        if(btn_all.getText().toString().equals("تلاش مجدد"))
        {
            //if (no_connection)
            {
                run_last_query();
            }
        }

    }
    public void clk_continue(View view) {
        if(question_num<=3) {
            LinearLayout lay_between = (LinearLayout) findViewById(R.id.lay_between);
            lay_between.setVisibility(View.INVISIBLE);
            LinearLayout lay_main = (LinearLayout) findViewById(R.id.lay_main);
            enableDisableView(lay_main, true);

        }
        opinion_question[question_num] = 0;
        next_question();

    }

    public void clk_continue_like(View view) {
        opinion_question[question_num]=1;
        if(question_num<=3) {
            LinearLayout lay_between = (LinearLayout) findViewById(R.id.lay_between);
            lay_between.setVisibility(View.INVISIBLE);
            LinearLayout lay_main = (LinearLayout) findViewById(R.id.lay_main);
            enableDisableView(lay_main, true);
        }
        next_question();
    }

    public void clk_continue_dislike(View view) {
        opinion_question[question_num]=-1;
        if(question_num<=3) {
            LinearLayout lay_between = (LinearLayout) findViewById(R.id.lay_between);
            lay_between.setVisibility(View.INVISIBLE);
            LinearLayout lay_main = (LinearLayout) findViewById(R.id.lay_main);
            enableDisableView(lay_main, true);
        }
        next_question();
    }
    private void remove_message()
    {
        LinearLayout lay_main = (LinearLayout) findViewById(R.id.lay_main);

        fun.enableDisableView(lay_main, true);
        LinearLayout lay_between = (LinearLayout) findViewById(R.id.lay_between);

        fun.enableDisableView(lay_between, true);
        RelativeLayout lay_message = (RelativeLayout) findViewById(R.id.lay_message);
        lay_message.setVisibility(View.GONE);
    }
    private void run_last_query()
    {
        remove_message();
        lay_wait.setVisibility(View.VISIBLE);
        x = 1;
        is_requested = true;


        mm = new MyAsyncTask();

        mm.url = (last_requested_query);
        mm.execute("");

    }

    public void clk_people(View view) {
        if(already_get_people==0 && already_answered == 0) {
            if(coin_count>=30) {

                LinearLayout lay_people_cover = (LinearLayout) findViewById(R.id.lay_people_cover);
                lay_people_cover.setVisibility(View.VISIBLE);
                coin_count-=30;
                TextView txt_coint_count = (TextView) findViewById(R.id.txt_coin_count);
                TextView txt_coint_count1 = (TextView) findViewById(R.id.txt_coin_count1);
                txt_coint_count.setText(String.valueOf(coin_count));
                txt_coint_count1.setText(String.valueOf(coin_count));
                already_get_people = 1;
                txt_ans1_others.setVisibility(View.VISIBLE);
                txt_ans2_others.setVisibility(View.VISIBLE);
                txt_ans3_others.setVisibility(View.VISIBLE);
                txt_ans4_others.setVisibility(View.VISIBLE);



            }
            else
            {
                Toast.makeText(getBaseContext(),"تعداد سکه ها کاافی نیست",Toast.LENGTH_SHORT).show();
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


                if(param_str.equals("msg_sent")) {


                }
                if(param_str.equals("save_game")) {
                    //Toast.makeText(getBaseContext(),output_str,Toast.LENGTH_LONG).show();
                    start1 = output_str.indexOf("<question_number>");
                    end1 = output_str.indexOf("</question_number>");
                    String
                            q_num= output_str.substring(start1 + 17, end1);
                    if(q_num.equals("4")) {

                        finish();

                    }
                }
                    if(param_str.equals("get_question")) {
                    is_requested=false;
                    // output_str = ss.substring(start + 8, end);
                   // Toast.makeText(getBaseContext(),output_str,Toast.LENGTH_LONG).show();
                    for(int k=1;k<=functions.question_count;k++) {

                        start1 = output_str.indexOf("<question_"+String.valueOf(k)+">");
                        end1 = output_str.indexOf("</question_"+String.valueOf(k)+">");
                        //txt_question.setText();
                        question[k] = output_str.substring(start1 + 12, end1);





                        start1 = output_str.indexOf("<answered_"+String.valueOf(k)+">");
                        end1 = output_str.indexOf("</answered_"+String.valueOf(k)+">");
                        String
                            answered = (output_str.substring(start1 + 12, end1));
                        selected_answer[k]=Integer.valueOf(answered);


                        start1 = output_str.indexOf("<ans1_"+String.valueOf(k)+">");
                        end1 = output_str.indexOf("</ans1_"+String.valueOf(k)+">");

                        ans1[k] = (output_str.substring(start1 + 8, end1));

                        start1 = output_str.indexOf("<ans_others1_"+String.valueOf(k)+">");
                        end1 = output_str.indexOf("</ans_others1_"+String.valueOf(k)+">");

                        ans1_others[k] = (output_str.substring(start1 + 15, end1));


                        start1 = output_str.indexOf("<ans_others2_"+String.valueOf(k)+">");
                        end1 = output_str.indexOf("</ans_others2_"+String.valueOf(k)+">");

                        ans2_others[k] = (output_str.substring(start1 + 15, end1));


                        start1 = output_str.indexOf("<ans_others3_"+String.valueOf(k)+">");
                        end1 = output_str.indexOf("</ans_others3_"+String.valueOf(k)+">");

                        ans3_others[k] = (output_str.substring(start1 + 15, end1));


                        start1 = output_str.indexOf("<ans_others4_"+String.valueOf(k)+">");
                        end1 = output_str.indexOf("</ans_others4_"+String.valueOf(k)+">");

                        ans4_others[k] = (output_str.substring(start1 + 15, end1));




                        start1 = output_str.indexOf("<ans2_"+String.valueOf(k)+">");
                        end1 = output_str.indexOf("</ans2_"+String.valueOf(k)+">");
                        ans2[k] = (output_str.substring(start1 + 8, end1));

                        start1 = output_str.indexOf("<ans3_"+String.valueOf(k)+">");
                        end1 = output_str.indexOf("</ans3_"+String.valueOf(k)+">");
                        ans3[k] = (output_str.substring(start1 + 8, end1));


                        start1 = output_str.indexOf("<ans4_"+String.valueOf(k)+">");
                        end1 = output_str.indexOf("</ans4_"+String.valueOf(k)+">");
                        ans4[k] = (output_str.substring(start1 + 8, end1));


                        start1 = output_str.indexOf("<right_ans_"+String.valueOf(k)+">");
                        end1 = output_str.indexOf("</right_ans_"+String.valueOf(k)+">");
                       // Toast.makeText(getBaseContext(),output_str.substring(start1 + 13, end1),Toast.LENGTH_SHORT).show();
                        right_answer[k]  = Integer.valueOf(output_str.substring(start1 + 13, end1));
                    }
                    start1 = output_str.indexOf("<question_subject>");
                    end1 = output_str.indexOf("</question_subject>");
                    String
                            question_subject= (output_str.substring(start1 + 18, end1));
                    start1 = output_str.indexOf("<coin_count>");
                    end1 = output_str.indexOf("</coin_count>");
                    coin_count= Integer.valueOf(output_str.substring(start1 + 12, end1));

                    start1 = output_str.indexOf("<rival_name>");
                    end1 = output_str.indexOf("</rival_name>");
                    String
                            rival_name= (output_str.substring(start1 + 12, end1));

                        start1 = ss.indexOf("<rival_avatar_id>");
                        end1 = ss.indexOf("</rival_avatar_id>");
                        String
                                rival_avatar_id = ss.substring(start1 + 17, end1);

                        start1 = ss.indexOf("<rival_gender>");
                        end1 = ss.indexOf("</rival_gender>");
                        String
                                rival_gender = ss.substring(start1 + 14, end1);


                        start1 = output_str.indexOf("<max_q_num>");
                        end1 = output_str.indexOf("</max_q_num>");
                        String
                                max_q_num= (output_str.substring(start1 + 11, end1));

                    int
                            int_max_q_num=0;

                        try {
                            int_max_q_num = Integer.valueOf(max_q_num);

                        }catch (Exception e1)
                        {
                            int_max_q_num=1;
                        }
                        if (int_max_q_num > 0) {
                            question_num = (int_max_q_num+1);
                        }
                    TextView txt_coint_count = (TextView) findViewById(R.id.txt_coin_count);
                    TextView txt_coint_count1 = (TextView) findViewById(R.id.txt_coin_count1);
                    txt_coint_count.setText(String.valueOf(coin_count));
                    txt_coint_count1.setText(String.valueOf(coin_count));

                    TextView txt_rival_name = (TextView) findViewById(R.id.txt_opponent_name);
                    TextView txt_rival_name1 = (TextView) findViewById(R.id.txt_opponent_name1);
                    txt_rival_name.setText(rival_name);
                    txt_rival_name1.setText(rival_name);

                    TextView subject_name = (TextView) findViewById(R.id.txt_subject);
                    TextView subject_name1 = (TextView) findViewById(R.id.txt_subject1);

                    subject_name.setText(question_subject);
                    subject_name1.setText(question_subject);

                    TextView txt_your_name = (TextView) findViewById(R.id.txt_your_name);
                    TextView txt_your_name1 = (TextView) findViewById(R.id.txt_your_name1);
                    txt_your_name.setText(fun.u_name);
                    txt_your_name1.setText(fun.u_name);







                    txt_question.setText(question[question_num]);
                    txt_question1.setText(question[question_num]);

                        txt_ans1.setText(ans1[question_num]);
                        txt_ans2.setText(ans2[question_num]);
                        txt_ans3.setText(ans3[question_num]);
                        txt_ans4.setText(ans4[question_num]);
                        txt_ans1_others.setText(String.valueOf(Integer.valueOf(ans1_others[question_num])*100));
                        txt_ans2_others.setText(String.valueOf(Integer.valueOf(ans2_others[question_num])*100));
                        txt_ans3_others.setText(String.valueOf(Integer.valueOf(ans3_others[question_num])*100));
                        txt_ans4_others.setText(String.valueOf(Integer.valueOf(ans4_others[question_num])*100));


                    //txt_question.setText(output_str);
                    TextView txt_question_num= (TextView) findViewById(R.id.txt_question_num);
                    txt_question_num.setText(String.valueOf(question_num));
                    TextView txt_question_num1= (TextView) findViewById(R.id.txt_question_num1);
                    txt_question_num1.setText(String.valueOf(question_num));
                    lay_wait.setVisibility(View.GONE);
                    LinearLayout lay_main = (LinearLayout) findViewById(R.id.lay_main);
                    LinearLayout lay_mbetween = (LinearLayout) findViewById(R.id.lay_between);

                    fun.enableDisableView(lay_main,true);
                    fun.enableDisableView(lay_mbetween,true);

                    tim_question_deadline.start();

                        final ImageView image = (ImageView) findViewById(R.id.img_user_pic);

                        if(functions.gender.equals("boy"))
                        {
                            image.setBackground(getResources().getDrawable(R.drawable.profile_mail));

                        }
                        else
                        {
                            image.setBackground(getResources().getDrawable(R.drawable.profile_female));

                        }



                        // Image url
                        String image_url = "http://sputa-app.ir/app/trivia/pic/"+functions.avatar_name;


                        Picasso.with(getBaseContext()).load(image_url).fit().into(image);




                        final ImageView image1 = (ImageView) findViewById(R.id.img_rival_pic);

                        if(rival_gender.equals("1"))
                        {
                            image1.setBackground(getResources().getDrawable(R.drawable.profile_mail));

                        }
                        else
                        {
                            image1.setBackground(getResources().getDrawable(R.drawable.profile_female));

                        }



                        // Image url
                        image_url = "http://sputa-app.ir/app/trivia/pic/"+rival_name+"_"+rival_avatar_id+".jpg";


                        Picasso.with(getBaseContext()).load(image_url).fit().into(image1);


                        final ImageView image2 = (ImageView) findViewById(R.id.img_user_pic1);

                        if(functions.gender.equals("boy"))
                        {
                            image2.setBackground(getResources().getDrawable(R.drawable.profile_mail));

                        }
                        else
                        {
                            image2.setBackground(getResources().getDrawable(R.drawable.profile_female));

                        }



                        // Image url
                        image_url = "http://sputa-app.ir/app/trivia/pic/"+functions.avatar_name;


                        Picasso.with(getBaseContext()).load(image_url).fit().into(image2);




                        final ImageView image21 = (ImageView) findViewById(R.id.img_rival_pic1);

                        if(rival_gender.equals("1"))
                        {
                            image21.setBackground(getResources().getDrawable(R.drawable.profile_mail));

                        }
                        else
                        {
                            image21.setBackground(getResources().getDrawable(R.drawable.profile_female));

                        }



                        // Image url
                        image_url = "http://sputa-app.ir/app/trivia/pic/"+rival_name+"_"+rival_avatar_id+".jpg";


                        Picasso.with(getBaseContext()).load(image_url).fit().into(image21);


                        //  tim.start();

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

                                if(((int)x/100) > fun.request_time_out && is_requested)
                                {
                             //       Toast.makeText(getBaseContext(),"hello",Toast.LENGTH_SHORT).show();
                                    lay_wait.setVisibility(View.GONE);
                                    show_msg("اووپس","عدم ارتباط با سرور","تلاش مجدد&خروج");
                                    is_requested=false;

                                }

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
                        if(typ.equals("question_deadline"))
                        {
                            float
                                increase_bar = screenWidth/question_deadline/(float)100.0;
                            progress_bar += increase_bar;
                            if(progress_bar<=screenWidth && already_answered==0) {
                                LinearLayout lay_bar = (LinearLayout) findViewById(R.id.lay_progress);
                                LinearLayout.LayoutParams lp_lay_bar = new LinearLayout.LayoutParams((int) progress_bar, LinearLayout.LayoutParams.MATCH_PARENT);
                                lay_bar.setLayoutParams(lp_lay_bar);
                            }
                            else
                            {
                               // already_answered = 1;
                                LinearLayout lay1 = (LinearLayout) findViewById(R.id.lay_ans1);
                                check_answer(0,lay1);
                            }
                          //  txt_ans1.setText(String.valueOf(progress_bar)+"---"+String.valueOf(increase_bar));

                        }
                        if(typ.equals("next_question")) {

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
