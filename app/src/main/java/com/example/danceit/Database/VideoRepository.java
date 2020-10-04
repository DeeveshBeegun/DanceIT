package com.example.danceit.Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.danceit.Model.Video;

import java.util.List;

public class VideoRepository {
    private Video_Dao video_dao;
    private LiveData<List<Video>> videos;
    private List<Video> allVideos;

    public VideoRepository(Application application) {
        Video_database video_database = Video_database.getInstance(application);
        video_dao = video_database.video_dao();
        videos = video_dao.getAll();
        allVideos = video_dao.getAllVideos();
    }

    public void insert_video(Video video) {
        new InsertAsync(video_dao).execute(video);
    }

    public void update(Video video) { new UpdateAsync(video_dao).execute(video); }

    public void delete_video(Video video) { new DeleteAsync(video_dao).execute(video); }

    public void deleteAll() { new DeleteAllAsync(video_dao).execute(); }

    public LiveData<List<Video>> getAll() {
        return videos;
    }

    public List<Video> getAllVideos() {
        return allVideos;
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

    private static class UpdateAsync extends AsyncTask<Video, Void, Void> {
        private Video_Dao video_dao;

        private UpdateAsync(Video_Dao video_dao) {
            this.video_dao = video_dao;
        }

        @Override
        protected Void doInBackground(Video... videos) {
            video_dao.update(videos[0]);
            return null;
        }

    }

    private static class DeleteAsync extends AsyncTask<Video, Void, Void> {
        private Video_Dao video_dao;

        private DeleteAsync(Video_Dao video_dao) {
            this.video_dao = video_dao;
        }

        @Override
        protected Void doInBackground(Video... videos) {
            video_dao.delete_video(videos[0]);
            return null;
        }

    }

    private static class DeleteAllAsync extends AsyncTask<Video, Void, Void> {
        private Video_Dao video_dao;

        private DeleteAllAsync(Video_Dao video_dao) {
            this.video_dao = video_dao;
        }

        @Override
        protected Void doInBackground(Video... videos) {
            video_dao.deleteAll();
            return null;
        }

    }

}
