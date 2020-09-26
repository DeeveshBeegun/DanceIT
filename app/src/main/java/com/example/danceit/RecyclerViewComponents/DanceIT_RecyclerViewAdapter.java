package com.example.danceit.RecyclerViewComponents;

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
import androidx.recyclerview.widget.RecyclerView;

import com.example.danceit.Model.Video;
import com.example.danceit.R;
import com.example.danceit.UpdateTagActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;


public class DanceIT_RecyclerViewAdapter extends FirestoreRecyclerAdapter<Video, DanceIT_RecyclerViewAdapter.videoHolder> {
    Video temp;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    DocumentReference  reference;


    public DanceIT_RecyclerViewAdapter(@NonNull FirestoreRecyclerOptions<Video> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final videoHolder holder, final int position, @NonNull final Video model) {
        holder.textView.setText(model.getUrl());
        holder.chipGroup.animate();
        holder.chipGroup.removeAllViews();

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

    @NonNull
    @Override
    public videoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout,
                parent, false);
        return new videoHolder(view);
    }

    class videoHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public ChipGroup chipGroup;
        public Context context;

        public videoHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_recycler);
            chipGroup = (ChipGroup) itemView.findViewById(R.id.chipGroup);
            context = itemView.getContext();
        }
    }
}
