package com.example.newsapp.mvvmnewsapp.data.db

import androidx.room.TypeConverter
import com.example.newsapp.mvvmnewsapp.models.Source

class Converters {
    //we made this class because room can only handle primitive data types and string not custom classes
    @TypeConverter
    fun toString(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}