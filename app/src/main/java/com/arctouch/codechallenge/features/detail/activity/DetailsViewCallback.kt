package com.arctouch.codechallenge.features.detail.activity

import com.arctouch.codechallenge.features.home.model.Movie

/**
 * Created by Joao on 27/03/2018.
 */
interface DetailsViewCallback {
    fun setMovieInfo(movie: Movie)
}