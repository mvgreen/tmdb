package com.mvgreen.data.storage

import android.content.Context
import com.mvgreen.data.storage.StorageKeys.APP_PREFERENCES
import com.mvgreen.data.storage.StorageKeys.KEY_LIST_MODE
import com.mvgreen.data.utils.getPrefs
import com.mvgreen.domain.repository.AppPreferencesStorage
import com.mvgreen.domain.repository.AppPreferencesStorage.Companion.LIST_MODE_GRID
import com.mvgreen.domain.repository.AppPreferencesStorage.Companion.LIST_MODE_LINEAR
import java.lang.IllegalArgumentException
import javax.inject.Inject

class AppPreferencesStorageImpl @Inject constructor(
    private val context: Context
): AppPreferencesStorage {

    override fun setListMode(listMode: Int) {
        if (listMode != LIST_MODE_LINEAR && listMode != LIST_MODE_GRID) {
            throw IllegalArgumentException()
        }
        with(writePrefs(APP_PREFERENCES)) {
            putInt(KEY_LIST_MODE, listMode)
            commit()
        }
    }

    override fun getListMode(): Int {
        with(readPrefs(APP_PREFERENCES)) {
            return getInt(KEY_LIST_MODE, LIST_MODE_LINEAR)
        }
    }


    private fun readPrefs(filename: String) = getPrefs(context, filename)

    private fun writePrefs(filename: String) = getPrefs(context, filename).edit()

}