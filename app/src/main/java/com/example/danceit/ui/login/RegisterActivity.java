package com.example.danceit.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danceit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    private EditText editTextUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        final Intent i=new  Intent(this,LoginActivity.class);
        final TextView textView=(TextView) findViewById(R.id.link_signup);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);//switch to sign in view

            }
        });
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


    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        /*if (!validateForm()) {
            return;
        }

        showProgressBar();

         */

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                            assert user != null;
                            String userId = user.getUid();

                            createUserDatabase(userId); // creates user database on firebase real time database

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
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
        DocumentReference reference = database.collection("users").document();

        HashMap<String, String> hashMap = new HashMap<>();
       // hashMap.put("userId", userId); // auto generated userId
        hashMap.put("username", editTextUsername.getText().toString()); // name of user
        hashMap.put("Email", Objects.requireNonNull(mAuth.getCurrentUser()).getEmail());

        reference.set(hashMap);

    }


}