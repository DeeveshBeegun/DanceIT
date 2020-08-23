import java.util.ArrayList;
import java.util.Arrays;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.File;
import java.util.Scanner;

public class LibraryModel {

   private ArrayList<Video> videoList;
   private String storage_filename;

    public LibraryModel() {
        videoList = new ArrayList<Video>();
        storage_filename = "URL_storage.txt";
    }

    public void addVideo(Video video) {
        videoList.add(video);
    }

    public void saveVideo() {
        try {

            File file = new File(storage_filename);

            if(!file.exists() || file.length() == 0)
                writeFileObj(videoList);

            else {
                ArrayList<Video> videoObjs = readFileObj(storage_filename);
                for(Video videoObj : videoList)
                    videoObjs.add(videoObj);
                writeFileObj(videoObjs);
            }
            videoList.clear();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void findVideo(String search_input) throws Exception {
        ArrayList<Video> videoList = readFileObj(storage_filename);
        for(Video video : videoList) {
            if(search_input.equals(video.getTag())) {
                System.out.println(video.getTag());
            }
        }
        //	System.out.println((Arrays.toString(readFileObj(storage_filename).toArray())));
    }

    public ArrayList<Video> readFileObj(String storage_filename) throws Exception {
        FileInputStream fileInput = new FileInputStream(storage_filename);
        ObjectInputStream objectInput = new ObjectInputStream(fileInput);
        @SuppressWarnings({"unchecked"})
        ArrayList<Video> videoObjs = (ArrayList<Video>)objectInput.readObject();
        objectInput.close();

        return videoObjs;

    }

    public void writeFileObj(ArrayList<Video> videoList) throws Exception {
        FileOutputStream fileOutput = new FileOutputStream(storage_filename);
        ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
        objectOutput.writeObject(videoList);

    }






}