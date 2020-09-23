package com.example.danceit;

import com.example.danceit.Database.VideoViewModel;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Objects;

public class AddVideoActivity extends AppCompatActivity {

    private boolean isPrivate = false; // privacy is public
    private VideoViewModel videoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);

        saveInput();

    }

    public void saveInput() {

        videoViewModel = new VideoViewModel(getApplication());

        final TextInputLayout textInputUrl = findViewById(R.id.textInput_url);
        final TextInputLayout textInputTags = findViewById(R.id.textInput_tag);
        checkPrivacy();
        //Get save button
        final Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri=Uri.parse(textInputUrl.getEditText().getText().toString().trim());

                if(isPrivate) {
                    if(isURL(textInputUrl.getEditText().getText().toString().trim())) {
                        videoViewModel.insert_video(new Video(new User("username", "password"),
                                Objects.requireNonNull(textInputUrl.getEditText()).getText().toString().trim(), tagInput(Objects.requireNonNull(textInputTags.getEditText())
                                .getText().toString()), isPrivate));
                        Toast toast = Toast.makeText(getApplicationContext(), "Url saved as private.", Toast.LENGTH_SHORT);
                        toast.show();
                    }else {

                        Toast toast = Toast.makeText(getApplicationContext(), "Url not valid", Toast.LENGTH_SHORT);
                        toast.show();

                    }
                }
                else {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("Video URLs database");

                    Video video = new Video(new User("username", "password"),
                            Objects.requireNonNull(textInputUrl.getEditText()).getText().toString().trim(), tagInput(Objects.requireNonNull(textInputTags.getEditText())
                            .getText().toString()), isPrivate);

                    String videoId = reference.push().getKey();

                    assert videoId != null;
                    reference.child(videoId).setValue(video);

                    Toast toast = Toast.makeText(getApplicationContext(), "Url saved as public.", Toast.LENGTH_SHORT);
                    toast.show();
                }

                }

        });
    }

    public boolean isURL(String url) {
       if(url.contains("http")&& (url.contains("youtu.be")||url.contains("youtu.be"))){
           return true;
       }else {
           return false;
       }
    }

    public ArrayList<Tag> tagInput(String textInputTags) {
        ArrayList<Tag> tag_lists = new ArrayList<Tag>();
        String[] description = textInputTags.split(" ");

        for (int i = 0; i < description.length; i++) {
                Tag tag = new Tag(new User("username", "password"), description[i], false);
                tag_lists.add(tag);
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