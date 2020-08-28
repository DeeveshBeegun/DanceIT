package com.example.danceit.RecyclerViewComponents;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danceit.Model.Video;
import com.example.danceit.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private List<Video> dataset;

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
    public RecyclerViewAdapter(List<Video> myDataset) {
        dataset = myDataset;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {
        // create a new view
        View v = (View) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_layout, viewGroup, false);
        //feature on click for share button
        AppCompatImageButton appCompatImageButton= v.findViewById(R.id.shareButton);
        appCompatImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get all video data
                String dataOut=dataset.get(i).getUrl().toString();
                dataOut+="\nTags\n";

                for (int j = 0; j <dataset.get(i).getTag_list().size() ; j++) {
                    dataOut+=dataset.get(i).getTag_list().get(j).getDescription()+" ";

                }

                //copy video data to clip board
                ClipboardManager clipboardManager=(ClipboardManager) viewGroup.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData=ClipData.newPlainText("video data",dataOut);
                clipboardManager.setPrimaryClip(clipData);

                Snackbar snackbar = Snackbar
                        .make(viewGroup.getRootView(),"Data saved to clipboard!!!", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

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
        dataset.addAll(videos);
        notifyDataSetChanged();
    }

    public void deleteDataset(List<Video> videos) {
        dataset.clear();
        notifyDataSetChanged();
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataset.size();
    }

}
