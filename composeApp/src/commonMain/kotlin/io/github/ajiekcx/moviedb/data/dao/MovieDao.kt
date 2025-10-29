package io.github.ajiekcx.moviedb.data.dao

import io.github.ajiekcx.moviedb.data.MovieDatabase
import io.github.ajiekcx.moviedb.data.entity.Actor
import io.github.ajiekcx.moviedb.data.entity.Director
import io.github.ajiekcx.moviedb.data.entity.Movie
import io.github.ajiekcx.moviedb.data.entity.Review
import io.github.ajiekcx.moviedb.data.relations.MovieWithActors
import io.github.ajiekcx.moviedb.data.relations.MovieWithDetailsRelation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

interface MovieDao {
    suspend fun insert(movie: Movie): Long
    suspend fun insertAll(movies: List<Movie>)
    suspend fun getAllMovies(): List<Movie>
    suspend fun getMovieById(movieId: Long): Movie?
    suspend fun getMoviesByDirector(directorId: Long): List<Movie>
    suspend fun getMoviesWithActors(): List<MovieWithActors>
    suspend fun getMovieWithActors(movieId: Long): MovieWithActors?
    suspend fun getAllMoviesWithDetailsRelation(): List<MovieWithDetailsRelation>
    suspend fun getMovieWithDetailsRelation(movieId: Long): MovieWithDetailsRelation?
}

class MovieDaoImpl(private val database: MovieDatabase) : MovieDao {
    
    override suspend fun insert(movie: Movie): Long {
        return withContext(Dispatchers.IO) {
            database.movieQueries.insert(
                title = movie.title,
                releaseYear = movie.releaseYear.toLong(),
                directorId = movie.directorId
            )
            database.movieQueries.lastInsertRowId().executeAsOne()
        }
    }
    
    override suspend fun insertAll(movies: List<Movie>) {
        return withContext(Dispatchers.IO) {
            database.transaction {
                movies.forEach { movie ->
                    database.movieQueries.insert(
                        title = movie.title,
                        releaseYear = movie.releaseYear.toLong(),
                        directorId = movie.directorId
                    )
                }
            }
        }
    }
    
    override suspend fun getAllMovies(): List<Movie> {
        return withContext(Dispatchers.IO) {
            database.movieQueries.getAllMovies { id, title, releaseYear, directorId ->
                Movie(
                    id = id,
                    title = title,
                    releaseYear = releaseYear.toInt(),
                    directorId = directorId
                )
            }.executeAsList()
        }
    }
    
    override suspend fun getMovieById(movieId: Long): Movie? {
        return withContext(Dispatchers.IO) {
            database.movieQueries.getMovieById(movieId) { id, title, releaseYear, directorId ->
                Movie(
                    id = id,
                    title = title,
                    releaseYear = releaseYear.toInt(),
                    directorId = directorId
                )
            }.executeAsOneOrNull()
        }
    }
    
    override suspend fun getMoviesByDirector(directorId: Long): List<Movie> {
        return withContext(Dispatchers.IO) {
            database.movieQueries.getMoviesByDirector(directorId) { id, title, releaseYear, directorId_ ->
                Movie(
                    id = id,
                    title = title,
                    releaseYear = releaseYear.toInt(),
                    directorId = directorId_
                )
            }.executeAsList()
        }
    }
    
    override suspend fun getMoviesWithActors(): List<MovieWithActors> {
        return withContext(Dispatchers.IO) {
            val results = database.movieRelationsQueries.getMoviesWithActors().executeAsList()

            results.groupBy {
                it.movie_id
            }.map { (movieId, rows) ->
                val firstRow = rows.first()
                val movie = Movie(
                    id = firstRow.movie_id,
                    title = firstRow.movie_title,
                    releaseYear = firstRow.movie_releaseYear.toInt(),
                    directorId = firstRow.movie_directorId
                )
                val actors = rows.mapNotNull { row ->
                    if (row.actor_id != null) {
                        Actor(
                            id = row.actor_id,
                            name = row.actor_name!!,
                            birthYear = row.actor_birthYear!!.toInt()
                        )
                    } else null
                }
                MovieWithActors(movie = movie, actors = actors)
            }
        }
    }
    
