package com.bonait.bnframework.application;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

/**
 * Created by LY on 2019/3/19.
 */
public class MainApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
    }
}
