package com.arctouch.codechallenge.features.home.service.http

import com.arctouch.codechallenge.application.CodeChallengeApplication
import com.arctouch.codechallenge.features.home.model.GenreResponse
import com.arctouch.codechallenge.features.home.model.Movie
import com.arctouch.codechallenge.features.home.model.UpcomingMoviesResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by Joao on 25/03/2018.
 */
class HomeHttpService {

    interface TmdbApi {

        @GET("genre/movie/list")
        fun genres(): Observable<GenreResponse>

        @GET("movie/upcoming")
        fun upcomingMovies(@Query("page") page: Long,
                           @Query("region") region: String
        ): Observable<UpcomingMoviesResponse>

        @GET("movie/{id}")
        fun movie(
                @Path("id") id: Long
        ): Observable<Movie>
    }


    companion object {
        fun upcomingMovies(page: Long, defaultLanguage: String): Observable<UpcomingMoviesResponse> {
            return CodeChallengeApplication.instance.api.upcomingMovies(page, defaultLanguage)
        }
    }
}