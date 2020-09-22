package com.example.danceit;

import com.example.danceit.Database.VideoViewModel;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.danceit.Database.Video_database;
import com.example.danceit.Model.Tag;
import com.example.danceit.Model.User;
import com.example.danceit.Model.Video;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddVideoActivity extends AppCompatActivity {

    private boolean privacy = false; // privacy is public
    private VideoViewModel videoViewModel;

    RadioButton radiobutton_private;

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

        radiobutton_private = findViewById(R.id.radioButton_private);

        if (radiobutton_private.isChecked()) {
            privacy = true; // privacy is private
        }

        //Get save button
        final Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (privacy == false) {
                    videoViewModel.insert_video(new Video(new User("username", "password"),
                            textInputUrl.getEditText().getText().toString().trim(), tagInput(textInputTags.getEditText()
                            .getText().toString()), privacy));
                    Toast toast = Toast.makeText(getApplicationContext(), "Url saved.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    // Write a message to the database
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("message");

                    myRef.setValue("Hello, World!");
                }
            }
        });
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


}