    override suspend fun getMovieWithActors(movieId: Long): MovieWithActors? {
        return withContext(Dispatchers.IO) {
            val results = database.movieRelationsQueries.getMovieWithActors(movieId).executeAsList()

            if (results.isEmpty()) return@withContext null

            val firstRow = results.first()
            val movie = Movie(
                id = firstRow.movie_id,
                title = firstRow.movie_title,
                releaseYear = firstRow.movie_releaseYear.toInt(),
                directorId = firstRow.movie_directorId
            )
            val actors = results.mapNotNull { row ->
                if (row.actor_id != null) {
                    Actor(
                        id = row.actor_id,
                        name = row.actor_name!!,
                        birthYear = row.actor_birthYear!!.toInt()
                    )
                } else null
            }
            MovieWithActors(movie = movie, actors = actors)
        }
    }
    
    /**
     * Fetches all movies with complete details (director, actors, reviews) using joins.
     * Uses a single JOIN query for optimal performance.
     */
    override suspend fun getAllMoviesWithDetailsRelation(): List<MovieWithDetailsRelation> = withContext(Dispatchers.IO) {
        val results = database.movieDetailsRelationsQueries.getAllMoviesWithDetailsRelation().executeAsList()
        
        results.groupBy { it.movie_id }.map { (movieId, rows) ->
            val firstRow = rows.first()
            val movie = Movie(
                id = firstRow.movie_id,
                title = firstRow.movie_title,
                releaseYear = firstRow.movie_releaseYear.toInt(),
                directorId = firstRow.movie_directorId
            )
            val director = Director(
                id = firstRow.director_id,
                name = firstRow.director_name,
                birthDate = kotlinx.datetime.LocalDate.parse(firstRow.director_birthDate)
            )
            
            // Collect unique actors
            val actors = rows.mapNotNull { row ->
                if (row.actor_id != null) {
                    Actor(
                        id = row.actor_id,
                        name = row.actor_name!!,
                        birthYear = row.actor_birthYear!!.toInt()
                    )
                } else null
            }.distinctBy { it.id }
            
            // Collect unique reviews
            val reviews = rows.mapNotNull { row ->
                if (row.review_id != null) {
                    Review(
                        id = row.review_id,
                        movieId = row.review_movieId!!,
                        rating = row.review_rating!!.toInt(),
                        comment = row.review_comment!!
                    )
                } else null
            }.distinctBy { it.id }
            
            MovieWithDetailsRelation(
                movie = movie,
                director = director,
                actors = actors,
                reviews = reviews
            )
        }
    }
    
    /**
     * Fetches a single movie with complete details using joins.
     */
    override suspend fun getMovieWithDetailsRelation(movieId: Long): MovieWithDetailsRelation? = withContext(Dispatchers.IO) {
        val results = database.movieDetailsRelationsQueries.getMovieWithDetailsRelation(movieId).executeAsList()
        
        if (results.isEmpty()) return@withContext null
        
        val firstRow = results.first()
        val movie = Movie(
            id = firstRow.movie_id,
            title = firstRow.movie_title,
            releaseYear = firstRow.movie_releaseYear.toInt(),
            directorId = firstRow.movie_directorId
        )
        val director = Director(
            id = firstRow.director_id,
            name = firstRow.director_name,
            birthDate = kotlinx.datetime.LocalDate.parse(firstRow.director_birthDate)
        )
        
        // Collect unique actors
        val actors = results.mapNotNull { row ->
            if (row.actor_id != null) {
                Actor(
                    id = row.actor_id,
                    name = row.actor_name!!,
                    birthYear = row.actor_birthYear!!.toInt()
                )
            } else null
        }.distinctBy { it.id }
        
        // Collect unique reviews
        val reviews = results.mapNotNull { row ->
            if (row.review_id != null) {
                Review(
                    id = row.review_id,
                    movieId = row.review_movieId!!,
                    rating = row.review_rating!!.toInt(),
                    comment = row.review_comment!!
                )
            } else null
        }.distinctBy { it.id }
        
        MovieWithDetailsRelation(
            movie = movie,
            director = director,
            actors = actors,
            reviews = reviews
        )
    }
}

