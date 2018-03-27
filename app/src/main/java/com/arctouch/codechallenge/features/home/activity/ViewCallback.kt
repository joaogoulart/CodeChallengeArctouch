package com.arctouch.codechallenge.features.home.activity

import com.arctouch.codechallenge.features.home.model.Movie

interface ViewCallback {
    fun setUpRecycler()
    fun hideProgress()
    fun updateRecycler(moviesWithGenres: MutableList<Movie>)
    fun showNoMovies()
    fun showError(msg: String)
    fun hideNoMovies()
    fun hideRecycler()
    fun allMoviesLoaded()
}