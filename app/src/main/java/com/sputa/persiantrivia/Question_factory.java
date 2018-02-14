package com.sputa.persiantrivia;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Question_factory extends AppCompatActivity {

    functions fun;
    String
            majid="majid";
    String
            font_name = "";
    Typeface tf;



    int screenWidth  = 0;
    int screenHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_factory);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        fun = new functions();


        if(MainActivity.music_playing)
            MainActivity.player.start();

        font_name = fun.font_name;
        tf = Typeface.createFromAsset(getAssets(),font_name );


        set_content();

    }


    private void set_content()
    {



        LinearLayout.LayoutParams lp_laybtn_log_out = new LinearLayout.LayoutParams((int)(screenWidth*0.8),(int)(screenHeight*0.103));



        LinearLayout lay_btn_new_question = (LinearLayout) findViewById(R.id.btn_new_question);
        lay_btn_new_question.setLayoutParams(lp_laybtn_log_out);

        LinearLayout lay_btn_confirmed_question = (LinearLayout) findViewById(R.id.btn_confirmed_question);
        lay_btn_confirmed_question.setLayoutParams(lp_laybtn_log_out);

        LinearLayout lay_btn_unconfirmed_question = (LinearLayout) findViewById(R.id.btn_unconfirmed_question);
        lay_btn_unconfirmed_question.setLayoutParams(lp_laybtn_log_out);

        LinearLayout lay_btn_rejected_question = (LinearLayout) findViewById(R.id.btn_rejected_question);
        lay_btn_rejected_question.setLayoutParams(lp_laybtn_log_out);

        LinearLayout lay_btn_return = (LinearLayout) findViewById(R.id.btn_return);
        lay_btn_return.setLayoutParams(lp_laybtn_log_out);





        TextView txt_describ = (TextView) findViewById(R.id.txt_describ);
        txt_describ.setTypeface(tf);
        txt_describ.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.055));


        TextView lbl_setting = (TextView) findViewById(R.id.lbl_setting);
        lbl_setting.setTypeface(tf);
        lbl_setting.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.065));


        TextView lbl_new_question = (TextView) findViewById(R.id.lbl_new_question);
        lbl_new_question.setTypeface(tf);
        lbl_new_question.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.065));

        TextView lbl_confired_question = (TextView) findViewById(R.id.lbl_confired_question);
        lbl_confired_question.setTypeface(tf);
        lbl_confired_question.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.065));

        TextView lvl_unconfirmed_question = (TextView) findViewById(R.id.lvl_unconfirmed_question);
        lvl_unconfirmed_question.setTypeface(tf);
        lvl_unconfirmed_question.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.065));

        TextView lbl_rejected_question = (TextView) findViewById(R.id.lbl_rejected_question);
        lbl_rejected_question.setTypeface(tf);
        lbl_rejected_question.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.065));

        TextView lbl_return = (TextView) findViewById(R.id.lbl_return);
        lbl_return.setTypeface(tf);
        lbl_return.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int) (screenWidth * 0.065));


    }


    public void clk_back(View view) {
        finish();
    }

    public void clk_new_question(View view) {
        Intent i = new Intent(this,New_question.class);
        i.putExtra("q_id","0");
        startActivity(i);

    }

    public void clk_rejected(View view) {
        Intent i = new Intent(this,QuestionList.class);
        i.putExtra("list_type","rejected");
        startActivity(i);
    }

    public void clk_confirmed(View view) {
        Intent i = new Intent(this,QuestionList.class);
        i.putExtra("list_type","confirmed");
        startActivity(i);
    }

    public void clk_unconfirmed(View view) {
        Intent i = new Intent(this,QuestionList.class);
        i.putExtra("list_type","unconfirmed");
        startActivity(i);
    }
}
