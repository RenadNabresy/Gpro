package com.google.gpro.newapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.ParseException;
import java.util.List;


/**
 * Created by Horizon on 5/6/2015.
 */
public class adapter extends ArrayAdapter<ParseObject>{

    public adapter(Context context, List<ParseObject> post) {
        super(context,R.layout.custom_row, post);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater myInflater = LayoutInflater.from(getContext());
        final View customView = myInflater.inflate(R.layout.custom_row, parent, false);

        TextView postDetails = (TextView)customView.findViewById(R.id.post_time_date);
        TextView postContent = (TextView)customView.findViewById(R.id.post_text);
        ImageView postImage = (ImageView)customView.findViewById(R.id.post_image);
        ToggleButton like = (ToggleButton)customView.findViewById(R.id.toggleButton);

        final ParseObject post = getItem(position);
        String createdBy = post.get("createdBy").toString();

        ParseQuery<ParseUser> query = ParseQuery.getUserQuery();
        query.getInBackground(createdBy, new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, com.parse.ParseException e) {
                if (e == null) {
                    //set the user name
                    TextView username = (TextView)customView.findViewById(R.id.profile_name);
                    username.setText(user.getUsername());
                    //set the user profile image
                    ParseFile image = user.getParseFile("ProfilePhoto");
                    final ParseImageView profileImage = (ParseImageView) customView.findViewById(R.id.profile_image);
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

                } else {
                    // something went wrong
                }
            }
        });


        //set the date and the time of the post
        String details = post.getCreatedAt().toString();
        postDetails.setText(details);

        //set the post content
        String content = post.getString("text");
        postContent.setText(content);

        //set the post image if there is

        ParseObject likeObject;
        if(like.isChecked()){
            if(post.getInt("C_num")==1){
                likeObject = new ParseObject("Like1");
            }else if(post.getInt("C_num")==2){
                likeObject = new ParseObject("Like2");
            }else if(post.getInt("C_num")==3) {
                likeObject = new ParseObject("Like3");
            }
            post.put("post_id",post.getObjectId());
            post.put("user_id",ParseUser.getCurrentUser().getObjectId());
            post.saveInBackground(new SaveCallback() {
                @Override
                public void done(com.parse.ParseException e) {
                post.increment("NumOfLikes",1);
                }
            });
        }

        return customView;
    }
}
