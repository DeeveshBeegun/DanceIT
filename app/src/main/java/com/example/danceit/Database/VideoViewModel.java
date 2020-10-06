//package com.example.danceit.Database;
//
//import android.app.Application;
//import androidx.annotation.NonNull;
//
//import androidx.lifecycle.AndroidViewModel;
//import androidx.lifecycle.LiveData;
//
//import com.example.danceit.Model.Video;
//
//import java.util.List;
//
//public class VideoViewModel extends AndroidViewModel {
//    private VideoRepository videoRepository;
//    private LiveData<List<Video>> videos;
//    private List<Video> allVideos;
//
//    public VideoViewModel(@NonNull Application application) {
//        super(application);
//        videoRepository = new VideoRepository(application);
//        videos = videoRepository.getAll();
//        allVideos = videoRepository.getAllVideos();
//    }
//
//    public void insert_video(Video video) {
//        videoRepository.insert_video(video);
//    }
//
//    public void update(Video video) { videoRepository.update(video); }
//
//    public LiveData<List<Video>> getAll() {
//        return videos;
//    }
//
//    public List<Video> getAllVideos() {
//        return allVideos;
//    }
//
//    public void delete_video(Video video) {
//        videoRepository.delete_video(video);
//    }
//
//    public void deleteAll() {videoRepository.deleteAll();}
//}
