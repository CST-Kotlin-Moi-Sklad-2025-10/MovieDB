package io.github.ajiekcx.moviedb.data

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

actual object DatabaseBuilder {
    private lateinit var appContext: Context
    
    fun init(context: Context) {
        appContext = context.applicationContext
    }
    
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = MovieDatabase.Schema,
            context = appContext,
            name = "movie.db"
        )
    }
}

