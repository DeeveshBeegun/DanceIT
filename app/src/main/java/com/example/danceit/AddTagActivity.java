package com.example.danceit;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.danceit.Model.FirebaseManager;
import com.example.danceit.Model.Video;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

/**
 * This class is responsible for adding a tag in a video object on the database
 * and for updating the user interface with the new tags added.
 */
public class AddTagActivity extends AppCompatActivity {
    FirebaseManager firebaseManager = new FirebaseManager(); // create an instance of FirebaseManager class; used to add tags to database.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tag);

        Button addButton = (Button) findViewById(R.id.add_tag_button); // add button in the chip group
        final TextInputLayout addTag_textInput = (TextInputLayout) findViewById(R.id.textInput_tag); // text input on popup menu

        addTag(addButton, addTag_textInput);

    }

    /**
     * This method is used to add a tag in the video object in the database.
     * It retrieves a video object which is parsed when the AddTagActivity class is
     * called from the Firebase_RecyclerViewAdapter. The firebaseManager class is
     * used to update the newly added tag on the database.
     * @param addButton opens a pop up menu to add a tag when pressed.
     * @param addTag_textInput is the text input for adding the tag.
     */
    public void addTag(Button addButton, final TextInputLayout addTag_textInput) {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = getIntent().getExtras();
                assert bundle != null;
                final Video video = bundle.getParcelable("video_obj");

                assert video != null;
                String video_id = bundle.getString("video_id");
                String privacy = video.getPrivacy();
                String newTag = Objects.requireNonNull(addTag_textInput.getEditText()).getText().toString();

                assert video_id != null;
                firebaseManager.addTag(video_id, newTag, privacy);

                finish(); // close popup menu
            }
        });

        Button cancelButton = (Button)findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // close popup menu
            }
        });
    }

}
