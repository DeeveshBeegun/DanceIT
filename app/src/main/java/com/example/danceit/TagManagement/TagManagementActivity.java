package com.example.danceit.TagManagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import com.example.danceit.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class TagManagementActivity extends AppCompatActivity {

    private ChipGroup chipGroup;//display available tags
    private List<String> tags;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_management);

        tags=new ArrayList<>();

        //Chip group to show all the tags
        chipGroup=(ChipGroup) findViewById(R.id.chip_group_tagmanagement);

        SearchView searchView=(SearchView) findViewById(R.id.tag_management_searchview);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        dummy();
        populateChipgroup();
    }

    private void filter(String text){
        List<Integer> chipids=chipGroup.getCheckedChipIds();

        for (int i=0;i<chipids.size();i++){
            Chip chip=(Chip)chipGroup.getChildAt(i);

            if(!chip.getText().toString().contains(text)){

                chip.setVisibility(View.INVISIBLE);

            }
        }
    }

    private void dummy(){

            tags.add("dance");
            tags.add("joburg");
            tags.add("capetown");
            tags.add("Kizomba");
            tags.add("gwara gwara");



    }

    private void populateChipgroup(){

        for (int i = 0; i <tags.size() ; i++) {

            Chip chip=new Chip(this);
            chip.setText(tags.get(i));
            chip.setVisibility(View.VISIBLE);

            chipGroup.addView(chip);
        }
    }






}