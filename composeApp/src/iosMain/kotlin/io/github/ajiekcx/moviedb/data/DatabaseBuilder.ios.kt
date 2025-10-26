package io.github.ajiekcx.moviedb.data

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSHomeDirectory

actual object DatabaseBuilder {
    actual fun build(): RoomDatabase.Builder<MovieDatabase> {
        val dbFilePath = NSHomeDirectory() + "/movie.db"
        return Room.databaseBuilder<MovieDatabase>(
            name = dbFilePath
        )
    }
}

