package io.github.ajiekcx.moviedb.data.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import io.github.ajiekcx.moviedb.data.DatabaseBuilder
import io.github.ajiekcx.moviedb.data.MovieDatabase
import io.github.ajiekcx.moviedb.data.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

object AppDIContainer {
    private val database: MovieDatabase by lazy {
        DatabaseBuilder.build()
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    val repository: MovieRepository by lazy {
        MovieRepository(database)
    }
}