package io.github.ajiekcx.moviedb.data

import app.cash.sqldelight.db.SqlDriver
import io.github.ajiekcx.moviedb.data.dao.ActorDao
import io.github.ajiekcx.moviedb.data.dao.CastDao
import io.github.ajiekcx.moviedb.data.dao.DirectorDao
import io.github.ajiekcx.moviedb.data.dao.MovieDao
import io.github.ajiekcx.moviedb.data.dao.ReviewDao

/**
 * Wrapper class around SQLDelight-generated MovieDatabase.
 * Provides DAO instances for database operations.
 */
class MovieDatabaseWrapper(driver: SqlDriver) {
    private val database = MovieDatabase(driver)
    
    private val _directorDao: DirectorDao by lazy { DirectorDao(database) }
    private val _movieDao: MovieDao by lazy { MovieDao(database) }
    private val _actorDao: ActorDao by lazy { ActorDao(database) }
    private val _castDao: CastDao by lazy { CastDao(database) }
    private val _reviewDao: ReviewDao by lazy { ReviewDao(database) }
    
    fun directorDao(): DirectorDao = _directorDao
    fun movieDao(): MovieDao = _movieDao
    fun actorDao(): ActorDao = _actorDao
    fun castDao(): CastDao = _castDao
    fun reviewDao(): ReviewDao = _reviewDao
}