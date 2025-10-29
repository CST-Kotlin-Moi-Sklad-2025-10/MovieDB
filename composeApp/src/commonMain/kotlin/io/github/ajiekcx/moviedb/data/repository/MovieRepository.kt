package io.github.ajiekcx.moviedb.data.repository

import io.github.ajiekcx.moviedb.data.MovieDatabase
import io.github.ajiekcx.moviedb.data.entity.Actor
import io.github.ajiekcx.moviedb.data.entity.Cast
import io.github.ajiekcx.moviedb.data.entity.Director
import io.github.ajiekcx.moviedb.data.entity.Movie
import io.github.ajiekcx.moviedb.data.entity.Review
import io.github.ajiekcx.moviedb.data.relations.MovieWithDetails
import kotlinx.datetime.LocalDate

class MovieRepository(private val database: MovieDatabase) {
    suspend fun seedData() {
        // Check if data already exists
        val existingDirectors = database.directorDao().getAllDirectors()
        if (existingDirectors.isNotEmpty()) {
            return // Data already seeded
        }

        // Insert Directors
        val directors = listOf(
            Director(name = "Christopher Nolan", birthDate = LocalDate(1970, 7, 30)),
            Director(name = "Steven Spielberg", birthDate = LocalDate(1946, 12, 18)),
            Director(name = "Quentin Tarantino", birthDate = LocalDate(1963, 3, 27))
        )

        // Insert Movies
        val movies = listOf(
            Movie(title = "Inception", releaseYear = 2010, directorId = directors[0].id),
            Movie(title = "Interstellar", releaseYear = 2014, directorId = directors[0].id),
            Movie(title = "The Dark Knight", releaseYear = 2008, directorId = directors[0].id),
            Movie(title = "Jurassic Park", releaseYear = 1993, directorId = directors[1].id),
            Movie(title = "Schindler's List", releaseYear = 1993, directorId = directors[1].id),
            Movie(title = "Pulp Fiction", releaseYear = 1994, directorId = directors[2].id),
            Movie(title = "Django Unchained", releaseYear = 2012, directorId = directors[2].id),
            Movie(title = "Kill Bill", releaseYear = 2003, directorId = directors[2].id)
        )
        val movieIds = database.movieDao().insertAll(movies)

        // Insert Actors
        val actors = listOf(
            Actor(name = "Leonardo DiCaprio", birthYear = 1974),
            Actor(name = "Marion Cotillard", birthYear = 1975),
            Actor(name = "Matthew McConaughey", birthYear = 1969),
            Actor(name = "Anne Hathaway", birthYear = 1982),
            Actor(name = "Christian Bale", birthYear = 1974),
            Actor(name = "Heath Ledger", birthYear = 1979),
            Actor(name = "Sam Neill", birthYear = 1947),
            Actor(name = "Laura Dern", birthYear = 1967),
            Actor(name = "John Travolta", birthYear = 1954),
            Actor(name = "Uma Thurman", birthYear = 1970),
            Actor(name = "Jamie Foxx", birthYear = 1967)
        )

        // Insert Cast (Movie-Actor relationships)
        val castList = listOf(
            // Inception
            Cast(movieId = movies[0].id, actorId = actors[0].id), // Leonardo
            Cast(movieId = movies[0].id, actorId = actors[1].id), // Marion
            // Interstellar
            Cast(movieId = movies[1].id, actorId = actors[2].id), // Matthew
            Cast(movieId = movies[1].id, actorId = actors[3].id), // Anne
            // The Dark Knight
            Cast(movieId = movies[2].id, actorId = actors[4].id), // Christian
            Cast(movieId = movies[2].id, actorId = actors[5].id), // Heath
            // Jurassic Park
            Cast(movieId = movies[3].id, actorId = actors[6].id), // Sam
            Cast(movieId = movies[3].id, actorId = actors[7].id), // Laura
            // Schindler's List
            // (No actors from our list for this one)
            // Pulp Fiction
            Cast(movieId = movies[5].id, actorId = actors[8].id), // John
            Cast(movieId = movies[5].id, actorId = actors[9].id), // Uma
            // Django Unchained
            Cast(movieId = movies[6].id, actorId = actors[0].id), // Leonardo
            Cast(movieId = movies[6].id, actorId = actors[10].id), // Jamie
            // Kill Bill
            Cast(movieId = movies[7].id, actorId = actors[9].id)  // Uma
        )
        database.castDao().insertAll(castList)

        // Insert Reviews
        val reviews = listOf(
            Review(movieId = movies[0].id, rating = 5, comment = "Mind-bending masterpiece!"),
            Review(movieId = movies[0].id, rating = 5, comment = "Incredible visuals and story"),
            Review(movieId = movies[0].id, rating = 4, comment = "Complex but worth it"),
            Review(movieId = movies[1].id, rating = 5, comment = "Epic space adventure"),
            Review(movieId = movies[1].id, rating = 5, comment = "Emotionally powerful"),
            Review(movieId = movies[2].id, rating = 5, comment = "Best superhero movie ever!"),
            Review(movieId = movies[2].id, rating = 5, comment = "Heath Ledger is phenomenal"),
            Review(movieId = movies[3].id, rating = 5, comment = "Timeless classic"),
            Review(movieId = movies[3].id, rating = 4, comment = "The dinosaurs are amazing!"),
            Review(movieId = movies[4].id, rating = 5, comment = "A profound masterpiece"),
            Review(movieId = movies[5].id, rating = 5, comment = "Tarantino at his best"),
            Review(movieId = movies[5].id, rating = 5, comment = "Unforgettable dialogue"),
            Review(movieId = movies[6].id, rating = 4, comment = "Great performances"),
            Review(movieId = movies[6].id, rating = 5, comment = "Powerful and entertaining"),
            Review(movieId = movies[7].id, rating = 5, comment = "Stylish and action-packed")
        )
        database.reviewDao().insertAll(reviews)
    }

    /**
     * Fetches all movies with their complete details using database joins.
     * This is optimized to use a single query with joins instead of N+1 queries.
     *
     * Before optimization: 1 query for movies + N queries for (director + actors + reviews)
     * After optimization: 1 query with joins for all data
     */
    suspend fun getAllMoviesWithDetails(): List<MovieWithDetails> {
        val moviesWithDetailsRelation = database.movieDao().getAllMoviesWithDetailsRelation()
        return moviesWithDetailsRelation.map { relation ->
            MovieWithDetails(
                movie = relation.movie,
                director = relation.director,
                actors = relation.actors,
                reviews = relation.reviews
            )
        }
    }

    /**
     * Fetches a single movie with complete details using database joins.
     * Optimized to use a single query with joins instead of multiple separate queries.
     */
    suspend fun getMovieWithDetails(movieId: Long): MovieWithDetails? {
        val relation = database.movieDao().getMovieWithDetailsRelation(movieId) ?: return null
        return MovieWithDetails(
            movie = relation.movie,
            director = relation.director,
            actors = relation.actors,
            reviews = relation.reviews
        )
    }
}