package com.example.danceit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddVideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);

        //Get editText widgets
        final EditText editTextTag=(EditText) findViewById(R.id.editTextTags);
        final  EditText editTextURL=(EditText) findViewById(R.id.editTextURL);

        //Get save button
        final Button saveButton=(Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get user in put from the edit text
                String tagFromUser =editTextTag.getText().toString();
                String urlFromUser=editTextURL.getText().toString();
                //TODO add private and public

            }
        });
    }
}