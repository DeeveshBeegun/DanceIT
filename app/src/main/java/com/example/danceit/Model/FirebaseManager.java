package com.example.danceit.Model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

public class FirebaseManager {
    private DocumentReference documentReference;
    private CollectionReference collectionReference;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public void addTag(String video_id, String newTag, String privacy) {

        if (privacy.equals("private"))
            documentReference = getPrivate_videoReference()
                    .document(video_id);

        else if (privacy.equals("received"))
            documentReference = getReceive_videoReference()
                    .document(video_id);

        else
            documentReference = getPublic_videoReference()
                    .document(video_id);

        documentReference.update("tags", FieldValue.arrayUnion(newTag));


    }

    public void deleteTag(String video_id, String tag, String privacy) {
        if(privacy.equals("public"))
            documentReference = getPublic_videoReference().document(video_id);

        else if (privacy.equals("received"))
            documentReference = getReceive_videoReference().document(video_id);

        else
            documentReference = getPrivate_videoReference().document(video_id);

        documentReference.update("tags", FieldValue.arrayRemove(tag));

    }


    public void addPrivate_video(Video video) {
        getPrivate_videoReference().add(video);
    }

    public void addPublic_video(Video video) {
         getPublic_videoReference().add(video);
    }

    public void deleteVideo(String video_id, String privacy) {
        if(privacy.equals("public"))
            getPublic_videoReference().document(video_id).delete();

        else if(privacy.equals("received"))
            getReceive_videoReference().document(video_id).delete();

        else
            getPrivate_videoReference().document(video_id).delete();
    }

    public void sendVideoToUsers(List<String> selected_users, int i, Video video) {
         collectionReference = database.collection("video_sent").document(selected_users.get(i))
                .collection("video_received"); // path where video sent is saved
        collectionReference.add(video);
    }

    public CollectionReference getPrivate_videoReference() {
        collectionReference = FirebaseFirestore.getInstance().collection("video_urls_private")
                .document(Objects.requireNonNull
                        (Objects.requireNonNull(mAuth.getCurrentUser()).getEmail())).collection("private_video");
        return collectionReference;
    }

    public CollectionReference getReceive_videoReference() {
        collectionReference = FirebaseFirestore.getInstance().collection("video_sent")
                    .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName()))
                .collection("video_received");

        return collectionReference;

    }

    public CollectionReference getPublic_videoReference() {
        return collectionReference = database.collection("video_url");
    }

    public CollectionReference getUser_reference() {
        return collectionReference = database.collection("users");
    }



}
