package com.example.danceit;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

//import com.example.danceit.Database.VideoViewModel;
import com.example.danceit.Model.Video;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class AddTagActivity extends AppCompatActivity {
    //VideoViewModel videoViewModel;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tag);

        //videoViewModel = new VideoViewModel(getApplication());
        mAuth = FirebaseAuth.getInstance();

        Button addButton = (Button) findViewById(R.id.add_tag_button);
        final TextInputLayout addTag_textInput = (TextInputLayout) findViewById(R.id.textInput_tag);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = getIntent().getExtras();
                assert bundle != null;
                final Video video = bundle.getParcelable("video_obj");

                String video_id = bundle.getString("video_id");
                FirebaseFirestore database = FirebaseFirestore.getInstance();

                assert video != null;
                if (video.getPrivacy().equals("private")) {
                    assert video_id != null;
                    DocumentReference reference = database.collection("video_urls_private").document(Objects.requireNonNull(mAuth.getCurrentUser().getEmail()))
                            .collection("private_video")
                            .document(video_id);
                    reference.update("tags", FieldValue.arrayUnion((Objects.requireNonNull(addTag_textInput.getEditText())).getText().toString()));
                    finish();
                }
                else if (video.getPrivacy().equals("received")) {
                    assert video_id != null;
                    DocumentReference reference = database.collection("video_sent").document(Objects.requireNonNull(mAuth.getCurrentUser().getDisplayName()))
                            .collection("video_received")
                            .document(video_id);
                    reference.update("tags", FieldValue.arrayUnion((Objects.requireNonNull(addTag_textInput.getEditText())).getText().toString()));
                    finish();
                }
                else {
                    assert video_id != null;
                    DocumentReference reference = database.collection("video_urls")
                            .document(video_id);
                    reference.update("tags", FieldValue.arrayUnion((Objects.requireNonNull(addTag_textInput.getEditText())).getText().toString()));
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
