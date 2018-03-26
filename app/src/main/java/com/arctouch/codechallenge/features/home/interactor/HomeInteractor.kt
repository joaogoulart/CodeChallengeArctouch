package com.arctouch.codechallenge.features.home.interactor

import com.arctouch.codechallenge.model.UpcomingMoviesResponse
import io.reactivex.Observable

/**
 * Created by Joao on 25/03/2018.
 */
interface HomeInteractor {

    interface UpcomingMoviesListener {

    }

    fun getUpcomingMovies(page: Long, defaultLanguage: String): Observable<UpcomingMoviesResponse>

}