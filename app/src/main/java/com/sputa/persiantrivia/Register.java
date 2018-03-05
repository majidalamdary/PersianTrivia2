package com.sputa.persiantrivia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.view.View.OnFocusChangeListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Random;
import static com.sputa.persiantrivia.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.sputa.persiantrivia.CommonUtilities.EXTRA_MESSAGE;
import static com.sputa.persiantrivia.CommonUtilities.SENDER_ID;
import com.google.android.gcm.GCMRegistrar;

import ir.adad.client.Adad;

public class Register extends AppCompatActivity  implements OnFocusChangeListener{
    int       screenWidth  = 0;
    public    RelativeLayout lay_wait;
    int       screenHeight = 0;
    boolean   is_registering = true;



    public   ImageView img_circle1;
    public   MyAsyncTask mm;

    int      x=1;
    Boolean
             is_requested = false;
    String
             font_name = "";
    Timer    tim_circulation;
    Boolean  is_rotation = true;
    Typeface tf;
    TextView lbl_msg ;
    functions fun;


    boolean userTouchedView;
    TextView lbl_hint ;
    EditText txt_pas;
    EditText txt;
    @Override
    public void onFocusChange(View v, boolean hasFocus) {


        if (hasFocus && !userTouchedView) {


        }
        else if(!hasFocus) {

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Adad.initialize(getApplicationContext());

        setContentView(R.layout.activity_register);
        fun = new functions();
        SharedPreferences  settings1 = getApplicationContext().getSharedPreferences("homeScore", 0);
        String homeScore = settings1.getString("homeScore", "");
       // Toast.makeText(getBaseContext(),homeScore,Toast.LENGTH_SHORT).show();



        if(!homeScore.equals(""))
        {
            functions.u_name = homeScore;
            Intent i = new Intent(Register.this,MainActivity.class);
            startActivity(i);
            finish();
        }

        lbl_msg = (TextView) findViewById(R.id.lbl_message);
        fun = new functions();
        font_name = fun.font_name;
        tf = Typeface.createFromAsset(getAssets(),font_name );
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        lay_wait = (RelativeLayout) findViewById(R.id.lay_wait);
        final EditText txt_uname = (EditText) findViewById(R.id.txt_uname);
        final EditText txt_pass  = (EditText) findViewById(R.id.txt_pass);
        final EditText txt_mobile  = (EditText) findViewById(R.id.txt_mobile);
        img_circle1 = (ImageView) findViewById(R.id.circle1);
        tim_circulation       = new Timer("circulation");
        tim_circulation.start();


         txt= (EditText) findViewById(R.id.txt_uname);
        txt_pas= (EditText) findViewById(R.id.txt_pass);
        txt.setOnFocusChangeListener(this);
        txt_pass.setOnFocusChangeListener(this);

        txt.setFilters(new InputFilter[] {
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start, int end, Spanned dst, int dstart, int dend) {
                        if(src.equals("")){ // for backspace
                            return src;
                        }
                        if(src.toString().matches("[a-zA-Z 0-9 _]+")){
                            return src;
                        }
                        return "";
                    }
                }
        });
        txt_pas.setFilters(new InputFilter[] {
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start, int end, Spanned dst, int dstart, int dend) {
                        if(src.equals("")){ // for backspace
                            return src;
                        }
                        if(src.toString().matches("[a-zA-Z 0-9 _]+")){
                            return src;
                        }
                        return "";
                    }
                }
        });

        set_content_size();
    }
    boolean
        is_pass_cleared = false,is_uname_cleared = false,is_mobile_cleared = false;



    public void clk_register(View view)
    {
        LinearLayout lay_register_fields = (LinearLayout) findViewById(R.id.lay_register_field);
        TextView lbl_register = (TextView) findViewById(R.id.lbl_register);
        TextView lbl_already_register = (TextView) findViewById(R.id.lbl_already_registered);
        TextView lbl_login = (TextView) findViewById(R.id.lbl_login);
        TextView lbl_title = (TextView) findViewById(R.id.lbl_title);
        EditText txt_uname = (EditText) findViewById(R.id.txt_uname);
        EditText txt_pass = (EditText) findViewById(R.id.txt_pass);

        LinearLayout lay_uname_pic = (LinearLayout) findViewById(R.id.lay_uname_pic);
        LinearLayout lay_pass_pic = (LinearLayout) findViewById(R.id.lay_pass_pic);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        lbl_msg.setText("");
        if(!is_registering)
        {
            //lay_uname_pic.setLayoutParams(new TableLayout.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT, 0.5f));
            LinearLayout.LayoutParams loparams = (LinearLayout.LayoutParams) lay_uname_pic.getLayoutParams();
 //           loparams.weight = 1.5f;
            lay_uname_pic.setLayoutParams(loparams);
            lay_pass_pic.setLayoutParams(loparams);
            lay_register_fields.setVisibility(View.VISIBLE);
            lbl_register.setText("از اینجا وارد شوید");
            lbl_already_register.setText("آیا قبلا عضو شده اید؟");
            lbl_login.setText("برو بریم");
            lbl_title.setText("ثبت نام");
            is_registering = true;
            LinearLayout.LayoutParams lp_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            txt_uname.setLayoutParams(lp_params);
            txt_pass.setLayoutParams(lp_params);

        }
        else
        {
            LinearLayout.LayoutParams loparams = (LinearLayout.LayoutParams) lay_uname_pic.getLayoutParams();
 //           loparams.weight = 2f;
            lay_uname_pic.setLayoutParams(loparams);
            lay_pass_pic.setLayoutParams(loparams);
            lay_register_fields.setVisibility(View.GONE);
            lbl_register.setText("از اینجا ثبت نام کنید");
            lbl_already_register.setText("آیا قبلا عضو نشده اید؟");
            lbl_login.setText("برو بریم");

            LinearLayout.LayoutParams lp_params = (LinearLayout.LayoutParams) txt_uname.getLayoutParams();
            lp_params.height = (int)(screenHeight*.09);
            txt_uname.setLayoutParams(lp_params);
            txt_pass.setLayoutParams(lp_params);


            lbl_title.setText("ورود به بازی");


            is_registering = false;
        }
    }
    public void clk_login(View view)
    {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        EditText txt_uname = (EditText) findViewById(R.id.txt_uname);
        EditText txt_pass = (EditText) findViewById(R.id.txt_pass);
        EditText txt_mobile = (EditText) findViewById(R.id.txt_mobile);
        RadioButton rdb_boy = (RadioButton) findViewById(R.id.rdb_boy);

        int
                gender =0;
        if(rdb_boy.isChecked())
            gender = 1;
        else
            gender = 2;
        Boolean
                allow_send= true;
        if(is_registering) {
            //Toast.makeText(getBaseContext(),String.valueOf(txt_mobile.getText().subSequence(0,2)),Toast.LENGTH_SHORT).show();
            int
            flag=0;
            for(int i=0;i<txt_uname.getText().toString().length();i++)
            {
                if(txt_uname.getText().toString().charAt(i)== ' ')
                {
                    flag=1;
                }
            }
            if(flag==1)
            {
                lbl_msg.setText("از خط فاصله در نام کاربری استفاده نکنید");
                allow_send = false;
            }
            else if(txt_uname.getText().length()<3)
            {
                lbl_msg.setText("نام کاربری باید حداقل 3 کاراکتر باشد");
                allow_send = false;
            }
            else if(txt_pass.getText().length()<5)
            {
                lbl_msg.setText(" رمز عبور باید حداقل 5 کاراکتر باشد");
                allow_send = false;
            }
            else if(txt_mobile.getText().length() !=11)
            {
                lbl_msg.setText("شماره موبایل باید 11 رقم باشد");
                allow_send = false;
            }
            else if( !txt_mobile.getText().toString().substring(0, 2).equals("09"))
            {
                lbl_msg.setText("شماره موبایل باید با 09 شروع شود");
                allow_send = false;
            }

        }
        if(!is_registering) {
            //Toast.makeText(getBaseContext(),String.valueOf(txt_mobile.getText().subSequence(0,2)),Toast.LENGTH_SHORT).show();
            int
                    flag=0;
            for(int i=0;i<txt_uname.getText().toString().length();i++)
            {
                if(txt_uname.getText().toString().charAt(i)== ' ')
                {
                    flag=1;
                }
            }
            if(flag==1)
            {
                lbl_msg.setText("از خط فاصله در نام کاربری استفاده نکنید");
                allow_send = false;
            }
            else if(txt_uname.getText().length()<3)
            {
                lbl_msg.setText("نام کاربری باید حداقل 3 کاراکتر باشد");
                allow_send = false;
            }
            else if(txt_pass.getText().length()<5)
            {
                lbl_msg.setText(" رمز عبور باید حداقل 5 کاراکتر باشد");
                allow_send = false;
            }
        }
        if(allow_send) {
            mm = new MyAsyncTask();
            {

                lay_wait.setVisibility(View.VISIBLE);
                x = 1;

                String
                        param = "";
                if (is_registering)
                    param = "register";
                else
                    param = "login";

                String
                        uname = "";
                LinearLayout lay_main = (LinearLayout) findViewById(R.id.lay_main);
                fun.enableDisableView(lay_main,false);
                mm.url = (getResources().getString(R.string.site_url) + "register.php?param=" + param + "&uname=" + URLEncoder.encode(txt_uname.getText().toString()) + "&pass=" + URLEncoder.encode(txt_pass.getText().toString()) + "&mobile=" + URLEncoder.encode(txt_mobile.getText().toString()) + "&gender=" + String.valueOf(gender)+"&rnd="+String.valueOf(new Random().nextInt()));
               // Toast.makeText(this,getResources().getString(R.string.site_url) + "register.php?param=" + param + "&uname=" + URLEncoder.encode(txt_uname.getText().toString()) + "&pass=" + URLEncoder.encode(txt_pass.getText().toString()) + "&mobile=" + URLEncoder.encode(txt_mobile.getText().toString()) + "&gender=" + String.valueOf(gender),Toast.LENGTH_LONG).show();
                mm.execute("");
                is_requested = true;
                lbl_msg.setText("");
            }
        }
    }
    public void clk_exit(View view)
    {
        finish();
    }



    public  void clk_boy(View v)
    {
        RadioButton rdb_boy = (RadioButton) findViewById(R.id.rdb_boy);
        RadioButton rdb_girl = (RadioButton) findViewById(R.id.rdb_girl);
        rdb_boy.setChecked(true);
        rdb_girl.setChecked(false);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
    public  void clk_girl(View v)
    {
        RadioButton rdb_boy = (RadioButton) findViewById(R.id.rdb_boy);
        RadioButton rdb_girl = (RadioButton) findViewById(R.id.rdb_girl);
        rdb_boy.setChecked(false);
        rdb_girl.setChecked(true);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

    }



    private void set_content_size()
    {

//        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        TextView lbl_title = (TextView) findViewById(R.id.lbl_title);
        lbl_title.setTypeface(tf);
        lbl_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));


        TextView lbl_username = (TextView) findViewById(R.id.lbl_username);
        lbl_username.setTypeface(tf);
        lbl_username.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.046));

        TextView lbl_pass = (TextView) findViewById(R.id.lbl_pass);
        lbl_pass.setTypeface(tf);
        lbl_pass.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.046));

        TextView lbl_mobile = (TextView) findViewById(R.id.lbl_mobile);
        lbl_mobile.setTypeface(tf);
        lbl_mobile.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.046));

        TextView lbl_jensiyat = (TextView) findViewById(R.id.lbl_jensiyat);
        lbl_jensiyat.setTypeface(tf);
        lbl_jensiyat.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.049));

        TextView txt_user_hint = (TextView) findViewById(R.id.txt_user_hint);
        txt_user_hint.setTypeface(tf);
        txt_user_hint.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.021));

        TextView txt_pass_hint = (TextView) findViewById(R.id.txt_pass_hint);
        txt_pass_hint.setTypeface(tf);
        txt_pass_hint.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.021));


        TextView lbl_title_new = (TextView) findViewById(R.id.lbl_title_new);
        lbl_title_new.setTypeface(tf);
        lbl_title_new.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));


        EditText txt_uname = (EditText) findViewById(R.id.txt_uname);
        txt_uname.setTypeface(tf);
        txt_uname.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.042));

        EditText txt_pass = (EditText) findViewById(R.id.txt_pass);
        txt_pass.setTypeface(tf);
        txt_pass.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.042));

        EditText txt_mobile = (EditText) findViewById(R.id.txt_mobile);
        txt_mobile.setTypeface(tf);
        txt_mobile.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.042));

        RadioButton rdb_boy = (RadioButton) findViewById(R.id.rdb_boy);
        rdb_boy.setTypeface(tf);
        rdb_boy.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));

        RadioButton rdb_girl = (RadioButton) findViewById(R.id.rdb_girl);
        rdb_girl.setTypeface(tf);
        rdb_girl.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));


        TextView lbl_login = (TextView) findViewById(R.id.lbl_login);
        lbl_login.setTypeface(tf);
        lbl_login.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.042));

        TextView lbl_exit = (TextView) findViewById(R.id.lbl_exit);
        lbl_exit.setTypeface(tf);
        lbl_exit.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.042));

        LinearLayout laybtn_login = (LinearLayout) findViewById(R.id.laybtn_login);
        //LinearLayout.LayoutParams lp_lay_tabBar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)(screenHeight*0.1));
        LinearLayout.LayoutParams lp_laybtn_login = new LinearLayout.LayoutParams((int)(screenWidth*0.3),(int)(screenHeight*0.083));
        laybtn_login.setLayoutParams(lp_laybtn_login);


        LinearLayout laybtn_exit = (LinearLayout) findViewById(R.id.laybtn_exit);

        laybtn_exit.setLayoutParams(lp_laybtn_login);

        TextView lbl_already_registered = (TextView) findViewById(R.id.lbl_already_registered);
        lbl_already_registered.setTypeface(tf);
        lbl_already_registered.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.042));

        TextView lbl_register = (TextView) findViewById(R.id.lbl_register);
        lbl_register.setTypeface(tf);
        lbl_register.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.042));

        TextView lbl_message = (TextView) findViewById(R.id.lbl_message);
        lbl_message.setTypeface(tf);
        lbl_message.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.052));


    }


    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        //    String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping
            //WakeLocker.acquire(getApplicationContext());

            /**
             * Take appropriate action on this message
             * depending upon your app requirement
             * For now i am just displaying it on the screen
             * */
           // wake
            // Showing received message
            //lblMessage.append(newMessage + "\n");
   //         Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();

            // Releasing  lock
            //  WakeLocker.release();
        }
    };

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
                                if(((int)x/100) >6 && is_requested)
                                {
                                    TextView lbl_message = (TextView) findViewById(R.id.lbl_message);
                                    LinearLayout lay_main = (LinearLayout) findViewById(R.id.lay_main);
                                    fun.enableDisableView(lay_main,true);
                                    lbl_message.setText("ارتباط قطع شد");
                                    lay_wait.setVisibility(View.GONE);
                                    is_requested=false;

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
            TextView lbl_message1 = (TextView) findViewById(R.id.lbl_message);
            lbl_message1.setText("");
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
                    if (param_str.equals("register")) {
                        LinearLayout lay_main = (LinearLayout) findViewById(R.id.lay_main);
                        fun.enableDisableView(lay_main,true);
                        if (output_str.equals("tekrari")) {
                            TextView lbl_message = (TextView) findViewById(R.id.lbl_message);
                            lbl_message.setText("این کاربر قبلا ثبت نام کرده است");
                        }
                        if (output_str.equals("registered")) {

                            TextView lbl_message = (TextView) findViewById(R.id.lbl_message);
                            lbl_message.setText("ثبت نام انجام شد");
                            EditText txt_uname = (EditText) findViewById(R.id.txt_uname);
                            functions.u_name = txt_uname.getText().toString();
                            functions.reg_id = "0";
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Do something after 5s = 5000ms
                                    Intent i = new Intent(Register.this, MainActivity.class);
                                     finish();
                                    startActivity(i);
                                }
                            }, 1000);
                        }
                        lay_wait.setVisibility(View.GONE);
                        is_requested = false;
                    }
                    if (param_str.equals("login")) {
                        LinearLayout lay_main = (LinearLayout) findViewById(R.id.lay_main);
                        fun.enableDisableView(lay_main,true);
                        if (output_str.substring(0,5).equals("exist")) {
                            int
                                    k = output_str.indexOf(",");
                            String
                                    reg_id= output_str.substring(6,output_str.length());
                            //lbl_msg.setText(reg_id);
                            functions.reg_id = reg_id;
                            lbl_msg.setText("در حال ورود");
                            EditText txt_uname = (EditText) findViewById(R.id.txt_uname);
                            functions.u_name = txt_uname.getText().toString();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Do something after 5s = 5000ms
                                    Intent i = new Intent(Register.this, MainActivity.class);
                                    finish();
                                    startActivity(i);
                                }
                            }, 1000);

                        }
                        if (output_str.equals("not_exist")) {
                            lbl_msg.setText("کاربر پیدا نشد");
                        }
                        lay_wait.setVisibility(View.GONE);
                        is_requested = false;
                    }


                    if (param_str.equals("register_gcm")) {
                        LinearLayout lay_main = (LinearLayout) findViewById(R.id.lay_main);
                        fun.enableDisableView(lay_main,true);
                        if (output_str.equals("updated")) {
                            lbl_msg.setText("آپدیت شد");
                            GCMRegistrar.setRegisteredOnServer(getBaseContext(), true);
                        }
                        lay_wait.setVisibility(View.GONE);
                        is_requested = false;
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
