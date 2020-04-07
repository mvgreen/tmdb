package com.mvgreen.data.storage

import android.content.Context
import android.content.SharedPreferences
import com.mvgreen.data.storage.StorageKeys.KEY_SESSION_TOKEN
import com.mvgreen.data.storage.StorageKeys.TOKEN_PREFERENCES
import com.mvgreen.data.utils.getPrefs
import javax.inject.Inject

class TokenStorage @Inject constructor(private val context: Context) {

    fun saveSessionToken(sessionToken: String) {
        with(writePrefs(TOKEN_PREFERENCES)) {
            putString(KEY_SESSION_TOKEN, sessionToken)
        }
    }

    fun getSessionToken(): String? {
        with(readPrefs(TOKEN_PREFERENCES)) {
            return getString(KEY_SESSION_TOKEN, null)
        }
    }

    private fun readPrefs(filename: String) = getPrefs(context, filename)

    private fun writePrefs(filename: String) = getPrefs(context, filename).edit()
}