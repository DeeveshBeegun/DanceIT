package com.example.danceit.Sharing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import com.example.danceit.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class SharingVideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing_video);
        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<String> ls=new ArrayList<>();
        ls.add("John");
        ls.add("Joe");
        ls.add("Bob");
        ls.add("Ben");
        ls.add("Jane");
        ls.add("Seth");
        ls.add("Carol");
        ls.add("Lebron");




        final MyAdapter myAdapter =new MyAdapter(ls);
        recyclerView.setAdapter(myAdapter);

        SearchView searchView=(SearchView) findViewById(R.id.searchView);
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