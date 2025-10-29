package io.github.ajiekcx.moviedb.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.github.ajiekcx.moviedb.data.entity.Review

@Dao
interface ReviewDao {
    @Insert
    suspend fun insert(review: Review)
    
    @Insert
    suspend fun insertAll(reviews: List<Review>): List<Long>
    
    @Query("SELECT * FROM reviews WHERE movieId = :movieId")
    suspend fun getReviewsByMovie(movieId: Long): List<Review>
    
    @Query("SELECT * FROM reviews WHERE id = :reviewId")
    suspend fun getReviewById(reviewId: Long): Review?
    
    @Query("SELECT AVG(rating) FROM reviews WHERE movieId = :movieId")
    suspend fun getAverageRating(movieId: Long): Double?
}

