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

    ListView listView;
    List searchResults = new ArrayList<String>();
    ArrayAdapter adapter;
    RecyclerViewAdapter mAdapter;
    VideoViewModel videoViewModel;
    Tag [] searchTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String[] searchKeywords = intent.getExtras().getStringArray("Search Keywords");

        searchTags = new Tag[searchKeywords.length];

        for (int i=0; i<searchKeywords.length; i++ ){
            String tag = searchKeywords[i];
            Tag newTag = new Tag(new User(tag, "d"), tag, false );
            searchTags[i]=newTag;
        }

        List<Video> allVideos = intent.getExtras().getParcelableArrayList("Videos");

        //listView = (ListView)findViewById(R.id.list_view);

        Search search = new Search (searchTags, allVideos);
        ArrayList<Video> searchResults = search.searchResults();

        //ArrayList<String> searchResults1 = new ArrayList<>() ;
        //searchResults1.add("Tes");

        /*if(searchResults.size()!=0){
            for(int i=0; i<searchResults.size(); i++){
                //System.out.println(searchResults.get(i).toString());
                searchResults1.add(searchResults.get(i).toString());
            }
        }
        else{
            searchResults1.add("No results found");
        }*/

        mAdapter = new RecyclerViewAdapter(searchResults);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //add videos
        //adapter = new ArrayAdapter(LibrarySearchActivity.this, android.R.layout.simple_list_item_1, searchResults1);
        //listView.setAdapter(adapter);

    }
}