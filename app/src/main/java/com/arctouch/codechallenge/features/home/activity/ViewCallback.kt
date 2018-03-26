package com.arctouch.codechallenge.features.home.activity

import com.arctouch.codechallenge.model.Movie

interface ViewCallback {
    fun setUpRecycler()
    fun hideProgress()
    fun populateRecycler(moviesWithGenres: List<Movie>)
    fun showNoMovies()
    fun showError(msg: String)
    fun hideNoMovies()
}