package com.example.danceit.ui.login;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danceit.MainActivity;
import com.example.danceit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    private ProgressBar loadingProgressBar;
    private LoginViewModel loginViewModel;
// ...

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginViewModel=new LoginViewModel();


        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);

        final Intent i=new  Intent(this,RegisterActivity.class);
        final TextView textView=(TextView) findViewById(R.id.link_signup);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);//switch to sign up view

            }
        });



        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
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
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());

            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {


                return false;
            }
        });

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loading(true);
                //sign  to the server
                mAuth.signInWithEmailAndPassword(usernameEditText.getText().toString(), passwordEditText.getText().toString())
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    loading(false);
                                    String welcome = getString(R.string.welcome) ;
                                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
                                    ///updateUI(user);
                                } else {
                                    loading(false);
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    // ...
                                }
                                // ...
                            }
                        });

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


    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            String welcome = getString(R.string.welcome) ;
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        }

    }

    private void updateUiWithUser() {
        String welcome = getString(R.string.welcome) ;
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
 
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}