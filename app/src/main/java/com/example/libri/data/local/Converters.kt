package com.example.libri.data.local

import androidx.room.TypeConverter

class Converters {
  @TypeConverter
  fun fromStringToList(string: String): List<String> {
    return string.split(",")
  }

  @TypeConverter
  fun listToString(list: List<String>): String {
    return list.joinToString()
  }
}