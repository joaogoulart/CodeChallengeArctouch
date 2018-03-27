package com.arctouch.codechallenge.features.home.model

import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.application.CodeChallengeApplication
import com.arctouch.codechallenge.common.utils.getString
import com.arctouch.codechallenge.features.home.model.exception.MoviesException
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Joao on 25/03/2018.
 */
class HomeService {

    interface TmdbApi {

        @GET("genre/movie/list")
        fun genres(): Call<GenreResponse>

        @GET("movie/upcoming")
        fun upcomingMovies(@Query("page") page: Long
//                           @Query("region") region: String
        ): Call<UpcomingMoviesResponse>

        @GET("search/movie")
        fun searchMovie(@Query("query") query: String): Call<UpcomingMoviesResponse>

        @GET("movie/{id}")
        fun movie(
                @Path("id") id: Long
        ): Observable<Movie>
    }


    companion object {

        fun getGenres(): List<Genre> {
            val execute = CodeChallengeApplication.instance.api.genres().execute()
            val genreResponse = execute.body()
            genreResponse?.let {
                CodeChallengeApplication.instance.genres = it.genres
                return it.genres
            }
            throw MoviesException(2, getString(R.string.error_empity_genres))
        }

        @Throws
        fun upcomingMovies(genres: List<Genre>, page: Long): MutableList<Movie> {
            val execute = CodeChallengeApplication.instance.api.upcomingMovies(page).execute()
            val upcomingMoviesResponse = execute.body()
            upcomingMoviesResponse?.let {
                val moviesWithGenres = it.results.map { movie ->
                    movie.copy(genres = genres.filter { movie.genreIds?.contains(it.id) == true })
                }

                if (moviesWithGenres.isEmpty()) {
                    throw MoviesException(1, getString(R.string.error_empity_movies))
                }
                return moviesWithGenres.toMutableList()
            }
            throw MoviesException(1, getString(R.string.error_empity_movies))
        }

        @Throws
        fun searchMovie(query: String): MutableList<Movie> {
            val execute = CodeChallengeApplication.instance.api.searchMovie(query).execute()
            val upcomingMoviesResponse = execute.body()
            upcomingMoviesResponse?.let {
                if (it.results.isEmpty()) {
                    throw MoviesException(3, getString(R.string.error_empity_movies))
                }
                val moviesWithGenres = it.results.map { movie ->
                    movie.copy(genres = CodeChallengeApplication.instance.genres.filter { movie.genreIds?.contains(it.id) == true })
                }

                return moviesWithGenres.toMutableList()
            }

            return mutableListOf()
        }
    }
}