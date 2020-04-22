package com.mvgreen.domain.repository

interface AppPreferencesStorage {

    companion object {
        const val LIST_MODE_LINEAR = 0
        const val LIST_MODE_GRID = 1
    }

    fun setListMode(listMode: Int)

    fun getListMode(): Int

}