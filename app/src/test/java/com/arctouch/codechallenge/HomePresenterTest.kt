package com.arctouch.codechallenge

import android.widget.ImageView
import com.arctouch.codechallenge.features.home.activity.HomeViewCallback
import com.arctouch.codechallenge.features.home.interactor.HomeInteractor
import com.arctouch.codechallenge.features.home.model.Movie
import com.arctouch.codechallenge.features.home.presenter.HomePresenterImpl
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by João Goulart on 27/03/18.
 */
@RunWith(MockitoJUnitRunner::class)
class HomePresenterTest {

    val homeView: HomeViewCallback = Mockito.mock(HomeViewCallback::class.java)
    val homeInteractor: HomeInteractor = Mockito.mock(HomeInteractor::class.java)

    private lateinit var presenter: HomePresenterImpl

    @Before
    fun setUp() {
        presenter = Mockito.spy(HomePresenterImpl(homeView, homeInteractor))
    }

    @Test
    fun checksIfLoadMovies() {
        presenter.onCreate(null)
        verify(homeView, times(1)).setUpRecycler()
        verify(homeInteractor, times(1)).getUpcomingMovies(1, presenter)
    }

    @Test
    fun checkIfLoadsMovies() {
        presenter.loadMovies()
        verify(homeInteractor, times(1)).getUpcomingMovies(1, presenter)
    }

    @Test
    fun loadedMoviesCorrectly() {
        val moviesWithGenres = mutableListOf<Movie>()
        presenter.onUpcomingMoviesSuccess(moviesWithGenres)
        verify(homeView, times(1)).hideProgress()
        verify(homeView, times(1)).hideNoMovies()
        verify(presenter).updateMoviesList(moviesWithGenres)
        verify(homeView, times(1)).updateRecycler(moviesWithGenres)
    }

    @Test
    fun refresh() {
        presenter.onRefresh()
        verify(homeInteractor, times(1)).getUpcomingMovies(1, presenter, true)
    }

    @Test
    fun onClickItem() {
        val movie = Movie(1, "Test", "teste", mutableListOf(), listOf(), "",
                "", "testeDate")
        val imageView = Mockito.mock(ImageView::class.java)
        presenter.onClickItem(movie, imageView)
        verify(homeView, times(1)).showDetailsActivity(movie, imageView)
    }

    @Test
    fun onSearchMovie() {
        val searchView = Mockito.mock(android.support.v7.widget.SearchView::class.java)
        presenter.onSearchMovie(searchView)
        verify(homeInteractor, times(1)).onSearchMovie(searchView, presenter)
    }

    @Test
    fun onDetach() {
        presenter.onDetach()
        verify(homeInteractor).onDetach()
    }


}