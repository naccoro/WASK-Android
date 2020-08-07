package com.naccoro.wask;

import android.app.Application;

public class WaskApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    private void init() {
        SharedPreferenceManager.getInstance().initInstance(getApplicationContext());
    }
}
