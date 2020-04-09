package com.mvgreen.data.storage

import android.content.Context
import com.mvgreen.data.storage.StorageKeys.AUTH_PREFERENCES
import com.mvgreen.data.storage.StorageKeys.KEY_LOGIN
import com.mvgreen.data.storage.StorageKeys.KEY_PASSWORD
import com.mvgreen.data.storage.StorageKeys.KEY_SESSION_TOKEN
import com.mvgreen.data.utils.getPrefs
import com.mvgreen.domain.repository.CredentialsStorage
import javax.inject.Inject

class CredentialsStorageImpl @Inject constructor(private val context: Context) : CredentialsStorage {

    override fun saveAuthData(sessionToken: String, email: String, password: String) {
        with(writePrefs(AUTH_PREFERENCES)) {
            putString(KEY_SESSION_TOKEN, sessionToken)
            putString(KEY_LOGIN, email)
            putString(KEY_PASSWORD, password)
            commit()
        }
    }

    override fun getSessionToken(): String? {
        with(readPrefs(AUTH_PREFERENCES)) {
            return getString(KEY_SESSION_TOKEN, null)
        }
    }

    override fun getCredentials(): Pair<String?, String?> {
        with(readPrefs(AUTH_PREFERENCES)) {
            return Pair(
                getString(KEY_LOGIN, null),
                getString(KEY_PASSWORD, null)
            )
        }
    }

    private fun readPrefs(filename: String) = getPrefs(context, filename)

    private fun writePrefs(filename: String) = getPrefs(context, filename).edit()

}