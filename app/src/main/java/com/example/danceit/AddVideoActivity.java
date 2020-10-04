package com.example.danceit;

import com.example.danceit.Database.VideoViewModel;
import com.firebase.ui.firestore.paging.FirestoreDataSource;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.danceit.Model.Tag;
import com.example.danceit.Model.User;
import com.example.danceit.Model.Video;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AddVideoActivity extends AppCompatActivity {

    private boolean isPrivate; // privacy is public
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);

        saveInput();

    }

    public void saveInput() {

        final Intent intent=new Intent(this,MainActivity.class);

        final TextInputLayout textInputUrl = findViewById(R.id.textInput_url);
        final TextInputLayout textInputTags = findViewById(R.id.textInput_tag);
        checkPrivacy();
        //Get save button
        final Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri=Uri.parse(textInputUrl.getEditText().getText().toString().trim());
                if(isURL(textInputUrl.getEditText().getText().toString().trim())) {

                    if (isPrivate) {

                        // Initialize Firebase Auth
                        mAuth = FirebaseAuth.getInstance();

                        CollectionReference reference = FirebaseFirestore.getInstance().collection("video_urls_private").
                                document(Objects.requireNonNull(mAuth.getCurrentUser().getEmail())).collection("private_video");

                        Video video = new Video(new User("username", "password"),
                                Objects.requireNonNull(textInputUrl.getEditText()).getText().toString().trim(), tagInput_string((textInputTags.getEditText())
                                .getText().toString()), "private");

                        reference.add(video);


                    }
                    else {

                        CollectionReference reference = FirebaseFirestore.getInstance().collection("video_urls");
                        reference.add(new Video(new User("username", "password"),
                                Objects.requireNonNull(textInputUrl.getEditText()).getText().toString().trim(), tagInput_string((textInputTags.getEditText())
                                .getText().toString()), "public"));

                        Toast toast = Toast.makeText(getApplicationContext(), "Url saved as public.", Toast.LENGTH_SHORT);
                            toast.show();
                    }

                    startActivity(intent);
            }else {

                    Toast toast = Toast.makeText(getApplicationContext(), "Url not valid", Toast.LENGTH_SHORT);
                    toast.show();
                }
        }

        });

    }

    public boolean isURL(String url) {
       if(url.contains("http")&& (url.contains("youtube")||url.contains("youtu.be"))){
           return true;
       }else {
           return false;
       }
    }

    public ArrayList<String> tagInput_string(String textInputTags) {
        ArrayList<String> tag_lists = new ArrayList<String>();
        String[] description = textInputTags.split(" ");

        for (int i = 0; i < description.length; i++) {
            tag_lists.add(description[i]);
        }
        return tag_lists;
    }

    public void checkPrivacy() {
        RadioButton radiobutton_private;
        radiobutton_private = findViewById(R.id.radioButton_private);
        radiobutton_private.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isPrivate = b;
            }
        });

    }



}