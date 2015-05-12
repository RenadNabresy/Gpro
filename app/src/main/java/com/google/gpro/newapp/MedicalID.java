package com.google.gpro.newapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class MedicalID extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_medial_id, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

         //initialize
        final EditText mAge = (EditText)getView().findViewById(R.id.editText);
        final EditText mBlood = (EditText)getView().findViewById(R.id.editText2);
        final EditText mcondition = (EditText)getView().findViewById(R.id.editText3);
        final EditText mAllergiesReaction = (EditText) getView().findViewById(R.id.editText5);
        final EditText mMedications = (EditText) getView().findViewById(R.id.editText6);
        final EditText mNote = (EditText) getView().findViewById(R.id.editText4);
        Button mSave = (Button) getView().findViewById(R.id.save);


        //show medical ID info
        ParseQuery<ParseObject> query=ParseQuery.getQuery("MedicalID");
        query.whereEqualTo("user_id", ParseUser.getCurrentUser().getObjectId());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                if (parseObject != null) {

                    mAge.setHint(parseObject.getNumber("Age").toString());
                    mBlood.setHint(parseObject.getString("bloodgroup"));
                    mcondition.setHint(parseObject.getString("MedicalCondition"));
                    mAllergiesReaction.setHint(parseObject.getString("AllergiesReaction"));
                    mNote.setHint(parseObject.getString("MedicalNote"));
                    mMedications.setHint(parseObject.getString("Medications"));
                }
            }
        });

        //show user info
      /*  ParseQuery<ParseUser> query1 = ParseQuery.getUserQuery();
        query1.getInBackground(ParseUser.getCurrentUser().getObjectId(), new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, com.parse.ParseException e) {
                if (e == null) {
                    //set the user name
                    TextView username = (TextView)getView().findViewById(R.id.textView2);
                    username.setText(user.getUsername());
                    //set the user profile image
                    ParseFile image = user.getParseFile("ProfilePhoto");
                    final ParseImageView profileImage = (ParseImageView)getView().findViewById(R.id.imageView);
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

                } else {
                    // something went wrong
                }
            }
        });
*/

        //listen to Registration button click
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //get the data
                int Age = Integer.parseInt(mAge.getText().toString());
                String Blood = mBlood.getText().toString().trim();
                String condition = mcondition.getText().toString();
                String AllergiesReaction = mAllergiesReaction.getText().toString();
                String Note = mNote.getText().toString();
                String Medications = mMedications.getText().toString();


                //store user in parse
                ParseObject medicalID = new ParseObject("MedicalID");
                medicalID.put("user_id", ParseUser.getCurrentUser().getObjectId());
                medicalID.put("Age",Age);
                medicalID.put("bloodgroup", Blood);
                medicalID.put("MedicalCondition", condition);
                medicalID.put("AllergiesReaction", AllergiesReaction);
                medicalID.put("MedicalNote", Note);
                medicalID.put("Medications", Medications);
                medicalID.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            Toast.makeText(getActivity(), "Saved successfully", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getActivity(), "not saved", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

    }





}
