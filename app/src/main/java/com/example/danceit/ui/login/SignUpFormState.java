package com.example.danceit.ui.login;

import androidx.annotation.Nullable;

public class SignUpFormState {
    @Nullable
    private Integer emailError;
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    private boolean isDataValid;


    SignUpFormState(@Nullable Integer emailError, @Nullable Integer usernameError, @Nullable Integer passwordError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.emailError=emailError;
        this.isDataValid = false;
    }

    SignUpFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.emailError=null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    Integer getEmailError() {
        return emailError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}
