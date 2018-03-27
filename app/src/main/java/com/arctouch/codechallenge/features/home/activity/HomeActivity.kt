package com.arctouch.codechallenge.features.home.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.view.View
import android.widget.ProgressBar
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.features.home.adapter.HomeAdapter
import com.arctouch.codechallenge.features.home.model.Movie
import com.arctouch.codechallenge.features.home.presenter.HomePresenterImpl
import kotlinx.android.synthetic.main.home_activity.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.okButton
import org.jetbrains.anko.toast

class HomeActivity : AppCompatActivity(), ViewCallback {

    private val homePresenter by lazy { HomePresenterImpl(this) }
    private var adapter: HomeAdapter? = null
    private lateinit var currentLoadMoreProgress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        homePresenter.onCreate()
    }

    override fun setUpRecycler() {
        recyclerView.itemAnimator = DefaultItemAnimator()
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    override fun updateRecycler(moviesWithGenres: MutableList<Movie>) {
        if (adapter == null) {
            adapter = HomeAdapter(moviesWithGenres, {
                currentLoadMoreProgress = it
                homePresenter.loadMore()
            }, {
                onClickItem(it)
            })
            recyclerView.adapter = adapter
        } else {
            currentLoadMoreProgress.visibility = View.GONE
            adapter?.handleItens(moviesWithGenres)
        }
        recyclerView.visibility = View.VISIBLE
    }

    override fun allMoviesLoaded() {
        currentLoadMoreProgress.visibility = View.GONE
        adapter?.isMoreDataAvailable = false
        toast(R.string.all_movies_loaded)
    }

    private fun onClickItem(movie: Movie) {

    }

    override fun showNoMovies() {
        tNoMovies.visibility = View.VISIBLE
    }

    override fun hideNoMovies() {
        tNoMovies.visibility = View.GONE
    }

    override fun hideRecycler() {
        recyclerView.visibility = View.GONE
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
