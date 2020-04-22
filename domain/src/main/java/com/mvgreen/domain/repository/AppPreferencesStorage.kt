package com.mvgreen.domain.repository

interface AppPreferencesStorage {

    fun setListMode(listMode: Int)

    fun getListMode(): Int

}