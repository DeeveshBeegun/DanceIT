/**
 * This class represents the model for the Tag object
 *
 * @author Bohlale Motsieloa (MTSBOH002)
 * @date: 24/08/2020
 * @version: 0.0 (Prototype)
 */

package com.example.danceit.Model;
 
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.io.Serializable;

public class Tag implements Parcelable {
    private User owner;
    private String description;
    private boolean hasComplaint;


    /*Constructor*/
    public Tag(User owner, String description, boolean hasComplaint) {
        this.owner = owner;
        this.description = description;
        this.hasComplaint = hasComplaint;
    }

    protected Tag(Parcel in) {
        description = in.readString();
        hasComplaint = in.readByte() != 0;
    }

    public static final Creator<Tag> CREATOR = new Creator<Tag>() {
        @Override
        public Tag createFromParcel(Parcel in) {
            return new Tag(in);
        }

        @Override
        public Tag[] newArray(int size) {
            return new Tag[size];
        }
    };

    /*Get and Set Methods*/
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean getHasComplaint() {
        return hasComplaint;
    }

    public void setHasComplaint (boolean hasComplaint) {
        this.hasComplaint = hasComplaint;
    }


    /*Overriding equals method to allow correct Tag object comparison*/
    @Override
    public boolean equals(Object object) 
    {

        if(this == object){
           return true;  
        } 
           
        if(object == null || object.getClass()!= this.getClass()) {
            return false;
        }    
           
        Tag tag = (Tag) object;
          
        return tag.description.equalsIgnoreCase(this.description) ; 
    }

    /*Overriding hashCode method to allow correct Tag object comparison*/
    @Override
    public int hashCode() 
    {  
        return this.description.hashCode(); 
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(description);
        parcel.writeByte((byte) (hasComplaint ? 1 : 0));
    }
}
