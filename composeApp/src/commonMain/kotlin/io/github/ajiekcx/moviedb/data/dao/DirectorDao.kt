package io.github.ajiekcx.moviedb.data.dao

import io.github.ajiekcx.moviedb.data.MovieDatabase
import io.github.ajiekcx.moviedb.data.entity.Director
import io.github.ajiekcx.moviedb.data.entity.Movie
import io.github.ajiekcx.moviedb.data.relations.DirectorWithMovies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class DirectorDao(private val database: MovieDatabase) {
    
    suspend fun insert(director: Director): Long = withContext(Dispatchers.IO) {
        database.directorQueries.insert(
            name = director.name,
            birthDate = director.birthDate.toString()
        )
        database.directorQueries.lastInsertRowId().executeAsOne()
    }
    
    suspend fun insertAll(directors: List<Director>) = withContext(Dispatchers.IO) {
        database.transaction {
            directors.forEach { director ->
                database.directorQueries.insert(
                    name = director.name,
                    birthDate = director.birthDate.toString()
                )
            }
        }
    }
    
    suspend fun getAllDirectors(): List<Director> = withContext(Dispatchers.IO) {
        database.directorQueries.getAllDirectors { id, name, birthDate ->
            Director(
                id = id,
                name = name,
                birthDate = kotlinx.datetime.LocalDate.parse(birthDate)
            )
        }.executeAsList()
    }
    
    suspend fun getDirectorById(directorId: Long): Director? = withContext(Dispatchers.IO) {
        database.directorQueries.getDirectorById(directorId) { id, name, birthDate ->
            Director(
                id = id,
                name = name,
                birthDate = kotlinx.datetime.LocalDate.parse(birthDate)
            )
        }.executeAsOneOrNull()
    }
    
    suspend fun getDirectorsWithMovies(): List<DirectorWithMovies> = withContext(Dispatchers.IO) {
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
    
    suspend fun getDirectorWithMovies(directorId: Long): DirectorWithMovies? = withContext(Dispatchers.IO) {
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

