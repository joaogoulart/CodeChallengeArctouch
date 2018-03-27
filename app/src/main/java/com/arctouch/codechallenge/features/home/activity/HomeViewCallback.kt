package com.arctouch.codechallenge.features.home.activity

import android.widget.ImageView
import com.arctouch.codechallenge.features.home.model.Movie

open interface HomeViewCallback {
    fun setUpRecycler()
    fun hideProgress()
    fun updateRecycler(moviesWithGenres: MutableList<Movie>)
    fun showNoMovies()
    fun showError(msg: String)
    fun hideNoMovies()
    fun hideRecycler()
    fun allMoviesLoaded()
    fun scrollToPosition(firstItemVisiblePosition: Int)
    fun showErrorWithCallback(msg: String)
    fun showEmpitySearch(msg: String, callback: () -> Unit)
    fun setAdapterAbleToPaginate(ableToPaginate: Boolean)
    fun showDetailsActivity(movie: Movie, image: ImageView)
}