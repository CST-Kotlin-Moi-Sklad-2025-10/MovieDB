package io.github.ajiekcx.moviedb.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.ajiekcx.moviedb.data.di.AppDIContainer
import io.github.ajiekcx.moviedb.data.relations.MovieWithDetails
import io.github.ajiekcx.moviedb.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {
    // Inject the repository into constructor in real app
    private val repository: MovieRepository
        get() = AppDIContainer.repository

    private val _movies = MutableStateFlow<List<MovieWithDetails>>(emptyList())
    val movies: StateFlow<List<MovieWithDetails>> = _movies.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadMovies()
    }

    private fun loadMovies() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // Seed the database with sample data
                repository.seedData()
                // Load all movies with details
                _movies.value = repository.getAllMoviesWithDetails()
            } catch (e: Exception) {
                e.printStackTrace()
                _movies.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}

