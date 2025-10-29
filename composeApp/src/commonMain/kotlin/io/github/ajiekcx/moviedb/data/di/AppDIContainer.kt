package io.github.ajiekcx.moviedb.data.di

import io.github.ajiekcx.moviedb.data.DatabaseBuilder
import io.github.ajiekcx.moviedb.data.MovieDatabaseWrapper
import io.github.ajiekcx.moviedb.data.repository.MovieRepository

object AppDIContainer {
    private val database: MovieDatabaseWrapper by lazy {
        val driver = DatabaseBuilder.createDriver()
        MovieDatabaseWrapper(driver)
    }

    val repository: MovieRepository by lazy {
        MovieRepository(database)
    }
}