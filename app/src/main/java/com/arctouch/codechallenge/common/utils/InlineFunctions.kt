package com.arctouch.codechallenge.common.utils

import com.arctouch.codechallenge.application.CodeChallengeApplication

/**
 * Created by João Goulart on 26/03/18.
 */

inline fun getString(resourceId: Int): String {
    return CodeChallengeApplication.instance.baseContext.getString(resourceId)
}