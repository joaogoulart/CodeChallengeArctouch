package com.arctouch.codechallenge.features.home.model.exception

/**
 * Created by Joao on 26/03/2018.
 */
class MoviesException(val code: Int, val msg: String): Exception(msg)