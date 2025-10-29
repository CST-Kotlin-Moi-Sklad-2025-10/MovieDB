package io.github.ajiekcx.moviedb.data.relations

import io.github.ajiekcx.moviedb.data.entity.Actor
import io.github.ajiekcx.moviedb.data.entity.Director
import io.github.ajiekcx.moviedb.data.entity.Movie
import io.github.ajiekcx.moviedb.data.entity.Review

/**
 * Relation class that represents movie with all related data.
 * Data will be fetched using JOIN queries in SQLDelight.
 */
data class MovieWithDetailsRelation(
    val movie: Movie,
    val director: Director,
    val actors: List<Actor>,
    val reviews: List<Review>
)

