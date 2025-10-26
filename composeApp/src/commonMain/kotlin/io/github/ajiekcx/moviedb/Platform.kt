package io.github.ajiekcx.moviedb

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform