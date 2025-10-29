package io.github.ajiekcx.moviedb.data.entity

data class Movie(
    val id: Long = 0,
    val title: String,
    val releaseYear: Int,
    val directorId: Long
)

