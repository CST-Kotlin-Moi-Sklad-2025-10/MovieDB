package io.github.ajiekcx.moviedb.data.converter

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate

class DateConverters {
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.let { LocalDate.parse(it) }
    }
}

