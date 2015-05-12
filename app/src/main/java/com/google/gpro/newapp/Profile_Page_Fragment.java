package com.google.gpro.newapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class Profile_Page_Fragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_page, container, false);


    }

    @Override
    public void onStart() {
        super.onStart();
        ParseUser user= ParseUser.getCurrentUser();

        ParseQuery<ParseUser> query = ParseQuery.getUserQuery();
        query.getInBackground(user.getObjectId(), new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, com.parse.ParseException e) {
                if (e == null) {
                    //show the user name
                    TextView username = (TextView)getView().findViewById(R.id.profile_name);
                    username.setText(user.getUsername());
                    //show the user profile image
                    ParseFile image = user.getParseFile("ProfilePhoto");
                    final ParseImageView profileImage = (ParseImageView) getView().findViewById(R.id.profile_image);
                    profileImage.setParseFile(image);
                    profileImage.loadInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, com.parse.ParseException e) {
                            if(e==null) {
                                // The image is loaded and displayed!
                                //profileImage.setImageResource(R.drawable.someday);
                            }
                        }
                    });
                    //show the user email
                    TextView email = (TextView)getView().findViewById(R.id.emailView);
                    email.setText(user.getEmail());

                } else {
                    // something went wrong
                }
            }
        });


        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Post");
        query1.whereEqualTo("createdBy",user.getObjectId());

        query1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> postList, ParseException e) {
                if (e == null) {
                    Log.d("post", "Retrieved " + postList.size() + " posts");
                    ListAdapter PostAdapter = new adapter(getActivity(), postList);
                    ListView PostsListView = (ListView) getView().findViewById(R.id.posts_listview);
                    PostsListView.setAdapter(PostAdapter);
                } else {
                    Log.d("post", "Error: " + e.getMessage());
                    Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
                }
            }
        });


    }


}
