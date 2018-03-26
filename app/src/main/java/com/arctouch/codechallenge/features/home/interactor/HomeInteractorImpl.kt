package com.arctouch.codechallenge.features.home.interactor

import android.arch.lifecycle.LifecycleOwner
import com.arctouch.codechallenge.common.base.BaseInteractor
import com.arctouch.codechallenge.features.home.service.http.HomeHttpService
import com.arctouch.codechallenge.model.UpcomingMoviesResponse
import io.reactivex.Observable

/**
 * Created by Joao on 25/03/2018.
 */
class HomeInteractorImpl(lifecycleOwner: LifecycleOwner): BaseInteractor(lifecycleOwner), HomeInteractor {
    override fun getUpcomingMovies(page: Long, defaultLanguage: String): Observable<UpcomingMoviesResponse> {
        return HomeHttpService.upcomingMovies(page, defaultLanguage)
    }
}