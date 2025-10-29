package io.github.ajiekcx.moviedb.data.dao

import io.github.ajiekcx.moviedb.data.MovieDatabase
import io.github.ajiekcx.moviedb.data.entity.Cast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class CastDao(private val database: MovieDatabase) {
    
    suspend fun insert(cast: Cast): Unit = withContext(Dispatchers.IO) {
        database.castQueries.insert(
            movieId = cast.movieId,
            actorId = cast.actorId
        )
    }
    
    suspend fun insertAll(castList: List<Cast>) = withContext(Dispatchers.IO) {
        database.transaction {
            castList.forEach { cast ->
                database.castQueries.insert(
                    movieId = cast.movieId,
                    actorId = cast.actorId
                )
            }
        }
    }
    
    suspend fun getCastByMovie(movieId: Long): List<Cast> = withContext(Dispatchers.IO) {
        database.castQueries.getCastByMovie(movieId) { movieId_, actorId ->
            Cast(
                movieId = movieId_,
                actorId = actorId
            )
        }.executeAsList()
    }
    
    suspend fun getCastByActor(actorId: Long): List<Cast> = withContext(Dispatchers.IO) {
        database.castQueries.getCastByActor(actorId) { movieId, actorId_ ->
            Cast(
                movieId = movieId,
                actorId = actorId_
            )
        }.executeAsList()
    }
    
    suspend fun deleteCast(movieId: Long, actorId: Long) = withContext(Dispatchers.IO) {
        database.castQueries.deleteCast(movieId, actorId)
    }
}

