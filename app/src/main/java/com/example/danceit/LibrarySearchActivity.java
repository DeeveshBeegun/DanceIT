/**
 * This activity handles the library search queries
 *
 * @author Bohlale Motsieloa (MTSBOH002)
 * @date: 29/09/2020
 * @version: 1.0
 */
package com.example.danceit;

import android.content.Intent;
import android.os.Bundle;

import com.example.danceit.Database.VideoViewModel;
import com.example.danceit.Database.Video_database;
import com.example.danceit.Model.Search;
import com.example.danceit.Model.Tag;
import com.example.danceit.Model.User;
import com.example.danceit.Model.Video;
import com.example.danceit.RecyclerViewComponents.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class LibrarySearchActivity extends AppCompatActivity {

    RecyclerViewAdapter mAdapter;
    Tag [] searchTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Getting intent, user search query and all the videos in the user's library from the Main Activity
        Intent intent = getIntent();
        String[] searchKeywords = intent.getExtras().getStringArray("Search Keywords");
        List<Video> allVideos = intent.getExtras().getParcelableArrayList("Videos");

        //Creating temporary tags from the user's search query words
        searchTags = new Tag[searchKeywords.length];

        for (int i=0; i<searchKeywords.length; i++ ){
            String tag = searchKeywords[i];
            Tag newTag = new Tag(new User(tag, "d"), tag, false );
            searchTags[i]=newTag;
        }

        //Create Search object to get search results and set the adapter
        Search search = new Search (searchTags, allVideos);
        ArrayList<Video> searchResults = search.searchResults();

        mAdapter = new RecyclerViewAdapter(searchResults);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}