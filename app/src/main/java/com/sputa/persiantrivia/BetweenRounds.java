package com.sputa.persiantrivia;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.Timer;

public class BetweenRounds extends AppCompatActivity {




    int screenWidth  = 0;
    int screenHeight = 0;

    int
        user_nobat = 0;

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
    int      x=1;
    Timer tim_circulation;
    Boolean  is_rotation = true;
    String last_requested_query="";
    public ImageView img_circle1;
    Button msg_btn1,msg_btn2,msg_btn3;
    String
            r_id ="";

    int
            user_round_count_finished = 0;
    int
            rival_round_count_finished = 0;


    int[] mine_round_id;
    int[] rival_round_id;


    String
            g_id="";

    int
        rival_round_coun=0,user_round_count=0;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_between_rounds);
        fun=new functions();

        //et_guild_name = (EditText) findViewById(R.id.txt_guild_name);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        fun = new functions();



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

        Intent i =getIntent();
        g_id = i.getStringExtra("uig_id");






        mine_round_id= new int[4];
        rival_round_id= new int[4];


        int
                img_profile_pic_size = (int)(screenHeight*.098);


        LinearLayout.LayoutParams lp_img_profile_pic_size = new LinearLayout.LayoutParams(img_profile_pic_size,img_profile_pic_size);

        ImageView img_user_profile_pic = (ImageView) findViewById(R.id.img_user_pic);
        img_user_profile_pic.setLayoutParams(lp_img_profile_pic_size);




        ImageView img_rival_pic = (ImageView) findViewById(R.id.img_rival_pic);
        img_rival_pic.setLayoutParams(lp_img_profile_pic_size);

        set_content();

        get_game_brief();
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
    private void get_game_brief()
    {
        lay_wait.setVisibility(View.VISIBLE);
        x = 1;
        is_requested = true;
        mm = new MyAsyncTask();
        //////////////////////////////////////////بازی شماره 5 داند اول حریف اول بازی کرده
        ////////////////////////////////////////بازی شماره 6 بازیکن اول بازی کرده است
        /////////////////////////////////////بازی شماره 7 هردو راند اول را به پایان رسانده اند
///////////////////////////////////////////بازی شماره 8 یکیشون راند دوم رو بازی کرده اون یکی نکرده
        ////////////////////////////////////////////بازی راند شماره 9 هردو راند دوم رو تموم کردم

       // Toast.makeText(getBaseContext(),g_id,Toast.LENGTH_SHORT).show();
        last_requested_query=getResources().getString(R.string.site_url) + "do.php?param=get_game_brief&uname="+fun.u_name+"&g_id="+g_id+"&rnd="+String.valueOf(new Random().nextInt());;
        //  Toast.makeText(getBaseContext(),last_requested_query,Toast.LENGTH_LONG).show();
        mm.url = (last_requested_query);
        mm.execute("");

    }
    public void clk_new_game(View view) {


        if((rival_round_count_finished==user_round_count_finished && rival_round_count_finished==3) || user_nobat==2 || user_nobat==1)
        {

        }
        else {
            finish();
            if (user_nobat == 0 && rival_round_coun == user_round_count) {

                if(rival_round_coun==1 && user_round_count_finished==1)
                {
                    r_id="";
                }
                if(rival_round_coun==2 && user_round_count_finished==2)
                {
                    r_id="";
                }

                if (r_id.equals("")) {
                //    Toast.makeText(getBaseContext(),"rrr"+ r_id, Toast.LENGTH_SHORT).show();
                    Intent j = getIntent();
                    Intent i = new Intent(getBaseContext(), select_subject.class);
                    i.putExtra("g_id", g_id);
                    startActivity(i);
                } else {
                    Intent i = new Intent(getBaseContext(), GameBoard.class);
                 //   Toast.makeText(getBaseContext(), r_id, Toast.LENGTH_SHORT).show();
                    i.putExtra("r_id", r_id);
                    startActivity(i);
                }

            } else if (user_nobat == 0 && rival_round_coun != user_round_count) {
                Intent i = new Intent(getBaseContext(), GameBoard.class);
                //Toast.makeText(getBaseContext(), r_id, Toast.LENGTH_SHORT).show();
                i.putExtra("r_id", r_id);
                startActivity(i);
            }
        }



    }

    public void clk1(View view) {
//        Random rnd=new Random();
//        String
//                str_rnd=String.valueOf(rnd.nextDouble());
//        TextView txt1= (TextView) findViewById(R.id.txt_score);
//        txt1.setText(str_rnd);


    }
    private void set_content()
    {



        TextView txt_opponent_name = (TextView) findViewById(R.id.txt_opponent_name);
        txt_opponent_name.setTypeface(tf);
        txt_opponent_name.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.042));

        TextView txt_your_name = (TextView) findViewById(R.id.txt_your_name);
        txt_your_name.setTypeface(tf);
        txt_your_name.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.042));

        TextView txt_score= (TextView) findViewById(R.id.txt_score);
        txt_score.setTypeface(tf);
        txt_score.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.044));


        TextView txt_num_mine11 = (TextView) findViewById(R.id.num_mine11);
        TextView txt_num_mine12 = (TextView) findViewById(R.id.num_mine12);
        TextView txt_num_mine13 = (TextView) findViewById(R.id.num_mine13);
        TextView txt_num_mine14 = (TextView) findViewById(R.id.num_mine14);





        TextView txt_num_rival11 = (TextView) findViewById(R.id.num_rival11);
        TextView txt_num_rival12 = (TextView) findViewById(R.id.num_rival12);
        TextView txt_num_rival13 = (TextView) findViewById(R.id.num_rival13);
        TextView txt_num_rival14 = (TextView) findViewById(R.id.num_rival14);


        TextView txt_title_mine_r1 = (TextView) findViewById(R.id.txt_mine_title_r1);


        TextView txt_title_rival_r1 = (TextView) findViewById(R.id.txt_rival_title_r1);


        txt_num_mine11.setTypeface(tf);
        txt_num_mine11.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));
        txt_num_mine12.setTypeface(tf);
        txt_num_mine12.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));
        txt_num_mine13.setTypeface(tf);
        txt_num_mine13.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));
        txt_num_mine14.setTypeface(tf);
        txt_num_mine14.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));

        txt_num_rival11.setTypeface(tf);
        txt_num_rival11.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));
        txt_num_rival12.setTypeface(tf);
        txt_num_rival12.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));
        txt_num_rival13.setTypeface(tf);
        txt_num_rival13.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));
        txt_num_rival14.setTypeface(tf);
        txt_num_rival14.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));



        txt_title_mine_r1.setTypeface(tf);
        txt_title_mine_r1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.042));

        txt_title_rival_r1.setTypeface(tf);
        txt_title_rival_r1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.042));

        TextView txt_subject_r1_n = (TextView) findViewById(R.id.txt_title_round1);
        txt_subject_r1_n.setTypeface(tf);
        txt_subject_r1_n.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.046));







        TextView txt_num_mine21 = (TextView) findViewById(R.id.num_mine21);
        TextView txt_num_mine22 = (TextView) findViewById(R.id.num_mine22);
        TextView txt_num_mine23 = (TextView) findViewById(R.id.num_mine23);
        TextView txt_num_mine24 = (TextView) findViewById(R.id.num_mine24);



        TextView txt_num_rival21 = (TextView) findViewById(R.id.num_rival21);
        TextView txt_num_rival22 = (TextView) findViewById(R.id.num_rival22);
        TextView txt_num_rival23 = (TextView) findViewById(R.id.num_rival23);
        TextView txt_num_rival24 = (TextView) findViewById(R.id.num_rival24);









        TextView txt_title_mine_r2 = (TextView) findViewById(R.id.txt_mine_title_r2);


        TextView txt_title_rival_r2 = (TextView) findViewById(R.id.txt_rival_title_r2);




        txt_num_mine21.setTypeface(tf);
        txt_num_mine21.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));
        txt_num_mine22.setTypeface(tf);
        txt_num_mine22.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));
        txt_num_mine23.setTypeface(tf);
        txt_num_mine23.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));
        txt_num_mine24.setTypeface(tf);
        txt_num_mine24.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));

        txt_num_rival21.setTypeface(tf);
        txt_num_rival21.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));
        txt_num_rival22.setTypeface(tf);
        txt_num_rival22.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));
        txt_num_rival23.setTypeface(tf);
        txt_num_rival23.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));
        txt_num_rival24.setTypeface(tf);
        txt_num_rival24.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));



        txt_title_mine_r2.setTypeface(tf);
        txt_title_mine_r2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.042));

        txt_title_rival_r2.setTypeface(tf);
        txt_title_rival_r2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.042));

        TextView txt_subject_r2_n = (TextView) findViewById(R.id.txt_title_round2);
        txt_subject_r2_n.setTypeface(tf);
        txt_subject_r2_n.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.046));


        TextView txt_num_mine31 = (TextView) findViewById(R.id.num_mine31);
        TextView txt_num_mine32 = (TextView) findViewById(R.id.num_mine32);
        TextView txt_num_mine33 = (TextView) findViewById(R.id.num_mine33);
        TextView txt_num_mine34 = (TextView) findViewById(R.id.num_mine34);



        TextView txt_num_rival31 = (TextView) findViewById(R.id.num_rival31);
        TextView txt_num_rival32 = (TextView) findViewById(R.id.num_rival32);
        TextView txt_num_rival33 = (TextView) findViewById(R.id.num_rival33);
        TextView txt_num_rival34 = (TextView) findViewById(R.id.num_rival34);









        TextView txt_title_mine_r3 = (TextView) findViewById(R.id.txt_mine_title_r3);


        TextView txt_title_rival_r3 = (TextView) findViewById(R.id.txt_rival_title_r3);




        txt_num_mine31.setTypeface(tf);
        txt_num_mine31.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));
        txt_num_mine32.setTypeface(tf);
        txt_num_mine32.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));
        txt_num_mine33.setTypeface(tf);
        txt_num_mine33.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));
        txt_num_mine34.setTypeface(tf);
        txt_num_mine34.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));

        txt_num_rival31.setTypeface(tf);
        txt_num_rival31.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));
        txt_num_rival32.setTypeface(tf);
        txt_num_rival32.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));
        txt_num_rival33.setTypeface(tf);
        txt_num_rival33.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));
        txt_num_rival34.setTypeface(tf);
        txt_num_rival34.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));



        txt_title_mine_r3.setTypeface(tf);
        txt_title_mine_r3.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.042));

        txt_title_rival_r3.setTypeface(tf);
        txt_title_rival_r3.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.042));

        TextView txt_subject_r3_n = (TextView) findViewById(R.id.txt_title_round3);
        txt_subject_r3_n.setTypeface(tf);
        txt_subject_r3_n.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.046));











        TextView txt_start_game = (TextView) findViewById(R.id.txt_start_game);
        txt_start_game.setTypeface(tf);
        txt_start_game.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.046));

        LinearLayout.LayoutParams lp_lay_btn_start_game = new LinearLayout.LayoutParams((int) (screenWidth * 0.5),(int) (screenHeight * 0.076));

        LinearLayout lay_btn_start_game = (LinearLayout) findViewById(R.id.lay_btn_start_game);
        lay_btn_start_game.setLayoutParams(lp_lay_btn_start_game);



    }
    public void set_round1(String ss,int user_turn,int rival_turn)
    {
        r_id="";
        TextView txt_num_mine11 = (TextView) findViewById(R.id.num_mine11);
        TextView txt_num_mine12 = (TextView) findViewById(R.id.num_mine12);
        TextView txt_num_mine13 = (TextView) findViewById(R.id.num_mine13);
        TextView txt_num_mine14 = (TextView) findViewById(R.id.num_mine14);





        TextView txt_num_rival11 = (TextView) findViewById(R.id.num_rival11);
        TextView txt_num_rival12 = (TextView) findViewById(R.id.num_rival12);
        TextView txt_num_rival13 = (TextView) findViewById(R.id.num_rival13);
        TextView txt_num_rival14 = (TextView) findViewById(R.id.num_rival14);



        ImageView img_mine11 = (ImageView) findViewById(R.id.img_mine1_1);
        ImageView img_mine12 = (ImageView) findViewById(R.id.img_mine1_2);
        ImageView img_mine13 = (ImageView) findViewById(R.id.img_mine1_3);
        ImageView img_mine14 = (ImageView) findViewById(R.id.img_mine1_4);




        ImageView img_rival11 = (ImageView) findViewById(R.id.img_other1_1);
        ImageView img_rival12 = (ImageView) findViewById(R.id.img_other1_2);
        ImageView img_rival13 = (ImageView) findViewById(R.id.img_other1_3);
        ImageView img_rival14 = (ImageView) findViewById(R.id.img_other1_4);

        TextView txt_title_mine_r1 = (TextView) findViewById(R.id.txt_mine_title_r1);


        TextView txt_title_rival_r1 = (TextView) findViewById(R.id.txt_rival_title_r1);


        if(rival_round_coun==user_round_count) {
            if ((user_turn) == 0) {



                img_rival11.setBackground(getResources().getDrawable(R.drawable.check_unknown));
                img_rival12.setBackground(getResources().getDrawable(R.drawable.check_unknown));
                img_rival13.setBackground(getResources().getDrawable(R.drawable.check_unknown));
                img_rival14.setBackground(getResources().getDrawable(R.drawable.check_unknown));



                int
                        start1 = ss.indexOf("<round_user1>");
                int
                        end1 = ss.indexOf("</round_user1>");
                String
                        round1 = ss.substring(start1 + 13, end1);

                start1 = round1.indexOf("<r_id>");
                end1 = round1.indexOf("</r_id>");
                r_id = round1.substring(start1 + 6, end1);
                start1 = round1.indexOf("<r_id>");
                end1 = round1.indexOf("</r_id>");
                mine_round_id[1] = Integer.valueOf(round1.substring(start1 + 6, end1));
                start1 = round1.indexOf("<q1>");
                end1 = round1.indexOf("</q1>");
                String
                        q1 = round1.substring(start1 + 4, end1);
                start1 = round1.indexOf("<q2>");
                end1 = round1.indexOf("</q2>");
                String
                        q2 = round1.substring(start1 + 4, end1);
                start1 = round1.indexOf("<q3>");
                end1 = round1.indexOf("</q3>");
                String
                        q3 = round1.substring(start1 + 4, end1);
                start1 = round1.indexOf("<q4>");
                end1 = round1.indexOf("</q4>");
                String
                        q4 = round1.substring(start1 + 4, end1);

                start1 = round1.indexOf("<subject>");
                end1 = round1.indexOf("</subject>");
                String
                        subject = round1.substring(start1 + 9, end1);

                TextView txt_subject_r1 = (TextView) findViewById(R.id.txt_title_round1);
                txt_subject_r1.setText("راند اول - " + subject);


                if (q1.equals("wrong")) {
                    img_mine11.setBackground(getResources().getDrawable(R.drawable.check_false));
                }
                if (q1.equals("right")) {
                    img_mine11.setBackground(getResources().getDrawable(R.drawable.check_true));
                }
                if (q1.equals("not_answer")) {
                    img_mine11.setBackground(getResources().getDrawable(R.drawable.check_none));
                }

                if (q2.equals("wrong")) {
                    img_mine12.setBackground(getResources().getDrawable(R.drawable.check_false));
                }
                if (q2.equals("right")) {
                    img_mine12.setBackground(getResources().getDrawable(R.drawable.check_true));
                }
                if (q2.equals("not_answer")) {
                    img_mine12.setBackground(getResources().getDrawable(R.drawable.check_none));
                }

                if (q3.equals("wrong")) {
                    img_mine13.setBackground(getResources().getDrawable(R.drawable.check_false));
                }
                if (q3.equals("right")) {
                    img_mine13.setBackground(getResources().getDrawable(R.drawable.check_true));
                }
                if (q3.equals("not_answer")) {
                    img_mine13.setBackground(getResources().getDrawable(R.drawable.check_none));
                }

                if (q4.equals("wrong")) {
                    img_mine14.setBackground(getResources().getDrawable(R.drawable.check_false));
                }
                if (q4.equals("right")) {
                    img_mine14.setBackground(getResources().getDrawable(R.drawable.check_true));
                }
                if (q4.equals("not_answer")) {
                    img_mine14.setBackground(getResources().getDrawable(R.drawable.check_none));
                }


                txt_title_mine_r1.setText("نوبت شما");
                txt_title_mine_r1.setTextColor(Color.RED);
            }
            if ((user_turn) == 1) {
                // Toast.makeText(getBaseContext(), "sass", Toast.LENGTH_SHORT).show();


                img_rival11.setVisibility(View.INVISIBLE);
                img_rival12.setVisibility(View.INVISIBLE);
                img_rival13.setVisibility(View.INVISIBLE);
                img_rival14.setVisibility(View.INVISIBLE);


                int
                        start1 = ss.indexOf("<round_user1>");
                int
                        end1 = ss.indexOf("</round_user1>");
                String
                        round1 = ss.substring(start1 + 13, end1);
//                Toast.makeText(getBaseContext(),round1,Toast.LENGTH_LONG).show();
//                Toast.makeText(getBaseContext(),ss,Toast.LENGTH_LONG).show();

                start1 = round1.indexOf("<r_id>");
                end1 = round1.indexOf("</r_id>");
                r_id = round1.substring(start1 + 6, end1);


                start1 = round1.indexOf("<r_id>");
                end1 = round1.indexOf("</r_id>");
                mine_round_id[1] = Integer.valueOf(round1.substring(start1 + 6, end1));

                start1 = round1.indexOf("<q1>");
                end1 = round1.indexOf("</q1>");
                String
                        q1 = round1.substring(start1 + 4, end1);
                start1 = round1.indexOf("<q2>");
                end1 = round1.indexOf("</q2>");
                String
                        q2 = round1.substring(start1 + 4, end1);
                start1 = round1.indexOf("<q3>");
                end1 = round1.indexOf("</q3>");
                String
                        q3 = round1.substring(start1 + 4, end1);
                start1 = round1.indexOf("<q4>");
                end1 = round1.indexOf("</q4>");
                String
                        q4 = round1.substring(start1 + 4, end1);

                start1 = round1.indexOf("<subject>");
                end1 = round1.indexOf("</subject>");
                String
                        subject = round1.substring(start1 + 9, end1);

                TextView txt_subject_r1 = (TextView) findViewById(R.id.txt_title_round1);
                txt_subject_r1.setText("راند اول - " + subject);


                if (q1.equals("wrong")) {
                    img_mine11.setBackground(getResources().getDrawable(R.drawable.check_false));
                }
                if (q1.equals("right")) {
                    img_mine11.setBackground(getResources().getDrawable(R.drawable.check_true));
                }
                if (q1.equals("not_answer")) {
                    img_mine11.setBackground(getResources().getDrawable(R.drawable.check_none));
                }

                if (q2.equals("wrong")) {
                    img_mine12.setBackground(getResources().getDrawable(R.drawable.check_false));
                }
                if (q2.equals("right")) {
                    img_mine12.setBackground(getResources().getDrawable(R.drawable.check_true));
                }
                if (q2.equals("not_answer")) {
                    img_mine12.setBackground(getResources().getDrawable(R.drawable.check_none));
                }

                if (q3.equals("wrong")) {
                    img_mine13.setBackground(getResources().getDrawable(R.drawable.check_false));
                }
                if (q3.equals("right")) {
                    img_mine13.setBackground(getResources().getDrawable(R.drawable.check_true));
                }
                if (q3.equals("not_answer")) {
                    img_mine13.setBackground(getResources().getDrawable(R.drawable.check_none));
                }
               // Toast.makeText(getBaseContext(),q4,Toast.LENGTH_SHORT).show();
                if (q4.equals("wrong")) {
                    img_mine14.setBackground(getResources().getDrawable(R.drawable.check_false));
                }
                if (q4.equals("right")) {
                    img_mine14.setBackground(getResources().getDrawable(R.drawable.check_true));
                }
                if (q4.equals("not_answer")) {
                    img_mine14.setBackground(getResources().getDrawable(R.drawable.check_none));
                }


                txt_title_rival_r1.setText("نوبت حریف");
                txt_title_rival_r1.setTextColor(Color.RED);
            }
        }
        else
        {
            if ((user_turn) == 0) {






                if(rival_round_coun<user_round_count) {
                    img_rival11.setVisibility(View.INVISIBLE);
                    img_rival12.setVisibility(View.INVISIBLE);
                    img_rival13.setVisibility(View.INVISIBLE);
                    img_rival14.setVisibility(View.INVISIBLE);
                    int
                            start1 = ss.indexOf("<round_user1>");
                    int
                            end1 = ss.indexOf("</round_user1>");
                    String
                            round1 = ss.substring(start1 + 13, end1);

                    start1 = round1.indexOf("<r_id>");
                    end1 = round1.indexOf("</r_id>");
                    r_id = round1.substring(start1 + 6, end1);

                    start1 = round1.indexOf("<r_id>");
                    end1 = round1.indexOf("</r_id>");
                    mine_round_id[1] = Integer.valueOf(round1.substring(start1 + 6, end1));

                    start1 = round1.indexOf("<q1>");
                    end1 = round1.indexOf("</q1>");
                    String
                            q1 = round1.substring(start1 + 4, end1);
                    start1 = round1.indexOf("<q2>");
                    end1 = round1.indexOf("</q2>");
                    String
                            q2 = round1.substring(start1 + 4, end1);
                    start1 = round1.indexOf("<q3>");
                    end1 = round1.indexOf("</q3>");
                    String
                            q3 = round1.substring(start1 + 4, end1);
                    start1 = round1.indexOf("<q4>");
                    end1 = round1.indexOf("</q4>");
                    String
                            q4 = round1.substring(start1 + 4, end1);

                    start1 = round1.indexOf("<subject>");
                    end1 = round1.indexOf("</subject>");
                    String
                            subject = round1.substring(start1 + 9, end1);

                    TextView txt_subject_r1 = (TextView) findViewById(R.id.txt_title_round1);
                    txt_subject_r1.setText("راند اول - " + subject);


                    if (q1.equals("wrong")) {
                        img_mine11.setBackground(getResources().getDrawable(R.drawable.check_false));
                    }
                    if (q1.equals("right")) {
                        img_mine11.setBackground(getResources().getDrawable(R.drawable.check_true));
                    }
                    if (q1.equals("not_answer")) {
                        img_mine11.setBackground(getResources().getDrawable(R.drawable.check_none));
                    }

                    if (q2.equals("wrong")) {
                        img_mine12.setBackground(getResources().getDrawable(R.drawable.check_false));
                    }
                    if (q2.equals("right")) {
                        img_mine12.setBackground(getResources().getDrawable(R.drawable.check_true));
                    }
                    if (q2.equals("not_answer")) {
                        img_mine12.setBackground(getResources().getDrawable(R.drawable.check_none));
                    }

                    if (q3.equals("wrong")) {
                        img_mine13.setBackground(getResources().getDrawable(R.drawable.check_false));
                    }
                    if (q3.equals("right")) {
                        img_mine13.setBackground(getResources().getDrawable(R.drawable.check_true));
                    }
                    if (q3.equals("not_answer")) {
                        img_mine13.setBackground(getResources().getDrawable(R.drawable.check_none));
                    }

                    if (q4.equals("wrong")) {
                        img_mine14.setBackground(getResources().getDrawable(R.drawable.check_false));
                    }
                    if (q4.equals("right")) {
                        img_mine14.setBackground(getResources().getDrawable(R.drawable.check_true));
                    }
                    if (q4.equals("not_answer")) {
                        img_mine14.setBackground(getResources().getDrawable(R.drawable.check_none));
                    }
                }
                else
                {
                    img_mine11.setVisibility(View.INVISIBLE);
                    img_mine12.setVisibility(View.INVISIBLE);
                    img_mine13.setVisibility(View.INVISIBLE);
                    img_mine14.setVisibility(View.INVISIBLE);

                    img_rival11.setBackground(getResources().getDrawable(R.drawable.check_unknown));
                    img_rival12.setBackground(getResources().getDrawable(R.drawable.check_unknown));
                    img_rival13.setBackground(getResources().getDrawable(R.drawable.check_unknown));
                    img_rival14.setBackground(getResources().getDrawable(R.drawable.check_unknown));
                }

                txt_title_mine_r1.setText("نوبت شما");
                txt_title_mine_r1.setTextColor(Color.RED);
            }
            if ((user_turn) == 1) {

                img_rival11.setVisibility(View.INVISIBLE);
                img_rival12.setVisibility(View.INVISIBLE);
                img_rival13.setVisibility(View.INVISIBLE);
                img_rival14.setVisibility(View.INVISIBLE);


                int
                        start1 = ss.indexOf("<round_user1>");
                int
                        end1 = ss.indexOf("</round_user1>");
                String
                        round1 = ss.substring(start1 + 13, end1);

                start1 = round1.indexOf("<r_id>");
                end1 = round1.indexOf("</r_id>");
                r_id = round1.substring(start1 + 6, end1);

                start1 = round1.indexOf("<r_id>");
                end1 = round1.indexOf("</r_id>");
                r_id = round1.substring(start1 + 6, end1);


                start1 = round1.indexOf("<r_id>");
                end1 = round1.indexOf("</r_id>");
                mine_round_id[1] = Integer.valueOf(round1.substring(start1 + 6, end1));
                start1 = round1.indexOf("<q1>");
                end1 = round1.indexOf("</q1>");
                String
                        q1 = round1.substring(start1 + 4, end1);
                start1 = round1.indexOf("<q2>");
                end1 = round1.indexOf("</q2>");
                String
                        q2 = round1.substring(start1 + 4, end1);
                start1 = round1.indexOf("<q3>");
                end1 = round1.indexOf("</q3>");
                String
                        q3 = round1.substring(start1 + 4, end1);
                start1 = round1.indexOf("<q4>");
                end1 = round1.indexOf("</q4>");
                String
                        q4 = round1.substring(start1 + 4, end1);

                start1 = round1.indexOf("<subject>");
                end1 = round1.indexOf("</subject>");
                String
                        subject = round1.substring(start1 + 9, end1);

                TextView txt_subject_r1 = (TextView) findViewById(R.id.txt_title_round1);
                txt_subject_r1.setText("راند اول - " + subject);


                if (q1.equals("wrong")) {
                    img_mine11.setBackground(getResources().getDrawable(R.drawable.check_false));
                }
                if (q1.equals("right")) {
                    img_mine11.setBackground(getResources().getDrawable(R.drawable.check_true));
                }
                if (q1.equals("not_answer")) {
                    img_mine11.setBackground(getResources().getDrawable(R.drawable.check_none));
                }

                if (q2.equals("wrong")) {
                    img_mine12.setBackground(getResources().getDrawable(R.drawable.check_false));
                }
                if (q2.equals("right")) {
                    img_mine12.setBackground(getResources().getDrawable(R.drawable.check_true));
                }
                if (q2.equals("not_answer")) {
                    img_mine12.setBackground(getResources().getDrawable(R.drawable.check_none));
                }

                if (q3.equals("wrong")) {
                    img_mine13.setBackground(getResources().getDrawable(R.drawable.check_false));
                }
                if (q3.equals("right")) {
                    img_mine13.setBackground(getResources().getDrawable(R.drawable.check_true));
                }
                if (q3.equals("not_answer")) {
                    img_mine13.setBackground(getResources().getDrawable(R.drawable.check_none));
                }

                if (q4.equals("wrong")) {
                    img_mine14.setBackground(getResources().getDrawable(R.drawable.check_false));
                }
                if (q4.equals("right")) {
                    img_mine14.setBackground(getResources().getDrawable(R.drawable.check_true));
                }
                if (q4.equals("not_answer")) {
                    img_mine14.setBackground(getResources().getDrawable(R.drawable.check_none));
                }


                txt_title_rival_r1.setText("نوبت حریف");
                txt_title_rival_r1.setTextColor(Color.RED);
            }

        }
        if((user_turn)==2)
        {



//            img_rival11.setVisibility(View.INVISIBLE);
//            img_rival12.setVisibility(View.INVISIBLE);
//            img_rival13.setVisibility(View.INVISIBLE);
//            img_rival14.setVisibility(View.INVISIBLE);
//

            int
                    start1 = ss.indexOf("<round_user1>");
            int
                    end1 = ss.indexOf("</round_user1>");
            String
                    round1 = ss.substring(start1 + 13, end1);

            start1 = round1.indexOf("<r_id>");
            end1 = round1.indexOf("</r_id>");
            r_id = round1.substring(start1 + 6, end1);

            start1 = round1.indexOf("<r_id>");
            end1 = round1.indexOf("</r_id>");
            mine_round_id[1] = Integer.valueOf(round1.substring(start1 + 6, end1));

            start1 = round1.indexOf("<q1>");
            end1 = round1.indexOf("</q1>");
            String
                    q1 = round1.substring(start1 + 4, end1);
            start1 = round1.indexOf("<q2>");
            end1 = round1.indexOf("</q2>");
            String
                    q2 = round1.substring(start1 + 4, end1);
            start1 = round1.indexOf("<q3>");
            end1 = round1.indexOf("</q3>");
            String
                    q3 = round1.substring(start1 + 4, end1);
            start1 = round1.indexOf("<q4>");
            end1 = round1.indexOf("</q4>");
            String
                    q4 = round1.substring(start1 + 4, end1);

            start1 = round1.indexOf("<subject>");
            end1 = round1.indexOf("</subject>");
            String
                    subject = round1.substring(start1 + 9, end1);

            TextView txt_subject_r1 = (TextView) findViewById(R.id.txt_title_round1);
            txt_subject_r1.setText("راند اول - "+subject);

            if(q1.equals("wrong"))
            {
                img_mine11.setBackground(getResources().getDrawable(R.drawable.check_false));
            }
            if(q1.equals("right"))
            {
                img_mine11.setBackground(getResources().getDrawable(R.drawable.check_true));
            }
            if(q1.equals("not_answer"))
            {
                img_mine11.setBackground(getResources().getDrawable(R.drawable.check_none));
            }

            if(q2.equals("wrong"))
            {
                img_mine12.setBackground(getResources().getDrawable(R.drawable.check_false));
            }
            if(q2.equals("right"))
            {
                img_mine12.setBackground(getResources().getDrawable(R.drawable.check_true));
            }
            if(q2.equals("not_answer"))
            {
                img_mine12.setBackground(getResources().getDrawable(R.drawable.check_none));
            }

            if(q3.equals("wrong"))
            {
                img_mine13.setBackground(getResources().getDrawable(R.drawable.check_false));
            }
            if(q3.equals("right"))
            {
                img_mine13.setBackground(getResources().getDrawable(R.drawable.check_true));
            }
            if(q3.equals("not_answer"))
            {
                img_mine13.setBackground(getResources().getDrawable(R.drawable.check_none));
            }

            if(q4.equals("wrong"))
            {
                img_mine14.setBackground(getResources().getDrawable(R.drawable.check_false));
            }
            if(q4.equals("right"))
            {
                img_mine14.setBackground(getResources().getDrawable(R.drawable.check_true));
            }
            if(q4.equals("not_answer"))
            {
                img_mine14.setBackground(getResources().getDrawable(R.drawable.check_none));
            }

            start1 = ss.indexOf("<round_rival1>");
            end1 = ss.indexOf("</round_rival1>");
            round1 = ss.substring(start1 + 14, end1);


            start1 = round1.indexOf("<r_id>");
            end1 = round1.indexOf("</r_id>");
            rival_round_id[1] = Integer.valueOf(round1.substring(start1 + 6, end1));

            start1 = round1.indexOf("<q1>");
            end1 = round1.indexOf("</q1>");
            q1 = round1.substring(start1 + 4, end1);
            start1 = round1.indexOf("<q2>");
            end1 = round1.indexOf("</q2>");
            q2 = round1.substring(start1 + 4, end1);
            start1 = round1.indexOf("<q3>");
            end1 = round1.indexOf("</q3>");
            q3 = round1.substring(start1 + 4, end1);
            start1 = round1.indexOf("<q4>");
            end1 = round1.indexOf("</q4>");
            q4 = round1.substring(start1 + 4, end1);


            if(q1.equals("wrong"))
            {
                img_rival11.setBackground(getResources().getDrawable(R.drawable.check_false));
            }
            if(q1.equals("right"))
            {
                img_rival11.setBackground(getResources().getDrawable(R.drawable.check_true));
            }
            if(q1.equals("not_answer"))
            {
                img_rival11.setBackground(getResources().getDrawable(R.drawable.check_none));
            }

            if(q2.equals("wrong"))
            {
                img_rival12.setBackground(getResources().getDrawable(R.drawable.check_false));
            }
            if(q2.equals("right"))
            {
                img_rival12.setBackground(getResources().getDrawable(R.drawable.check_true));
            }
            if(q2.equals("not_answer"))
            {
                img_rival12.setBackground(getResources().getDrawable(R.drawable.check_none));
            }

            if(q3.equals("wrong"))
            {
                img_rival13.setBackground(getResources().getDrawable(R.drawable.check_false));
            }
            if(q3.equals("right"))
            {
                img_rival13.setBackground(getResources().getDrawable(R.drawable.check_true));
            }
            if(q3.equals("not_answer"))
            {
                img_rival13.setBackground(getResources().getDrawable(R.drawable.check_none));
            }

            if(q4.equals("wrong"))
            {
                img_rival14.setBackground(getResources().getDrawable(R.drawable.check_false));
            }
            if(q4.equals("right"))
            {
                img_rival14.setBackground(getResources().getDrawable(R.drawable.check_true));
            }
            if(q4.equals("not_answer"))
            {
                img_rival14.setBackground(getResources().getDrawable(R.drawable.check_none));
            }

        }
     //   Toast.makeText(BetweenRounds.this, r_id+"1", Toast.LENGTH_SHORT).show();

    }

    public void set_round2(String ss,int user_turn,int rival_turn)
    {
        r_id="";
        TextView txt_num_mine21 = (TextView) findViewById(R.id.num_mine21);
        TextView txt_num_mine22 = (TextView) findViewById(R.id.num_mine22);
        TextView txt_num_mine23 = (TextView) findViewById(R.id.num_mine23);
        TextView txt_num_mine24 = (TextView) findViewById(R.id.num_mine24);



        TextView txt_num_rival21 = (TextView) findViewById(R.id.num_rival21);
        TextView txt_num_rival22 = (TextView) findViewById(R.id.num_rival22);
        TextView txt_num_rival23 = (TextView) findViewById(R.id.num_rival23);
        TextView txt_num_rival24 = (TextView) findViewById(R.id.num_rival24);




        ImageView img_mine21 = (ImageView) findViewById(R.id.img_mine2_1);
        ImageView img_mine22 = (ImageView) findViewById(R.id.img_mine2_2);
        ImageView img_mine23 = (ImageView) findViewById(R.id.img_mine2_3);
        ImageView img_mine24 = (ImageView) findViewById(R.id.img_mine2_4);




        ImageView img_rival21 = (ImageView) findViewById(R.id.img_other2_1);
        ImageView img_rival22 = (ImageView) findViewById(R.id.img_other2_2);
        ImageView img_rival23 = (ImageView) findViewById(R.id.img_other2_3);
        ImageView img_rival24 = (ImageView) findViewById(R.id.img_other2_4);

        TextView txt_title_mine_r2 = (TextView) findViewById(R.id.txt_mine_title_r2);


        TextView txt_title_rival_r2 = (TextView) findViewById(R.id.txt_rival_title_r2);




        txt_num_mine21.setTypeface(tf);
        txt_num_mine21.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));
        txt_num_mine22.setTypeface(tf);
        txt_num_mine22.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));
        txt_num_mine23.setTypeface(tf);
        txt_num_mine23.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));
        txt_num_mine24.setTypeface(tf);
        txt_num_mine24.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));

        txt_num_rival21.setTypeface(tf);
        txt_num_rival21.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));
        txt_num_rival22.setTypeface(tf);
        txt_num_rival22.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));
        txt_num_rival23.setTypeface(tf);
        txt_num_rival23.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));
        txt_num_rival24.setTypeface(tf);
        txt_num_rival24.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));



        txt_title_mine_r2.setTypeface(tf);
        txt_title_mine_r2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.042));

        txt_title_rival_r2.setTypeface(tf);
        txt_title_rival_r2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.042));

        TextView txt_subject_r2_n = (TextView) findViewById(R.id.txt_title_round2);
        txt_subject_r2_n.setTypeface(tf);
        txt_subject_r2_n.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.046));

        if(rival_round_coun==user_round_count) {
            if ((user_turn) == 0) {



                img_rival21.setBackground(getResources().getDrawable(R.drawable.check_unknown));
                img_rival22.setBackground(getResources().getDrawable(R.drawable.check_unknown));
                img_rival23.setBackground(getResources().getDrawable(R.drawable.check_unknown));
                img_rival24.setBackground(getResources().getDrawable(R.drawable.check_unknown));



                int
                        start1 = ss.indexOf("<round_user2>");
                int
                        end1 = ss.indexOf("</round_user2>");
                String
                        round1 = ss.substring(start1 + 13, end1);

                start1 = round1.indexOf("<r_id>");
                end1 = round1.indexOf("</r_id>");
                r_id = round1.substring(start1 + 6, end1);
                start1 = round1.indexOf("<r_id>");
                end1 = round1.indexOf("</r_id>");
                mine_round_id[2] = Integer.valueOf(round1.substring(start1 + 6, end1));
                start1 = round1.indexOf("<q1>");
                end1 = round1.indexOf("</q1>");
                String
                        q1 = round1.substring(start1 + 4, end1);
                start1 = round1.indexOf("<q2>");
                end1 = round1.indexOf("</q2>");
                String
                        q2 = round1.substring(start1 + 4, end1);
                start1 = round1.indexOf("<q3>");
                end1 = round1.indexOf("</q3>");
                String
                        q3 = round1.substring(start1 + 4, end1);
                start1 = round1.indexOf("<q4>");
                end1 = round1.indexOf("</q4>");
                String
                        q4 = round1.substring(start1 + 4, end1);

                start1 = round1.indexOf("<subject>");
                end1 = round1.indexOf("</subject>");
                String
                        subject = round1.substring(start1 + 9, end1);

                TextView txt_subject_r1 = (TextView) findViewById(R.id.txt_title_round2);
                txt_subject_r1.setText("راند دوم - " + subject);


                if (q1.equals("wrong")) {
                    img_mine21.setBackground(getResources().getDrawable(R.drawable.check_false));
                }
                if (q1.equals("right")) {
                    img_mine21.setBackground(getResources().getDrawable(R.drawable.check_true));
                }
                if (q1.equals("not_answer")) {
                    img_mine21.setBackground(getResources().getDrawable(R.drawable.check_none));
                }

                if (q2.equals("wrong")) {
                    img_mine22.setBackground(getResources().getDrawable(R.drawable.check_false));
                }
                if (q2.equals("right")) {
                    img_mine22.setBackground(getResources().getDrawable(R.drawable.check_true));
                }
                if (q2.equals("not_answer")) {
                    img_mine22.setBackground(getResources().getDrawable(R.drawable.check_none));
                }

                if (q3.equals("wrong")) {
                    img_mine23.setBackground(getResources().getDrawable(R.drawable.check_false));
                }
                if (q3.equals("right")) {
                    img_mine23.setBackground(getResources().getDrawable(R.drawable.check_true));
                }
                if (q3.equals("not_answer")) {
                    img_mine23.setBackground(getResources().getDrawable(R.drawable.check_none));
                }

                if (q4.equals("wrong")) {
                    img_mine24.setBackground(getResources().getDrawable(R.drawable.check_false));
                }
                if (q4.equals("right")) {
                    img_mine24.setBackground(getResources().getDrawable(R.drawable.check_true));
                }
                if (q4.equals("not_answer")) {
                    img_mine24.setBackground(getResources().getDrawable(R.drawable.check_none));
                }


                txt_title_mine_r2.setText("نوبت شما");
                txt_title_mine_r2.setTextColor(Color.RED);
            }
            if ((user_turn) == 1) {
                // Toast.makeText(getBaseContext(), "sass", Toast.LENGTH_SHORT).show();


                img_rival21.setVisibility(View.INVISIBLE);
                img_rival22.setVisibility(View.INVISIBLE);
                img_rival23.setVisibility(View.INVISIBLE);
                img_rival24.setVisibility(View.INVISIBLE);


                int
                        start1 = ss.indexOf("<round_user2>");
                int
                        end1 = ss.indexOf("</round_user2>");
                String
                        round1 = ss.substring(start1 + 13, end1);

                start1 = round1.indexOf("<r_id>");
                end1 = round1.indexOf("</r_id>");
                r_id = round1.substring(start1 + 6, end1);

                start1 = round1.indexOf("<r_id>");
                end1 = round1.indexOf("</r_id>");
                mine_round_id[2] = Integer.valueOf(round1.substring(start1 + 6, end1));

                start1 = round1.indexOf("<q1>");
                end1 = round1.indexOf("</q1>");
                String
                        q1 = round1.substring(start1 + 4, end1);
                start1 = round1.indexOf("<q2>");
                end1 = round1.indexOf("</q2>");
                String
                        q2 = round1.substring(start1 + 4, end1);
                start1 = round1.indexOf("<q3>");
                end1 = round1.indexOf("</q3>");
                String
                        q3 = round1.substring(start1 + 4, end1);
                start1 = round1.indexOf("<q4>");
                end1 = round1.indexOf("</q4>");
                String
                        q4 = round1.substring(start1 + 4, end1);

                start1 = round1.indexOf("<subject>");
                end1 = round1.indexOf("</subject>");
                String
                        subject = round1.substring(start1 + 9, end1);

                TextView txt_subject_r1 = (TextView) findViewById(R.id.txt_title_round2);
                txt_subject_r1.setText("راند دوم - " + subject);


                if (q1.equals("wrong")) {
                    img_mine21.setBackground(getResources().getDrawable(R.drawable.check_false));
                }
                if (q1.equals("right")) {
                    img_mine21.setBackground(getResources().getDrawable(R.drawable.check_true));
                }
                if (q1.equals("not_answer")) {
                    img_mine21.setBackground(getResources().getDrawable(R.drawable.check_none));
                }

                if (q2.equals("wrong")) {
                    img_mine22.setBackground(getResources().getDrawable(R.drawable.check_false));
                }
                if (q2.equals("right")) {
                    img_mine22.setBackground(getResources().getDrawable(R.drawable.check_true));
                }
                if (q2.equals("not_answer")) {
                    img_mine22.setBackground(getResources().getDrawable(R.drawable.check_none));
                }

                if (q3.equals("wrong")) {
                    img_mine23.setBackground(getResources().getDrawable(R.drawable.check_false));
                }
                if (q3.equals("right")) {
                    img_mine23.setBackground(getResources().getDrawable(R.drawable.check_true));
                }
                if (q3.equals("not_answer")) {
                    img_mine23.setBackground(getResources().getDrawable(R.drawable.check_none));
                }

                if (q4.equals("wrong")) {
                    img_mine24.setBackground(getResources().getDrawable(R.drawable.check_false));
                }
                if (q4.equals("right")) {
                    img_mine24.setBackground(getResources().getDrawable(R.drawable.check_true));
                }
                if (q4.equals("not_answer")) {
                    img_mine24.setBackground(getResources().getDrawable(R.drawable.check_none));
                }


                txt_title_rival_r2.setText("نوبت حریف");
                txt_title_rival_r2.setTextColor(Color.RED);
            }
        }
        else
        {
            if ((user_turn) == 0) {






                if(rival_round_coun<user_round_count) {
                    img_rival21.setVisibility(View.INVISIBLE);
                    img_rival22.setVisibility(View.INVISIBLE);
                    img_rival23.setVisibility(View.INVISIBLE);
                    img_rival24.setVisibility(View.INVISIBLE);
                    int
                            start1 = ss.indexOf("<round_user2>");
                    int
                            end1 = ss.indexOf("</round_user2>");
                    String
                            round1 = ss.substring(start1 + 13, end1);

                    start1 = round1.indexOf("<r_id>");
                    end1 = round1.indexOf("</r_id>");
                    r_id = round1.substring(start1 + 6, end1);

                    start1 = round1.indexOf("<r_id>");
                    end1 = round1.indexOf("</r_id>");
                    mine_round_id[2] = Integer.valueOf(round1.substring(start1 + 6, end1));

                    start1 = round1.indexOf("<q1>");
                    end1 = round1.indexOf("</q1>");
                    String
                            q1 = round1.substring(start1 + 4, end1);
                    start1 = round1.indexOf("<q2>");
                    end1 = round1.indexOf("</q2>");
                    String
                            q2 = round1.substring(start1 + 4, end1);
                    start1 = round1.indexOf("<q3>");
                    end1 = round1.indexOf("</q3>");
                    String
                            q3 = round1.substring(start1 + 4, end1);
                    start1 = round1.indexOf("<q4>");
                    end1 = round1.indexOf("</q4>");
                    String
                            q4 = round1.substring(start1 + 4, end1);

                    start1 = round1.indexOf("<subject>");
                    end1 = round1.indexOf("</subject>");
                    String
                            subject = round1.substring(start1 + 9, end1);

                    TextView txt_subject_r1 = (TextView) findViewById(R.id.txt_title_round2);
                    txt_subject_r1.setText("راند دوم - " + subject);


                    if (q1.equals("wrong")) {
                        img_mine21.setBackground(getResources().getDrawable(R.drawable.check_false));
                    }
                    if (q1.equals("right")) {
                        img_mine21.setBackground(getResources().getDrawable(R.drawable.check_true));
                    }
                    if (q1.equals("not_answer")) {
                        img_mine21.setBackground(getResources().getDrawable(R.drawable.check_none));
                    }

                    if (q2.equals("wrong")) {
                        img_mine22.setBackground(getResources().getDrawable(R.drawable.check_false));
                    }
                    if (q2.equals("right")) {
                        img_mine22.setBackground(getResources().getDrawable(R.drawable.check_true));
                    }
                    if (q2.equals("not_answer")) {
                        img_mine22.setBackground(getResources().getDrawable(R.drawable.check_none));
                    }

                    if (q3.equals("wrong")) {
                        img_mine23.setBackground(getResources().getDrawable(R.drawable.check_false));
                    }
                    if (q3.equals("right")) {
                        img_mine23.setBackground(getResources().getDrawable(R.drawable.check_true));
                    }
                    if (q3.equals("not_answer")) {
                        img_mine23.setBackground(getResources().getDrawable(R.drawable.check_none));
                    }

                    if (q4.equals("wrong")) {
                        img_mine24.setBackground(getResources().getDrawable(R.drawable.check_false));
                    }
                    if (q4.equals("right")) {
                        img_mine24.setBackground(getResources().getDrawable(R.drawable.check_true));
                    }
                    if (q4.equals("not_answer")) {
                        img_mine24.setBackground(getResources().getDrawable(R.drawable.check_none));
                    }
                }
                else
                {
                    img_mine21.setVisibility(View.INVISIBLE);
                    img_mine22.setVisibility(View.INVISIBLE);
                    img_mine23.setVisibility(View.INVISIBLE);
                    img_mine24.setVisibility(View.INVISIBLE);

                    img_rival21.setBackground(getResources().getDrawable(R.drawable.check_unknown));
                    img_rival22.setBackground(getResources().getDrawable(R.drawable.check_unknown));
                    img_rival23.setBackground(getResources().getDrawable(R.drawable.check_unknown));
                    img_rival24.setBackground(getResources().getDrawable(R.drawable.check_unknown));
                }

                txt_title_mine_r2.setText("نوبت شما");
                txt_title_mine_r2.setTextColor(Color.RED);
            }
            if ((user_turn) == 1) {

                img_rival21.setVisibility(View.INVISIBLE);
                img_rival22.setVisibility(View.INVISIBLE);
                img_rival23.setVisibility(View.INVISIBLE);
                img_rival24.setVisibility(View.INVISIBLE);
                if(user_round_count==2) {
                    int
                            start1 = ss.indexOf("<round_user2>");
                    int
                            end1 = ss.indexOf("</round_user2>");
                    String
                            round1 = ss.substring(start1 + 13, end1);

                    start1 = round1.indexOf("<r_id>");
                    end1 = round1.indexOf("</r_id>");
                    r_id = round1.substring(start1 + 6, end1);


                    start1 = round1.indexOf("<r_id>");
                    end1 = round1.indexOf("</r_id>");
                    mine_round_id[2] = Integer.valueOf(round1.substring(start1 + 6, end1));

                    start1 = round1.indexOf("<q1>");
                    end1 = round1.indexOf("</q1>");
                    String
                            q1 = round1.substring(start1 + 4, end1);
                    start1 = round1.indexOf("<q2>");
                    end1 = round1.indexOf("</q2>");
                    String
                            q2 = round1.substring(start1 + 4, end1);
                    start1 = round1.indexOf("<q3>");
                    end1 = round1.indexOf("</q3>");
                    String
                            q3 = round1.substring(start1 + 4, end1);
                    start1 = round1.indexOf("<q4>");
                    end1 = round1.indexOf("</q4>");
                    String
                            q4 = round1.substring(start1 + 4, end1);

                    start1 = round1.indexOf("<subject>");
                    end1 = round1.indexOf("</subject>");
                    String
                            subject = round1.substring(start1 + 9, end1);

                    TextView txt_subject_r1 = (TextView) findViewById(R.id.txt_title_round2);
                    txt_subject_r1.setText("راند - دوم " + subject);


                    if (q1.equals("wrong")) {
                        img_mine21.setBackground(getResources().getDrawable(R.drawable.check_false));
                    }
                    if (q1.equals("right")) {
                        img_mine21.setBackground(getResources().getDrawable(R.drawable.check_true));
                    }
                    if (q1.equals("not_answer")) {
                        img_mine21.setBackground(getResources().getDrawable(R.drawable.check_none));
                    }

                    if (q2.equals("wrong")) {
                        img_mine22.setBackground(getResources().getDrawable(R.drawable.check_false));
                    }
                    if (q2.equals("right")) {
                        img_mine22.setBackground(getResources().getDrawable(R.drawable.check_true));
                    }
                    if (q2.equals("not_answer")) {
                        img_mine22.setBackground(getResources().getDrawable(R.drawable.check_none));
                    }

                    if (q3.equals("wrong")) {
                        img_mine23.setBackground(getResources().getDrawable(R.drawable.check_false));
                    }
                    if (q3.equals("right")) {
                        img_mine23.setBackground(getResources().getDrawable(R.drawable.check_true));
                    }
                    if (q3.equals("not_answer")) {
                        img_mine23.setBackground(getResources().getDrawable(R.drawable.check_none));
                    }

                    if (q4.equals("wrong")) {
                        img_mine24.setBackground(getResources().getDrawable(R.drawable.check_false));
                    }
                    if (q4.equals("right")) {
                        img_mine24.setBackground(getResources().getDrawable(R.drawable.check_true));
                    }
                    if (q4.equals("not_answer")) {
                        img_mine24.setBackground(getResources().getDrawable(R.drawable.check_none));
                    }
                }
                else
                {
                    img_mine21.setVisibility(View.INVISIBLE);
                    img_mine22.setVisibility(View.INVISIBLE);
                    img_mine23.setVisibility(View.INVISIBLE);
                    img_mine24.setVisibility(View.INVISIBLE);
                }
                txt_title_rival_r2.setText("نوبت حریف");
                txt_title_rival_r2.setTextColor(Color.RED);
            }

        }
        if((user_turn)==2)
        {



//            img_rival11.setVisibility(View.INVISIBLE);
//            img_rival12.setVisibility(View.INVISIBLE);
//            img_rival13.setVisibility(View.INVISIBLE);
//            img_rival14.setVisibility(View.INVISIBLE);
//

            int
                    start1 = ss.indexOf("<round_user2>");
            int
                    end1 = ss.indexOf("</round_user2>");
            String
                    round1 = ss.substring(start1 + 13, end1);

            start1 = round1.indexOf("<r_id>");
            end1 = round1.indexOf("</r_id>");
            r_id = round1.substring(start1 + 6, end1);

            start1 = round1.indexOf("<r_id>");
            end1 = round1.indexOf("</r_id>");
            mine_round_id[2] = Integer.valueOf(round1.substring(start1 + 6, end1));

            start1 = round1.indexOf("<q1>");
            end1 = round1.indexOf("</q1>");
            String
                    q1 = round1.substring(start1 + 4, end1);
            start1 = round1.indexOf("<q2>");
            end1 = round1.indexOf("</q2>");
            String
                    q2 = round1.substring(start1 + 4, end1);
            start1 = round1.indexOf("<q3>");
            end1 = round1.indexOf("</q3>");
            String
                    q3 = round1.substring(start1 + 4, end1);
            start1 = round1.indexOf("<q4>");
            end1 = round1.indexOf("</q4>");
            String
                    q4 = round1.substring(start1 + 4, end1);

            start1 = round1.indexOf("<subject>");
            end1 = round1.indexOf("</subject>");
            String
                    subject = round1.substring(start1 + 9, end1);

            TextView txt_subject_r1 = (TextView) findViewById(R.id.txt_title_round2);
            txt_subject_r1.setText("راند دوم - "+subject);

            if(q1.equals("wrong"))
            {
                img_mine21.setBackground(getResources().getDrawable(R.drawable.check_false));
            }
            if(q1.equals("right"))
            {
                img_mine21.setBackground(getResources().getDrawable(R.drawable.check_true));
            }
            if(q1.equals("not_answer"))
            {
                img_mine21.setBackground(getResources().getDrawable(R.drawable.check_none));
            }

            if(q2.equals("wrong"))
            {
                img_mine22.setBackground(getResources().getDrawable(R.drawable.check_false));
            }
            if(q2.equals("right"))
            {
                img_mine22.setBackground(getResources().getDrawable(R.drawable.check_true));
            }
            if(q2.equals("not_answer"))
            {
                img_mine22.setBackground(getResources().getDrawable(R.drawable.check_none));
            }

            if(q3.equals("wrong"))
            {
                img_mine23.setBackground(getResources().getDrawable(R.drawable.check_false));
            }
            if(q3.equals("right"))
            {
                img_mine23.setBackground(getResources().getDrawable(R.drawable.check_true));
            }
            if(q3.equals("not_answer"))
            {
                img_mine23.setBackground(getResources().getDrawable(R.drawable.check_none));
            }

            if(q4.equals("wrong"))
            {
                img_mine24.setBackground(getResources().getDrawable(R.drawable.check_false));
            }
            if(q4.equals("right"))
            {
                img_mine24.setBackground(getResources().getDrawable(R.drawable.check_true));
            }
            if(q4.equals("not_answer"))
            {
                img_mine24.setBackground(getResources().getDrawable(R.drawable.check_none));
            }

            start1 = ss.indexOf("<round_rival2>");
            end1 = ss.indexOf("</round_rival2>");
            round1 = ss.substring(start1 + 14, end1);


            start1 = round1.indexOf("<r_id>");
            end1 = round1.indexOf("</r_id>");
            rival_round_id[2] = Integer.valueOf(round1.substring(start1 + 6, end1));

            start1 = round1.indexOf("<q1>");
            end1 = round1.indexOf("</q1>");
            q1 = round1.substring(start1 + 4, end1);
            start1 = round1.indexOf("<q2>");
            end1 = round1.indexOf("</q2>");
            q2 = round1.substring(start1 + 4, end1);
            start1 = round1.indexOf("<q3>");
            end1 = round1.indexOf("</q3>");
            q3 = round1.substring(start1 + 4, end1);
            start1 = round1.indexOf("<q4>");
            end1 = round1.indexOf("</q4>");
            q4 = round1.substring(start1 + 4, end1);


            if(q1.equals("wrong"))
            {
                img_rival21.setBackground(getResources().getDrawable(R.drawable.check_false));
            }
            if(q1.equals("right"))
            {
                img_rival21.setBackground(getResources().getDrawable(R.drawable.check_true));
            }
            if(q1.equals("not_answer"))
            {
                img_rival21.setBackground(getResources().getDrawable(R.drawable.check_none));
            }

            if(q2.equals("wrong"))
            {
                img_rival22.setBackground(getResources().getDrawable(R.drawable.check_false));
            }
            if(q2.equals("right"))
            {
                img_rival22.setBackground(getResources().getDrawable(R.drawable.check_true));
            }
            if(q2.equals("not_answer"))
            {
                img_rival22.setBackground(getResources().getDrawable(R.drawable.check_none));
            }

            if(q3.equals("wrong"))
            {
                img_rival23.setBackground(getResources().getDrawable(R.drawable.check_false));
            }
            if(q3.equals("right"))
            {
                img_rival23.setBackground(getResources().getDrawable(R.drawable.check_true));
            }
            if(q3.equals("not_answer"))
            {
                img_rival23.setBackground(getResources().getDrawable(R.drawable.check_none));
            }

            if(q4.equals("wrong"))
            {
                img_rival24.setBackground(getResources().getDrawable(R.drawable.check_false));
            }
            if(q4.equals("right"))
            {
                img_rival24.setBackground(getResources().getDrawable(R.drawable.check_true));
            }
            if(q4.equals("not_answer"))
            {
                img_rival24.setBackground(getResources().getDrawable(R.drawable.check_none));
            }

        }

      //  Toast.makeText(BetweenRounds.this, r_id+"2"+"---"+g_id, Toast.LENGTH_SHORT).show();
    }


    public void set_round3(String ss,int user_turn,int rival_turn)
    {
        r_id="";
        TextView txt_num_mine31 = (TextView) findViewById(R.id.num_mine31);
        TextView txt_num_mine32 = (TextView) findViewById(R.id.num_mine32);
        TextView txt_num_mine33 = (TextView) findViewById(R.id.num_mine33);
        TextView txt_num_mine34 = (TextView) findViewById(R.id.num_mine34);



        TextView txt_num_rival31 = (TextView) findViewById(R.id.num_rival31);
        TextView txt_num_rival32 = (TextView) findViewById(R.id.num_rival32);
        TextView txt_num_rival33 = (TextView) findViewById(R.id.num_rival33);
        TextView txt_num_rival34 = (TextView) findViewById(R.id.num_rival34);




        ImageView img_mine31 = (ImageView) findViewById(R.id.img_mine3_1);
        ImageView img_mine32 = (ImageView) findViewById(R.id.img_mine3_2);
        ImageView img_mine33 = (ImageView) findViewById(R.id.img_mine3_3);
        ImageView img_mine34 = (ImageView) findViewById(R.id.img_mine3_4);




        ImageView img_rival31 = (ImageView) findViewById(R.id.img_other3_1);
        ImageView img_rival32 = (ImageView) findViewById(R.id.img_other3_2);
        ImageView img_rival33 = (ImageView) findViewById(R.id.img_other3_3);
        ImageView img_rival34 = (ImageView) findViewById(R.id.img_other3_4);

        TextView txt_title_mine_r3 = (TextView) findViewById(R.id.txt_mine_title_r3);


        TextView txt_title_rival_r3 = (TextView) findViewById(R.id.txt_rival_title_r3);
        if(rival_round_coun==user_round_count) {
            if ((user_turn) == 0) {



                img_rival31.setBackground(getResources().getDrawable(R.drawable.check_unknown));
                img_rival32.setBackground(getResources().getDrawable(R.drawable.check_unknown));
                img_rival33.setBackground(getResources().getDrawable(R.drawable.check_unknown));
                img_rival34.setBackground(getResources().getDrawable(R.drawable.check_unknown));



                int
                        start1 = ss.indexOf("<round_user3>");
                int
                        end1 = ss.indexOf("</round_user3>");
                String
                        round1 = ss.substring(start1 + 13, end1);

                start1 = round1.indexOf("<r_id>");
                end1 = round1.indexOf("</r_id>");
                r_id = round1.substring(start1 + 6, end1);


                start1 = round1.indexOf("<r_id>");
                end1 = round1.indexOf("</r_id>");
                mine_round_id[3] = Integer.valueOf(round1.substring(start1 + 6, end1));

                start1 = round1.indexOf("<q1>");
                end1 = round1.indexOf("</q1>");
                String
                        q1 = round1.substring(start1 + 4, end1);
                start1 = round1.indexOf("<q2>");
                end1 = round1.indexOf("</q2>");
                String
                        q2 = round1.substring(start1 + 4, end1);
                start1 = round1.indexOf("<q3>");
                end1 = round1.indexOf("</q3>");
                String
                        q3 = round1.substring(start1 + 4, end1);
                start1 = round1.indexOf("<q4>");
                end1 = round1.indexOf("</q4>");
                String
                        q4 = round1.substring(start1 + 4, end1);

                start1 = round1.indexOf("<subject>");
                end1 = round1.indexOf("</subject>");
                String
                        subject = round1.substring(start1 + 9, end1);

                TextView txt_subject_r1 = (TextView) findViewById(R.id.txt_title_round3);
                txt_subject_r1.setText("راند سوم - " + subject);


                if (q1.equals("wrong")) {
                    img_mine31.setBackground(getResources().getDrawable(R.drawable.check_false));
                }
                if (q1.equals("right")) {
                    img_mine31.setBackground(getResources().getDrawable(R.drawable.check_true));
                }
                if (q1.equals("not_answer")) {
                    img_mine31.setBackground(getResources().getDrawable(R.drawable.check_none));
                }

                if (q2.equals("wrong")) {
                    img_mine32.setBackground(getResources().getDrawable(R.drawable.check_false));
                }
                if (q2.equals("right")) {
                    img_mine32.setBackground(getResources().getDrawable(R.drawable.check_true));
                }
                if (q2.equals("not_answer")) {
                    img_mine32.setBackground(getResources().getDrawable(R.drawable.check_none));
                }

                if (q3.equals("wrong")) {
                    img_mine33.setBackground(getResources().getDrawable(R.drawable.check_false));
                }
                if (q3.equals("right")) {
                    img_mine33.setBackground(getResources().getDrawable(R.drawable.check_true));
                }
                if (q3.equals("not_answer")) {
                    img_mine33.setBackground(getResources().getDrawable(R.drawable.check_none));
                }

                if (q4.equals("wrong")) {
                    img_mine34.setBackground(getResources().getDrawable(R.drawable.check_false));
                }
                if (q4.equals("right")) {
                    img_mine34.setBackground(getResources().getDrawable(R.drawable.check_true));
                }
                if (q4.equals("not_answer")) {
                    img_mine34.setBackground(getResources().getDrawable(R.drawable.check_none));
                }


                txt_title_mine_r3.setText("نوبت شما");
                txt_title_mine_r3.setTextColor(Color.RED);
            }
            if ((user_turn) == 1) {
                // Toast.makeText(getBaseContext(), "sass", Toast.LENGTH_SHORT).show();


                img_rival31.setVisibility(View.INVISIBLE);
                img_rival32.setVisibility(View.INVISIBLE);
                img_rival33.setVisibility(View.INVISIBLE);
                img_rival34.setVisibility(View.INVISIBLE);


                int
                        start1 = ss.indexOf("<round_user3>");
                int
                        end1 = ss.indexOf("</round_user3>");
                String
                        round1 = ss.substring(start1 + 13, end1);

                start1 = round1.indexOf("<r_id>");
                end1 = round1.indexOf("</r_id>");
                r_id = round1.substring(start1 + 6, end1);


                start1 = round1.indexOf("<r_id>");
                end1 = round1.indexOf("</r_id>");
                mine_round_id[3] = Integer.valueOf(round1.substring(start1 + 6, end1));

                start1 = round1.indexOf("<q1>");
                end1 = round1.indexOf("</q1>");
                String
                        q1 = round1.substring(start1 + 4, end1);
                start1 = round1.indexOf("<q2>");
                end1 = round1.indexOf("</q2>");
                String
                        q2 = round1.substring(start1 + 4, end1);
                start1 = round1.indexOf("<q3>");
                end1 = round1.indexOf("</q3>");
                String
                        q3 = round1.substring(start1 + 4, end1);
                start1 = round1.indexOf("<q4>");
                end1 = round1.indexOf("</q4>");
                String
                        q4 = round1.substring(start1 + 4, end1);

                start1 = round1.indexOf("<subject>");
                end1 = round1.indexOf("</subject>");
                String
                        subject = round1.substring(start1 + 9, end1);

                TextView txt_subject_r1 = (TextView) findViewById(R.id.txt_title_round3);
                txt_subject_r1.setText("راند سوم - " + subject);


                if (q1.equals("wrong")) {
                    img_mine31.setBackground(getResources().getDrawable(R.drawable.check_false));
                }
                if (q1.equals("right")) {
                    img_mine31.setBackground(getResources().getDrawable(R.drawable.check_true));
                }
                if (q1.equals("not_answer")) {
                    img_mine31.setBackground(getResources().getDrawable(R.drawable.check_none));
                }

                if (q2.equals("wrong")) {
                    img_mine32.setBackground(getResources().getDrawable(R.drawable.check_false));
                }
                if (q2.equals("right")) {
                    img_mine32.setBackground(getResources().getDrawable(R.drawable.check_true));
                }
                if (q2.equals("not_answer")) {
                    img_mine32.setBackground(getResources().getDrawable(R.drawable.check_none));
                }

                if (q3.equals("wrong")) {
                    img_mine33.setBackground(getResources().getDrawable(R.drawable.check_false));
                }
                if (q3.equals("right")) {
                    img_mine33.setBackground(getResources().getDrawable(R.drawable.check_true));
                }
                if (q3.equals("not_answer")) {
                    img_mine33.setBackground(getResources().getDrawable(R.drawable.check_none));
                }

                if (q4.equals("wrong")) {
                    img_mine34.setBackground(getResources().getDrawable(R.drawable.check_false));
                }
                if (q4.equals("right")) {
                    img_mine34.setBackground(getResources().getDrawable(R.drawable.check_true));
                }
                if (q4.equals("not_answer")) {
                    img_mine34.setBackground(getResources().getDrawable(R.drawable.check_none));
                }


                txt_title_rival_r3.setText("نوبت حریف");
                txt_title_rival_r3.setTextColor(Color.RED);
            }
        }
        else
        {
            if ((user_turn) == 0) {






                if(rival_round_coun<user_round_count) {
                    img_rival31.setVisibility(View.INVISIBLE);
                    img_rival32.setVisibility(View.INVISIBLE);
                    img_rival33.setVisibility(View.INVISIBLE);
                    img_rival34.setVisibility(View.INVISIBLE);
                    int
                            start1 = ss.indexOf("<round_user3>");
                    int
                            end1 = ss.indexOf("</round_user3>");
                    String
                            round1 = ss.substring(start1 + 13, end1);

                    start1 = round1.indexOf("<r_id>");
                    end1 = round1.indexOf("</r_id>");
                    r_id = round1.substring(start1 + 6, end1);


                    start1 = round1.indexOf("<r_id>");
                    end1 = round1.indexOf("</r_id>");
                    mine_round_id[3] = Integer.valueOf(round1.substring(start1 + 6, end1));

                    start1 = round1.indexOf("<q1>");
                    end1 = round1.indexOf("</q1>");
                    String
                            q1 = round1.substring(start1 + 4, end1);
                    start1 = round1.indexOf("<q2>");
                    end1 = round1.indexOf("</q2>");
                    String
                            q2 = round1.substring(start1 + 4, end1);
                    start1 = round1.indexOf("<q3>");
                    end1 = round1.indexOf("</q3>");
                    String
                            q3 = round1.substring(start1 + 4, end1);
                    start1 = round1.indexOf("<q4>");
                    end1 = round1.indexOf("</q4>");
                    String
                            q4 = round1.substring(start1 + 4, end1);

                    start1 = round1.indexOf("<subject>");
                    end1 = round1.indexOf("</subject>");
                    String
                            subject = round1.substring(start1 + 9, end1);

                    TextView txt_subject_r1 = (TextView) findViewById(R.id.txt_title_round3);
                    txt_subject_r1.setText("راند سوم - " + subject);


                    if (q1.equals("wrong")) {
                        img_mine31.setBackground(getResources().getDrawable(R.drawable.check_false));
                    }
                    if (q1.equals("right")) {
                        img_mine31.setBackground(getResources().getDrawable(R.drawable.check_true));
                    }
                    if (q1.equals("not_answer")) {
                        img_mine31.setBackground(getResources().getDrawable(R.drawable.check_none));
                    }

                    if (q2.equals("wrong")) {
                        img_mine32.setBackground(getResources().getDrawable(R.drawable.check_false));
                    }
                    if (q2.equals("right")) {
                        img_mine32.setBackground(getResources().getDrawable(R.drawable.check_true));
                    }
                    if (q2.equals("not_answer")) {
                        img_mine32.setBackground(getResources().getDrawable(R.drawable.check_none));
                    }

                    if (q3.equals("wrong")) {
                        img_mine33.setBackground(getResources().getDrawable(R.drawable.check_false));
                    }
                    if (q3.equals("right")) {
                        img_mine33.setBackground(getResources().getDrawable(R.drawable.check_true));
                    }
                    if (q3.equals("not_answer")) {
                        img_mine33.setBackground(getResources().getDrawable(R.drawable.check_none));
                    }

                    if (q4.equals("wrong")) {
                        img_mine34.setBackground(getResources().getDrawable(R.drawable.check_false));
                    }
                    if (q4.equals("right")) {
                        img_mine34.setBackground(getResources().getDrawable(R.drawable.check_true));
                    }
                    if (q4.equals("not_answer")) {
                        img_mine34.setBackground(getResources().getDrawable(R.drawable.check_none));
                    }
                }
                else
                {
                    img_mine31.setVisibility(View.INVISIBLE);
                    img_mine32.setVisibility(View.INVISIBLE);
                    img_mine33.setVisibility(View.INVISIBLE);
                    img_mine34.setVisibility(View.INVISIBLE);

                    img_rival31.setBackground(getResources().getDrawable(R.drawable.check_unknown));
                    img_rival32.setBackground(getResources().getDrawable(R.drawable.check_unknown));
                    img_rival33.setBackground(getResources().getDrawable(R.drawable.check_unknown));
                    img_rival34.setBackground(getResources().getDrawable(R.drawable.check_unknown));
                }

                txt_title_mine_r3.setText("نوبت شما");
                txt_title_mine_r3.setTextColor(Color.RED);
            }
            if ((user_turn) == 1) {

                img_rival31.setVisibility(View.INVISIBLE);
                img_rival32.setVisibility(View.INVISIBLE);
                img_rival33.setVisibility(View.INVISIBLE);
                img_rival34.setVisibility(View.INVISIBLE);
                if(user_round_count==3) {
                    int
                            start1 = ss.indexOf("<round_user3>");
                    int
                            end1 = ss.indexOf("</round_user3>");
                    String
                            round1 = ss.substring(start1 + 13, end1);

                    start1 = round1.indexOf("<r_id>");
                    end1 = round1.indexOf("</r_id>");
                    r_id = round1.substring(start1 + 6, end1);



                    start1 = round1.indexOf("<r_id>");
                    end1 = round1.indexOf("</r_id>");
                    mine_round_id[3] = Integer.valueOf(round1.substring(start1 + 6, end1));

                    start1 = round1.indexOf("<q1>");
                    end1 = round1.indexOf("</q1>");
                    String
                            q1 = round1.substring(start1 + 4, end1);
                    start1 = round1.indexOf("<q2>");
                    end1 = round1.indexOf("</q2>");
                    String
                            q2 = round1.substring(start1 + 4, end1);
                    start1 = round1.indexOf("<q3>");
                    end1 = round1.indexOf("</q3>");
                    String
                            q3 = round1.substring(start1 + 4, end1);
                    start1 = round1.indexOf("<q4>");
                    end1 = round1.indexOf("</q4>");
                    String
                            q4 = round1.substring(start1 + 4, end1);

                    start1 = round1.indexOf("<subject>");
                    end1 = round1.indexOf("</subject>");
                    String
                            subject = round1.substring(start1 + 9, end1);

                    TextView txt_subject_r1 = (TextView) findViewById(R.id.txt_title_round3);
                    txt_subject_r1.setText("راند سوم - " + subject);


                    if (q1.equals("wrong")) {
                        img_mine31.setBackground(getResources().getDrawable(R.drawable.check_false));
                    }
                    if (q1.equals("right")) {
                        img_mine31.setBackground(getResources().getDrawable(R.drawable.check_true));
                    }
                    if (q1.equals("not_answer")) {
                        img_mine31.setBackground(getResources().getDrawable(R.drawable.check_none));
                    }

                    if (q2.equals("wrong")) {
                        img_mine32.setBackground(getResources().getDrawable(R.drawable.check_false));
                    }
                    if (q2.equals("right")) {
                        img_mine32.setBackground(getResources().getDrawable(R.drawable.check_true));
                    }
                    if (q2.equals("not_answer")) {
                        img_mine32.setBackground(getResources().getDrawable(R.drawable.check_none));
                    }

                    if (q3.equals("wrong")) {
                        img_mine33.setBackground(getResources().getDrawable(R.drawable.check_false));
                    }
                    if (q3.equals("right")) {
                        img_mine33.setBackground(getResources().getDrawable(R.drawable.check_true));
                    }
                    if (q3.equals("not_answer")) {
                        img_mine33.setBackground(getResources().getDrawable(R.drawable.check_none));
                    }

                    if (q4.equals("wrong")) {
                        img_mine34.setBackground(getResources().getDrawable(R.drawable.check_false));
                    }
                    if (q4.equals("right")) {
                        img_mine34.setBackground(getResources().getDrawable(R.drawable.check_true));
                    }
                    if (q4.equals("not_answer")) {
                        img_mine34.setBackground(getResources().getDrawable(R.drawable.check_none));
                    }

                }
                else
                {
                    img_mine31.setVisibility(View.INVISIBLE);
                    img_mine32.setVisibility(View.INVISIBLE);
                    img_mine33.setVisibility(View.INVISIBLE);
                    img_mine34.setVisibility(View.INVISIBLE);
                }
                txt_title_rival_r3.setText("نوبت حریف");
                txt_title_rival_r3.setTextColor(Color.RED);
            }

        }
        if((user_turn)==2)
        {



//            img_rival11.setVisibility(View.INVISIBLE);
//            img_rival12.setVisibility(View.INVISIBLE);
//            img_rival13.setVisibility(View.INVISIBLE);
//            img_rival14.setVisibility(View.INVISIBLE);
//

            int
                    start1 = ss.indexOf("<round_user3>");
            int
                    end1 = ss.indexOf("</round_user3>");
            String
                    round1 = ss.substring(start1 + 13, end1);


            start1 = round1.indexOf("<r_id>");
            end1 = round1.indexOf("</r_id>");
            r_id = round1.substring(start1 + 6, end1);



            start1 = round1.indexOf("<r_id>");
            end1 = round1.indexOf("</r_id>");
            mine_round_id[3] = Integer.valueOf(round1.substring(start1 + 6, end1));
            start1 = round1.indexOf("<q1>");
            end1 = round1.indexOf("</q1>");
            String
                    q1 = round1.substring(start1 + 4, end1);
            start1 = round1.indexOf("<q2>");
            end1 = round1.indexOf("</q2>");
            String
                    q2 = round1.substring(start1 + 4, end1);
            start1 = round1.indexOf("<q3>");
            end1 = round1.indexOf("</q3>");
            String
                    q3 = round1.substring(start1 + 4, end1);
            start1 = round1.indexOf("<q4>");
            end1 = round1.indexOf("</q4>");
            String
                    q4 = round1.substring(start1 + 4, end1);

            start1 = round1.indexOf("<subject>");
            end1 = round1.indexOf("</subject>");
            String
                    subject = round1.substring(start1 + 9, end1);

            TextView txt_subject_r1 = (TextView) findViewById(R.id.txt_title_round3);
            txt_subject_r1.setText("راند سوم - "+subject);

            if(q1.equals("wrong"))
            {
                img_mine31.setBackground(getResources().getDrawable(R.drawable.check_false));
            }
            if(q1.equals("right"))
            {
                img_mine31.setBackground(getResources().getDrawable(R.drawable.check_true));
            }
            if(q1.equals("not_answer"))
            {
                img_mine31.setBackground(getResources().getDrawable(R.drawable.check_none));
            }

            if(q2.equals("wrong"))
            {
                img_mine32.setBackground(getResources().getDrawable(R.drawable.check_false));
            }
            if(q2.equals("right"))
            {
                img_mine32.setBackground(getResources().getDrawable(R.drawable.check_true));
            }
            if(q2.equals("not_answer"))
            {
                img_mine32.setBackground(getResources().getDrawable(R.drawable.check_none));
            }

            if(q3.equals("wrong"))
            {
                img_mine33.setBackground(getResources().getDrawable(R.drawable.check_false));
            }
            if(q3.equals("right"))
            {
                img_mine33.setBackground(getResources().getDrawable(R.drawable.check_true));
            }
            if(q3.equals("not_answer"))
            {
                img_mine33.setBackground(getResources().getDrawable(R.drawable.check_none));
            }

            if(q4.equals("wrong"))
            {
                img_mine34.setBackground(getResources().getDrawable(R.drawable.check_false));
            }
            if(q4.equals("right"))
            {
                img_mine34.setBackground(getResources().getDrawable(R.drawable.check_true));
            }
            if(q4.equals("not_answer"))
            {
                img_mine34.setBackground(getResources().getDrawable(R.drawable.check_none));
            }

            start1 = ss.indexOf("<round_rival3>");
            end1 = ss.indexOf("</round_rival3>");
            round1 = ss.substring(start1 + 14, end1);



            start1 = round1.indexOf("<r_id>");
            end1 = round1.indexOf("</r_id>");
            rival_round_id[3] = Integer.valueOf(round1.substring(start1 + 6, end1));

            start1 = round1.indexOf("<q1>");
            end1 = round1.indexOf("</q1>");
            q1 = round1.substring(start1 + 4, end1);
            start1 = round1.indexOf("<q2>");
            end1 = round1.indexOf("</q2>");
            q2 = round1.substring(start1 + 4, end1);
            start1 = round1.indexOf("<q3>");
            end1 = round1.indexOf("</q3>");
            q3 = round1.substring(start1 + 4, end1);
            start1 = round1.indexOf("<q4>");
            end1 = round1.indexOf("</q4>");
            q4 = round1.substring(start1 + 4, end1);


            if(q1.equals("wrong"))
            {
                img_rival31.setBackground(getResources().getDrawable(R.drawable.check_false));
            }
            if(q1.equals("right"))
            {
                img_rival31.setBackground(getResources().getDrawable(R.drawable.check_true));
            }
            if(q1.equals("not_answer"))
            {
                img_rival31.setBackground(getResources().getDrawable(R.drawable.check_none));
            }

            if(q2.equals("wrong"))
            {
                img_rival32.setBackground(getResources().getDrawable(R.drawable.check_false));
            }
            if(q2.equals("right"))
            {
                img_rival32.setBackground(getResources().getDrawable(R.drawable.check_true));
            }
            if(q2.equals("not_answer"))
            {
                img_rival32.setBackground(getResources().getDrawable(R.drawable.check_none));
            }

            if(q3.equals("wrong"))
            {
                img_rival33.setBackground(getResources().getDrawable(R.drawable.check_false));
            }
            if(q3.equals("right"))
            {
                img_rival33.setBackground(getResources().getDrawable(R.drawable.check_true));
            }
            if(q3.equals("not_answer"))
            {
                img_rival33.setBackground(getResources().getDrawable(R.drawable.check_none));
            }

            if(q4.equals("wrong"))
            {
                img_rival34.setBackground(getResources().getDrawable(R.drawable.check_false));
            }
            if(q4.equals("right"))
            {
                img_rival34.setBackground(getResources().getDrawable(R.drawable.check_true));
            }
            if(q4.equals("not_answer"))
            {
                img_rival34.setBackground(getResources().getDrawable(R.drawable.check_none));
            }

        }

       // Toast.makeText(BetweenRounds.this, r_id+"3", Toast.LENGTH_SHORT).show();
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

    public void clk_show_question(View view) {
     //   Toast.makeText(BetweenRounds.this, r_id, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getBaseContext(), ShowGameResult.class);
        i.putExtra("r_id", r_id);
        startActivity(i);
    }

    public void clk_show_result(View view) {

        ImageView img;

        img = (ImageView) view;
        String IdAsString = img.getResources().getResourceName(img.getId());
     //   Toast.makeText(BetweenRounds.this, IdAsString, Toast.LENGTH_SHORT).show();
        String
                round_id="";
        String
                question_id="";
        String
                round_type="";

        if(IdAsString.length()>12) {
            round_id = IdAsString.substring((IdAsString.length() - 3),(IdAsString.length() - 2));
            question_id = IdAsString.substring(IdAsString.length() - 1, IdAsString.length() );
            round_type = IdAsString.substring(IdAsString.length() - 8, (IdAsString.length() - 3));
         //   Toast.makeText(BetweenRounds.this, round_id + "---" + question_id + "----" + round_type, Toast.LENGTH_SHORT).show();
//            int drawableId =  (Integer)img.getTag();
//            Toast.makeText(BetweenRounds.this, String.valueOf(drawableId), Toast.LENGTH_SHORT).show();

            if(round_type.equals("other"))
            {


                if(Integer.valueOf(round_id)<=user_round_count_finished || user_nobat==2 )
                {

                    Intent i = new Intent(getBaseContext(), ShowGameResult.class);
                    i.putExtra("r_id", String.valueOf(rival_round_id[Integer.valueOf(round_id)]));
                    i.putExtra("q_number",question_id);
                    startActivity(i);
                    //Toast.makeText(BetweenRounds.this,"rival: "+ rival_round_id[Integer.valueOf(round_id)] , Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                if( Integer.valueOf(round_id) <= user_round_count_finished  || user_nobat==2)
                {

                    Intent i = new Intent(getBaseContext(), ShowGameResult.class);
                    i.putExtra("r_id", String.valueOf(mine_round_id[Integer.valueOf(round_id)]));
                    i.putExtra("q_number",question_id);

                    startActivity(i);
                  //  Toast.makeText(BetweenRounds.this,"mine: "+ mine_round_id[Integer.valueOf(round_id)] , Toast.LENGTH_SHORT).show();
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

                if ((param_str.equals("get_game_brief") ) && is_requested) {




                    TextView txt_num_mine11 = (TextView) findViewById(R.id.num_mine11);
                    TextView txt_num_mine12 = (TextView) findViewById(R.id.num_mine12);
                    TextView txt_num_mine13 = (TextView) findViewById(R.id.num_mine13);
                    TextView txt_num_mine14 = (TextView) findViewById(R.id.num_mine14);

                    TextView txt_num_mine21 = (TextView) findViewById(R.id.num_mine21);
                    TextView txt_num_mine22 = (TextView) findViewById(R.id.num_mine22);
                    TextView txt_num_mine23 = (TextView) findViewById(R.id.num_mine23);
                    TextView txt_num_mine24 = (TextView) findViewById(R.id.num_mine24);

                    TextView txt_num_mine31 = (TextView) findViewById(R.id.num_mine31);
                    TextView txt_num_mine32 = (TextView) findViewById(R.id.num_mine32);
                    TextView txt_num_mine33 = (TextView) findViewById(R.id.num_mine33);
                    TextView txt_num_mine34 = (TextView) findViewById(R.id.num_mine34);

                    TextView txt_num_rival11 = (TextView) findViewById(R.id.num_rival11);
                    TextView txt_num_rival12 = (TextView) findViewById(R.id.num_rival12);
                    TextView txt_num_rival13 = (TextView) findViewById(R.id.num_rival13);
                    TextView txt_num_rival14 = (TextView) findViewById(R.id.num_rival14);

                    TextView txt_num_rival21 = (TextView) findViewById(R.id.num_rival21);
                    TextView txt_num_rival22 = (TextView) findViewById(R.id.num_rival22);
                    TextView txt_num_rival23 = (TextView) findViewById(R.id.num_rival23);
                    TextView txt_num_rival24 = (TextView) findViewById(R.id.num_rival24);

                    TextView txt_num_rival31 = (TextView) findViewById(R.id.num_rival31);
                    TextView txt_num_rival32 = (TextView) findViewById(R.id.num_rival32);
                    TextView txt_num_rival33 = (TextView) findViewById(R.id.num_rival33);
                    TextView txt_num_rival34 = (TextView) findViewById(R.id.num_rival34);

                    TextView txt_title_mine_r1 = (TextView) findViewById(R.id.txt_mine_title_r1);
                    TextView txt_title_mine_r2 = (TextView) findViewById(R.id.txt_mine_title_r2);
                    TextView txt_title_mine_r3 = (TextView) findViewById(R.id.txt_mine_title_r3);

                    TextView txt_title_rival_r1 = (TextView) findViewById(R.id.txt_rival_title_r1);
                    TextView txt_title_rival_r2 = (TextView) findViewById(R.id.txt_rival_title_r2);
                    TextView txt_title_rival_r3 = (TextView) findViewById(R.id.txt_rival_title_r3);




                    ImageView img_mine11 = (ImageView) findViewById(R.id.img_mine1_1);
                    ImageView img_mine12 = (ImageView) findViewById(R.id.img_mine1_2);
                    ImageView img_mine13 = (ImageView) findViewById(R.id.img_mine1_3);
                    ImageView img_mine14 = (ImageView) findViewById(R.id.img_mine1_4);

                    ImageView img_mine21 = (ImageView) findViewById(R.id.img_mine2_1);
                    ImageView img_mine22 = (ImageView) findViewById(R.id.img_mine2_2);
                    ImageView img_mine23 = (ImageView) findViewById(R.id.img_mine2_3);
                    ImageView img_mine24 = (ImageView) findViewById(R.id.img_mine2_4);

                    ImageView img_mine31 = (ImageView) findViewById(R.id.img_mine3_1);
                    ImageView img_mine32 = (ImageView) findViewById(R.id.img_mine3_2);
                    ImageView img_mine33 = (ImageView) findViewById(R.id.img_mine3_3);
                    ImageView img_mine34 = (ImageView) findViewById(R.id.img_mine3_4);


                    ImageView img_rival11 = (ImageView) findViewById(R.id.img_other1_1);
                    ImageView img_rival12 = (ImageView) findViewById(R.id.img_other1_2);
                    ImageView img_rival13 = (ImageView) findViewById(R.id.img_other1_3);
                    ImageView img_rival14 = (ImageView) findViewById(R.id.img_other1_4);

                    ImageView img_rival21 = (ImageView) findViewById(R.id.img_other2_1);
                    ImageView img_rival22 = (ImageView) findViewById(R.id.img_other2_2);
                    ImageView img_rival23 = (ImageView) findViewById(R.id.img_other2_3);
                    ImageView img_rival24 = (ImageView) findViewById(R.id.img_other2_4);

                    ImageView img_rival31 = (ImageView) findViewById(R.id.img_other3_1);
                    ImageView img_rival32 = (ImageView) findViewById(R.id.img_other3_2);
                    ImageView img_rival33 = (ImageView) findViewById(R.id.img_other3_3);
                    ImageView img_rival34 = (ImageView) findViewById(R.id.img_other3_4);


                    start1 = ss.indexOf("<round_count_user>");
                    end1 = ss.indexOf("</round_count_user>");
                    String
                            round_count_user = ss.substring(start1 + 18, end1);
                    user_round_count = Integer.valueOf(round_count_user);

                    start1 = ss.indexOf("<round_count_rival>");
                    end1 = ss.indexOf("</round_count_rival>");
                    String
                            round_count_rival = ss.substring(start1 + 19, end1);
                    rival_round_coun = Integer.valueOf(round_count_rival);




                    start1 = ss.indexOf("<round_count_user_finished>");
                    end1 = ss.indexOf("</round_count_user_finished>");
                    String
                            round_count_user_finished = ss.substring(start1 + 27, end1);
                    user_round_count_finished = Integer.valueOf(round_count_user_finished);



                    start1 = ss.indexOf("<round_count_rival_finished>");
                    end1 = ss.indexOf("</round_count_rival_finished>");
                    String
                            round_count_rival_finished = ss.substring(start1 + 28, end1);
                    rival_round_count_finished = Integer.valueOf(round_count_rival_finished);




                    start1 = ss.indexOf("<g_id>");
                    end1 = ss.indexOf("</g_id>");
                    g_id = ss.substring(start1 + 6, end1);



                    start1 = ss.indexOf("<right_ans_user>");
                    end1 = ss.indexOf("</right_ans_user>");
                    String
                            right_ans_user = ss.substring(start1 + 16, end1);

                    start1 = ss.indexOf("<right_ans_rival>");
                    end1 = ss.indexOf("</right_ans_rival>");
                    String
                            right_ans_rival = ss.substring(start1 + 17, end1);

                    start1 = ss.indexOf("<turn_user>");
                    end1 = ss.indexOf("</turn_user>");
                    String
                            turn_user = ss.substring(start1 + 11, end1);

                    start1 = ss.indexOf("<turn_rival>");
                    end1 = ss.indexOf("</turn_rival>");
                    String
                            turn_rival = ss.substring(start1 + 12, end1);


                    start1 = ss.indexOf("<user_result>");
                    end1 = ss.indexOf("</user_result>");
                    String
                            user_result = ss.substring(start1 + 13, end1);

                    start1 = ss.indexOf("<rival_result>");
                    end1 = ss.indexOf("</rival_result>");
                    String
                            rival_result = ss.substring(start1 + 14, end1);

                    start1 = ss.indexOf("<rival_uname>");
                    end1 = ss.indexOf("</rival_uname>");
                    String
                            rival_uname = ss.substring(start1 + 13, end1);

                    start1 = ss.indexOf("<rival_avatar_id>");
                    end1 = ss.indexOf("</rival_avatar_id>");
                    String
                            rival_avatar_id = ss.substring(start1 + 17, end1);

                    start1 = ss.indexOf("<rival_gender>");
                    end1 = ss.indexOf("</rival_gender>");
                    String
                            rival_gender = ss.substring(start1 + 14, end1);


                    TextView txt_my_name = (TextView) findViewById(R.id.txt_your_name);
                    TextView txt_rival_name = (TextView) findViewById(R.id.txt_opponent_name);

                    txt_my_name.setText(fun.u_name);
                    txt_rival_name.setText(rival_uname);



                    TextView txt_score = (TextView) findViewById(R.id.txt_score);
                    txt_score.setText(right_ans_user+"-"+right_ans_rival);


                    user_nobat = Integer.valueOf(turn_user);
                    int max_level =0;
                    if(Integer.valueOf(round_count_rival)>=Integer.valueOf(round_count_user))
                    {
                        max_level = Integer.valueOf(round_count_rival);
                    }
                    else
                    {
                        max_level = Integer.valueOf(round_count_user);
                    }

                    LinearLayout lay_round1= (LinearLayout) findViewById(R.id.round1);
                    LinearLayout lay_round2= (LinearLayout) findViewById(R.id.round2);
                    LinearLayout lay_round3= (LinearLayout) findViewById(R.id.round3);

                    if(rival_round_count_finished==user_round_count_finished && rival_round_count_finished==1 )
                    {
                        max_level=2;
                    }
                    if(rival_round_count_finished==user_round_count_finished && rival_round_count_finished==2 )
                    {
                        max_level=3;
                    }
                    //Toast.makeText(getBaseContext(),String.valueOf(turn_user)+"--"+String.valueOf(turn_rival)+"***"+String.valueOf(max_level),Toast.LENGTH_SHORT).show();
                    lay_round1.setVisibility(View.VISIBLE);
                    lay_round3.setVisibility(View.VISIBLE);
                    lay_round2.setVisibility(View.VISIBLE);
                    if(max_level==1 )
                    {
                        lay_round2.setVisibility(View.INVISIBLE);
                        lay_round3.setVisibility(View.INVISIBLE);

                      // Toast.makeText(getBaseContext(),"ddd",Toast.LENGTH_SHORT).show();
                        set_round1(ss,Integer.valueOf(turn_user),Integer.valueOf(turn_rival));



                    }
                    else if(max_level==2 )
                    {

                        lay_round2.setVisibility(View.VISIBLE);
                        set_round1(ss,2,2);

                        if (round_count_rival.equals("1") && round_count_user.equals("1"))
                        {
                            img_mine21.setVisibility(View.INVISIBLE);
                            img_mine22.setVisibility(View.INVISIBLE);
                            img_mine23.setVisibility(View.INVISIBLE);
                            img_mine24.setVisibility(View.INVISIBLE);

                            img_rival21.setVisibility(View.INVISIBLE);
                            img_rival22.setVisibility(View.INVISIBLE);
                            img_rival23.setVisibility(View.INVISIBLE);
                            img_rival24.setVisibility(View.INVISIBLE);

                            txt_num_mine21.setVisibility(View.INVISIBLE);
                            txt_num_mine22.setVisibility(View.INVISIBLE);
                            txt_num_mine23.setVisibility(View.INVISIBLE);
                            txt_num_mine24.setVisibility(View.INVISIBLE);

                            txt_num_rival21.setVisibility(View.INVISIBLE);
                            txt_num_rival22.setVisibility(View.INVISIBLE);
                            txt_num_rival23.setVisibility(View.INVISIBLE);
                            txt_num_rival24.setVisibility(View.INVISIBLE);

                            if(turn_rival.equals("1") ) {
                                txt_title_mine_r2.setText("نوبت شما");
                                txt_title_mine_r2.setTextColor(Color.RED);
                            }
                            else
                            {
                                txt_title_rival_r2.setText("نوبت حریف");
                                txt_title_rival_r2.setTextColor(Color.RED);
                            }
                            TextView txt_title_round2= (TextView) findViewById(R.id.txt_title_round2);
                            txt_title_round2.setText("راند دوم - موضوع انتخاب نشده");


                        }








                        else
                        {
                            set_round2(ss,Integer.valueOf(turn_user),Integer.valueOf(turn_rival));
                        }






                        lay_round3.setVisibility(View.INVISIBLE);
                    }
                    else if((max_level==3  ))
                    {
                        lay_round3.setVisibility(View.VISIBLE);
                        lay_round2.setVisibility(View.VISIBLE);

                        set_round1(ss,2,2);
                        set_round2(ss,2,2);
                        if (round_count_rival_finished.equals("2") && round_count_user_finished.equals("2"))
                        {
                            img_mine31.setVisibility(View.INVISIBLE);
                            img_mine32.setVisibility(View.INVISIBLE);
                            img_mine33.setVisibility(View.INVISIBLE);
                            img_mine34.setVisibility(View.INVISIBLE);

                            img_rival31.setVisibility(View.INVISIBLE);
                            img_rival32.setVisibility(View.INVISIBLE);
                            img_rival33.setVisibility(View.INVISIBLE);
                            img_rival34.setVisibility(View.INVISIBLE);

                            txt_num_mine31.setVisibility(View.INVISIBLE);
                            txt_num_mine32.setVisibility(View.INVISIBLE);
                            txt_num_mine33.setVisibility(View.INVISIBLE);
                            txt_num_mine34.setVisibility(View.INVISIBLE);

                            txt_num_rival31.setVisibility(View.INVISIBLE);
                            txt_num_rival32.setVisibility(View.INVISIBLE);
                            txt_num_rival33.setVisibility(View.INVISIBLE);
                            txt_num_rival34.setVisibility(View.INVISIBLE);

                            if(turn_rival.equals("1") ) {
                                txt_title_mine_r3.setText("نوبت شما");
                                txt_title_mine_r3.setTextColor(Color.RED);
                            }
                            else
                            {
                                txt_title_rival_r3.setText("نوبت حریف");
                                txt_title_rival_r3.setTextColor(Color.RED);
                            }
                            TextView txt_title_round3= (TextView) findViewById(R.id.txt_title_round3);
                            txt_title_round3.setText("راند سوم - موضوع انتخاب نشده");

                        }
                        else
                        {
                            if(rival_round_count_finished==user_round_count_finished && rival_round_count_finished==3)
                            {
                                set_round3(ss,2,2);
                            }
                            else
                            {
                                set_round3(ss,Integer.valueOf(turn_user),Integer.valueOf(turn_rival));
                            }

                        }

                    }
                    if(max_level==0)
                    {
                        lay_round2.setVisibility(View.INVISIBLE);
                        lay_round3.setVisibility(View.INVISIBLE);


                        TextView txt_title_round1= (TextView) findViewById(R.id.txt_title_round1);
                        txt_title_round1.setText("راند اول - موضوع انتخاب نشده");

                        img_mine11.setVisibility(View.INVISIBLE);
                        img_mine12.setVisibility(View.INVISIBLE);
                        img_mine13.setVisibility(View.INVISIBLE);
                        img_mine14.setVisibility(View.INVISIBLE);
                        img_rival11.setVisibility(View.INVISIBLE);
                        img_rival12.setVisibility(View.INVISIBLE);
                        img_rival13.setVisibility(View.INVISIBLE);
                        img_rival14.setVisibility(View.INVISIBLE);

                        if(turn_user.equals("1")) {
                            txt_num_mine11.setVisibility(View.INVISIBLE);
                            txt_num_mine12.setVisibility(View.INVISIBLE);
                            txt_num_mine13.setVisibility(View.INVISIBLE);
                            txt_num_mine14.setVisibility(View.INVISIBLE);
                            txt_title_rival_r1.setText("نوبت حریف");
                            txt_title_mine_r1.setText("");
                        }
                        else {




                            txt_num_rival11.setVisibility(View.INVISIBLE);
                            txt_num_rival12.setVisibility(View.INVISIBLE);
                            txt_num_rival13.setVisibility(View.INVISIBLE);
                            txt_num_rival14.setVisibility(View.INVISIBLE);
                            txt_title_mine_r1.setText("نوبت شما");
                            txt_title_rival_r1.setText("");
                        }





                    }

                    if(Integer.valueOf(turn_user)==0)
                    {
                        TextView txt_start_game = (TextView) findViewById(R.id.txt_start_game);
                        txt_start_game.setText("شروع بازی");
                        RelativeLayout lay_start_new_game = (RelativeLayout) findViewById(R.id.btn_start_game);
                        lay_start_new_game.setBackground(getResources().getDrawable(R.drawable.green_btn));
                        LinearLayout lay_btn_start_game = (LinearLayout) findViewById(R.id.lay_btn_start_game);
                        lay_btn_start_game.setBackground(getResources().getDrawable(R.drawable.dark_green_btn));

                    }
                    else
                    {
                        TextView txt_start_game = (TextView) findViewById(R.id.txt_start_game);
                        txt_start_game.setText("نوبت حریف");
                        RelativeLayout lay_start_new_game = (RelativeLayout) findViewById(R.id.btn_start_game);
                        lay_start_new_game.setBackground(getResources().getDrawable(R.drawable.red_btn));
                        LinearLayout lay_btn_start_game = (LinearLayout) findViewById(R.id.lay_btn_start_game);
                        lay_btn_start_game.setBackground(getResources().getDrawable(R.drawable.dark_red_btn));
                    }
                    if(!user_result.equals("none") || !rival_result.equals("none"))
                    {
                        TextView txt_start_game = (TextView) findViewById(R.id.txt_start_game);
                        String
                                ended_game= "بازی تمام شده است";
                        int
                                right_ans_user_int = Integer.valueOf(right_ans_user);
                        int
                                right_ans_rival_int = Integer.valueOf(right_ans_rival);

                        if(user_result.equals("won"))
                        {
                            ended_game+=" برنده شدید ";
                        }
                        else if(user_result.equals("lost"))
                        {
                            ended_game+=" باختید ";
                        }
                        else
                        {
                            ended_game+=" مساوی شدید ";
                        }
                        txt_start_game.setText(ended_game);
                        txt_start_game.setTextColor(Color.BLACK);
                        RelativeLayout lay_start_new_game = (RelativeLayout) findViewById(R.id.btn_start_game);
                        lay_start_new_game.setBackground(getResources().getDrawable(R.drawable.light_grey_btn));
                        LinearLayout lay_btn_start_game = (LinearLayout) findViewById(R.id.lay_btn_start_game);
                        lay_btn_start_game.setBackground(getResources().getDrawable(R.drawable.btn_dark_grey));
                    }

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
                    image_url = "http://sputa-app.ir/app/trivia/pic/"+rival_uname+"_"+rival_avatar_id+".jpg";


                    Picasso.with(getBaseContext()).load(image_url).fit().into(image1);




                   // Toast.makeText(getBaseContext(),param_str,Toast.LENGTH_SHORT).show();
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
                                RelativeLayout.LayoutParams lp_coin = new RelativeLayout.LayoutParams((int) (screenWidth * .2), (int) (screenWidth * .2));
                                //lp_coin.setMargins(100,400,0,0);
                                lp_coin.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                                img_circle1.setLayoutParams(lp_coin);

                                x++;
                               // img_circle1.setRotation(x);
                                if(((int)x/100) > fun.request_time_out && is_requested)
                                {
                                    TextView lbl_message = (TextView) findViewById(R.id.lbl_message);
                                    //  lbl_message.setText("ارتباط قطع شد");
                                    lay_wait.setVisibility(View.GONE);
                                    retry_msg();
                                    is_requested=false;
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
