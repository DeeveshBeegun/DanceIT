package com.example.danceit;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import androidx.navigation.fragment.NavHostFragment;

import com.example.danceit.RecyclerViewComponents.RecyclerViewAdapter;

import java.util.ArrayList;

public class FirstFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_first, container, false);

        //Recyclerview adapter creation and adding a layout and adaptor
        RecyclerViewAdapter mAdapter = new RecyclerViewAdapter(Dummy());
        RecyclerView recyclerView=(RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public ArrayList<String> Dummy(){
        ArrayList<String> arrayList=new ArrayList<>();

        for (int i = 0; i <20 ; i++) {
            arrayList.add("https://www.youtube.com/watch?v=FSol3_QZaaI");

        }
        return arrayList;
    }
}