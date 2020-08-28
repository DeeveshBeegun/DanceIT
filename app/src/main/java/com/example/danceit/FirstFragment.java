package com.example.danceit;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import androidx.navigation.fragment.NavHostFragment;

import com.example.danceit.RecyclerViewComponents.RecyclerViewAdapter;

import java.util.ArrayList;

public class FirstFragment extends Fragment {

    RecyclerView recyclerView;

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
        TabLayout tabLayout=(TabLayout) root.findViewById(R.id.tabLayout);



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