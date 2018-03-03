package com.sputa.persiantrivia;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
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

import com.android.vending.billing.IInAppBillingService;
import com.sputa.persiantrivia.util.IabHelper;
import com.sputa.persiantrivia.util.IabResult;
import com.sputa.persiantrivia.util.Inventory;
import com.sputa.persiantrivia.util.Purchase;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Store extends AppCompatActivity {


    int screenWidth  = 0;
    int screenHeight = 0;

    String
            coint_count="";
    String
            coint_count1="";
    String
            coint_count2="";
    String
            coint_count3="";
    String
            money1="";
    String
            money2="";
    String
            money3="";

    String
        sku="";

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

    // Debug tag, for logging
    static final String TAG = "majid";

    // SKUs for our products: the premium upgrade (non-consumable)
    static final String SKU_PREMIUM = "";

    // Does the user have the premium upgrade?
    boolean mIsPremium = false;

    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST =2 ;

    // The helper object
    IabHelper mHelper;

    IInAppBillingService mService;

    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mServiceConn != null) {
            unbindService(mServiceConn);
        }
    }

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");
            if (result.isFailure()) {
                Log.d(TAG, "Failed to query inventory: " + result);
                return;
            }
            else {
                Log.d(TAG, "Query inventory was successful.");
                // does the user have the premium upgrade?
                mIsPremium = inventory.hasPurchase(SKU_PREMIUM);

                // update UI accordingly

                Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));
            }

            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };
    String
            selected_coin_count="";
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (result.isFailure()) {
                Log.d(TAG, "Error purchasing: " + result);
                return;
            }
            else if (purchase.getSku().equals(sku)) {
                // give user access to premium content and update the UI
               // mHelper.queryInventoryAsync(mGotInventoryListener1);

                purchase1=purchase;



                last_requested_query=getResources().getString(R.string.site_url) + "do.php?param=consume_coin&uname="+fun.u_name+"&coin_count="+selected_coin_count+"&rnd="+String.valueOf(new Random().nextInt());
                run_last_query();
                Log.d(majid,"lop okeyde");
            }
        }
    };

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            if (result.isSuccess()) {
                // provision the in-app purchase to the user
                // (for example, credit 50 gold coins to player's character)
                Log.d(majid,"masraf olde");
                coint_count = String.valueOf(Integer.valueOf(coint_count)+Integer.valueOf(selected_coin_count));

                Toast.makeText(getBaseContext(),"تعداد "+selected_coin_count +" سکه به سکه های شما اضافه شد ",Toast.LENGTH_LONG).show();
                TextView txt_your_coin = (TextView) findViewById(R.id.txt_your_coin);
                txt_your_coin.setText("سکه های شما : "+coint_count);
            }
            else {
                // handle error
            }
        }
    };


    IabHelper.QueryInventoryFinishedListener mGotInventoryListener1 = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {

            if (result.isFailure()) {
                // handle error here
            }
            else {
                Log.d(majid,"lop okeyde");
                //mHelper.consumeAsync(inventory.getPurchase(sku), mConsumeFinishedListener);
            }
        }
    };
    Purchase purchase1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_store);



        ////////////////////////////////////bazar
        Intent serviceIntent = new Intent("ir.cafebazaar.pardakht.InAppBillingService.BIND");
        serviceIntent.setPackage("com.farsitel.bazaar");

        ////////////////////////////////////myket
//        Intent serviceIntent = new Intent("ir.mservices.market.InAppBillingService.BIND");
//        serviceIntent.setPackage("ir.mservices.market");


        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);


        //////////////////////////////////bazar
        String base64EncodedPublicKey="MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwCzXztGRTIK4LUhcL3Vh+sVhdEtFkIsaFDKFqozR2DxxWcaZiQTkrCcFrZDONTv1osHEbKZ3wpJ9uhqGzcgSP4oKQZjK+GDAtdlLYIGVelY6IqnjO3JDwQlNXDBCMDtt6gYxlcN+I0uac5Qzh7ZsWI+OQLTKf96QBCqjiy01gxj4YeemRO2A2MKWq/rwJHVnaPuP//+i9KQIYjO/P9WgV0CqnWYWfQEkuVnieHfp5sCAwEAAQ==";


