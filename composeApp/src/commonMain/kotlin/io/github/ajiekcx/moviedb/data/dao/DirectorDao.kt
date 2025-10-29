package io.github.ajiekcx.moviedb.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import io.github.ajiekcx.moviedb.data.entity.Director
import io.github.ajiekcx.moviedb.data.relations.DirectorWithMovies

@Dao
interface DirectorDao {
    @Insert
    suspend fun insert(director: Director)
    
    @Insert
    suspend fun insertAll(directors: List<Director>)
    
    @Query("SELECT * FROM directors")
    suspend fun getAllDirectors(): List<Director>
    
    @Query("SELECT * FROM directors WHERE id = :directorId")
    suspend fun getDirectorById(directorId: Long): Director?
    
    @Transaction
    @Query("SELECT * FROM directors")
    suspend fun getDirectorsWithMovies(): List<DirectorWithMovies>
    
    @Transaction
    @Query("SELECT * FROM directors WHERE id = :directorId")
    suspend fun getDirectorWithMovies(directorId: Long): DirectorWithMovies?
}

