package io.github.ajiekcx.moviedb.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import io.github.ajiekcx.moviedb.data.entity.Movie
import io.github.ajiekcx.moviedb.data.relations.MovieWithActors

@Dao
interface MovieDao {
    @Insert
    suspend fun insert(movie: Movie): Long
    
    @Insert
    suspend fun insertAll(movies: List<Movie>): List<Long>
    
    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<Movie>
    
    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovieById(movieId: Long): Movie?
    
    @Query("SELECT * FROM movies WHERE directorId = :directorId")
    suspend fun getMoviesByDirector(directorId: Long): List<Movie>
    
    @Transaction
    @Query("SELECT * FROM movies")
    suspend fun getMoviesWithActors(): List<MovieWithActors>
    
    @Transaction
    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovieWithActors(movieId: Long): MovieWithActors?
}

