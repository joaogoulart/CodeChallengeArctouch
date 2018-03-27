package com.arctouch.codechallenge.features.home.interactor

import com.arctouch.codechallenge.features.home.model.Movie

/**
 * Created by Joao on 25/03/2018.
 */
interface HomeInteractor {

    interface UpcomingMoviesListener {
        fun onUpcomingMoviesError(code: Int = 0, msg: String)
        fun onUpcomingMoviesSuccess(moviesWithGenres: MutableList<Movie>)
        fun onLoadMoreMovies(moviesWithGenres: MutableList<Movie>)
    }

    fun getUpcomingMovies(currentPage: Long, defaultLanguage: String, listener: UpcomingMoviesListener)
    fun onDetach()
    fun loadMore(listener: UpcomingMoviesListener, defaultRegion: String, currentPage: Long)

}