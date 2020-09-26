package com.example.danceit.RecyclerViewComponents;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.danceit.AddTagActivity;
import com.example.danceit.Database.VideoViewModel;
import com.example.danceit.Model.Video;
import com.example.danceit.R;
import com.example.danceit.UpdateTagActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.api.services.youtube.YouTube;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private List<Video> dataset;
    VideoViewModel videoViewModel;
    public  Activity activity;

    private String YOUTUBEAPI="AIzaSyAfKidnnKiL3B0yRHR_FRqgMKXg6Z8lT-8";

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
    public RecyclerViewAdapter(LiveData<List<Video>> myDataset, Activity activity) {
        dataset = (List<Video>) myDataset;
        this.activity = activity;
        videoViewModel = new VideoViewModel(activity.getApplication());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {
        // create a new view
        View v = (View) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_layout, viewGroup, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

        myViewHolder.textView.setText((CharSequence) dataset.get(i).getUrl());
        myViewHolder.chipGroup.animate();
        myViewHolder.chipGroup.removeAllViews();

       for (int j = 0; j <dataset.get(i).getTag_list().size() ; j++) {
            Chip chip=new Chip(myViewHolder.context);
            chip.setText(dataset.get(i).getTag_list().get(j).getDescription());
            chip.setClickable(true);
            myViewHolder.chipGroup.addView(chip);

           final int finalJ = j;
           chip.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(final View view) {
                   //Toast.makeText(myViewHolder.context, "Hello", Toast.LENGTH_SHORT).show();
                   PopupMenu popupMenu = new PopupMenu(myViewHolder.context, view);
                   popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                   popupMenu.show();
                   popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                       @Override
                       public boolean onMenuItemClick(MenuItem menuItem) {
                           switch (menuItem.getItemId()) {
                               case R.id.delete_tag:
                                   dataset.get(i).getTag_list().remove(finalJ);
                                   videoViewModel.update(dataset.get(i));
                                   break;
                               case R.id.update_tag:
                                   Intent intent = new Intent(view.getContext(), UpdateTagActivity.class);
                                   Bundle bundle = new Bundle();
                                   bundle.putParcelable("video_update", dataset.get(i));
                                   bundle.putBoolean("video_privacy", true);
                                   bundle.putInt("tag_index", finalJ);
                                   intent.putExtras(bundle);
                                   view.getContext().startActivity(intent);
                                   break;

                           }

                           return true;
                       }
                   });
               }
           });
        }

        //feature on click for share button
        AppCompatImageButton appCompatImageButton= myViewHolder.textView.getRootView().findViewById(R.id.shareButton);
        appCompatImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //create a  pop menu for options for a video
                final PopupMenu popupMenu=new PopupMenu(myViewHolder.context,v);
                popupMenu.getMenuInflater().inflate(R.menu.video_popoptions,popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch(item.getItemId()){
                            case R.id.share_info:

                                //get all video data
                                String dataOut=dataset.get(i).getUrl().toString();
                                dataOut+="\nTags\n";
                                for (int j = 0; j <dataset.get(i).getTag_list().size() ; j++) {
                                    dataOut+=dataset.get(i).getTag_list().get(j).getDescription()+" ";
                                }

                                Intent sendIntent = new Intent();
                                sendIntent.setAction(Intent.ACTION_SEND);
                                sendIntent.putExtra(Intent.EXTRA_TEXT, dataOut);
                                sendIntent.setType("text/plain");

                                Intent shareIntent = Intent.createChooser(sendIntent, null);
                                myViewHolder.context.startActivity(shareIntent);

                                break;
                            case R.id.delete_video:

                                break;

                        }
                        return false;
                    }
                });
            }
        });

        //Get video id from the url
        final String videoID;
        String url=dataset.get(i).getUrl();
        if(url.contains("youtube")){
            videoID=url.split("v=")[1];
        }else
        {
            videoID=url.split("be/")[1];
        }



        //implementing thumbnail on click to listen for clicks and play video
        YouTubeThumbnailView thumbnailView = (YouTubeThumbnailView) myViewHolder.itemView.getRootView().findViewById(R.id.thumbnail);
        thumbnailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse(dataset.get(i).getUrl().trim()));*/


                //Internal play of the appthe by starting an youtube API
                Intent intent = YouTubeStandalonePlayer.createVideoIntent(activity ,YOUTUBEAPI,videoID );
                activity.startActivity(intent);

                try{
                    activity.startActivity(intent);
                    //v.getContext().startActivity(viewIntent);
                }catch (Exception e){

                    Snackbar snackbar = Snackbar
                            .make(myViewHolder.itemView.getRootView(),"Invalid URL  "+   dataset.get(i).getUrl().trim(), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }
        });


        // Add the youtube thumbnail to the app

        thumbnailView.initialize(YOUTUBEAPI, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {

                        youTubeThumbnailLoader.setVideo(videoID);//set video id

                //youTubeThumbnailView.animate();

            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });




        /*

        ImageView imageView=(ImageView) myViewHolder.itemView.getRootView().findViewById(R.id.image_view);


        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.print(i +dataset.get(i).getUrl().toString());
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse(dataset.get(i).getUrl().trim()));

                try{
                    v.getContext().startActivity(viewIntent);
                }catch (Exception e){

                    Snackbar snackbar = Snackbar
                            .make(myViewHolder.itemView.getRootView(),"Invalid URL  "+   dataset.get(i).getUrl().trim(), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });*/

        Button addButton = (Button) myViewHolder.itemView.getRootView().findViewById(R.id.addTag);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), AddTagActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("video_obj", dataset.get(i));
                bundle.putBoolean("video_privacy", true);
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);

            }
        });

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

