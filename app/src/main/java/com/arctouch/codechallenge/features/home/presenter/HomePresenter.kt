package com.arctouch.codechallenge.features.home.presenter

import android.os.Bundle
import android.support.v7.widget.SearchView
import android.widget.ImageView
import com.arctouch.codechallenge.features.home.model.Movie

/**
 * Created by Joao on 25/03/2018.
 */
interface HomePresenter {

    fun onCreate(savedInstanceState: Bundle?)
    fun onDetach()
    fun loadMore()
    fun onSaveInstanceState(outState: Bundle?)
    fun onRefresh()
    fun onSearchMovie(searchView: SearchView)
    fun updateMoviesList(moviesWithGenres: MutableList<Movie>, fromLoadMore:Boolean = false)
    fun onClickItem(movie: Movie, image: ImageView)
}