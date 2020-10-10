package com.example.danceit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import com.example.danceit.Model.TagManagement;
import com.example.danceit.Model.Video;

import java.util.List;

public class TagManagementActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_management2);
        Intent intent = getIntent();
        List<Video> allVideos = intent.getExtras().getParcelableArrayList("Videos");

        TagManagement tagManagement = new TagManagement (allVideos, getApplication());


    }
}
