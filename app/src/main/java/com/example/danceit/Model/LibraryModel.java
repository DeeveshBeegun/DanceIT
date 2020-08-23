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

    public ArrayList<Video> readFileObj(String storage_filename) throws Exception {
        FileInputStream fileInput = new FileInputStream(storage_filename);
        ObjectInputStream objectInput = new ObjectInputStream(fileInput);
        @SuppressWarnings({"unchecked"})
        ArrayList<Video> videoObjs = (ArrayList<Video>)objectInput.readObject();
        objectInput.close();

        return videoObjs;

    }




}