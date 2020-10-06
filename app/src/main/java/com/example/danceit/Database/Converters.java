//package com.example.danceit.Database;
//
//import androidx.room.TypeConverter;
//
//import com.example.danceit.Model.Tag;
//import com.example.danceit.Model.User;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//
//public class Converters {
//    @TypeConverter
//    public String fromUser(User user) {
//        return user.getName();
//    }
//
//    @TypeConverter
//    public User fromString(String username) {
//        return new User(username, username);
//    }
//
//    @TypeConverter
//    public static ArrayList<Tag> fromStringArray(String value) {
//       Type arrayListType = new TypeToken<ArrayList<Tag>>() {}.getType();
//        return new Gson().fromJson(value, arrayListType);
//    }
//
//    @TypeConverter
//    public static String fromArrayList(ArrayList<Tag> list) {
//        Gson gson = new Gson();
//        String json = gson.toJson(list);
//        return json;
//    }
//}
