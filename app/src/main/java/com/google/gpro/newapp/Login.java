package com.google.gpro.newapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class Login extends ActionBarActivity {


    protected EditText mUserName;
    protected EditText mPassword;
    protected Button mLogin;
    protected Button mCreateAccountbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


       //initialize
        mUserName = (EditText)findViewById(R.id.UserName);
        mPassword = (EditText)findViewById(R.id.Password);
        mLogin = (Button)findViewById(R.id.Login);
        mCreateAccountbtn=(Button)findViewById(R.id.CreateAccount);


        //listen to mLogin button click
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //get user inputs
            String userName= mUserName.getText().toString().toLowerCase();
            String userPassword= mPassword.getText().toString().trim();
                //login the user using parse
                ParseUser.logInInBackground(userName,userPassword,new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (e==null){
                            //wow Success :]
                            Toast.makeText(Login.this,"Welcome "+ ParseUser.getCurrentUser().getUsername() ,Toast.LENGTH_LONG).show();
                            //Take the user to home page
                            Intent TakeUserHome=new Intent(Login.this,HomePageFragment.class);
                            startActivity(TakeUserHome);
                        }else {
                            //Sorry ! A problem
                            AlertDialog.Builder builder=new AlertDialog.Builder(Login.this);
                            builder.setMessage(e.getMessage());
                            builder.setTitle("Sorry");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    //Close the dialog
                                    dialogInterface.dismiss();
                                }
                            });
                            AlertDialog dialog=builder.create();
                            dialog.show();
                        }
                    }
                });
            }
        });

        mCreateAccountbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent TakeUserHome = new Intent(Login.this, Registration.class);
                startActivity(TakeUserHome);
            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.global, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
