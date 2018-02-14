package com.sputa.persiantrivia;

import android.content.res.Configuration;
import android.drm.DrmStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Locale;

public class Test1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Locale locale = new Locale("fa");
//        Locale.setDefault(locale);
//        Configuration config = getBaseContext().getResources().getConfiguration();
//        config.locale = locale;
//        getBaseContext().getResources().updateConfiguration(config,
//                getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.activity_test1);

        LinearLayout mn1= (LinearLayout) findViewById(R.id.mn1);


//        mn1.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);


    }

    public void clk1(View view) {

        Toast.makeText(getBaseContext(), "majid", Toast.LENGTH_SHORT).show();

    }
}
