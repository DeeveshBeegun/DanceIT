package com.example.danceit.Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.danceit.Model.Video;

import java.util.List;

public class VideoRepository {
    private Video_Dao video_dao;
    private LiveData<List<Video>> videos;

    public VideoRepository(Application application) {
        Video_database video_database = Video_database.getInstance(application);
        video_dao = video_database.video_dao();
        videos = video_dao.getAll();

    }

    public void insert_video(Video video) {
        new InsertAsync(video_dao).execute(video);
    }

    public LiveData<List<Video>> getAll() {
        return videos;
    }

    private static class InsertAsync extends AsyncTask<Video, Void, Void> {
       private Video_Dao video_dao;

       private InsertAsync(Video_Dao video_dao) {
           this.video_dao = video_dao;
       }

        @Override
        protected Void doInBackground(Video... videos) {
           video_dao.insert_video(videos[0]);
            return null;
        }
    }

}
