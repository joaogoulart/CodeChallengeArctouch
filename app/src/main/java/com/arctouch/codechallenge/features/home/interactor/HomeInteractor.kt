package com.arctouch.codechallenge.features.home.interactor

import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.model.UpcomingMoviesResponse
import io.reactivex.Observable

/**
 * Created by Joao on 25/03/2018.
 */
interface HomeInteractor {

    interface UpcomingMoviesListener {
        fun onUpcomingMoviesError(code: Int = 0, msg: String)
        fun onUpcomingMoviesSuccess(moviesWithGenres: List<Movie>)
    }

    fun getUpcomingMovies(page: Long, defaultLanguage: String, listener: UpcomingMoviesListener)
    fun onDetach()

}