package com.mvgreen.data.storage

import android.content.Context
import com.mvgreen.data.exception.StorageException
import com.mvgreen.data.storage.StorageKeys.KEY_AVATAR_HASH
import com.mvgreen.data.storage.StorageKeys.KEY_LOGIN
import com.mvgreen.data.storage.StorageKeys.KEY_NAME
import com.mvgreen.data.storage.StorageKeys.KEY_PASSWORD
import com.mvgreen.data.storage.StorageKeys.KEY_SESSION_TOKEN
import com.mvgreen.data.storage.StorageKeys.PROFILE_PREFERENCES
import com.mvgreen.data.utils.getPrefs
import com.mvgreen.domain.entity.ProfileData
import com.mvgreen.domain.repository.UserDataStorage
import javax.inject.Inject

class UserDataStorageImpl @Inject constructor(private val context: Context) : UserDataStorage {

    override fun saveAuthData(sessionToken: String, email: String, password: String) {
        with(writePrefs(PROFILE_PREFERENCES)) {
            putString(KEY_SESSION_TOKEN, sessionToken)
            putString(KEY_LOGIN, email)
            putString(KEY_PASSWORD, password)
            commit()
        }
    }

    override fun getSessionToken(): String? {
        with(readPrefs(PROFILE_PREFERENCES)) {
            return getString(KEY_SESSION_TOKEN, null)
        }
    }

    override fun getCredentials(): Pair<String?, String?> {
        with(readPrefs(PROFILE_PREFERENCES)) {
            return Pair(
                getString(KEY_LOGIN, null),
                getString(KEY_PASSWORD, null)
            )
        }
    }

    override fun getProfileData(): ProfileData {
        with(readPrefs(PROFILE_PREFERENCES)) {
            return ProfileData(
                getString(KEY_AVATAR_HASH, null) ?: throw StorageException(),
                getString(KEY_NAME, null) ?: throw StorageException(),
                getString(KEY_LOGIN, null) ?: throw StorageException()
            )
        }
    }

    override fun hasUserData(): Boolean {
        with(readPrefs(PROFILE_PREFERENCES)) {
            val dataExists = contains(KEY_LOGIN) && contains(KEY_PASSWORD) &&
                    contains(KEY_SESSION_TOKEN) && contains(KEY_AVATAR_HASH) && contains(KEY_NAME)
            if (!dataExists) {
                clearData()
            }
            return dataExists
        }
    }

    override fun clearData() {
        with(writePrefs(PROFILE_PREFERENCES)) {
            clear()
            commit()
        }
    }

    override fun saveProfileData(profileData: ProfileData) {
        with(writePrefs(PROFILE_PREFERENCES)) {
            putString(KEY_AVATAR_HASH, profileData.avatarHash)
            putString(KEY_NAME, profileData.name)
            commit()
        }
    }

    private fun readPrefs(filename: String) = getPrefs(context, filename)

    private fun writePrefs(filename: String) = getPrefs(context, filename).edit()

}