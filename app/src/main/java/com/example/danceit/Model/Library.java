package com.example.danceit.Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


/**
 * This class represents the model for the Library.The Library class
 * add, save, and search for videos; it saves videos to a text file
 * where it also searches for the video.
 *
 * @author Deevesh Beegun (BGNDEE001)
 * @date: 23/08/2020
 * @version: 0.0 (prototype)
 */

public class Library {

   private ArrayList<Video> videoList; // container storing video objects
   private String storage_filename; // file to store dance video URLs

    public Library() {
        videoList = new ArrayList<Video>();
        storage_filename = "URL_storage.txt"; // hardcoded text file
    }

//    /**
//     * This method adds a video to an arrayList when a user presses
//     * add button on the gui.
//     * @param video object
//     */
//    public void addVideo(Video video) {
//        videoList.add(video);
//    }

    /**
     * This method saves a video to a text file when a user presses save on the gui.
     */
    public void saveVideo(Video video) throws IOException {

            videoList.add(video);
            File file = new File(storage_filename);

            if(!file.exists() || file.length() == 0)
                writeFileObj(videoList); // creates a file and adds an arraylist of video object the the file.

            else {
                ArrayList<Video> videoObjs = readFileObj(storage_filename);
                for(Video videoObj : videoList)
                    videoObjs.add(videoObj);
                writeFileObj(videoObjs); // writes a video to a file which already contains an arraylist
            }
            videoList.clear(); // clears the initial arraylist to reduce memory usage on phone

    }

    /**
     * This method searches for a video input by a user.
     * @param search_input tags or country of origin
     * @throws Exception
     */
    public boolean findVideo(String search_input) throws Exception {
        boolean tag_found = false;
        ArrayList<Video> videoList = readFileObj(storage_filename);
        for(Video video : videoList) {
            if(search_input.equalsIgnoreCase(video.findTag(search_input)))
                tag_found = true;
               // System.out.println(video.findTag(search_input));
            else 
                tag_found = false;
        }
        return tag_found;
        //	System.out.println((Arrays.toString(readFileObj(storage_filename).toArray())));
    }

    /**
     * This method reads an arraylist containing video objects from a file.
     * @param storage_filename name of file storing the video objects.
     * @return arraylist of video objects.
     * @throws Exception
     */
    public ArrayList<Video> readFileObj(String storage_filename) throws IOException {
            FileInputStream fileInput = new FileInputStream(storage_filename);
            ObjectInputStream objectInput = new ObjectInputStream(fileInput);
            @SuppressWarnings({"unchecked"})
            ArrayList<Video> videoObjs = (ArrayList<Video>) objectInput.readObject(); // read object from file
            objectInput.close();
            return videoObjs;
    }

    /**
     * This method writes an arraylist containing video objects to a file.
     * @param videoList arraylist containing video objects.
     * @throws Exception
     */
    public void writeFileObj(ArrayList<Video> videoList) throws IOException{
            FileOutputStream fileOutput = new FileOutputStream(storage_filename);
            ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
            objectOutput.writeObject(videoList);
            objectOutput.close();

    }

}