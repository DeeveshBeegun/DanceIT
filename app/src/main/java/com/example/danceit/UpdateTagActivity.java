package com.example.danceit;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.danceit.Database.VideoViewModel;
import com.example.danceit.Model.Tag;
import com.example.danceit.Model.User;
import com.example.danceit.Model.Video;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class UpdateTagActivity extends AppCompatActivity {
    private VideoViewModel videoViewModel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tag);

        videoViewModel = new VideoViewModel(getApplication());

        Button addButton = (Button) findViewById(R.id.update_tag_button);
        final TextInputLayout addTag_textInput = (TextInputLayout) findViewById(R.id.textUpdate_tag);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = getIntent().getExtras();
                boolean isPrivate = bundle.getBoolean("video_privacy");
                Video video = bundle.getParcelable("video_update");


                if(isPrivate) {
                    Tag tag = new Tag(new User("username", "password"), Objects.requireNonNull(addTag_textInput.getEditText()).getText().toString(),false);
                    int tag_index = bundle.getInt("tag_index");
                    assert video != null;
                    ArrayList<Tag> tag_lists = video.getTag_list();
                    tag_lists.set(tag_index, tag);
                    video.setTag_list(tag_lists);
                    videoViewModel.update(video);
                    finish();
                }
                else {
                    String video_id = bundle.getString("video_id");
                    String tag_descp = bundle.getString("tag_descp");
                    FirebaseFirestore database = FirebaseFirestore.getInstance();
                    assert video_id != null;
                    DocumentReference reference = database.collection("video_urls")
                            .document(video_id);
                    reference.update("tags", FieldValue.arrayRemove(tag_descp));
                    reference.update("tags", FieldValue.arrayUnion((addTag_textInput.getEditText()).getText().toString()));
                    finish();
                }

            }
        });

    }
}
