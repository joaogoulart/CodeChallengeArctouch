package com.arctouch.codechallenge.features.home.interactor

import android.support.v7.widget.SearchView
import com.arctouch.codechallenge.features.home.model.Movie

/**
 * Created by Joao on 25/03/2018.
 */
interface HomeInteractor {

    interface UpcomingMoviesListener {
        fun onUpcomingMoviesError(code: Int = 0, msg: String = "")
        fun onUpcomingMoviesSuccess(moviesWithGenres: MutableList<Movie>)
        fun onLoadMoreMovies(moviesWithGenres: MutableList<Movie>)
        fun onSearchSucess(listmoviesSearch: MutableList<Movie>)
        fun onSearchEmpity()
    }

    fun getUpcomingMovies(currentPage: Long, defaultLanguage: String, listener: UpcomingMoviesListener, refresh: Boolean = false)
    fun onDetach()
    fun loadMore(listener: UpcomingMoviesListener, defaultRegion: String, currentPage: Long)
    fun onSearchMovie(searchView: SearchView, listener: HomeInteractor.UpcomingMoviesListener)
    fun searchFromServer(query: String, listener: HomeInteractor.UpcomingMoviesListener)

}