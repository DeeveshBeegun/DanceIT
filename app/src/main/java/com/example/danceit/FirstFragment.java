package com.example.danceit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.danceit.Model.Video;
import com.example.danceit.RecyclerViewComponents.Firebase_RecyclerViewAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;

import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

public class FirstFragment extends Fragment {
        RecyclerView recyclerView;
        Firebase_RecyclerViewAdapter adapter;
        private FirebaseAuth mAuth;
        List<Video> allVideos;

    @Override
        public View onCreateView(
                LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState
        ) {



            // Initialize Firebase Auth
            mAuth = FirebaseAuth.getInstance();

            FirebaseFirestore database = FirebaseFirestore.getInstance();
            CollectionReference reference = database.collection("video_urls_private");

            View root = inflater.inflate(R.layout.fragment_first, container, false);
            //videoList = new ArrayList<>();
            recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);

            Query query = reference.
                    document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail())).
                    collection("private_video");

            FirestoreRecyclerOptions<Video> options = new FirestoreRecyclerOptions.Builder<Video>()
                    .setQuery(query, new SnapshotParser<Video>() {
                        @NonNull
                        @Override
                        public Video parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                            Video video = snapshot.toObject(Video.class);
                            assert video != null;
                            video.setVideoId(snapshot.getId());
                            return video;
                        }
                    })
                    .setLifecycleOwner(this)
                    .build();
            adapter = new Firebase_RecyclerViewAdapter(options, getActivity());

            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            reference.addSnapshotListener(
                    new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                // Handle error
                                return;
                            }

                            // Convert query snapshot to a list of chats
                            allVideos = snapshot.toObjects(Video.class);
                            ((MainActivity) getActivity()).setAllVideos(allVideos);
                            // Update UI
                        }
                    });

        return root;
        }

        @Override
        public void onStart() {
            super.onStart();
            adapter.startListening();;

        }

        @Override
        public void onStop() {
            super.onStop();
            adapter.stopListening();
        }

        public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        }
    }

