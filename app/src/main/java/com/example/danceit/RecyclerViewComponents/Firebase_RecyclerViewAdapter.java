package com.example.danceit.RecyclerViewComponents;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.danceit.AddTagActivity;
import com.example.danceit.Model.FirebaseManager;
import com.example.danceit.Model.Video;
import com.example.danceit.R;
import com.example.danceit.Sharing.SharingVideoActivity;
import com.example.danceit.UpdateTagActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * This class is used to update the recyclerview for the "Your Library" fragment.
 * It contains a List which is observed by the videoViewModel which is an abstraction of
 * the room database operations. Any changes made to the dataset List will be reflected to the
 * recyclerview in "Your Library" fragment.
 */
public class Firebase_RecyclerViewAdapter extends FirestoreRecyclerAdapter<Video, Firebase_RecyclerViewAdapter.MyViewHolder> {

    Activity activity;
    List<Video> video_list = new ArrayList<>();
    HashMap<Integer, Video> videoHashMap= new HashMap<>();

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    FirebaseManager firebaseManager = new FirebaseManager();

    public static boolean selection=false;
    public static List<Integer> selected=new ArrayList<>();
    public ArrayList<MyViewHolder> myViewHolders=new ArrayList<>();

    private String YOUTUBEAPI="AIzaSyAfKidnnKiL3B0yRHR_FRqgMKXg6Z8lT-8";

    /**
     * Constructor of the recyclerview adapter
     * @param options query options
     * @param activity
     */
    public Firebase_RecyclerViewAdapter(@NonNull FirestoreRecyclerOptions<Video> options, Activity activity) {
        super(options);
        this.activity = activity;
        selection=false;
    }

    @Override
    protected void onBindViewHolder(@NonNull final MyViewHolder holder, final int position, @NonNull final Video model) {
        //holder.textView.setText(model.getUrl());
        holder.chipGroup.animate();
        holder.chipGroup.removeAllViews();
        holder.itemView.setTag(model.getParseId());

        initialise_chip(holder, position, model);
        createChipAddTagBtn(holder, position, model);
        setVideoThumbnail(holder, createVideoIdFromUrl(model), model);
        createMenuButton(holder, model, position);

        checkBox_listener(holder, position);

        setPrivacyTextView(model, holder);


    }

