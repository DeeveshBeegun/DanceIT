package com.example.danceit.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.danceit.Model.Video;

import java.util.List;

@Dao
public interface Video_Dao {
    @Insert
    void insert_video(Video video);

    @Update
    void update(Video video);

    @Delete
    void delete_video(Video video);

    @Query("SELECT * FROM video_table")
    LiveData<List<Video>> getAll();

    @Query("SELECT * FROM video_table")
    List<Video> getAllVideos();

    @Query("SELECT * FROM video_table ORDER BY videoId DESC LIMIT 1")
    List<Video> getRecentVideo();

}
