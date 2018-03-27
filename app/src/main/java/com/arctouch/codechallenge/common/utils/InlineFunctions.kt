package com.arctouch.codechallenge.common.utils

import android.content.Context
import android.net.ConnectivityManager
import com.arctouch.codechallenge.application.CodeChallengeApplication

/**
 * Created by João Goulart on 26/03/18.
 */

@Suppress("NOTHING_TO_INLINE")
inline fun getString(resourceId: Int): String {
    return CodeChallengeApplication.instance.baseContext.getString(resourceId)
}

fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeInfo = connectivityManager.activeNetworkInfo
    return activeInfo.isConnected
}