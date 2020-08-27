package com.example.danceit.Database;

import androidx.room.TypeConverter;

import com.example.danceit.Model.User;

public class Converters {
    @TypeConverter
    public String fromUser(User user) {
        return user.getName();
    }

    @TypeConverter
    public User fromString(String username, String password) {
        return new User(username, password);
    }
}
