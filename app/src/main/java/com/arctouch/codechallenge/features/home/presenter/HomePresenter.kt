package com.arctouch.codechallenge.features.home.presenter

import android.os.Bundle
import android.support.v7.widget.SearchView

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
}