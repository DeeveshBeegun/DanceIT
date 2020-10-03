package com.example.danceit.RecyclerViewComponents;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
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

import java.util.List;
import java.util.Objects;

/**
 * This class is used to update the recyclerview for the "Your Library" fragment.
 * It contains a List which is observed by the videoViewModel which is an abstraction of
 * the room database operations. Any changes made to the dataset List will be reflected to the
 * recyclerview in "Your Library" fragment.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private List<Video> dataset; // store video objects; access by the recyclerview
    VideoViewModel videoViewModel; // create instance of a view model to abstract database operations
    public  Activity activity;

    private String YOUTUBEAPI="AIzaSyAfKidnnKiL3B0yRHR_FRqgMKXg6Z8lT-8";

    /**
     * This class initialise the different views in the xml file.
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView; // displays the url of the videos in the recyclerview cardview
        public ChipGroup chipGroup; // contains all chips which is displayed in each recyclerview cardview
        public Context context;

        public MyViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.text_recycler);
            chipGroup=(ChipGroup) v.findViewById(R.id.chipGroup);
            context=v.getContext();

        }

    }

    /**
     * Constructor of the recyclerview adapter
     * @param myDataset List containing Video objects
     * @param activity
     */
    public RecyclerViewAdapter(LiveData<List<Video>> myDataset, Activity activity) {
        dataset = (List<Video>) myDataset;
        this.activity = activity;
        videoViewModel = new VideoViewModel(activity.getApplication());
    }

    public RecyclerViewAdapter(List<Video> myDataset) {
        dataset = myDataset;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {
        View view = (View) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_layout, viewGroup, false);  // create a new view

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

        myViewHolder.textView.setText((CharSequence) dataset.get(i).getUrl()); // sets url to textview
        myViewHolder.chipGroup.animate();
        myViewHolder.chipGroup.removeAllViews();

        initialise_chip(myViewHolder, i); // create chip and set text and clickable
        createMenuButton(myViewHolder, i); // create menu button and functionality in menu
        setVideoThumbnail(myViewHolder, createVideoIdFromUrl(i), i);
        createChipAddTagBtn(myViewHolder, i);

    }

    /**
     * This method creates chip objects and sets their text as different tags in the video object.
     * It also defines how the chips should behave when they are clicked.
     * @param myViewHolder
     * @param i is the position of the video in the recyclerview
     */

    public void initialise_chip(final MyViewHolder myViewHolder, final int i) {
        for (int j = 0; j <dataset.get(i).getTag_list().size() ; j++) {
            Chip chip=new Chip(myViewHolder.context);
            chip.setText(dataset.get(i).getTag_list().get(j).getDescription()); // set text as video tags
            chip.setClickable(true);
            myViewHolder.chipGroup.addView(chip);

            final int finalJ = j;
            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    PopupMenu popupMenu = new PopupMenu(myViewHolder.context, view);
                    popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.delete_tag:
                                    dataset.get(i).getTag_list().remove(finalJ); // remove tag from database
                                    videoViewModel.update(dataset.get(i));
                                    break;
                                case R.id.update_tag:
                                    Intent intent = new Intent(view.getContext(), UpdateTagActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable("video_update", dataset.get(i));
                                    bundle.putBoolean("video_privacy", true);
                                    bundle.putInt("tag_index", finalJ);
                                    intent.putExtras(bundle); // send information about video object to UpdateTagActivity
                                    view.getContext().startActivity(intent);
                                    break;

                            }

                            return true;
                        }
                    });
                }
            });
        }

    }

    /**
     * This method creates menu buttons on the different videos on the recyclerview.
     * @param myViewHolder
     * @param i is the position of the video in the recyclerview
     */
    public void createMenuButton(final MyViewHolder myViewHolder, final int i) {
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
                            case R.id.share_info: // share video to third party apps
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
                                videoViewModel.delete_video(dataset.get(i)); // delete video on the room database (local database)

                                break;

                        }
                        return false;
                    }
                });
            }
        });
    }

    /**
     * This method sets YouTube thumbnail on the different videos on the recyclerview
     * @param myViewHolder
     * @param videoID videoId created by the url
     * @param i is the position of the different videos on the recyclerview
     */
    public void setVideoThumbnail(final MyViewHolder myViewHolder, final String videoID, final int i) {

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

        // Add the YouTube thumbnail to the app
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

    }

    /**
     * This method creates videoId from the different videos using their urls
     * @param i is the position of the video in the recyclerview
     * @return videoId
     */
    public String createVideoIdFromUrl(final int i) {
        //Get video id from the url
        final String videoID;
        String url=dataset.get(i).getUrl();
        if(url.contains("youtube")){
            videoID=url.split("v=")[1];
        }else
        {
            videoID=url.split("be/")[1];
        }

        return videoID;

    }

    /**
     * This method creates the addTag button on the recyclerview
     * @param myViewHolder
     * @param i is the position of the different videos on the recyclerview
     */
    private void createChipAddTagBtn(MyViewHolder myViewHolder, final int i) {
        Chip add_chip = new Chip(myViewHolder.context);
        add_chip.setClickable(true);
        add_chip.setText("Add Tag");
        add_chip.setChipIcon(Objects.requireNonNull(ContextCompat.getDrawable(myViewHolder.context, R.drawable.ic_action_plus)));

        add_chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTag(v, i);
            }
        });

        myViewHolder.chipGroup.addView(add_chip);
    }

    public void addTag(View view, int i) {

        Intent intent=new Intent(view.getContext(), AddTagActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("video_obj", dataset.get(i));
        bundle.putBoolean("video_privacy", true);
        intent.putExtras(bundle);
        view.getContext().startActivity(intent);
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

