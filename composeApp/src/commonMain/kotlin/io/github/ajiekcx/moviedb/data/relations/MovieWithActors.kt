package io.github.ajiekcx.moviedb.data.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import io.github.ajiekcx.moviedb.data.entity.Actor
import io.github.ajiekcx.moviedb.data.entity.Cast
import io.github.ajiekcx.moviedb.data.entity.Movie

data class MovieWithActors(
    @Embedded val movie: Movie,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = Cast::class,
            parentColumn = "movieId",
            entityColumn = "actorId"
        )
    )
    val actors: List<Actor>
)

