package com.arctouch.codechallenge.features.home.presenter

import android.arch.lifecycle.LifecycleOwner
import com.arctouch.codechallenge.common.constants.ApiConstants
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.features.home.activity.ViewCallback
import com.arctouch.codechallenge.features.home.interactor.HomeInteractor
import com.arctouch.codechallenge.features.home.interactor.HomeInteractorImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Joao on 25/03/2018.
 */
class HomePresenterImpl(val viewCallback: ViewCallback, val interactor: HomeInteractor = HomeInteractorImpl(viewCallback as LifecycleOwner)): HomePresenter {


    override fun onCreate() {
        viewCallback.setUpRecycler()
        interactor.getUpcomingMovies(1, ApiConstants.DEFAULT_REGION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val moviesWithGenres = it.results.map { movie ->
                        movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
                    }
                    viewCallback.hideProgress()
                    viewCallback.populateRecycler(moviesWithGenres)
                }
    }
}