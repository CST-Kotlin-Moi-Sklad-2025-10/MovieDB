package io.github.ajiekcx.moviedb.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "actors")
data class Actor(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val birthYear: Int
)

