import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class LibraryModel {

   private ArrayList<Video> videoList;

    public LibraryModel() {
        videoList = new ArrayList<Video>();
    }

    public void addVideo(Video video) {
        videoList.add(video);
    }

    public void saveVideo() {
        String filename = "URL_storage.txt";
        try {
            for(int i = 0; i < videoList.size(); i++) {
                FileOutputStream fileOutput = new FileOutputStream(filename, true);
                ObjectOutputStream objectOutput = new ObjectOutputStream(fileOut);
                objectOutput.writeObject(videoList.indexOf(i));
                objectOutput.close();
            }
            videoList.clear();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


}