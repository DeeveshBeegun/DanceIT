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
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Objects;

public class ThirdFragment extends Fragment {
    RecyclerView recyclerView;
    Firebase_RecyclerViewAdapter adapter;
    private FirebaseAuth mAuth;
    ArrayList<Video> allVideos = new ArrayList<Video>();
    ArrayList<String> autoCompletion = new ArrayList<String>();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        CollectionReference reference = database.collection("video_sent");

        View root = inflater.inflate(R.layout.fragment_third, container, false);
        //videoList = new ArrayList<>();
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerViewThird);

            Query query = reference
            .document(Objects.requireNonNull(mAuth.getCurrentUser().getDisplayName())).
                    collection("video_received");

        FirestoreRecyclerOptions<Video> options = new FirestoreRecyclerOptions.Builder<Video>()
                        .setQuery(query, new SnapshotParser<Video>() {
                            @NonNull
                            @Override
                            public Video parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                                Video video = snapshot.toObject(Video.class);
                                assert video != null;
                                Video videoCopy = new Video (video.getVideoUploader(), video.getVideoId(), video.getUrl(), video.getTags(), video.getPrivacy());
                                allVideos.add(videoCopy);
                                autoCompletion.addAll(videoCopy.getTags());
                                ((MainActivity) getActivity()).setAutocompletion(autoCompletion.toArray(new String [0]));
                                video.setVideoId(snapshot.getId());
                                return video;
                            }
                        })
                        .setLifecycleOwner(this)
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
        ((MainActivity) getActivity()).setAllVideos(allVideos);
    }
}
