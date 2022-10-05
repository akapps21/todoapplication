package com.akapps.todoapp;

import android.app.Application;

public class MyApplication extends Application {

    private static MyApplication mMyApplication;

    @Override
    public void onCreate() {
        super.onCreate();

        mMyApplication = this;
    }

    public static MyApplication getApplication() {
        return mMyApplication;
    }
}
