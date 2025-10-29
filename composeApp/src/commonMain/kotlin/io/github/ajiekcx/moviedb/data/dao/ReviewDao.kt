package io.github.ajiekcx.moviedb.data.dao

import io.github.ajiekcx.moviedb.data.MovieDatabase
import io.github.ajiekcx.moviedb.data.entity.Review
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class ReviewDao(private val database: MovieDatabase) {
    
    suspend fun insert(review: Review): Long = withContext(Dispatchers.IO) {
        database.reviewQueries.insert(
            movieId = review.movieId,
            rating = review.rating.toLong(),
            comment = review.comment
        )
        database.reviewQueries.lastInsertRowId().executeAsOne()
    }
    
    suspend fun insertAll(reviews: List<Review>): List<Long> = withContext(Dispatchers.IO) {
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
    
    suspend fun getReviewsByMovie(movieId: Long): List<Review> = withContext(Dispatchers.IO) {
        database.reviewQueries.getReviewsByMovie(movieId) { id, movieId_, rating, comment ->
            Review(
                id = id,
                movieId = movieId_,
                rating = rating.toInt(),
                comment = comment
            )
        }.executeAsList()
    }
    
    suspend fun getReviewById(reviewId: Long): Review? = withContext(Dispatchers.IO) {
        database.reviewQueries.getReviewById(reviewId) { id, movieId, rating, comment ->
            Review(
                id = id,
                movieId = movieId,
                rating = rating.toInt(),
                comment = comment
            )
        }.executeAsOneOrNull()
    }
    
    suspend fun getAverageRating(movieId: Long): Double? = withContext(Dispatchers.IO) {
        database.reviewQueries.getAverageRating(movieId).executeAsOneOrNull()?.AVG
    }
}

