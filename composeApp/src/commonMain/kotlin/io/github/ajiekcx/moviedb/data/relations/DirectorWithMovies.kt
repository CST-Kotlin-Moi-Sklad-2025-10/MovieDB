package io.github.ajiekcx.moviedb.data.relations

import io.github.ajiekcx.moviedb.data.entity.Director
import io.github.ajiekcx.moviedb.data.entity.Movie

data class DirectorWithMovies(
    val director: Director,
    val movies: List<Movie>
)

