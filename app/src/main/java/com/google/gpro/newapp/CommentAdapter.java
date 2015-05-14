package com.google.gpro.newapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Horizon on 5/14/2015.
 */
public class CommentAdapter extends ArrayAdapter<ParseObject> {

    public CommentAdapter(Context context, List<ParseObject> comment) {
        super(context, R.layout.custom_dialog_item, comment);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater myInflater = LayoutInflater.from(getContext());
        final View customView = myInflater.inflate(R.layout.custom_dialog_item, parent, false);


        TextView name = (TextView)customView.findViewById(R.id.name);
        TextView text = (TextView)customView.findViewById(R.id.comment_text);

        final ParseObject comment = getItem(position);
        name.setText(comment.getString("user_name"));
        text.setText(comment.getString("comment"));

        return  customView;
    }
}
