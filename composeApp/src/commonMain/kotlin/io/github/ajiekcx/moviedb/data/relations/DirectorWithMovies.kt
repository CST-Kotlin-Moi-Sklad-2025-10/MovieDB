package io.github.ajiekcx.moviedb.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import io.github.ajiekcx.moviedb.data.entity.Director
import io.github.ajiekcx.moviedb.data.entity.Movie

data class DirectorWithMovies(
    @Embedded val director: Director,
    @Relation(
        parentColumn = "id",
        entityColumn = "directorId"
    )
    val movies: List<Movie>
)

