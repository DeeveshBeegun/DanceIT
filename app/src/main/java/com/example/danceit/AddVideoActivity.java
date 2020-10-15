package com.example.danceit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.danceit.Model.FirebaseManager;
import com.example.danceit.Model.Video;
import com.google.android.material.textfield.TextInputLayout;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

/**
 * This class is responsible for adding a video object on the database.
 */
public class AddVideoActivity extends AppCompatActivity {

    private boolean isPrivate; // privacy is public
    FirebaseManager firebaseManager = new FirebaseManager(); // create an instance of FirebaseManager class; used to add videos to database.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);

        final TextInputLayout textInputUrl = findViewById(R.id.textInput_url);
        final TextInputLayout textInputTags = findViewById(R.id.textInput_tag);
        final Button saveButton = (Button) findViewById(R.id.saveButton);

        addVideo(textInputUrl, textInputTags, saveButton);

    }

    /**
     * This method is used to add a video object in the database.
     * It creates a video object depending on the privacy and if it is being shared
     * to other users. The firebaseManager class is used to add the video object to the database.
     * @param textInputUrl is where the url is input.
     * @param textInputTags is where the tags are input.
     * @param saveButton saves a video to the database depending on the privacy of the video.
     */
    public void addVideo(final TextInputLayout textInputUrl, final TextInputLayout textInputTags, Button saveButton) {
        final Intent intent = new Intent(this,MainActivity.class);

        checkPrivacy(); // checks if video is public or private

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checks if the input url is a valid url
                if(isURL(textInputUrl.getEditText().getText().toString().trim())) {

                    if (isPrivate) {
                        Video video = new Video(firebaseManager.getUserEmail(), getAlphaNumericString(14), "",
                                Objects.requireNonNull(textInputUrl.getEditText()).getText().toString().trim(), tagInput_string((textInputTags.getEditText())
                                .getText().toString()), "private", "no");

                        firebaseManager.addPrivate_video(video); // add video as private in the database

                        Toast toast = Toast.makeText(getApplicationContext(), "Url saved as private.", Toast.LENGTH_SHORT);
                        toast.show();

                    }

                    else {
                        Video video = new Video(firebaseManager.getUserEmail(), getAlphaNumericString(14), "",
                                Objects.requireNonNull(textInputUrl.getEditText()).getText().toString().trim(), tagInput_string((textInputTags.getEditText())
                                .getText().toString()), "private", "yes");

                        firebaseManager.addPrivate_video(video); // add video in the private field of the database

                        video.setPrivacy("public");

                        firebaseManager.addPublic_video(video);// add video in the public field of the database

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

    /**
     * This method splits the text in the text input in AddVideoActivity and
     * adds to an arraylist.
     * @param textInputTags text from the text input in the AddVideoActivity
     * @return arraylist of tags.
     */
    public ArrayList<String> tagInput_string(String textInputTags) {
        ArrayList<String> tags = new ArrayList<>(); // stores the tags as string
        String[] description = textInputTags.split(" ");

        for (int i = 0; i < description.length; i++) {
            if(!description[i].equals(" ")) // does not include white spaces
                tags.add(description[i].trim());
        }
        return tags;
    }

    /**
     * This method checks which radio button (i.e. private or public) is checked.
     * If the private radio button is checked, the isPrivate variable is set to true.
     * Otherwise, it is set to false.
     */
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