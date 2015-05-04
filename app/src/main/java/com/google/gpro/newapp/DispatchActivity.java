package com.google.gpro.newapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.parse.Parse;
import com.parse.ParseUser;

/**
 * Created by Horizon on 5/2/2015.
 */
public class DispatchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ParseUser.getCurrentUser()!=null){
            startActivity(new Intent(this,MainActivity.class));
        }
        else{
            startActivity(new Intent(this, Login.class));
        }
    }
}
