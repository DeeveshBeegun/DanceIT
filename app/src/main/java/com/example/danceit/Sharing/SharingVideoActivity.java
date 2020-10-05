package com.example.danceit.Sharing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import com.example.danceit.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SharingVideoActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    CollectionReference reference = database.collection("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing_video);
        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

//        reference.add("bla");


        List<String> users = new ArrayList<>();
        users.add("Joe");
        users.add("Bob");
        users.add("Ben");
        users.add("Jane");
        users.add("Seth");
        users.add("Carol");
        users.add("Lebron");

        final MyAdapter myAdapter =new MyAdapter(users);
        recyclerView.setAdapter(myAdapter);

        final SearchView searchView=(SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                myAdapter.getFilter().filter(s);
                return true;
            }
        });


        FloatingActionButton floatingActionButton=(FloatingActionButton) findViewById(R.id.sendFloatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Send video
            }
        });





    }
}