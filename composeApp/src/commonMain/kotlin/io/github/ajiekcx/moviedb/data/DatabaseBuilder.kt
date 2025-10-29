package io.github.ajiekcx.moviedb.data

import app.cash.sqldelight.db.SqlDriver

expect object DatabaseBuilder {
    fun createDriver(): SqlDriver
}

