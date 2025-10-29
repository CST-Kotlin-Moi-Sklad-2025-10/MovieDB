package io.github.ajiekcx.moviedb.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

actual object DatabaseBuilder {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = MovieDatabase.Schema,
            name = "movie.db"
        )
    }
}

