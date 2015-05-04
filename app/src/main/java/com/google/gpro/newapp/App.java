package com.google.gpro.newapp;

import android.app.Application;
import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "01uXRlo3msYG9RffC5zewIKsbdvaXX9TfBcpBBDq", "4xqSbq48OpDaEIaLfgzyYMTLFam6qW0qVEaW8YR2");


    }
}
