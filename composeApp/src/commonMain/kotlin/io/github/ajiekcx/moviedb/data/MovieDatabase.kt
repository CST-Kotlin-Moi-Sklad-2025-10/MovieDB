package io.github.ajiekcx.moviedb.data

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.ajiekcx.moviedb.data.dao.ActorDao
import io.github.ajiekcx.moviedb.data.dao.CastDao
import io.github.ajiekcx.moviedb.data.dao.DirectorDao
import io.github.ajiekcx.moviedb.data.dao.MovieDao
import io.github.ajiekcx.moviedb.data.dao.ReviewDao
import io.github.ajiekcx.moviedb.data.entity.Actor
import io.github.ajiekcx.moviedb.data.entity.Cast
import io.github.ajiekcx.moviedb.data.entity.Director
import io.github.ajiekcx.moviedb.data.entity.Movie
import io.github.ajiekcx.moviedb.data.entity.Review

@Database(
    entities = [
        Director::class,
        Movie::class,
        Actor::class,
        Cast::class,
        Review::class
    ],
    version = 1,
    exportSchema = true
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun directorDao(): DirectorDao
    abstract fun movieDao(): MovieDao
    abstract fun actorDao(): ActorDao
    abstract fun castDao(): CastDao
    abstract fun reviewDao(): ReviewDao
}

