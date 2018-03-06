package com.sputa.persiantrivia;

import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import com.android.internal.http.multipart.MultipartEntity;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class Setting extends AppCompatActivity {


     String uploadFilePath = "/mnt/sdcard/";
     String uploadFileName = "majid.png";

    int serverResponseCode = 0;
    ProgressDialog dialog = null;

    String upLoadServerUri = null;

    int screenWidth  = 0;
    int screenHeight = 0;
    ImageView img_avatar;

    SQLiteDatabase mydatabase;


    TextView txt_wait_message;


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
    public MyAsyncTask1 mm1;
    public LinearLayout lay_main;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        txt_wait_message= (TextView) findViewById(R.id.txt_wait_message);
        lay_main = (LinearLayout) findViewById(R.id.lay_main);
        //et_guild_name = (EditText) findViewById(R.id.txt_guild_name);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        fun = new functions();



        font_name = fun.font_name;
        tf = Typeface.createFromAsset(getAssets(),font_name );

        img_avatar= (ImageView) findViewById(R.id.img_avatar);

        if(functions.gender.equals("boy"))
        {
            img_avatar.setBackground(getResources().getDrawable(R.drawable.profile_mail));

        }
        else
        {
            img_avatar.setBackground(getResources().getDrawable(R.drawable.profile_female));

        }


//        String image_url = "http://sputa-app.ir/app/trivia/pic/"+functions.avatar_name;
//
//        // Toast.makeText(getBaseContext(),image_url,Toast.LENGTH_LONG).show();
//        Picasso.with(getBaseContext()).load(image_url).fit().into(img_avatar);

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




file_uploaded();



        if(MainActivity.music_playing)
            MainActivity.player.start();

        if(MainActivity.music_playing)
        {

            ImageView img_music = (ImageView) findViewById(R.id.img_music);
            img_music.setBackground(getResources().getDrawable(R.drawable.sound_on_blue));
        }
        else
        {

            ImageView img_music = (ImageView) findViewById(R.id.img_music);
            img_music.setBackground(getResources().getDrawable(R.drawable.sound_off_blue));
        }


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
        mm1 = new MyAsyncTask1();

        mm1.url = (last_requested_query);
        mm1.execute("");

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
    private void set_content()
    {
//        LinearLayout laybtn_login = (LinearLayout) findViewById(R.id.top_lay);
//        //LinearLayout.LayoutParams lp_lay_tabBar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int)(screenHeight*0.1));
//        LinearLayout.LayoutParams lp_laybtn_login = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(int)(screenHeight*0.08));
//        laybtn_login.setLayoutParams(lp_laybtn_login);
//
//
//
//
//
//        TextView txt_level = (TextView) findViewById(R.id.lbl_start_game);
//        txt_level.setTypeface(tf);
//        txt_level.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.05));
//




        TextView lbl_change_pic = (TextView) findViewById(R.id.lbl_change_pic);
        lbl_change_pic.setTypeface(tf);
        lbl_change_pic.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.045));


        TextView lbl_remove_pic = (TextView) findViewById(R.id.lbl_remove_pic);
        lbl_remove_pic.setTypeface(tf);
        lbl_remove_pic.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.045));




//        LinearLayout.LayoutParams lp_laybtn_log_out = new LinearLayout.LayoutParams((int)(screenWidth*0.8),(int)(screenHeight*0.103));
//        LinearLayout laybtn_log_out = (LinearLayout) findViewById(R.id.btn_log_out);
//        laybtn_log_out.setLayoutParams(lp_laybtn_log_out);
//
//        LinearLayout laybtn_back = (LinearLayout) findViewById(R.id.btn_return);
//        laybtn_back.setLayoutParams(lp_laybtn_log_out);
//
//        LinearLayout btn_question_factory = (LinearLayout) findViewById(R.id.btn_question_factory);
//        btn_question_factory.setLayoutParams(lp_laybtn_log_out);








        TextView lbl_title = (TextView) findViewById(R.id.lbl_title);
        lbl_title.setTypeface(tf);
        lbl_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.065));


        TextView lbl_rules = (TextView) findViewById(R.id.lbl_rules);
        lbl_rules.setTypeface(tf);
