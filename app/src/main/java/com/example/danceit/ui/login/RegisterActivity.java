package com.example.danceit.ui.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danceit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    private EditText editTextUsername;
    private ProgressBar loadingProgressBar;
    private LoginViewModel signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // get model
        signup=new LoginViewModel();

        //set up edittext
        final EditText editTextPassword=(EditText) findViewById(R.id.password_new);
        final EditText editTextEmail=(EditText) findViewById(R.id.email_entry);
        final Button button=(Button) findViewById(R.id.signup);



        //set progressbar
        loadingProgressBar=(ProgressBar) findViewById(R.id.loading_SIGNUP);


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //click listener uf the user wants to sign in
        final Intent i=new  Intent(this,LoginActivity.class);
        final TextView textView=(TextView) findViewById(R.id.link_signup);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);//switch to sign in view

            }
        });


        signup.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                button.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    editTextEmail.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    editTextPassword.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });


        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                signup.loginDataChanged(editTextEmail.getText().toString(),
                        editTextPassword.getText().toString());

            }
        };



        editTextEmail.addTextChangedListener(afterTextChangedListener);
        editTextPassword.addTextChangedListener(afterTextChangedListener);



    }





    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
        }

        Button button=(Button) findViewById(R.id.signup);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editTextPassword=(EditText) findViewById(R.id.password_new);
                EditText editTextEmail=(EditText) findViewById(R.id.email_entry);
                editTextEmail.getText();
                editTextUsername = (EditText) findViewById(R.id.username_entry);
                createAccount(editTextEmail.getText().toString(),editTextPassword.getText().toString());

            }
        });
    }


    private  void loading(boolean choice){

        //loadingProgress visible or not
        if(choice){
            loadingProgressBar.setVisibility(View.VISIBLE);

        }else{
            loadingProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        /*if (!validateForm()) {
            return;
        }

        showProgressBar();

         */

        // [START create_user_with_email]
        loading(true);
        mAuth.createUserWithEmailAndPassword(email, password)

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(editTextUsername.getText().toString()).build();

                            assert user != null;
                            user.updateProfile(profileUpdates);

                            updateUI(user);

                            assert user != null;
                            String userId = user.getUid();

                            createUserDatabase(userId); // creates user database on firebase real time

                            Toast.makeText(RegisterActivity.this, "User successfully created.",
                                    Toast.LENGTH_SHORT).show();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            loading(false);
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                       // hideProgressBar();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private void updateUI(FirebaseUser user) {
    }

    /**
     * This method creates a database on the firestore database on firebase for registered users.
     * @param userId user ids
     */
    public void createUserDatabase(String userId) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference reference = database.collection("users").document(
                editTextUsername.getText().toString());

        HashMap<String, String> hashMap = new HashMap<>();
       // hashMap.put("userId", userId); // auto generated userId
        hashMap.put("username", editTextUsername.getText().toString()); // name of user

        reference.set(hashMap);

    }


}