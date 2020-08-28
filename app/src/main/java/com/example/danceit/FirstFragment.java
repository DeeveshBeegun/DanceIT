package com.example.danceit;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import androidx.navigation.fragment.NavHostFragment;

import com.example.danceit.Database.Video_database;
import com.example.danceit.Model.Tag;
import com.example.danceit.Model.User;
import com.example.danceit.Model.Video;
import com.example.danceit.RecyclerViewComponents.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {

    List<Video> video_list;
    private Video_database database;

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
            RecyclerViewAdapter mAdapter = new RecyclerViewAdapter(video_list);
            RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

}