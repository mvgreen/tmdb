package com.mvgreen.tmdbapp.utils

import android.util.Patterns

fun emailValid(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}