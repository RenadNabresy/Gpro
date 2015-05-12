package com.google.gpro.newapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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
        TextView categoryView = (TextView)customView.findViewById(R.id.textView4);
        TextView postContent = (TextView)customView.findViewById(R.id.post_text);
        ImageView postImage = (ImageView)customView.findViewById(R.id.post_image);
        final ToggleButton like = (ToggleButton)customView.findViewById(R.id.toggleButton);

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
                            if (e == null) {
                                // The image is loaded and displayed!
                                //profileImage.setImageResource(R.drawable.someday);
                            }
                        }
                    });
                    if(image==null){profileImage.setImageResource(R.drawable.default_image);}

                } else {
                    // something went wrong
                }
            }
        });

        //show the category name
        categoryView.setText(post.getString("category"));

        //set the date and the time of the post
        String details = post.getCreatedAt().toString();
        postDetails.setText(details);

        //set the post content
        String content = post.getString("text");
        postContent.setText(content);

        //state of like button
        if (post.getInt("C_num") ==  1) {
            likeButtonState("Like1", post, like);
        } else if (post.getInt("C_num") == 2) {
            likeButtonState("Like2",post, like);
        } else if (post.getInt("C_num") == 3) {
            likeButtonState("Like3",post, like);
        }




        final TextView numberOfLikes = (TextView)customView.findViewById(R.id.textView2);
        numberOfLikes.setText(post.get("NumOfLikes")+ " Likes");

        //set the post image if there is

        // attach an OnClickListener
        like.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (like.isChecked()) {//add like
                    if (post.getInt("C_num") ==  1) {
                        addLikeObject("Like1", post, numberOfLikes);
                    } else if (post.getInt("C_num") == 2) {
                        addLikeObject("Like2",post, numberOfLikes);
                    } else if (post.getInt("C_num") == 3) {
                        addLikeObject("Like3",post, numberOfLikes);
                    }

                }else {//delete like
                    if (post.getInt("C_num") ==  1) {
                        deleteLikeObject("Like1", post, numberOfLikes);
                    } else if (post.getInt("C_num") == 2) {
                        deleteLikeObject("Like2", post, numberOfLikes);
                    } else if (post.getInt("C_num") == 3) {
                        deleteLikeObject("Like3", post, numberOfLikes);
                    }
                }
            }
        });

        return customView;
    }

     private void addLikeObject(String likeObjName,final ParseObject post,final TextView numberOfLikes){
         ParseObject likeObject = new ParseObject(likeObjName);
         likeObject.put("post_id", post.getObjectId());
         likeObject.put("user_id", ParseUser.getCurrentUser().getObjectId());
         likeObject.saveInBackground(new SaveCallback() {
             @Override
             public void done(com.parse.ParseException e) {
                 if(e==null){
                     // Create a pointer to an object of class Point with id dlkj83d
                     ParseObject post1 = ParseObject.createWithoutData("Post", post.getObjectId());

                     // Increment the current value of the likes key by 1
                     post1.increment("NumOfLikes");

                     // Save
                     post1.saveInBackground(new SaveCallback() {
                         @Override
                         public void done(com.parse.ParseException e) {
                             if (e == null) {
                                 // Saved successfully.
                                 numberOfLikes.setText(post.get("NumOfLikes")+ " Likes");
                             } else {
                                 // The save failed.
                             }
                         }
                     });
                 }else {
                     Toast.makeText(getContext(), "Like wasn't saved" , Toast.LENGTH_LONG).show();
                 }
             }
         });
     }

    private void deleteLikeObject(String likeObjName,final ParseObject post, final TextView numberOfLikes){
        ParseQuery<ParseObject> query=ParseQuery.getQuery(likeObjName);
        query.whereEqualTo("post_id", post.getObjectId());
        query.whereEqualTo("user_id", ParseUser.getCurrentUser().getObjectId());

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                if(e==null) {
                    for (ParseObject delete : parseObjects) {//one raw will be deleted
                        delete.deleteInBackground();
                        Toast.makeText(getContext(), "deleted", Toast.LENGTH_SHORT).show();

                        // Create a pointer to an object of class Post with id
                        ParseObject post1 = ParseObject.createWithoutData("Post", post.getObjectId());

                        // Increment the current value of the likes key by 1-
                        post1.increment("NumOfLikes", -1);

                        // Save
                        post1.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(com.parse.ParseException e) {
                                if (e == null) {
                                    // Saved successfully.
                                    numberOfLikes.setText(post.get("NumOfLikes")+ " Likes");
                                } else {
                                    // The save failed.
                                }
                            }
                        });
                    }
                }else{
                    Toast.makeText(getContext(), "error in deleting", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void likeButtonState(String likeObjName,final ParseObject post, final ToggleButton like){
        ParseQuery<ParseObject> query=ParseQuery.getQuery(likeObjName);
        query.whereEqualTo("post_id", post.getObjectId());
        query.whereEqualTo("user_id", ParseUser.getCurrentUser().getObjectId());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                if(parseObject!= null){
                    like.setChecked(true);
                }
            }
        });
    }
}

