/**
 * This class represents the model for the Tag object
 *
 * @author Bohlale Motsieloa (MTSBOH002)
 * @date: 24/08/2020
 * @version: 0.0 (Prototype)
 */

package com.example.danceit.Model;
 
import java.util.ArrayList; 

public class Tag {
    private User owner;
    private String description;
    private boolean hasComplaint;


    /*Constructor*/
    public Tag(User owner, String description, boolean hasComplaint) {
        this.owner = owner;
        this.description = description;
        this.hasComplaint = hasComplaint;
    }

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
      
     
}
