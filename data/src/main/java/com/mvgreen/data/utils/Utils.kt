package com.mvgreen.data.utils

import android.content.Context
import android.content.SharedPreferences
import com.mvgreen.data.exception.UnexpectedResponseException

fun getPrefs(context: Context, fileName: String): SharedPreferences {
    return context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
}

fun <T> getOrUnexpected(item: T?) : T {
    return item ?: throw UnexpectedResponseException()
}