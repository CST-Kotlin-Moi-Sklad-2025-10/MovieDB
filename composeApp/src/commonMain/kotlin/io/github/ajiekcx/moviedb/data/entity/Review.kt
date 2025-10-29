package io.github.ajiekcx.moviedb.data.entity

data class Review(
    val id: Long = 0,
    val movieId: Long,
    val rating: Int, // 1-5 stars
    val comment: String
)

