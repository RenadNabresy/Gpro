package com.google.gpro.newapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.Fragment;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class Post extends ActionBarActivity{

    protected EditText myPost;
    protected Button postButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);

        myPost = (EditText)findViewById(R.id.post_text);
        postButton = (Button)findViewById(R.id.post_button);

        //listen to button
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the current user
                //ParseUser currentUser = ParseUser.getCurrentUser();
                //String currentUserName = currentUser.getUsername();

                String newPost = myPost.getText().toString();

                final ProgressDialog dig=new ProgressDialog(Post.this);
                dig.setTitle("Posting !!");
                dig.setMessage("please wait .");
                dig.show();

                ParseObject post = new ParseObject("Post");
                post.put("text",newPost);
                post.put("CreatedBy",ParseUser.getCurrentUser());
                post.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            dig.dismiss();
                            Intent goHomePage= new Intent(Post.this,MainActivity.class);
                            startActivity(goHomePage);
                            Toast.makeText(Post.this, "You have posted", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(Post.this,"Problem in posting", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}
