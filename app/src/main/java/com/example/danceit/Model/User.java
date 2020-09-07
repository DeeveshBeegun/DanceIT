/**
 * @Author Claudious Nhemwa (NHMCLA003)
 * class represents a user  and their personal details.
 *
 *
 */
package com.example.danceit.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.io.Serializable;

public class User implements Parcelable {
    private String name;
    private String passWord;


    //constructor
    public User(String name, String passWord) {
        this.name = name;
        this.passWord = passWord;
    }

    protected User(Parcel in) {
        name = in.readString();
        passWord = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(passWord);
    }
}
