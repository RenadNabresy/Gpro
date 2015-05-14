package com.google.gpro.newapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

public class Post extends ActionBarActivity implements AdapterView.OnItemSelectedListener{

    protected EditText myPost;
    protected Button postButton;
    protected Spinner spinner;
    protected int selection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);

        myPost = (EditText)findViewById(R.id.post_text);
        postButton = (Button)findViewById(R.id.post_button);
        spinner =(Spinner)findViewById(R.id.spinner);
        selection= 1;

        //Spinner Adapter
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.CategoryOfPost,android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //listen to button
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newPost = myPost.getText().toString();
                if(newPost.isEmpty()){
                    Toast.makeText(Post.this, "You can't save empty post!!", Toast.LENGTH_LONG).show();
                    return;
                }

                String type = "Fitness";
                if(selection==1){type ="Fitness";}
                else if(selection==2){type ="Maternal Health";}
                else if(selection==3){type ="Diet";}

                final ProgressDialog dig=new ProgressDialog(Post.this);
                dig.setTitle("Posting !!");
                dig.setMessage("please wait .");
                dig.show();

                ParseObject post = new ParseObject("Post");
                post.put("text",newPost);
                post.put("C_num",selection);
                post.put("category",type );
                post.put("createdBy",ParseUser.getCurrentUser().getObjectId());
                post.put("NumOfLikes",0);
                post.put("NumOfComments",0);
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
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String type = ((TextView) view).getText().toString();
        if(type.equals("Fitness")) { selection=1; }
        else if(type.equals("Maternal Health")) { selection=2; }
        else if(type.equals("Diet")) { selection=3; }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
