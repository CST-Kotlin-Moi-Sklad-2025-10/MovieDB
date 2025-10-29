package io.github.ajiekcx.moviedb.data.dao

import io.github.ajiekcx.moviedb.data.MovieDatabase
import io.github.ajiekcx.moviedb.data.entity.Review
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

interface ReviewDao {
    suspend fun insert(review: Review): Long
    suspend fun insertAll(reviews: List<Review>): List<Long>
    suspend fun getReviewsByMovie(movieId: Long): List<Review>
    suspend fun getReviewById(reviewId: Long): Review?
    suspend fun getAverageRating(movieId: Long): Double?
}

class ReviewDaoImpl(private val database: MovieDatabase) : ReviewDao {
    
    override suspend fun insert(review: Review): Long {
        return withContext(Dispatchers.IO) {
            database.reviewQueries.insert(
                movieId = review.movieId,
                rating = review.rating.toLong(),
                comment = review.comment
            )
            database.reviewQueries.lastInsertRowId().executeAsOne()
        }
    }
    
    override suspend fun insertAll(reviews: List<Review>): List<Long> {
        return withContext(Dispatchers.IO) {
            database.transactionWithResult {
                reviews.map { review ->
                    database.reviewQueries.insert(
                        movieId = review.movieId,
                        rating = review.rating.toLong(),
                        comment = review.comment
                    )
                    database.reviewQueries.lastInsertRowId().executeAsOne()
                }
            }
        }
    }
    
    override suspend fun getReviewsByMovie(movieId: Long): List<Review> {
        return withContext(Dispatchers.IO) {
            database.reviewQueries.getReviewsByMovie(movieId) { id, movieId_, rating, comment ->
                Review(
                    id = id,
                    movieId = movieId_,
                    rating = rating.toInt(),
                    comment = comment
                )
            }.executeAsList()
        }
    }
    
    override suspend fun getReviewById(reviewId: Long): Review? {
        return withContext(Dispatchers.IO) {
            database.reviewQueries.getReviewById(reviewId) { id, movieId, rating, comment ->
                Review(
                    id = id,
                    movieId = movieId,
                    rating = rating.toInt(),
                    comment = comment
                )
            }.executeAsOneOrNull()
        }
    }
    
    override suspend fun getAverageRating(movieId: Long): Double? {
        return withContext(Dispatchers.IO) {
            database.reviewQueries.getAverageRating(movieId).executeAsOneOrNull()?.AVG
        }
    }
}

