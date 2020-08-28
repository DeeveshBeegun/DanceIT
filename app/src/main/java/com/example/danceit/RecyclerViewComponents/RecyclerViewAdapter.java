package com.example.danceit.RecyclerViewComponents;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.danceit.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private ArrayList<String> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        public TextView textView;
        public ChipGroup chipGroup;
        public Context context;

        public MyViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.text_recycler);
            chipGroup=(ChipGroup) v.findViewById(R.id.chipGroup);
            context=v.getContext();
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    // add  arraylist of videos
    public RecyclerViewAdapter(ArrayList<String> myDataset) {
        mDataset = myDataset;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // create a new view
        View v = (View) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_layout, viewGroup, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.textView.setText(mDataset.get(i));
        myViewHolder.chipGroup.removeAllViews();
        //loop add all the tags chipGroup
        for (int j = 0; j <5 ; j++) {
            Chip temp=new Chip(myViewHolder.context);
            temp.setText("Dance");
            myViewHolder.chipGroup.addView(temp);

        }
       /*myViewHolder.chipGroup.removeAllViews();
        Chip temp=new Chip(myViewHolder.context);
        temp.setText("Dance");
        myViewHolder.chipGroup.addView(temp);*/



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
