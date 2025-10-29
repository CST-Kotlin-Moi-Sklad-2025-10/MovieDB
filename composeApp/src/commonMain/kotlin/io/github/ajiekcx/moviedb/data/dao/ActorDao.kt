package io.github.ajiekcx.moviedb.data.dao

import io.github.ajiekcx.moviedb.data.MovieDatabase
import io.github.ajiekcx.moviedb.data.entity.Actor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class ActorDao(private val database: MovieDatabase) {

    suspend fun insert(actor: Actor) = withContext(Dispatchers.IO) {
        database.actorQueries.insert(
            name = actor.name,
            birthYear = actor.birthYear.toLong()
        )
        database.actorQueries.lastInsertRowId().executeAsOne()
    }

    suspend fun insertAll(actors: List<Actor>) = withContext(Dispatchers.IO) {
        database.transaction {
            actors.forEach { actor ->
                database.actorQueries.insert(
                    name = actor.name,
                    birthYear = actor.birthYear.toLong()
                )
            }
        }
    }

    suspend fun getAllActors(): List<Actor> = withContext(Dispatchers.IO) {
        database.actorQueries.getAllActors { id, name, birthYear ->
            Actor(
                id = id,
                name = name,
                birthYear = birthYear.toInt()
            )
        }.executeAsList()
    }

    suspend fun getActorById(actorId: Long): Actor? = withContext(Dispatchers.IO) {
        database.actorQueries.getActorById(actorId) { id, name, birthYear ->
            Actor(
                id = id,
                name = name,
                birthYear = birthYear.toInt()
            )
        }.executeAsOneOrNull()
    }

    suspend fun getActorsByMovie(movieId: Long): List<Actor> = withContext(Dispatchers.IO) {
        database.actorQueries.getActorsByMovie(movieId) { id, name, birthYear ->
            Actor(
                id = id,
                name = name,
                birthYear = birthYear.toInt()
            )
        }.executeAsList()
    }
}

