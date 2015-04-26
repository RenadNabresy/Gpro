package com.google.gpro.newapp;

import android.widget.Toast;
import com.parse.LogInCallback;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class User {

    User(String name, String email, String password, String phone) {
        ParseUser user = new ParseUser();
        user.setUsername(name);
        user.setPassword(password);
        user.setEmail(email);

        // other fields can be set just like with ParseObject
        user.put("phone", phone);

        user.signUpInBackground( new SignUpCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    Toast.makeText(null,"SignUp Successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(null,"Sign up didn't succeed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void LogIn(String name, String password){
        ParseUser.logInInBackground(name, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, com.parse.ParseException e) {
                if (user != null) {
                    // Hooray! The user is logged in.
                } else {
                    // Sign in failed. Look at the ParseException to see what happened.
                }
            }
        });
    }
}