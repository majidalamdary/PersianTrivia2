package com.sputa.persiantrivia;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by diego on 27/01/2018.
 */

public class MyApplication extends Application
{
    private Locale locale = null;

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        if (locale != null)
        {
            newConfig.locale = locale;
            Locale.setDefault(locale);
            getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        }
    }

    @Override
    public void onCreate()
    {
        super.onCreate();


        Configuration config = getBaseContext().getResources().getConfiguration();
       // Toast.makeText(this,config.locale.getLanguage().toString(), Toast.LENGTH_SHORT).show();
        String lang = "fa";
        if (! "".equals(lang) && ! config.locale.getLanguage().equals(lang))
        {
            locale = new Locale(lang);
            Locale.setDefault(locale);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
    }
}