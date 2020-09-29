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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Entity(tableName="video_table")
public class Video implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int videoId;
    private User videoUploader;
    private String url;
    private ArrayList<Tag> tag_list;
    @Ignore
    private List<String> tags;
    private boolean privacy;


    /*Constructor*/
    public Video(User videoUploader, String url, ArrayList<Tag> tag_list, boolean privacy) {
        this.videoUploader = videoUploader;
        this.url = url;
        this.tag_list = tag_list;
        this.privacy = privacy;
    }

    public Video(User videoUploader, String url, List<String> tags, boolean privacy) {
        this.videoUploader = videoUploader;
        this.url = url;
        this.tags = tags;
        this.privacy = privacy;
    }


    public Video() {}


    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected Video(Parcel in) {
        videoUploader = in.readTypedObject(User.CREATOR);
        videoId = in.readInt();
        url = in.readString();
        tag_list = in.createTypedArrayList(Tag.CREATOR);
        privacy = in.readByte() != 0;
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
    public int getVideoId() { return videoId; }

    public void setVideoId(int id) { this.videoId = id; }

    public User getVideoUploader() {
        return videoUploader;
    }

    public void setVideoUploader(User videoUploader) {
        this.videoUploader = videoUploader;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<Tag> getTag_list() {
        return tag_list;
    }

    public void setTag_list(ArrayList<Tag> tag_list) {
        this.tag_list = tag_list;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public boolean getPrivacy() {
        return privacy;
    }

    public void setPrivacy(boolean Private) {
        this.privacy = privacy;
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

        if(video.videoId == this.videoId && video.url.equals(this.url) && video.videoUploader.getName().equals(this.videoUploader.getName()) && video.privacy == this.privacy){
            return true;
        }

        return false;
    }

    /*Overriding hashCode method to allow correct Video object comparison*/
    @Override
    public int hashCode()
    {
        return this.url.hashCode() * this.videoUploader.getName().hashCode() * this.videoId * 19;
    }

    @Override
    public String toString() {
        return url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedObject(videoUploader, 1);
        parcel.writeInt(videoId);
        parcel.writeString(url);
        parcel.writeTypedList(tag_list);
        parcel.writeByte((byte) (privacy ? 1 : 0));
    }
}
