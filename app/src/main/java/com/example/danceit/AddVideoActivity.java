package com.example.danceit;

//import com.example.danceit.Database.VideoViewModel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.danceit.Model.FirebaseManager;
import com.example.danceit.Model.User;
import com.example.danceit.Model.Video;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;

public class AddVideoActivity extends AppCompatActivity {

    private boolean isPrivate; // privacy is public
    FirebaseManager firebaseManager = new FirebaseManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);

        final TextInputLayout textInputUrl = findViewById(R.id.textInput_url);
        final TextInputLayout textInputTags = findViewById(R.id.textInput_tag);
        final Button saveButton = (Button) findViewById(R.id.saveButton);

        addVideo(textInputUrl, textInputTags, saveButton);

    }

    public void addVideo(final TextInputLayout textInputUrl, final TextInputLayout textInputTags, Button saveButton) {

        final Intent intent = new Intent(this,MainActivity.class);

        checkPrivacy(); // checks if video is public or private

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri=Uri.parse(textInputUrl.getEditText().getText().toString().trim());
                if(isURL(textInputUrl.getEditText().getText().toString().trim())) {

                    if (isPrivate) {
                        Video video = new Video(new User("username", "password"),
                                Objects.requireNonNull(textInputUrl.getEditText()).getText().toString().trim(), tagInput_string((textInputTags.getEditText())
                                .getText().toString()), "private");

                        firebaseManager.addPrivate_video("video_urls_private/private_video", video);

                        Toast toast = Toast.makeText(getApplicationContext(), "Url saved as private.", Toast.LENGTH_SHORT);
                        toast.show();

                    }
                    else {
                        Video video = new Video(new User("username", "password"),
                                Objects.requireNonNull(textInputUrl.getEditText()).getText().toString().trim(), tagInput_string((textInputTags.getEditText())
                                .getText().toString()), "private");

                        firebaseManager.addPrivate_video("video_urls_private/private_video", video);

                        video.setPrivacy("public");

                        firebaseManager.addPublic_video("video_url", video);

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
        ArrayList<String> tags = new ArrayList<String>();
        String[] description = textInputTags.split(" ");

        for (int i = 0; i < description.length; i++) {
            tags.add(description[i]);
        }
        return tags;
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