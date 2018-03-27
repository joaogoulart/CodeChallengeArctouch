package com.arctouch.codechallenge.features.home.presenter

import android.os.Bundle
import android.support.v7.widget.SearchView
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.common.constants.ApiConstants
import com.arctouch.codechallenge.common.constants.BundleConstants
import com.arctouch.codechallenge.common.utils.getString
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

    override fun onCreate(savedInstanceState: Bundle?) {
        viewCallback.setUpRecycler()
        if (savedInstanceState != null) {
            val movieList = (savedInstanceState.getSerializable(BundleConstants.MOVIES_KEY) as? Array<Movie>)?.toMutableList() ?: mutableListOf()
            if (movieList.isNotEmpty()) {
                currentPage = savedInstanceState.getLong(BundleConstants.CURRENT_PAGE_KEY)
                val firstItemVisiblePosition = savedInstanceState.getInt(BundleConstants.VISIBLE_ITEM_POSITION)
                onUpcomingMoviesSuccess(movieList)
                viewCallback.scrollToPosition(firstItemVisiblePosition)
                return
            }
        }
        loadMovies()
    }

    override fun onSearchMovie(searchView: SearchView) {
        interactor.onSearchMovie(searchView, this)
    }

    override fun onSearchSucess(listmoviesSearch: MutableList<Movie>) {
        viewCallback.setAdapterAbleToPaginate(false)
        viewCallback.updateRecycler(if (listmoviesSearch.isNotEmpty()) listmoviesSearch else {
            viewCallback.setAdapterAbleToPaginate(true)
            movieList
        })
    }

    override fun onSearchEmpity() {
        viewCallback.showEmpitySearch(getString(R.string.no_movies_found), {
            viewCallback.updateRecycler(movieList)
            viewCallback.setAdapterAbleToPaginate(true)
        })
    }

    override fun onRefresh() {
        currentPage = 1L
        movieList = mutableListOf()
        loadMovies(true)
    }

    fun loadMovies(refresh: Boolean = false) {
        interactor.getUpcomingMovies(currentPage, ApiConstants.DEFAULT_REGION, this, refresh)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putSerializable(BundleConstants.MOVIES_KEY, movieList.toTypedArray())
        outState?.putLong(BundleConstants.CURRENT_PAGE_KEY, currentPage)
    }

    override fun onUpcomingMoviesError(code: Int, msg: String) {
        viewCallback.hideProgress()
        if (code == 1) {
            viewCallback.hideRecycler()
            viewCallback.showNoMovies()
        } else if (code == 2) {
            viewCallback.showErrorWithCallback(msg)
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

        updateMoviesList(moviesWithGenres, true)
    }

    private fun updateMoviesList(moviesWithGenres: MutableList<Movie>, fromLoadMore:Boolean = false) {
        movieList.addAll(moviesWithGenres)
        viewCallback.updateRecycler(if (!fromLoadMore) moviesWithGenres else movieList)
    }
}