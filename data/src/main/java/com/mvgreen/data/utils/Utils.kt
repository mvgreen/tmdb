package com.mvgreen.data.utils

import android.content.Context
import android.content.SharedPreferences

fun getPrefs(context: Context, fileName: String): SharedPreferences {
    return context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
}