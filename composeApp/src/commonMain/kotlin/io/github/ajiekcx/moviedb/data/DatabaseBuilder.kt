package io.github.ajiekcx.moviedb.data

import androidx.room.RoomDatabase

expect object DatabaseBuilder {
    fun build(): RoomDatabase.Builder<MovieDatabase>
}

