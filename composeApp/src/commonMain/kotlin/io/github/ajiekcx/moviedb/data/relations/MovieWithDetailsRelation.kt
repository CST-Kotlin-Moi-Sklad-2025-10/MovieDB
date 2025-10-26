package io.github.ajiekcx.moviedb.data.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import io.github.ajiekcx.moviedb.data.entity.Actor
import io.github.ajiekcx.moviedb.data.entity.Cast
import io.github.ajiekcx.moviedb.data.entity.Director
import io.github.ajiekcx.moviedb.data.entity.Movie
import io.github.ajiekcx.moviedb.data.entity.Review

/**
 * Room relation class that fetches movie with all related data using joins.
 * This eliminates the N+1 query problem by using Room's @Relation annotations.
 */
data class MovieWithDetailsRelation(
    @Embedded val movie: Movie,
    
    @Relation(
        parentColumn = "directorId",
        entityColumn = "id"
    )
    val director: Director,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = Cast::class,
            parentColumn = "movieId",
            entityColumn = "actorId"
        )
    )
    val actors: List<Actor>,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "movieId"
    )
    val reviews: List<Review>
)

