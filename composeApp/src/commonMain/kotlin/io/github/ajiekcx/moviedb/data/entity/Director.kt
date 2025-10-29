package io.github.ajiekcx.moviedb.data.entity

import kotlinx.datetime.LocalDate

data class Director(
    val id: Long = 0,
    val name: String,
    val birthDate: LocalDate
)

