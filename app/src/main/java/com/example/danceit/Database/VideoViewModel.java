package com.example.danceit.Database;

import android.app.Application;
import android.support.annotation.NonNull;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.danceit.Model.Video;

import java.util.List;

public class VideoViewModel extends AndroidViewModel {
    private VideoRepositoty videoRepositoty;
    private LiveData<List<Video>> videos;

    public VideoViewModel(@NonNull Application application) {
        super(application);
        videoRepositoty = new VideoRepositoty(application);
        videos = videoRepositoty.getAll();
    }

    public void insert(Video video) {
        videoRepositoty.insert_video(video);
    }

    public LiveData<List<Video>> getAll() {
        return videos;
    }

}
