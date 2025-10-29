package io.github.ajiekcx.moviedb.data.dao

import io.github.ajiekcx.moviedb.data.MovieDatabase
import io.github.ajiekcx.moviedb.data.entity.Director
import io.github.ajiekcx.moviedb.data.entity.Movie
import io.github.ajiekcx.moviedb.data.relations.DirectorWithMovies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

interface DirectorDao {
    suspend fun insert(director: Director): Long
    suspend fun insertAll(directors: List<Director>)
    suspend fun getAllDirectors(): List<Director>
    suspend fun getDirectorById(directorId: Long): Director?
    suspend fun getDirectorsWithMovies(): List<DirectorWithMovies>
    suspend fun getDirectorWithMovies(directorId: Long): DirectorWithMovies?
}

class DirectorDaoImpl(private val database: MovieDatabase) : DirectorDao {
    
    override suspend fun insert(director: Director): Long {
        return withContext(Dispatchers.IO) {
            database.directorQueries.insert(
                name = director.name,
                birthDate = director.birthDate.toString()
            )
            database.directorQueries.lastInsertRowId().executeAsOne()
        }
    }
    
    override suspend fun insertAll(directors: List<Director>) {
        withContext(Dispatchers.IO) {
            database.transaction {
                directors.forEach { director ->
                    database.directorQueries.insert(
                        name = director.name,
                        birthDate = director.birthDate.toString()
                    )
                }
            }
        }
    }
    
    override suspend fun getAllDirectors(): List<Director> {
        return withContext(Dispatchers.IO) {
            database.directorQueries.getAllDirectors { id, name, birthDate ->
                Director(
                    id = id,
                    name = name,
                    birthDate = kotlinx.datetime.LocalDate.parse(birthDate)
                )
            }.executeAsList()
        }
    }
    
    override suspend fun getDirectorById(directorId: Long): Director? {
        return withContext(Dispatchers.IO) {
            database.directorQueries.getDirectorById(directorId) { id, name, birthDate ->
                Director(
                    id = id,
                    name = name,
                    birthDate = kotlinx.datetime.LocalDate.parse(birthDate)
                )
            }.executeAsOneOrNull()
        }
    }
    
    override suspend fun getDirectorsWithMovies(): List<DirectorWithMovies> {
        return withContext(Dispatchers.IO) {
            val results = database.directorRelationsQueries.getDirectorsWithMovies().executeAsList()

            results.groupBy {
                Triple(it.director_id, it.director_name, it.director_birthDate)
            }.map { (directorData, rows) ->
                val director = Director(
                    id = directorData.first,
                    name = directorData.second,
                    birthDate = kotlinx.datetime.LocalDate.parse(directorData.third)
                )
                val movies = rows.mapNotNull { row ->
                    if (row.movie_id != null) {
                        Movie(
                            id = row.movie_id,
                            title = row.movie_title!!,
                            releaseYear = row.movie_releaseYear!!.toInt(),
                            directorId = row.movie_directorId!!
                        )
                    } else null
                }
                DirectorWithMovies(director = director, movies = movies)
            }
        }
    }
    
    override suspend fun getDirectorWithMovies(directorId: Long): DirectorWithMovies? {
        return withContext(Dispatchers.IO) {
            val results = database.directorRelationsQueries.getDirectorWithMovies(directorId).executeAsList()

            if (results.isEmpty()) return@withContext null

            val firstRow = results.first()
            val director = Director(
                id = firstRow.director_id,
                name = firstRow.director_name,
                birthDate = kotlinx.datetime.LocalDate.parse(firstRow.director_birthDate)
            )
            val movies = results.mapNotNull { row ->
                if (row.movie_id != null) {
                    Movie(
                        id = row.movie_id,
                        title = row.movie_title!!,
                        releaseYear = row.movie_releaseYear!!.toInt(),
                        directorId = row.movie_directorId!!
                    )
                } else null
            }
            DirectorWithMovies(director = director, movies = movies)
        }
    }
}

