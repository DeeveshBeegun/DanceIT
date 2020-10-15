package com.example.danceit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.danceit.Model.FirebaseManager;
import com.example.danceit.Model.User;
import com.example.danceit.Model.Video;
import com.google.android.material.textfield.TextInputLayout;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class AddVideoActivity extends AppCompatActivity {

    private boolean isPrivate; // privacy is public
    FirebaseManager firebaseManager = new FirebaseManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);

        final TextInputLayout textInputUrl = findViewById(R.id.textInput_url);
        final TextInputLayout textInputTags = findViewById(R.id.textInput_tag);
        final Button saveButton = (Button) findViewById(R.id.saveButton);

        addVideo(textInputUrl, textInputTags, saveButton);

    }

    public void addVideo(final TextInputLayout textInputUrl, final TextInputLayout textInputTags, Button saveButton) {

        final Intent intent = new Intent(this,MainActivity.class);

        checkPrivacy(); // checks if video is public or private

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri=Uri.parse(textInputUrl.getEditText().getText().toString().trim());
                if(isURL(textInputUrl.getEditText().getText().toString().trim())) {

                    if (isPrivate) {
                        Video video = new Video(firebaseManager.getUserEmail(), getAlphaNumericString(14), "",
                                Objects.requireNonNull(textInputUrl.getEditText()).getText().toString().trim(), tagInput_string((textInputTags.getEditText())
                                .getText().toString()), "private", "no");

                        firebaseManager.addPrivate_video(video);

                        Toast toast = Toast.makeText(getApplicationContext(), "Url saved as private.", Toast.LENGTH_SHORT);
                        toast.show();

                    }

                    else {
                        final String videoId = getAlphaNumericString(14);
                        Video video = new Video(firebaseManager.getUserEmail(), videoId, "",
                                Objects.requireNonNull(textInputUrl.getEditText()).getText().toString().trim(), tagInput_string((textInputTags.getEditText())
                                .getText().toString()), "private", "yes");

                        firebaseManager.addPrivate_video(video);

                        video.setPrivacy("public");

                        firebaseManager.addPublic_video(video);

                        Toast toast = Toast.makeText(getApplicationContext(), "Url saved as public.", Toast.LENGTH_SHORT);
                            toast.show();
                    }

                    startActivity(intent);
            }else {

                    Toast toast = Toast.makeText(getApplicationContext(), "Url not valid", Toast.LENGTH_SHORT);
                    toast.show();
                }
        }

        });

    }

    // Java program generate a random AlphaNumeric String
    // using Regular Expressions method
    private String getAlphaNumericString(int n){
        {

            // length is bounded by 256 Character
            byte[] array = new byte[256];
            new Random().nextBytes(array);

            String randomString
                    = new String(array, Charset.forName("UTF-8"));

            // Create a StringBuffer to store the result
            StringBuffer r = new StringBuffer();

            // remove all spacial char
            String  AlphaNumericString
                    = randomString
                    .replaceAll("[^A-Za-z0-9]", "");

            // Append first 20 alphanumeric characters
            // from the generated random String into the result
            for (int k = 0; k < AlphaNumericString.length(); k++) {

                if (Character.isLetter(AlphaNumericString.charAt(k))
                        && (n > 0)
                        || Character.isDigit(AlphaNumericString.charAt(k))
                        && (n > 0)) {

                    r.append(AlphaNumericString.charAt(k));
                    n--;
                }
            }
            // return the resultant string
            return r.toString();
        }

    }

    public boolean isURL(String url) {
       if(url.contains("http")&& (url.contains("youtube")||url.contains("youtu.be"))){
           return true;
       }else {
           return false;
       }
    }

    public ArrayList<String> tagInput_string(String textInputTags) {
        ArrayList<String> tags = new ArrayList<String>();
        String[] description = textInputTags.split(" ");

        for (int i = 0; i < description.length; i++) {
            tags.add(description[i]);
        }
        return tags;
    }

    public void checkPrivacy() {
        RadioButton radiobutton_private;
        radiobutton_private = findViewById(R.id.radioButton_private);
        radiobutton_private.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isPrivate = b;
            }
        });

    }



}