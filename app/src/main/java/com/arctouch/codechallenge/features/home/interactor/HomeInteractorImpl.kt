package com.arctouch.codechallenge.features.home.interactor

import android.support.v7.widget.SearchView
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.application.CodeChallengeApplication
import com.arctouch.codechallenge.common.utils.RxSearchView
import com.arctouch.codechallenge.common.utils.getString
import com.arctouch.codechallenge.features.home.model.Genre
import com.arctouch.codechallenge.features.home.model.HomeService
import com.arctouch.codechallenge.features.home.model.exception.MoviesException
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by Joao on 25/03/2018.
 */
open class HomeInteractorImpl: HomeInteractor {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var loadedMovies = false

    override fun getUpcomingMovies(currentPage: Long, listener: HomeInteractor.UpcomingMoviesListener, refresh: Boolean) {

        val appInstance = CodeChallengeApplication.instance
        val fromCache = Observable.fromCallable { appInstance.genres }
        val fromServer = Observable.fromCallable { HomeService.getGenres() }

        compositeDisposable.add(Observable.concat(fromCache, fromServer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = {
                    if (it.isNotEmpty() && (!loadedMovies || refresh)) {
                        getMovies(it, currentPage, listener)
                        loadedMovies = true
                        return@subscribeBy
                    }
                }, onError = {
                    when (it){
                        is MoviesException -> {
                            listener.onUpcomingMoviesError(it.code, it.msg)
                        }
                        else -> listener.onUpcomingMoviesError(1, getString(R.string.error_empity_movies))
                    }
                }))
    }

    override fun onSearchMovie(searchView: SearchView, listener: HomeInteractor.UpcomingMoviesListener) {
        compositeDisposable.add(RxSearchView.fromSearchView(searchView)
                .debounce(400L, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = {
                    if (it.isEmpty()) {
                        listener.onSearchSucess(mutableListOf())
                    }
                    searchFromServer(it, listener)
                }))
    }

    override fun searchFromServer(query: String, listener: HomeInteractor.UpcomingMoviesListener) {
        compositeDisposable.add(Observable.fromCallable { HomeService.searchMovie(query) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = {
                    listener.onSearchSucess(it)
                }, onError = {
                    listener.onSearchEmpity()
                }))
    }

    private fun getMovies(genres: List<Genre>, page: Long, listener: HomeInteractor.UpcomingMoviesListener, loadMore: Boolean = false) {
        compositeDisposable.add(Observable.fromCallable { HomeService.upcomingMovies(genres, page) }
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

    override fun loadMore(listener: HomeInteractor.UpcomingMoviesListener, currentPage: Long) {
        val genres = CodeChallengeApplication.instance.genres
        getMovies(genres, currentPage, listener, true)
    }

    override fun onDetach() {
        compositeDisposable.dispose()
    }


}