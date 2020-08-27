package com.example.danceit.Database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.danceit.Model.User;
import com.example.danceit.Model.Video;

@Entity(tableName = "video_table")
public class Video_entity {

    @PrimaryKey(autoGenerate = true)
    private int videoId;
    private User user;
    private Video video;

    public Video_entity(User user, Video video) {
        this.user = user;
        this.video = video;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public int getVideoId() {
        return videoId;
    }

    public User getUser() {
        return user;
    }

    public Video getVideo() {
        return video;
    }
}
