package io.github.ajiekcx.moviedb.data.repository

import io.github.ajiekcx.moviedb.data.MovieDatabase
import io.github.ajiekcx.moviedb.data.entity.Actor
import io.github.ajiekcx.moviedb.data.entity.Cast
import io.github.ajiekcx.moviedb.data.entity.Director
import io.github.ajiekcx.moviedb.data.entity.Movie
import io.github.ajiekcx.moviedb.data.entity.Review
import io.github.ajiekcx.moviedb.data.relations.MovieWithDetails

class MovieRepository(private val database: MovieDatabase) {
    
    suspend fun seedData() {
        // Check if data already exists
        val existingDirectors = database.directorDao().getAllDirectors()
        if (existingDirectors.isNotEmpty()) {
            return // Data already seeded
        }
        
        // Insert Directors
        val directors = listOf(
            Director(name = "Christopher Nolan", birthYear = 1970),
            Director(name = "Steven Spielberg", birthYear = 1946),
            Director(name = "Quentin Tarantino", birthYear = 1963)
        )
        val directorIds = database.directorDao().insertAll(directors)
        
        // Insert Movies
        val movies = listOf(
            Movie(title = "Inception", releaseYear = 2010, directorId = directorIds[0]),
            Movie(title = "Interstellar", releaseYear = 2014, directorId = directorIds[0]),
            Movie(title = "The Dark Knight", releaseYear = 2008, directorId = directorIds[0]),
            Movie(title = "Jurassic Park", releaseYear = 1993, directorId = directorIds[1]),
            Movie(title = "Schindler's List", releaseYear = 1993, directorId = directorIds[1]),
            Movie(title = "Pulp Fiction", releaseYear = 1994, directorId = directorIds[2]),
            Movie(title = "Django Unchained", releaseYear = 2012, directorId = directorIds[2]),
            Movie(title = "Kill Bill", releaseYear = 2003, directorId = directorIds[2])
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
        val actorIds = database.actorDao().insertAll(actors)
        
        // Insert Cast (Movie-Actor relationships)
        val castList = listOf(
            // Inception
            Cast(movieId = movieIds[0], actorId = actorIds[0]), // Leonardo
            Cast(movieId = movieIds[0], actorId = actorIds[1]), // Marion
            // Interstellar
            Cast(movieId = movieIds[1], actorId = actorIds[2]), // Matthew
            Cast(movieId = movieIds[1], actorId = actorIds[3]), // Anne
            // The Dark Knight
            Cast(movieId = movieIds[2], actorId = actorIds[4]), // Christian
            Cast(movieId = movieIds[2], actorId = actorIds[5]), // Heath
            // Jurassic Park
            Cast(movieId = movieIds[3], actorId = actorIds[6]), // Sam
            Cast(movieId = movieIds[3], actorId = actorIds[7]), // Laura
            // Schindler's List
            // (No actors from our list for this one)
            // Pulp Fiction
            Cast(movieId = movieIds[5], actorId = actorIds[8]), // John
            Cast(movieId = movieIds[5], actorId = actorIds[9]), // Uma
            // Django Unchained
            Cast(movieId = movieIds[6], actorId = actorIds[0]), // Leonardo
            Cast(movieId = movieIds[6], actorId = actorIds[10]), // Jamie
            // Kill Bill
            Cast(movieId = movieIds[7], actorId = actorIds[9])  // Uma
        )
        database.castDao().insertAll(castList)
        
        // Insert Reviews
        val reviews = listOf(
            Review(movieId = movieIds[0], rating = 5, comment = "Mind-bending masterpiece!"),
            Review(movieId = movieIds[0], rating = 5, comment = "Incredible visuals and story"),
            Review(movieId = movieIds[0], rating = 4, comment = "Complex but worth it"),
            Review(movieId = movieIds[1], rating = 5, comment = "Epic space adventure"),
            Review(movieId = movieIds[1], rating = 5, comment = "Emotionally powerful"),
            Review(movieId = movieIds[2], rating = 5, comment = "Best superhero movie ever!"),
            Review(movieId = movieIds[2], rating = 5, comment = "Heath Ledger is phenomenal"),
            Review(movieId = movieIds[3], rating = 5, comment = "Timeless classic"),
            Review(movieId = movieIds[3], rating = 4, comment = "The dinosaurs are amazing!"),
            Review(movieId = movieIds[4], rating = 5, comment = "A profound masterpiece"),
            Review(movieId = movieIds[5], rating = 5, comment = "Tarantino at his best"),
            Review(movieId = movieIds[5], rating = 5, comment = "Unforgettable dialogue"),
            Review(movieId = movieIds[6], rating = 4, comment = "Great performances"),
            Review(movieId = movieIds[6], rating = 5, comment = "Powerful and entertaining"),
            Review(movieId = movieIds[7], rating = 5, comment = "Stylish and action-packed")
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

