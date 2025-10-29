package io.github.ajiekcx.moviedb.data

import app.cash.sqldelight.db.SqlDriver
import io.github.ajiekcx.moviedb.data.dao.ActorDao
import io.github.ajiekcx.moviedb.data.dao.ActorDaoImpl
import io.github.ajiekcx.moviedb.data.dao.CastDao
import io.github.ajiekcx.moviedb.data.dao.CastDaoImpl
import io.github.ajiekcx.moviedb.data.dao.DirectorDao
import io.github.ajiekcx.moviedb.data.dao.DirectorDaoImpl
import io.github.ajiekcx.moviedb.data.dao.MovieDao
import io.github.ajiekcx.moviedb.data.dao.MovieDaoImpl
import io.github.ajiekcx.moviedb.data.dao.ReviewDao
import io.github.ajiekcx.moviedb.data.dao.ReviewDaoImpl

/**
 * Wrapper class around SQLDelight-generated MovieDatabase.
 * Provides DAO instances for database operations.
 */
class MovieDatabaseWrapper(driver: SqlDriver) {
    private val database = MovieDatabase(driver)
    
    private val _directorDao: DirectorDao by lazy { DirectorDaoImpl(database) }
    private val _movieDao: MovieDao by lazy { MovieDaoImpl(database) }
    private val _actorDao: ActorDao by lazy { ActorDaoImpl(database) }
    private val _castDao: CastDao by lazy { CastDaoImpl(database) }
    private val _reviewDao: ReviewDao by lazy { ReviewDaoImpl(database) }
    
    fun directorDao(): DirectorDao = _directorDao
    fun movieDao(): MovieDao = _movieDao
    fun actorDao(): ActorDao = _actorDao
    fun castDao(): CastDao = _castDao
    fun reviewDao(): ReviewDao = _reviewDao
}