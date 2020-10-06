//package com.example.danceit.Database;
//
//import android.content.Context;
//
//import androidx.annotation.NonNull;
//import androidx.room.Database;
//import androidx.room.Room;
//import androidx.room.RoomDatabase;
//import androidx.room.TypeConverters;
//import androidx.sqlite.db.SupportSQLiteDatabase;
//
//import com.example.danceit.Model.Video;
//
//@Database(entities = Video.class, version = 5)
//@TypeConverters({Converters.class})
//public abstract class Video_database extends RoomDatabase {
//
//    private static Video_database instance;
//    private static final Object lock = new Object();
//
//    public abstract Video_Dao video_dao();
//
//    public static synchronized Video_database getInstance(Context context) {
//        if(instance == null) {
//            synchronized (lock) {
//                instance = Room.databaseBuilder(context.getApplicationContext(),
//                        Video_database.class, "video_database")
//                        .fallbackToDestructiveMigration()
//                        .allowMainThreadQueries() // change to multiple threads
//                        .build();
//
//            }
//
//        }
//        return instance;
//    }
//
//    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
//        @Override
//        public void onCreate(@NonNull SupportSQLiteDatabase db) {
//            super.onCreate(db);
//        }
//    };
//
//    public static void destroyInstance() {
//        instance = null;
//    }
//
//
//}
