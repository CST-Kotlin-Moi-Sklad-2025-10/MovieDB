package io.github.ajiekcx.moviedb.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

actual object DatabaseBuilder {
    private lateinit var appContext: Context
    
    fun init(context: Context) {
        appContext = context.applicationContext
    }
    
    actual fun build(): RoomDatabase.Builder<MovieDatabase> {
        val dbFile = appContext.getDatabasePath("movie.db")
        return Room.databaseBuilder<MovieDatabase>(
            context = appContext,
            name = dbFile.absolutePath
        )
    }
}

