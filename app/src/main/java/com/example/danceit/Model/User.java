/**
 * @Author Claudious Nhemwa (NHMCLA003)
 * class represents a user  and their personal details.
 *
 *
 */
package com.example.danceit.Model;

import java.util.ArrayList;

public class User {
    private String name;
    private String passWord;
    private ArrayList<String> contactDetails;


    //constructor
    public User(String name, String passWord, ArrayList<String> contactDetails) {
        this.name = name;
        this.passWord = passWord;
        this.contactDetails = contactDetails;
    }

    //allows the user to sign-in
    public boolean signIn(String password){

        //check if th passwords are equal release true if not false
        if(passWord.equals(password)){
            return  true;
        }
        return false;

    }


    //TODO  IMPLEMENT SIGNIN
    public void signOut(){}

    //TODO  IMPLEMENT SIGNUP
    public void signUp(){}

    // Get and setter methods for the class
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public ArrayList<String> getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(ArrayList<String> contactDetails) {
        this.contactDetails = contactDetails;
    }
}
