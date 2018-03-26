package com.arctouch.codechallenge.features.home.presenter

import android.arch.lifecycle.LifecycleOwner
import com.arctouch.codechallenge.common.constants.ApiConstants
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.features.home.activity.ViewCallback
import com.arctouch.codechallenge.features.home.interactor.HomeInteractor
import com.arctouch.codechallenge.features.home.interactor.HomeInteractorImpl
import com.arctouch.codechallenge.features.home.model.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Joao on 25/03/2018.
 */
class HomePresenterImpl(private val viewCallback: ViewCallback, private val interactor: HomeInteractor = HomeInteractorImpl()): HomePresenter, HomeInteractor.UpcomingMoviesListener {

    override fun onCreate() {
        viewCallback.setUpRecycler()
        interactor.getUpcomingMovies(1, ApiConstants.DEFAULT_REGION, this)
    }


    override fun onUpcomingMoviesError(code: Int, msg: String) {
        viewCallback.hideProgress()
        if (code == 1) {
            viewCallback.showNoMovies()
            return
        }

        viewCallback.showError(msg)
    }

    override fun onUpcomingMoviesSuccess(moviesWithGenres: List<Movie>) {
        viewCallback.hideProgress()
        viewCallback.hideNoMovies()
        viewCallback.populateRecycler(moviesWithGenres)
    }

    override fun onDetach() {
        interactor.onDetach()
    }
}