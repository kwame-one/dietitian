package com.kwame.dietitian.util;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.kwame.dietitian.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class Init extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
