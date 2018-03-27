package com.arctouch.codechallenge.common.utils

import com.arctouch.codechallenge.common.constants.ApiConstants


class MovieImageUrlBuilder {

    companion object {
        fun buildPosterUrl(posterPath: String): String {
            return ApiConstants.POSTER_API_URL + posterPath + "?api_key=" + ApiConstants.API_KEY
        }

        fun buildBackdropUrl(backdropPath: String): String {
            return ApiConstants.BACKDROP_API_URL + backdropPath + "?api_key=" + ApiConstants.API_KEY
        }
    }
}
