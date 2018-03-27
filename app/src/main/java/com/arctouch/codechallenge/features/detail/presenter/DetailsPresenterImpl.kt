package com.arctouch.codechallenge.features.detail.presenter

import android.content.Intent
import com.arctouch.codechallenge.common.constants.BundleConstants
import com.arctouch.codechallenge.features.detail.activity.DetailsViewCallback
import com.arctouch.codechallenge.features.home.model.Movie

/**
 * Created by Joao on 27/03/2018.
 */
class DetailsPresenterImpl(val viewCallback: DetailsViewCallback): DetailsPresenter {

    private lateinit var movie: Movie

    override fun onCreate(extras: Intent) {
        movie = extras.getParcelableExtra(BundleConstants.MOVIE_KEY)
        viewCallback.setMovieInfo(movie)
    }

}