    public void checkBox_listener(MyViewHolder holder, final int position) {
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    firebaseManager.getPrivate_videoReference()
                            .document(getSnapshots().getSnapshot(position).getId())
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document != null) {
                                    Video video = task.getResult().toObject(Video.class);
                                    videoHashMap.put(position, video);
                                }

                            }
                        }
                    });

                }
                else
                    if(videoHashMap.size() > 0)
                        videoHashMap.remove(position);
            }
        });

    }

    public void setPrivacyTextView(Video model, MyViewHolder holder) {
        if (model.getBeingShared().equals("yes"))
            holder.privacyTextView.setText("public");
        else
            holder.privacyTextView.setText("private");

    }


    /**
     * This method creates chip objects and sets their text as different tags in the video object.
     * It also defines how the chips should behave when they are clicked.
     * @param holder
     * @param position is the position of the video in the recyclerview
     * @param model video object
     */
    private void initialise_chip(final MyViewHolder holder, final int position, final Video model) {

        myViewHolders.add(holder);

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
                                        firebaseManager.deleteTag(getSnapshots().getSnapshot(position).getReference().getId(), model.getTags().get(finalJ), model.getPrivacy());
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


         /* MaterialCardView materialCardView=(MaterialCardView) myViewHolder.itemView.getRootView().findViewById(R.id.materialCardView);
        materialCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selection=true;
                myViewHolder.checkBox.setChecked(true);
                myViewHolder.checkBox.setVisibility(View.VISIBLE);
                //notifyDataSetChanged();
                return true;
            }
        });*/

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
        AppCompatImageButton appCompatImageButton= myViewHolder.chipGroup.getRootView().findViewById(R.id.shareButton);
        appCompatImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //create a  pop menu for options for a video
                final PopupMenu popupMenu=new PopupMenu(myViewHolder.context,v);
                popupMenu.getMenuInflater().inflate(R.menu.video_popoptions,popupMenu.getMenu());
                popupMenu.show();

                if(model.getBeingShared().equals("yes")) {
                    popupMenu.getMenu().findItem(R.id.make_public).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.make_private).setVisible(true);

                }

                if(model.getPrivacy().equals("public") || model.getPrivacy().equals("received")) {
                    popupMenu.getMenu().findItem(R.id.select_vid).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.send_info).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.make_public).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.make_private).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.save_video).setVisible(true);
                }

                if (!mAuth.getCurrentUser().getEmail().equals(model.getVideoUploader())) {
                    popupMenu.getMenu().findItem(R.id.delete_video).setVisible(false);
                }


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
                                String parseId = (String) myViewHolder.itemView.getTag();
                                    firebaseManager.deleteVideo(parseId, model.getPrivacy());

                                firebaseManager.getPrivate_videoReference().whereEqualTo("videoId", model.getVideoId())
                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                                model.setBeingShared("no");
                                                firebaseManager.getPrivate_videoReference().document(document.getId()).set(model);

                                            }
                                        }

                                    }
                                });

                                break;

                            case R.id.send_info:
                                //allows use to send and transictions to send activity to select users
                                Intent intent = new Intent(activity, SharingVideoActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("video_obj", model);
                                bundle.putString("single_user", "single_user");
                                intent.putExtras(bundle);
                                activity.startActivity(intent);

                                break;

                            case R.id.select_vid:
                                myViewHolder.checkBox.setVisibility(View.VISIBLE);
                                myViewHolder.checkBox.setChecked(true);

                                selection=true;
                                updateUI();
                                notifyDataSetChanged();
                                break;

                            case R.id.make_public:
                                model.setBeingShared("yes");
                                firebaseManager.getPrivate_videoReference()
                                        .document(getSnapshots().getSnapshot(position).getId()).set(model); // update private video

                                model.setPrivacy("public");
                                firebaseManager.addPublic_video(model);
                                setPrivacyTextView(model, myViewHolder);
                                Toast toast = Toast.makeText(activity, "Video made public.", Toast.LENGTH_SHORT);
                                toast.show();
                                break;

                            case R.id.make_private:
                                firebaseManager.getPublic_videoReference().whereEqualTo("videoId", model.getVideoId())
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult()))
                                                firebaseManager.deleteVideo(document.getId(), "public");
                                            model.setBeingShared("no");
                                            firebaseManager.getPrivate_videoReference()
                                                    .document(getSnapshots().getSnapshot(position).getId()).set(model);
                                        }

                                    }
                                });

                                setPrivacyTextView(model, myViewHolder);
                                toast = Toast.makeText(activity, "Video made private.", Toast.LENGTH_SHORT);
                                toast.show();

                                break;

                            case R.id.save_video:
                                firebaseManager.addPrivate_video(model);
                                toast = Toast.makeText(activity, "Video saved to library.", Toast.LENGTH_SHORT);
                                toast.show();

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

    private void addCheckBox(boolean  check){

        if(check){
            for (int i = 0; i <myViewHolders.size() ; i++) {

                myViewHolders.get(i).checkBox.setVisibility(View.VISIBLE);


            }
        }else{

            for (int i = 0; i <myViewHolders.size() ; i++) {

                myViewHolders.get(i).checkBox.setVisibility(View.INVISIBLE);

            }



        }
    }

    private void updateUI(){

        //add check boxes to view
        addCheckBox(true);

        final FloatingActionButton fabButton =(FloatingActionButton) activity.findViewById(R.id.fab);
        final FloatingActionButton cancelSelection =(FloatingActionButton) activity.findViewById(R.id.floatingActionButton);
        final FloatingActionButton sendButton =(FloatingActionButton) activity.findViewById(R.id.floatingActionButton2);


        cancelSelection.setVisibility(View.VISIBLE);
        sendButton.setVisibility(View.VISIBLE);


        fabButton.setVisibility(View.INVISIBLE);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video_list.addAll(videoHashMap.values());
                //allows use to send and transictions to send activity to select users
                Intent intent = new Intent(activity, SharingVideoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("video_list", (ArrayList<? extends Parcelable>) video_list);
                intent.putExtras(bundle);

                activity.startActivity(intent);

            }
        });

        //when cancel reset view back to normal
        cancelSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ;
                for (int i = 0; i <myViewHolders.size() ; i++)
                    myViewHolders.get(i).checkBox.setChecked(false);

                addCheckBox(false);//remove check boxes
                cancelSelection.setVisibility(View.INVISIBLE);
                sendButton.setVisibility(View.INVISIBLE);
                fabButton.setVisibility(View.VISIBLE);

            }
        });
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public ChipGroup chipGroup;
        public Context context;
        public Button addButton;
        public CheckBox checkBox;
        public TextView privacyTextView;
        public int pos;


        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
           // textView = (TextView) itemView.findViewById(R.id.text_recycler);
            chipGroup = (ChipGroup) itemView.findViewById(R.id.chipGroup);
            checkBox=(CheckBox) itemView.findViewById(R.id.checkbox_multiple_sel);
            privacyTextView = (TextView) itemView.findViewById(R.id.privacy_textView);

            if(selection){
                checkBox.setVisibility(View.VISIBLE);
            }

            context = itemView.getContext();
//            addButton = itemView.getRootView().findViewById(R.id.addTag);
        }
    }

}
