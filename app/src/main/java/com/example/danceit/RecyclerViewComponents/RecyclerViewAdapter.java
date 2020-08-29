package com.example.danceit.RecyclerViewComponents;

import android.content.Context;
import androidx.annotation.NonNull;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.danceit.Model.Video;
import com.example.danceit.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private List<Video> dataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
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
    public RecyclerViewAdapter(LiveData<List<Video>> myDataset) {
        dataset = (List<Video>) myDataset;
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

        myViewHolder.textView.setText((CharSequence) dataset.get(i).getUrl());
        myViewHolder.chipGroup.animate();
        myViewHolder.chipGroup.removeAllViews();

       for (int j = 0; j <dataset.get(i).getTag_list().size() ; j++) {
           Chip temp=new Chip(myViewHolder.context);
            temp.setText(dataset.get(i).getTag_list().get(j).getDescription());
            myViewHolder.chipGroup.addView(temp);

        }

    }

    public void updateDataset(List<Video> videos) {
        this.dataset = videos;
        notifyDataSetChanged();
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return null!=dataset?dataset.size():0;
    }

}
