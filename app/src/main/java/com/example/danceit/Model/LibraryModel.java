import java.util.ArrayList;

public class LibraryModel {

   private ArrayList<Video> videoList;

    public LibraryModel() {
        videoList = new ArrayList<Video>();
    }

    public void addVideo(Video video) {
        videoList.add(video);
    }


}