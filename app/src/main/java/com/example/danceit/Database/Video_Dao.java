package com.example.danceit.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface Video_Dao {
    @Insert
    void insert_video(Video_entity video_entity);

    @Update
    void update(Video_entity video_entity);

    @Delete
    void delete(Video_entity video_entity);

    @Query("SELECT * FROM video_table")
    LiveData<List<Video_entity>> selectAllVideos(Video_entity video_entity);

}
