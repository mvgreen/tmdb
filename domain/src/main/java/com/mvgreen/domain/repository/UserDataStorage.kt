package com.mvgreen.domain.repository

import com.mvgreen.domain.entity.ProfileData

interface UserDataStorage {

    fun saveAuthData(sessionToken: String, email: String, password: String)

    fun saveProfileData(profileData: ProfileData)

    fun getSessionToken(): String?

    fun getCredentials(): Pair<String?, String?>

    fun getProfileData(): ProfileData

    fun hasUserData(): Boolean

    fun clearData()

}