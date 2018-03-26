package com.arctouch.codechallenge.features.home.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.features.home.presenter.HomePresenterImpl
import com.arctouch.codechallenge.features.home.adapter.HomeAdapter
import com.arctouch.codechallenge.features.home.model.Movie
import kotlinx.android.synthetic.main.home_activity.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.okButton

class HomeActivity : AppCompatActivity(), ViewCallback {

    private val homePresenter by lazy { HomePresenterImpl(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        homePresenter.onCreate()
    }

    override fun setUpRecycler() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.itemAnimator = DefaultItemAnimator()
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    override fun populateRecycler(moviesWithGenres: List<Movie>) {
        recyclerView.adapter = HomeAdapter(moviesWithGenres)
    }

    override fun showNoMovies() {
        tNoMovies.visibility = View.VISIBLE
    }

    override fun hideNoMovies() {
        tNoMovies.visibility = View.GONE
    }

    override fun showError(msg: String) {
        alert {
            titleResource = R.string.error
            message = msg
            okButton { finish() }
            isCancelable = false
        }.show()
    }

    override fun onStop() {
        super.onStop()
        homePresenter.onDetach()
    }
}
