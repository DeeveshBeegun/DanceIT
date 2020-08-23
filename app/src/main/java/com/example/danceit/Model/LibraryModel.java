import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

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




}