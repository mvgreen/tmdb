package com.mvgreen.data.storage

import android.content.Context
import com.mvgreen.data.storage.StorageKeys.KEY_SESSION_TOKEN
import com.mvgreen.data.storage.StorageKeys.TOKEN_PREFERENCES
import com.mvgreen.data.utils.getPrefs
import com.mvgreen.domain.repository.TokenStorage
import javax.inject.Inject

class TokenStorageImpl @Inject constructor(private val context: Context) : TokenStorage {

    override fun saveSessionToken(sessionToken: String) {
        with(writePrefs(TOKEN_PREFERENCES)) {
            putString(KEY_SESSION_TOKEN, sessionToken)
        }
    }

    override fun getSessionToken(): String? {
        with(readPrefs(TOKEN_PREFERENCES)) {
            return getString(KEY_SESSION_TOKEN, null)
        }
    }

    private fun readPrefs(filename: String) = getPrefs(context, filename)

    private fun writePrefs(filename: String) = getPrefs(context, filename).edit()

}