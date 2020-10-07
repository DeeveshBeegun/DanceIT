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
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.danceit.AddTagActivity;
import com.example.danceit.Model.Video;
import com.example.danceit.R;
import com.example.danceit.Sharing.SharingVideoActivity;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

/**
 * This class is used to update the recyclerview for the "Your Library" fragment.
 * It contains a List which is observed by the videoViewModel which is an abstraction of
 * the room database operations. Any changes made to the dataset List will be reflected to the
 * recyclerview in "Your Library" fragment.
 */
public class Firebase_RecyclerViewAdapter extends FirestoreRecyclerAdapter<Video, Firebase_RecyclerViewAdapter.MyViewHolder> {
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    DocumentReference  reference;
    Activity activity;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();


    private String YOUTUBEAPI="AIzaSyAfKidnnKiL3B0yRHR_FRqgMKXg6Z8lT-8";

    /**
     * Constructor of the recyclerview adapter
     * @param options query options
     * @param activity
     */
    public Firebase_RecyclerViewAdapter(@NonNull FirestoreRecyclerOptions<Video> options, Activity activity) {
        super(options);
        this.activity = activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull final MyViewHolder holder, final int position, @NonNull final Video model) {
        holder.textView.setText(model.getUrl());
        holder.chipGroup.animate();
        holder.chipGroup.removeAllViews();

        initialise_chip(holder, position, model);
        createChipAddTagBtn(holder, position, model);
        setVideoThumbnail(holder, createVideoIdFromUrl(model), model);
        createMenuButton(holder, model, position);

    }

    /**
     * This method creates chip objects and sets their text as different tags in the video object.
     * It also defines how the chips should behave when they are clicked.
     * @param holder
     * @param position is the position of the video in the recyclerview
     * @param model video object
     */
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
                                    if(model.getPrivacy().equals("public")) {
                                        reference = database.collection("video_urls").document(getSnapshots().getSnapshot(position).getReference().getId());
                                        reference.update("tags", FieldValue.arrayRemove(model.getTags().get(finalJ)));

                                    }

                                    else if (model.getPrivacy().equals("received")) {


                                        DocumentReference reference = database.collection("video_sent").document(Objects.requireNonNull
                                                (Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                                                .collection("video_received")
                                                .document(getSnapshots().getSnapshot(position).getReference().getId());

                                        reference.update("tags", FieldValue.arrayRemove(model.getTags().get(finalJ)));
                                    }

                                    else {
                                        DocumentReference reference = database.collection("video_urls_private").document(Objects.requireNonNull
                                                (Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                                                .collection("private_video")
                                                .document(getSnapshots().getSnapshot(position).getReference().getId());

                                        reference.update("tags", FieldValue.arrayRemove(model.getTags().get(finalJ)));
                                    }
                                    break;
                                case R.id.update_tag:
                                    Intent intent = new Intent(view.getContext(), UpdateTagActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable("video_update", model);
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

    /**
     * This method creates the addTag button on the recyclerview
     * @param holder
     * @param position is the position of the different videos on the recyclerview
     */
    private void createChipAddTagBtn(final MyViewHolder holder, final int position, final Video model) {
        Chip add_chip = new Chip(holder.context);
        add_chip.setClickable(true);
        add_chip.setText("Add Tag");
        add_chip.setChipIcon(Objects.requireNonNull(ContextCompat.getDrawable(holder.context, R.drawable.ic_action_plus)));

        add_chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTag(v, position, model);
            }
        });

        holder.chipGroup.addView(add_chip);
    }

    public void addTag(View view, int position, Video model) {
        Intent intent=new Intent(view.getContext(), AddTagActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("video_id", getSnapshots().getSnapshot(position).getReference().getId());
        bundle.putParcelable("video_obj", model);
        intent.putExtras(bundle);
        view.getContext().startActivity(intent);
    }

    /**
     * This method sets YouTube thumbnail on the different videos on the recyclerview
     * @param myViewHolder
     * @param videoID videoId created by the url
     * @param model video object
     */
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

    /**
     * This method creates videoId from the different videos using their urls
     * @param model is the video object
     * @return videoId
     */
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


    /**
     * This method creates menu buttons on the different videos on the recyclerview.
     * @param myViewHolder
     */
    public void createMenuButton(final MyViewHolder myViewHolder, final Video model, final int position) {
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
                            case R.id.share_info: // share video url to third party apps
                                //get all video data
                                String dataOut = model.getUrl();
                                dataOut += "\nTags\n";
                                for (int j = 0; j <model.getTags().size() ; j++) {
                                    dataOut += model.getTags().get(j) + " ";
                                }

                                Intent sendIntent = new Intent();
                                sendIntent.setAction(Intent.ACTION_SEND);
                                sendIntent.putExtra(Intent.EXTRA_TEXT, dataOut);
                                sendIntent.setType("text/plain");

                                Intent shareIntent = Intent.createChooser(sendIntent, null);
                                myViewHolder.context.startActivity(shareIntent);

                                break;
                            case R.id.delete_video:

                                if(model.getPrivacy().equals("public")) {
                                    reference = database.collection("video_urls").document(getSnapshots().getSnapshot(position).getReference().getId());
                                    reference.delete();

                                }

                                else if (model.getPrivacy().equals("received")) {

                                    DocumentReference reference = database.collection("video_sent").document(Objects.requireNonNull
                                            (Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                                            .collection("video_received")
                                            .document(getSnapshots().getSnapshot(position).getReference().getId());

                                    reference.delete();
                                }

                                else {
                                    DocumentReference reference = database.collection("video_urls_private").document(Objects.requireNonNull
                                            (Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                                            .collection("private_video")
                                            .document(getSnapshots().getSnapshot(position).getReference().getId());

                                    reference.delete();
                                }

                                break;

                            case R.id.send_info:
                                //allows use to send and transictions to send activity to select users
                                Intent intent = new Intent(activity, SharingVideoActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("video_obj", model);
                                intent.putExtras(bundle);
                                activity.startActivity(intent);

                                break;

                        }
                        return false;
                    }
                });
            }
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout,
                parent, false);
        return new MyViewHolder(view);
    }


    /**
     * This class initialise the different views in the xml file.
     */
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
