package com.google.gpro.newapp;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Window;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class CustomDialog extends Dialog {



    public CustomDialog(Context context,String tableName, String postId){
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        // find your views and customize them here

        ParseQuery<ParseObject> query = ParseQuery.getQuery(tableName);
        query.whereEqualTo("post_id", postId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> commentList, ParseException e) {
                if (e == null) {
                    Log.d("comment", "Retrieved " + commentList.size() + " comments");
                    ListAdapter commentAdapter = new CommentAdapter(getContext(),commentList);
                    ListView commentListView = (ListView)findViewById(R.id.custom_dialog_list);
                    commentListView.setAdapter(commentAdapter);
                } else {
                    Log.d("post", "Error: " + e.getMessage());
                    Toast.makeText(getContext(), "error", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

}
