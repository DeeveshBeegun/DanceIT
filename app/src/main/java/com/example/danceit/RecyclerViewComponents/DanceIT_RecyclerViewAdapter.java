package com.example.danceit.RecyclerViewComponents;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.danceit.AddTagActivity;
import com.example.danceit.Model.Video;
import com.example.danceit.R;
import com.example.danceit.UpdateTagActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;


public class DanceIT_RecyclerViewAdapter extends FirestoreRecyclerAdapter<Video, DanceIT_RecyclerViewAdapter.MyViewHolder> {
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    DocumentReference  reference;
    Activity activity;

    private String YOUTUBEAPI="AIzaSyAfKidnnKiL3B0yRHR_FRqgMKXg6Z8lT-8";

    public DanceIT_RecyclerViewAdapter(@NonNull FirestoreRecyclerOptions<Video> options, Activity activity) {
        super(options);
        this.activity = activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull final MyViewHolder holder, final int position, @NonNull final Video model) {
        holder.textView.setText(model.getUrl());
        holder.chipGroup.animate();
        holder.chipGroup.removeAllViews();

        initialise_chip(holder, position, model);
        createChipAddTagBtn(holder, position);
        setVideoThumbnail(holder, createVideoIdFromUrl(model), model);

    }


    private void initialise_chip(final MyViewHolder holder, final int position, final Video model) {
        for (int j = 0; j <model.getTags().size() ; j++) {
            Chip chip = new Chip (holder.context);
            chip.setText(model.getTags().get(j));
            chip.setClickable(true);
            holder.chipGroup.addView(chip);

            final int finalJ = j;
            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    PopupMenu popupMenu = new PopupMenu(holder.context, view);
                    popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.delete_tag:
                                    reference = database.collection("video_urls").document(getSnapshots().getSnapshot(position).getReference().getId());
                                    reference.update("tags", FieldValue.arrayRemove(model.getTags().get(finalJ)));
                                    break;
                                case R.id.update_tag:
                                    Intent intent = new Intent(view.getContext(), UpdateTagActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable("video_update", model);
                                    bundle.putBoolean("video_privacy", false);
                                    bundle.putString("video_id", getSnapshots().getSnapshot(position).getReference().getId());
                                    bundle.putString("tag_descp", model.getTags().get(finalJ));
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
    }

    private void createChipAddTagBtn(final MyViewHolder holder, final int position) {
        Chip add_chip = new Chip(holder.context);
        add_chip.setClickable(true);
        add_chip.setText("Add Tag");
        add_chip.setChipIcon(Objects.requireNonNull(ContextCompat.getDrawable(holder.context, R.drawable.ic_action_plus)));

        add_chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTag(v, position);
            }
        });

        holder.chipGroup.addView(add_chip);
    }

    public void addTag(View view, int position) {
        Intent intent=new Intent(view.getContext(), AddTagActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("video_privacy", false);
        bundle.putString("video_id", getSnapshots().getSnapshot(position).getReference().getId());
        intent.putExtras(bundle);
        view.getContext().startActivity(intent);
    }

    public void setVideoThumbnail(final MyViewHolder myViewHolder, final String videoID, final Video model) {

        //implementing thumbnail on click to listen for clicks and play video
        YouTubeThumbnailView thumbnailView = (YouTubeThumbnailView) myViewHolder.itemView.getRootView().findViewById(R.id.thumbnail);
        thumbnailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Internal play of the appthe by starting an youtube API
                Intent intent = YouTubeStandalonePlayer.createVideoIntent(activity ,YOUTUBEAPI,videoID );
                activity.startActivity(intent);

                try{
                    activity.startActivity(intent);

                }catch (Exception e){

                    Snackbar snackbar = Snackbar
                            .make(myViewHolder.itemView.getRootView(),"Invalid URL  "+   model.getUrl().trim(), Snackbar.LENGTH_LONG);
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

    public String createVideoIdFromUrl(Video model) {
        //Get video id from the url
        final String videoID;
        String url=model.getUrl();
        if(url.contains("youtube")){
            videoID=url.split("v=")[1];
        }else
        {
            videoID=url.split("be/")[1];
        }

        return videoID;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout,
                parent, false);
        return new MyViewHolder(view);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public ChipGroup chipGroup;
        public Context context;
        public Button addButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_recycler);
            chipGroup = (ChipGroup) itemView.findViewById(R.id.chipGroup);
            context = itemView.getContext();
//            addButton = itemView.getRootView().findViewById(R.id.addTag);
        }
    }
}
