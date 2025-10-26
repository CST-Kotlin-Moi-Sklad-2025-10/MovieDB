package io.github.ajiekcx.moviedb.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "movies",
    foreignKeys = [
        ForeignKey(
            entity = Director::class,
            parentColumns = ["id"],
            childColumns = ["directorId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("directorId")]
)
data class Movie(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val releaseYear: Int,
    val directorId: Long
)

