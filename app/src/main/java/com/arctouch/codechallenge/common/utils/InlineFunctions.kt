package com.arctouch.codechallenge.common.utils

import com.arctouch.codechallenge.application.CodeChallengeApplication

/**
 * Created by João Goulart on 26/03/18.
 */

@Suppress("NOTHING_TO_INLINE")
inline fun getString(resourceId: Int): String {
    return CodeChallengeApplication.instance.baseContext.getString(resourceId)
}