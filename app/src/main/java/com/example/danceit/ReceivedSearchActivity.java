package com.example.danceit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.danceit.Model.FirebaseManager;
import com.example.danceit.Model.Search;
import com.example.danceit.Model.Video;
import com.example.danceit.RecyclerViewComponents.Firebase_RecyclerViewAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

/**
 * This class is responsible for searching for videos in a the Received Tab (ThirdFragment)
 * and displaying the results on the mobile screen.
 */
public class ReceivedSearchActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Firebase_RecyclerViewAdapter adapter;
    private FirebaseAuth mAuth;
    ArrayList<String> searchResults = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseManager firebaseManager = new FirebaseManager();

        // Getting intent, user search query and all the videos in the received database from the Main Activity
        Intent intent = getIntent();
        String [] searchKeywords = intent.getExtras().getStringArray("Search Keywords");
        ArrayList<Video> allVideos = intent.getExtras().getParcelableArrayList("Videos");

        //Create Search object to get search results and set the adapter
        Search search = new Search (searchKeywords, allVideos);
        ArrayList<String> searchResults = search.searchResults();

        if(!searchResults.isEmpty()){
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

            Query query = firebaseManager.getReceive_videoReference().whereIn("videoId", searchResults);

            FirestoreRecyclerOptions<Video> options = new FirestoreRecyclerOptions.Builder<Video>()
                    .setQuery(query, new SnapshotParser<Video>() {
                        @NonNull
                        @Override
                        public Video parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                            Video video = snapshot.toObject(Video.class);
                            return video;
                        }
                    })
                    .setLifecycleOwner(this)
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

    /**
     * This methods displays the adapter on screen and causes it to listen if
     * there are any changes in the database. If no search results are found the adapter
     * is not displayed.
     */
    @Override
    public void onStart() {
        super.onStart();
        if(!searchResults.isEmpty()){
            adapter.startListening();
        }
    }

    /**
     * This methods ceases to display the adapter on screen and causes it to stop listening
     * for any changes in the database. If no search results are found adapter.stopListening() is
     * not executed since adapter.startListening() was not executed before it.
     */
    @Override
    public void onStop() {
        super.onStop();
        if(!searchResults.isEmpty()){
            adapter.stopListening();
        }
    }
}