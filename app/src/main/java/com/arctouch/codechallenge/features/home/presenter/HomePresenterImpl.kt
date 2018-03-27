package com.arctouch.codechallenge.features.home.presenter

import com.arctouch.codechallenge.common.constants.ApiConstants
import com.arctouch.codechallenge.features.home.activity.ViewCallback
import com.arctouch.codechallenge.features.home.interactor.HomeInteractor
import com.arctouch.codechallenge.features.home.interactor.HomeInteractorImpl
import com.arctouch.codechallenge.features.home.model.Movie

/**
 * Created by Joao on 25/03/2018.
 */
class HomePresenterImpl(private val viewCallback: ViewCallback, private val interactor: HomeInteractor = HomeInteractorImpl()): HomePresenter, HomeInteractor.UpcomingMoviesListener {

    private var movieList: MutableList<Movie> = mutableListOf()
    private var currentPage = 1L

    override fun onCreate() {
        viewCallback.setUpRecycler()
        interactor.getUpcomingMovies(currentPage, ApiConstants.DEFAULT_REGION, this)
    }

    override fun onUpcomingMoviesError(code: Int, msg: String) {
        viewCallback.hideProgress()
        if (code == 1) {
            viewCallback.hideRecycler()
            viewCallback.showNoMovies()
            return
        }

        viewCallback.showError(msg)
    }

    override fun onUpcomingMoviesSuccess(moviesWithGenres: MutableList<Movie>) {
        viewCallback.hideProgress()
        viewCallback.hideNoMovies()
        updateMoviesList(moviesWithGenres)
    }

    override fun loadMore() {
        currentPage++
        interactor.loadMore(this, ApiConstants.DEFAULT_REGION, currentPage)
    }

    override fun onDetach() {
        interactor.onDetach()
    }

    override fun onLoadMoreMovies(moviesWithGenres: MutableList<Movie>) {
        if (moviesWithGenres.isEmpty()) {
            viewCallback.allMoviesLoaded()
            return
        }

        updateMoviesList(moviesWithGenres)
    }

    private fun updateMoviesList(moviesWithGenres: MutableList<Movie>) {
        movieList.addAll(moviesWithGenres)
        viewCallback.updateRecycler(movieList)
    }
}