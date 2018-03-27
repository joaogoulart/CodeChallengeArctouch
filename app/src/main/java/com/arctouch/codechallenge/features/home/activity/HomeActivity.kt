package com.arctouch.codechallenge.features.home.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.common.constants.BundleConstants
import com.arctouch.codechallenge.features.detail.activity.DetailsActivity
import com.arctouch.codechallenge.features.home.adapter.HomeAdapter
import com.arctouch.codechallenge.features.home.model.Movie
import com.arctouch.codechallenge.features.home.presenter.HomePresenterImpl
import kotlinx.android.synthetic.main.home_activity.*
import kotlinx.android.synthetic.main.include_toolbar.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.okButton
import org.jetbrains.anko.toast

class HomeActivity : AppCompatActivity(), HomeViewCallback {

    private val homePresenter by lazy { HomePresenterImpl(this) }
    private var adapter: HomeAdapter? = null
    private var currentLoadMoreProgress: ProgressBar? = null
    private var searchView: SearchView? = null
    private val linearLayoutManager: LinearLayoutManager by lazy { LinearLayoutManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        setSupportActionBar(toolbar)
        swipeRefreshLayout.setOnRefreshListener {
            adapter?.isMoreDataAvailable = true
            homePresenter.onRefresh()
        }
        homePresenter.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        val findFirstCompletelyVisibleItemPosition = if (searchView?.isActivated == true) 0 else linearLayoutManager.findFirstCompletelyVisibleItemPosition()
        outState?.putInt(BundleConstants.VISIBLE_ITEM_POSITION, findFirstCompletelyVisibleItemPosition)
        homePresenter.onSaveInstanceState(outState)
    }

    override fun setUpRecycler() {
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = linearLayoutManager
    }

    override fun scrollToPosition(firstItemVisiblePosition: Int) {
        recyclerView.scrollToPosition(firstItemVisiblePosition)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)

        val menuSearch = menu?.findItem(R.id.search)
        val searchView = menuSearch?.actionView as SearchView
        homePresenter.onSearchMovie(searchView)

        return true
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    override fun updateRecycler(moviesWithGenres: MutableList<Movie>) {
        if (adapter == null) {
            adapter = HomeAdapter(moviesWithGenres, {
                currentLoadMoreProgress = it
                homePresenter.loadMore()
            }, { movie, image ->
                onClickItem(movie, image)
            })
            recyclerView.adapter = adapter
        } else {
            currentLoadMoreProgress?.visibility = View.GONE
            swipeRefreshLayout.isRefreshing = false
            adapter?.handleItens(moviesWithGenres)
        }
        recyclerView.visibility = View.VISIBLE
    }

    private fun onClickItem(movie: Movie, image: ImageView) {
        val intent = Intent(this, DetailsActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable(BundleConstants.MOVIE_KEY, movie)
        intent.putExtras(bundle)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, image, getString(R.string.transition_image))
        startActivity(intent, options.toBundle())
    }

    override fun allMoviesLoaded() {
        currentLoadMoreProgress?.visibility = View.GONE
        adapter?.isMoreDataAvailable = false
        toast(R.string.all_movies_loaded)
    }

    override fun showEmpitySearch(msg: String, callback: () -> Unit) {
        alert {
            title = "Alert"
            message = msg
            okButton { callback() }
        }.show()
    }

    override fun setAdapterAbleToPaginate(ableToPaginate: Boolean) {
        adapter?.isMoreDataAvailable = ableToPaginate
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

    override fun showErrorWithCallback(msg: String) {
        alert {
            titleResource = R.string.error
            message = msg
            okButton { homePresenter.loadMovies() }
            isCancelable = false
        }.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        homePresenter.onDetach()
    }
}
