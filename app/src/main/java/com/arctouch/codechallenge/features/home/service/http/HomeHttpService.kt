package com.arctouch.codechallenge.features.home.service.http

import com.arctouch.codechallenge.application.CodeChallengeApplication
import com.arctouch.codechallenge.model.UpcomingMoviesResponse
import io.reactivex.Observable

/**
 * Created by Joao on 25/03/2018.
 */
class HomeHttpService {
    companion object {
        fun upcomingMovies(page: Long, defaultLanguage: String): Observable<UpcomingMoviesResponse> {
            return CodeChallengeApplication.instance.api.upcomingMovies(page, defaultLanguage)
        }
    }
}