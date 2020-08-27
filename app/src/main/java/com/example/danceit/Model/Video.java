/**
 * This class represents the model for the Video Object.
 * @author Bohlale Motsieloa (MTSBOH002)
 * @date: 24/08/2020
 * @version: 0.0 (Prototype)
 */
 
package com.example.danceit.Model;
 
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;

@Entity(tableName="video_table")
public class Video implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int videoId;
    private User videoUploader;
    private String url;
    @Ignore
    private ArrayList<Tag> tag_list;
    private boolean privacy;


    /*Constructor*/
    @Ignore
    public Video(User videoUploader, String url, ArrayList<Tag> tag_list, boolean privacy) {
        this.videoUploader = videoUploader;
        this.url = url;
        this.tag_list = tag_list;
        this.privacy = privacy;
    }

    public Video(User videoUploader, String url, boolean privacy) {
        this.videoUploader = videoUploader;
        this.url = url;
        this.privacy = privacy;
    }

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

    public ArrayList<Tag> getTagList() {
        return tag_list;
    }

    public void setTagList(ArrayList<Tag> tag_list) {
        this.tag_list = tag_list;
    }

    public boolean getPrivacy() {
        return privacy;
    }

    public void setPrivacy(boolean Private) {
        this.privacy = privacy;
    }
    

}