//        ////////////////////////////////////myket
//        String base64EncodedPublicKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnVJeNQGy4ZeBOgGSvHYVPtVTesBz+VRoo9eWaVldabySG0XWghlWill+05D1EtWCo4ue1Yb+QVsOkaWEflcqJYIzcxOqSfvXCy1aEzGbBIEdXJU4G8GHMWneWPim6hYkfnKh17TY99IyrKaEFasS0BJJDZGNfM9dOUEMqgd4E/wIDAQAB";





        // compute your public key and store it in base64EncodedPublicKey
        mHelper = new IabHelper(this, base64EncodedPublicKey);



        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {

                    Log.d(TAG, "Problem setting up In-app Billing: " + result);
                }
            }
        });



        //lbl_msg = (TextView) findViewById(R.id.lbl_message);
        fun = new functions();
        font_name = fun.font_name;
        tf = Typeface.createFromAsset(getAssets(), font_name);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        lay_wait = (RelativeLayout) findViewById(R.id.lay_wait);

        img_circle1 = (ImageView) findViewById(R.id.circle1);
        tim_circulation = new Timer("circulation");
        tim_circulation.start();


        get_store();

        set_content_size();


    }
    private void get_store()
    {
        lay_wait.setVisibility(View.VISIBLE);
        x = 1;
        is_requested = true;
        mm = new MyAsyncTask();

        last_requested_query=getResources().getString(R.string.site_url) + "do.php?param=get_store&uname="+fun.u_name+"&rnd="+String.valueOf(new Random().nextInt());
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

//        LinearLayout laybtn_subject1 = (LinearLayout) findViewById(R.id.btn_subject1);
//        //LinearLayout.LayoutParams lp_lay_tabBar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)(screenHeight*0.1));
//        LinearLayout.LayoutParams lp_laybtn_subject1 = new LinearLayout.LayoutParams((int)(screenWidth*0.8),(int)(screenHeight*0.103));
//        laybtn_subject1.setLayoutParams(lp_laybtn_subject1);
//
//
//        LinearLayout laybtn_subject2 = (LinearLayout) findViewById(R.id.btn_subject2);
//        laybtn_subject2.setLayoutParams(lp_laybtn_subject1);
//
//        LinearLayout laybtn_subject3 = (LinearLayout) findViewById(R.id.btn_subject3);
//        laybtn_subject3.setLayoutParams(lp_laybtn_subject1);
//
//
//        TextView txt_coin_count1 = (TextView) findViewById(R.id.txt_coint_count1);
//        txt_coin_count1.setTypeface(tf);
//        txt_coin_count1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.062));
//
//        TextView txt_coin_count2 = (TextView) findViewById(R.id.txt_coint_count2);
//        txt_coin_count2.setTypeface(tf);
//        txt_coin_count2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.062));
//
//        TextView txt_coin_count3 = (TextView) findViewById(R.id.txt_coint_count3);
//        txt_coin_count3.setTypeface(tf);
//        txt_coin_count3.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.062));
//
//
//        TextView txt_money1 = (TextView) findViewById(R.id.txt_money1);
//        txt_money1.setTypeface(tf);
//        txt_money1.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.062));
//
//        TextView txt_money2 = (TextView) findViewById(R.id.txt_money2);
//        txt_money2.setTypeface(tf);
//        txt_money2.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.062));
//
//        TextView txt_money3 = (TextView) findViewById(R.id.txt_money3);
//        txt_money3.setTypeface(tf);
//        txt_money3.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.062));
//
//        TextView txt_your_coin = (TextView) findViewById(R.id.txt_your_coin);
//        txt_your_coin.setTypeface(tf);
//        txt_your_coin.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.062));
    }

    public void clk_subject1(View view) {

        sku="500coin";
        //sku="coin1";

        selected_coin_count=coint_count1;
        try {
            mHelper.launchPurchaseFlow(this, sku, 10001, mPurchaseFinishedListener, "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
            Log.d("majid","purchasssssss");
        } catch (Exception e) {
            Log.d("majid",e.getMessage());


            e.printStackTrace();
        }
    }

    public void clk_subject2(View view) {

        sku="coin1000";
        //sku="coin2";
        selected_coin_count=coint_count2;
        try {
            mHelper.launchPurchaseFlow(this, sku, 10001, mPurchaseFinishedListener, "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
            Log.d("majid","purchasssssss");
        } catch (Exception e) {
            Log.d("majid",e.getMessage());


            e.printStackTrace();
        }

    }

    public void clk_subject3(View view) {

        sku="coin2000";
        //sku="coin3";
        selected_coin_count=coint_count3;
        try {
            mHelper.launchPurchaseFlow(this, sku, 10001, mPurchaseFinishedListener, "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
            Log.d("majid","purchasssssss");
        } catch (Exception e) {
            Log.d("majid",e.getMessage());


            e.printStackTrace();
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
                if ((param_str.equals("consume_coin") ) && is_requested) {
                    //Toast.makeText(getBaseContext(),ss,Toast.LENGTH_SHORT).show();
                    LinearLayout lay_main = (LinearLayout) findViewById(R.id.lay_main);
                    fun.enableDisableView(lay_main,true);
                    lay_wait.setVisibility(View.GONE);
                    is_requested = false;
                    start1 = ss.indexOf("<result>");
                    end1 = ss.indexOf("</result>");
                    String
                            result1= ss.substring(start1 + 8, end1);
                        if(result1.equals("ok")) {
                            mHelper.consumeAsync(purchase1, mConsumeFinishedListener);
                        }

                    }



                    if ((param_str.equals("get_store") ) && is_requested) {
                    //Toast.makeText(getBaseContext(),ss,Toast.LENGTH_SHORT).show();


//                        start1 = ss.indexOf("<coin_count>");
//                        end1 = ss.indexOf("</coin_count>");
//                        coint_count= ss.substring(start1 + 12, end1);
//
//                    start1 = ss.indexOf("<coin_count1>");
//                    end1 = ss.indexOf("</coin_count1>");
//                    coint_count1= ss.substring(start1 + 13, end1);
//
//                    start1 = ss.indexOf("<coin_count2>");
//                    end1 = ss.indexOf("</coin_count2>");
//                    coint_count2= ss.substring(start1 + 13, end1);
//
//                    start1 = ss.indexOf("<coin_count3>");
//                    end1 = ss.indexOf("</coin_count3>");
//                    coint_count3= ss.substring(start1 + 13, end1);
//
//                    start1 = ss.indexOf("<money1>");
//                    end1 = ss.indexOf("</money1>");
//                    money1= ss.substring(start1 + 8, end1);
//
//                    start1 = ss.indexOf("<money2>");
//                    end1 = ss.indexOf("</money2>");
//                    money2= ss.substring(start1 + 8, end1);
//
//                    start1 = ss.indexOf("<money3>");
//                    end1 = ss.indexOf("</money3>");
//                    money3= ss.substring(start1 + 8, end1);
//
//
//
//
//                    TextView txt_coint_count1 = (TextView) findViewById(R.id.txt_coint_count1);
//                    TextView txt_coint_count2 = (TextView) findViewById(R.id.txt_coint_count2);
//                    TextView txt_coint_count3 = (TextView) findViewById(R.id.txt_coint_count3);
//
//                    TextView txt_money1 = (TextView) findViewById(R.id.txt_money1);
//                    TextView txt_money2 = (TextView) findViewById(R.id.txt_money2);
//                    TextView txt_money3 = (TextView) findViewById(R.id.txt_money3);
//
//                    txt_coint_count1.setText(coint_count1+"سکه");
//                    txt_coint_count2.setText(coint_count2+"سکه");
//                    txt_coint_count3.setText(coint_count3+"سکه");
//
//                    txt_money1.setText(money1+"تومان");
//                    txt_money2.setText(money2+"تومان");
//                    txt_money3.setText(money3+"تومان");
//
//
//                        TextView txt_your_coin = (TextView) findViewById(R.id.txt_your_coin);
//                    txt_your_coin.setText("سکه های شما : "+coint_count);
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
