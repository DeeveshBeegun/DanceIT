package com.example.danceit.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.danceit.Model.Video;

@Database(entities = Video.class, version = 2)
@TypeConverters({Converters.class})
public abstract class Video_database extends RoomDatabase {

    private static Video_database instance;

    public abstract Video_Dao video_dao();

    public static synchronized Video_database getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    Video_database.class, "video_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() // change to multiple threads
                    .build();

        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }


}
