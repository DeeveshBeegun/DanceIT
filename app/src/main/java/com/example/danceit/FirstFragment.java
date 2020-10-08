package com.example.danceit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.danceit.Model.Video;
import com.example.danceit.RecyclerViewComponents.Firebase_RecyclerViewAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class FirstFragment extends Fragment {
        RecyclerView recyclerView;
        Firebase_RecyclerViewAdapter adapter;
        private FirebaseAuth mAuth;

    @Override
        public View onCreateView(
                LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState
        ) {



            // Initialize Firebase Auth
            mAuth = FirebaseAuth.getInstance();

            FirebaseFirestore database = FirebaseFirestore.getInstance();
            CollectionReference reference = database.collection("video_urls_private").
                    document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail())).
                    collection("private_video");

            View root = inflater.inflate(R.layout.fragment_first, container, false);
            //videoList = new ArrayList<>();
            recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);

            Query query = reference.limit(100);

            FirestoreRecyclerOptions<Video> options = new FirestoreRecyclerOptions.Builder<Video>()
                    .setQuery(query, Video.class)
                    .build();
            adapter = new Firebase_RecyclerViewAdapter(options, getActivity());

            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));




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

