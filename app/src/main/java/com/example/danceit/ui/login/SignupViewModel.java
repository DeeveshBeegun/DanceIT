package com.example.danceit.ui.login;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.danceit.R;

public class SignupViewModel {

    //stores the state of the sign up
    private MutableLiveData<SignUpFormState> signUpFormState = new MutableLiveData<>();

    SignupViewModel(){

    }


    public void signUpDataChanged(String username,String email, String password) {
        if (!isUserNameValid(username)) {
            signUpFormState.setValue(new SignUpFormState(null,R.string.invalid_username, null));
        }else if(!isEmailValid(email)){

            signUpFormState.setValue(new SignUpFormState(R.string.invalid_email,null,null));

        }
        else if (!isPasswordValid(password)) {
            signUpFormState.setValue(new SignUpFormState(null, null,R.string.invalid_password));
        }else {
            signUpFormState.setValue(new SignUpFormState(true));
        }
    }

    LiveData<SignUpFormState> getLoginFormState() {
        return signUpFormState;
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        return username != null && username.trim().length() > 2;
    }


    // A placeholder username validation check
    private boolean isEmailValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }




    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }





}
