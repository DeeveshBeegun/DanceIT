package com.example.danceit;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.danceit.Model.FirebaseManager;
import com.example.danceit.Model.Video;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class AddTagActivity extends AppCompatActivity {

    FirebaseManager firebaseManager = new FirebaseManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tag);

        Button addButton = (Button) findViewById(R.id.add_tag_button);
        final TextInputLayout addTag_textInput = (TextInputLayout) findViewById(R.id.textInput_tag);

        addTag(addButton, addTag_textInput);


    }

    public void addTag(Button addButton, final TextInputLayout addTag_textInput) {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = getIntent().getExtras();
                assert bundle != null;
                final Video video = bundle.getParcelable("video_obj");

                String video_id = bundle.getString("video_id");

                assert video != null;
                String privacy = video.getPrivacy();

                String newTag = Objects.requireNonNull(addTag_textInput.getEditText()).getText().toString();

                if (privacy.equals("private")) {
                    assert video_id != null;
                    firebaseManager.addTag(video_id, newTag,
                            "video_urls_private/private_video", privacy);

                    finish();
                }
                else if (video.getPrivacy().equals("received")) {
                    assert video_id != null;
                    firebaseManager.addTag(video_id, newTag,
                            "video_sent/video_received", privacy);

                    finish();
                }
                else {
                    assert video_id != null;
                    firebaseManager.addTag(video_id, newTag,
                            "video_url", privacy);

                    finish();
                }
            }
        });

        Button cancelButton = (Button)findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
