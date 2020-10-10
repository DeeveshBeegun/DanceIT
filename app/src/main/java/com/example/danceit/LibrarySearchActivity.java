/**
 * This activity handles the library search queries
 *
 * @author Bohlale Motsieloa (MTSBOH002)
 * @date: 29/09/2020
 * @version: 1.0
 */
package com.example.danceit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.example.danceit.Model.Search;
import com.example.danceit.Model.Tag;
import com.example.danceit.Model.User;
import com.example.danceit.Model.Video;
import com.example.danceit.RecyclerViewComponents.Firebase_RecyclerViewAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LibrarySearchActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Firebase_RecyclerViewAdapter adapter;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference reference = database.collection("video_urls_private").
                document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail())).
                collection("private_video");


        // Getting intent, user search query and all the videos in the user's library from the Main Activity
        Intent intent = getIntent();
        String [] searchKeywords = intent.getExtras().getStringArray("Search Keywords");
        ArrayList<Video> allVideos = intent.getExtras().getParcelableArrayList("Videos");
        System.out.println("hshjdhksjdjckjskdjkjdkjcksdjck");
        System.out.println(allVideos.size());

        //Create Search object to get search results and set the adapter
        Search search = new Search (searchKeywords, allVideos);
        ArrayList<Video> searchResults = search.searchResults();
        System.out.println(searchResults.size());

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        Query query = reference.limit(100);

        //Query query = reference.whereArrayContainsAny("tags", searchResults).limit(100);

        FirestoreRecyclerOptions<Video> options = new FirestoreRecyclerOptions.Builder<Video>()
                .setQuery(query, Video.class)
                .build();
        adapter = new Firebase_RecyclerViewAdapter(options, this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();;

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}