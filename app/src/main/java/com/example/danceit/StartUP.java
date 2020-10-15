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
        setContentView(R.layout.activity_start_u_p);

        //delay  for a 10sec and switch activity
        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(5000);  //Delay of 5 seconds
                } catch (Exception e) {

                } finally {

                    Intent i = new Intent(StartUP.this,
                            LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        welcomeThread.start();


    }
}