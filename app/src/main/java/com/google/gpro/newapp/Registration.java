package com.google.gpro.newapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class Registration extends ActionBarActivity implements AdapterView.OnItemSelectedListener{

    protected EditText mUsername;
    protected EditText mEmail;
    protected EditText mPassword;
    protected EditText vPassword;
    protected Spinner spinner;
    protected int selection;
    protected Button mSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //initialize
        mUsername = (EditText)findViewById(R.id.UsernameRegistration);
        mEmail    = (EditText)findViewById(R.id.EmailRegistration);
        mPassword  = (EditText)findViewById(R.id.PasswordRegistration);
        vPassword  = (EditText)findViewById(R.id.PasswordVerification);
        spinner =(Spinner)findViewById(R.id.spinner);
        mSignUp    =(Button)findViewById(R.id.SginUpRegistration);
        selection= 0;

        //Spinner Adapter
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.UserType,android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        //listen to Registration button click
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get the data
                String username = mUsername.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String passwordV = vPassword.getText().toString().trim();

                //validate the sign up data
                boolean validationError = false;
                StringBuilder validationErrorMessage = new StringBuilder("Please ");
                if (username.isEmpty()) {
                    validationError = true;
                    validationErrorMessage.append("enter a username");
                }
                if (email.isEmpty()) {
                    if (validationError) {
                        validationErrorMessage.append(", and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("enter an email");
                }
                if (password.isEmpty()) {
                    if (validationError) {
                        validationErrorMessage.append(", and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("enter a password");
                }
                if (passwordV.isEmpty() || !passwordV.equals(password)) {
                    if (validationError) {
                        validationErrorMessage.append(", and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("enter the same password twice");
                }
                validationErrorMessage.append(".");

                //If there is a validation error, display the error
                if (validationError) {
                    Toast.makeText(Registration.this, validationErrorMessage.toString(), Toast.LENGTH_LONG).show();
                    return;
                } else {

                    //set progress dialog
                    final ProgressDialog dig = new ProgressDialog(Registration.this);
                    dig.setTitle("please wait!!");
                    dig.setMessage("signing up.");
                    dig.show();

                    //store user in parse
                    ParseUser user = new ParseUser();
                    user.setUsername(username);
                    user.setPassword(password);
                    user.setEmail(email);


                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {

                            if (e == null) {
                                //if the user is a doctor
                                if (selection == 1) {
                                    ParseObject doctor = new ParseObject("Doctor");
                                    doctor.put("is", ParseUser.getCurrentUser());
                                    doctor.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) { }
                                    });
                                }
                                // Hooray! Let them use the app now.
                                dig.dismiss();
                                Toast.makeText(Registration.this, "Signed up successfully !! ", Toast.LENGTH_LONG).show();

                                //take user home
                                Intent TakeUserHome = new Intent(Registration.this, MainActivity.class);
                                startActivity(TakeUserHome);

                            } else {
                                // Sign up didn't succeed.
                                dig.dismiss();
                                Toast.makeText(Registration.this, "You did not register !! ", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String type = ((TextView) view).getText().toString();
        if(type.equals("Patient")) { selection=0; }
        else {
            selection = 1;//if doctor
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
