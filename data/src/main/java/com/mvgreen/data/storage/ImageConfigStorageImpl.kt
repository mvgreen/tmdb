package com.mvgreen.data.storage

import android.content.Context
import com.mvgreen.data.exception.StorageException
import com.mvgreen.data.storage.StorageKeys.IMAGE_PREFERENCES
import com.mvgreen.data.storage.StorageKeys.KEY_IMAGE_SERVICE_PATH
import com.mvgreen.data.storage.StorageKeys.KEY_IMAGE_SERVICE_URL
import com.mvgreen.data.utils.getPrefs
import com.mvgreen.domain.entity.ImageServiceConfiguration
import com.mvgreen.domain.repository.ImageConfigStorage
import javax.inject.Inject

// TODO добавить в даггер
class ImageConfigStorageImpl @Inject constructor(
    private val context: Context
) : ImageConfigStorage {

    override fun setConfiguration(configuration: ImageServiceConfiguration) {
        with(writePrefs(IMAGE_PREFERENCES)) {
            putString(KEY_IMAGE_SERVICE_URL, configuration.url)
            putString(KEY_IMAGE_SERVICE_PATH, configuration.path)
            commit()
        }
    }

    override fun getUrl(): String {
        with(readPrefs(IMAGE_PREFERENCES)) {
            return getString(KEY_IMAGE_SERVICE_URL, null) ?: throw StorageException()
        }
    }

    override fun getPath(): String {
        with(readPrefs(IMAGE_PREFERENCES)) {
            return getString(KEY_IMAGE_SERVICE_PATH, null) ?: throw StorageException()
        }
    }

    private fun readPrefs(filename: String) = getPrefs(context, filename)

    private fun writePrefs(filename: String) = getPrefs(context, filename).edit()

}