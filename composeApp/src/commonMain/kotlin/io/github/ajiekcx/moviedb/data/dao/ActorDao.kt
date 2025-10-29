package io.github.ajiekcx.moviedb.data.dao

import io.github.ajiekcx.moviedb.data.MovieDatabase
import io.github.ajiekcx.moviedb.data.entity.Actor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

interface ActorDao {
    suspend fun insert(actor: Actor): Long
    suspend fun insertAll(actors: List<Actor>)
    suspend fun getAllActors(): List<Actor>
    suspend fun getActorById(actorId: Long): Actor?
    suspend fun getActorsByMovie(movieId: Long): List<Actor>
}

class ActorDaoImpl(private val database: MovieDatabase) : ActorDao {

    override suspend fun insert(actor: Actor): Long {
        return withContext(Dispatchers.IO) {
            database.actorQueries.insert(
                name = actor.name,
                birthYear = actor.birthYear.toLong()
            )
            database.actorQueries.lastInsertRowId().executeAsOne()
        }
    }

    override suspend fun insertAll(actors: List<Actor>) {
        return withContext(Dispatchers.IO) {
            database.transaction {
                actors.forEach { actor ->
                    database.actorQueries.insert(
                        name = actor.name,
                        birthYear = actor.birthYear.toLong()
                    )
                }
            }
        }
    }

    override suspend fun getAllActors(): List<Actor> {
        return withContext(Dispatchers.IO) {
            database.actorQueries.getAllActors { id, name, birthYear ->
                Actor(
                    id = id,
                    name = name,
                    birthYear = birthYear.toInt()
                )
            }.executeAsList()
        }
    }

    override suspend fun getActorById(actorId: Long): Actor? {
        return withContext(Dispatchers.IO) {
            database.actorQueries.getActorById(actorId) { id, name, birthYear ->
                Actor(
                    id = id,
                    name = name,
                    birthYear = birthYear.toInt()
                )
            }.executeAsOneOrNull()
        }
    }

    override suspend fun getActorsByMovie(movieId: Long): List<Actor> {
        return withContext(Dispatchers.IO) {
            database.actorQueries.getActorsByMovie(movieId) { id, name, birthYear ->
                Actor(
                    id = id,
                    name = name,
                    birthYear = birthYear.toInt()
                )
            }.executeAsList()
        }
    }
}

