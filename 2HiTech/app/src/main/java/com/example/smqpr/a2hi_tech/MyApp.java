package com.example.smqpr.a2hi_tech;

import android.app.Application;

import com.backendless.Backendless;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Backendless.setUrl(Defaults.SERVER_URL);
        Backendless.initApp(this,Defaults.APPLICATION_ID, Defaults.API_KEY);
    }
}
