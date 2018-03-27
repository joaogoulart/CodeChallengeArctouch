package com.arctouch.codechallenge.features.home.interactor

import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.application.CodeChallengeApplication
import com.arctouch.codechallenge.common.constants.ApiConstants
import com.arctouch.codechallenge.common.utils.getString
import com.arctouch.codechallenge.features.home.model.Genre
import com.arctouch.codechallenge.features.home.model.HomeService
import com.arctouch.codechallenge.features.home.model.exception.MoviesException
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

/**
 * Created by Joao on 25/03/2018.
 */
class HomeInteractorImpl: HomeInteractor {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var loadedMovies = false

    override fun getUpcomingMovies(currentPage: Long, defaultLanguage: String, listener: HomeInteractor.UpcomingMoviesListener) {
        if (defaultLanguage.isEmpty()) {
            listener.onUpcomingMoviesError(msg = getString(R.string.error_language))
            return
        }

        val appInstance = CodeChallengeApplication.instance
        val fromCache = Observable.fromCallable { appInstance.genres }
        val fromServer = Observable.fromCallable { HomeService.getGenres() }

        compositeDisposable.add(Observable.concat(fromCache, fromServer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = {
                    if (it.isNotEmpty() && !loadedMovies) {
                        getMovies(it, currentPage, defaultLanguage, listener)
                        loadedMovies = true
                        return@subscribeBy
                    }
                }, onError = {
                    when (it){
                        is MoviesException -> {
                            listener.onUpcomingMoviesError(it.code, it.msg)
                        }
                        else -> listener.onUpcomingMoviesError(-1, getString(R.string.error_empity_movies))
                    }
                }))
    }

    private fun getMovies(genres: List<Genre>, page: Long, defaultLanguage: String, listener: HomeInteractor.UpcomingMoviesListener, loadMore: Boolean = false) {
        compositeDisposable.add(Observable.fromCallable { HomeService.upcomingMovies(genres, page, defaultLanguage) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = {
                    if (loadMore) {
                        listener.onLoadMoreMovies(it)
                    } else {
                        listener.onUpcomingMoviesSuccess(it)
                    }
                }, onError = {
                    when (it) {
                        is MoviesException -> {
                            if (page == 1L) {
                                listener.onUpcomingMoviesError(it.code, it.msg)
                            } else {
                                listener.onLoadMoreMovies(mutableListOf())
                            }
                        }
                    }
                }))
    }

    override fun loadMore(listener: HomeInteractor.UpcomingMoviesListener, defaultRegion: String, currentPage: Long) {
        val genres = CodeChallengeApplication.instance.genres
        getMovies(genres, currentPage, ApiConstants.DEFAULT_REGION, listener, true)
    }

    override fun onDetach() {
        compositeDisposable.dispose()
    }


}