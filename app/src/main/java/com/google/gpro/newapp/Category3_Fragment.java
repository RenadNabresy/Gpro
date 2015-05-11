package com.google.gpro.newapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Horizon on 4/20/2015.
 */
public class Category3_Fragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.posts_show_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.whereEqualTo("C_num", 3);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> postList, ParseException e) {
                if (e == null) {
                    Log.d("post", "Retrieved " + postList.size() + " posts");
                    ListAdapter PostAdapter = new adapter(getActivity(),postList);
                    ListView PostsListView = (ListView)getView().findViewById(R.id.posts_listview);
                    PostsListView.setAdapter(PostAdapter);
                } else {
                    Log.d("post", "Error: " + e.getMessage());
                    Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

