package io.github.ajiekcx.moviedb.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.github.ajiekcx.moviedb.data.entity.Cast

@Dao
interface CastDao {
    @Insert
    suspend fun insert(cast: Cast)
    
    @Insert
    suspend fun insertAll(castList: List<Cast>)
    
    @Query("SELECT * FROM cast WHERE movieId = :movieId")
    suspend fun getCastByMovie(movieId: Long): List<Cast>
    
    @Query("SELECT * FROM cast WHERE actorId = :actorId")
    suspend fun getCastByActor(actorId: Long): List<Cast>
    
    @Query("DELETE FROM cast WHERE movieId = :movieId AND actorId = :actorId")
    suspend fun deleteCast(movieId: Long, actorId: Long)
}

