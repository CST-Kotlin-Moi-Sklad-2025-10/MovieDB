package io.github.ajiekcx.moviedb.data.dao

import io.github.ajiekcx.moviedb.data.MovieDatabase
import io.github.ajiekcx.moviedb.data.entity.Cast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

interface CastDao {
    suspend fun insert(cast: Cast)
    suspend fun insertAll(castList: List<Cast>)
    suspend fun getCastByMovie(movieId: Long): List<Cast>
    suspend fun getCastByActor(actorId: Long): List<Cast>
    suspend fun deleteCast(movieId: Long, actorId: Long)
}

class CastDaoImpl(private val database: MovieDatabase) : CastDao {

    override suspend fun insert(cast: Cast) {
        withContext(Dispatchers.IO) {
            database.castQueries.insert(
                movieId = cast.movieId,
                actorId = cast.actorId
            )
        }
    }

    override suspend fun insertAll(castList: List<Cast>) {
        withContext(Dispatchers.IO) {
            database.transaction {
                castList.forEach { cast ->
                    database.castQueries.insert(
                        movieId = cast.movieId,
                        actorId = cast.actorId
                    )
                }
            }
        }
    }

    override suspend fun getCastByMovie(movieId: Long): List<Cast> {
        return withContext(Dispatchers.IO) {
            database.castQueries.getCastByMovie(movieId) { movieId_, actorId ->
                Cast(
                    movieId = movieId_,
                    actorId = actorId
                )
            }.executeAsList()
        }
    }

    override suspend fun getCastByActor(actorId: Long): List<Cast> {
        return withContext(Dispatchers.IO) {
            database.castQueries.getCastByActor(actorId) { movieId, actorId_ ->
                Cast(
                    movieId = movieId,
                    actorId = actorId_
                )
            }.executeAsList()
        }
    }

    override suspend fun deleteCast(movieId: Long, actorId: Long) {
        withContext(Dispatchers.IO) {
            database.castQueries.deleteCast(movieId, actorId)
        }
    }
}

