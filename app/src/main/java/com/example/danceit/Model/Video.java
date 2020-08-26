/**
 * This class represents the model for the Video Object.
 * @author Bohlale Motsieloa (MTSBOH002)
 * @date: 24/08/2020
 * @version: 0.0 (Prototype)
 */
 
package com.example.danceit.Model;
 
import java.io.Serializable;
import java.util.ArrayList;

public class Video implements Serializable {
    private User videoUploader;
    private String URL;
    private ArrayList<Tag> Tags;
    private boolean Private;


    /*Constructor*/
    public Video(User videoUploader, String URL, ArrayList<Tag> Tags, boolean Private) {
        this.videoUploader = videoUploader;
        this.URL = URL;
        this.Tags = Tags;
        this.Private = Private;
    }

    /*Get and Set Methods*/
    public User getVideoUploader() {
        return videoUploader;
    }

    public void setVideoUploader(User videoUploader) {
        this.videoUploader = videoUploader;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public ArrayList<Tag> getTagList() {
        return Tags;
    }

    public void setTagList(ArrayList<Tag> Tags) {
        this.Tags = Tags;
    }

    public boolean getPrivate() {
        return Private;
    }

    public void setPrivate(boolean Private) {
        this.Private = Private;
    }
    
    /**
     * This method adds a Tag to the Video object.
     * If a tag is in the list, false is returned to indicate no addition happened.
     * @param tag arraylist containing Tag objects.
     */
     public boolean addTag(String tag) {
         Tag tag1 = new Tag(this.videoUploader, tag, false); // Creating new Tag
         if ( Tags.contains(tag1) ){
            return false; // Shows tag description already exists 
         }
         else{
            Tags.add(tag1);
            return true;
         }
        
     }
     
     /**
     * This method finds if the Video object has a certain tag description.
     * returns false if a video does not have tags 
     * @param tag arraylist containing Tag objects.
     */
     
     public boolean findTag(String tag) { 
         if( Tags.isEmpty() ){
            return false;
         }
         return Tags.contains(new Tag(this.videoUploader, tag, false));
     }
  
}
