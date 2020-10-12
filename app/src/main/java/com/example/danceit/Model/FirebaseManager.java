package com.example.danceit.Model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class FirebaseManager {
    private DocumentReference documentReference;
    private CollectionReference collectionReference;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public void addTag(String video_id, String newTag, String collection_path, String privacy) {

        if (privacy.equals("private")) {
            String[] path = collection_path.split("/");

            documentReference = database.collection(path[0])
                    .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                    .collection(path[1])
                    .document(video_id);
            documentReference.update("tags", FieldValue.arrayUnion(newTag));

        }

        else if (privacy.equals("received")) {
            String[] path = collection_path.split("/");

            documentReference = database.collection(path[0])
                     .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName()))
                    .collection(path[1])
                    .document(video_id);
            documentReference.update("tags", FieldValue.arrayUnion(newTag));
        }

        else {
            documentReference = database.collection(collection_path)
                    .document(video_id);
            documentReference.update("tags", FieldValue.arrayUnion(newTag));

        }


    }

    public void addPrivate_video(String collection_path, Video video) {
        String[] path = collection_path.split("/");

            collectionReference = FirebaseFirestore.getInstance().collection(path[0])
                    .document(Objects.requireNonNull(mAuth.getCurrentUser().getEmail())).collection(path[1]);

            collectionReference.add(video);

    }

    public void addPublic_video(String collection_path, Video video) {
         collectionReference = FirebaseFirestore.getInstance().collection(collection_path);
         collectionReference.add(video);
    }

    public CollectionReference getPrivate_videoReference(String collection_path) {
        String[] path = collection_path.split("/");

        collectionReference = FirebaseFirestore.getInstance().collection(path[0])
                .document(Objects.requireNonNull
                        (Objects.requireNonNull(mAuth.getCurrentUser()).getEmail())).collection(path[1]);
        return collectionReference;
    }

    public CollectionReference getReceive_videoReference(String collection_path) {
        String[] path = collection_path.split("/");

        collectionReference = FirebaseFirestore.getInstance().collection(path[0])
                    .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName()))
                .collection(path[1]);

        return collectionReference;

    }

    public CollectionReference getPublic_videoReference(String collection_path) {
        return collectionReference = database.collection(collection_path);
    }


}
