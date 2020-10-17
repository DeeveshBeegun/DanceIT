package com.example.danceit.Model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

/**
 * This class has methods that can be used by other classes to access and perform various
 * operation on the video object on the firestore database. These methods can be accessed
 * by creating an instance of this class.
 */
public class FirebaseManager {
    private DocumentReference documentReference;
    private CollectionReference collectionReference;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    /**
     * This method can be used to add a tag to the video object in the firestore database
     * based on the privacy of the video.
     * @param video_id is the id of the video object were tags need to be added.
     * @param newTag is the tag to be added.
     * @param video is the video where tags need to be added.
     */
    public void addTag(String video_id, String newTag, Video video) {
        if (video.getPrivacy().equals("private"))
            documentReference = getPrivate_videoReference()
                    .document(video_id);

        else if (video.getPrivacy().equals("received"))
            documentReference = getReceive_videoReference()
                    .document(video_id);

        else
            documentReference = getPublic_videoReference()
                    .document(video_id);

        documentReference.update("tags", FieldValue.arrayUnion(newTag));


    }

    /**
     * This method can be used to delete a tag in a video object in the firestore database
     * based on the privacy of the video.
     * @param video_id is the id of the video object where tags need to be deleted
     * @param tag is the tag to be deleted.
     * @param privacy is the privacy of the video i.e. public, private or received.
     */
    public void deleteTag(String video_id, String tag, String privacy) {
        if(privacy.equals("public"))
            documentReference = getPublic_videoReference().document(video_id);

        else if (privacy.equals("received"))
            documentReference = getReceive_videoReference().document(video_id);

        else
            documentReference = getPrivate_videoReference().document(video_id);

        documentReference.update("tags", FieldValue.arrayRemove(tag));

    }


    /**
     * This method can be used to add a video object to the firestore database
     * with private as privacy.
     * @param video is the video to be added.
     */
    public void addPrivate_video(Video video) {
        getPrivate_videoReference().add(video);
    }

    /**
     * This method can be used to add a video object to the firestore database
     * with public as privacy.
     * @param video is the video to be added.
     */
    public void addPublic_video(Video video) {
         getPublic_videoReference().add(video);
    }

    /**
     * This method can be used to delete a video object in the firestore database
     * based on its privacy.
     * @param video_id is the video to be deleted
     * @param privacy is the privacy of the video i.e. public, private or received.
     */
    public void deleteVideo(String video_id, String privacy) {
        if(privacy.equals("public"))
            getPublic_videoReference().document(video_id).delete();

        else if(privacy.equals("received"))
            getReceive_videoReference().document(video_id).delete();

        else
            getPrivate_videoReference().document(video_id).delete();
    }

    /**
     * This method can be used to send a video to selected users.
     * @param selected_users contains the users who will receive the video.
     * @param i is the position in the selected_users list.
     * @param video is the video object to send.
     */
    public void sendVideoToUsers(List<String> selected_users, int i, Video video) {
         collectionReference = database.collection("video_sent").document(selected_users.get(i))
                .collection("video_received"); // path where video sent is saved
        collectionReference.add(video);
    }

    /**
     * This method can be used to get the video object reference in the firestore
     * database for videos with privacy private.
     * @return the collection reference of private video in the firestore database.
     */
    public CollectionReference getPrivate_videoReference() {
        collectionReference = FirebaseFirestore.getInstance().collection("video_urls_private")
                .document(Objects.requireNonNull
                        (Objects.requireNonNull(mAuth.getCurrentUser()).getEmail())).collection("private_video");
        return collectionReference;
    }

    /**
     * This method can be used to get the video object reference in the firestore
     * database for videos with privacy received.
     * @return the collection reference of received video in the firestore database.
     */
    public CollectionReference getReceive_videoReference() {
        collectionReference = FirebaseFirestore.getInstance().collection("video_sent")
                    .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName()))
                .collection("video_received");
        return collectionReference;

    }

    /**
     * This method can be used to get the video object reference in the firestore
     * database for videos with privacy public.
     * @return the collection reference of public video in the firestore database.
     */
    public CollectionReference getPublic_videoReference() {
        return collectionReference = database.collection("video_url");
    }

    /**
     * This method can be used to get the user reference in the firestore
     * @return the collection reference of users signed up.
     */
    public CollectionReference getUser_reference() {
        return collectionReference = database.collection("users");
    }

    /**
     * This method gets the username of the current user.
     * @return username of current user.
     */
    public String getUsername() {
        return Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName();
    }



}
