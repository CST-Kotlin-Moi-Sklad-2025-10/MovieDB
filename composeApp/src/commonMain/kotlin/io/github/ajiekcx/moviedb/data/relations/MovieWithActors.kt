package io.github.ajiekcx.moviedb.data.relations

import io.github.ajiekcx.moviedb.data.entity.Actor
import io.github.ajiekcx.moviedb.data.entity.Movie

data class MovieWithActors(
    val movie: Movie,
    val actors: List<Actor>
)

