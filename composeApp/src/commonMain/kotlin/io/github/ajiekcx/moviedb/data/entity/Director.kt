package io.github.ajiekcx.moviedb.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "directors")
data class Director(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val birthYear: Int
)

