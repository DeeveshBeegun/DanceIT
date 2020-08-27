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

    /*Get and Set Methods*/
    public User getVideoUploader() {
        return videoUploader;
    }

    public void setVideoUploader(User videoUploader) {
        this.videoUploader = videoUploader;
    }

    public String getURL() {
        return url;
    }

    public void setURL(String URL) {
        this.url = url;
    }

    public ArrayList<Tag> getTagList() {
        return tag_list;
    }

    public void setTagList(ArrayList<Tag> tag_list) {
        this.tag_list = tag_list;
    }

    public boolean getPrivate() {
        return privacy;
    }

    public void setPrivate(boolean Private) {
        this.privacy = privacy;
    }
    

}
