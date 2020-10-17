package com.example.danceit.Model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.RequiresApi;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the Video Object and implements Parcelable to
 * allow Video objects to be passed between Activities.
 */

public class Video implements Parcelable {
    private String videoUploader;
    private String videoId;
    private String parseId;
    private String url;
    private ArrayList<String> tags = null;
    public String privacy;
    public String beingShared;

    /**
     * Constructor
     *@param videoId unique identifier in the database.
     *@param parseId documentID which contains this Video,
     * used by Firebase_RecyclerViewAdapter to display video.
     *@param beingShared is the video in a shared status or not.
     */
    public Video(String videoUploader, String videoId , String parseId, String url, List<String> tags,
                 String privacy, String beingShared) {
        this.videoUploader = videoUploader;
        this.videoId = videoId;
        this.parseId = parseId;
        this.url = url;
        this.tags = (ArrayList<String>) tags;
        this.privacy = privacy;
        this.beingShared = beingShared;
    }

    /**
     * Default constructor needed for the FirebaseManger to function
     */
    public Video() {}


    /**
     * Instructions on how the Video Object will be read into a Parcel
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected Video(Parcel in) {
        videoUploader = in.readString();
        videoId = in.readString();
        parseId = in.readString();
        url = in.readString();
        privacy = in.readString();
        beingShared = in.readString();
        tags = in.createStringArrayList();
    }

    /**
     * Instructions on how the Video Object will be created from a Parcel and
     * how an Array of Videos will be instantiated
     */
    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    /**
     * Get and Set Methods
     */
    public String getVideoId() { return videoId; }

    public void setVideoId(String id) { this.videoId = id; }

    public String getParseId() { return parseId; }

    public void setParseId(String id) { this.parseId = id; }

    public String getVideoUploader() {
        return videoUploader;
    }

    public void setVideoUploader(String videoUploader) {
        this.videoUploader = videoUploader;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = (ArrayList<String>) tags;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) { this.privacy = privacy; }

    public String getBeingShared() { return beingShared; }

    public void setBeingShared(String beingShared) { this.beingShared = beingShared; }


    @Override
    public String toString() {
        return url;
    }

    /**
     * Overriding equals method to allow correct comparison between Video objects
     */
    @Override
    public boolean equals(Object object)
    {

        if(this == object){
            return true;
        }

        if(object == null || object.getClass()!= this.getClass()) {
            return false;
        }

        Video video = (Video) object;

        if(video.url.equals(this.url) && video.videoUploader.equals(this.videoUploader)
                && video.privacy.equals(this.privacy) && video.beingShared.equals(this.beingShared) && video.videoId.equals(this.videoId) &&
        video.parseId.equals(this.parseId)){
            return true;
        }

        return false;
    }

    /**
     * Overriding hashCode method to allow correct comparison between Video objects
     */
    @Override
    public int hashCode()
    {
        return this.url.hashCode() * this.videoUploader.hashCode() * this.videoId.hashCode() * this.parseId.hashCode() * 19;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Instructions on how the Video Object will be written into a Parcel
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(videoUploader);
        parcel.writeString(videoId);
        parcel.writeString(parseId);
        parcel.writeString(url);
        parcel.writeString(privacy);
        parcel.writeString(beingShared);
        parcel.writeStringList(tags);
    }
}
