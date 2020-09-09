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
                Tag tag = new Tag(new User("username", "password"), Objects.requireNonNull(addTag_textInput.getEditText()).getText().toString(),false);

                Bundle bundle = getIntent().getExtras();
                assert bundle != null;
                Video video = bundle.getParcelable("video_update");
                assert video != null;
                String tag_descp = bundle.getString("tag_descp");

                int tag_index = bundle.getInt("tag_index");


                ArrayList<Tag> tag_lists = video.getTag_list();
                tag_lists.set(tag_index, tag);
                video.setTag_list(tag_lists);


                videoViewModel.update(video);
                finish();
            }
        });

    }
}
