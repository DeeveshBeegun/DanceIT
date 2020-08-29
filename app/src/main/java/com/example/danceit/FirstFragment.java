package com.example.danceit;


import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.danceit.Database.VideoViewModel;
import com.example.danceit.Database.Video_database;
import com.example.danceit.Model.Video;

import com.example.danceit.RecyclerViewComponents.RecyclerViewAdapter;

import java.util.List;

public class FirstFragment extends Fragment  {

    LiveData<List<Video>> video_list;
    private Video_database database;
    RecyclerViewAdapter mAdapter;
    private VideoViewModel videoViewModel;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        videoViewModel = new ViewModelProvider((ViewModelStoreOwner) this).get(VideoViewModel.class);
        videoViewModel.getAll().observe((LifecycleOwner) this, new Observer<List<Video>>() {
            @Override
            public void onChanged(List<Video> videos) {
                mAdapter.updateDataset(videos);
            }
        });

        database = Video_database.getInstance(getContext());

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_first, container, false);

        //Recyclerview adapter creation and adding a layout and adaptor
           // video_list = database.video_dao().getAll();
            mAdapter = new RecyclerViewAdapter(video_list);
            RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}