/**
 * This class represents the model for the Video Object.
 * @author Bohlale Motsieloa (MTSBOH002)
 * @date: 24/08/2020
 * @version: 0.0 (Prototype)
 */
 
package com.example.danceit.Model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

//@Entity(tableName="video_table")
public class Video implements Parcelable {
    private String videoUploader;
    private String videoId;
    private String url;
    private ArrayList<String> tags = null;
    public String privacy;
    public String beingShared;


//
//    /*Constructor*/
//    public Video(User videoUploader, String url, ArrayList<Tag> tag_list, boolean privacy) {
//        this.videoUploader = videoUploader;
//        this.url = url;
//        this.tag_list = tag_list;
//        this.privacy = privacy;
//    }

    public Video(String videoUploader, String videoId ,String url, List<String> tags,
                 String privacy, String beingShared) {
        this.videoUploader = videoUploader;
        this.videoId = videoId;
        this.url = url;
        this.tags = (ArrayList<String>) tags;
        this.privacy = privacy;
        this.beingShared = beingShared;
    }


    public Video() {}


    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected Video(Parcel in) {
        videoUploader = in.readString();
        videoId = in.readString();
        url = in.readString();
        privacy = in.readString();
        beingShared = in.readString();
        tags = in.createStringArrayList();
    }


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

    /*Get and Set Methods*/
    public String getVideoId() { return videoId; }

    public void setVideoId(String id) { this.videoId = id; }

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

//    public ArrayList<Tag> getTag_list() {
//        return tag_list;
//    }
//
//    public void setTag_list(ArrayList<Tag> tag_list) {
//        this.tag_list = tag_list;
//    }

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

    /*Overriding equals method to allow correct Video object comparison*/
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
                && video.privacy.equals(this.privacy) && video.beingShared.equals(this.beingShared) && video.videoId.equals(this.videoId)){
            return true;
        }

        return false;
    }

    /*Overriding hashCode method to allow correct Video object comparison*/
    @Override
    public int hashCode()
    {
        return this.url.hashCode() * this.videoUploader.hashCode() * this.videoId.hashCode() * 19;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(videoUploader);
        parcel.writeString(videoId);
        parcel.writeString(url);
        parcel.writeString(privacy);
        parcel.writeString(beingShared);
        parcel.writeStringList(tags);
    }
}
