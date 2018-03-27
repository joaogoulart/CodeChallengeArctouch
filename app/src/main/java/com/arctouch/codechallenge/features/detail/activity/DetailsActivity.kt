package com.arctouch.codechallenge.features.detail.activity

import android.os.Build
import android.os.Bundle
import android.support.transition.TransitionManager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.common.utils.MovieImageUrlBuilder
import com.arctouch.codechallenge.features.detail.presenter.DetailsPresenterImpl
import com.arctouch.codechallenge.features.home.model.Movie
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity(), DetailsViewCallback {

    private val detailsPresenter by lazy { DetailsPresenterImpl(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setUpToolbar()
        detailsPresenter.onCreate(intent)
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun setMovieInfo(movie: Movie) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(coordinator)
        }
        with(movie) {
            collapsingToolbar.title = title
            tSynopsis.text = overview
            tDateRelease.text = releaseDate
            tGenres.text = getGendersText(this)

        }

        Glide.with(this)
                .load(movie.backdropPath?.let { MovieImageUrlBuilder.buildBackdropUrl(it) })
                .into(imgBackdrop)
        Glide.with(this)
                .load(movie.posterPath?.let { MovieImageUrlBuilder.buildPosterUrl(it) })
                .into(posterImageView)
    }

    private fun getGendersText(movie: Movie): String {
        var genres = ""
        movie.genres?.forEachIndexed { index, genre ->
            genres += if (index == movie.genres.size - 1) {
                genre.name
            } else {
                "${genre.name}, "
            }
        }

        return genres
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            supportFinishAfterTransition()
        }
        return super.onOptionsItemSelected(item)
    }
}
