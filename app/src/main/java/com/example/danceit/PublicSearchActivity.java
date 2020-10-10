package com.example.danceit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.danceit.Model.Search;
import com.example.danceit.Model.Tag;
import com.example.danceit.Model.User;
import com.example.danceit.Model.Video;
import com.example.danceit.RecyclerViewComponents.Firebase_RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class PublicSearchActivity extends AppCompatActivity {

    Firebase_RecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_search2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Getting intent, user search query and all the videos in the user's library from the Main Activity
        Intent intent = getIntent();
        String[] searchKeywords = intent.getExtras().getStringArray("Search Keywords");
        ArrayList<Video> allVideos = intent.getExtras().getParcelableArrayList("Videos");

        //Create Search object to get search results and set the adapter
        Search search = new Search (searchKeywords, allVideos);
        //System.out.println("Just before searchResults1");
        ArrayList<Video> searchResults = search.searchResults();

        //mAdapter = new RecyclerViewAdapter(searchResults);
        //RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //recyclerView.setAdapter(mAdapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}
