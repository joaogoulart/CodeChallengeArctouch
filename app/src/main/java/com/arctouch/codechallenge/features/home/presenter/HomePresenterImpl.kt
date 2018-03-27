package com.arctouch.codechallenge.features.home.presenter

import android.os.Bundle
import android.support.v7.widget.SearchView
import android.widget.ImageView
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.common.constants.BundleConstants
import com.arctouch.codechallenge.common.utils.getString
import com.arctouch.codechallenge.features.home.activity.HomeViewCallback
import com.arctouch.codechallenge.features.home.interactor.HomeInteractor
import com.arctouch.codechallenge.features.home.interactor.HomeInteractorImpl
import com.arctouch.codechallenge.features.home.model.Movie

@Suppress("UNCHECKED_CAST")
/**
 * Created by Joao on 25/03/2018.
 */
open class HomePresenterImpl(private val homeViewCallback: HomeViewCallback, private val interactor: HomeInteractor = HomeInteractorImpl()): HomePresenter, HomeInteractor.UpcomingMoviesListener {

    private var movieList: MutableList<Movie> = mutableListOf()
    private var currentPage = 1L

    override fun onCreate(savedInstanceState: Bundle?) {
        homeViewCallback.setUpRecycler()
        if (savedInstanceState != null) {
            val movieList = (savedInstanceState.getSerializable(BundleConstants.MOVIES_KEY) as? Array<Movie>)?.toMutableList() ?: mutableListOf()
            if (movieList.isNotEmpty()) {
                currentPage = savedInstanceState.getLong(BundleConstants.CURRENT_PAGE_KEY)
                val firstItemVisiblePosition = savedInstanceState.getInt(BundleConstants.VISIBLE_ITEM_POSITION)
                onUpcomingMoviesSuccess(movieList)
                homeViewCallback.scrollToPosition(firstItemVisiblePosition)
                return
            }
        }
        loadMovies()
    }

    override fun onSearchMovie(searchView: SearchView) {
        interactor.onSearchMovie(searchView, this)
    }

    override fun onSearchSucess(listmoviesSearch: MutableList<Movie>) {
        homeViewCallback.setAdapterAbleToPaginate(false)
        homeViewCallback.updateRecycler(if (listmoviesSearch.isNotEmpty()) listmoviesSearch else {
            homeViewCallback.setAdapterAbleToPaginate(true)
            movieList
        })
    }

    override fun onSearchEmpity() {
        homeViewCallback.showEmpitySearch(getString(R.string.no_movies_found), {
            homeViewCallback.updateRecycler(movieList)
            homeViewCallback.setAdapterAbleToPaginate(true)
        })
    }

    override fun onClickItem(movie: Movie, image: ImageView) {
        homeViewCallback.showDetailsActivity(movie, image)
    }

    override fun onRefresh() {
        currentPage = 1L
        movieList = mutableListOf()
        loadMovies(true)
    }

    fun loadMovies(refresh: Boolean = false) {
        interactor.getUpcomingMovies(currentPage, this, refresh)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putSerializable(BundleConstants.MOVIES_KEY, movieList.toTypedArray())
        outState?.putLong(BundleConstants.CURRENT_PAGE_KEY, currentPage)
    }

    override fun onUpcomingMoviesError(code: Int, msg: String) {
        homeViewCallback.hideProgress()
        if (code == 1) {
            homeViewCallback.hideRecycler()
            homeViewCallback.showNoMovies()
        } else if (code == 2) {
            homeViewCallback.showErrorWithCallback(msg)
        }

        homeViewCallback.showError(msg)
    }

    override fun onUpcomingMoviesSuccess(moviesWithGenres: MutableList<Movie>) {
        homeViewCallback.hideProgress()
        homeViewCallback.hideNoMovies()
        updateMoviesList(moviesWithGenres)
    }

    override fun loadMore() {
        currentPage++
        interactor.loadMore(this, currentPage)
    }

    override fun onDetach() {
        interactor.onDetach()
    }

    override fun onLoadMoreMovies(moviesWithGenres: MutableList<Movie>) {
        if (moviesWithGenres.isEmpty()) {
            homeViewCallback.allMoviesLoaded()
            return
        }

        updateMoviesList(moviesWithGenres, true)
    }

    override fun updateMoviesList(moviesWithGenres: MutableList<Movie>, fromLoadMore:Boolean) {
        movieList.addAll(moviesWithGenres)
        homeViewCallback.updateRecycler(if (!fromLoadMore) moviesWithGenres else movieList)
    }
}