package com.example.newsapp.mvvmnewsapp.data.db

import androidx.room.TypeConverter
import com.example.newsapp.mvvmnewsapp.models.Source

class Converters {
    @TypeConverter
    fun toString(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}