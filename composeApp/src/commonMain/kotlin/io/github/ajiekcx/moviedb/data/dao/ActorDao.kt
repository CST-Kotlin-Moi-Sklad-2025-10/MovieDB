package io.github.ajiekcx.moviedb.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.github.ajiekcx.moviedb.data.entity.Actor

@Dao
interface ActorDao {
    @Insert
    suspend fun insert(actor: Actor)
    
    @Insert
    suspend fun insertAll(actors: List<Actor>)
    
    @Query("SELECT * FROM actors")
    suspend fun getAllActors(): List<Actor>
    
    @Query("SELECT * FROM actors WHERE id = :actorId")
    suspend fun getActorById(actorId: Long): Actor?
    
    @Query(
        "SELECT a.id, a.name, a.birthYear FROM actors a " +
        "INNER JOIN film_cast c ON a.id = c.actorId " +
        "WHERE c.movieId = :movieId"
    )
    suspend fun getActorsByMovie(movieId: Long): List<Actor>
}

