package com.google.gpro.newapp;

import android.app.Activity;
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

import com.google.gpro.newapp.R;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;


public class Login extends Activity {


    protected EditText mEmail;
    protected EditText mPassword;
    protected Button mLogin;
    protected Button mCreateAccountbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


       //initialize
        mEmail = (EditText)findViewById(R.id.Email);
        mPassword = (EditText)findViewById(R.id.Password);
        mLogin = (Button)findViewById(R.id.Login);
        mCreateAccountbtn=(Button)findViewById(R.id.CreateAccount);


        //listen to mLogin button click
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get user inputs
                String useremail= mEmail.getText().toString().trim();
                String userpassword= mPassword.getText().toString().trim();



                //login the user using parse
                ParseUser.logInInBackground(useremail,userpassword,new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (e==null){
                            //wow Success :]
                            Toast.makeText(Login.this,"Welcome !"+ ParseUser.getCurrentUser().getUsername().toString() ,Toast.LENGTH_LONG).show();
                            //Take the user to home page
                            Intent TakeUserHome=new Intent(Login.this,MainActivity.class);
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

        //noinspection SimplifiableIfStatement
        /* if (id == R.id.SignOut) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}
