package com.sputa.persiantrivia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.Random;

public class Game_list extends AppCompatActivity {
    private String array_spinner[];
    ListView lv;
    Context context;



    ArrayList prgmName;



    public static String [] user_name;
    public static String [] user_desc;

    public static String [] user_level;
    public static String [] uig_id;
    public static String [] user_gender;
    public static String [] user_avatar_id;
    public static String [] game_result;

    int screenWidth  = 0;
    int screenHeight = 0;



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



    public String search_type ="";


    String
        list_type="";


    boolean
            no_connection =false;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        search_type= "search_friend_list";

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

        set_content();




        list_type="wait";

        Intent intent = getIntent();
        list_type = intent.getStringExtra("list_type");
        context=this;

        lv=(ListView) findViewById(R.id.listView);

        mydatabase = openOrCreateDatabase("aa", MODE_PRIVATE, null);

        // Cursor resultSet = mydatabase.rawQuery("Select * from " + "dd", null)
        //if(search_type.equals("search_friend_list")) {

        //}

        if(list_type.equals("wait"))
        {
            search_type="get_games_list&typ=wait";
        }
        if(list_type.equals("turn"))
        {
            search_type="get_games_list&typ=turn";
        }
        if(list_type.equals("done"))
        {
            search_type="get_games_list&typ=done";
        }
        get_friend_list("*");

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
    private void get_friend_list(String search_phrase)
    {
        lay_wait.setVisibility(View.VISIBLE);
        x = 1;
        is_requested = true;
        mm = new MyAsyncTask();
        last_requested_query=getResources().getString(R.string.site_url) + "do.php?param="+search_type+"&uname="+functions.u_name+"&rnd="+String.valueOf(new Random().nextInt());;
       // Toast.makeText(getBaseContext(),last_requested_query,Toast.LENGTH_LONG).show();
        mm.url = (last_requested_query);
        mm.execute("");

    }
    private void set_content()
    {
        LinearLayout laybtn_login = (LinearLayout) findViewById(R.id.top_lay);
        //LinearLayout.LayoutParams lp_lay_tabBar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)(screenHeight*0.1));
        LinearLayout.LayoutParams lp_laybtn_login = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(int)(screenHeight*0.08));
        laybtn_login.setLayoutParams(lp_laybtn_login);





        TextView txt_level = (TextView) findViewById(R.id.lbl_start_game);
        txt_level.setTypeface(tf);
        txt_level.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.05));


//        TextView txt_friend_name = (TextView) findViewById(R.id.lbl_friend_name);
//        txt_friend_name.setTypeface(tf);
//        txt_friend_name.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.06));




    }

    public void clk_search(View view) {
        TextView txt_search = (TextView) findViewById(R.id.txt_search);
        if(search_type.equals("search_friend_list")) {
            if(txt_search.getText().toString().equals(""))
                get_friend_list("*");
            else
                get_friend_list(txt_search.getText().toString());
        }
        else
        {
            get_friend_list(txt_search.getText().toString());
        }



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

                if (param_str.equals("get_games_list") && is_requested) {
                    //    Toast.makeText(getBaseContext(),ss,Toast.LENGTH_LONG).show();
                    lay_wait.setVisibility(View.GONE);
                    is_requested = false;
                    if(ss.length()>13)


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
                            for(int i=0;i<cnt;i++) {

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
                                        udesc = "زمان باقی مانده : "+r_hour+" ساعت و "+r_min+"دقیقه";
                                if (list_type.equals("done")) {
                                    user_desc[i]="تمام شده";

                                    int
                                            int_user_right_ans = 0;
                                    int
                                            int_rival_right_ans = 0;
                                    try {

                                        int_rival_right_ans = Integer.valueOf(rival_right_ans);
                                    }catch (Exception ee){

                                    }

                                    try {
                                        int_user_right_ans = Integer.valueOf(user_right_ans);

                                    }catch (Exception ee){

                                    }


                                    game_result[i]=game_result1;
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


                                }
                                else {
                                    user_desc[i] = udesc;
                                }
                            }
                            lv.setVisibility(View.VISIBLE);
                            Activity act;

                            lv.setAdapter(new Game_adapter(Game_list.this, user_name, user_desc, user_level,uig_id, user_gender, user_avatar_id,game_result, mydatabase,list_type));
                        }
                    }
                    else
                    {
                        lv.setVisibility(View.GONE);
//                        TextView txt_default = (TextView) findViewById(R.id.txt_default);
//                        txt_default.setVisibility(View.VISIBLE);
//                        txt_default.setText("موردی یافت نشد");
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
                                RelativeLayout.LayoutParams lp_coin = new RelativeLayout.LayoutParams((int) (screenWidth * .2), (int) (screenWidth * .2));
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
