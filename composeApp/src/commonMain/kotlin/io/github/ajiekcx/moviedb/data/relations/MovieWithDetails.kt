package io.github.ajiekcx.moviedb.data.relations

import io.github.ajiekcx.moviedb.data.entity.Actor
import io.github.ajiekcx.moviedb.data.entity.Director
import io.github.ajiekcx.moviedb.data.entity.Movie
import io.github.ajiekcx.moviedb.data.entity.Review

data class MovieWithDetails(
    val movie: Movie,
    val director: Director,
    val actors: List<Actor>,
    val reviews: List<Review>
)

