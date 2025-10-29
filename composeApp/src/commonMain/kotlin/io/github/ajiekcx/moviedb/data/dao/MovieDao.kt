package io.github.ajiekcx.moviedb.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import io.github.ajiekcx.moviedb.data.entity.Movie
import io.github.ajiekcx.moviedb.data.relations.MovieWithActors
import io.github.ajiekcx.moviedb.data.relations.MovieWithDetailsRelation

@Dao
interface MovieDao {
    @Insert
    suspend fun insert(movie: Movie)
    
    @Insert
    suspend fun insertAll(movies: List<Movie>)
    
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
    
    /**
     * Fetches all movies with complete details (director, actors, reviews) using joins.
     * The @Transaction annotation ensures this is executed as a single database transaction.
     * Room will automatically perform the necessary joins based on @Relation annotations.
     */
    @Transaction
    @Query("SELECT * FROM movies")
    suspend fun getAllMoviesWithDetailsRelation(): List<MovieWithDetailsRelation>
    
    /**
     * Fetches a single movie with complete details using joins.
     */
    @Transaction
    @Query("SELECT * FROM movies WHERE id = :movieId")
    suspend fun getMovieWithDetailsRelation(movieId: Long): MovieWithDetailsRelation?
}

