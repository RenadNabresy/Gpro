package com.google.gpro.newapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;


public class adapter extends ArrayAdapter<ParseObject>{

    public adapter(Context context, List<ParseObject> post) {
        super(context,R.layout.post_view, post);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater myInflater = LayoutInflater.from(getContext());
        final View customView = myInflater.inflate(R.layout.post_view, parent, false);

        TextView postDetails = (TextView)customView.findViewById(R.id.post_time_date);
        TextView categoryView = (TextView)customView.findViewById(R.id.category);
        TextView postContent = (TextView)customView.findViewById(R.id.post_text);
        ImageView postImage = (ImageView)customView.findViewById(R.id.post_image);
        final ToggleButton like = (ToggleButton)customView.findViewById(R.id.toggleButton);
        ImageView comment_view = (ImageView)customView.findViewById(R.id.comment_btn);

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

        // show number of likes
        final TextView numberOfLikes = (TextView)customView.findViewById(R.id.textView2);
        numberOfLikes.setText(post.get("NumOfLikes")+ " Likes");

        // show number of comments
        final TextView numberOfComments = (TextView)customView.findViewById(R.id.click);
        numberOfComments.setText(post.get("NumOfComments")+ " Comments");

        // show the comments
        numberOfComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog d;
                if (post.getInt("C_num") ==  1) {
                    d = new CustomDialog(getContext(),"Comment1",post.getObjectId());
                    d.show();
                } else if (post.getInt("C_num") == 2) {
                    d = new CustomDialog(getContext(),"Comment2",post.getObjectId());
                    d.show();
                } else if (post.getInt("C_num") == 3) {
                    d = new CustomDialog(getContext(),"Comment3",post.getObjectId());
                    d.show();
                }

            }
        });

        // click to add a comment
        comment_view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                final EditText input = new EditText(getContext());

                dialogBuilder.setTitle("Comment");
                dialogBuilder.setMessage("write your comment ..");
                dialogBuilder.setView(input);
                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String comment = input.getText().toString().trim();
                        if (!comment.matches("")) {
                            if (post.getInt("C_num") == 1) {
                                addComment(comment, post, "Comment1", numberOfComments);
                            } else if (post.getInt("C_num") == 2) {
                                addComment(comment, post, "Comment2", numberOfComments);
                            } else if (post.getInt("C_num") == 3) {
                                addComment(comment, post, "Comment3", numberOfComments);
                            }
                            Toast.makeText(getContext(), "comment saved", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
            }

        });

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
                 if(e==null){ //update the number of likes without retrieving a complete raw

                     // Create a pointer to an object of class Post with id
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

    private void addComment(String text, final ParseObject post, String commentTable, final TextView numberOfComments){
        ParseObject commentObject = new ParseObject(commentTable);
        commentObject.put("post_id", post.getObjectId());
        commentObject.put("user_id", ParseUser.getCurrentUser().getObjectId());
        commentObject.put("user_name", ParseUser.getCurrentUser().getUsername());
        commentObject.put("comment", text);
        commentObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                // Create a pointer to an object of class Post with id
                ParseObject post1 = ParseObject.createWithoutData("Post", post.getObjectId());

                // Increment the current value of the likes key by 1
                post1.increment("NumOfComments");

                // Save
                post1.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(com.parse.ParseException e) {
                        if (e == null) {
                            // Saved successfully.
                            numberOfComments.setText(post.get("NumOfComments")+ " Comments");
                        } else {
                            // The save failed.
                        }
                    }
                });
            }
        });

    }

}