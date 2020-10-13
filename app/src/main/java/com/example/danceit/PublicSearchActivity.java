package com.example.danceit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.danceit.Model.Search;

import com.example.danceit.Model.Video;
import com.example.danceit.RecyclerViewComponents.Firebase_RecyclerViewAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class PublicSearchActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Firebase_RecyclerViewAdapter adapter;
    private FirebaseAuth mAuth;
    ArrayList<String> searchResults = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_search2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference reference = database.collection("video_url");


        // Getting intent, user search query and all the videos in the public database from the Main Activity
        Intent intent = getIntent();
        String [] searchKeywords = intent.getExtras().getStringArray("Search Keywords");
        ArrayList<Video> allVideos = intent.getExtras().getParcelableArrayList("Videos");

        //Create Search object to get search results and set the adapter
        Search search = new Search (searchKeywords, allVideos);
        searchResults = search.searchResults();

        if(!searchResults.isEmpty()){
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

            Query query = reference.whereIn("videoId", searchResults);

            FirestoreRecyclerOptions<Video> options = new FirestoreRecyclerOptions.Builder<Video>()
                    .setQuery(query, Video.class)
                    .build();

            adapter = new Firebase_RecyclerViewAdapter(options, this);

            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));

        }
        else{
            TextView noResults = (TextView) findViewById(R.id.noSearchResults);
            TextView word = (TextView) findViewById(R.id.search_keywords);
            String searchKeyword = "";
            for(String words : searchKeywords){
                searchKeyword=searchKeyword + " "+words;
            }
            noResults.setText(R.string.NoResults);
            word.setText("\""+searchKeyword+" \"");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!searchResults.isEmpty()){
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(!searchResults.isEmpty()){
            adapter.stopListening();
        }
    }

}
