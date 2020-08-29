package com.example.danceit;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;


import com.example.danceit.Database.Video_database;
import com.example.danceit.Model.Tag;
import com.example.danceit.Model.User;
import com.example.danceit.Model.Video;

import com.example.danceit.RecyclerViewComponents.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment  {

    RecyclerView recyclerView;



    List<Video> video_list;
    private Video_database database;
    RecyclerViewAdapter mAdapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        database = Video_database.getInstance(getContext());

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_first, container, false);

        //Recyclerview adapter creation and adding a layout and adaptor
            video_list = database.video_dao().getAll();
            mAdapter = new RecyclerViewAdapter(video_list);
            RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));





        //SearchView  to get search input by the user
       /* SearchView  searchView=(SearchView) root.findViewById(R.id.searchview);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });*/



        //Get tab layout and have relavent processes...Tab is
       // TabLayout tabLayout=(TabLayout) root.findViewById(R.id.tabLayout);



        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.updateDataset(database.video_dao().getRecentVideo());

    }

}