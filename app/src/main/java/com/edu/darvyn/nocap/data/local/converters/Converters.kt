package com.edu.darvyn.nocap.data.local.converters

import androidx.room.TypeConverter

 class Converters {
    @TypeConverter
    fun fromListString(value: List<String>?): String {
        return value?.joinToString(separator = "|") ?: ""
    }

    @TypeConverter
    fun toListString(value: String): List<String> {
        if (value.isEmpty()) return emptyList()
        return value.split("|")
    }
}