package com.arctouch.codechallenge.features.home.interactor

import android.arch.lifecycle.LifecycleOwner
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.common.utils.getString
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.features.home.service.http.HomeHttpService
import com.arctouch.codechallenge.model.UpcomingMoviesResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Joao on 25/03/2018.
 */
class HomeInteractorImpl: HomeInteractor {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun getUpcomingMovies(page: Long, defaultLanguage: String, listener: HomeInteractor.UpcomingMoviesListener) {
        if (defaultLanguage.isEmpty()) {
            listener.onUpcomingMoviesError(msg = getString(R.string.error_language))
            return
        }

        compositeDisposable.add(HomeHttpService.upcomingMovies(page, defaultLanguage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val moviesWithGenres = it.results.map { movie ->
                        movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
                    }

                    if (moviesWithGenres.isEmpty()) {
                        listener.onUpcomingMoviesError(1, getString(R.string.error_empity_movies))
                        return@subscribe
                    }

                    listener.onUpcomingMoviesSuccess(moviesWithGenres)
                })
    }

    override fun onDetach() {
        compositeDisposable.dispose()
    }


}