//        lbl_rules.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.045));
//
        TextView lbl_contact_us = (TextView) findViewById(R.id.lbl_contact_us);
        lbl_contact_us.setTypeface(tf);
//        lbl_contact_us.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.045));
//
        TextView lbl_exit = (TextView) findViewById(R.id.lbl_exit);
        lbl_exit.setTypeface(tf);
//        lbl_exit.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.045));
    }



    public void clk_change_photo(View view) {


        if(fun.check_need_permission())
        {
            if(fun.checkIfAlreadyhavePermission(android.Manifest.permission.READ_EXTERNAL_STORAGE,this))
            {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
        else
        {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 1);
        }



    }
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, 1);

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this,"مجوز داده نشد",Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();

                String filePath = getPath(selectedImage);
                String file_extn = filePath.substring(filePath.lastIndexOf(".") + 1);
              //  image_name_tv.setText(filePath);

                try {
                    if (file_extn.equals("img") || file_extn.equals("jpg") || file_extn.equals("jpeg") || file_extn.equals("gif") || file_extn.equals("png")) {

                        Log.d("majid",filePath);
                        upLoadServerUri = "http://sputa-app.ir/app/trivia/upload.php?uname="+functions.u_name+"&rnd="+String.valueOf(new Random().nextInt());
                        uploadFileName=filePath;
                                lay_wait.setVisibility(View.VISIBLE);
                                fun.enableDisableView(lay_main,false);
                        txt_wait_message.setText("لطفا کمی صبر کنید در حال ارسال فایل...");
                        new Thread(new Runnable() {
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        Log.d("majid","uploading started.....");
                                    }
                                });

                                mm =  new MyAsyncTask();

                                mm.execute("");
                                //uploadFile(uploadFilePath + "" + uploadFileName);






                            }
                        }).start();

                    } else {
                        //NOT IN REQUIRED FORMAT
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
    }
    String
        imagePath="";
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        imagePath = cursor.getString(column_index);

        return cursor.getString(column_index);
    }
    public void file_uploaded()
    {
        remove_message();
        lay_wait.setVisibility(View.VISIBLE);
        x = 1;
        is_requested = true;

        no_connection=false;
        mm1 = new MyAsyncTask1();
        txt_wait_message.setText("لطفا کمی صبر کنید ");
        last_requested_query = getResources().getString(R.string.site_url) + "do.php?param=get_avatar_index&uname=" + functions.u_name +"&rnd="+String.valueOf(new Random().nextInt());
        mm1.url = last_requested_query ;
        mm1.execute("");
    }
    public void file_not_sent()
    {


    }
    public void clk_home(View view) {
finish();
    }
    public void clk_statistics(View view) {
        Intent i = new Intent(this,Statistics.class);
        startActivity(i);
        finish();
    }
    public void clk_stotr(View view) {
        Intent i = new Intent(this,Store.class);
        startActivity(i);
        finish();
    }
    public void stop_music(View view) {
        if(MainActivity.music_playing)
        {
            MainActivity.player.pause();
            MainActivity.music_playing=false;
            ImageView img_music = (ImageView) findViewById(R.id.img_music);
            img_music.setBackground(getResources().getDrawable(R.drawable.sound_off_blue));
        }
        else
        {
            MainActivity.player.start();
            MainActivity.music_playing=true;
            ImageView img_music = (ImageView) findViewById(R.id.img_music);
            img_music.setBackground(getResources().getDrawable(R.drawable.sound_on_blue));
        }

    }
    public void clk_logout(View view) {
        SharedPreferences settings = getApplicationContext().getSharedPreferences("homeScore", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("homeScore", "");
        editor.apply();

//        Intent intent = this.getIntent();
//        this.setResult(RESULT_OK, intent);
        startActivity(new Intent(this,Register.class));
        finish();
        MainActivity.fa.finish();


    }

    public void clk_back(View view) {
        finish();
    }

    public void clk_question_factory(View view) {
        startActivity(new Intent(Setting.this,Question_factory.class));


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

            //  pb.setVisibility(View.GONE);
            int
                    start = ss.indexOf("<output>");
            int
                    end = ss.indexOf("</output>");
            String
                    output_str = ss;

        }




        protected void onProgressUpdate(Integer... progress) {
            //pb.setProgress(progress[0]);
        }

        public void postData() throws IOException {


            String fileName = uploadFileName;

            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File sourceFile = new File( uploadFileName);

            if (!sourceFile.isFile()) {


                Toast.makeText(getBaseContext(),"فایل پیدا نشد",Toast.LENGTH_SHORT);

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getBaseContext(),"فایل پیدا نشد",Toast.LENGTH_SHORT);
                    }
                });

                // return 0;

            }
            else
            {
                try {

                    // open a URL connection to the Servlet
                    FileInputStream fileInputStream = new FileInputStream(sourceFile);
                    URL url = new URL(upLoadServerUri);

                    // Open a HTTP  connection to  the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("uploaded_file", fileName);

                    dos = new DataOutputStream(conn.getOutputStream());

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName + "\"" + lineEnd);

                    dos.writeBytes(lineEnd);

                    // create a buffer of  maximum size
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {

                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    }

                    // send multipart form data necesssary after file data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    // Responses from the server (code and message)
                    serverResponseCode = conn.getResponseCode();
                    String serverResponseMessage = conn.getResponseMessage();

                    Log.i("uploadFile", "HTTP Response is : "
                            + serverResponseMessage + ": " + serverResponseCode);

                    if(serverResponseCode == 200){

                        runOnUiThread(new Runnable() {
                            public void run() {



                                //Log.d("majid",msg);
                                lay_wait.setVisibility(View.INVISIBLE);
                                fun.enableDisableView(lay_main,true);
                                Toast.makeText(Setting.this,"عکس با موفقیت ارسال شد",Toast.LENGTH_SHORT);
                                file_uploaded();
                                //Toast.makeText(UploadToServer.this, "File Upload Complete.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    //close the streams //
                    fileInputStream.close();
                    dos.flush();
                    dos.close();

                } catch (MalformedURLException ex) {

                    //  dialog.dismiss();
                    ex.printStackTrace();

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Log.d("majid","MalformedURLException Exception : check script url.");
//                            Toast.makeText(UploadToServer.this, "MalformedURLException",
//                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                    Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                } catch ( Exception e) {

                    //  dialog.dismiss();
                    e.printStackTrace();
                    final Exception e2=e;
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Log.d("majid","Got Exception : see logcat "+e2.getMessage());
                                    fun.enableDisableView(lay_main,true);
                                    lay_wait.setVisibility(View.INVISIBLE);
                                    Toast.makeText(Setting.this,"خطا در ارسال فایل",Toast.LENGTH_SHORT).show();
                        }
                    });

                }
//                dialog.dismiss();
                //  return serverResponseCode;

            }
        }
    }


    private class MyAsyncTask1 extends AsyncTask<String, Integer, Double> {


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

                if (param_str.equals("get_avatar_index") && is_requested) {


                    TextView lbl_remove_pic = (TextView) findViewById(R.id.lbl_remove_pic);
                    lbl_remove_pic.setText(functions.u_name);
                    fun.enableDisableView(lay_main,true);
                    lay_wait.setVisibility(View.INVISIBLE);
                    is_requested=false;
                    //st.makeText(getBaseContext(),functions.u_name,Toast.LENGTH_LONG).show();
                    start1 = ss.indexOf("<avatar_id>");
                    end1 = ss.indexOf("</avatar_id>");
                    String  res = (ss.substring(start1 + 11, end1));


                    functions.avatar_name = functions.u_name+"_"+res+".jpg";

                    String image_url = "http://sputa-app.ir/app/trivia/pic/"+functions.avatar_name;

                   // Toast.makeText(getBaseContext(),image_url,Toast.LENGTH_LONG).show();
                    Picasso.with(getBaseContext()).load(image_url).fit().into(img_avatar);

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
