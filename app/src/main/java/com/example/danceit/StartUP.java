package com.example.danceit;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.danceit.Youube.MainYoutube;
import com.example.danceit.ui.login.LoginActivity;

import java.util.Timer;
import java.util.TimerTask;

public class StartUP extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent=new Intent(this,
                MainActivity.class);

        //calling  the activity login
        /*final Intent intent=new Intent(this,
                LoginActivity.class);

        // Testing if the Youtube API is working
       /* final Intent intent=new Intent(this,
                MainYoutube.class);*/




        startActivity(intent);


    }
}